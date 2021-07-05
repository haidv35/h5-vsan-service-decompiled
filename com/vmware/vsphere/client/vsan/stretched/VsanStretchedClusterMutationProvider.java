/*     */ package com.vmware.vsphere.client.vsan.stretched;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.VirtualNic;
/*     */ import com.vmware.vim.binding.vim.host.VirtualNicManager;
/*     */ import com.vmware.vim.binding.vim.host.VirtualNicManagerInfo;
/*     */ import com.vmware.vim.binding.vim.host.VsanSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VSANStretchedClusterFaultDomainConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanSemiAutoClaimDisksData;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class VsanStretchedClusterMutationProvider
/*     */ {
/*     */   private static final String VNIC_PREFIX = "vim.host.VirtualNic-";
/*     */   private static final String VIRTUAL_NIC_MANAGER_INFO_PROPERTY = "config.virtualNicManagerInfo";
/*     */   private static final String VIRTUAL_NIC_PROPERTY = "config.network.vnic";
/*     */   private static final String PARENT_PROPERTY = "parent";
/*     */   private static final String CONNECTION_STATE_PROPERTY = "runtime.connectionState";
/*     */   private static final String POWER_STATE_PROPERTY = "runtime.powerState";
/*     */   private static final String MAINTENANCE_MODE_PROPERTY = "runtime.inMaintenanceMode";
/*     */   private static final String DISABLED_METHODS_PROPERTY = "disabledMethod";
/*     */   private static final String VSAN_SEMI_AUTO_DISKS_PROPERTY_NAME = "vsanSemiAutoClaimDisksData";
/*     */   private static final String ENTER_MAINTENANCE_MODE_DISABLED_METHOD = "EnterMaintenanceMode_Task";
/*     */   private static final String EXIT_MAINTENANCE_MODE_DISABLED_METHOD = "ExitMaintenanceMode_Task";
/*  54 */   private static final Log _logger = LogFactory.getLog(VsanStretchedClusterMutationProvider.class);
/*     */   
/*  56 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanStretchedClusterMutationProvider.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public WitnessHostValidationResult validateWitnessHost(ManagedObjectReference clusterRef, WitnessHostSpec spec) throws Exception {
/*  68 */     return getWitnessValidationResult(clusterRef, spec.witnessHost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference configureStretchedCluster(ManagedObjectReference clusterRef, VsanStretchedClusterConfig spec) throws Exception {
/*  79 */     _logger.info("Invoke configure stretched cluster mutation operation for cluster: " + 
/*  80 */         clusterRef.getServerGuid());
/*     */     
/*  82 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/*  83 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/*     */     
/*  85 */     _logger.info("Configuring stretched cluster with witness `" + 
/*  86 */         spec.witnessHost.getServerGuid() + "` and preffered fault domain `" + 
/*  87 */         spec.preferredSiteName + "`");
/*  88 */     ManagedObjectReference stretchedClusterTask = null;
/*  89 */     DiskMapping diskMapping = null;
/*  90 */     if (spec.witnessHostDiskMapping != null) {
/*  91 */       diskMapping = copyProperties(spec.witnessHostDiskMapping);
/*     */     }
/*  93 */     if (spec.isFaultDomainConfigurationChanged) {
/*  94 */       Exception exception2; VSANStretchedClusterFaultDomainConfig fdConfig = new VSANStretchedClusterFaultDomainConfig();
/*  95 */       fdConfig.firstFdName = spec.preferredSiteName;
/*  96 */       fdConfig.firstFdHosts = spec.preferredSiteHosts.<ManagedObjectReference>toArray(
/*  97 */           new ManagedObjectReference[spec.preferredSiteHosts.size()]);
/*  98 */       fdConfig.secondFdName = spec.secondarySiteName;
/*  99 */       fdConfig.secondFdHosts = spec.secondarySiteHosts.<ManagedObjectReference>toArray(
/* 100 */           new ManagedObjectReference[spec.secondarySiteHosts.size()]);
/* 101 */       Exception exception1 = null;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 106 */       Exception exception2, exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 111 */     stretchedClusterTask.setServerGuid(clusterRef.getServerGuid());
/* 112 */     return stretchedClusterTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference setPreferredFaultDomain(ManagedObjectReference clusterRef, PreferredFaultDomainData preferredFaultDomainData) throws Exception {
/* 124 */     _logger.info("Invoke set preferred fault domain mutation operation for cluster: " + 
/* 125 */         clusterRef.getServerGuid());
/*     */     
/* 127 */     Exception exception1 = null, exception2 = null;
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
/* 138 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference setWitnessHost(ManagedObjectReference clusterRef, VsanWitnessConfig witnessConfig) throws Exception {
/* 148 */     _logger.info("Invoke change witness host mutation operation for cluster: " + 
/* 149 */         clusterRef);
/*     */     
/* 151 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 152 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/* 153 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 161 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference removeWitnessHost(ManagedObjectReference clusterRef, WitnessHostSpec spec) throws Exception {
/* 171 */     _logger.info("Invoke remove witness host mutation operation for cluster: " + 
/* 172 */         clusterRef.getServerGuid());
/* 173 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 174 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/* 175 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 181 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean reconcileUnicastAgents(ManagedObjectReference clusterRef, ResyncUnicastAgentSpec spec) throws Exception {
/* 190 */     _logger.info("Invoke resync unicast agents mutation operation for cluster: " + 
/* 191 */         clusterRef.getServerGuid());
/* 192 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 198 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   private WitnessHostValidationResult getWitnessValidationResult(ManagedObjectReference clusterRef, ManagedObjectReference hostRef) throws Exception {
/* 203 */     if (hostRef == null) {
/* 204 */       return null;
/*     */     }
/* 206 */     PropertyValue[] propValue = QueryUtil.getProperties(
/* 207 */         hostRef, 
/* 208 */         new String[] { "config.virtualNicManagerInfo", 
/* 209 */           "config.network.vnic", 
/* 210 */           "parent", 
/* 211 */           "runtime.connectionState", 
/* 212 */           "runtime.powerState", 
/* 213 */           "runtime.inMaintenanceMode", 
/* 214 */           "disabledMethod", 
/* 215 */           "vsanSemiAutoClaimDisksData", 
/* 216 */           "configManager.vsanSystem" }).getPropertyValues();
/* 217 */     VirtualNicManagerInfo nicManagerInfo = null;
/* 218 */     VirtualNic[] vnic = null;
/* 219 */     ManagedObjectReference parent = null;
/* 220 */     boolean isConnected = false;
/* 221 */     boolean isPoweredOn = false;
/* 222 */     boolean isInMaintenanceMode = false;
/* 223 */     boolean canClaimHybridGroup = false;
/* 224 */     boolean hasDiskGroups = false;
/* 225 */     boolean isVsanEnabled = false;
/* 226 */     long claimedCapacity = 0L; byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/* 228 */     for (i = (arrayOfPropertyValue1 = propValue).length, b = 0; b < i; ) { List<String> disabledMethods; VsanSemiAutoClaimDisksData eligibleDisks; boolean canClaimCacheDisk, canClaimDataDisk, allFlashDiskGroupExist, hybridDiskGroupExist; Exception exception1, exception2; PropertyValue prop = arrayOfPropertyValue1[b]; String str;
/* 229 */       switch ((str = prop.propertyName).hashCode()) { case -1890534126: if (!str.equals("config.virtualNicManagerInfo")) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 234 */           nicManagerInfo = (VirtualNicManagerInfo)prop.value; break;
/*     */         case -995424086:
/*     */           if (!str.equals("parent"))
/* 237 */             break;  parent = (ManagedObjectReference)prop.value;
/*     */           break;
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
/*     */         case -533996354:
/*     */           if (!str.equals("config.network.vnic")) {
/*     */             break;
/*     */           }
/*     */           vnic = (VirtualNic[])prop.value;
/*     */           break;
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
/*     */         case -453313988:
/*     */           if (!str.equals("configManager.vsanSystem")) {
/*     */             break;
/*     */           }
/* 271 */           exception1 = null; exception2 = null;
/*     */         case 541171298: if (!str.equals("runtime.powerState"))
/*     */             break;  isPoweredOn = prop.value.equals(HostSystem.PowerState.poweredOn); break;
/*     */         case 767699581: if (!str.equals("disabledMethod"))
/*     */             break;  disabledMethods = Arrays.asList((String[])prop.value); isInMaintenanceMode |= disabledMethods.containsAll(Arrays.asList((Object[])new String[] { "EnterMaintenanceMode_Task", "ExitMaintenanceMode_Task" })); break;
/*     */         case 898574043: if (!str.equals("runtime.inMaintenanceMode"))
/*     */             break;  isInMaintenanceMode |= ((Boolean)prop.value).booleanValue(); break;
/*     */         case 1457701451: if (!str.equals("vsanSemiAutoClaimDisksData"))
/*     */             break;  eligibleDisks = (VsanSemiAutoClaimDisksData)prop.value; canClaimCacheDisk = false; canClaimDataDisk = false; allFlashDiskGroupExist = false; hybridDiskGroupExist = false; if (eligibleDisks != null) { canClaimCacheDisk = (eligibleDisks.numNotInUseSsdDisks > 0); canClaimDataDisk = (eligibleDisks.numNotInUseDataDisks > 0); allFlashDiskGroupExist = eligibleDisks.allFlashDiskGroupExist; hybridDiskGroupExist = eligibleDisks.hybridDiskGroupExist; claimedCapacity = eligibleDisks.claimedCapacity; }  canClaimHybridGroup = (canClaimCacheDisk && canClaimDataDisk); hasDiskGroups = !(!allFlashDiskGroupExist && !hybridDiskGroupExist); break;
/*     */         case 2004020797:
/*     */           if (!str.equals("runtime.connectionState"))
/* 282 */             break;  isConnected = prop.value.equals(HostSystem.ConnectionState.connected); break; }  b++; }  WitnessHostValidationResult result = new WitnessHostValidationResult();
/* 283 */     result.witnessHostRef = hostRef;
/* 284 */     result.isHostInTheSameCluster = parent.equals(clusterRef);
/*     */     
/* 286 */     if (!result.isHostInTheSameCluster && 
/* 287 */       parent.getType().equals(ClusterComputeResource.class.getSimpleName())) {
/* 288 */       result.isHostInVsanEnabledCluster = (
/* 289 */         (Boolean)QueryUtil.getProperty(parent, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled", null)).booleanValue();
/*     */     }
/*     */     
/* 292 */     if (!result.isHostInTheSameCluster && 
/* 293 */       !result.isHostInVsanEnabledCluster && 
/* 294 */       vnic != null) {
/* 295 */       VirtualNic[] arrayOfVirtualNic; for (int j = (arrayOfVirtualNic = vnic).length; i < j; ) { VirtualNic virtualNic = arrayOfVirtualNic[i];
/* 296 */         if (isVsanEnabled(virtualNic.device, nicManagerInfo)) {
/* 297 */           result.hasVsanEnabledNic = true;
/*     */           break;
/*     */         } 
/*     */         i++; }
/*     */     
/*     */     } 
/* 303 */     boolean isEncrypted = false;
/*     */     
/*     */     try {
/* 306 */       Exception exception = null;
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
/* 317 */     catch (Exception ex) {
/* 318 */       _logger.error("Cannot retrieve host's configuration from VsanSystem living in vSAN Health service.", ex);
/*     */     } 
/*     */     
/* 321 */     result.isHostConnected = isConnected;
/* 322 */     result.isPoweredOn = isPoweredOn;
/* 323 */     result.isHostInMaintenanceMode = isInMaintenanceMode;
/* 324 */     result.isStretchedClusterSupported = VsanCapabilityUtils.isStretchedClusterSupportedOnHost(hostRef);
/* 325 */     result.canClaimHybridGroup = canClaimHybridGroup;
/* 326 */     result.hasDiskGroups = hasDiskGroups;
/* 327 */     result.claimedCapacity = claimedCapacity;
/* 328 */     result.isExternalWitness = (isVsanEnabled && !result.isHostInVsanEnabledCluster);
/* 329 */     result.isEncrypted = isEncrypted;
/* 330 */     result.autoClaimMode = ((Boolean)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.autoClaimStorage", null)).booleanValue();
/*     */     
/* 332 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isVsanEnabled(String vnicDevice, VirtualNicManagerInfo nicInfo) {
/* 337 */     if (vnicDevice == null || nicInfo == null || nicInfo.netConfig == null)
/* 338 */       return false;  byte b;
/*     */     int i;
/*     */     VirtualNicManager.NetConfig[] arrayOfNetConfig;
/* 341 */     for (i = (arrayOfNetConfig = nicInfo.netConfig).length, b = 0; b < i; ) { VirtualNicManager.NetConfig netConfig = arrayOfNetConfig[b];
/* 342 */       if ((VirtualNicManager.NicType.vsan.toString().equals(netConfig.nicType) || 
/* 343 */         VirtualNicManager.NicType.vsanWitness.toString().equals(netConfig.nicType)) && 
/* 344 */         netConfig.selectedVnic != null) {
/* 345 */         byte b1; int j; String[] arrayOfString; for (j = (arrayOfString = netConfig.selectedVnic).length, b1 = 0; b1 < j; ) { String selectedVnic = arrayOfString[b1];
/* 346 */           if (vnicDevice.equals(getVnicDeviceName(selectedVnic)))
/* 347 */             return true; 
/*     */           b1++; }
/*     */       
/*     */       } 
/*     */       b++; }
/*     */     
/* 353 */     return false;
/*     */   }
/*     */   
/*     */   private String getVnicDeviceName(String deviceKey) {
/* 357 */     if (deviceKey == null) {
/* 358 */       return deviceKey;
/*     */     }
/*     */     
/* 361 */     int vnicPrefix = deviceKey.indexOf("vim.host.VirtualNic-");
/* 362 */     if (vnicPrefix < 0) {
/* 363 */       return deviceKey;
/*     */     }
/*     */     
/* 366 */     return deviceKey.substring(vnicPrefix + "vim.host.VirtualNic-".length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DiskMapping copyProperties(DiskMapping original) {
/* 373 */     DiskMapping result = new DiskMapping();
/* 374 */     result.ssd = original.ssd;
/* 375 */     result.nonSsd = new com.vmware.vim.binding.vim.host.ScsiDisk[original.nonSsd.length];
/* 376 */     for (int i = 0; i < result.nonSsd.length; i++) {
/* 377 */       result.nonSsd[i] = original.nonSsd[i];
/*     */     }
/*     */     
/* 380 */     return result;
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
/*     */   public void updateDiskClaimingMode(ManagedObjectReference clusterRef, DiskClaimingModeSpec spec) throws Exception {
/* 394 */     Exception exception1 = null, exception2 = null;
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
/*     */   public void removeHost(ManagedObjectReference clusterRef, LeaveVsanClusterSpec spec) throws Exception {
/* 418 */     Exception exception1 = null, exception2 = null;
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
/*     */   private List<VsanSystem> getVsanSystems(ManagedObjectReference clusterRef, VcConnection conn) {
/* 431 */     List<VsanSystem> vsanSystems = new ArrayList<>();
/*     */     
/*     */     try {
/* 434 */       PropertyValue[] propValues = QueryUtil.getPropertiesForRelatedObjects(clusterRef, 
/* 435 */           "witnessHost", 
/* 436 */           ClusterComputeResource.class.getSimpleName(), 
/* 437 */           new String[] { "configManager.vsanSystem" }).getPropertyValues(); byte b; int i;
/*     */       PropertyValue[] arrayOfPropertyValue1;
/* 439 */       for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 440 */         ManagedObjectReference vsanSystemRef = (ManagedObjectReference)propValue.value;
/* 441 */         VsanSystem vsanSystem = VsanProviderUtils.getHostVsanSystem(vsanSystemRef, conn);
/* 442 */         vsanSystems.add(vsanSystem); b++; }
/*     */     
/* 444 */     } catch (Exception ex) {
/* 445 */       _logger.error("Could not retrieve witness hosts and their vsan system: ", ex);
/*     */     } 
/* 447 */     return vsanSystems;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/VsanStretchedClusterMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */