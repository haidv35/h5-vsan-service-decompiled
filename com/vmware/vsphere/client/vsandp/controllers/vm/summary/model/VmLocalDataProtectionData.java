/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.summary.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*    */ import java.util.Date;
/*    */ import java.util.TreeSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VmLocalDataProtectionData
/*    */ {
/*    */   public VmDataProtectionStatus statusCode;
/*    */   public String statusDescription;
/*    */   public Date latestSyncPoint;
/*    */   public Date oldestSyncPoint;
/*    */   
/*    */   public VmLocalDataProtectionData setPitsInfo(TreeSet<VmProtectionInstance> allInstances) {
/* 22 */     if (allInstances != null && !allInstances.isEmpty()) {
/* 23 */       this.latestSyncPoint = ((VmProtectionInstance)allInstances.first()).syncPoint;
/* 24 */       this.oldestSyncPoint = ((VmProtectionInstance)allInstances.last()).syncPoint;
/*    */     } else {
/* 26 */       this.latestSyncPoint = null;
/* 27 */       this.oldestSyncPoint = null;
/*    */     } 
/* 29 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/summary/model/VmLocalDataProtectionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */