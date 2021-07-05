/*     */ package com.vmware.vsan.client.services.virtualobjects;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vsan.client.services.common.data.VmData;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectsFilter;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VmObjectsData;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanVmObject;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectType;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VsanVirtualDisksDataProvider
/*     */ {
/*     */   @Autowired
/*     */   private VsanVirtualObjectsProvider vsanVirtualObjectsProvider;
/*     */   @Autowired
/*     */   private VsanObjectSystemProvider vsanObjectSystemProvider;
/*  36 */   private static final Log _logger = LogFactory.getLog(VsanVirtualDisksDataProvider.class);
/*  37 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanVirtualDisksDataProvider.class);
/*     */   
/*  39 */   private static final String[] DISK_MAPPINGS_VM_PROPERTIES = new String[] {
/*  40 */       "name", 
/*  41 */       "primaryIconId", 
/*  42 */       "config.hardware.device", 
/*  43 */       "summary.config.vmPathName"
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmObjectsData[] getVirtualDiskObjectData(ManagedObjectReference clusterRef, VirtualObjectsFilter filter) throws Exception {
/*  54 */     if (!VsanCapabilityUtils.isObjectSystemSupported(clusterRef)) {
/*  55 */       return new VmObjectsData[0];
/*     */     }
/*     */     
/*  58 */     _logger.info("Getting virtual objects using the object uuids on all the physical disks.");
/*     */     
/*  60 */     Set<String> vsanObjectUuids = this.vsanVirtualObjectsProvider.getVirtualObjectsUuids(clusterRef);
/*  61 */     if (ArrayUtils.isEmpty(vsanObjectUuids.toArray()))
/*     */     {
/*  63 */       return new VmObjectsData[0];
/*     */     }
/*     */     
/*  66 */     return getVirtualDiskObjectDataByFilterAndUuids(clusterRef, filter, vsanObjectUuids);
/*     */   }
/*     */ 
/*     */   
/*     */   public VmObjectsData[] getAllVirtualDiskObjectDataByUuids(ManagedObjectReference clusterRef, Set<String> vsanObjectUuids) throws Exception {
/*  71 */     if (!VsanCapabilityUtils.isObjectSystemSupported(clusterRef)) {
/*  72 */       return new VmObjectsData[0];
/*     */     }
/*     */     
/*  75 */     if (ArrayUtils.isEmpty(vsanObjectUuids.toArray()))
/*     */     {
/*  77 */       return new VmObjectsData[0];
/*     */     }
/*     */     
/*  80 */     return getVirtualDiskObjectDataByFilterAndUuids(clusterRef, null, vsanObjectUuids);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VmObjectsData[] getVirtualDiskObjectDataByFilterAndUuids(ManagedObjectReference clusterRef, VirtualObjectsFilter filter, Set<String> vsanObjectUuids) {
/*  86 */     Map<VirtualObjectsFilter, List<VsanObjectIdentity>> vsanObjectIdentities = 
/*  87 */       getVsanObjectIdentityData(clusterRef, vsanObjectUuids);
/*     */ 
/*     */     
/*  90 */     Map<ManagedObjectReference, VmData> vmDataMap = getVmData(clusterRef, 
/*  91 */         vsanObjectIdentities.get(VirtualObjectsFilter.VMS));
/*     */ 
/*     */     
/*  94 */     Map<String, VsanObjectHealthData> vsanHealthData = this.vsanObjectSystemProvider.getVsanHealthData(
/*  95 */         clusterRef, vsanObjectUuids);
/*     */ 
/*     */     
/*  98 */     List<VmObjectsData> vmObjectsData = mergeVsanVmDataResults(
/*  99 */         vmDataMap, vsanObjectIdentities, vsanHealthData, vsanObjectUuids, null);
/*     */     
/* 101 */     return vmObjectsData.<VmObjectsData>toArray(new VmObjectsData[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<VirtualObjectsFilter, List<VsanObjectIdentity>> getVsanObjectIdentityData(ManagedObjectReference clusterRef, Set<String> vsanObjectUuids) {
/* 107 */     VsanObjectIdentity[] vsanObjectIdentities = this.vsanObjectSystemProvider.getObjectIdentities(clusterRef, vsanObjectUuids);
/*     */     
/* 109 */     Map<VirtualObjectsFilter, List<VsanObjectIdentity>> result = initVsanObjectIdentitiesMap();
/* 110 */     if (vsanObjectIdentities == null)
/* 111 */       return result; 
/*     */     byte b;
/*     */     int i;
/*     */     VsanObjectIdentity[] arrayOfVsanObjectIdentity1;
/* 115 */     for (i = (arrayOfVsanObjectIdentity1 = vsanObjectIdentities).length, b = 0; b < i; ) { VsanObjectIdentity objectIdentity = arrayOfVsanObjectIdentity1[b];
/* 116 */       if (isVmObject(objectIdentity)) {
/* 117 */         ((List<VsanObjectIdentity>)result.get(VirtualObjectsFilter.VMS)).add(objectIdentity);
/* 118 */       } else if (isIsciObject(objectIdentity)) {
/* 119 */         ((List<VsanObjectIdentity>)result.get(VirtualObjectsFilter.ISCSI_TARGETS)).add(objectIdentity);
/*     */       } else {
/* 121 */         ((List<VsanObjectIdentity>)result.get(VirtualObjectsFilter.OTHERS)).add(objectIdentity);
/*     */       }  b++; }
/*     */     
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   private Map<VirtualObjectsFilter, List<VsanObjectIdentity>> initVsanObjectIdentitiesMap() {
/* 128 */     Map<VirtualObjectsFilter, List<VsanObjectIdentity>> result = new HashMap<>();
/* 129 */     result.put(VirtualObjectsFilter.VMS, new ArrayList<>());
/* 130 */     result.put(VirtualObjectsFilter.ISCSI_TARGETS, new ArrayList<>());
/* 131 */     result.put(VirtualObjectsFilter.OTHERS, new ArrayList<>());
/* 132 */     return result;
/*     */   }
/*     */   
/*     */   private boolean isVmObject(VsanObjectIdentity vsanObjectIdentity) {
/* 136 */     return (vsanObjectIdentity.vm != null);
/*     */   }
/*     */   
/*     */   private boolean isIsciObject(VsanObjectIdentity vsanObjectIdentity) {
/* 140 */     return vsanObjectIdentity.type.startsWith("iscsi");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<ManagedObjectReference, VmData> getVmData(ManagedObjectReference clusterRef, List<VsanObjectIdentity> vsanObjectIdentities) {
/* 147 */     Set<ManagedObjectReference> vmRefs = getVmsFromVsanObjects(clusterRef, vsanObjectIdentities);
/*     */     
/* 149 */     if (vmRefs == null || vmRefs.size() == 0) {
/* 150 */       return new HashMap<>();
/*     */     }
/*     */     
/* 153 */     try { Exception exception1 = null, exception2 = null;
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
/*     */       try {  }
/*     */       finally
/* 189 */       { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }  }  } catch (Exception ex)
/* 190 */     { _logger.error("Failed to retrieve VM properties: ", ex);
/*     */       
/* 192 */       return new HashMap<>(); }
/*     */   
/*     */   }
/*     */   
/*     */   private Set<ManagedObjectReference> getVmsFromVsanObjects(ManagedObjectReference clusterRef, List<VsanObjectIdentity> vsanObjectIdentities) {
/* 197 */     Set<ManagedObjectReference> vmRefs = new HashSet<>();
/* 198 */     if (vsanObjectIdentities != null) {
/* 199 */       for (VsanObjectIdentity identity : vsanObjectIdentities) {
/*     */         
/* 201 */         if (identity.vm == null) {
/*     */           continue;
/*     */         }
/* 204 */         ManagedObjectReference vmRef = identity.vm;
/* 205 */         VmodlHelper.assignServerGuid(vmRef, clusterRef.getServerGuid());
/* 206 */         vmRefs.add(vmRef);
/*     */       } 
/*     */     }
/* 209 */     return vmRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<VmObjectsData> mergeVsanVmDataResults(Map<ManagedObjectReference, VmData> vmDataMap, Map<VirtualObjectsFilter, List<VsanObjectIdentity>> vsanObjectIdentities, Map<String, VsanObjectHealthData> vsanHealthData, Set<String> vsanObjectUuids, VirtualObjectsFilter filter) {
/* 220 */     List<VmObjectsData> result = new ArrayList<>();
/*     */ 
/*     */     
/* 223 */     if (filter == null || filter == VirtualObjectsFilter.VMS) {
/* 224 */       Map<ManagedObjectReference, VmObjectsData> vmObjectsDataMap = new HashMap<>();
/* 225 */       List<VsanObjectIdentity> vmObjectIdentities = vsanObjectIdentities.get(VirtualObjectsFilter.VMS);
/* 226 */       for (VsanObjectIdentity vmIdentityData : vmObjectIdentities) {
/* 227 */         updateVmObjectData(vmDataMap, vmIdentityData, vsanHealthData, vmObjectsDataMap);
/*     */       }
/* 229 */       result.addAll(vmObjectsDataMap.values());
/*     */     } 
/*     */     
/* 232 */     if (filter == null || filter == VirtualObjectsFilter.ISCSI_TARGETS) {
/* 233 */       List<VmObjectsData> iscsiObjectsData = new ArrayList<>();
/* 234 */       List<VsanObjectIdentity> iscsiObjectIdentities = vsanObjectIdentities.get(VirtualObjectsFilter.ISCSI_TARGETS);
/* 235 */       for (VsanObjectIdentity iscsiIdentityData : iscsiObjectIdentities) {
/* 236 */         updateOtherObjectData(iscsiIdentityData, vsanHealthData, iscsiObjectsData);
/*     */       }
/* 238 */       result.addAll(iscsiObjectsData);
/*     */     } 
/*     */ 
/*     */     
/* 242 */     List<VmObjectsData> otherObjectsData = new ArrayList<>();
/* 243 */     List<VmObjectsData> unassociatedObjectsData = new ArrayList<>();
/* 244 */     if (filter == null || filter == VirtualObjectsFilter.OTHERS) {
/* 245 */       List<VsanObjectIdentity> otherObjectIdentities = vsanObjectIdentities.get(VirtualObjectsFilter.OTHERS);
/* 246 */       for (VsanObjectIdentity objectIdentityData : otherObjectIdentities) {
/* 247 */         updateOtherObjectData(objectIdentityData, vsanHealthData, otherObjectsData);
/*     */       }
/* 249 */       result.addAll(otherObjectsData);
/*     */ 
/*     */       
/* 252 */       unassociatedObjectsData = getUnassociatedObjectsData(
/* 253 */           vsanObjectUuids, vsanObjectIdentities, vsanHealthData, filter);
/* 254 */       result.addAll(unassociatedObjectsData);
/*     */     } 
/*     */     
/* 257 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateVmObjectData(Map<ManagedObjectReference, VmData> vmDataMap, VsanObjectIdentity vsanObjectIdentity, Map<String, VsanObjectHealthData> vsanHealthData, Map<ManagedObjectReference, VmObjectsData> vmObjectsDataMap) {
/* 265 */     VmData vmData = vmDataMap.get(vsanObjectIdentity.vm);
/* 266 */     VmObjectsData vmObjectsData = vmObjectsDataMap.get(vsanObjectIdentity.vm);
/* 267 */     if (vmObjectsData == null) {
/* 268 */       vmObjectsData = new VmObjectsData(vmData);
/* 269 */       vmObjectsData.vmObjects = new ArrayList();
/* 270 */       vmObjectsDataMap.put(vsanObjectIdentity.vm, vmObjectsData);
/*     */     } 
/* 272 */     VsanVmObject vsanVmObject = new VsanVmObject(vsanObjectIdentity, vmData);
/*     */     
/* 274 */     vsanVmObject.updateHealthData(vsanHealthData);
/*     */     
/* 276 */     vmObjectsData.vmObjects.add(vsanVmObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateOtherObjectData(VsanObjectIdentity vsanObjectData, Map<String, VsanObjectHealthData> vsanHealthData, List<VmObjectsData> otherObjectsData) {
/* 283 */     VmObjectsData otherObject = new VmObjectsData();
/* 284 */     VsanVmObject vsanOtherObject = new VsanVmObject(vsanObjectData);
/* 285 */     vsanOtherObject.name = vsanObjectData.getDescription();
/*     */     
/* 287 */     vsanOtherObject.updateHealthData(vsanHealthData);
/* 288 */     otherObject.vmObjects = new ArrayList();
/* 289 */     otherObject.vmObjects.add(vsanOtherObject);
/* 290 */     otherObjectsData.add(otherObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<VmObjectsData> getUnassociatedObjectsData(Set<String> vsanObjectUuids, Map<VirtualObjectsFilter, List<VsanObjectIdentity>> vsanObjectIdentities, Map<String, VsanObjectHealthData> vsanHealthData, VirtualObjectsFilter filter) {
/* 299 */     List<VmObjectsData> result = new ArrayList<>();
/*     */     
/* 301 */     if (vsanObjectUuids == null || vsanObjectUuids.size() == 0)
/*     */     {
/* 303 */       return result;
/*     */     }
/*     */     
/* 306 */     Set<String> unassociatedObjectIds = new HashSet<>(vsanObjectUuids);
/* 307 */     if (filter == VirtualObjectsFilter.VMS || filter == VirtualObjectsFilter.ISCSI_TARGETS)
/*     */     {
/* 309 */       return result;
/*     */     }
/*     */     
/* 312 */     for (VsanObjectIdentity vmObjectIdentity : vsanObjectIdentities.get(VirtualObjectsFilter.VMS)) {
/* 313 */       unassociatedObjectIds.remove(vmObjectIdentity.getUuid());
/*     */     }
/* 315 */     for (VsanObjectIdentity vmObjectIdentity : vsanObjectIdentities.get(VirtualObjectsFilter.ISCSI_TARGETS)) {
/* 316 */       unassociatedObjectIds.remove(vmObjectIdentity.getUuid());
/*     */     }
/* 318 */     for (VsanObjectIdentity vmObjectIdentity : vsanObjectIdentities.get(VirtualObjectsFilter.OTHERS)) {
/* 319 */       unassociatedObjectIds.remove(vmObjectIdentity.getUuid());
/*     */     }
/*     */ 
/*     */     
/* 323 */     for (String uuid : unassociatedObjectIds) {
/* 324 */       VmObjectsData unassociatedObject = new VmObjectsData();
/* 325 */       VsanObject vsanOtherObject = new VsanObject(uuid);
/* 326 */       vsanOtherObject.name = uuid;
/* 327 */       vsanOtherObject.objectType = VsanObjectType.other;
/*     */       
/* 329 */       unassociatedObject.vmObjects = new ArrayList();
/* 330 */       unassociatedObject.vmObjects.add(vsanOtherObject);
/* 331 */       vsanOtherObject.updateHealthData(vsanHealthData);
/* 332 */       result.add(unassociatedObject);
/*     */     } 
/* 334 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getVmHomeVsanUuid(String vmFilePath) {
/* 344 */     if (vmFilePath == null) {
/* 345 */       return null;
/*     */     }
/* 347 */     int startIndex = vmFilePath.indexOf(']');
/* 348 */     int endIndex = vmFilePath.indexOf('/');
/* 349 */     if (startIndex >= 0 && endIndex > startIndex) {
/* 350 */       return vmFilePath.substring(startIndex + 1, endIndex).trim();
/*     */     }
/* 352 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/VsanVirtualDisksDataProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */