/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.summary.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
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
/*    */ 
/*    */ @data
/*    */ public class VmArchiveDataProtectionData
/*    */ {
/*    */   public String datastoreName;
/*    */   public ManagedObjectReference datastoreRef;
/*    */   public VmDataProtectionStatus statusCode;
/*    */   public String statusDescription;
/*    */   public Date latestSyncPoint;
/*    */   public Date oldestSyncPoint;
/*    */   
/*    */   public VmArchiveDataProtectionData setPitsInfo(TreeSet<VmProtectionInstance> allInstances) {
/* 26 */     if (allInstances != null && !allInstances.isEmpty()) {
/* 27 */       this.latestSyncPoint = ((VmProtectionInstance)allInstances.first()).syncPoint;
/* 28 */       this.oldestSyncPoint = ((VmProtectionInstance)allInstances.last()).syncPoint;
/*    */     } else {
/* 30 */       this.latestSyncPoint = null;
/* 31 */       this.oldestSyncPoint = null;
/*    */     } 
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public VmArchiveDataProtectionData setDatastoreInf(ManagedObjectReference dsRef, String dsName) {
/* 37 */     this.datastoreRef = dsRef;
/* 38 */     this.datastoreName = dsName;
/* 39 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return "VmArchiveDataProtectionData {statusCode='" + 
/* 45 */       this.statusCode + "'" + 
/* 46 */       ", protectionStatus='" + this.statusDescription + "'" + 
/* 47 */       ", latestSyncPoint=" + this.latestSyncPoint + 
/* 48 */       ", oldestSyncPoint=" + this.oldestSyncPoint + 
/* 49 */       ", datastoreName=" + this.datastoreName + 
/* 50 */       ", datastoreRef=" + this.datastoreRef + 
/* 51 */       '}';
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/summary/model/VmArchiveDataProtectionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */