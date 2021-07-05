/*     */ package com.vmware.vsan.client.services.hci.model;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.SDDCBase;
/*     */ import com.vmware.vim.binding.vim.cluster.ConfigSpecEx;
/*     */ import com.vmware.vim.binding.vim.cluster.DasConfigInfo;
/*     */ import com.vmware.vim.binding.vim.cluster.DrsConfigInfo;
/*     */ import com.vmware.vim.binding.vim.dvs.DistributedVirtualPort;
/*     */ import com.vmware.vim.binding.vim.dvs.DistributedVirtualPortgroup;
/*     */ import com.vmware.vim.binding.vim.dvs.VmwareDistributedVirtualSwitch;
/*     */ import com.vmware.vim.binding.vim.host.DateTimeConfig;
/*     */ import com.vmware.vim.binding.vim.host.NtpConfig;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfsvcConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanExtendedConfig;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanConfigSpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @data
/*     */ public class ClusterConfigData
/*     */ {
/*     */   private static final String OBJECT_NAME_SEPARATOR = " ";
/*     */   private static final long DEFAULT_OBJECT_REPAIR_TIMER = 60L;
/*     */   public BasicClusterConfigData basicConfig;
/*     */   public boolean enableAdmissionControl;
/*     */   public VsanConfigSpec vsanConfigSpec;
/*     */   public int hostFTT;
/*     */   public boolean enableHostMonitoring;
/*     */   public boolean enableVmMonitoring;
/*     */   public DrsAutoLevel automationLevel;
/*     */   public int migrationThreshold;
/*     */   public boolean enableEVC;
/*     */   public String selectedEvcMode;
/*  50 */   public LockdownMode lockdownMode = LockdownMode.DISABLED;
/*     */   
/*     */   public String ntpServer;
/*     */   
/*     */   public boolean optOutOfNetConfig;
/*     */   
/*     */   public DvsSpec[] dvsSpecs;
/*     */   public NetServiceConfig[] netServiceConfigs;
/*     */   public boolean largeScaleClusterSupport;
/*     */   
/*     */   public ClusterComputeResource.HCIConfigSpec getHciConfigSpec(ManagedObjectReference clusterRef, boolean hasEncryptionPermissions) throws Exception {
/*  61 */     ClusterComputeResource.HCIConfigSpec hciConfigSpec = new ClusterComputeResource.HCIConfigSpec();
/*  62 */     if (this.basicConfig.vsanEnabled) {
/*  63 */       ReconfigSpec reconfigSpec = this.vsanConfigSpec.getReconfigSpec(clusterRef, hasEncryptionPermissions);
/*  64 */       reconfigSpec.perfsvcConfig = new VsanPerfsvcConfig();
/*  65 */       reconfigSpec.perfsvcConfig.enabled = true;
/*     */ 
/*     */       
/*  68 */       VsanExtendedConfig extendedConfig = new VsanExtendedConfig(
/*  69 */           Long.valueOf(60L), 
/*  70 */           Boolean.valueOf(false), 
/*  71 */           Boolean.valueOf(true), 
/*  72 */           Boolean.valueOf(this.largeScaleClusterSupport));
/*  73 */       reconfigSpec.setExtendedConfig(extendedConfig);
/*  74 */       hciConfigSpec.vSanConfigSpec = (SDDCBase)reconfigSpec;
/*     */     } 
/*     */     
/*  77 */     hciConfigSpec.vcProf = getVcProfile();
/*  78 */     hciConfigSpec.dvsProf = getDvsProfiles();
/*  79 */     hciConfigSpec.hostConfigProfile = getHostConfigProfile();
/*     */     
/*  81 */     return hciConfigSpec;
/*     */   }
/*     */   
/*     */   private ClusterComputeResource.VCProfile getVcProfile() {
/*  85 */     ConfigSpecEx configSpec = new ConfigSpecEx();
/*     */ 
/*     */     
/*  88 */     configSpec.inHciWorkflow = null;
/*     */     
/*  90 */     if (this.basicConfig.haEnabled) {
/*  91 */       DasConfigInfo dasConfig = new DasConfigInfo();
/*  92 */       dasConfig.enabled = Boolean.valueOf(true);
/*  93 */       dasConfig.admissionControlEnabled = Boolean.valueOf(this.enableAdmissionControl);
/*  94 */       dasConfig.failoverLevel = Integer.valueOf(this.hostFTT);
/*  95 */       dasConfig.hostMonitoring = (this.enableHostMonitoring ? 
/*  96 */         DasConfigInfo.ServiceState.enabled : DasConfigInfo.ServiceState.disabled).toString();
/*  97 */       dasConfig.vmMonitoring = (this.enableVmMonitoring ? 
/*  98 */         DasConfigInfo.VmMonitoringState.vmMonitoringOnly : 
/*  99 */         DasConfigInfo.VmMonitoringState.vmMonitoringDisabled).toString();
/* 100 */       configSpec.dasConfig = dasConfig;
/*     */     } 
/* 102 */     if (this.basicConfig.drsEnabled) {
/* 103 */       DrsConfigInfo drsConfig = new DrsConfigInfo();
/* 104 */       drsConfig.enabled = Boolean.valueOf(true);
/* 105 */       drsConfig.defaultVmBehavior = getDrsBehavior(this.automationLevel);
/* 106 */       drsConfig.vmotionRate = Integer.valueOf(6 - this.migrationThreshold);
/* 107 */       configSpec.drsConfig = drsConfig;
/*     */     } 
/*     */     
/* 110 */     ClusterComputeResource.VCProfile vcProfile = new ClusterComputeResource.VCProfile();
/* 111 */     vcProfile.clusterSpec = configSpec;
/* 112 */     if (this.enableEVC) {
/* 113 */       vcProfile.evcModeKey = this.selectedEvcMode;
/*     */     }
/* 115 */     return vcProfile;
/*     */   }
/*     */   
/*     */   private DrsConfigInfo.DrsBehavior getDrsBehavior(DrsAutoLevel autoLevel) {
/* 119 */     switch (autoLevel) {
/*     */       case null:
/* 121 */         return DrsConfigInfo.DrsBehavior.fullyAutomated;
/*     */       case MANUAL:
/* 123 */         return DrsConfigInfo.DrsBehavior.manual;
/*     */       case PARTIALLY_AUTOMATED:
/* 125 */         return DrsConfigInfo.DrsBehavior.partiallyAutomated;
/*     */     } 
/* 127 */     return DrsConfigInfo.DrsBehavior.fullyAutomated;
/*     */   }
/*     */   
/*     */   public ClusterComputeResource.DvsProfile[] getDvsProfiles() throws Exception {
/* 131 */     List<ClusterComputeResource.DvsProfile> dvsProfiles = new ArrayList<>();
/*     */     
/* 133 */     if (!this.optOutOfNetConfig && this.dvsSpecs != null) {
/* 134 */       byte b; int i; DvsSpec[] arrayOfDvsSpec; for (i = (arrayOfDvsSpec = this.dvsSpecs).length, b = 0; b < i; ) { DvsSpec dvsSpec = arrayOfDvsSpec[b];
/* 135 */         ClusterComputeResource.DvsProfile profile = new ClusterComputeResource.DvsProfile();
/* 136 */         if (dvsSpec.existingDvsMor != null) {
/* 137 */           profile.dvSwitch = dvsSpec.existingDvsMor;
/*     */         } else {
/* 139 */           profile.dvsName = dvsSpec.name;
/*     */         } 
/*     */         
/* 142 */         profile.pnicDevices = getPnicDevices(dvsSpec);
/* 143 */         profile.dvPortgroupMapping = getDvPortgroupMappings(dvsSpec, this.netServiceConfigs);
/* 144 */         dvsProfiles.add(profile);
/*     */         b++; }
/*     */     
/*     */     } 
/* 148 */     return dvsProfiles.<ClusterComputeResource.DvsProfile>toArray(new ClusterComputeResource.DvsProfile[0]);
/*     */   }
/*     */   
/*     */   private String[] getPnicDevices(DvsSpec dvsSpec) {
/* 152 */     if (dvsSpec.adapters != null) {
/* 153 */       String[] result = new String[dvsSpec.adapters.length];
/* 154 */       for (int i = 0; i < dvsSpec.adapters.length; i++) {
/* 155 */         result[i] = (dvsSpec.adapters[i]).deviceName;
/*     */       }
/*     */       
/* 158 */       return result;
/*     */     } 
/*     */     
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping[] getDvPortgroupMappings(DvsSpec dvsSpec, NetServiceConfig[] netServiceConfigs) {
/* 166 */     if (dvsSpec.services != null) {
/*     */       
/* 168 */       List<ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping> result = new ArrayList<>(); byte b; int i; Service[] arrayOfService;
/* 169 */       for (i = (arrayOfService = dvsSpec.services).length, b = 0; b < i; ) { Service service = arrayOfService[b];
/*     */ 
/*     */ 
/*     */         
/* 173 */         if (service != Service.MANAGEMENT) {
/* 174 */           ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping mapping = new ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping();
/* 175 */           mapping.service = service.getText();
/*     */           
/* 177 */           NetServiceConfig netServiceConfig = getNetServiceConfig(netServiceConfigs, service);
/* 178 */           if (netServiceConfig != null) {
/*     */ 
/*     */ 
/*     */             
/* 182 */             if (netServiceConfig.existingDvpgMor != null) {
/* 183 */               mapping.dvPortgroup = netServiceConfig.existingDvpgMor;
/*     */             } else {
/* 185 */               mapping.dvPortgroupSpec = new DistributedVirtualPortgroup.ConfigSpec();
/* 186 */               mapping.dvPortgroupSpec.name = netServiceConfig.dvpgName;
/* 187 */               mapping.dvPortgroupSpec.type = "earlyBinding";
/*     */               
/* 189 */               if (netServiceConfig.useVlan) {
/* 190 */                 VmwareDistributedVirtualSwitch.VlanIdSpec vlanIdSpec = new VmwareDistributedVirtualSwitch.VlanIdSpec();
/* 191 */                 vlanIdSpec.vlanId = netServiceConfig.vlan;
/* 192 */                 VmwareDistributedVirtualSwitch.VmwarePortConfigPolicy configPolicy = 
/* 193 */                   new VmwareDistributedVirtualSwitch.VmwarePortConfigPolicy();
/* 194 */                 configPolicy.vlan = (VmwareDistributedVirtualSwitch.VlanSpec)vlanIdSpec;
/*     */                 
/* 196 */                 mapping.dvPortgroupSpec.defaultPortConfig = (DistributedVirtualPort.Setting)configPolicy;
/*     */               } 
/*     */             } 
/*     */             
/* 200 */             result.add(mapping);
/*     */           } 
/*     */         }  b++; }
/*     */       
/* 204 */       return result.<ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping>toArray(new ClusterComputeResource.DvsProfile.DVPortgroupSpecToServiceMapping[0]);
/*     */     } 
/*     */     
/* 207 */     return null;
/*     */   }
/*     */   
/*     */   private NetServiceConfig getNetServiceConfig(NetServiceConfig[] configs, Service service) {
/* 211 */     if (configs != null) {
/* 212 */       byte b; int i; NetServiceConfig[] arrayOfNetServiceConfig; for (i = (arrayOfNetServiceConfig = configs).length, b = 0; b < i; ) { NetServiceConfig config = arrayOfNetServiceConfig[b];
/* 213 */         if (config.service == service) {
/* 214 */           return config;
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/* 219 */     return null;
/*     */   }
/*     */   
/*     */   private ClusterComputeResource.HostConfigurationProfile getHostConfigProfile() {
/* 223 */     ClusterComputeResource.HostConfigurationProfile result = new ClusterComputeResource.HostConfigurationProfile();
/*     */     
/* 225 */     if (!StringUtils.isBlank(this.ntpServer)) {
/* 226 */       DateTimeConfig timeConfig = new DateTimeConfig();
/* 227 */       timeConfig.ntpConfig = new NtpConfig();
/* 228 */       String[] ntpServers = StringUtils.split(this.ntpServer, ",");
/* 229 */       for (int i = 0; i < ntpServers.length; i++) {
/* 230 */         ntpServers[i] = ntpServers[i].trim();
/*     */       }
/* 232 */       timeConfig.ntpConfig.server = ntpServers;
/* 233 */       result.dateTimeConfig = timeConfig;
/*     */     } 
/* 235 */     result.lockdownMode = this.lockdownMode.getVmodlLockdownMode();
/*     */     
/* 237 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClusterComputeResource.HostConfigurationInput[] getHostConfigurationInputs(List<HostInCluster> hosts) throws Exception {
/* 242 */     List<ClusterComputeResource.HostConfigurationInput> result = new ArrayList<>();
/* 243 */     if (this.basicConfig.hciWorkflowState == HciWorkflowState.DONE || (
/* 244 */       this.basicConfig.hciWorkflowState == HciWorkflowState.IN_PROGRESS && 
/* 245 */       !this.optOutOfNetConfig && this.netServiceConfigs != null)) {
/* 246 */       for (HostInCluster host : hosts) {
/* 247 */         result.add(getHostConfigurationInput(host));
/*     */       }
/*     */     }
/*     */     
/* 251 */     return (result.size() > 0) ? result.<ClusterComputeResource.HostConfigurationInput>toArray(new ClusterComputeResource.HostConfigurationInput[0]) : null;
/*     */   }
/*     */   
/*     */   private ClusterComputeResource.HostConfigurationInput getHostConfigurationInput(HostInCluster host) {
/* 255 */     ClusterComputeResource.HostConfigurationInput result = new ClusterComputeResource.HostConfigurationInput();
/* 256 */     result.host = host.moRef;
/* 257 */     result.hostVmkNics = getHostVmkNicInfos(host.name);
/*     */     
/* 259 */     return result;
/*     */   }
/*     */   
/*     */   private ClusterComputeResource.HostVmkNicInfo[] getHostVmkNicInfos(String hostName) {
/* 263 */     List<ClusterComputeResource.HostVmkNicInfo> result = new ArrayList<>();
/* 264 */     if (this.netServiceConfigs != null) {
/* 265 */       byte b; int i; NetServiceConfig[] arrayOfNetServiceConfig; for (i = (arrayOfNetServiceConfig = this.netServiceConfigs).length, b = 0; b < i; ) { NetServiceConfig netServiceConfig = arrayOfNetServiceConfig[b];
/* 266 */         result.add(netServiceConfig.getHostVmkNicInfo(hostName));
/*     */         b++; }
/*     */     
/*     */     } 
/* 270 */     return result.<ClusterComputeResource.HostVmkNicInfo>toArray(new ClusterComputeResource.HostVmkNicInfo[0]);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/ClusterConfigData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */