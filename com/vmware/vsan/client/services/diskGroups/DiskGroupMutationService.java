/*     */ package com.vmware.vsan.client.services.diskGroups;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.host.MaintenanceSpec;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DecommissionMode;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.DiskMappingSpec;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.RemoveDiskGroupSpec;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.RemoveDiskSpec;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.VsanDiskMapping;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.impl.VsanMutationProvider;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanDiskMappingSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanRemoveDataDiskSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanRemoveDiskGroupSpec;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class DiskGroupMutationService
/*     */ {
/*     */   @Autowired
/*     */   private VsanMutationProvider vsanMutationProvider;
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   
/*     */   @TsService
/*     */   public Object createDiskGroup(ManagedObjectReference hostRef, DiskMappingSpec spec) throws Exception {
/*  47 */     VsanDiskMappingSpec vsanSpec = new VsanDiskMappingSpec();
/*  48 */     vsanSpec.clusterRef = spec.clusterRef;
/*  49 */     ArrayList<DiskMapping> mappings = new ArrayList(); byte b; int i; VsanDiskMapping[] arrayOfVsanDiskMapping;
/*  50 */     for (i = (arrayOfVsanDiskMapping = spec.mappings).length, b = 0; b < i; ) { VsanDiskMapping diskMapping = arrayOfVsanDiskMapping[b];
/*  51 */       DiskMapping vsanDiskMapping = new DiskMapping();
/*  52 */       vsanDiskMapping.ssd = diskMapping.ssd;
/*  53 */       vsanDiskMapping.nonSsd = diskMapping.nonSsd;
/*  54 */       mappings.add(vsanDiskMapping); b++; }
/*     */     
/*  56 */     vsanSpec.mappings = mappings.toArray();
/*  57 */     vsanSpec.isAllFlashSupported = spec.isAllFlashSupported;
/*     */     
/*  59 */     ManagedObjectReference task = this.vsanMutationProvider.claimDisks(hostRef, vsanSpec);
/*  60 */     return ImmutableMap.of("task", 
/*  61 */         new ManagedObjectReference(task.getType(), task.getValue(), hostRef.getServerGuid()));
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Map<String, ManagedObjectReference> addDiskToDiskGroup(ManagedObjectReference clusterRef, ManagedObjectReference hostRef, VsanDiskMapping vsanDiskMappings) throws Exception {
/*  67 */     DiskMapping diskMapping = new DiskMapping();
/*  68 */     diskMapping.ssd = vsanDiskMappings.ssd;
/*  69 */     diskMapping.nonSsd = vsanDiskMappings.nonSsd;
/*     */     
/*  71 */     VsanDiskMappingSpec spec = new VsanDiskMappingSpec();
/*  72 */     spec.mappings = new Object[] { diskMapping };
/*  73 */     spec.clusterRef = clusterRef;
/*  74 */     spec.isAllFlashSupported = VsanCapabilityUtils.isAllFlashSupported(hostRef);
/*     */     
/*  76 */     ManagedObjectReference task = this.vsanMutationProvider.claimDisks(hostRef, spec);
/*  77 */     return (Map<String, ManagedObjectReference>)ImmutableMap.of("task", 
/*  78 */         new ManagedObjectReference(task.getType(), task.getValue(), hostRef.getServerGuid()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<ManagedObjectReference> removeDisksAndMappings(ManagedObjectReference hostRef, RemoveDiskGroupSpec diskGroupSpec, RemoveDiskSpec diskSpec) throws Exception {
/*  85 */     List<ManagedObjectReference> tasks = new ArrayList<>();
/*     */     
/*  87 */     if (diskGroupSpec != null && ArrayUtils.isNotEmpty((Object[])diskGroupSpec.mappings)) {
/*  88 */       ManagedObjectReference task = removeDiskGroup(hostRef, diskGroupSpec);
/*  89 */       tasks.add(task);
/*     */     } 
/*  91 */     if (diskSpec != null && ArrayUtils.isNotEmpty((Object[])diskSpec.disks)) {
/*  92 */       ManagedObjectReference task = removeDisks(hostRef, diskSpec);
/*  93 */       tasks.add(task);
/*     */     } 
/*     */     
/*  96 */     return tasks;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference removeDiskGroup(ManagedObjectReference hostRef, RemoveDiskGroupSpec spec) throws Exception {
/* 101 */     VsanRemoveDiskGroupSpec vsanSpec = new VsanRemoveDiskGroupSpec();
/* 102 */     vsanSpec.evacuateData = new MaintenanceSpec();
/*     */     
/* 104 */     if (spec.decommissionMode != null) {
/* 105 */       DecommissionMode mode = new DecommissionMode(spec.decommissionMode.toString());
/* 106 */       vsanSpec.evacuateData.setVsanMode(mode);
/*     */     } 
/*     */     
/* 109 */     ArrayList<DiskMapping> mappings = new ArrayList<>(); byte b; int i; VsanDiskMapping[] arrayOfVsanDiskMapping;
/* 110 */     for (i = (arrayOfVsanDiskMapping = spec.mappings).length, b = 0; b < i; ) { VsanDiskMapping diskMapping = arrayOfVsanDiskMapping[b];
/* 111 */       DiskMapping vsanDiskMapping = new DiskMapping();
/* 112 */       vsanDiskMapping.ssd = diskMapping.ssd;
/* 113 */       vsanDiskMapping.nonSsd = diskMapping.nonSsd;
/* 114 */       mappings.add(vsanDiskMapping); b++; }
/*     */     
/* 116 */     vsanSpec.mappings = mappings.<DiskMapping>toArray(new DiskMapping[mappings.size()]);
/*     */     
/* 118 */     ManagedObjectReference task = this.vsanMutationProvider.removeDiskGroup(hostRef, vsanSpec);
/* 119 */     return new ManagedObjectReference(task.getType(), task.getValue(), hostRef.getServerGuid());
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference removeDisks(ManagedObjectReference hostRef, RemoveDiskSpec spec) throws Exception {
/* 124 */     VsanRemoveDataDiskSpec vsanSpec = new VsanRemoveDataDiskSpec();
/* 125 */     vsanSpec.evacuateData = new MaintenanceSpec();
/*     */     
/* 127 */     if (spec.decommissionMode != null) {
/* 128 */       DecommissionMode mode = new DecommissionMode(spec.decommissionMode.toString());
/* 129 */       vsanSpec.evacuateData.setVsanMode(mode);
/*     */     } 
/* 131 */     vsanSpec.disks = spec.disks;
/*     */     
/* 133 */     ManagedObjectReference task = this.vsanMutationProvider.removeDisk(hostRef, vsanSpec);
/* 134 */     return new ManagedObjectReference(task.getType(), task.getValue(), hostRef.getServerGuid());
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference setDiskLedState(ManagedObjectReference hostRef, String[] diskUuids, boolean on) throws Exception {
/* 140 */     ManagedObjectReference taskRef, storageSystemRef = (ManagedObjectReference)QueryUtil.getProperty(hostRef, "storageSystem", null);
/*     */     
/* 142 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public List<ManagedObjectReference> setDiskLocality(ManagedObjectReference hostRef, String[] diskUuids, boolean local) throws Exception {
/* 156 */     ManagedObjectReference storageSystemRef = (ManagedObjectReference)QueryUtil.getProperty(hostRef, "storageSystem", null);
/* 157 */     List<ManagedObjectReference> tasks = new ArrayList<>(diskUuids.length);
/* 158 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public List<ManagedObjectReference> setDiskType(ManagedObjectReference hostRef, String[] diskUuids, boolean ssd) throws Exception {
/* 171 */     ManagedObjectReference storageSystemRef = (ManagedObjectReference)QueryUtil.getProperty(hostRef, "storageSystem", null);
/* 172 */     List<ManagedObjectReference> tasks = new ArrayList<>(diskUuids.length);
/* 173 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskGroups/DiskGroupMutationService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */