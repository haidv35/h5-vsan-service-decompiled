/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class NotAuthenticatedException
/*    */   extends RuntimeException
/*    */ {
/*    */   public NotAuthenticatedException() {}
/*    */   
/*    */   public NotAuthenticatedException(String message) {
/* 16 */     super(message);
/*    */   }
/*    */   
/*    */   public NotAuthenticatedException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */   
/*    */   public NotAuthenticatedException(String message, Throwable cause) {
/* 24 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/NotAuthenticatedException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */