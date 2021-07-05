/*    */ package com.vmware.vsan.client.util;
/*    */ 
/*    */ import com.vmware.vim.vmomi.core.impl.BlockingFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MeasurableFuture<T>
/*    */   extends BlockingFuture<T>
/*    */ {
/*    */   private final Measure closeHandler;
/*    */   
/*    */   public MeasurableFuture(Measure measure, String task) {
/* 16 */     this.closeHandler = measure.start(task);
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(T ret) {
/*    */     try {
/* 22 */       this.closeHandler.close();
/* 23 */     } catch (Exception e) {
/* 24 */       e.printStackTrace();
/*    */     } 
/* 26 */     super.set(ret);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setException(Exception fault) {
/*    */     try {
/* 32 */       this.closeHandler.close();
/* 33 */     } catch (Exception e) {
/* 34 */       e.printStackTrace();
/*    */     } 
/* 36 */     super.setException(fault);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/MeasurableFuture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */