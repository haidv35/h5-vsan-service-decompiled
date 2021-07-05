/*    */ package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.util.Utils;
/*    */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*    */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*    */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.RestoreVmSpec;
/*    */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.VmInventoryModel;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ 
/*    */ @Component
/*    */ public class SingleVmRestoreBacking
/*    */ {
/*    */   @Autowired
/*    */   private RestoreWorkflowBacking commonBacking;
/*    */   @Autowired
/*    */   private VsanDpInventoryHelper inventoryHelper;
/*    */   
/*    */   @TsService
/*    */   public List<ManagedObjectReference> getRestoreVm(ManagedObjectReference vmRef, RestoreVmSpec spec) throws Exception {
/* 38 */     ManagedObjectReference clusterRef = this.inventoryHelper.getVmCluster(vmRef);
/* 39 */     ManagedObjectReference vmFolder = this.inventoryHelper.getVmFolderOfDataCenter(spec.selectedVmFolder);
/* 40 */     List<ManagedObjectReference> resultTasks = new ArrayList<>(); byte b; int i; VmProtectionInstance[] arrayOfVmProtectionInstance;
/* 41 */     for (i = (arrayOfVmProtectionInstance = spec.selectedSyncPoints).length, b = 0; b < i; ) { VmProtectionInstance pit = arrayOfVmProtectionInstance[b];
/* 42 */       ManagedObjectReference taskRef = 
/* 43 */         (ManagedObjectReference)this.commonBacking.restore(vmRef, pit, 
/* 44 */           Boolean.valueOf(spec.powerOn), Boolean.valueOf(spec.createIndependentVm), vmFolder, spec.storagePolicyId, spec.vmName, 
/* 45 */           spec.selectedNetwork, spec.selectedResourcePool, clusterRef).get();
/* 46 */       ManagedObjectReference resultTask = new ManagedObjectReference(taskRef.getType(), taskRef.getValue(), vmRef.getServerGuid());
/* 47 */       resultTasks.add(resultTask); b++; }
/*    */     
/* 49 */     return resultTasks;
/*    */   }
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public String getValidateTargetInventory(ManagedObjectReference vmRef, VmInventoryModel targetInventory) throws Exception {
/* 55 */     String result = this.commonBacking.getValidatePermissions(vmRef, targetInventory);
/* 56 */     if (result != null)
/*    */     {
/* 58 */       return result;
/*    */     }
/*    */ 
/*    */     
/* 62 */     if (!this.commonBacking.checkHostConnectionState(new ManagedObjectReference[] { targetInventory.compute.ref })) {
/* 63 */       return Utils.getLocalizedString("vsan.restore.validation.compute.connected.error");
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/SingleVmRestoreBacking.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */