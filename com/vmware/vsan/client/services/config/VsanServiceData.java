/*    */ package com.vmware.vsan.client.services.config;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanServiceData
/*    */ {
/*    */   public VsanServiceStatus status;
/*    */   public Object details;
/*    */   
/*    */   public VsanServiceData() {}
/*    */   
/*    */   public VsanServiceData(VsanServiceStatus status) {
/* 19 */     this.status = status;
/*    */   }
/*    */   
/*    */   public VsanServiceData(VsanServiceStatus status, Object details) {
/* 23 */     this.status = status;
/* 24 */     this.details = details;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/config/VsanServiceData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */