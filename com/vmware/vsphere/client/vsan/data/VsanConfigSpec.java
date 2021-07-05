/*     */ package com.vmware.vsphere.client.vsan.data;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.encryption.KeyProviderId;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.vsan.cluster.ConfigInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanDiskMappingsConfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanFaultDomainSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanFaultDomainsConfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanHostDiskMapping;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanWitnessSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataEfficiencyConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.DataEncryptionConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsan.client.services.ProxygenSerializer.ElementType;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
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
/*     */ @data
/*     */ public class VsanConfigSpec
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public boolean enabledDedup;
/*     */   public boolean allowReducedRedundancy;
/*     */   public boolean enableEncryption;
/*     */   public boolean eraseDisksBeforeUse;
/*     */   public String kmipClusterId;
/*     */   public boolean largeScaleClusterSupport;
/*     */   public VsanStretchedClusterConfig stretchedClusterConfig;
/*     */   public boolean autoClaimDisks;
/*     */   @ElementType(VsanSemiAutoDiskMappingsSpec.class)
/*     */   public List<VsanSemiAutoDiskMappingsSpec> diskMappings;
/*     */   @ElementType(VsanFaultDomainSpec.class)
/*     */   public List<VsanFaultDomainSpec> faultDomainSpecs;
/*     */   
/*     */   public ReconfigSpec getReconfigSpec(ManagedObjectReference clusterRef, boolean hasEncryptionPermissions) throws Exception {
/*  93 */     ConfigInfo vsanClusterConfig = getVsanConfigInfo();
/*  94 */     DataEfficiencyConfig dedupConfig = null;
/*  95 */     if (VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef)) {
/*  96 */       dedupConfig = getDataEfficiencySpec();
/*     */     }
/*     */     
/*  99 */     DataEncryptionConfig encryptionConfig = null;
/* 100 */     if (VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef) && hasEncryptionPermissions) {
/* 101 */       encryptionConfig = getEncryptionSpec();
/*     */     }
/*     */     
/* 104 */     ReconfigSpec reconfigSpec = getBasicReconfigSpec();
/* 105 */     reconfigSpec.vsanClusterConfig = vsanClusterConfig;
/* 106 */     reconfigSpec.dataEfficiencyConfig = dedupConfig;
/* 107 */     reconfigSpec.dataEncryptionConfig = encryptionConfig;
/* 108 */     reconfigSpec.allowReducedRedundancy = Boolean.valueOf(this.allowReducedRedundancy);
/*     */     
/* 110 */     return reconfigSpec;
/*     */   }
/*     */   
/*     */   public ReconfigSpec getBasicReconfigSpec() throws Exception {
/* 114 */     ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 115 */     reconfigSpec.diskMappingSpec = getDiskMappingsConfigSpec();
/* 116 */     reconfigSpec.faultDomainsSpec = getFdSpec();
/* 117 */     reconfigSpec.modify = true;
/*     */     
/* 119 */     return reconfigSpec;
/*     */   }
/*     */   
/*     */   private ConfigInfo getVsanConfigInfo() {
/* 123 */     ConfigInfo vsanClusterConfig = new ConfigInfo();
/* 124 */     vsanClusterConfig.enabled = Boolean.valueOf(true);
/* 125 */     vsanClusterConfig.defaultConfig = new ConfigInfo.HostDefaultInfo();
/* 126 */     vsanClusterConfig.defaultConfig.autoClaimStorage = Boolean.valueOf(this.autoClaimDisks);
/* 127 */     return vsanClusterConfig;
/*     */   }
/*     */   
/*     */   private DataEncryptionConfig getEncryptionSpec() {
/* 131 */     DataEncryptionConfig encryptionConfig = new DataEncryptionConfig();
/* 132 */     encryptionConfig.encryptionEnabled = this.enableEncryption;
/* 133 */     if (encryptionConfig.encryptionEnabled) {
/* 134 */       if (!StringUtils.isEmpty(this.kmipClusterId)) {
/* 135 */         encryptionConfig.kmsProviderId = new KeyProviderId(this.kmipClusterId);
/*     */       }
/* 137 */       encryptionConfig.eraseDisksBeforeUse = Boolean.valueOf(this.eraseDisksBeforeUse);
/*     */     } 
/* 139 */     return encryptionConfig;
/*     */   }
/*     */   
/*     */   private DataEfficiencyConfig getDataEfficiencySpec() {
/* 143 */     DataEfficiencyConfig dedupConfig = new DataEfficiencyConfig();
/* 144 */     dedupConfig.dedupEnabled = this.enabledDedup;
/* 145 */     dedupConfig.compressionEnabled = Boolean.valueOf(dedupConfig.dedupEnabled);
/* 146 */     return dedupConfig;
/*     */   }
/*     */   
/*     */   private VsanDiskMappingsConfigSpec getDiskMappingsConfigSpec() {
/* 150 */     VsanDiskMappingsConfigSpec diskMappingsSpec = null;
/*     */     
/* 152 */     if (!this.autoClaimDisks && !CollectionUtils.isEmpty(this.diskMappings)) {
/* 153 */       diskMappingsSpec = new VsanDiskMappingsConfigSpec();
/* 154 */       diskMappingsSpec.hostDiskMappings = getHostsDisksMappings(this.diskMappings);
/*     */     } 
/* 156 */     return diskMappingsSpec;
/*     */   }
/*     */   
/*     */   private VsanFaultDomainsConfigSpec getFdSpec() {
/* 160 */     VsanFaultDomainsConfigSpec fdConfigSpec = null;
/* 161 */     if (this.stretchedClusterConfig != null) {
/* 162 */       fdConfigSpec = new VsanFaultDomainsConfigSpec();
/* 163 */       List<VsanFaultDomainSpec> fdSpecs = new ArrayList<>();
/* 164 */       fdSpecs.add(createFaultDomainSpec(this.stretchedClusterConfig.preferredSiteName, 
/* 165 */             this.stretchedClusterConfig.preferredSiteHosts));
/* 166 */       fdSpecs.add(createFaultDomainSpec(this.stretchedClusterConfig.secondarySiteName, 
/* 167 */             this.stretchedClusterConfig.secondarySiteHosts));
/* 168 */       fdConfigSpec.faultDomains = 
/* 169 */         fdSpecs.<VsanFaultDomainSpec>toArray(new VsanFaultDomainSpec[fdSpecs.size()]);
/* 170 */       fdConfigSpec.witness = getVsanWitnessSpec(this.stretchedClusterConfig);
/*     */     } 
/*     */     
/* 173 */     if (!CollectionUtils.isEmpty(this.faultDomainSpecs)) {
/* 174 */       fdConfigSpec = new VsanFaultDomainsConfigSpec();
/* 175 */       Map<String, List<ManagedObjectReference>> fdToHostsMap = new HashMap<>();
/* 176 */       List<VsanFaultDomainSpec> fdSpecs = new ArrayList<>();
/* 177 */       for (VsanFaultDomainSpec spec : this.faultDomainSpecs) {
/* 178 */         if (!fdToHostsMap.containsKey(spec.faultDomain)) {
/* 179 */           fdToHostsMap.put(spec.faultDomain, new ArrayList<>());
/*     */         }
/* 181 */         ((List<ManagedObjectReference>)fdToHostsMap.get(spec.faultDomain)).add(spec.hostRef);
/*     */       } 
/* 183 */       for (String fdName : fdToHostsMap.keySet()) {
/* 184 */         List<ManagedObjectReference> hosts = fdToHostsMap.get(fdName);
/* 185 */         fdSpecs.add(createFaultDomainSpec(fdName, hosts));
/*     */       } 
/*     */       
/* 188 */       fdConfigSpec.faultDomains = 
/* 189 */         fdSpecs.<VsanFaultDomainSpec>toArray(new VsanFaultDomainSpec[fdSpecs.size()]);
/*     */     } 
/* 191 */     return fdConfigSpec;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanWitnessSpec getVsanWitnessSpec(VsanStretchedClusterConfig stretchedClusterConfig) {
/* 196 */     VsanWitnessSpec result = new VsanWitnessSpec();
/* 197 */     result.host = stretchedClusterConfig.witnessHost;
/* 198 */     result.preferredFaultDomainName = stretchedClusterConfig.preferredSiteName;
/* 199 */     result.diskMapping = stretchedClusterConfig.witnessHostDiskMapping;
/*     */     
/* 201 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanFaultDomainSpec createFaultDomainSpec(String fdName, List<ManagedObjectReference> hosts) {
/* 206 */     VsanFaultDomainSpec faultDomainSpec = 
/* 207 */       new VsanFaultDomainSpec();
/* 208 */     faultDomainSpec.name = fdName;
/* 209 */     faultDomainSpec.hosts = hosts.<ManagedObjectReference>toArray(new ManagedObjectReference[hosts.size()]);
/*     */     
/* 211 */     return faultDomainSpec;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping[] getHostsDisksMappings(List<VsanSemiAutoDiskMappingsSpec> semiAutoDiskSpecs) {
/* 216 */     List<VsanHostDiskMapping> result = new ArrayList<>(semiAutoDiskSpecs.size());
/* 217 */     for (VsanSemiAutoDiskMappingsSpec spec : semiAutoDiskSpecs) {
/* 218 */       result.add(getDiskMappingsSpec(spec));
/*     */     }
/* 220 */     return result.<VsanHostDiskMapping>toArray(new VsanHostDiskMapping[result.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping getDiskMappingsSpec(VsanSemiAutoDiskMappingsSpec semiAutoDiskSpec) {
/* 226 */     List<ScsiDisk> cacheDisksToAdd = new ArrayList<>();
/* 227 */     List<ScsiDisk> storageDisksToAdd = new ArrayList<>();
/*     */     
/* 229 */     boolean isAllFlash = false; byte b; int i; VsanSemiAutoDiskSpec[] arrayOfVsanSemiAutoDiskSpec;
/* 230 */     for (i = (arrayOfVsanSemiAutoDiskSpec = semiAutoDiskSpec.disks).length, b = 0; b < i; ) { VsanSemiAutoDiskSpec disk = arrayOfVsanSemiAutoDiskSpec[b];
/* 231 */       if (ClaimOption.ClaimForCache == disk.claimOption) {
/* 232 */         cacheDisksToAdd.add(disk.disk);
/* 233 */       } else if (ClaimOption.ClaimForStorage == disk.claimOption) {
/* 234 */         storageDisksToAdd.add(disk.disk);
/* 235 */         isAllFlash = disk.markedAsFlash;
/*     */       } 
/*     */       b++; }
/*     */     
/* 239 */     VsanHostDiskMapping createSpec = createVsanHostDiskMapping(
/* 240 */         semiAutoDiskSpec.hostRef, cacheDisksToAdd, storageDisksToAdd, 
/* 241 */         isAllFlash);
/* 242 */     return createSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHostDiskMapping createVsanHostDiskMapping(ManagedObjectReference hostRef, List<ScsiDisk> cacheDisksToAdd, List<ScsiDisk> storageDisksToAdd, boolean isAllFlash) {
/* 248 */     VsanHostDiskMapping createSpec = new VsanHostDiskMapping();
/* 249 */     createSpec.host = hostRef;
/*     */     
/* 251 */     createSpec.cacheDisks = cacheDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[cacheDisksToAdd.size()]);
/* 252 */     createSpec.capacityDisks = storageDisksToAdd.<ScsiDisk>toArray(new ScsiDisk[storageDisksToAdd.size()]);
/* 253 */     createSpec.type = isAllFlash ? 
/* 254 */       VsanHostDiskMapping.VsanDiskGroupCreationType.allflash.toString() : 
/* 255 */       VsanHostDiskMapping.VsanDiskGroupCreationType.hybrid.toString();
/* 256 */     return createSpec;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/VsanConfigSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */