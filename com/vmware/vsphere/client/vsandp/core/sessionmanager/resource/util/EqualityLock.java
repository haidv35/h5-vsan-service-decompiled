/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EqualityLock
/*    */ {
/* 22 */   protected final Map<Object, Object> locks = new HashMap<>();
/*    */   
/*    */   public Object getLock(Object object) {
/* 25 */     Object lock = this.locks.get(object);
/* 26 */     if (lock != null) {
/* 27 */       return lock;
/*    */     }
/*    */     
/* 30 */     lock = new Idlock();
/*    */     
/* 32 */     this.locks.put(object, lock);
/*    */     
/* 34 */     return lock;
/*    */   }
/*    */   
/*    */   public void evict(Object object) {
/* 38 */     this.locks.remove(object);
/*    */   }
/*    */   
/*    */   protected static class Idlock {}
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/util/EqualityLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */