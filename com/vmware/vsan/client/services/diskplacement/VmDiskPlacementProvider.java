/*    */ package com.vmware.vsan.client.services.diskplacement;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.services.virtualobjects.VirtualObjectsService;
/*    */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectModel;
/*    */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.lang.StringUtils;
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
/*    */ public class VmDiskPlacementProvider
/*    */ {
/*    */   private static final String SWAP_STORAGE_OBJECT_ID = "config.swapStorageObjectId";
/*    */   @Autowired
/*    */   private VirtualObjectsService virtualObjectsService;
/*    */   
/*    */   @TsService
/*    */   public List<VirtualObjectModel> getVmVirtualObjects(ManagedObjectReference vmRef) throws Exception {
/* 40 */     DataServiceResponse vmProperties = QueryUtil.getProperties(vmRef, 
/* 41 */         new String[] {
/* 42 */           "cluster", 
/* 43 */           "config.hardware.device", 
/* 44 */           "config.vmStorageObjectId", 
/* 45 */           "config.swapStorageObjectId"
/*    */         });
/* 47 */     ManagedObjectReference vmCluster = (ManagedObjectReference)vmProperties.getProperty(vmRef, "cluster");
/*    */     
/* 49 */     Set<String> vmObjectUuids = new HashSet<>();
/* 50 */     String vmHomeObjectUuid = (String)vmProperties.getProperty(vmRef, "config.vmStorageObjectId");
/* 51 */     vmObjectUuids.add(vmHomeObjectUuid);
/*    */ 
/*    */     
/* 54 */     String vmSwapObjectUuid = (String)vmProperties.getProperty(vmRef, "config.swapStorageObjectId");
/* 55 */     if (StringUtils.isNotEmpty(vmSwapObjectUuid)) {
/* 56 */       vmObjectUuids.add(vmSwapObjectUuid);
/*    */     }
/*    */ 
/*    */     
/* 60 */     VirtualDevice[] vmDevices = (VirtualDevice[])vmProperties.getProperty(vmRef, "config.hardware.device");
/* 61 */     vmObjectUuids.addAll(getVmDiskObjectUuids(vmDevices));
/*    */ 
/*    */     
/* 64 */     Exception exception1 = null, exception2 = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Set<String> getVmDiskObjectUuids(VirtualDevice[] vmDevices) {
/* 75 */     Set<String> result = new HashSet<>(); byte b; int i; VirtualDevice[] arrayOfVirtualDevice;
/* 76 */     for (i = (arrayOfVirtualDevice = vmDevices).length, b = 0; b < i; ) { VirtualDevice device = arrayOfVirtualDevice[b];
/* 77 */       if (device instanceof VirtualDisk) {
/*    */ 
/*    */         
/* 80 */         VirtualDisk disk = (VirtualDisk)device;
/* 81 */         if (disk.backing != null && disk.backing instanceof VirtualDevice.FileBackingInfo)
/*    */         
/*    */         { 
/* 84 */           VirtualDevice.FileBackingInfo fileBackingInfo = (VirtualDevice.FileBackingInfo)disk.backing;
/* 85 */           if (!StringUtils.isEmpty(fileBackingInfo.backingObjectId))
/*    */           {
/*    */             
/* 88 */             result.add(fileBackingInfo.backingObjectId); }  } 
/*    */       }  b++; }
/* 90 */      return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskplacement/VmDiskPlacementProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */