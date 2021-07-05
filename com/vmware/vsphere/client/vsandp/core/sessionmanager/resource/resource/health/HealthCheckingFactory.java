/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.health;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.CacheEntry;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.CachedResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class HealthCheckingFactory<R extends Resource, S>
/*     */   extends CachedResourceFactory<R, S>
/*     */ {
/*  39 */   private static final Log logger = LogFactory.getLog(HealthCheckingFactory.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ScheduledExecutorService scheduler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final long timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final IHealthMonitor<R, ? super S> monitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ScheduledFuture<?> pinger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ScheduledFuture<?> evictor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HealthCheckingFactory(ResourceFactory<R, S> parentFactory, IHealthMonitor<R, ? super S> monitor, ScheduledExecutorService scheduler, long delay, long timeout, final long retention) {
/*  81 */     super(new ResourceFactory<R, S>(parentFactory, monitor)
/*     */         {
/*     */           public R acquire(final S settings)
/*     */           {
/*     */             final Resource resource;
/*     */             try {
/*  87 */               resource = parentFactory.acquire(settings);
/*  88 */             } catch (Exception e) {
/*     */               try {
/*  90 */                 monitor.onError(null, settings, e);
/*  91 */               } catch (Exception e2) {
/*  92 */                 HealthCheckingFactory.logger.warn("onError failure", e2);
/*     */               } 
/*  94 */               throw e;
/*     */             } 
/*     */             
/*     */             try {
/*  98 */               monitor.onCreated(resource, settings);
/*  99 */             } catch (Exception e) {
/*     */               try {
/* 101 */                 HealthCheckingFactory.logger.warn(
/* 102 */                     "Closing resource due to an onCreated handler failure", e);
/* 103 */                 resource.close();
/* 104 */               } catch (Exception e2) {
/* 105 */                 HealthCheckingFactory.logger.warn("Could not dispose of resource", e2);
/*     */               } 
/* 107 */               throw e;
/*     */             } 
/*     */             
/* 110 */             final Runnable originalCloseHandler = resource.getCloseHandler();
/*     */             
/* 112 */             resource.setCloseHandler(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/* 116 */                     if (originalCloseHandler != null) {
/* 117 */                       originalCloseHandler.run();
/*     */                     }
/*     */                     
/* 120 */                     monitor.onDisposed(resource, settings);
/*     */                   }
/*     */                 });
/*     */ 
/*     */             
/* 125 */             return (R)resource;
/*     */           }
/*     */         });
/*     */     
/* 129 */     this.scheduler = scheduler;
/* 130 */     this.timeout = timeout;
/* 131 */     this.monitor = monitor;
/*     */     
/* 133 */     this.pinger = scheduler.scheduleWithFixedDelay(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 137 */             HealthCheckingFactory.this.checkEntries();
/*     */           }
/*     */         }, 
/* 140 */         delay, delay, TimeUnit.MILLISECONDS);
/*     */     
/* 142 */     if (retention > 0L) {
/* 143 */       this.evictor = scheduler.scheduleWithFixedDelay(new Runnable()
/*     */           {
/*     */             public void run() {
/* 146 */               HealthCheckingFactory.this.evictAll(System.currentTimeMillis() - retention);
/*     */             }
/* 148 */           },  retention, retention, TimeUnit.MILLISECONDS);
/*     */     } else {
/* 150 */       this.evictor = null;
/*     */     } 
/*     */   }
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
/*     */   public void checkEntries() {
/*     */     try {
/* 166 */       checkEntriesImpl();
/* 167 */     } catch (Exception e) {
/* 168 */       CheckedRunnable.handle(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkEntriesImpl() {
/* 173 */     List<CheckInProgress> checks = new ArrayList<>();
/*     */     
/* 175 */     Map<S, CacheEntry<R>> snapshot = new HashMap<>();
/* 176 */     synchronized (this) {
/* 177 */       snapshot.putAll(this.cache);
/*     */     } 
/*     */ 
/*     */     
/* 181 */     for (Map.Entry<S, CacheEntry<R>> entry : snapshot.entrySet()) {
/*     */       try {
/* 183 */         final S settings = entry.getKey();
/* 184 */         final Resource resource = (Resource)((CacheEntry)entry.getValue()).getResource();
/* 185 */         checks.add(new CheckInProgress(settings, this.scheduler.submit(new Runnable()
/*     */                 {
/*     */                   public void run() {
/*     */                     try {
/* 189 */                       HealthCheckingFactory.this.monitor.check(resource, settings);
/* 190 */                     } catch (RuntimeException e) {
/* 191 */                       throw e;
/* 192 */                     } catch (Exception e) {
/* 193 */                       throw new RuntimeException("Health-check failed.", e);
/*     */                     } 
/*     */                   }
/*     */                 })));
/* 197 */       } catch (RejectedExecutionException e) {
/* 198 */         logger.warn("Could not schedule check for entry " + entry.getKey(), e);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 203 */     List<BrokenEntry> brokenEntries = new ArrayList<>();
/* 204 */     long timeout = 30000L;
/* 205 */     long deadline = System.currentTimeMillis() + timeout;
/* 206 */     for (CheckInProgress check : checks) {
/*     */       try {
/* 208 */         logger.debug(String.format("Awaiting check for %s, with timeout %s", new Object[] { check.getSettings(), Long.valueOf(timeout) }));
/* 209 */         check.getFuture().get(timeout, TimeUnit.MILLISECONDS);
/* 210 */         logger.debug("Resource is healthy " + check.getSettings());
/* 211 */       } catch (InterruptedException ie) {
/* 212 */         logger.warn("Interrupted while checking resource: " + check.getSettings(), ie.getCause());
/* 213 */         brokenEntries.add(new BrokenEntry(check.getSettings(), ie.getCause())); continue;
/* 214 */       } catch (ExecutionException ee) {
/* 215 */         logger.warn("Resource is broken: " + check.getSettings(), ee.getCause());
/* 216 */         brokenEntries.add(new BrokenEntry(check.getSettings(), ee.getCause())); continue;
/* 217 */       } catch (TimeoutException te) {
/* 218 */         logger.warn("Timeout while waiting for health check for resource: " + check.getSettings());
/* 219 */         brokenEntries.add(new BrokenEntry(check.getSettings(), te.getCause())); continue;
/*     */       } finally {
/* 221 */         timeout = deadline - System.currentTimeMillis();
/* 222 */         if (timeout < 0L) {
/* 223 */           timeout = 0L;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 228 */     for (BrokenEntry brokenEntry : brokenEntries) {
/* 229 */       CacheEntry<R> entry = (CacheEntry<R>)this.cache.get(brokenEntry.settings);
/*     */       try {
/* 231 */         this.monitor.onError(
/* 232 */             (entry == null) ? null : (R)entry.getResource(), 
/* 233 */             brokenEntry.settings, brokenEntry.error);
/* 234 */       } catch (Exception e) {
/* 235 */         logger.warn("onError failure", e);
/*     */       } 
/*     */     } 
/*     */     
/* 239 */     List<Runnable> closeHandlers = new ArrayList<>();
/* 240 */     synchronized (this) {
/* 241 */       for (BrokenEntry brokenEntry : brokenEntries) {
/* 242 */         CacheEntry<R> entry = (CacheEntry<R>)this.cache.get(brokenEntry.settings);
/* 243 */         if (entry == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 248 */         if (entry.getRefCount() > 0) {
/* 249 */           logger.debug(String.format(
/* 250 */                 "Evicting broken resource: %s, with non-zero refcount: %s", new Object[] {
/* 251 */                   entry.getResource(), Integer.valueOf(entry.getRefCount())
/*     */                 }));
/*     */         }
/*     */         
/* 255 */         this.cache.remove(brokenEntry.settings);
/*     */ 
/*     */ 
/*     */         
/* 259 */         this.locks.evict(brokenEntry.settings);
/*     */         
/* 261 */         if (entry.getParentCloseHandler() != null) {
/* 262 */           closeHandlers.add(entry.getParentCloseHandler());
/*     */         }
/*     */         
/* 265 */         logger.debug("Evicted broken resource " + entry.getResource());
/*     */       } 
/*     */       
/* 268 */       notify();
/*     */     } 
/*     */     
/* 271 */     for (Runnable closeHandler : closeHandlers) {
/*     */       try {
/* 273 */         closeHandler.run();
/* 274 */       } catch (Exception e) {
/* 275 */         logger.warn("Ignoring unsuccessful disposal: {}", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 282 */     this.pinger.cancel(false);
/* 283 */     if (this.evictor != null) {
/* 284 */       this.evictor.cancel(false);
/*     */     }
/* 286 */     super.shutdown();
/*     */   }
/*     */   
/*     */   protected class CheckInProgress
/*     */   {
/*     */     protected S settings;
/*     */     protected Future<?> future;
/*     */     
/*     */     public CheckInProgress(S settings, Future<?> future) {
/* 295 */       this.settings = settings;
/* 296 */       this.future = future;
/*     */     }
/*     */     
/*     */     public S getSettings() {
/* 300 */       return this.settings;
/*     */     }
/*     */     
/*     */     public Future<?> getFuture() {
/* 304 */       return this.future;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class BrokenEntry {
/*     */     public final S settings;
/*     */     public Throwable error;
/*     */     
/*     */     public BrokenEntry(S settings, Throwable error) {
/* 313 */       this.settings = settings;
/* 314 */       this.error = error;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/health/HealthCheckingFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */