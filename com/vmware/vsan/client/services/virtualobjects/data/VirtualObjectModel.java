/*     */ package com.vmware.vsan.client.services.virtualobjects.data;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.vmware.vim.binding.vim.vm.ConfigInfo;
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUN;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTarget;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentityAndHealth;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectInformation;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectHealthState;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectType;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ 
/*     */ @data
/*     */ public class VirtualObjectModel {
/*     */   public String uid;
/*     */   public ManagedObjectReference vmRef;
/*     */   
/*     */   public VirtualObjectModel() {
/*  33 */     this.applicableFilter = VirtualObjectsFilter.OTHERS;
/*     */   }
/*     */   public String iconId; public String name; public VirtualObjectsFilter applicableFilter; public VsanObjectHealthState healthState; public VsanWhatIfComplianceStatus whatIfComplianceStatus;
/*     */   public VsanObjectDataProtectionHealthState dataProtectionHealthState;
/*     */   public String storagePolicy;
/*     */   public VirtualObjectModel[] children;
/*     */   
/*     */   public VirtualObjectModel cloneWithoutChildren() {
/*  41 */     VirtualObjectModel o = new VirtualObjectModel();
/*     */     
/*  43 */     o.uid = this.uid;
/*  44 */     o.vmRef = this.vmRef;
/*  45 */     o.name = this.name;
/*  46 */     o.iconId = this.iconId;
/*  47 */     o.healthState = this.healthState;
/*  48 */     o.whatIfComplianceStatus = this.whatIfComplianceStatus;
/*  49 */     o.dataProtectionHealthState = this.dataProtectionHealthState;
/*  50 */     o.storagePolicy = this.storagePolicy;
/*  51 */     o.children = new VirtualObjectModel[0];
/*     */     
/*  53 */     return o;
/*     */   }
/*     */   
/*     */   public VirtualObjectModel cloneWithChildren() {
/*  57 */     VirtualObjectModel o = cloneWithoutChildren();
/*     */     
/*  59 */     List<VirtualObjectModel> children = new ArrayList<>(this.children.length); byte b; int i; VirtualObjectModel[] arrayOfVirtualObjectModel;
/*  60 */     for (i = (arrayOfVirtualObjectModel = this.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/*  61 */       children.add(child.cloneWithoutChildren()); b++; }
/*     */     
/*  63 */     o.children = children.<VirtualObjectModel>toArray(new VirtualObjectModel[children.size()]);
/*     */     
/*  65 */     return o;
/*     */   }
/*     */   
/*     */   public void mergeChildren(VirtualObjectModel clone) {
/*  69 */     if (clone == null || ArrayUtils.isEmpty((Object[])clone.children)) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     Set<VirtualObjectModel> children = new HashSet<>(Arrays.asList(this.children)); byte b; int i; VirtualObjectModel[] arrayOfVirtualObjectModel;
/*  74 */     for (i = (arrayOfVirtualObjectModel = clone.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/*  75 */       children.add(child);
/*     */       b++; }
/*     */     
/*  78 */     this.children = children.<VirtualObjectModel>toArray(new VirtualObjectModel[children.size()]);
/*     */   }
/*     */   
/*     */   public static List<VirtualObjectModel> buildIscsiTargets(VsanIscsiTarget[] vsanIscsiTargets, VsanIscsiLUN[] vsanIscsiLUNs) {
/*  82 */     List<VirtualObjectModel> result = new ArrayList<>(); byte b; int i;
/*     */     VsanIscsiTarget[] arrayOfVsanIscsiTarget;
/*  84 */     for (i = (arrayOfVsanIscsiTarget = vsanIscsiTargets).length, b = 0; b < i; ) { VsanIscsiTarget iscsiTarget = arrayOfVsanIscsiTarget[b];
/*  85 */       VirtualObjectModel iscsiModel = fromObjInfo(iscsiTarget.objectInformation, iscsiTarget.objectInformation.vsanObjectUuid);
/*  86 */       iscsiModel.name = iscsiTarget.alias;
/*  87 */       iscsiModel.iconId = "iscsi-target-icon";
/*  88 */       iscsiModel.applicableFilter = VirtualObjectsFilter.ISCSI_TARGETS;
/*     */       
/*  90 */       result.add(iscsiModel);
/*     */ 
/*     */       
/*  93 */       if (vsanIscsiLUNs != null) {
/*     */ 
/*     */ 
/*     */         
/*  97 */         List<VirtualObjectModel> lunModels = new ArrayList<>(); byte b1; int j; VsanIscsiLUN[] arrayOfVsanIscsiLUN;
/*  98 */         for (j = (arrayOfVsanIscsiLUN = vsanIscsiLUNs).length, b1 = 0; b1 < j; ) { VsanIscsiLUN lun = arrayOfVsanIscsiLUN[b1];
/*  99 */           if (lun.targetAlias.equals(iscsiTarget.alias)) {
/* 100 */             VirtualObjectModel lunModel = fromObjInfo(lun.objectInformation, lun.objectInformation.vsanObjectUuid);
/* 101 */             lunModel.name = Utils.getLocalizedString("vsan.virtualObjects.iscsiLun", new String[] {
/* 102 */                   (lun.alias != null) ? lun.alias : "", 
/* 103 */                   Integer.toString((lun.lunId != null) ? lun.lunId.intValue() : 0) }).trim();
/* 104 */             lunModel.iconId = "iscsi-lun-icon";
/* 105 */             lunModels.add(lunModel);
/*     */           } 
/*     */           b1++; }
/*     */         
/* 109 */         iscsiModel.children = lunModels.<VirtualObjectModel>toArray(new VirtualObjectModel[lunModels.size()]);
/* 110 */         Arrays.sort(iscsiModel.children, COMPARATOR);
/*     */       }  b++; }
/*     */     
/* 113 */     Collections.sort(result, COMPARATOR);
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<VirtualObjectModel> buildFcds(VsanObjectIdentityAndHealth identities, VsanObjectInformation[] vsanObjectInformations) {
/* 120 */     Map<String, VsanObjectIdentity> objIdentityByVsanUuid = mapObjIdentities(identities);
/* 121 */     Map<String, VsanObjectInformation> objInfoByVsanUuid = mapObjInfos(vsanObjectInformations);
/* 122 */     List<VirtualObjectModel> allFcds = new ArrayList<>();
/*     */     
/* 124 */     for (VsanObjectIdentity identity : objIdentityByVsanUuid.values()) {
/* 125 */       VsanObjectType type = VsanObjectType.parse(identity.type);
/* 126 */       if (type != VsanObjectType.improvedVirtualDisk || identity.vm != null) {
/*     */         continue;
/*     */       }
/*     */       
/* 130 */       VirtualObjectModel model = fromObjInfo(objInfoByVsanUuid.get(identity.uuid), identity.uuid);
/* 131 */       model.iconId = "disk-icon";
/* 132 */       model.name = identity.description;
/* 133 */       model.applicableFilter = VirtualObjectsFilter.FCD_OBJECTS;
/* 134 */       allFcds.add(model);
/*     */     } 
/*     */     
/* 137 */     Collections.sort(allFcds, COMPARATOR);
/* 138 */     return allFcds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<VirtualObjectModel> buildVms(VsanObjectIdentityAndHealth identities, Set<ManagedObjectReference> vmRefs, VsanObjectInformation[] vsanObjectInformations, Map<Object, Map<String, Object>> dsProperties, Multimap<ManagedObjectReference, ConfigInfo> snapshots) {
/* 147 */     Map<String, VsanObjectIdentity> objIdentityByVsanUuid = mapObjIdentities(identities);
/* 148 */     Map<String, VsanObjectInformation> objInfoByVsanUuid = mapObjInfos(vsanObjectInformations);
/*     */ 
/*     */     
/* 151 */     HashMultimap hashMultimap = HashMultimap.create();
/*     */     
/* 153 */     for (VsanObjectIdentity identity : objIdentityByVsanUuid.values()) {
/* 154 */       VirtualDevice[] devices; Collection<ConfigInfo> snapshotConfig; VsanObjectType type = VsanObjectType.parse(identity.type);
/* 155 */       String name = null;
/* 156 */       String iconId = null;
/*     */       
/* 158 */       switch (type) {
/*     */         case namespace:
/* 160 */           name = Utils.getLocalizedString("vsan.virtualObjects.vmHome");
/* 161 */           iconId = "folder";
/*     */           break;
/*     */         case null:
/* 164 */           name = identity.description;
/* 165 */           iconId = "disk-icon";
/*     */           break;
/*     */         case vdisk:
/* 168 */           devices = dsProperties.containsKey(identity.vm) ? 
/* 169 */             (VirtualDevice[])((Map)dsProperties.get(identity.vm)).get("config.hardware.device") : 
/* 170 */             new VirtualDevice[0];
/* 171 */           snapshotConfig = snapshots.containsKey(identity.vm) ? 
/* 172 */             snapshots.get(identity.vm) : 
/* 173 */             Collections.<ConfigInfo>emptyList();
/* 174 */           name = getDiskLabel(identity, 
/* 175 */               devices, 
/* 176 */               snapshotConfig);
/* 177 */           iconId = "disk-icon";
/*     */           break;
/*     */         case vdisk_snapshot:
/* 180 */           name = Utils.getLocalizedString("vsan.virtualObjects.vmSnapshot");
/* 181 */           iconId = "disk-icon";
/*     */           break;
/*     */         case vmswap:
/* 184 */           name = Utils.getLocalizedString("vsan.virtualObjects.vmSwap");
/*     */           break;
/*     */         case vmem:
/* 187 */           name = Utils.getLocalizedString("vsan.virtualObjects.vmMemory");
/*     */           break;
/*     */       } 
/*     */       
/* 191 */       if (name == null || identity.vm == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 195 */       VirtualObjectModel vmObject = fromObjInfo(objInfoByVsanUuid.get(identity.uuid), identity.uuid);
/* 196 */       vmObject.name = name;
/* 197 */       vmObject.iconId = iconId;
/* 198 */       if (vmObject.healthState == null)
/*     */       {
/* 200 */         vmObject.healthState = VsanObjectHealthState.INACCESSIBLE;
/*     */       }
/* 202 */       hashMultimap.put(identity.vm, vmObject);
/*     */     } 
/*     */     
/* 205 */     List<VirtualObjectModel> vmModels = new ArrayList<>();
/* 206 */     for (ManagedObjectReference vmRef : vmRefs) {
/* 207 */       VirtualObjectModel vmModel = new VirtualObjectModel();
/* 208 */       vmModel.vmRef = vmRef;
/* 209 */       vmModel.applicableFilter = VirtualObjectsFilter.VMS;
/* 210 */       vmModel.iconId = (String)((Map)dsProperties.get(vmRef)).get("primaryIconId");
/* 211 */       vmModel.name = (String)((Map)dsProperties.get(vmRef)).get("name");
/*     */       
/* 213 */       vmModel.children = (VirtualObjectModel[])hashMultimap.get(vmRef).toArray((Object[])new VirtualObjectModel[hashMultimap.get(vmRef).size()]);
/* 214 */       Arrays.sort(vmModel.children, COMPARATOR);
/*     */ 
/*     */       
/* 217 */       vmModel.healthState = VsanObjectHealthState.HEALTHY; byte b; int i; VirtualObjectModel[] arrayOfVirtualObjectModel;
/* 218 */       for (i = (arrayOfVirtualObjectModel = vmModel.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/* 219 */         if (child.healthState.ordinal() > vmModel.healthState.ordinal()) {
/* 220 */           vmModel.healthState = child.healthState;
/*     */         }
/*     */         
/*     */         b++; }
/*     */       
/* 225 */       vmModel.dataProtectionHealthState = null;
/* 226 */       for (i = (arrayOfVirtualObjectModel = vmModel.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/* 227 */         if (vmModel.dataProtectionHealthState == null)
/*     */         {
/* 229 */           vmModel.dataProtectionHealthState = child.dataProtectionHealthState;
/*     */         }
/* 231 */         if (child.dataProtectionHealthState != null && 
/* 232 */           child.dataProtectionHealthState.ordinal() > vmModel.dataProtectionHealthState.ordinal()) {
/* 233 */           vmModel.dataProtectionHealthState = child.dataProtectionHealthState;
/*     */         }
/*     */         b++; }
/*     */       
/* 237 */       vmModels.add(vmModel);
/*     */     } 
/*     */     
/* 240 */     Collections.sort(vmModels, COMPARATOR);
/* 241 */     return vmModels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<VirtualObjectModel> buildOthers(Set<String> allVsanUuids, VsanObjectIdentityAndHealth identities, VsanObjectInformation[] vsanObjectInformations) {
/* 248 */     Map<String, VsanObjectIdentity> objIdentityByVsanUuid = mapObjIdentities(identities);
/* 249 */     Map<String, VsanObjectInformation> objInfoByVsanUuid = mapObjInfos(vsanObjectInformations);
/*     */     
/* 251 */     List<VirtualObjectModel> models = new ArrayList<>();
/*     */     
/* 253 */     for (String vsanUuid : allVsanUuids) {
/* 254 */       if (!isOtherType(vsanUuid, objIdentityByVsanUuid.get(vsanUuid))) {
/*     */         continue;
/*     */       }
/* 257 */       VirtualObjectModel model = fromObjInfo(objInfoByVsanUuid.get(vsanUuid), vsanUuid);
/*     */       
/* 259 */       if (objIdentityByVsanUuid.containsKey(vsanUuid)) {
/* 260 */         model.name = ((VsanObjectIdentity)objIdentityByVsanUuid.get(vsanUuid)).description;
/*     */       }
/*     */       
/* 263 */       models.add(model);
/*     */     } 
/*     */     
/* 266 */     Collections.sort(models, COMPARATOR);
/* 267 */     return models;
/*     */   }
/*     */   
/*     */   public static VirtualDisk findDisk(VirtualDevice[] virtualDevices, String diskId) {
/* 271 */     if (virtualDevices == null)
/* 272 */       return null;  byte b; int i;
/*     */     VirtualDevice[] arrayOfVirtualDevice;
/* 274 */     for (i = (arrayOfVirtualDevice = virtualDevices).length, b = 0; b < i; ) { VirtualDevice device = arrayOfVirtualDevice[b];
/* 275 */       if (device instanceof VirtualDisk) {
/*     */ 
/*     */         
/* 278 */         VirtualDisk disk = (VirtualDisk)device;
/* 279 */         VirtualDevice.FileBackingInfo backing = findBacking(disk, diskId);
/* 280 */         if (backing != null)
/* 281 */           return disk; 
/*     */       }  b++; }
/*     */     
/* 284 */     return null;
/*     */   }
/*     */   
/*     */   private static VirtualDevice.FileBackingInfo findBacking(VirtualDisk disk, String backingId) {
/* 288 */     if (!(disk.backing instanceof VirtualDevice.FileBackingInfo))
/*     */     {
/* 290 */       return null;
/*     */     }
/*     */     
/* 293 */     VirtualDevice.FileBackingInfo backing = (VirtualDevice.FileBackingInfo)disk.getBacking();
/* 294 */     return backingId.equals(backing.backingObjectId) ? backing : null;
/*     */   }
/*     */   
/*     */   private static VirtualObjectModel fromObjInfo(VsanObjectInformation objInfo, String vsanUuid) {
/* 298 */     VirtualObjectModel model = new VirtualObjectModel();
/* 299 */     model.uid = vsanUuid;
/* 300 */     model.name = vsanUuid;
/* 301 */     if (objInfo != null) {
/* 302 */       model.storagePolicy = objInfo.spbmProfileUuid;
/* 303 */       model.healthState = VsanObjectHealthState.fromString(objInfo.vsanHealth);
/* 304 */       if (objInfo.vsanDataProtectionHealth != null) {
/* 305 */         model.dataProtectionHealthState = VsanObjectDataProtectionHealthState.fromString(objInfo.vsanDataProtectionHealth);
/*     */       }
/*     */     } 
/* 308 */     return model;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<String, VsanObjectIdentity> mapObjIdentities(VsanObjectIdentityAndHealth identities) {
/* 314 */     Map<String, ManagedObjectReference> vmByVsanUuid = new HashMap<>();
/* 315 */     Map<String, VsanObjectIdentity> objIdentityByVsanUuid = new HashMap<>();
/* 316 */     if (!ArrayUtils.isEmpty((Object[])identities.identities)) {
/* 317 */       byte b; int i; VsanObjectIdentity[] arrayOfVsanObjectIdentity; for (i = (arrayOfVsanObjectIdentity = identities.identities).length, b = 0; b < i; ) { VsanObjectIdentity identity = arrayOfVsanObjectIdentity[b];
/* 318 */         if (identity.vm != null)
/*     */         {
/*     */           
/* 321 */           vmByVsanUuid.put(identity.uuid, identity.vm); } 
/*     */         b++; }
/*     */       
/* 324 */       for (i = (arrayOfVsanObjectIdentity = identities.identities).length, b = 0; b < i; ) { VsanObjectIdentity identity = arrayOfVsanObjectIdentity[b];
/* 325 */         if (objIdentityByVsanUuid.containsKey(identity.uuid)) {
/* 326 */           VsanObjectType type = VsanObjectType.parse(identity.type);
/* 327 */           if (type != VsanObjectType.improvedVirtualDisk) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 336 */         identity.vm = vmByVsanUuid.get(identity.uuid);
/* 337 */         objIdentityByVsanUuid.put(identity.uuid, identity); continue; b++; }
/*     */     
/*     */     } 
/* 340 */     return objIdentityByVsanUuid;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, VsanObjectInformation> mapObjInfos(VsanObjectInformation[] vsanObjectInformations) {
/* 345 */     Map<String, VsanObjectInformation> objInfoByVsanUuid = new HashMap<>(); byte b; int i; VsanObjectInformation[] arrayOfVsanObjectInformation;
/* 346 */     for (i = (arrayOfVsanObjectInformation = vsanObjectInformations).length, b = 0; b < i; ) { VsanObjectInformation info = arrayOfVsanObjectInformation[b];
/* 347 */       objInfoByVsanUuid.put(info.vsanObjectUuid, info); b++; }
/*     */     
/* 349 */     return objInfoByVsanUuid;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOtherType(String vsanUuid, VsanObjectIdentity identity) {
/* 355 */     if (identity == null) {
/* 356 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 360 */     if (identity.vm != null) {
/* 361 */       return false;
/*     */     }
/*     */     
/* 364 */     VsanObjectType type = VsanObjectType.parse(identity.type);
/*     */ 
/*     */ 
/*     */     
/* 368 */     return (type != VsanObjectType.iscsiLun && 
/* 369 */       type != VsanObjectType.iscsiTarget && 
/* 370 */       type != VsanObjectType.improvedVirtualDisk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getDiskLabel(VsanObjectIdentity identity, VirtualDevice[] devices, Collection<ConfigInfo> configSnapshots) {
/* 377 */     String uuid = identity.getUuid();
/*     */     
/* 379 */     VirtualDisk disk = findDisk(devices, uuid);
/* 380 */     if (disk != null) {
/* 381 */       return disk.deviceInfo.label;
/*     */     }
/* 383 */     for (ConfigInfo configSnapshot : configSnapshots) {
/* 384 */       disk = findDisk(configSnapshot.hardware.device, uuid);
/* 385 */       if (disk == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 389 */       String path = ((VirtualDevice.FileBackingInfo)disk.backing).fileName;
/* 390 */       int lastSeparator = path.lastIndexOf('/');
/* 391 */       if (lastSeparator != -1) {
/* 392 */         path = path.substring(lastSeparator + 1);
/*     */       }
/* 394 */       return Utils.getLocalizedString("vsan.virtualObjects.vmSnapshot", new String[] { disk.deviceInfo.label, path });
/*     */     } 
/* 396 */     return identity.description;
/*     */   }
/*     */   
/* 399 */   public static final Comparator<VirtualObjectModel> COMPARATOR = new Comparator<VirtualObjectModel>()
/*     */     {
/*     */       public int compare(VirtualObjectModel o1, VirtualObjectModel o2) {
/* 402 */         return o1.name.compareTo(o2.name);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 408 */     if (this == o) {
/* 409 */       return true;
/*     */     }
/*     */     
/* 412 */     if (!(o instanceof VirtualObjectModel)) {
/* 413 */       return false;
/*     */     }
/*     */     
/* 416 */     VirtualObjectModel that = (VirtualObjectModel)o;
/* 417 */     if (this.uid == null) {
/* 418 */       return Objects.equals(this.name, that.name);
/*     */     }
/* 420 */     return Objects.equals(this.uid, that.uid);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 425 */     if (this.uid == null) {
/* 426 */       return Objects.hash(new Object[] { this.name });
/*     */     }
/* 428 */     return Objects.hash(new Object[] { this.uid });
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VirtualObjectModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */