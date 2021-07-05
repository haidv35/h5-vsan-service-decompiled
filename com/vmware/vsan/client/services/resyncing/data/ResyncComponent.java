/*     */ package com.vmware.vsan.client.services.resyncing.data;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanComponentSyncState;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanObjectSyncState;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsan.client.services.common.data.VmData;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiLun;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiTarget;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectType;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @data
/*     */ public class ResyncComponent
/*     */ {
/*     */   public String name;
/*     */   public String iconId;
/*     */   public ManagedObjectReference moRef;
/*     */   public String storageProfile;
/*     */   public String hostName;
/*     */   public long bytesToResync;
/*     */   public long etaToResync;
/*     */   public ResyncReasonCode reason;
/*     */   public SortedSet<ResyncComponent> children;
/*     */   private ResyncComponent parent;
/*     */   private VmData vmData;
/*     */   private static final String DISK_ICON = "disk-icon";
/*     */   private static final String FOLDER_ICON = "vsphere-icon-folder";
/*     */   private static final String ISCSI_TARGET_ICON = "iscsi-target-icon";
/*     */   private static final String ISCSI_LUN_ICON = "iscsi-lun-icon";
/*     */   
/*     */   public ResyncComponent() {
/*  50 */     this.bytesToResync = 0L;
/*  51 */     this.etaToResync = -1L;
/*  52 */     this.children = new TreeSet<>(new ResyncComponentComparator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResyncComponent(VmData vmData) {
/*  59 */     this();
/*  60 */     if (vmData != null) {
/*  61 */       this.name = vmData.name;
/*  62 */       this.iconId = vmData.primaryIconId;
/*  63 */       this.moRef = vmData.vmRef;
/*  64 */       this.vmData = vmData;
/*     */     } 
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
/*     */   public ResyncComponent(VsanObject iscsiData, VsanObjectSyncState syncData, VsanObjectHealthData healthData, Map<String, String> hostUuidToHostNames) {
/*  77 */     this();
/*  78 */     if (iscsiData != null) {
/*  79 */       if (iscsiData instanceof IscsiTarget) {
/*  80 */         this.name = ((IscsiTarget)iscsiData).alias;
/*  81 */         this.iconId = "iscsi-target-icon";
/*  82 */       } else if (iscsiData instanceof IscsiLun) {
/*  83 */         String alias; IscsiLun lun = (IscsiLun)iscsiData;
/*     */         
/*  85 */         if (!StringUtils.isEmpty(lun.alias)) {
/*  86 */           alias = lun.alias;
/*     */         } else {
/*  88 */           alias = "-";
/*     */         } 
/*  90 */         this.name = Utils.getLocalizedString("vsan.virtualObjects.iscsiLun", new String[] { alias, Integer.toString(lun.lunId.intValue()) });
/*  91 */         this.iconId = "iscsi-lun-icon";
/*     */       } 
/*     */     }
/*     */     
/*  95 */     updateHealthData(healthData);
/*  96 */     if (syncData != null) {
/*  97 */       addComponents(syncData, hostUuidToHostNames);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResyncComponent(String name, String hostName, long bytesToResync, long etaToResync, ResyncReasonCode reason) {
/* 105 */     this();
/* 106 */     this.name = name;
/* 107 */     this.hostName = hostName;
/* 108 */     this.bytesToResync = bytesToResync;
/* 109 */     this.etaToResync = etaToResync;
/* 110 */     this.reason = reason;
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
/*     */   public ResyncComponent addChildObject(String name, VsanObjectIdentity objectIdentity, VsanObjectSyncState syncData, VsanObjectHealthData healthData, Map<String, String> hostUuidToHostNames) {
/* 122 */     ResyncComponent child = new ResyncComponent();
/* 123 */     child.name = name;
/* 124 */     if (this.vmData != null) {
/*     */       
/* 126 */       child.parent = this;
/* 127 */       child.processVmObjects(objectIdentity, healthData, this.vmData);
/*     */     } 
/* 129 */     child.updateHealthData(healthData);
/* 130 */     if (syncData != null) {
/* 131 */       child.addComponents(syncData, hostUuidToHostNames);
/*     */     }
/* 133 */     this.children.add(child);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   private void processVmObjects(VsanObjectIdentity objectIdentity, VsanObjectHealthData healthData, VmData vmData) {
/* 138 */     if (objectIdentity.type.equals(VsanObjectType.vdisk.toString())) {
/* 139 */       if (vmData.uuidToVirtualDiskMap.containsKey(objectIdentity.uuid)) {
/* 140 */         VirtualDisk virtualDisk = (VirtualDisk)vmData.uuidToVirtualDiskMap.get(objectIdentity.uuid);
/* 141 */         if (virtualDisk.deviceInfo != null) {
/* 142 */           this.name = virtualDisk.deviceInfo.label;
/* 143 */           this.iconId = "disk-icon";
/*     */         } 
/* 145 */       } else if (vmData.uuidToDiskSnapshotMap.containsKey(objectIdentity.uuid)) {
/* 146 */         this.name = vmData.getSnapshotName(objectIdentity.uuid);
/* 147 */         this.iconId = "disk-icon";
/*     */       } 
/* 149 */     } else if (objectIdentity.type.equals(VsanObjectType.namespace.toString())) {
/* 150 */       this.name = Utils.getLocalizedString("vsan.resyncing.components.vm.home.label");
/* 151 */       this.iconId = "vsphere-icon-folder";
/*     */       
/* 153 */       this.parent.updateHealthData(healthData);
/*     */     } 
/*     */   } private void addComponents(VsanObjectSyncState syncData, Map<String, String> hostUuidToHostNames) {
/*     */     byte b;
/*     */     int i;
/*     */     VsanComponentSyncState[] arrayOfVsanComponentSyncState;
/* 159 */     for (i = (arrayOfVsanComponentSyncState = syncData.components).length, b = 0; b < i; ) { VsanComponentSyncState vmResyncComponent = arrayOfVsanComponentSyncState[b];
/* 160 */       ResyncComponent component = new ResyncComponent(
/* 161 */           vmResyncComponent.uuid, 
/* 162 */           hostUuidToHostNames.get(vmResyncComponent.hostUuid), 
/* 163 */           vmResyncComponent.bytesToSync, 
/* 164 */           (vmResyncComponent.recoveryETA != null) ? vmResyncComponent.recoveryETA.longValue() : -1L, 
/* 165 */           getResyncReason(vmResyncComponent.reasons));
/* 166 */       this.children.add(component);
/*     */       b++; }
/*     */   
/*     */   }
/*     */   private void updateHealthData(VsanObjectHealthData healthData) {
/* 171 */     if (healthData != null)
/*     */     {
/* 173 */       this.storageProfile = healthData.policyName;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private ResyncReasonCode getResyncReason(String[] reasons) {
/* 179 */     if (ArrayUtils.isEmpty((Object[])reasons)) {
/* 180 */       return ResyncReasonCode.stale;
/*     */     }
/* 182 */     EnumSet<ResyncReasonCode> resonsSet = EnumSet.noneOf(ResyncReasonCode.class); byte b; int i; String[] arrayOfString;
/* 183 */     for (i = (arrayOfString = reasons).length, b = 0; b < i; ) { String reason = arrayOfString[b];
/* 184 */       resonsSet.add(ResyncReasonCode.valueOf(reason)); b++; }
/*     */     
/* 186 */     return resonsSet.iterator().next();
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
/*     */ 
/*     */   
/*     */   @data
/*     */   public enum ResyncReasonCode
/*     */   {
/* 204 */     evacuate,
/* 205 */     dying_evacuate,
/* 206 */     rebalance,
/* 207 */     repair,
/* 208 */     reconfigure,
/* 209 */     stale,
/* 210 */     merge_concat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ResyncComponentComparator
/*     */     implements Comparator<ResyncComponent>
/*     */   {
/*     */     public int compare(ResyncComponent o1, ResyncComponent o2) {
/* 234 */       if (Utils.getLocalizedString("vsan.resyncing.components.other").equals(o1.name))
/* 235 */         return 1; 
/* 236 */       if (Utils.getLocalizedString("vsan.resyncing.components.iscsi").equals(o1.name)) {
/* 237 */         if (!Utils.getLocalizedString("vsan.resyncing.components.other").equals(o2.name)) {
/* 238 */           return 1;
/*     */         }
/* 240 */         return -1;
/*     */       } 
/* 242 */       if (Utils.getLocalizedString("vsan.resyncing.components.other").equals(o2.name))
/* 243 */         return -1; 
/* 244 */       if (Utils.getLocalizedString("vsan.resyncing.components.iscsi").equals(o2.name)) {
/* 245 */         if (!Utils.getLocalizedString("vsan.resyncing.components.other").equals(o1.name)) {
/* 246 */           return -1;
/*     */         }
/* 248 */         return 1;
/*     */       } 
/*     */ 
/*     */       
/* 252 */       return o1.name.compareTo(o2.name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/data/ResyncComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */