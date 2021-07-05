/*     */ package com.vmware.vsphere.client.vsan.data;
/*     */ 
/*     */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.node.ArrayNode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @data
/*     */ public class PhysicalDiskData
/*     */   extends DataObjectImpl
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String DISK_USED_CAPACITY_KEY = "capacityUsed";
/*     */   private static final String DISK_RESERVED_CAPACITY_KEY = "capacityReserved";
/*     */   private static final String DISK_HEALTH_KEY = "disk_health";
/*     */   private static final String DISK_HEALTH_FLAGS_KEY = "healthFlags";
/*     */   private static final String LSOM_OBJECTS = "lsom_objects";
/*     */   private static final String CONTENT = "content";
/*     */   private static final String COMPOSITE_UUID = "compositeUuid";
/*     */   public ManagedObjectReference hostRef;
/*     */   public ManagedObjectReference clusterRef;
/*     */   public String diskName;
/*     */   public String uuid;
/*     */   public long capacity;
/*     */   public String[] operationalState;
/*     */   public Boolean ineligible;
/*     */   public String diskIssue;
/*     */   public Boolean isSsd;
/*     */   public boolean isCacheDisk;
/*     */   public String vsanDiskGroupUuid;
/*     */   public String[] physicalLocation;
/*     */   public String usedCapacity;
/*     */   public String reservedCapacity;
/*     */   public String diskHealthFlag;
/*  93 */   public List<String> virtualDiskUuids = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public PhysicalDiskData() {}
/*     */   
/*     */   public PhysicalDiskData(VsanDiskData diskData, ManagedObjectReference diskHostRef, JsonNode json, ManagedObjectReference clusterRef) {
/*  99 */     this.hostRef = diskHostRef;
/* 100 */     this.clusterRef = clusterRef;
/* 101 */     ScsiDisk disk = diskData.disk;
/* 102 */     this.capacity = BaseUtils.lbaToBytes(disk.capacity);
/* 103 */     this.operationalState = disk.operationalState;
/*     */     
/* 105 */     this.ineligible = Boolean.valueOf(diskData.ineligible);
/* 106 */     if (this.ineligible.booleanValue()) {
/* 107 */       if (!StringUtils.isEmpty(diskData.stateReason)) {
/* 108 */         this.diskIssue = diskData.stateReason;
/*     */       }
/* 110 */     } else if (!ArrayUtils.isEmpty((Object[])diskData.issues)) {
/* 111 */       this.diskIssue = diskData.issues[0];
/*     */     } 
/*     */     
/* 114 */     this.isSsd = disk.ssd;
/* 115 */     this.isCacheDisk = diskData.isCacheDisk;
/* 116 */     this.diskName = getDiskName(disk);
/* 117 */     this.uuid = disk.uuid;
/* 118 */     this.vsanDiskGroupUuid = diskData.diskGroupUuid;
/* 119 */     this.physicalLocation = disk.physicalLocation;
/* 120 */     JsonNode contentRoot = json.get(diskData.vsanUuid);
/*     */     
/* 122 */     if (contentRoot != null) {
/* 123 */       this.usedCapacity = contentRoot.get("capacityUsed").toString();
/* 124 */       this.reservedCapacity = contentRoot.get("capacityReserved").toString();
/*     */       
/* 126 */       JsonNode diskHealthNode = contentRoot.get("disk_health");
/* 127 */       this.diskHealthFlag = diskHealthNode.get("healthFlags").toString();
/*     */       
/* 129 */       JsonNode lsomObjects = contentRoot.get("lsom_objects");
/* 130 */       if (lsomObjects instanceof ArrayNode) {
/* 131 */         ArrayNode lsomArray = (ArrayNode)lsomObjects;
/* 132 */         Iterator<JsonNode> iterator = lsomArray.iterator();
/* 133 */         while (iterator.hasNext()) {
/* 134 */           JsonNode lsomObjectNode = iterator.next();
/* 135 */           JsonNode contentNode = lsomObjectNode.get("content");
/* 136 */           if (contentNode == null) {
/*     */             continue;
/*     */           }
/* 139 */           JsonNode compositeUuidNode = contentNode.get("compositeUuid");
/* 140 */           if (compositeUuidNode == null) {
/*     */             continue;
/*     */           }
/* 143 */           this.virtualDiskUuids.add(compositeUuidNode.asText());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getDiskName(ScsiDisk disk) {
/* 150 */     if (disk.displayName != null) {
/* 151 */       return disk.displayName;
/*     */     }
/* 153 */     if (disk.canonicalName != null) {
/* 154 */       return disk.canonicalName;
/*     */     }
/* 156 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/PhysicalDiskData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */