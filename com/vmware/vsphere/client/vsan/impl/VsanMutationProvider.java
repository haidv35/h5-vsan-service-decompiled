/*     */ package com.vmware.vsphere.client.vsan.impl;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.cluster.ConfigSpecEx;
/*     */ import com.vmware.vim.binding.vim.encryption.KeyProviderId;
/*     */ import com.vmware.vim.binding.vim.host.MaintenanceSpec;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.vsan.cluster.ConfigInfo;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DecommissionMode;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcDiskManagementSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataEncryptionConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataProtectionInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ResyncIopsInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMappingCreationSpec;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.RecreateDiskGroupSpec;
/*     */ import com.vmware.vsan.client.services.encryption.EncryptionPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.config.ResyncThrottlingSpec;
/*     */ import com.vmware.vsphere.client.vsan.config.VsanSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanDiskMappingSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanFaultDomainSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanRemoveDataDiskSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanRemoveDiskGroupSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanSemiAutoDiskMappingsSpec;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class VsanMutationProvider
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(VsanMutationProvider.class);
/*     */   
/*  55 */   private static final VsanProfiler profiler = new VsanProfiler(VsanMutationProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private EncryptionPropertyProvider encryptionPropertyProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference configure(ManagedObjectReference clusterRef, VsanSpec spec) throws Exception {
/*  79 */     assert clusterRef != null;
/*  80 */     assert spec != null;
/*     */     
/*  82 */     ManagedObjectReference configureClusterTask = null;
/*     */     
/*  84 */     if (VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/*  85 */       configureClusterTask = getWizardReconfigureTask(clusterRef, spec);
/*     */     } else {
/*  87 */       configureClusterTask = getLegacyApiReconfigureTask(clusterRef, spec);
/*     */     } 
/*     */     
/*  90 */     return configureClusterTask;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference turnOffVsan(ManagedObjectReference clusterRef) throws Exception {
/*  95 */     assert clusterRef != null;
/*     */     
/*  97 */     ManagedObjectReference configureClusterTask = null;
/*     */     
/*  99 */     ConfigInfo configInfo = new ConfigInfo();
/* 100 */     configInfo.enabled = Boolean.valueOf(false);
/*     */     
/* 102 */     if (VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/* 103 */       Exception exception2; ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 104 */       reconfigSpec.vsanClusterConfig = configInfo;
/* 105 */       reconfigSpec.modify = true;
/*     */       
/* 107 */       VsanVcClusterConfigSystem vsanConfigSystem = 
/* 108 */         VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 109 */       Exception exception1 = null;
/*     */     } else {
/*     */       Exception exception2;
/*     */       
/* 113 */       ConfigSpecEx clusterSpecEx = new ConfigSpecEx();
/* 114 */       clusterSpecEx.vsanConfig = configInfo;
/*     */       
/* 116 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (configureClusterTask != null && configureClusterTask.getServerGuid() == null) {
/* 125 */       configureClusterTask.setServerGuid(clusterRef.getServerGuid());
/*     */     }
/*     */     
/* 128 */     return configureClusterTask;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference configureDataProtection(ManagedObjectReference clusterRef, VsanSpec spec) throws Exception {
/* 134 */     assert clusterRef != null;
/*     */     
/* 136 */     ManagedObjectReference configureClusterTask = null;
/*     */     
/* 138 */     if (VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/* 139 */       Exception exception2; VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 140 */       ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 141 */       addDataProtectionConfig(reconfigSpec, clusterRef, spec);
/* 142 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 147 */     return configureClusterTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ManagedObjectReference getWizardReconfigureTask(ManagedObjectReference clusterRef, VsanSpec spec) throws Exception {
/* 154 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*     */     
/* 156 */     ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 157 */     reconfigSpec.setVsanClusterConfig(spec.toVmodlSpec());
/* 158 */     reconfigSpec.setDataEfficiencyConfig(spec.dataEfficiency.toVmodlSpec());
/* 159 */     reconfigSpec.setAllowReducedRedundancy(Boolean.valueOf(spec.allowReducedRedundancy));
/*     */     
/* 161 */     boolean hasEncryptioPermissions = this.encryptionPropertyProvider.getEncryptionPermissions(clusterRef);
/* 162 */     if (hasEncryptioPermissions) {
/* 163 */       DataEncryptionConfig encryptionConfig = new DataEncryptionConfig();
/* 164 */       encryptionConfig.encryptionEnabled = spec.isEncryptionEnabled;
/* 165 */       if (spec.isEncryptionEnabled) {
/* 166 */         if (!StringUtils.isEmpty(spec.kmipClusterId)) {
/* 167 */           encryptionConfig.kmsProviderId = new KeyProviderId(spec.kmipClusterId);
/*     */         }
/* 169 */         encryptionConfig.eraseDisksBeforeUse = Boolean.valueOf(spec.eraseDisksBeforeUse);
/*     */       } 
/* 171 */       reconfigSpec.setDataEncryptionConfig(encryptionConfig);
/*     */     } 
/*     */ 
/*     */     
/* 175 */     addDataProtectionConfig(reconfigSpec, clusterRef, spec);
/*     */     
/* 177 */     reconfigSpec.setModify(true);
/*     */     
/* 179 */     ManagedObjectReference configureClusterTask = null;
/* 180 */     Exception exception1 = null, exception2 = null;
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
/*     */   private void addDataProtectionConfig(ReconfigSpec reconfigSpec, ManagedObjectReference clusterRef, VsanSpec spec) {
/* 192 */     if (VsanCapabilityUtils.isLocalDataProtectionSupported(clusterRef)) {
/* 193 */       boolean isArchivalSupported = VsanCapabilityUtils.isArchiveDataProtectionSupported(clusterRef);
/* 194 */       DataProtectionInfo dpConfig = spec.getDpConfigInfo(isArchivalSupported);
/* 195 */       reconfigSpec.setDataProtectionConfig(dpConfig);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ManagedObjectReference getLegacyApiReconfigureTask(ManagedObjectReference clusterRef, VsanSpec spec) throws Exception {
/* 203 */     ConfigSpecEx clusterSpecEx = new ConfigSpecEx();
/* 204 */     clusterSpecEx.vsanConfig = new ConfigInfo();
/* 205 */     clusterSpecEx.vsanConfig.setEnabled(Boolean.valueOf(spec.isEnabled));
/* 206 */     clusterSpecEx.vsanConfig.setDefaultConfig(spec.toVmodlSpec().getDefaultConfig());
/*     */     
/* 208 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 215 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference claimDisks(ManagedObjectReference hostRef, VsanDiskMappingSpec spec) throws Exception {
/* 227 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 265 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference autoClaimDisks(ManagedObjectReference hostRef, VsanSemiAutoDiskMappingsSpec spec) throws Exception {
/* 276 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 314 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   private DiskMappingCreationSpec createDiskMappingCreationSpec(ManagedObjectReference hostRef, List<ScsiDisk> cacheDisksToAdd, List<ScsiDisk> storageDisksToAdd, boolean isAllFlash) {
/* 320 */     DiskMappingCreationSpec createSpec = new DiskMappingCreationSpec();
/* 321 */     createSpec.host = hostRef;
/*     */     
/* 323 */     createSpec.cacheDisks = cacheDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[cacheDisksToAdd.size()]);
/* 324 */     createSpec.capacityDisks = storageDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[storageDisksToAdd.size()]);
/* 325 */     createSpec.creationType = isAllFlash ? 
/* 326 */       DiskMappingCreationSpec.DiskMappingCreationType.allFlash.toString() : 
/* 327 */       DiskMappingCreationSpec.DiskMappingCreationType.hybrid.toString();
/* 328 */     return createSpec;
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
/*     */   public ManagedObjectReference removeDisk(ManagedObjectReference hostRef, VsanRemoveDataDiskSpec spec) throws Exception {
/* 341 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 350 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference removeDiskGroup(ManagedObjectReference hostRef, VsanRemoveDiskGroupSpec spec) throws Exception {
/* 363 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 372 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
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
/*     */   @TsService
/*     */   public ManagedObjectReference updateFaultDomainInfo(ManagedObjectReference hostRef, VsanFaultDomainSpec spec) throws Exception {
/* 387 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 402 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference resyncThrottling(ManagedObjectReference clusterRef, ResyncThrottlingSpec spec) throws Exception {
/* 409 */     VsanVcClusterConfigSystem vsanConfigSystem = 
/* 410 */       VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 411 */     ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 412 */     reconfigSpec.resyncIopsLimitConfig = new ResyncIopsInfo();
/* 413 */     reconfigSpec.resyncIopsLimitConfig.setResyncIops(spec.iopsLimit);
/*     */     
/* 415 */     reconfigSpec.setModify(true);
/*     */     
/* 417 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 424 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference recreateDiskGroup(ManagedObjectReference hostRef, RecreateDiskGroupSpec spec) throws Exception {
/* 431 */     VsanVcDiskManagementSystem diskManagement = VsanProviderUtils.getVcDiskManagementSystem(hostRef);
/*     */     
/* 433 */     MaintenanceSpec maintenanceSpec = new MaintenanceSpec();
/* 434 */     DecommissionMode mode = new DecommissionMode(spec.decommissionMode.toString());
/* 435 */     maintenanceSpec.setVsanMode(mode);
/*     */     
/* 437 */     ManagedObjectReference initializeDisksTask = null;
/* 438 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/impl/VsanMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */