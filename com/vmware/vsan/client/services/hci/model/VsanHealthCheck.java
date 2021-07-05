/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.health.VsanHealthStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanHealthCheck
/*    */ {
/*    */   public String perspective;
/*    */   public String healthGroup;
/*    */   public String healthTest;
/*    */   public String healthCheckLabel;
/*    */   public VsanHealthStatus status;
/*    */   
/*    */   public VsanHealthCheck() {}
/*    */   
/*    */   public VsanHealthCheck(String perspective, String healthGroup, String healthTest, String healthCheckLabel, String healthStatus) {
/* 22 */     this.perspective = perspective;
/* 23 */     this.healthGroup = healthGroup;
/* 24 */     this.healthTest = healthTest;
/* 25 */     this.healthCheckLabel = healthCheckLabel;
/* 26 */     this.status = VsanHealthStatus.valueOf(healthStatus);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/VsanHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */