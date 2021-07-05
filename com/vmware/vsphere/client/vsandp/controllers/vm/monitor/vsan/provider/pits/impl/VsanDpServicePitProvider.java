/*     */ package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.impl;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.ArchivalProtectionInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.CgInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.GroupInstanceData;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.LocalProtectionInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.cluster.ProtectionService;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.VmDataProtectionSyncPointsController;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.PitProvider;
/*     */ import com.vmware.vsphere.client.vsandp.data.ProtectionType;
/*     */ import com.vmware.vsphere.client.vsandp.dataproviders.vm.VmConsistencyGroupPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.TreeSet;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VsanDpServicePitProvider
/*     */   implements PitProvider
/*     */ {
/*  31 */   private static final Logger logger = LoggerFactory.getLogger(VsanDpServicePitProvider.class);
/*     */   
/*     */   @Autowired
/*     */   private VmConsistencyGroupPropertyProvider cgProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanDpInventoryHelper inventoryHelper;
/*     */ 
/*     */   
/*     */   public TreeSet<VmProtectionInstance> getLocalPits(ManagedObjectReference vmRef) throws Exception {
/*  41 */     TreeSet<VmProtectionInstance> localPits = new TreeSet<>((Comparator<? super VmProtectionInstance>)new VmDataProtectionSyncPointsController.VmProtectionInstanceComparator());
/*     */ 
/*     */     
/*  44 */     CgInfo vmCgInfo = this.cgProvider.getVmDataProtection(vmRef);
/*  45 */     if (vmCgInfo == null || vmCgInfo.getLocal() == null) {
/*     */       
/*  47 */       logger.error("Unable to get local restore points for vm " + vmRef.getValue());
/*  48 */       return localPits;
/*     */     } 
/*  50 */     LocalProtectionInfo localProtection = vmCgInfo.getLocal();
/*     */     
/*  52 */     GroupInstanceData[] pitsData = localProtection.getInstance();
/*     */     
/*  54 */     if (pitsData == null)
/*  55 */       return localPits;  byte b;
/*     */     int i;
/*     */     GroupInstanceData[] arrayOfGroupInstanceData1;
/*  58 */     for (i = (arrayOfGroupInstanceData1 = pitsData).length, b = 0; b < i; ) { GroupInstanceData instance = arrayOfGroupInstanceData1[b];
/*  59 */       localPits.add(createProtectionInstance(localProtection.series.key, ProtectionType.LOCAL, instance));
/*     */       b++; }
/*     */     
/*  62 */     return localPits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeSet<VmProtectionInstance> getArchivePits(ManagedObjectReference vmRef) throws Exception {
/*  69 */     ManagedObjectReference clusterRef = this.inventoryHelper.getVmCluster(vmRef);
/*  70 */     if (clusterRef == null || !VsanCapabilityUtils.isArchiveDataProtectionSupported(clusterRef)) {
/*  71 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  75 */     CgInfo cgInfo = this.cgProvider.getVmDataProtection(vmRef);
/*  76 */     ArchivalProtectionInfo[] archiveProtections = cgInfo.getArchive();
/*  77 */     if (archiveProtections == null || archiveProtections.length < 1) {
/*  78 */       logger.info("No result was received when archival protection info was queried for vmRef: {}", vmRef);
/*  79 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     ArchivalProtectionInfo archiveProtetion = archiveProtections[0];
/*     */     
/*  85 */     TreeSet<VmProtectionInstance> archivePits = 
/*  86 */       new TreeSet<>((Comparator<? super VmProtectionInstance>)new VmDataProtectionSyncPointsController.VmProtectionInstanceComparator());
/*     */     
/*  88 */     ProtectionService.InstanceQuery.Result.SeriesEntry[] allSeries = 
/*  89 */       this.cgProvider.getArchivalSeries(clusterRef, cgInfo.getKey());
/*  90 */     if (allSeries != null) {
/*  91 */       byte b; int i; ProtectionService.InstanceQuery.Result.SeriesEntry[] arrayOfSeriesEntry; for (i = (arrayOfSeriesEntry = allSeries).length, b = 0; b < i; ) { ProtectionService.InstanceQuery.Result.SeriesEntry series = arrayOfSeriesEntry[b];
/*  92 */         GroupInstanceData[] pitsData = series.getInstance();
/*  93 */         if (pitsData != null) {
/*  94 */           byte b1; int j; GroupInstanceData[] arrayOfGroupInstanceData; for (j = (arrayOfGroupInstanceData = pitsData).length, b1 = 0; b1 < j; ) { GroupInstanceData instance = arrayOfGroupInstanceData[b1];
/*  95 */             archivePits.add(
/*  96 */                 createProtectionInstance(archiveProtetion.series.key, ProtectionType.ARCHIVE, instance)); b1++; }
/*     */         
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } else {
/* 102 */       logger.info("No archival series were found when querying archival pits for VM {}", vmRef);
/*     */     } 
/*     */     
/* 105 */     return archivePits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmProtectionInstance createProtectionInstance(String series, ProtectionType type, GroupInstanceData instance) {
/* 112 */     long duration = 0L;
/* 113 */     Calendar startTime = instance.getSnapshotTimestamp();
/* 114 */     if (instance.getInstanceStats().getEndTime() != null) {
/* 115 */       duration = instance.getInstanceStats().getEndTime().getTimeInMillis() - startTime.getTimeInMillis();
/*     */     } else {
/* 117 */       duration = Calendar.getInstance().getTimeInMillis() - 
/* 118 */         instance.getInstanceStats().getStartTime().getTimeInMillis();
/*     */     } 
/*     */     
/* 121 */     boolean isManual = checkCgForManualSnapshots(instance);
/* 122 */     return new VmProtectionInstance(instance.getKey(), 
/* 123 */         series, 
/* 124 */         type, 
/* 125 */         startTime.getTime(), 
/* 126 */         instance.getAllocatedBytes(), 
/* 127 */         duration, 
/* 128 */         false, 
/* 129 */         VmProtectionInstance.QuiescingType.fromQuescedType(instance.quiescedType), 
/* 130 */         false, 
/* 131 */         isManual);
/*     */   }
/*     */   
/*     */   private boolean checkCgForManualSnapshots(GroupInstanceData instance) {
/* 135 */     if (instance.getMember() == null || (instance.getMember()).length == 0) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     VmProtectionInstance.QuiescingType type = VmProtectionInstance.QuiescingType.fromQuescedType(instance.quiescedType); byte b; int i; GroupInstanceData.ObjectSnapshot[] arrayOfObjectSnapshot;
/* 140 */     for (i = (arrayOfObjectSnapshot = instance.getMember()).length, b = 0; b < i; ) { GroupInstanceData.ObjectSnapshot os = arrayOfObjectSnapshot[b];
/* 141 */       if (type.equals(VmProtectionInstance.QuiescingType.NONE)) {
/* 142 */         if (os.getNumManagedSnapshots() > 0) {
/* 143 */           return true;
/*     */         
/*     */         }
/*     */       }
/* 147 */       else if (os.getNumManagedSnapshots() > 1) {
/* 148 */         return true;
/*     */       } 
/*     */       b++; }
/*     */     
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/provider/pits/impl/VsanDpServicePitProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */