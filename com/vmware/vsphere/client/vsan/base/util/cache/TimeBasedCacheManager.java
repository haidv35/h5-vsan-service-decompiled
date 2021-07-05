/*     */ package com.vmware.vsphere.client.vsan.base.util.cache;
/*     */ 
/*     */ import com.vmware.vise.security.ClientSessionEndListener;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.lang.Validate;
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
/*     */ public abstract class TimeBasedCacheManager<K, V extends Cacheable<V>>
/*     */   implements ClientSessionEndListener
/*     */ {
/*  28 */   private static final Log _logger = LogFactory.getLog(TimeBasedCacheManager.class);
/*     */ 
/*     */   
/*     */   private final int _expirationTimeMin;
/*     */ 
/*     */   
/*     */   private final int _expirationTimeMax;
/*     */ 
/*     */   
/*     */   private final int _cleanThreshold;
/*     */ 
/*     */   
/*  40 */   private ConcurrentHashMap<String, ConcurrentHashMap<String, TimeBasedCacheEntry<V>>> _sessionMap = new ConcurrentHashMap<>();
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
/*     */   public TimeBasedCacheManager(int expirationTimeMin, int expirationTimeMax, int cleanThreshold) {
/*  58 */     Validate.isTrue((expirationTimeMin > 0));
/*  59 */     Validate.isTrue((expirationTimeMax > expirationTimeMin));
/*  60 */     Validate.isTrue((cleanThreshold > 0));
/*     */     
/*  62 */     this._expirationTimeMin = expirationTimeMin;
/*  63 */     this._expirationTimeMax = expirationTimeMax;
/*  64 */     this._cleanThreshold = cleanThreshold;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected V get(K keyObj, CacheType type) {
/*  82 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/*  84 */       Thread.currentThread().setContextClassLoader(TimeBasedCacheManager.class.getClassLoader());
/*     */       
/*  86 */       validate(keyObj);
/*     */ 
/*     */       
/*  89 */       clean();
/*     */ 
/*     */       
/*  92 */       String key = getKey(keyObj, type);
/*  93 */       if (StringUtils.isEmpty(key)) {
/*     */ 
/*     */         
/*  96 */         _logger.warn("Cannot generate a key from object: " + keyObj);
/*  97 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 101 */       TimeBasedCacheEntry<V> result = getCacheEntry(key, keyObj, type);
/* 102 */       if (result == null) {
/* 103 */         return null;
/*     */       }
/*     */       
/* 106 */       return result.get();
/*     */     } finally {
/* 108 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String sessionKey();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getKey(K paramK, CacheType paramCacheType);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract TimeBasedCacheEntry<V> createEntry(K paramK, CacheType paramCacheType);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void validate(K paramK);
/*     */ 
/*     */ 
/*     */   
/*     */   private TimeBasedCacheEntry<V> getCacheEntry(String key, K keyObj, CacheType type) {
/* 133 */     ConcurrentHashMap<String, TimeBasedCacheEntry<V>> cache = getSessionCache();
/*     */ 
/*     */     
/* 136 */     TimeBasedCacheEntry<V> result = cache.get(key);
/* 137 */     if (result == null) {
/*     */ 
/*     */       
/* 140 */       TimeBasedCacheEntry<V> newEntity = createEntry(keyObj, type);
/* 141 */       if (newEntity != null) {
/*     */         
/* 143 */         int expirationTime = calculateExpirationTime();
/* 144 */         newEntity.setExpirationTime(expirationTime);
/*     */       }
/*     */       else {
/*     */         
/* 148 */         _logger.warn("Cannot create a cache entry for key object: " + keyObj);
/* 149 */         return null;
/*     */       } 
/*     */       
/* 152 */       result = cache.putIfAbsent(key, newEntity);
/* 153 */       if (result == null) {
/* 154 */         _logger.debug("Cache entry created: {" + key + "} => {" + newEntity + "}");
/* 155 */         result = newEntity;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 160 */     return result;
/*     */   }
/*     */   
/*     */   private ConcurrentHashMap<String, TimeBasedCacheEntry<V>> getSessionCache() {
/* 164 */     ConcurrentHashMap<String, TimeBasedCacheEntry<V>> cache = this._sessionMap.get(sessionKey());
/* 165 */     if (cache == null) {
/* 166 */       ConcurrentHashMap<String, TimeBasedCacheEntry<V>> newCache = new ConcurrentHashMap<>();
/* 167 */       cache = this._sessionMap.putIfAbsent(sessionKey(), newCache);
/* 168 */       if (cache == null) {
/* 169 */         _logger.debug("Session entry created: {" + sessionKey() + "} => {" + newCache + "}");
/* 170 */         cache = newCache;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     return cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateExpirationTime() {
/* 184 */     return ThreadLocalRandom.current().nextInt(this._expirationTimeMin, 
/* 185 */         this._expirationTimeMax);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionEnded(String clientId) {
/* 193 */     if (this._sessionMap.containsKey(clientId)) {
/* 194 */       this._sessionMap.remove(clientId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 199 */     this._sessionMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clean() {
/* 207 */     ConcurrentHashMap<String, TimeBasedCacheEntry<V>> cache = this._sessionMap.get(sessionKey());
/*     */     
/* 209 */     if (cache == null || cache.size() < this._cleanThreshold) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 214 */     synchronized (cache) {
/* 215 */       for (Map.Entry<String, TimeBasedCacheEntry<V>> entry : cache.entrySet()) {
/* 216 */         String key = entry.getKey();
/* 217 */         TimeBasedCacheEntry<V> value = entry.getValue();
/*     */ 
/*     */         
/* 220 */         if (value.isExpired())
/* 221 */           cache.remove(key); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static interface CacheType {}
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/cache/TimeBasedCacheManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */