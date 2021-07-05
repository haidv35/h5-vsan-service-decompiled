/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.binding.vmodl.fault.InvalidArgument;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
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
/*    */ @Component
/*    */ public class VirtualObjectsDataProtectionController
/*    */ {
/*    */   @TsService
/*    */   public VirtualDisk getDiskDetails(ManagedObjectReference vmRef, String diskId) throws Exception {
/* 28 */     VirtualDevice[] virtualDevices = (VirtualDevice[])QueryUtil.getProperty(vmRef, "config.hardware.device", null); byte b; int i; VirtualDevice[] arrayOfVirtualDevice1;
/* 29 */     for (i = (arrayOfVirtualDevice1 = virtualDevices).length, b = 0; b < i; ) { VirtualDevice device = arrayOfVirtualDevice1[b];
/* 30 */       if (device instanceof VirtualDisk) {
/*    */ 
/*    */         
/* 33 */         VirtualDisk disk = (VirtualDisk)device;
/* 34 */         if (disk.backing != null && disk.backing instanceof VirtualDevice.FileBackingInfo) {
/* 35 */           VirtualDevice.FileBackingInfo fileBacking = (VirtualDevice.FileBackingInfo)disk.backing;
/* 36 */           if (fileBacking.backingObjectId.equals(diskId))
/* 37 */             return disk; 
/*    */         } 
/*    */       } 
/*    */       b++; }
/*    */     
/* 42 */     throw new InvalidArgument(diskId);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/VirtualObjectsDataProtectionController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */