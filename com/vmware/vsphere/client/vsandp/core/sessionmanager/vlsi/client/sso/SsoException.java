/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SsoException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -2170845508916630604L;
/*    */   
/*    */   public SsoException() {}
/*    */   
/*    */   public SsoException(String message, Throwable cause) {
/* 16 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public SsoException(String message) {
/* 20 */     super(message);
/*    */   }
/*    */   
/*    */   public SsoException(Throwable cause) {
/* 24 */     super(cause);
/*    */   }
/*    */   
/*    */   public static RuntimeException toSsoEx(Throwable e) {
/* 28 */     if (e instanceof RuntimeException) {
/* 29 */       return (RuntimeException)e;
/*    */     }
/*    */     
/* 32 */     return new SsoException(e);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */