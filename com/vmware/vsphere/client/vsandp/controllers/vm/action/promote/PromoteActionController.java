/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.action.promote;
/*    */ 
/*    */ import com.google.common.collect.ArrayListMultimap;
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.VirtualMachine;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import com.vmware.vsan.client.util.VcPropertiesFacade;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
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
/*    */ @Component
/*    */ public class PromoteActionController
/*    */ {
/*    */   private static final String MANUAL_SNAPSHOT_ISSUE = "manualSnapshotExists";
/*    */   private static final String POWER_OFF_ISSUE = "vmIsPoweredOff";
/*    */   private static final String VM_SNAPSHOT = "snapshot";
/*    */   private static final String VM_POWER_STATE = "runtime.powerState";
/*    */   private static final String VM_DEVICES = "config.hardware.device";
/*    */   @Autowired
/*    */   private VcPropertiesFacade vcPropertiesFacade;
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */   
/*    */   @TsService
/*    */   public Map<String, Collection<ManagedObjectReference>> validate(ManagedObjectReference[] vmRefs) throws Exception {
/* 44 */     PropertyValue[] values = QueryUtil.getProperties((Object[])vmRefs, new String[] { "snapshot", "runtime.powerState" }).getPropertyValues();
/* 45 */     ArrayListMultimap arrayListMultimap = ArrayListMultimap.create(); byte b; int i;
/*    */     PropertyValue[] arrayOfPropertyValue1;
/* 47 */     for (i = (arrayOfPropertyValue1 = values).length, b = 0; b < i; ) { PropertyValue item = arrayOfPropertyValue1[b]; String str;
/* 48 */       switch ((str = item.propertyName).hashCode()) { case 284874180: if (!str.equals("snapshot"))
/*    */             break; 
/* 50 */           if (item.value != null)
/* 51 */             arrayListMultimap.put("manualSnapshotExists", item.resourceObject);  break;
/*    */         case 541171298:
/*    */           if (!str.equals("runtime.powerState"))
/*    */             break; 
/* 55 */           if (item.value.equals(VirtualMachine.PowerState.poweredOff)) {
/* 56 */             arrayListMultimap.put("vmIsPoweredOff", item.resourceObject);
/*    */           }
/*    */           break; }
/*    */       
/*    */       b++; }
/*    */     
/* 62 */     return arrayListMultimap.asMap();
/*    */   }
/*    */   
/*    */   @TsService
/*    */   public void promote(ManagedObjectReference[] vmRefs) throws Exception {
/* 67 */     PropertyValue[] vmDeviceProperties = QueryUtil.getProperties((Object[])vmRefs, new String[] { "config.hardware.device" }).getPropertyValues(); byte b; int i;
/*    */     PropertyValue[] arrayOfPropertyValue1;
/* 69 */     for (i = (arrayOfPropertyValue1 = vmDeviceProperties).length, b = 0; b < i; ) { PropertyValue property = arrayOfPropertyValue1[b];
/* 70 */       ArrayList<VirtualDisk> disks = new ArrayList<>(); byte b1; int j; VirtualDevice[] arrayOfVirtualDevice;
/* 71 */       for (j = (arrayOfVirtualDevice = (VirtualDevice[])property.value).length, b1 = 0; b1 < j; ) { VirtualDevice device = arrayOfVirtualDevice[b1];
/* 72 */         if (device instanceof VirtualDisk) {
/* 73 */           VirtualDisk disk = (VirtualDisk)device;
/* 74 */           if (this.vcPropertiesFacade.isNativeUnmanagedLinkedClone(disk)) {
/* 75 */             disks.add(disk);
/*    */           }
/*    */         } 
/*    */         b1++; }
/*    */       
/* 80 */       ManagedObjectReference vmRef = (ManagedObjectReference)property.resourceObject;
/* 81 */       Exception exception1 = null, exception2 = null; }
/*    */   
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/action/promote/PromoteActionController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */