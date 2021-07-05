/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SessionLocal<T>
/*    */ {
/* 16 */   private static final Logger logger = LoggerFactory.getLogger(SessionLocal.class);
/*    */   
/* 18 */   private ConcurrentHashMap<String, T> sessionContext = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected T get() {
/* 26 */     String key = sessionKey();
/* 27 */     T result = this.sessionContext.get(key);
/* 28 */     if (result == null) {
/* 29 */       T newEntity = create();
/* 30 */       result = this.sessionContext.putIfAbsent(key, newEntity);
/* 31 */       if (result == null) {
/* 32 */         logger.debug("Session entry created: {} => {}", key, newEntity);
/* 33 */         result = newEntity;
/*    */       } else {
/*    */         
/* 36 */         destroy(newEntity);
/*    */       } 
/*    */     } 
/* 39 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void remove(String clientId) {
/*    */     try {
/* 47 */       String key = (clientId != null) ? clientId : sessionKey();
/* 48 */       T removedEntity = this.sessionContext.remove(key);
/* 49 */       if (removedEntity != null) {
/* 50 */         destroy(removedEntity);
/* 51 */         logger.debug("Session entry dropped: {} => {}", key, removedEntity);
/*    */       } 
/* 53 */     } catch (Exception e) {
/* 54 */       logger.error("Failed to clear client's session context: {}", this, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void clear() {
/* 62 */     logger.debug("Dropping all session entries: {}", Integer.valueOf(this.sessionContext.size()));
/*    */     
/* 64 */     Iterator<Map.Entry<String, T>> iterator = this.sessionContext.entrySet().iterator();
/* 65 */     while (iterator.hasNext()) {
/* 66 */       Map.Entry<String, T> entry = iterator.next();
/* 67 */       iterator.remove();
/* 68 */       destroy(entry.getValue());
/* 69 */       logger.debug("Session entry dropped: {} => {}", entry.getKey(), entry.getValue());
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract String sessionKey();
/*    */   
/*    */   protected abstract T create();
/*    */   
/*    */   protected abstract void destroy(T paramT);
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/SessionLocal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */