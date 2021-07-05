/*    */ package com.vmware.vsan.client.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoOpMeasure
/*    */   extends Measure
/*    */ {
/*    */   public NoOpMeasure() {
/*  9 */     super("");
/*    */   }
/*    */ 
/*    */   
/*    */   public Measure start(String task) {
/* 14 */     return new NoOpMeasure();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/NoOpMeasure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */