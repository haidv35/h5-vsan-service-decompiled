/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
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
/*    */ 
/*    */ public abstract class CheckedRunnable
/*    */ {
/*    */   public static void withoutChecked(CheckedRunnable closure) {
/*    */     try {
/* 25 */       closure.run();
/* 26 */     } catch (Exception e) {
/* 27 */       handle(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void handle(Exception e) {
/* 32 */     if (e instanceof ExecutionException && (
/* 33 */       (ExecutionException)e).getCause() != null) {
/* 34 */       Throwable t = ((ExecutionException)e).getCause();
/* 35 */       if (t instanceof Exception) {
/* 36 */         e = (Exception)t;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     if (e instanceof RuntimeException)
/*    */     {
/* 42 */       throw (RuntimeException)e;
/*    */     }
/*    */ 
/*    */     
/* 46 */     throw new RuntimeException(e);
/*    */   }
/*    */   
/*    */   public abstract void run() throws Exception;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/util/CheckedRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */