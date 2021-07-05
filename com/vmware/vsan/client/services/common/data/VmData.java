/*     */ package com.vmware.vsan.client.services.common.data;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VmData
/*     */ {
/*     */   public String name;
/*     */   public String primaryIconId;
/*     */   public Map<String, VirtualDisk> uuidToVirtualDiskMap;
/*     */   public Map<String, VirtualDisk.FlatVer2BackingInfo> uuidToDiskSnapshotMap;
/*     */   public Map<VirtualDisk.FlatVer2BackingInfo, VirtualDisk> backingInfoToDiskMap;
/*     */   public String vmPathUuid;
/*     */   public ManagedObjectReference vmRef;
/*     */   public Object namespaceCapabilityMetadata;
/*     */   
/*     */   public VmData(ManagedObjectReference vmRef) {
/*  65 */     this.vmRef = vmRef;
/*     */   }
/*     */   
/*     */   public void updateVmData(PropertyValue propValue) {
/*  69 */     if (propValue == null || propValue.value == null) {
/*     */       return;
/*     */     }
/*     */     String str;
/*  73 */     switch ((str = propValue.propertyName).hashCode()) { case -1099694814: if (!str.equals("namespaceCapabilityMetadata")) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  87 */         this.namespaceCapabilityMetadata = propValue.value; break;
/*     */       case -826278890:
/*     */         if (!str.equals("primaryIconId"))
/*     */           break;  this.primaryIconId = (String)propValue.value; break;
/*     */       case -637434256:
/*     */         if (!str.equals("config.hardware.device"))
/*     */           break;  setVirtualDiskMaps((VirtualDevice[])propValue.value); break;
/*     */       case 3373707:
/*     */         if (!str.equals("name"))
/*     */           break;  this.name = (String)propValue.value; break;
/*     */       case 814403083:
/*     */         if (!str.equals("summary.config.vmPathName"))
/*  99 */           break;  this.vmPathUuid = getVmHomeVsanUuid((String)propValue.value); break; }  } private String getVmHomeVsanUuid(String vmFilePath) { if (vmFilePath == null) {
/* 100 */       return null;
/*     */     }
/* 102 */     int startIndex = vmFilePath.indexOf(']');
/* 103 */     int endIndex = vmFilePath.indexOf('/');
/* 104 */     if (startIndex >= 0 && endIndex > startIndex) {
/* 105 */       return vmFilePath.substring(startIndex + 1, endIndex).trim();
/*     */     }
/* 107 */     return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVirtualDiskMaps(VirtualDevice[] virtualDevices) {
/* 115 */     if (virtualDevices == null || virtualDevices.length == 0) {
/*     */       return;
/*     */     }
/* 118 */     Map<String, VirtualDisk> uuidToVirtualDiskMap = new HashMap<>();
/* 119 */     Map<String, VirtualDisk.FlatVer2BackingInfo> uuidToDiskSnapshotMap = new HashMap<>();
/* 120 */     Map<VirtualDisk.FlatVer2BackingInfo, VirtualDisk> backingInfoToDiskMap = new HashMap<>(); byte b; int i; VirtualDevice[] arrayOfVirtualDevice;
/* 121 */     for (i = (arrayOfVirtualDevice = virtualDevices).length, b = 0; b < i; ) { VirtualDevice device = arrayOfVirtualDevice[b];
/* 122 */       if (!(device instanceof VirtualDisk)) {
/*     */         continue;
/*     */       }
/* 125 */       VirtualDisk disk = (VirtualDisk)device;
/* 126 */       if (disk.backing != null && disk.backing instanceof VirtualDevice.FileBackingInfo) {
/* 127 */         VirtualDevice.FileBackingInfo fileBackingInfo = (VirtualDevice.FileBackingInfo)disk.backing;
/* 128 */         if (StringUtils.isEmpty(fileBackingInfo.backingObjectId)) {
/*     */           continue;
/*     */         }
/* 131 */         uuidToVirtualDiskMap.put(fileBackingInfo.backingObjectId, disk);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 136 */       Object parentBackingInfoObject = getParentVirtualDiskBacking(disk);
/* 137 */       if (parentBackingInfoObject instanceof VirtualDisk.FlatVer2BackingInfo) {
/*     */ 
/*     */         
/* 140 */         VirtualDisk.FlatVer2BackingInfo parentBackingInfo = (VirtualDisk.FlatVer2BackingInfo)parentBackingInfoObject;
/* 141 */         while (parentBackingInfo != null) {
/* 142 */           uuidToDiskSnapshotMap.put(parentBackingInfo.backingObjectId, 
/* 143 */               parentBackingInfo);
/* 144 */           backingInfoToDiskMap.put(parentBackingInfo, disk);
/* 145 */           parentBackingInfo = parentBackingInfo.parent;
/*     */         } 
/*     */       }  continue; b++; }
/* 148 */      this.uuidToDiskSnapshotMap = uuidToDiskSnapshotMap;
/* 149 */     this.backingInfoToDiskMap = backingInfoToDiskMap;
/* 150 */     this.uuidToVirtualDiskMap = uuidToVirtualDiskMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object getParentVirtualDiskBacking(VirtualDisk disk) {
/* 160 */     if (disk.backing != null && 
/* 161 */       disk.backing instanceof VirtualDisk.FlatVer2BackingInfo) {
/* 162 */       return ((VirtualDisk.FlatVer2BackingInfo)disk.backing).parent;
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSnapshotName(String uuid) {
/* 171 */     VirtualDisk.FlatVer2BackingInfo parentBackingInfo = this.uuidToDiskSnapshotMap.get(uuid);
/* 172 */     VirtualDisk disk = this.backingInfoToDiskMap.get(parentBackingInfo);
/* 173 */     if (disk == null) {
/* 174 */       return "";
/*     */     }
/* 176 */     return String.format("%s - %s", new Object[] { disk.deviceInfo.label, getVirtualDiskFileName(parentBackingInfo.fileName) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getVirtualDiskFileName(String filePath) {
/* 189 */     if (!StringUtils.isEmpty(filePath)) {
/*     */       
/* 191 */       String[] splittedPath = filePath.split("/");
/* 192 */       if (!ArrayUtils.isEmpty((Object[])splittedPath)) {
/* 193 */         return StringUtils.trim(
/* 194 */             splittedPath[splittedPath.length - 1]);
/*     */       }
/*     */     } 
/*     */     
/* 198 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/data/VmData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */