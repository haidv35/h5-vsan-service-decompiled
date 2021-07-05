/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.summary.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VmDataProtectionData
/*    */ {
/*    */   public VmLocalDataProtectionData localProtectionData;
/*    */   public VmArchiveDataProtectionData archiveProtectionData;
/*    */   public boolean hasRestorePermission;
/*    */   
/*    */   public VmDataProtectionData(VmLocalDataProtectionData localProtectionData, VmArchiveDataProtectionData archiveProtectionData, boolean hasRestorePermission) {
/* 18 */     this.localProtectionData = localProtectionData;
/* 19 */     this.archiveProtectionData = archiveProtectionData;
/* 20 */     this.hasRestorePermission = hasRestorePermission;
/*    */   }
/*    */   
/*    */   public VmDataProtectionData() {}
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/summary/model/VmDataProtectionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */