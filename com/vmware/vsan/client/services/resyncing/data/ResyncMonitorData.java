/*     */ package com.vmware.vsan.client.services.resyncing.data;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanObjectSyncState;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanSyncingObjectQueryResult;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsan.client.services.common.data.VmData;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiLun;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiTarget;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @data
/*     */ public class ResyncMonitorData
/*     */ {
/*     */   public long bytesToResync;
/*     */   public long etaToResync;
/*     */   public long componentsToSync;
/*     */   public DelayTimerData delayTimerData;
/*     */   public RepairTimerData repairTimerData;
/*     */   public boolean isResyncThrottlingSupported;
/*     */   public boolean isVsanClusterPartitioned;
/*     */   public boolean isResyncFilterApiSupported;
/*     */   public int resyncThrottlingValue;
/*     */   public SortedSet<ResyncComponent> components;
/*     */   private Map<String, String> hostUuidToHostNames;
/*  44 */   private Map<String, VsanObjectSyncState> componentsSyncData = new HashMap<>();
/*     */   public ResyncMonitorData() {}
/*     */   
/*     */   public ResyncMonitorData(VsanSyncingObjectQueryResult syncingObjectsData, Map<String, String> hostUuidToHostNames) {
/*  48 */     this();
/*  49 */     this.etaToResync = syncingObjectsData.totalRecoveryETA.longValue();
/*  50 */     this.componentsToSync = syncingObjectsData.totalObjectsToSync.longValue();
/*  51 */     this.bytesToResync = syncingObjectsData.totalBytesToSync.longValue();
/*  52 */     this.hostUuidToHostNames = hostUuidToHostNames; byte b; int i; VsanObjectSyncState[] arrayOfVsanObjectSyncState;
/*  53 */     for (i = (arrayOfVsanObjectSyncState = syncingObjectsData.objects).length, b = 0; b < i; ) { VsanObjectSyncState vsanObject = arrayOfVsanObjectSyncState[b];
/*  54 */       this.componentsSyncData.put(vsanObject.uuid, vsanObject); b++; }
/*     */     
/*  56 */     if (this.componentsSyncData.size() > 0) {
/*  57 */       this.components = new TreeSet<>(new ResyncComponent.ResyncComponentComparator());
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<String> getVsanObjectUuids() {
/*  62 */     return this.componentsSyncData.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResyncMonitorData processVmObjects(List<VsanObjectIdentity> vmObjectIdentities, Map<ManagedObjectReference, VmData> vmDataMap, Map<String, VsanObjectHealthData> vsanHealthData) {
/*  71 */     Map<ManagedObjectReference, ResyncComponent> vmResyncDataMap = new HashMap<>();
/*     */     
/*  73 */     for (VsanObjectIdentity objectIdentity : vmObjectIdentities) {
/*  74 */       VmData vmData = vmDataMap.get(objectIdentity.vm);
/*  75 */       ResyncComponent vmResyncData = vmResyncDataMap.get(objectIdentity.vm);
/*     */       
/*  77 */       if (vmResyncData == null) {
/*  78 */         vmResyncData = new ResyncComponent(vmData);
/*  79 */         vmResyncDataMap.put(objectIdentity.vm, vmResyncData);
/*     */       } 
/*     */ 
/*     */       
/*  83 */       vmResyncData.addChildObject(objectIdentity.description, 
/*  84 */           objectIdentity, 
/*  85 */           this.componentsSyncData.get(objectIdentity.uuid), 
/*  86 */           vsanHealthData.get(objectIdentity.uuid), 
/*  87 */           this.hostUuidToHostNames);
/*     */     } 
/*  89 */     Collection<ResyncComponent> resyncComponents = vmResyncDataMap.values();
/*  90 */     for (ResyncComponent vmResyncData : resyncComponents) {
/*  91 */       updateTotalBytesAndEtaToSync(vmResyncData);
/*     */     }
/*  93 */     this.components.addAll(resyncComponents);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResyncMonitorData processOtherObjects(List<VsanObjectIdentity> vmObjectIdentities, List<String> orphanedSyncObjects, Map<String, VsanObjectHealthData> vsanHealthData) {
/* 102 */     if (CollectionUtils.isEmpty(vmObjectIdentities) && CollectionUtils.isEmpty(orphanedSyncObjects))
/*     */     {
/* 104 */       return this;
/*     */     }
/*     */     
/* 107 */     ResyncComponent othersComponent = new ResyncComponent();
/* 108 */     othersComponent.name = Utils.getLocalizedString("vsan.resyncing.components.other");
/*     */ 
/*     */     
/* 111 */     if (vmObjectIdentities != null) {
/* 112 */       for (VsanObjectIdentity objectIdentity : vmObjectIdentities) {
/* 113 */         othersComponent.addChildObject(objectIdentity.description, objectIdentity, 
/* 114 */             this.componentsSyncData.get(objectIdentity.uuid), 
/* 115 */             vsanHealthData.get(objectIdentity.uuid), 
/* 116 */             this.hostUuidToHostNames);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 121 */     if (orphanedSyncObjects != null) {
/* 122 */       for (String orphanedSyncObject : orphanedSyncObjects) {
/* 123 */         othersComponent.addChildObject(orphanedSyncObject, null, 
/* 124 */             this.componentsSyncData.get(orphanedSyncObject), 
/* 125 */             vsanHealthData.get(orphanedSyncObject), 
/* 126 */             this.hostUuidToHostNames);
/*     */       }
/*     */     }
/*     */     
/* 130 */     updateTotalBytesAndEtaToSync(othersComponent);
/* 131 */     this.components.add(othersComponent);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResyncMonitorData processIscsiObjects(List<VsanObjectIdentity> vmObjectIdentities, Map<String, VsanObjectHealthData> vsanHealthData, Map<String, VsanObject> iscsiObjects) {
/* 140 */     ResyncComponent iscsiComponent = new ResyncComponent();
/* 141 */     iscsiComponent.name = Utils.getLocalizedString("vsan.resyncing.components.iscsi");
/*     */     
/* 143 */     List<IscsiLun> iscsiLuns = new ArrayList<>();
/* 144 */     for (VsanObjectIdentity objectIdentity : vmObjectIdentities) {
/* 145 */       if (iscsiObjects != null && iscsiObjects.containsKey(objectIdentity.uuid)) {
/* 146 */         VsanObject iscsiTargetObject = iscsiObjects.get(objectIdentity.uuid);
/* 147 */         if (iscsiTargetObject instanceof IscsiTarget) {
/* 148 */           ResyncComponent iscsiTargetComponent = new ResyncComponent(iscsiTargetObject, 
/* 149 */               this.componentsSyncData.get(objectIdentity.uuid), 
/* 150 */               vsanHealthData.get(objectIdentity.uuid), 
/* 151 */               this.hostUuidToHostNames);
/* 152 */           iscsiComponent.children.add(iscsiTargetComponent); continue;
/* 153 */         }  if (iscsiTargetObject instanceof IscsiLun)
/*     */         {
/* 155 */           iscsiLuns.add((IscsiLun)iscsiTargetObject);
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 160 */       iscsiComponent.addChildObject(objectIdentity.description, 
/* 161 */           objectIdentity, 
/* 162 */           this.componentsSyncData.get(objectIdentity.uuid), 
/* 163 */           vsanHealthData.get(objectIdentity.uuid), 
/* 164 */           this.hostUuidToHostNames);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 169 */     for (IscsiLun iscsiLun : iscsiLuns) {
/* 170 */       String targetAlias = iscsiLun.targetAlias;
/* 171 */       boolean parentTargetFound = false;
/* 172 */       for (ResyncComponent iscsiTargetComponent : iscsiComponent.children) {
/* 173 */         if (iscsiTargetComponent.name.equals(targetAlias)) {
/* 174 */           parentTargetFound = true;
/* 175 */           ResyncComponent iscsiLunComponent = new ResyncComponent((VsanObject)iscsiLun, 
/* 176 */               this.componentsSyncData.get(iscsiLun.vsanObjectUuid), 
/* 177 */               vsanHealthData.get(iscsiLun.vsanObjectUuid), 
/* 178 */               this.hostUuidToHostNames);
/* 179 */           iscsiTargetComponent.children.add(iscsiLunComponent);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 184 */       if (!parentTargetFound) {
/* 185 */         for (VsanObject vsanObject : iscsiObjects.values()) {
/* 186 */           if (vsanObject instanceof IscsiTarget && 
/* 187 */             ((IscsiTarget)vsanObject).alias.equals(targetAlias)) {
/*     */             
/* 189 */             ResyncComponent iscsiTargetComponent = new ResyncComponent(vsanObject, 
/* 190 */                 this.componentsSyncData.get(vsanObject.vsanObjectUuid), 
/* 191 */                 vsanHealthData.get(vsanObject.vsanObjectUuid), 
/* 192 */                 this.hostUuidToHostNames);
/* 193 */             iscsiComponent.children.add(iscsiTargetComponent);
/*     */             
/* 195 */             ResyncComponent iscsiLunComponent = new ResyncComponent((VsanObject)iscsiLun, 
/* 196 */                 this.componentsSyncData.get(iscsiLun.vsanObjectUuid), 
/* 197 */                 vsanHealthData.get(iscsiLun.vsanObjectUuid), 
/* 198 */                 this.hostUuidToHostNames);
/* 199 */             iscsiTargetComponent.children.add(iscsiLunComponent);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 206 */     updateTotalBytesAndEtaToSync(iscsiComponent);
/* 207 */     this.components.add(iscsiComponent);
/* 208 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateTotalBytesAndEtaToSync(ResyncComponent resyncComponent) {
/* 215 */     for (ResyncComponent childComponent : resyncComponent.children) {
/* 216 */       if (childComponent.children.size() > 0) {
/* 217 */         updateTotalBytesAndEtaToSync(childComponent);
/*     */       }
/* 219 */       resyncComponent.bytesToResync += childComponent.bytesToResync;
/* 220 */       resyncComponent.etaToResync = Math.max(childComponent.etaToResync, resyncComponent.etaToResync);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/data/ResyncMonitorData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */