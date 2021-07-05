/*    */ package com.vmware.vsan.client.services.virtualobjects.data;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsan.client.services.common.data.VmData;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectDataProtectionHealthState;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectType;
/*    */ import java.util.Map;
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
/*    */ public class VsanVmObject
/*    */   extends VsanObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String description;
/*    */   public ManagedObjectReference vmRef;
/*    */   public String vmName;
/*    */   public String vmPrimaryIconId;
/*    */   
/*    */   public VsanVmObject() {}
/*    */   
/*    */   public VsanVmObject(VsanObjectIdentity objectIdentity) {
/* 34 */     this.objectType = VsanObjectType.parse(objectIdentity.type);
/* 35 */     this.description = objectIdentity.description;
/* 36 */     this.vmRef = objectIdentity.vm;
/* 37 */     this.vsanObjectUuid = objectIdentity.uuid;
/* 38 */     if (this.objectType.equals(VsanObjectType.other)) {
/* 39 */       this.name = this.description;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public VsanVmObject(VsanObjectType objectType, String objectUuid, ManagedObjectReference vmRef) {
/* 45 */     this.objectType = objectType;
/* 46 */     this.vmRef = vmRef;
/* 47 */     this.vsanObjectUuid = objectUuid;
/*    */   }
/*    */   
/*    */   public VsanVmObject(VsanObjectIdentity objectData, VmData vmData) {
/* 51 */     this(objectData);
/*    */     
/* 53 */     this.vmName = vmData.name;
/* 54 */     this.vmPrimaryIconId = vmData.primaryIconId;
/* 55 */     this.namespaceCapabilityMetadata = vmData.namespaceCapabilityMetadata;
/*    */ 
/*    */     
/* 58 */     if (this.objectType.equals(VsanObjectType.vdisk)) {
/* 59 */       if (vmData.uuidToVirtualDiskMap.containsKey(this.vsanObjectUuid)) {
/* 60 */         VirtualDisk disk = (VirtualDisk)vmData.uuidToVirtualDiskMap.get(this.vsanObjectUuid);
/* 61 */         this.name = disk.deviceInfo.label;
/* 62 */       } else if (vmData.uuidToDiskSnapshotMap.containsKey(this.vsanObjectUuid)) {
/* 63 */         this.name = vmData.getSnapshotName(this.vsanObjectUuid);
/* 64 */         this.objectType = VsanObjectType.vdisk_snapshot;
/*    */       } 
/* 66 */     } else if (this.objectType.equals(VsanObjectType.vdisk_snapshot)) {
/* 67 */       this.name = vmData.getSnapshotName(this.vsanObjectUuid);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateHealthData(Map<String, VsanObjectHealthData> vsanHealthData) {
/* 72 */     if (!vsanHealthData.containsKey(this.vsanObjectUuid)) {
/*    */       return;
/*    */     }
/*    */     
/* 76 */     VsanObjectHealthData vsanHealthInfo = vsanHealthData.get(this.vsanObjectUuid);
/* 77 */     super.updateHealthData(vsanHealthData);
/*    */ 
/*    */     
/* 80 */     if (vsanHealthInfo.vsanDataProtectionHealthState != null)
/* 81 */       this.dataProtectionHealthState = VsanObjectDataProtectionHealthState.fromString(
/* 82 */           vsanHealthInfo.vsanDataProtectionHealthState); 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VsanVmObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */