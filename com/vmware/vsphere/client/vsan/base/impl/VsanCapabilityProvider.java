/*     */ package com.vmware.vsphere.client.vsan.base.impl;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanCapabilityData;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanCapabilityProvider
/*     */ {
/*  23 */   private static final Log _logger = LogFactory.getLog(VsanCapabilityProvider.class);
/*     */   
/*     */   @Autowired
/*     */   private ObjectReferenceService objectRefService;
/*     */   
/*     */   @TsService
/*     */   public VsanCapabilityData getVcCapabilityData(ManagedObjectReference moRef) throws Exception {
/*  30 */     VsanCapabilityData capabilityData = new VsanCapabilityData();
/*     */     
/*     */     try {
/*  33 */       capabilityData = VsanCapabilityUtils.getVcCapabilities(moRef);
/*  34 */     } catch (Exception ex) {
/*  35 */       _logger.error("Cannot load capabilities", ex);
/*     */     } 
/*     */     
/*  38 */     return capabilityData;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanCapabilityData getClusterCapabilityData(ManagedObjectReference moRef) throws Exception {
/*  43 */     VsanCapabilityData capabilityData = new VsanCapabilityData();
/*     */     
/*     */     try {
/*  46 */       capabilityData = VsanCapabilityUtils.getCapabilities(moRef);
/*  47 */     } catch (Exception ex) {
/*  48 */       _logger.error("Cannot load capabilities", ex);
/*     */     } 
/*     */     
/*  51 */     return capabilityData;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanCapabilityData getHostCapabilityData(ManagedObjectReference moRef) {
/*  56 */     VsanCapabilityData capabilityData = new VsanCapabilityData();
/*     */     
/*     */     try {
/*  59 */       capabilityData = VsanCapabilityUtils.getCapabilities(moRef);
/*  60 */     } catch (Exception ex) {
/*  61 */       _logger.error("Cannot load capabilities", ex);
/*     */     } 
/*     */     
/*  64 */     return capabilityData;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public Map<String, VsanCapabilityData> getHostsCapabilitiyData(ManagedObjectReference[] hostRefs) {
/*  69 */     Map<String, VsanCapabilityData> result = new HashMap<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/*  70 */     for (i = (arrayOfManagedObjectReference = hostRefs).length, b = 0; b < i; ) { ManagedObjectReference hostRef = arrayOfManagedObjectReference[b];
/*  71 */       result.put(this.objectRefService.getUid(hostRef), getHostCapabilityData(hostRef)); b++; }
/*     */     
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsDeduplicationSupported(ManagedObjectReference clusterRef) {
/*  78 */     boolean dedupSupported = 
/*  79 */       VsanCapabilityUtils.isDeduplicationAndCompressionSupported(clusterRef);
/*  80 */     return dedupSupported;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsEncryptionSupported(ManagedObjectReference clusterRef) {
/*  85 */     boolean encryptionSupported = 
/*  86 */       VsanCapabilityUtils.isEncryptionSupported(clusterRef);
/*  87 */     return encryptionSupported;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsLocalDataProtectionSupportedOnVc(ManagedObjectReference objectRef) throws Exception {
/*  92 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/*  93 */     Validate.notNull(clusterRef);
/*     */     
/*  95 */     boolean localDpSupported = (getVcCapabilityData(clusterRef)).isLocalDataProtectionSupported;
/*  96 */     return localDpSupported;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsHistoricalCapacitySupported(ManagedObjectReference objectRef) throws Exception {
/* 101 */     return VsanCapabilityUtils.getIsHistoricalCapacitySupported(objectRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsPerfVerboseModeSupported(ManagedObjectReference clusterRef) throws Exception {
/* 106 */     return VsanCapabilityUtils.isPerfVerboseModeSupportedOnVc(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsPerfNetworkDiagnosticModeSupported(ManagedObjectReference clusterRef) throws Exception {
/* 111 */     return VsanCapabilityUtils.isPerfDiagnosticModeSupported(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean isAdvancedClusterSettingsSupported(ManagedObjectReference clusterRef) throws Exception {
/* 116 */     return VsanCapabilityUtils.isAdvancedClusterSettingsSupported(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean isRecreateDiskGroupSupported(ManagedObjectReference clusterRef) throws Exception {
/* 121 */     return VsanCapabilityUtils.isRecreateDiskGroupSupported(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean isPurgeInaccessibleVmSwapObjectsSupported(ManagedObjectReference moRef) throws Exception {
/* 126 */     return VsanCapabilityUtils.isPurgeInaccessibleVmSwapObjectsSupported(moRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean isUpdateVumReleaseCatalogOfflineSupported(ManagedObjectReference clusterRef) throws Exception {
/* 131 */     return VsanCapabilityUtils.isUpdateVumReleaseCatalogOfflineSupported(clusterRef);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/impl/VsanCapabilityProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */