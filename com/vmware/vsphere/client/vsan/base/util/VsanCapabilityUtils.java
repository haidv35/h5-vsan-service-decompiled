/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanCapabilityData;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class VsanCapabilityUtils
/*     */ {
/*  27 */   private static final Log _logger = LogFactory.getLog(VsanCapabilityUtils.class);
/*     */   
/*  29 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanCapabilityUtils.class);
/*     */   
/*     */   private static VsanCapabilityCacheManager capabilityCacheManager;
/*     */   
/*     */   public static void setVsanCapabilityCacheManager(VsanCapabilityCacheManager capabilityCacheManager) {
/*  34 */     VsanCapabilityUtils.capabilityCacheManager = capabilityCacheManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanCapabilityData getVcCapabilities(ManagedObjectReference moRef) {
/*  45 */     return capabilityCacheManager.getVcCapabilities(moRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanCapabilityData getCapabilities(ManagedObjectReference moRef) {
/*  56 */     validateMoRef(moRef);
/*     */     
/*  58 */     VsanCapabilityData capabilityData = 
/*  59 */       HostSystem.class.getSimpleName().equals(moRef.getType()) ? 
/*  60 */       capabilityCacheManager.getHostCapabilities(moRef) : 
/*  61 */       capabilityCacheManager.getClusterCapabilities(moRef);
/*  62 */     return capabilityData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpgradeSystemExSupportedOnVc(ManagedObjectReference moRef) {
/*  73 */     return (getVcCapabilities(moRef)).isUpgradeSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpgradeSystem2SupportedOnVc(ManagedObjectReference moRef) {
/*  84 */     return isUpgradeSystemExSupportedOnVc(moRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isObjectSystemSupported(ManagedObjectReference moRef) {
/*  95 */     return (getCapabilities(moRef)).isObjectIdentitiesSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isObjectSystemSupportedOnVc(ManagedObjectReference moRef) {
/* 106 */     return (getVcCapabilities(moRef)).isObjectIdentitiesSupported;
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
/*     */   public static boolean isClusterConfigSystemSupportedOnVc(ManagedObjectReference moRef) {
/* 119 */     return (getVcCapabilities(moRef)).isClusterConfigSupported;
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
/*     */   public static boolean isDeduplicationAndCompressionSupportedOnVc(ManagedObjectReference moRef) {
/* 132 */     return (getVcCapabilities(moRef)).isDeduplicationAndCompressionSupported;
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
/*     */   public static boolean isDeduplicationAndCompressionSupported(ManagedObjectReference moRef) {
/* 144 */     return (getCapabilities(moRef)).isDeduplicationAndCompressionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIscsiTargetsSupportedOnVc(ManagedObjectReference moRef) {
/* 155 */     return (getVcCapabilities(moRef)).isIscsiTargetsSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIscsiTargetsSupportedOnHost(ManagedObjectReference moRef) {
/* 165 */     validateMoRef(moRef);
/* 166 */     return (getCapabilities(moRef)).isIscsiTargetsSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isIscsiTargetsSupportedOnCluster(ManagedObjectReference moRef) {
/* 176 */     validateMoRef(moRef);
/* 177 */     return (getCapabilities(moRef)).isIscsiTargetsSupported;
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
/*     */   public static boolean isStretchedClusterSupported(ManagedObjectReference moRef) {
/* 189 */     validateMoRef(moRef);
/*     */     
/* 191 */     boolean isSupported = false;
/*     */     
/* 193 */     if (ClusterComputeResource.class.getSimpleName().equals(moRef.getType())) {
/* 194 */       isSupported = isStretchedClusterSupportedOnCluster(moRef);
/* 195 */     } else if (HostSystem.class.getSimpleName().equals(moRef.getType())) {
/* 196 */       isSupported = isStretchedClusterSupportedOnHost(moRef);
/*     */     } 
/*     */     
/* 199 */     return isSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAllFlashSupported(ManagedObjectReference moRef) {
/* 210 */     validateMoRef(moRef);
/*     */     
/* 212 */     boolean isSupported = false;
/*     */     
/* 214 */     if (ClusterComputeResource.class.getSimpleName().equals(moRef.getType())) {
/* 215 */       isSupported = isAllFlashSupportedOnCluster(moRef);
/* 216 */     } else if (HostSystem.class.getSimpleName().equals(moRef.getType())) {
/* 217 */       isSupported = isAllFlashSupportedOnHost(moRef);
/*     */     } 
/*     */     
/* 220 */     return isSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEncryptionSupportedOnVc(ManagedObjectReference moRef) {
/* 231 */     return (getVcCapabilities(moRef)).isEncryptionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEncryptionSupported(ManagedObjectReference moRef) {
/* 242 */     return (getCapabilities(moRef)).isEncryptionSupported;
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
/*     */   public static boolean isSilentCheckSupportedOnVc(ManagedObjectReference moRef) {
/* 256 */     return (getVcCapabilities(moRef)).isEncryptionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPerfVerboseModeSupportedOnVc(ManagedObjectReference moRef) {
/* 266 */     return (getVcCapabilities(moRef)).isPerfVerboseModeSupported;
/*     */   }
/*     */   
/*     */   public static boolean isPerfSvcAutoConfigSupportedOnVc(ManagedObjectReference moRef) {
/* 270 */     return (getVcCapabilities(moRef)).isPerfSvcAutoConfigSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isResyncThrottlingSupported(ManagedObjectReference moRef) {
/* 280 */     VsanCapabilityData capabilityData = getCapabilities(moRef);
/* 281 */     _logger.info("Resync throttling supported on cluster is: " + capabilityData.isResyncThrottlingSupported);
/* 282 */     return capabilityData.isResyncThrottlingSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isConfigAssistSupportedOnVc(ManagedObjectReference moRef) {
/* 292 */     return (getVcCapabilities(moRef)).isConfigAssistSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpdatesMgmtSupportedOnVc(ManagedObjectReference moRef) {
/* 302 */     return (getVcCapabilities(moRef)).isUpdatesMgmtSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWhatIfComplianceSupported(ManagedObjectReference moRef) {
/* 312 */     validateMoRef(moRef);
/* 313 */     return (getVcCapabilities(moRef)).isWhatIfComplianceSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getIsHistoricalCapacitySupported(ManagedObjectReference objectRef) throws Exception {
/* 323 */     ManagedObjectReference hosts[], clusterRef = BaseUtils.getCluster(objectRef);
/* 324 */     Validate.notNull(clusterRef);
/*     */ 
/*     */     
/*     */     try {
/* 328 */       hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/* 329 */       if (ArrayUtils.isEmpty((Object[])hosts)) {
/* 330 */         return false;
/*     */       }
/* 332 */     } catch (Exception e) {
/* 333 */       _logger.warn("Failed to list hosts, presumably empty cluster.", e);
/* 334 */       return false;
/*     */     }  byte b; int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 337 */     for (i = (arrayOfManagedObjectReference1 = hosts).length, b = 0; b < i; ) { ManagedObjectReference host = arrayOfManagedObjectReference1[b];
/* 338 */       boolean isSupported = getIsHistoricalCapacitySupportedOnHost(host);
/* 339 */       if (!isSupported) {
/* 340 */         return false;
/*     */       }
/*     */       b++; }
/*     */     
/* 344 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean getIsHistoricalCapacitySupportedOnHost(ManagedObjectReference hostRef) throws Exception {
/* 348 */     validateMoRef(hostRef);
/* 349 */     return (getCapabilities(hostRef)).isHistoricalCapacitySupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWhatIfSupported(ManagedObjectReference moRef) {
/* 359 */     return (getCapabilities(moRef)).isWhatIfSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPerfAnalysisSupportedOnVc(ManagedObjectReference moRef) {
/* 369 */     validateMoRef(moRef);
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCloudHealthSupportedOnVc(ManagedObjectReference moRef) {
/* 381 */     validateMoRef(moRef);
/* 382 */     return (getVcCapabilities(moRef)).isCloudHealthSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStretchedClusterSupportedOnHost(ManagedObjectReference moRef) {
/* 387 */     boolean isSupported = false;
/*     */     try {
/* 389 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 396 */     catch (Exception ex) {
/* 397 */       _logger
/* 398 */         .error("Failed to retrieve VSAN stretched cluster capability for host: " + 
/* 399 */           ex.getMessage());
/* 400 */       isSupported = false;
/*     */     } 
/*     */     
/* 403 */     return isSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isStretchedClusterSupportedOnCluster(ManagedObjectReference moRef) {
/* 408 */     boolean isSupported = true;
/*     */     try {
/* 410 */       Exception exception2, exception1 = null;
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
/*     */     }
/* 423 */     catch (Exception ex) {
/* 424 */       _logger.error("Failed to retrieve stretched cluster capabilities: " + 
/* 425 */           ex.getMessage());
/* 426 */       isSupported = false;
/*     */     } 
/* 428 */     return isSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAllFlashSupportedOnCluster(ManagedObjectReference clusterRef) {
/* 433 */     boolean isSupported = true;
/*     */     try {
/* 435 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 446 */     catch (Exception e) {
/* 447 */       _logger.error("Cannot retrieve isAllFlashSupported property: ", e);
/* 448 */       isSupported = false;
/*     */     } 
/*     */     
/* 451 */     return isSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAllFlashSupportedOnHost(ManagedObjectReference hostRef) {
/* 456 */     boolean isSupported = false;
/*     */     try {
/* 458 */       Exception exception2, exception1 = null;
/*     */ 
/*     */     
/*     */     }
/* 462 */     catch (Exception e) {
/* 463 */       _logger.error("Cannot retrieve isAllFlashSupported property: ", e);
/* 464 */       isSupported = false;
/*     */     } 
/*     */     
/* 467 */     return isSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLocalDataProtectionSupported(ManagedObjectReference moRef) {
/* 474 */     return (getCapabilities(moRef)).isLocalDataProtectionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLocalDataProtectionSupportedOnVc(ManagedObjectReference moRef) {
/* 481 */     return (getVcCapabilities(moRef)).isLocalDataProtectionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isArchiveDataProtectionSupported(ManagedObjectReference moRef) {
/* 488 */     return (getCapabilities(moRef)).isArchiveDataProtectionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isArchiveDataProtectionSupportedOnVc(ManagedObjectReference moRef) {
/* 495 */     return (getVcCapabilities(moRef)).isArchiveDataProtectionSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isResyncEnhancedApiSupported(ManagedObjectReference hostRef) {
/* 502 */     return (getCapabilities(hostRef)).isResyncEnhancedApiSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFileServiceSupported(ManagedObjectReference clusterRef) {
/* 509 */     return (getCapabilities(clusterRef)).isFileServiceSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNetworkPerfTestSupportedOnCluster(ManagedObjectReference clusterRef) {
/* 517 */     return (getCapabilities(clusterRef)).isNetworkPerfTestSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVsanVumIntegrationSupported(ManagedObjectReference clusterRef) {
/* 524 */     return (getVcCapabilities(clusterRef)).isVsanVumIntegrationSupported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWhatIfCapacitySupported(ManagedObjectReference clusterRef) {
/* 531 */     return (getVcCapabilities(clusterRef)).isWhatIfCapacitySupported;
/*     */   }
/*     */   
/*     */   public static boolean isVsanNestedFdsSupportedOnVc(ManagedObjectReference moRef) {
/* 535 */     return (getVcCapabilities(moRef)).isNestedFdsSupported;
/*     */   }
/*     */   
/*     */   public static boolean isRepairTimerInResyncStatsSupported(ManagedObjectReference hostRef) {
/* 539 */     return (getCapabilities(hostRef)).isRepairTimerInResyncStatsSupported;
/*     */   }
/*     */   
/*     */   public static boolean isPurgeInaccessibleVmSwapObjectsSupported(ManagedObjectReference clusterRef) {
/* 543 */     return (getCapabilities(clusterRef)).isPurgeInaccessibleVmSwapObjectsSupported;
/*     */   }
/*     */   
/*     */   public static boolean isRecreateDiskGroupSupported(ManagedObjectReference clusterRef) {
/* 547 */     return (getCapabilities(clusterRef)).isRecreateDiskGroupSupported;
/*     */   }
/*     */   
/*     */   public static boolean isUpdateVumReleaseCatalogOfflineSupported(ManagedObjectReference clusterRef) {
/* 551 */     return (getVcCapabilities(clusterRef)).isUpdateVumReleaseCatalogOfflineSupported;
/*     */   }
/*     */   
/*     */   public static boolean isAdvancedClusterSettingsSupported(ManagedObjectReference clusterRef) {
/* 555 */     return (getCapabilities(clusterRef)).isAdvancedClusterOptionsSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPerfDiagnosticModeSupported(ManagedObjectReference clusterRef) {
/* 560 */     VsanCapabilityData capabilityData = getCapabilities(clusterRef);
/* 561 */     _logger.debug("Performance Diagnostic Mode Supported: " + capabilityData.isPerfDiagnosticModeSupported);
/* 562 */     return capabilityData.isPerfDiagnosticModeSupported;
/*     */   }
/*     */   
/*     */   public static boolean isGetHclLastUpdateOnVcSupported(ManagedObjectReference vcRef) {
/* 566 */     VsanCapabilityData capabilityData = getVcCapabilities(vcRef);
/* 567 */     _logger.debug("GetHclLastUpdate methdo on VC Supported: " + capabilityData.isGetHclLastUpdateOnVcSupported);
/* 568 */     return capabilityData.isGetHclLastUpdateOnVcSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void validateMoRef(ManagedObjectReference moRef) {
/* 573 */     Validate.notNull(moRef);
/*     */ 
/*     */     
/* 576 */     String type = moRef.getType();
/* 577 */     if (!ClusterComputeResource.class.getSimpleName().equals(type) && 
/* 578 */       !HostSystem.class.getSimpleName().equals(type))
/* 579 */       throw new IllegalArgumentException(
/* 580 */           "Unsupported ManagedObjectReference type given: " + type); 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/VsanCapabilityUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */