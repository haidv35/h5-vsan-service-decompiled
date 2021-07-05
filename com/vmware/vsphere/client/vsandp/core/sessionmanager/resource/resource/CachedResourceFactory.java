/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.EqualityLock;
/*     */ import java.io.Closeable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedResourceFactory<R extends Resource, S>
/*     */   implements ResourceFactory<R, S>
/*     */ {
/*     */   protected static final long GC_TIMEOUT = 30000L;
/*  47 */   private static final Logger logger = LoggerFactory.getLogger(CachedResourceFactory.class);
/*     */   
/*  49 */   protected final Map<S, CacheEntry<R>> cache = new HashMap<>();
/*     */   
/*     */   protected final ResourceFactory<R, S> factory;
/*     */   
/*     */   protected volatile boolean isShutdown = false;
/*     */   
/*  55 */   protected final EqualityLock locks = new EqualityLock();
/*     */   
/*     */   public CachedResourceFactory(ResourceFactory<R, S> factory) {
/*  58 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   
/*     */   public R acquire(final S settings) {
/*  63 */     CacheEntry<R> entry = null;
/*  64 */     Object entryLock = null;
/*     */     
/*  66 */     synchronized (this) {
/*  67 */       if (this.isShutdown) {
/*  68 */         throw new IllegalStateException(
/*  69 */             "Attempt to acquire resource from a shutdown factory");
/*     */       }
/*     */       
/*  72 */       entry = this.cache.get(settings);
/*  73 */       if (entry != null) {
/*     */         
/*  75 */         entry.incRefCount();
/*  76 */         logger.debug("Acquired cached connection (RC={}): {}", 
/*  77 */             Integer.valueOf(entry.getRefCount()), entry.getResource());
/*  78 */         return entry.getResource();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       entryLock = this.locks.getLock(settings);
/*     */     } 
/*     */     
/*  87 */     synchronized (entryLock) {
/*  88 */       synchronized (this) {
/*  89 */         entry = this.cache.get(settings);
/*  90 */         if (entry != null) {
/*     */ 
/*     */           
/*  93 */           entry.incRefCount();
/*  94 */           logger.debug("Acquired cached connection (RC={}): {}", 
/*  95 */               Integer.valueOf(entry.getRefCount()), entry.getResource());
/*  96 */           return entry.getResource();
/*     */         } 
/*     */       } 
/*     */       
/* 100 */       R resource = this.factory.acquire(settings);
/* 101 */       Runnable parentCloseHandler = resource.getCloseHandler();
/* 102 */       resource.setCloseHandler(new Runnable()
/*     */           {
/*     */             public void run() {
/* 105 */               CachedResourceFactory.this.release(settings);
/*     */             }
/*     */           });
/*     */       
/* 109 */       entry = (CacheEntry)new CacheEntry<>((Closeable)resource, parentCloseHandler);
/* 110 */       logger.debug("Acquired connection from factory (RC={}): {}", 
/* 111 */           Integer.valueOf(entry.getRefCount()), entry.getResource());
/*     */       
/* 113 */       synchronized (this) {
/* 114 */         this.cache.put(settings, entry);
/* 115 */         notify();
/*     */       } 
/*     */       
/* 118 */       return resource;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected synchronized void release(S settings) {
/* 123 */     CacheEntry<R> entry = this.cache.get(settings);
/* 124 */     if (entry == null) {
/* 125 */       logger.warn("Not found in cache: " + settings);
/*     */       
/*     */       return;
/*     */     } 
/* 129 */     entry.decRefCount();
/*     */     
/* 131 */     notify();
/*     */     
/* 133 */     logger.debug("Released connection (RC={}): {}", 
/* 134 */         Integer.valueOf(entry.getRefCount()), entry.getResource());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public R evict(S settings) {
/* 145 */     CacheEntry<R> entry = null;
/* 146 */     synchronized (this) {
/* 147 */       entry = this.cache.get(settings);
/* 148 */       if (entry == null) {
/* 149 */         throw new IllegalStateException(
/* 150 */             "Evicting a resource which is not in the cache!");
/*     */       }
/*     */       
/* 153 */       if (entry.getRefCount() > 0) {
/* 154 */         logger.debug("Connection won't be evicted, ref-count is {}: {}", 
/* 155 */             Integer.valueOf(entry.getRefCount()), entry.getResource());
/* 156 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 160 */       this.cache.remove(settings);
/*     */ 
/*     */ 
/*     */       
/* 164 */       this.locks.evict(settings);
/*     */       
/* 166 */       logger.debug("Evicted connection {} ref-count 0 reached at {}", 
/* 167 */           entry.getResource(), new Date(entry.getLastReleaseTime()));
/*     */     } 
/*     */     
/* 170 */     if (entry.getParentCloseHandler() != null) {
/* 171 */       logger.trace("Invoking original close handler to close the connection: {}", 
/* 172 */           entry.getResource());
/*     */       try {
/* 174 */         entry.getParentCloseHandler().run();
/* 175 */       } catch (Exception e) {
/* 176 */         logger.warn("Ignoring unsuccessful connection close: {}", entry.getResource(), e);
/*     */       } 
/*     */     } else {
/* 179 */       logger.trace("No original close handler to invoke: {}", entry.getResource());
/*     */     } 
/*     */     
/* 182 */     return entry.getResource();
/*     */   }
/*     */   
/*     */   public List<R> evictAll(long releasedBefore) {
/* 186 */     logger.debug("Evicting all entries that have 0 ref-count since " + new Date(releasedBefore));
/* 187 */     List<CacheEntry<R>> evictedEntries = new ArrayList<>();
/* 188 */     synchronized (this) {
/* 189 */       Iterator<S> it = this.cache.keySet().iterator();
/* 190 */       while (it.hasNext()) {
/* 191 */         S settings = it.next();
/* 192 */         CacheEntry<R> entry = this.cache.get(settings);
/*     */         
/* 194 */         if (entry.getRefCount() > 0 || 
/* 195 */           entry.getLastReleaseTime() > releasedBefore) {
/* 196 */           logger.trace("Not evicting entry, not applicable: {}", entry);
/*     */           
/*     */           continue;
/*     */         } 
/* 200 */         logger.trace("Evicting entry: {}", entry);
/* 201 */         evictedEntries.add(entry);
/*     */ 
/*     */         
/* 204 */         it.remove();
/*     */ 
/*     */ 
/*     */         
/* 208 */         this.locks.evict(settings);
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     List<R> result = new ArrayList<>();
/* 213 */     for (CacheEntry<R> entry : evictedEntries) {
/* 214 */       logger.trace("Closing evicted entity: {}", entry);
/*     */       try {
/* 216 */         if (entry.getParentCloseHandler() != null) {
/* 217 */           entry.getParentCloseHandler().run();
/*     */         }
/* 219 */       } catch (RuntimeException e) {
/* 220 */         logger.warn("Ignoring unsuccessful resource close: {}", entry, e);
/*     */       } 
/* 222 */       result.add(entry.getResource());
/*     */     } 
/*     */     
/* 225 */     logger.debug("Evicted {} entries.", Integer.valueOf(evictedEntries.size()));
/* 226 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized void gc() {
/* 230 */     gcImpl();
/*     */   }
/*     */   
/*     */   protected void gcImpl() {
/* 234 */     long now = System.currentTimeMillis();
/* 235 */     long deadline = now + 30000L;
/* 236 */     while (now <= deadline) {
/* 237 */       List<S> settings = new ArrayList<>(this.cache.keySet());
/* 238 */       for (S setting : settings) {
/* 239 */         evict(setting);
/*     */       }
/*     */       
/* 242 */       if (this.cache.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 247 */         wait(deadline - now);
/* 248 */       } catch (InterruptedException interruptedException) {
/* 249 */         logger.warn("Garbage collector interrupted while waiting!");
/*     */       } 
/*     */       
/* 252 */       now = System.currentTimeMillis();
/*     */     } 
/*     */     
/* 255 */     logger.warn("Garbage collector was unable to collect " + this.cache.size() + 
/* 256 */         " entries, which are still in use");
/*     */   }
/*     */   
/*     */   public synchronized void shutdown() {
/* 260 */     logger.debug("Shut-down initiated, evicting all cached entities.");
/* 261 */     this.isShutdown = true;
/* 262 */     gcImpl();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/CachedResourceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */