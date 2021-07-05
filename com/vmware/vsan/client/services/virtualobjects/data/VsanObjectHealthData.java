/*    */ package com.vmware.vsan.client.services.virtualobjects.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ @data
/*    */ public class VsanObjectHealthData
/*    */ {
/*    */   public String vsanHealthState;
/*    */   public String vsanDataProtectionHealthState;
/*    */   public String policyName;
/*    */   
/*    */   public VsanObjectHealthData(String vsanHealthState, String vsanDpHealthState, String policyName) {
/* 25 */     this.vsanHealthState = vsanHealthState;
/* 26 */     this.vsanDataProtectionHealthState = vsanDpHealthState;
/* 27 */     this.policyName = policyName;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VsanObjectHealthData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */