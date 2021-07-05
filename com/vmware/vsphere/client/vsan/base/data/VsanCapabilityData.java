/*     */ package com.vmware.vsphere.client.vsan.base.data;
/*     */ 
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapability;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilityStatus;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.util.cache.Cacheable;
/*     */ import org.apache.commons.lang.ArrayUtils;
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
/*     */ public class VsanCapabilityData
/*     */   implements Cacheable<VsanCapabilityData>
/*     */ {
/*     */   public boolean isDisconnected;
/*     */   public boolean isCapabilitiesSupported;
/*     */   public boolean isAllFlashSupported;
/*     */   public boolean isStretchedClusterSupported;
/*     */   public boolean isClusterConfigSupported;
/*     */   public boolean isDeduplicationAndCompressionSupported;
/*     */   public boolean isUpgradeSupported;
/*     */   public boolean isObjectIdentitiesSupported;
/*     */   public boolean isIscsiTargetsSupported;
/*     */   public boolean isWitnessManagementSupported;
/*     */   public boolean isPerfVerboseModeSupported;
/*     */   public boolean isPerfSvcAutoConfigSupported;
/*     */   public boolean isConfigAssistSupported;
/*     */   public boolean isUpdatesMgmtSupported;
/*     */   public boolean isWhatIfComplianceSupported;
/*     */   public boolean isPerfAnalysisSupported;
/*     */   public boolean isResyncThrottlingSupported;
/*     */   public boolean isEncryptionSupported;
/*     */   public boolean isWhatIfSupported;
/*     */   public boolean isLocalDataProtectionSupported;
/*     */   public boolean isArchiveDataProtectionSupported;
/*     */   public boolean isCloudHealthSupported;
/*     */   public boolean isResyncEnhancedApiSupported;
/*     */   public boolean isFileServiceSupported;
/*     */   public boolean isNetworkPerfTestSupported;
/*     */   public boolean isVsanVumIntegrationSupported;
/*     */   public boolean isWhatIfCapacitySupported;
/*     */   public boolean isHistoricalCapacitySupported;
/*     */   public boolean isNestedFdsSupported;
/*     */   public boolean isRepairTimerInResyncStatsSupported;
/*     */   public boolean isPurgeInaccessibleVmSwapObjectsSupported;
/*     */   public boolean isRecreateDiskGroupSupported;
/*     */   public boolean isUpdateVumReleaseCatalogOfflineSupported;
/*     */   public boolean isAdvancedClusterOptionsSupported;
/*     */   public boolean isPerfDiagnosticModeSupported;
/*     */   public boolean isAdvancedPerformanceSupported;
/*     */   public boolean isGetHclLastUpdateOnVcSupported;
/*     */   
/*     */   public VsanCapabilityData clone() {
/*  88 */     VsanCapabilityData clone = new VsanCapabilityData();
/*  89 */     clone.isDisconnected = this.isDisconnected;
/*     */     
/*  91 */     clone.isClusterConfigSupported = this.isClusterConfigSupported;
/*  92 */     clone.isDeduplicationAndCompressionSupported = this.isDeduplicationAndCompressionSupported;
/*  93 */     clone.isObjectIdentitiesSupported = this.isObjectIdentitiesSupported;
/*  94 */     clone.isUpgradeSupported = this.isUpgradeSupported;
/*  95 */     clone.isStretchedClusterSupported = this.isStretchedClusterSupported;
/*  96 */     clone.isAllFlashSupported = this.isAllFlashSupported;
/*  97 */     clone.isCapabilitiesSupported = this.isCapabilitiesSupported;
/*  98 */     clone.isIscsiTargetsSupported = this.isIscsiTargetsSupported;
/*  99 */     clone.isWitnessManagementSupported = this.isWitnessManagementSupported;
/* 100 */     clone.isPerfVerboseModeSupported = this.isPerfVerboseModeSupported;
/* 101 */     clone.isPerfSvcAutoConfigSupported = this.isPerfSvcAutoConfigSupported;
/*     */     
/* 103 */     clone.isConfigAssistSupported = this.isConfigAssistSupported;
/* 104 */     clone.isUpdatesMgmtSupported = this.isUpdatesMgmtSupported;
/* 105 */     clone.isWhatIfComplianceSupported = this.isWhatIfComplianceSupported;
/* 106 */     clone.isPerfAnalysisSupported = this.isPerfAnalysisSupported;
/* 107 */     clone.isResyncThrottlingSupported = this.isResyncThrottlingSupported;
/* 108 */     clone.isEncryptionSupported = this.isEncryptionSupported;
/* 109 */     clone.isWhatIfSupported = this.isWhatIfSupported;
/* 110 */     clone.isCloudHealthSupported = this.isCloudHealthSupported;
/*     */     
/* 112 */     clone.isLocalDataProtectionSupported = this.isLocalDataProtectionSupported;
/* 113 */     clone.isArchiveDataProtectionSupported = this.isArchiveDataProtectionSupported;
/* 114 */     clone.isResyncEnhancedApiSupported = this.isResyncEnhancedApiSupported;
/* 115 */     clone.isFileServiceSupported = this.isFileServiceSupported;
/*     */     
/* 117 */     clone.isNetworkPerfTestSupported = this.isNetworkPerfTestSupported;
/* 118 */     clone.isVsanVumIntegrationSupported = this.isVsanVumIntegrationSupported;
/* 119 */     clone.isWhatIfCapacitySupported = this.isWhatIfCapacitySupported;
/* 120 */     clone.isHistoricalCapacitySupported = this.isHistoricalCapacitySupported;
/* 121 */     clone.isNestedFdsSupported = this.isNestedFdsSupported;
/*     */     
/* 123 */     clone.isRepairTimerInResyncStatsSupported = this.isRepairTimerInResyncStatsSupported;
/* 124 */     clone.isPurgeInaccessibleVmSwapObjectsSupported = this.isPurgeInaccessibleVmSwapObjectsSupported;
/* 125 */     clone.isRecreateDiskGroupSupported = this.isRecreateDiskGroupSupported;
/* 126 */     clone.isUpdateVumReleaseCatalogOfflineSupported = this.isUpdateVumReleaseCatalogOfflineSupported;
/* 127 */     clone.isAdvancedClusterOptionsSupported = this.isAdvancedClusterOptionsSupported;
/* 128 */     clone.isPerfDiagnosticModeSupported = this.isPerfDiagnosticModeSupported;
/*     */     
/* 130 */     clone.isAdvancedPerformanceSupported = this.isAdvancedPerformanceSupported;
/* 131 */     clone.isGetHclLastUpdateOnVcSupported = this.isGetHclLastUpdateOnVcSupported;
/*     */     
/* 133 */     return clone;
/*     */   }
/*     */   
/*     */   public static VsanCapabilityData fromVsanCapability(VsanCapability vsanCapability) {
/* 137 */     VsanCapabilityData result = new VsanCapabilityData();
/* 138 */     if (vsanCapability.statuses != null) {
/* 139 */       byte b; int i; String[] arrayOfString; for (i = (arrayOfString = vsanCapability.statuses).length, b = 0; b < i; ) { String status = arrayOfString[b];
/* 140 */         if (status.equals(VsanCapabilityStatus.disconnected.toString())) {
/* 141 */           result.isDisconnected = true;
/*     */           break;
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 147 */     if (vsanCapability != null && 
/* 148 */       !ArrayUtils.isEmpty((Object[])vsanCapability.capabilities))
/* 149 */     { byte b; int i; String[] arrayOfString; for (i = (arrayOfString = vsanCapability.capabilities).length, b = 0; b < i; ) { String capability = arrayOfString[b]; String str1;
/* 150 */         switch ((str1 = capability).hashCode()) { case -2036548965: if (!str1.equals("whatifcapacity")) {
/*     */               break;
/*     */             }
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
/* 228 */             result.isWhatIfCapacitySupported = true;
/*     */             break;
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
/*     */           case -1854461844:
/*     */             if (!str1.equals("updatevumreleasecatalogoffline")) {
/*     */               break;
/*     */             }
/* 249 */             result.isUpdateVumReleaseCatalogOfflineSupported = true; break;case -1806256184: if (!str1.equals("perfsvcautoconfig")) break;  result.isPerfSvcAutoConfigSupported = true; break;case -1804936456: if (!str1.equals("throttleresync"))
/*     */               break;  result.isResyncThrottlingSupported = true; break;case -1544169576: if (!str1.equals("netperftest"))
/*     */               break;  result.isNetworkPerfTestSupported = true; break;case -1512632445: if (!str1.equals("encryption"))
/*     */               break;  result.isEncryptionSupported = true; break;case -1379946052: if (!str1.equals("clusterconfig"))
/*     */               break;  result.isClusterConfigSupported = true; break;case -1364523474: if (!str1.equals("localdataprotection"))
/*     */               break;  result.isLocalDataProtectionSupported = true; break;case -1195883754: if (!str1.equals("performanceforsupport"))
/* 255 */               break;  result.isAdvancedPerformanceSupported = true; break;case -1162221165: if (!str1.equals("dataefficiency")) break;  result.isDeduplicationAndCompressionSupported = true; break;case -1160008507: if (!str1.equals("perfanalysis")) break;  result.isPerfAnalysisSupported = true; break;case -936991535: if (!str1.equals("cloudhealth")) break;  result.isCloudHealthSupported = true; break;case -831219140: if (!str1.equals("witnessmanagement"))
/*     */               break;  result.isWitnessManagementSupported = true; break;case -783669992: if (!str1.equals("capability"))
/*     */               break;  result.isCapabilitiesSupported = true; break;case -713444186: if (!str1.equals("gethcllastupdateonvc"))
/* 258 */               break;  result.isGetHclLastUpdateOnVcSupported = true; break;case -525799692: if (!str1.equals("repairtimerinresyncstats")) break;  result.isRepairTimerInResyncStatsSupported = true; break;case -458757541: if (!str1.equals("objectidentities")) break;  result.isObjectIdentitiesSupported = true; break;case -335589684: if (!str1.equals("genericnestedfd")) break;  result.isNestedFdsSupported = true; break;case -231171556: if (!str1.equals("upgrade")) break;  result.isUpgradeSupported = true; break;case -144980092: if (!str1.equals("historicalcapacity")) break;  result.isHistoricalCapacitySupported = true; break;case -86618386: if (!str1.equals("perfsvcverbosemode")) break;  result.isPerfVerboseModeSupported = true; break;case 26254577: if (!str1.equals("archivaldataprotection")) break;  result.isArchiveDataProtectionSupported = true; break;case 92063072: if (!str1.equals("complianceprecheck")) break;  result.isWhatIfComplianceSupported = true; break;case 289188182: if (!str1.equals("enhancedresyncapi")) break;  result.isResyncEnhancedApiSupported = true; break;case 560333555: if (!str1.equals("recreatediskgroup")) break;  result.isRecreateDiskGroupSupported = true; break;case 599591979: if (!str1.equals("configassist")) break;  result.isConfigAssistSupported = true; break;case 613526532: if (!str1.equals("purgeinaccessiblevmswapobjects")) break;  result.isPurgeInaccessibleVmSwapObjectsSupported = true; break;case 1114122305: if (!str1.equals("decomwhatif")) break;  result.isWhatIfSupported = true; break;case 1252691946: if (!str1.equals("fullStackFw"))
/*     */               break;  result.isVsanVumIntegrationSupported = true; break;case 1265995522: if (!str1.equals("clusteradvancedoptions"))
/*     */               break;  result.isAdvancedClusterOptionsSupported = true; break;case 1338309188: if (!str1.equals("firmwareupdate"))
/*     */               break;  result.isUpdatesMgmtSupported = true; break;case 1344181014: if (!str1.equals("stretchedcluster"))
/*     */               break;  result.isStretchedClusterSupported = true; break;case 1567155603: if (!str1.equals("iscsitargets"))
/*     */               break;  result.isIscsiTargetsSupported = true; break;case 1804489455: if (!str1.equals("allflash"))
/*     */               break;  result.isAllFlashSupported = true; break;case 2101078474: if (!str1.equals("diagnosticmode"))
/* 265 */               break;  result.isPerfDiagnosticModeSupported = true; break; }  b++; }  }  return result;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanCapabilityData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */