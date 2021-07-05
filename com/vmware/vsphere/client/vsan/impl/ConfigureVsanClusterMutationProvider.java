/*     */ package com.vmware.vsphere.client.vsan.impl;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.encryption.KeyProviderId;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.vsan.cluster.ConfigInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanDiskMappingsConfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanFaultDomainSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanFaultDomainsConfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanHostDiskMapping;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfsvcConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanWitnessSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataEfficiencyConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataEncryptionConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanExtendedConfig;
/*     */ import com.vmware.vsan.client.services.common.PermissionService;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.ClaimOption;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanConfigSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanFaultDomainSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanSemiAutoDiskMappingsSpec;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanSemiAutoDiskSpec;
/*     */ import com.vmware.vsphere.client.vsan.stretched.VsanStretchedClusterConfig;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ public class ConfigureVsanClusterMutationProvider {
/*  40 */   private static final Log _logger = LogFactory.getLog(ConfigureVsanClusterMutationProvider.class);
/*  41 */   private static final VsanProfiler _profiler = new VsanProfiler(ConfigureVsanClusterMutationProvider.class);
/*     */ 
/*     */   
/*     */   private static final long DEFAULT_OBJECT_REPAIR_TIMER = 60L;
/*     */ 
/*     */   
/*     */   private static final boolean DEFAULT_READ_SITE_LOCALITY = false;
/*     */   
/*     */   private static final boolean DEFAULT_ENABLE_CUSTOMIZED_SWAP_OBJECT = true;
/*     */   
/*     */   @Autowired
/*     */   private PermissionService permissionService;
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference configure(ManagedObjectReference clusterRef, VsanConfigSpec spec) throws Exception {
/*  57 */     if (!VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/*  58 */       throw new UnsupportedOperationException("This operation is not supported on the current VC instance.");
/*     */     }
/*     */     
/*  61 */     _logger.info("Invoke configure vsan cluster mutation operation for cluster: " + 
/*  62 */         clusterRef.getValue());
/*     */     
/*  64 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*  65 */     ReconfigSpec reconfigSpec = getReconfigSpec(clusterRef, spec);
/*     */     
/*  67 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ReconfigSpec getReconfigSpec(ManagedObjectReference clusterRef, VsanConfigSpec spec) throws Exception {
/*  78 */     ConfigInfo vsanClusterConfig = getVsanConfigInfo(spec);
/*  79 */     DataEfficiencyConfig dedupConfig = null;
/*  80 */     if (VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef)) {
/*  81 */       dedupConfig = getDataEfficiencySpec(spec);
/*     */     }
/*     */     
/*  84 */     DataEncryptionConfig encryptionConfig = null;
/*  85 */     boolean hasEncryptionPermissions = this.permissionService.hasPermissions(clusterRef, 
/*  86 */         new String[] { "Cryptographer.ManageKeys", "Cryptographer.ManageEncryptionPolicy", "Cryptographer.ManageKeyServers" });
/*     */     
/*  88 */     if (VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef) && hasEncryptionPermissions) {
/*  89 */       encryptionConfig = getEncryptionSpec(spec);
/*     */     }
/*  91 */     VsanDiskMappingsConfigSpec diskMappingsSpec = getDiskMappingsConfigSpec(spec);
/*  92 */     VsanFaultDomainsConfigSpec fdConfigSpec = getFdSpec(spec);
/*     */     
/*  94 */     ReconfigSpec reconfigSpec = new ReconfigSpec();
/*  95 */     reconfigSpec.vsanClusterConfig = vsanClusterConfig;
/*  96 */     reconfigSpec.dataEfficiencyConfig = dedupConfig;
/*  97 */     reconfigSpec.diskMappingSpec = diskMappingsSpec;
/*  98 */     reconfigSpec.dataEncryptionConfig = encryptionConfig;
/*  99 */     reconfigSpec.faultDomainsSpec = fdConfigSpec;
/* 100 */     reconfigSpec.modify = true;
/* 101 */     reconfigSpec.allowReducedRedundancy = Boolean.valueOf(spec.allowReducedRedundancy);
/*     */     
/* 103 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 104 */     ConfigInfoEx vsanConfig = vsanConfigSystem.getConfigInfoEx(clusterRef);
/* 105 */     if (vsanConfig != null) {
/* 106 */       if (vsanConfig.perfsvcConfig == null) {
/*     */         
/* 108 */         reconfigSpec.perfsvcConfig = new VsanPerfsvcConfig();
/* 109 */         reconfigSpec.perfsvcConfig.enabled = true;
/*     */       } else {
/*     */         
/* 112 */         reconfigSpec.perfsvcConfig = vsanConfig.perfsvcConfig;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 117 */     VsanExtendedConfig extendedConfig = 
/* 118 */       new VsanExtendedConfig(
/* 119 */         Long.valueOf(60L), 
/* 120 */         Boolean.valueOf(false), 
/* 121 */         Boolean.valueOf(true), 
/* 122 */         Boolean.valueOf(spec.largeScaleClusterSupport));
/* 123 */     reconfigSpec.setExtendedConfig(extendedConfig);
/*     */     
/* 125 */     return reconfigSpec;
/*     */   }
/*     */   
/*     */   private ConfigInfo getVsanConfigInfo(VsanConfigSpec configSpec) {
/* 129 */     ConfigInfo vsanClusterConfig = new ConfigInfo();
/* 130 */     vsanClusterConfig.enabled = Boolean.valueOf(true);
/* 131 */     vsanClusterConfig.defaultConfig = new ConfigInfo.HostDefaultInfo();
/* 132 */     vsanClusterConfig.defaultConfig.autoClaimStorage = Boolean.valueOf(configSpec.autoClaimDisks);
/* 133 */     return vsanClusterConfig;
/*     */   }
/*     */   
/*     */   private DataEncryptionConfig getEncryptionSpec(VsanConfigSpec configSpec) {
/* 137 */     DataEncryptionConfig encryptionConfig = new DataEncryptionConfig();
/* 138 */     encryptionConfig.encryptionEnabled = configSpec.enableEncryption;
/* 139 */     if (encryptionConfig.encryptionEnabled) {
/* 140 */       if (!StringUtils.isEmpty(configSpec.kmipClusterId)) {
/* 141 */         encryptionConfig.kmsProviderId = new KeyProviderId(configSpec.kmipClusterId);
/*     */       }
/* 143 */       encryptionConfig.eraseDisksBeforeUse = Boolean.valueOf(configSpec.eraseDisksBeforeUse);
/*     */     } 
/* 145 */     return encryptionConfig;
/*     */   }
/*     */   
/*     */   private DataEfficiencyConfig getDataEfficiencySpec(VsanConfigSpec configSpec) {
/* 149 */     DataEfficiencyConfig dedupConfig = new DataEfficiencyConfig();
/* 150 */     dedupConfig.dedupEnabled = configSpec.enabledDedup;
/* 151 */     dedupConfig.compressionEnabled = Boolean.valueOf(dedupConfig.dedupEnabled);
/* 152 */     return dedupConfig;
/*     */   }
/*     */   
/*     */   private VsanDiskMappingsConfigSpec getDiskMappingsConfigSpec(VsanConfigSpec configSpec) {
/* 156 */     VsanDiskMappingsConfigSpec diskMappingsSpec = null;
/*     */     
/* 158 */     if (!configSpec.autoClaimDisks && !CollectionUtils.isEmpty(configSpec.diskMappings)) {
/* 159 */       diskMappingsSpec = new VsanDiskMappingsConfigSpec();
/* 160 */       diskMappingsSpec.hostDiskMappings = getHostsDisksMappings(configSpec.diskMappings);
/*     */     } 
/* 162 */     return diskMappingsSpec;
/*     */   }
/*     */   
/*     */   private VsanFaultDomainsConfigSpec getFdSpec(VsanConfigSpec configSpec) {
/* 166 */     VsanFaultDomainsConfigSpec fdConfigSpec = null;
/* 167 */     if (configSpec.stretchedClusterConfig != null) {
/* 168 */       fdConfigSpec = new VsanFaultDomainsConfigSpec();
/* 169 */       List<VsanFaultDomainSpec> fdSpecs = new ArrayList<>();
/* 170 */       fdSpecs.add(createFaultDomainSpec(configSpec.stretchedClusterConfig.preferredSiteName, 
/* 171 */             configSpec.stretchedClusterConfig.preferredSiteHosts));
/* 172 */       fdSpecs.add(createFaultDomainSpec(configSpec.stretchedClusterConfig.secondarySiteName, 
/* 173 */             configSpec.stretchedClusterConfig.secondarySiteHosts));
/* 174 */       fdConfigSpec.faultDomains = 
/* 175 */         fdSpecs.<VsanFaultDomainSpec>toArray(new VsanFaultDomainSpec[fdSpecs.size()]);
/* 176 */       fdConfigSpec.witness = getVsanWitnessSpec(configSpec.stretchedClusterConfig);
/*     */     } 
/*     */     
/* 179 */     if (!CollectionUtils.isEmpty(configSpec.faultDomainSpecs)) {
/* 180 */       fdConfigSpec = new VsanFaultDomainsConfigSpec();
/* 181 */       Map<String, List<ManagedObjectReference>> fdToHostsMap = new HashMap<>();
/* 182 */       List<VsanFaultDomainSpec> fdSpecs = new ArrayList<>();
/* 183 */       for (VsanFaultDomainSpec spec : configSpec.faultDomainSpecs) {
/* 184 */         if (!fdToHostsMap.containsKey(spec.faultDomain)) {
/* 185 */           fdToHostsMap.put(spec.faultDomain, new ArrayList<>());
/*     */         }
/* 187 */         ((List<ManagedObjectReference>)fdToHostsMap.get(spec.faultDomain)).add(spec.hostRef);
/*     */       } 
/* 189 */       for (String fdName : fdToHostsMap.keySet()) {
/* 190 */         List<ManagedObjectReference> hosts = fdToHostsMap.get(fdName);
/* 191 */         fdSpecs.add(createFaultDomainSpec(fdName, hosts));
/*     */       } 
/*     */       
/* 194 */       fdConfigSpec.faultDomains = 
/* 195 */         fdSpecs.<VsanFaultDomainSpec>toArray(new VsanFaultDomainSpec[fdSpecs.size()]);
/*     */     } 
/* 197 */     return fdConfigSpec;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanWitnessSpec getVsanWitnessSpec(VsanStretchedClusterConfig stretchedClusterConfig) {
/* 202 */     VsanWitnessSpec result = new VsanWitnessSpec();
/* 203 */     result.host = stretchedClusterConfig.witnessHost;
/* 204 */     result.preferredFaultDomainName = stretchedClusterConfig.preferredSiteName;
/* 205 */     result.diskMapping = stretchedClusterConfig.witnessHostDiskMapping;
/*     */     
/* 207 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanFaultDomainSpec createFaultDomainSpec(String fdName, List<ManagedObjectReference> hosts) {
/* 212 */     VsanFaultDomainSpec faultDomainSpec = new VsanFaultDomainSpec();
/* 213 */     faultDomainSpec.name = fdName;
/* 214 */     faultDomainSpec.hosts = hosts.<ManagedObjectReference>toArray(new ManagedObjectReference[hosts.size()]);
/*     */     
/* 216 */     return faultDomainSpec;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping[] getHostsDisksMappings(List<VsanSemiAutoDiskMappingsSpec> semiAutoDiskSpecs) {
/* 221 */     List<VsanHostDiskMapping> result = new ArrayList<>();
/* 222 */     for (VsanSemiAutoDiskMappingsSpec spec : semiAutoDiskSpecs) {
/* 223 */       result.add(getDiskMappingsSpec(spec));
/*     */     }
/* 225 */     return result.<VsanHostDiskMapping>toArray(new VsanHostDiskMapping[result.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping getDiskMappingsSpec(VsanSemiAutoDiskMappingsSpec semiAutoDiskSpec) {
/* 231 */     List<ScsiDisk> cacheDisksToAdd = new ArrayList<>();
/* 232 */     List<ScsiDisk> storageDisksToAdd = new ArrayList<>();
/*     */     
/* 234 */     boolean isAllFlash = false; byte b; int i; VsanSemiAutoDiskSpec[] arrayOfVsanSemiAutoDiskSpec;
/* 235 */     for (i = (arrayOfVsanSemiAutoDiskSpec = semiAutoDiskSpec.disks).length, b = 0; b < i; ) { VsanSemiAutoDiskSpec disk = arrayOfVsanSemiAutoDiskSpec[b];
/* 236 */       if (ClaimOption.ClaimForCache == disk.claimOption) {
/* 237 */         cacheDisksToAdd.add(disk.disk);
/* 238 */       } else if (ClaimOption.ClaimForStorage == disk.claimOption) {
/* 239 */         storageDisksToAdd.add(disk.disk);
/* 240 */         isAllFlash = disk.markedAsFlash;
/*     */       } 
/*     */       b++; }
/*     */     
/* 244 */     VsanHostDiskMapping createSpec = createVsanHostDiskMapping(
/* 245 */         semiAutoDiskSpec.hostRef, cacheDisksToAdd, storageDisksToAdd, 
/* 246 */         isAllFlash);
/* 247 */     return createSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping createVsanHostDiskMapping(ManagedObjectReference hostRef, List<ScsiDisk> cacheDisksToAdd, List<ScsiDisk> storageDisksToAdd, boolean isAllFlash) {
/* 253 */     VsanHostDiskMapping createSpec = new VsanHostDiskMapping();
/* 254 */     createSpec.host = hostRef;
/*     */     
/* 256 */     createSpec.cacheDisks = cacheDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[cacheDisksToAdd.size()]);
/* 257 */     createSpec.capacityDisks = storageDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[storageDisksToAdd.size()]);
/* 258 */     createSpec.type = isAllFlash ? 
/* 259 */       VsanHostDiskMapping.VsanDiskGroupCreationType.allflash.toString() : 
/* 260 */       VsanHostDiskMapping.VsanDiskGroupCreationType.hybrid.toString();
/* 261 */     return createSpec;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/impl/ConfigureVsanClusterMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */