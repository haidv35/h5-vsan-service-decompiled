/*     */ package com.vmware.vsan.client.services.virtualobjects.data;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComponent;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComponentState;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanRaidConfig;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections.CollectionUtils;
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
/*     */ @data
/*     */ public class VirtualObjectPlacementModel
/*     */ {
/*     */   private static final String INVALID_DISK_UUID = "Object not found";
/*     */   public String nodeUuid;
/*     */   public String label;
/*     */   public String iconId;
/*     */   public VsanComponentState state;
/*     */   public VirtualObjectPlacementModel host;
/*     */   public ManagedObjectReference navigationTarget;
/*     */   public String faultDomain;
/*     */   public VirtualObjectPlacementModel cacheDisk;
/*     */   public VirtualObjectPlacementModel capacityDisk;
/*     */   public List<VirtualObjectPlacementModel> children;
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private Map<ManagedObjectReference, Map<String, Object>> hostData;
/*  60 */     private Map<String, DiskResult> diskByUuid = new HashMap<>();
/*     */     
/*     */     public Builder(List<DiskResult> claimedDisks, DataServiceResponse hostData) {
/*  63 */       this.hostData = hostData.getMap();
/*  64 */       for (DiskResult disk : claimedDisks) {
/*  65 */         this.diskByUuid.put(disk.vsanUuid, disk);
/*     */       }
/*     */     }
/*     */     
/*     */     public List<VirtualObjectPlacementModel> build(VsanObject virtualObject) {
/*  70 */       if (virtualObject.rootConfig == null || CollectionUtils.isEmpty(virtualObject.rootConfig.children)) {
/*  71 */         return Collections.emptyList();
/*     */       }
/*     */       
/*  74 */       List<VirtualObjectPlacementModel> models = new ArrayList<>();
/*  75 */       for (VsanComponent component : virtualObject.rootConfig.children) {
/*  76 */         if (component instanceof VsanRaidConfig) {
/*  77 */           models.add(buildRaid((VsanRaidConfig)component)); continue;
/*     */         } 
/*  79 */         models.add(buildComponent(component));
/*     */       } 
/*     */       
/*  82 */       return models;
/*     */     }
/*     */     
/*     */     private VirtualObjectPlacementModel buildRaid(VsanRaidConfig raidConfig) {
/*  86 */       VirtualObjectPlacementModel result = new VirtualObjectPlacementModel();
/*  87 */       result.label = raidConfig.type;
/*  88 */       if (CollectionUtils.isEmpty(raidConfig.children)) {
/*  89 */         return result;
/*     */       }
/*     */       
/*  92 */       result.children = new ArrayList<>();
/*  93 */       for (VsanComponent component : raidConfig.children) {
/*  94 */         if (component instanceof VsanRaidConfig) {
/*  95 */           result.children.add(buildRaid((VsanRaidConfig)component)); continue;
/*     */         } 
/*  97 */         result.children.add(buildComponent(component));
/*     */       } 
/*     */ 
/*     */       
/* 101 */       return result;
/*     */     }
/*     */     
/*     */     private VirtualObjectPlacementModel buildComponent(VsanComponent component) {
/* 105 */       VirtualObjectPlacementModel result = new VirtualObjectPlacementModel();
/* 106 */       result.nodeUuid = component.componentUuid;
/* 107 */       result.label = component.type;
/* 108 */       result.state = component.state;
/* 109 */       result.host = buildHost(component.hostUuid);
/* 110 */       if (!StringUtils.isEmpty(component.cacheDiskUuid) && !"Object not found".equals(component.cacheDiskUuid)) {
/* 111 */         result.cacheDisk = buildDisk(component.cacheDiskUuid);
/*     */       }
/* 113 */       if (!StringUtils.isEmpty(component.capacityDiskUuid) && !"Object not found".equals(component.capacityDiskUuid)) {
/* 114 */         result.capacityDisk = buildDisk(component.capacityDiskUuid);
/*     */       }
/* 116 */       return result;
/*     */     }
/*     */     
/*     */     private VirtualObjectPlacementModel buildHost(String nodeUuid) {
/* 120 */       if (StringUtils.isEmpty(nodeUuid)) {
/* 121 */         return null;
/*     */       }
/* 123 */       VirtualObjectPlacementModel result = new VirtualObjectPlacementModel();
/* 124 */       result.nodeUuid = nodeUuid;
/*     */       
/* 126 */       Map<String, Object> hostProperties = getHostData(nodeUuid);
/* 127 */       result.label = (String)hostProperties.get("name");
/* 128 */       result.iconId = (String)hostProperties.get("primaryIconId");
/* 129 */       result.faultDomain = (String)hostProperties.get("config.vsanHostConfig.faultDomainInfo.name");
/* 130 */       result.navigationTarget = (ManagedObjectReference)hostProperties.get("__resourceObject");
/* 131 */       return result;
/*     */     }
/*     */     
/*     */     private VirtualObjectPlacementModel buildDisk(String nodeUuid) {
/* 135 */       if (StringUtils.isEmpty(nodeUuid)) {
/* 136 */         return null;
/*     */       }
/* 138 */       VirtualObjectPlacementModel result = new VirtualObjectPlacementModel();
/* 139 */       result.nodeUuid = nodeUuid;
/* 140 */       if (this.diskByUuid.containsKey(nodeUuid)) {
/* 141 */         result.iconId = ((DiskResult)this.diskByUuid.get(nodeUuid)).disk.ssd.booleanValue() ? "ssd-disk-icon" : "disk-icon";
/* 142 */         result.label = ((DiskResult)this.diskByUuid.get(nodeUuid)).disk.displayName;
/*     */       } else {
/* 144 */         result.label = nodeUuid;
/*     */       } 
/* 146 */       return result;
/*     */     }
/*     */     
/*     */     private Map<String, Object> getHostData(String vsanUuid) {
/* 150 */       for (ManagedObjectReference hostRef : this.hostData.keySet()) {
/* 151 */         Map<String, Object> hostProperties = this.hostData.get(hostRef);
/* 152 */         if (vsanUuid.equals(hostProperties.get("config.vsanHostConfig.clusterInfo.nodeUuid"))) {
/* 153 */           return hostProperties;
/*     */         }
/*     */       } 
/*     */       
/* 157 */       throw new IllegalStateException("Host data not found: nodeUuid=" + vsanUuid);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VirtualObjectPlacementModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */