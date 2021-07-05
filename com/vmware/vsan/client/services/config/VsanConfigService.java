/*     */ package com.vmware.vsan.client.services.config;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vsan.client.services.advancedoptions.AdvancedOptionsService;
/*     */ import com.vmware.vsan.client.services.dataprotection.model.ClusterDpConfigData;
/*     */ import com.vmware.vsan.client.services.encryption.EncryptionStatus;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.EncryptionState;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanHealthPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanHealthServiceStatus;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiTargetConfig;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.providers.VsanIscsiPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.perf.VsanPerfPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerfStatsObjectInfo;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VsanConfigService
/*     */ {
/*  28 */   private static final Log logger = LogFactory.getLog(VsanConfigService.class);
/*     */   
/*     */   @Autowired
/*     */   private VsanHealthPropertyProvider healthPropertyProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanPerfPropertyProvider perfPropertyProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanIscsiPropertyProvider iscsiPropertyProvider;
/*     */   
/*     */   @Autowired
/*     */   private AdvancedOptionsService advancedOptionsService;
/*     */   
/*     */   @TsService
/*     */   public String getVsanVersion(ManagedObjectReference clusterRef) throws Exception {
/*  44 */     VsanHealthServiceStatus status = this.healthPropertyProvider.getVsanHealthServiceStatus(clusterRef);
/*  45 */     if (status == null || status.versionCheck == null) {
/*  46 */       return null;
/*     */     }
/*  48 */     return status.versionCheck.latestVersiobNumber;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getDedupConfig(ManagedObjectReference clusterRef) throws Exception {
/*  53 */     if (!VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef) || 
/*  54 */       !VsanCapabilityUtils.isDeduplicationAndCompressionSupported(clusterRef)) {
/*  55 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */     
/*  58 */     boolean dedupEnabled = ((Boolean)QueryUtil.getProperty(clusterRef, "dataEfficiencyStatus")).booleanValue();
/*  59 */     VsanServiceStatus status = dedupEnabled ? VsanServiceStatus.ENABLED : VsanServiceStatus.DISABLED;
/*  60 */     return new VsanServiceData(status);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getEcnryptionConfig(ManagedObjectReference clusterRef) throws Exception {
/*  65 */     if (!VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef) || 
/*  66 */       !VsanCapabilityUtils.isEncryptionSupported(clusterRef)) {
/*  67 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */     
/*  70 */     EncryptionStatus config = (EncryptionStatus)QueryUtil.getProperty(clusterRef, 
/*  71 */         "vsanEncryptionStatus");
/*     */     
/*  73 */     if (config == null || config.state == null) {
/*  74 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */ 
/*     */     
/*  78 */     switch (config.state)
/*     */     { case Enabled:
/*     */       case EnabledNoKmip:
/*  81 */         serviceStatus = VsanServiceStatus.ENABLED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         return new VsanServiceData(serviceStatus, config); }  VsanServiceStatus serviceStatus = VsanServiceStatus.DISABLED; return new VsanServiceData(serviceStatus, config);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getPerformanceConfig(ManagedObjectReference clusterRef) throws Exception {
/*  94 */     VsanServiceStatus serviceStatus = this.perfPropertyProvider.getPerfServiceEnabled(clusterRef).booleanValue() ? 
/*  95 */       VsanServiceStatus.ENABLED : VsanServiceStatus.DISABLED;
/*  96 */     PerfStatsObjectInfo statsInfo = this.perfPropertyProvider.getStatesObjectInformation(clusterRef);
/*  97 */     if (statsInfo.vsanHealth != null) {
/*  98 */       statsInfo.vsanHealth = statsInfo.vsanHealth.toUpperCase().replaceAll("-", "_");
/*     */     }
/* 100 */     return new VsanServiceData(serviceStatus, statsInfo);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getIscsiTargetConfig(ManagedObjectReference clusterRef) throws Exception {
/* 105 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef) || 
/* 106 */       !VsanCapabilityUtils.isIscsiTargetsSupportedOnCluster(clusterRef)) {
/* 107 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */     
/* 110 */     VsanIscsiTargetConfig config = this.iscsiPropertyProvider.getVsanIscsiTargetConfig(clusterRef);
/* 111 */     VsanServiceStatus serviceStatus = config.status ? VsanServiceStatus.ENABLED : VsanServiceStatus.DISABLED;
/*     */     
/* 113 */     return new VsanServiceData(serviceStatus, config);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getDataProtectionConfig(ManagedObjectReference clusterRef) throws Exception {
/* 118 */     if (!(VsanCapabilityUtils.getVcCapabilities(clusterRef)).isLocalDataProtectionSupported) {
/* 119 */       return null;
/*     */     }
/* 121 */     if (!(VsanCapabilityUtils.getCapabilities(clusterRef)).isLocalDataProtectionSupported) {
/* 122 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */     
/* 125 */     ConfigInfoEx vsanConfig = (ConfigInfoEx)QueryUtil.getProperty(clusterRef, "vsanConfigInfo");
/* 126 */     if (vsanConfig == null || vsanConfig.dataProtectionConfig == null) {
/* 127 */       logger.error("Vsan Data protection config is missing!");
/* 128 */       return null;
/*     */     } 
/* 130 */     ClusterDpConfigData details = new ClusterDpConfigData();
/* 131 */     details.usageThreshold = vsanConfig.dataProtectionConfig.usageThreshold;
/*     */     
/* 133 */     return new VsanServiceData(VsanServiceStatus.ENABLED, details);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getFileServicesConfig(ManagedObjectReference clusterRef) {
/* 138 */     if (!(VsanCapabilityUtils.getVcCapabilities(clusterRef)).isFileServiceSupported) {
/* 139 */       return null;
/*     */     }
/* 141 */     if (!(VsanCapabilityUtils.getCapabilities(clusterRef)).isFileServiceSupported) {
/* 142 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/* 144 */     return new VsanServiceData(VsanServiceStatus.ENABLED);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanServiceData getAdvancedOptionsConfig(ManagedObjectReference clusterRef) throws Exception {
/* 149 */     if (!(VsanCapabilityUtils.getVcCapabilities(clusterRef)).isAdvancedClusterOptionsSupported) {
/* 150 */       return null;
/*     */     }
/*     */     
/* 153 */     if (!(VsanCapabilityUtils.getCapabilities(clusterRef)).isAdvancedClusterOptionsSupported) {
/* 154 */       return new VsanServiceData(VsanServiceStatus.NOT_SUPPORTED);
/*     */     }
/*     */     
/* 157 */     return new VsanServiceData(
/* 158 */         VsanServiceStatus.ENABLED, 
/* 159 */         this.advancedOptionsService.getAdvancedOptionsInfo(clusterRef));
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/config/VsanConfigService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */