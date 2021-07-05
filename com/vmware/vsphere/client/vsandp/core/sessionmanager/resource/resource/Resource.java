/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource;
/*    */ 
/*    */ import java.io.Closeable;
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
/*    */ 
/*    */ public class Resource
/*    */   implements Closeable
/*    */ {
/*    */   private Runnable onClose;
/*    */   
/*    */   public void setCloseHandler(Runnable onClose) {
/* 26 */     this.onClose = onClose;
/*    */   }
/*    */   
/*    */   public Runnable getCloseHandler() {
/* 30 */     return this.onClose;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 35 */     if (this.onClose != null)
/* 36 */       this.onClose.run(); 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/Resource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */