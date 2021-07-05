/*     */ package com.vmware.vsan.client.services.physicaldisks;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.host.ScsiLun;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectModel;
/*     */ import com.vmware.vsphere.client.vsan.data.HostPhysicalMappingData;
/*     */ import com.vmware.vsphere.client.vsan.data.PhysicalDiskData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @data
/*     */ public class PhysicalDisksHostData {
/*  24 */   private static final Logger logger = LoggerFactory.getLogger(PhysicalDisksHostData.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public String name;
/*     */ 
/*     */ 
/*     */   
/*     */   public String iconId;
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedObjectReference hostRef;
/*     */ 
/*     */ 
/*     */   
/*     */   public String faultDomain;
/*     */ 
/*     */ 
/*     */   
/*     */   public String vsanUuid;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSsd;
/*     */ 
/*     */ 
/*     */   
/*     */   public String vsanDiskGroupUuid;
/*     */ 
/*     */ 
/*     */   
/*     */   public long capacity;
/*     */ 
/*     */ 
/*     */   
/*     */   public long usedCapacity;
/*     */ 
/*     */ 
/*     */   
/*     */   public long reservedCapacity;
/*     */ 
/*     */ 
/*     */   
/*     */   public DeviceState state;
/*     */ 
/*     */   
/*     */   public boolean ineligible;
/*     */ 
/*     */   
/*     */   public String[] issues;
/*     */ 
/*     */   
/*     */   public boolean isCacheDisk;
/*     */ 
/*     */   
/*     */   public long diskHealthFlag;
/*     */ 
/*     */   
/*     */   public long componentsNumber;
/*     */ 
/*     */   
/*     */   public String[] physicalLocation;
/*     */ 
/*     */   
/*     */   public List<PhysicalDisksHostData> physicalDisks;
/*     */ 
/*     */   
/*  92 */   private List<String> virtualObjectUuids = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public List<VirtualObjectModel> virtualObjecs = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] deviceAdaptersData;
/*     */ 
/*     */ 
/*     */   
/*     */   public PhysicalDisksHostData() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public PhysicalDisksHostData(HostPhysicalMappingData item) {
/* 110 */     this.name = item.name;
/* 111 */     this.iconId = item.primaryIconId;
/* 112 */     this.hostRef = item.hostRef;
/* 113 */     this.faultDomain = item.faultDomain;
/* 114 */     this.physicalDisks = new ArrayList<>();
/* 115 */     this.capacity = 0L;
/* 116 */     this.usedCapacity = 0L;
/* 117 */     this.reservedCapacity = 0L;
/* 118 */     this.componentsNumber = 0L;
/* 119 */     this.deviceAdaptersData = item.storageAdapterDevices;
/* 120 */     if (CollectionUtils.isNotEmpty(item.physicalDisks)) {
/* 121 */       for (PhysicalDiskData physicalDisk : item.physicalDisks) {
/* 122 */         PhysicalDisksHostData diskItem = new PhysicalDisksHostData();
/* 123 */         diskItem.name = physicalDisk.diskName;
/* 124 */         diskItem.vsanDiskGroupUuid = physicalDisk.vsanDiskGroupUuid;
/* 125 */         diskItem.vsanUuid = physicalDisk.uuid;
/* 126 */         diskItem.isSsd = physicalDisk.isSsd.booleanValue();
/* 127 */         diskItem.capacity = physicalDisk.capacity;
/* 128 */         diskItem.usedCapacity = parseLong(physicalDisk.usedCapacity);
/* 129 */         diskItem.reservedCapacity = parseLong(physicalDisk.reservedCapacity);
/* 130 */         diskItem.isCacheDisk = physicalDisk.isCacheDisk;
/*     */ 
/*     */         
/* 133 */         if (!physicalDisk.isCacheDisk) {
/* 134 */           this.capacity += diskItem.capacity;
/* 135 */           this.usedCapacity += diskItem.usedCapacity;
/* 136 */           this.reservedCapacity += diskItem.reservedCapacity;
/*     */         } 
/* 138 */         diskItem.state = DeviceState.fromScsiState(physicalDisk.operationalState);
/* 139 */         diskItem.ineligible = physicalDisk.ineligible.booleanValue();
/* 140 */         if (physicalDisk.diskIssue != null) {
/* 141 */           diskItem.issues = new String[] { physicalDisk.diskIssue };
/*     */         }
/* 143 */         this.diskHealthFlag = parseLong(physicalDisk.diskHealthFlag);
/* 144 */         if (physicalDisk.virtualDiskUuids != null) {
/* 145 */           diskItem.componentsNumber = physicalDisk.virtualDiskUuids.size();
/* 146 */           diskItem.physicalLocation = physicalDisk.physicalLocation;
/* 147 */           this.componentsNumber += physicalDisk.virtualDiskUuids.size();
/* 148 */           this.virtualObjectUuids.addAll(physicalDisk.virtualDiskUuids);
/* 149 */           diskItem.virtualObjectUuids = physicalDisk.virtualDiskUuids;
/*     */         } 
/*     */         
/* 152 */         this.physicalDisks.add(diskItem);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getVirtualObjectUuids() {
/* 158 */     return this.virtualObjectUuids;
/*     */   }
/*     */   
/*     */   public void setVirtualObjectsData(List<VirtualObjectModel> virtualObjects) {
/* 162 */     if (CollectionUtils.isEmpty(virtualObjects)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     Map<VirtualObjectModel, VirtualObjectModel> virtualObjectsMap = new LinkedHashMap<>();
/*     */     
/* 171 */     for (PhysicalDisksHostData diskData : this.physicalDisks) {
/* 172 */       if (CollectionUtils.isEmpty(diskData.virtualObjectUuids)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 178 */       Set<String> diskObjectUuids = new HashSet<>(diskData.virtualObjectUuids);
/*     */       
/* 180 */       for (VirtualObjectModel virtualObject : virtualObjects) {
/* 181 */         VirtualObjectModel clone = prepareCloneVirtualObject(virtualObject, diskObjectUuids);
/*     */         
/* 183 */         if (clone == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 189 */         diskData.virtualObjecs.add(clone);
/*     */ 
/*     */ 
/*     */         
/* 193 */         if (virtualObjectsMap.containsKey(virtualObject)) {
/*     */           
/* 195 */           VirtualObjectModel vom = virtualObjectsMap.get(virtualObject);
/* 196 */           vom.mergeChildren(clone);
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 201 */         virtualObjectsMap.put(virtualObject, clone.cloneWithChildren());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 206 */     this.virtualObjecs = new ArrayList<>(virtualObjectsMap.values());
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
/*     */   
/*     */   private static VirtualObjectModel prepareCloneVirtualObject(VirtualObjectModel virtualObject, Collection<String> diskObjectUuids) {
/* 220 */     VirtualObjectModel clone = virtualObject.cloneWithoutChildren();
/*     */ 
/*     */ 
/*     */     
/* 224 */     if (ArrayUtils.isNotEmpty((Object[])virtualObject.children)) {
/* 225 */       List<VirtualObjectModel> children = new ArrayList<>(virtualObject.children.length); byte b; int i; VirtualObjectModel[] arrayOfVirtualObjectModel;
/* 226 */       for (i = (arrayOfVirtualObjectModel = virtualObject.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/* 227 */         if (diskObjectUuids.contains(child.uid)) {
/* 228 */           children.add(child);
/*     */         }
/*     */         b++; }
/*     */       
/* 232 */       clone.children = children.<VirtualObjectModel>toArray(new VirtualObjectModel[children.size()]);
/*     */     } 
/*     */ 
/*     */     
/* 236 */     if ((StringUtils.isNotEmpty(clone.uid) && diskObjectUuids.contains(clone.uid)) || 
/* 237 */       ArrayUtils.isNotEmpty((Object[])clone.children)) {
/* 238 */       return clone;
/*     */     }
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseLong(String value) {
/*     */     try {
/* 249 */       return Long.parseLong(value);
/* 250 */     } catch (Exception exception) {
/* 251 */       logger.warn("Cannot parse to long. Probably the disk is absent: " + value);
/* 252 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @data
/*     */   public enum DeviceState
/*     */   {
/* 262 */     OK, OFF, LOST, ERROR, UNKNOWN;
/*     */     
/*     */     public static DeviceState fromScsiState(String[] stateKeys) {
/* 265 */       Set<ScsiLun.State> states = new HashSet<>(); byte b; int i; String[] arrayOfString;
/* 266 */       for (i = (arrayOfString = stateKeys).length, b = 0; b < i; ) { String key = arrayOfString[b];
/* 267 */         states.add(ScsiLun.State.valueOf(key)); b++; }
/*     */       
/* 269 */       if (states.contains(ScsiLun.State.ok)) {
/* 270 */         return OK;
/*     */       }
/* 272 */       if (states.contains(ScsiLun.State.off)) {
/* 273 */         return OFF;
/*     */       }
/* 275 */       if (states.contains(ScsiLun.State.lostCommunication)) {
/* 276 */         return LOST;
/*     */       }
/* 278 */       if (states.contains(ScsiLun.State.error)) {
/* 279 */         return ERROR;
/*     */       }
/*     */ 
/*     */       
/* 283 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/physicaldisks/PhysicalDisksHostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */