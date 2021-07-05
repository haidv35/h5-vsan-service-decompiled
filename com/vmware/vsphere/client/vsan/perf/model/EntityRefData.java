/*     */ package com.vmware.vsphere.client.vsan.perf.model;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityInfo;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
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
/*     */ @data
/*     */ public class EntityRefData
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public String entityRefId;
/*     */   public String metricName;
/*     */   public PerformanceObjectType performanceObjectType;
/*     */   public String objectName;
/*     */   public ManagedObjectReference managedObjectRef;
/*     */   public String managedObjectName;
/*     */   public String vsanUuid;
/*     */   public boolean isEntityMissing = false;
/*     */   
/*     */   public EntityRefData() {}
/*     */   
/*     */   public EntityRefData(VsanPerfEntityInfo entityInfo, ManagedObjectReference clusterRef) {
/*  60 */     this.entityRefId = entityInfo.entityRefId;
/*  61 */     this.objectName = entityInfo.entityName;
/*  62 */     this.performanceObjectType = getPerformanceObjectType(entityInfo.entityRefType);
/*     */     
/*  64 */     if (StringUtils.isEmpty(entityInfo.entityRelatedMoRef)) {
/*  65 */       if (isClusterData(this.performanceObjectType)) {
/*  66 */         this.managedObjectRef = clusterRef;
/*     */       } else {
/*  68 */         this.isEntityMissing = true;
/*     */       } 
/*     */     } else {
/*  71 */       this.managedObjectRef = BaseUtils.generateMor(entityInfo.entityRelatedMoRef, 
/*  72 */           clusterRef.getServerGuid());
/*     */     } 
/*     */     
/*  75 */     if (StringUtils.isEmpty(this.entityRefId)) {
/*     */       return;
/*     */     }
/*  78 */     String[] parts = this.entityRefId.split(":");
/*  79 */     if (parts == null || parts.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     this.metricName = parts[0];
/*  84 */     this.vsanUuid = parts[1];
/*     */   }
/*     */   
/*     */   private PerformanceObjectType getPerformanceObjectType(String entityRefType) {
/*  88 */     PerformanceObjectType result = null; String str;
/*  89 */     switch ((str = entityRefType).hashCode()) { case -1775894832: if (!str.equals("capacity-disk")) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/*  94 */         result = PerformanceObjectType.capacityDisk;
/*     */         break;
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
/*     */       case -1229779238:
/*     */         if (!str.equals("cluster-domcompmgr")) {
/*     */           break;
/*     */         }
/* 128 */         result = PerformanceObjectType.clusterBackend; break;case -1114158933: if (!str.equals("vsan-pnic-net")) break;  result = PerformanceObjectType.hostPnic; break;case -982244677: if (!str.equals("vsan-host-net"))
/*     */           break;  result = PerformanceObjectType.hostNet; break;case -712982753: if (!str.equals("virtual-disk"))
/*     */           break;  result = PerformanceObjectType.virtualDisk; break;case -624594652: if (!str.equals("cluster-domowner"))
/* 131 */           break;  result = PerformanceObjectType.clusterDomOwner; break;case -17571576: if (!str.equals("cache-disk"))
/*     */           break;  result = PerformanceObjectType.cacheDisk; break;
/*     */       case 94783762: if (!str.equals("cmmds"))
/* 134 */           break;  result = PerformanceObjectType.cmmds; break;case 112500252: if (!str.equals("vscsi")) break;  result = PerformanceObjectType.vscsi; break;case 594137398: if (!str.equals("host-domowner")) break;  result = PerformanceObjectType.hostBackend; break;case 752768485: if (!str.equals("vsan-vnic-net"))
/*     */           break;  result = PerformanceObjectType.hostVnic; break;case 884532648: if (!str.equals("host-domclient"))
/*     */           break;  result = PerformanceObjectType.hostVmConsumption; break;case 1032473077: if (!str.equals("clom-disk-stats"))
/* 137 */           break;  result = PerformanceObjectType.clomDiskStats; break;case 1439608399: if (!str.equals("disk-group"))
/*     */           break;  result = PerformanceObjectType.diskGroup; break;case 1592855429: if (!str.equals("virtual-machine"))
/*     */           break;  result = PerformanceObjectType.vm; break;case 1684192704: if (!str.equals("clom-host-stats"))
/* 140 */           break;  result = PerformanceObjectType.clomHostStats; break;case 1740616300: if (!str.equals("host-domcompmgr"))
/*     */           break;  result = PerformanceObjectType.hostBackend; break;
/*     */       case 1758544762: if (!str.equals("cluster-domclient"))
/* 143 */           break;  result = PerformanceObjectType.clusterVmConsumption; break; }  return result;
/*     */   }
/*     */   
/*     */   private boolean isClusterData(PerformanceObjectType objectType) {
/* 147 */     if (objectType == null) {
/* 148 */       return false;
/*     */     }
/* 150 */     return !(!objectType.equals(PerformanceObjectType.clusterBackend) && 
/* 151 */       !objectType.equals(PerformanceObjectType.clusterDomOwner) && 
/* 152 */       !objectType.equals(PerformanceObjectType.clusterVmConsumption));
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/EntityRefData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */