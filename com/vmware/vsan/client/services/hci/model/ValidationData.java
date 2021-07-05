/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class ValidationData
/*    */ {
/*    */   public List<VsanHealthCheck> vsanHealthChecks;
/*    */   public boolean isVsanEnabled;
/*    */   
/*    */   public ValidationData() {}
/*    */   
/*    */   public ValidationData(List<VsanHealthCheck> vsanHealthChecks, boolean isVsanEnabled) {
/* 20 */     this.vsanHealthChecks = vsanHealthChecks;
/* 21 */     this.isVsanEnabled = isVsanEnabled;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/ValidationData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */