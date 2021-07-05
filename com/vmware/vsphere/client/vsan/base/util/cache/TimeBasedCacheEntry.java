/*    */ package com.vmware.vsphere.client.vsan.base.util.cache;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TimeBasedCacheEntry<T extends Cacheable<T>>
/*    */ {
/*    */   private int _expirationTime;
/*    */   private long _lastTimeUpdated;
/*    */   private volatile T _value;
/* 22 */   private static final Log _logger = LogFactory.getLog(TimeBasedCacheEntry.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized T get() {
/* 29 */     if (isExpired() || this._value == null) {
/* 30 */       this._value = load();
/* 31 */       this._lastTimeUpdated = now();
/*    */     } 
/*    */     
/* 34 */     return (T)this._value.clone();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract T load();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isExpired() {
/* 48 */     return (now() - this._expirationTime > this._lastTimeUpdated);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpirationTime(int expirationTime) {
/* 55 */     this._expirationTime = expirationTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static long now() {
/* 62 */     return (new Date()).getTime();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/cache/TimeBasedCacheEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */