/*     */ package com.vmware.vsphere.client.vsan.dataprovider;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.host.VsanSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ClusterStatus;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ConfigInfo;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskMapInfo;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vim.vsan.host.VsanRuntimeInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcDiskManagementSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.multithreading.VsanAsyncQueryUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.ClaimOption;
/*     */ import com.vmware.vsphere.client.vsan.data.DiskLocalityType;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskAndGroupData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskGroupData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanHostData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanSemiAutoClaimDisksData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanVirtualPhysicalMappingData;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsan.util.VsanAllFlashClaimOptionRecommender;
/*     */ import com.vmware.vsphere.client.vsan.util.VsanHybridClaimOptionRecommender;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanHostPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*     */   public static final String VSAN_SEMI_AUTO_DISKS_PROPERTY_NAME = "vsanSemiAutoClaimDisksData";
/*     */   public static final String VSAN_VIRTUAL_PHYSICAL_MAPPING_DATA_PROPERTY = "vsanVirtualPhysicalMappingData";
/*     */   public static final String FAULT_DOMAIN_PROPERTY = "config.vsanHostConfig.faultDomainInfo.name";
/*     */   public static final String VSAN_HOST_CLUSTER_STATUS_PROPERTY = "vsanHostClusterStatus";
/*     */   public static final String VSAN_HOST_DISKS_FOR_VSAN_PROPERTY = "vsanHostDisksForVsan";
/*     */   public static final String VSAN_PHYSICAL_DISK_HEALTH_AND_VERSION_PROPERTY = "vsanPhysicalDiskHealthAndVersion";
/*     */   public static final String HOST_VSAN_STORAGE_INFO_PROPERTY = "config.vsanHostConfig.storageInfo";
/*     */   public static final String HOST_VSAN_RUNTIME_INFO = "runtime.vsanRuntimeInfo";
/*  70 */   public static final String[] PHYSICAL_DISK_HEALTH_AND_VERSION_PROPERTIES = new String[] { "disk_health", "formatVersion", "publicFormatVersion", "self_only" }; public static final String STORAGE_ADAPTER_DEVICES = "storageAdapterDevices"; public static final String HOST_OPERATIONAL_PROPERTY_NAME = "isHostOperational"; public static final String CONNECTION_STATE_PROPERTY = "runtime.connectionState"; public static final String POWER_STATE_PROPERTY = "runtime.powerState"; public static final String MAINTENANCE_MODE_PROPERTY = "runtime.inMaintenanceMode"; public static final String DISABLED_METHODS_PROPERTY = "disabledMethod"; public static final String ENTER_MAINTENANCE_MODE_DISABLED_METHOD = "EnterMaintenanceMode_Task"; public static final String EXIT_MAINTENANCE_MODE_DISABLED_METHOD = "ExitMaintenanceMode_Task";
/*     */   public static final String IS_ALL_FLASH_SUPPORTED_PROPERTY = "isAllFlashSupported";
/*  72 */   public static final String[] PHYSICAL_DISK_VIRTUAL_MAPPING_PROPERTIES = new String[] { "lsom_objects", "lsom_objects_count", "disk_health", 
/*  73 */       "capacityReserved", "capacityUsed", "self_only" };
/*     */   
/*     */   public static final String HOST_PARENT_PROPERTY = "parent";
/*     */   
/*  77 */   private static final Log _logger = LogFactory.getLog(VsanHostPropertyProviderAdapter.class);
/*  78 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanHostPropertyProviderAdapter.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final VcClient _vcClient;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanHostPropertyProviderAdapter(DataServiceExtensionRegistry registry, VcClient vcClient) {
/*  88 */     Validate.notNull(registry);
/*  89 */     this._vcClient = vcClient;
/*     */     
/*  91 */     TypeInfo hostInfo = new TypeInfo();
/*  92 */     hostInfo.type = HostSystem.class.getSimpleName();
/*  93 */     hostInfo.properties = new String[] { 
/*  94 */         "vsanDisksAndGroupsData", 
/*  95 */         "vsanDiskMapData", 
/*  96 */         "vsanSemiAutoClaimDisksData", 
/*  97 */         "vsanVirtualPhysicalMappingData", 
/*  98 */         "vsanHostClusterStatus", 
/*  99 */         "vsanHostDisksForVsan", 
/* 100 */         "vsanPhysicalDiskHealthAndVersion", 
/* 101 */         "vsanStorageAdapterDevices", 
/* 102 */         "vsanPhysicalDiskVirtualMapping", "isHostOperational",
/*     */         
/* 104 */         "isAllFlashSupported" };
/*     */ 
/*     */     
/* 107 */     TypeInfo[] providedProperties = { hostInfo };
/* 108 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/* 113 */     if (!isValidRequest(propertyRequest)) {
/* 114 */       ResultSet result = new ResultSet();
/* 115 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/* 116 */       return result;
/*     */     } 
/*     */     
/* 119 */     Set<ManagedObjectReference> allHosts = getHosts(propertyRequest.objects);
/* 120 */     Object[] hosts = allHosts.toArray();
/*     */     
/* 122 */     boolean isAllFlashSupported = false;
/* 123 */     boolean allFlashCheckRequested = 
/* 124 */       QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] {
/* 125 */           "isAllFlashSupported", "vsanSemiAutoClaimDisksData" });
/* 126 */     if (allFlashCheckRequested) {
/* 127 */       isAllFlashSupported = isAllFlashSupported(hosts);
/*     */     }
/*     */     
/* 130 */     String[] allProperties = QueryUtil.getPropertyNames(propertyRequest.properties);
/*     */ 
/*     */     
/* 133 */     HashMap<ManagedObjectReference, HashMap<String, Object>> hostPropsFromDs = null;
/* 134 */     ResultSet resultSet = null;
/*     */     try {
/* 136 */       hostPropsFromDs = getHostPropsFromDs(hosts, allProperties);
/* 137 */       if (allFlashCheckRequested) {
/* 138 */         appendAllFlashSupportedProperty(hostPropsFromDs, isAllFlashSupported);
/*     */       }
/* 140 */     } catch (Exception ex) {
/* 141 */       _logger.error("Failed to retrieve properties from DS. ", ex);
/* 142 */       resultSet = new ResultSet();
/* 143 */       resultSet.error = ex;
/* 144 */       return resultSet;
/*     */     } 
/*     */     
/* 147 */     List<Callable<VsanAsyncQueryUtils.RequestResult>> requestTasks = 
/* 148 */       getRequestTasks(allHosts, allProperties, hostPropsFromDs);
/*     */     
/* 150 */     resultSet = VsanAsyncQueryUtils.getProperties(requestTasks);
/*     */     
/* 152 */     return resultSet;
/*     */   }
/*     */   
/*     */   private boolean isAllFlashSupported(Object[] hosts) {
/* 156 */     if (ArrayUtils.isEmpty(hosts)) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     ManagedObjectReference host = (ManagedObjectReference)hosts[0];
/* 161 */     ManagedObjectReference parent = getHostParent(host);
/* 162 */     if (parent != null && parent.getType().equals(ClusterComputeResource.class.getSimpleName())) {
/* 163 */       return VsanCapabilityUtils.isAllFlashSupportedOnCluster(parent);
/*     */     }
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   private ManagedObjectReference getHostParent(ManagedObjectReference host) {
/* 169 */     ManagedObjectReference parent = null;
/*     */     
/*     */     try {
/* 172 */       parent = (ManagedObjectReference)QueryUtil.getProperty(host, "parent", null);
/* 173 */     } catch (Exception ex) {
/* 174 */       _logger.warn("Cannot get host's parent", ex);
/*     */     } 
/*     */     
/* 177 */     return parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendAllFlashSupportedProperty(Map<ManagedObjectReference, HashMap<String, Object>> hostPropsFromDs, boolean isAllFlashSupported) {
/* 183 */     if (hostPropsFromDs == null || hostPropsFromDs.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 187 */     for (HashMap<String, Object> hostProperties : hostPropsFromDs.values()) {
/* 188 */       hostProperties.put("isAllFlashSupported", Boolean.valueOf(isAllFlashSupported));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HashMap<ManagedObjectReference, HashMap<String, Object>> getHostPropsFromDs(Object[] allHosts, String[] propertyNames) throws Exception {
/* 195 */     Set<String> propsToRetrieve = new HashSet<>(); byte b; int i; String[] arrayOfString;
/* 196 */     for (i = (arrayOfString = propertyNames).length, b = 0; b < i; ) { String propertyName = arrayOfString[b];
/* 197 */       if (propertyName.equals("vsanDisksAndGroupsData")) {
/* 198 */         propsToRetrieve.add("runtime.vsanRuntimeInfo");
/* 199 */         propsToRetrieve.add("vsanDiskMapData");
/* 200 */       } else if (propertyName.equals("vsanSemiAutoClaimDisksData")) {
/* 201 */         propsToRetrieve.add("config.vsanHostConfig.storageInfo");
/* 202 */       } else if (propertyName.equals("vsanVirtualPhysicalMappingData")) {
/* 203 */         propsToRetrieve.add("config.vsanHostConfig.clusterInfo.nodeUuid");
/* 204 */         propsToRetrieve.add("name");
/* 205 */         propsToRetrieve.add("config.vsanHostConfig.faultDomainInfo.name");
/* 206 */       } else if (propertyName.equals("isHostOperational")) {
/* 207 */         propsToRetrieve.add("runtime.connectionState");
/* 208 */         propsToRetrieve.add("runtime.powerState");
/* 209 */         propsToRetrieve.add("runtime.inMaintenanceMode");
/* 210 */         propsToRetrieve.add("disabledMethod");
/*     */       } 
/*     */       b++; }
/*     */     
/* 214 */     return getMultipleProperties(allHosts, 
/* 215 */         propsToRetrieve.<String>toArray(new String[propsToRetrieve.size()]));
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
/*     */   private List<Callable<VsanAsyncQueryUtils.RequestResult>> getRequestTasks(Set<ManagedObjectReference> hosts, String[] properties, HashMap<ManagedObjectReference, HashMap<String, Object>> hostProperties) {
/* 229 */     List<Callable<VsanAsyncQueryUtils.RequestResult>> result = new ArrayList<>();
/* 230 */     for (ManagedObjectReference hostRef : hosts) {
/* 231 */       byte b; int i; String[] arrayOfString; for (i = (arrayOfString = properties).length, b = 0; b < i; ) { String property = arrayOfString[b];
/* 232 */         Callable<VsanAsyncQueryUtils.RequestResult> requestTask = new Callable<VsanAsyncQueryUtils.RequestResult>(hostRef, property, hostProperties)
/*     */           {
/*     */             private final ManagedObjectReference _target;
/*     */             private final String _property;
/*     */             private final HashMap<String, Object> _hostProp;
/*     */             
/*     */             public VsanAsyncQueryUtils.RequestResult call() {
/* 239 */               return VsanHostPropertyProviderAdapter.this.executeRequest(this._target, this._property, this._hostProp);
/*     */             }
/*     */           };
/*     */         
/* 243 */         result.add(requestTask); b++; }
/*     */     
/*     */     } 
/* 246 */     return result;
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
/*     */   private VsanAsyncQueryUtils.RequestResult executeRequest(ManagedObjectReference target, String property, HashMap<String, Object> hostProperties) {
/* 263 */     Exception error = null;
/* 264 */     Object result = null;
/*     */     
/*     */     try {
/* 267 */       if (property.equals("vsanDisksAndGroupsData")) {
/* 268 */         result = getHostDisksAndGroupsData(target, hostProperties);
/* 269 */       } else if (property.equals("vsanDiskMapData")) {
/* 270 */         result = getDiskMappingData(target);
/* 271 */       } else if (property.equals("vsanSemiAutoClaimDisksData")) {
/* 272 */         result = getSemiAutoClaimDisksData(target, hostProperties);
/* 273 */       } else if (property.equals("vsanVirtualPhysicalMappingData")) {
/* 274 */         result = getVsanVirtualPhysicalMappingData(target, hostProperties);
/* 275 */       } else if (property.equals("vsanHostClusterStatus")) {
/* 276 */         result = getVsanHostClusterStatus(target);
/* 277 */       } else if (property.equals("vsanHostDisksForVsan")) {
/* 278 */         result = getVsanHostDiskForVsanProperty(target);
/* 279 */       } else if (property.equals("vsanPhysicalDiskHealthAndVersion")) {
/* 280 */         result = getVsanPhyscialDiskHealthAndVersionProperty(target, hostProperties);
/* 281 */       } else if (property.equals("vsanPhysicalDiskVirtualMapping")) {
/* 282 */         result = getVsanPhysicalDiskVirtualMapping(target);
/* 283 */       } else if (property.equals("vsanStorageAdapterDevices")) {
/* 284 */         result = getVsanHostStorageAdapterDevices(target, hostProperties);
/* 285 */       } else if (property.equals("isHostOperational")) {
/* 286 */         result = Boolean.valueOf(isHostOperational(target, hostProperties));
/* 287 */       } else if (property.equals("isAllFlashSupported")) {
/* 288 */         result = hostProperties.get("isAllFlashSupported");
/*     */       } 
/* 290 */     } catch (Exception exception) {
/* 291 */       error = exception;
/*     */     } 
/*     */     
/* 294 */     if (error != null) {
/* 295 */       _logger.error("Request for DiskMapping for " + target.toString() + 
/* 296 */           " failed.", error);
/*     */     }
/*     */     
/* 299 */     return new VsanAsyncQueryUtils.RequestResult(result, error, target, property);
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
/*     */   public VsanVirtualPhysicalMappingData getVsanVirtualPhysicalMappingData(ManagedObjectReference host, HashMap<String, Object> hostProps) throws Exception {
/* 312 */     if (hostProps == null) {
/* 313 */       return null;
/*     */     }
/*     */     
/* 316 */     VsanVirtualPhysicalMappingData mappingData = new VsanVirtualPhysicalMappingData();
/* 317 */     ArrayList<VsanHostData> hostsData = new ArrayList<>();
/* 318 */     ArrayList<DiskResult> disksData = new ArrayList<>(); try {
/* 319 */       Exception exception2, exception1 = null;
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
/*     */     }
/* 339 */     catch (Exception exp) {
/* 340 */       _logger.error(exp.getMessage());
/* 341 */       throw exp;
/*     */     } 
/*     */     
/* 344 */     mappingData.hosts = hostsData.<VsanHostData>toArray(new VsanHostData[hostsData.size()]);
/* 345 */     mappingData.disks = disksData.<DiskResult>toArray(new DiskResult[disksData.size()]);
/* 346 */     return mappingData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClusterStatus getVsanHostClusterStatus(ManagedObjectReference host) throws Exception {
/* 354 */     Exception exception1 = null, exception2 = null;
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
/*     */   private DiskResult[] getVsanHostDiskForVsanProperty(ManagedObjectReference host) throws Exception {
/* 375 */     Exception exception1 = null, exception2 = null; try {
/*     */     
/*     */     } finally {
/* 378 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/*     */   private String getVsanPhyscialDiskHealthAndVersionProperty(ManagedObjectReference host, HashMap<String, Object> hostProps) throws Exception {
/* 390 */     if (hostProps == null) {
/* 391 */       return null;
/*     */     }
/*     */     
/* 394 */     String result = "";
/* 395 */     Exception exception1 = null, exception2 = null;
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
/*     */   
/*     */   private String getVsanPhysicalDiskVirtualMapping(ManagedObjectReference host) throws Exception {
/* 419 */     String result = "";
/* 420 */     Exception exception1 = null, exception2 = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] getVsanHostStorageAdapterDevices(ManagedObjectReference host, HashMap<String, Object> hostProps) throws Exception {
/* 447 */     return (Object[])QueryUtil.getProperty(host, "storageAdapterDevices", null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanSemiAutoClaimDisksData getSemiAutoClaimDisksData(ManagedObjectReference hostRef, HashMap<String, Object> hostProps) throws Exception {
/*     */     VsanSemiAutoClaimDisksData data;
/* 457 */     if (hostProps == null) {
/* 458 */       return null;
/*     */     }
/*     */     
/* 461 */     boolean isAllFlashAvailable = ((Boolean)hostProps.get("isAllFlashSupported")).booleanValue();
/* 462 */     DiskResult[] vsanDisks = null;
/* 463 */     Exception exception1 = null, exception2 = null; try { VcConnection connection = this._vcClient.getConnection(hostRef.getServerGuid()); 
/* 464 */       try { VsanSystem vsanSystem = VsanProviderUtils.getHostVsanSystem(hostRef, connection);
/* 465 */         vsanDisks = getHostDisksForVsan(vsanSystem); }
/* 466 */       finally { if (connection != null) connection.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 473 */     ArrayList<VsanDiskData> eligibleDisks = new ArrayList<>(); byte b; int i; DiskResult[] arrayOfDiskResult1;
/* 474 */     for (i = (arrayOfDiskResult1 = vsanDisks).length, b = 0; b < i; ) { DiskResult diskResult = arrayOfDiskResult1[b];
/*     */       
/* 476 */       if (Utils.isDiskEligible(diskResult)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 481 */         if (diskResult.disk.ssd.booleanValue()) {
/* 482 */           data.numNotInUseSsdDisks++;
/*     */         } else {
/* 484 */           data.numNotInUseDataDisks++;
/*     */         } 
/*     */         
/* 487 */         VsanDiskData diskData = new VsanDiskData();
/* 488 */         diskData.disk = diskResult.disk;
/* 489 */         diskData.inUse = false;
/*     */         
/* 491 */         diskData.markedAsCapacityFlash = false;
/*     */         
/* 493 */         diskData.possibleClaimOptions = getPossibleClaimingOptions(
/* 494 */             isAllFlashAvailable, diskResult.disk.ssd.booleanValue());
/* 495 */         diskData.possibleClaimOptionsIfMarkedAsOppositeType = 
/* 496 */           getPossibleClaimingOptions(isAllFlashAvailable, !diskResult.disk.ssd.booleanValue());
/*     */         
/* 498 */         eligibleDisks.add(diskData);
/*     */       }  b++; }
/*     */     
/* 501 */     populateExistingDiskGroupsInfo(data, hostProps);
/* 502 */     data.notInUseDisks = eligibleDisks.<VsanDiskData>toArray(new VsanDiskData[eligibleDisks.size()]);
/* 503 */     data.isAllFlashAvailable = isAllFlashAvailable;
/*     */     
/* 505 */     VsanAllFlashClaimOptionRecommender allFlashRecommender = 
/* 506 */       new VsanAllFlashClaimOptionRecommender(data, null);
/* 507 */     VsanHybridClaimOptionRecommender hybridRecommender = 
/* 508 */       new VsanHybridClaimOptionRecommender(data);
/* 509 */     allFlashRecommender.recommend();
/* 510 */     hybridRecommender.recommend();
/*     */     
/* 512 */     return data;
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
/*     */   private void populateExistingDiskGroupsInfo(VsanSemiAutoClaimDisksData data, HashMap<String, Object> hostProps) {
/* 525 */     ConfigInfo.StorageInfo storageInfo = 
/* 526 */       (ConfigInfo.StorageInfo)hostProps.get("config.vsanHostConfig.storageInfo");
/*     */     
/* 528 */     if (storageInfo == null) {
/*     */       return;
/*     */     }
/* 531 */     DiskMapping[] diskMapping = storageInfo.diskMapping;
/* 532 */     if (ArrayUtils.isEmpty((Object[])diskMapping)) {
/*     */       return;
/*     */     }
/*     */     
/* 536 */     long totalCapacity = 0L;
/* 537 */     long totalCache = 0L; byte b; int i; DiskMapping[] arrayOfDiskMapping1;
/* 538 */     for (i = (arrayOfDiskMapping1 = diskMapping).length, b = 0; b < i; ) { DiskMapping mapping = arrayOfDiskMapping1[b];
/* 539 */       totalCache += BaseUtils.lbaToBytes(mapping.ssd.capacity);
/* 540 */       ScsiDisk[] storageDisks = mapping.nonSsd;
/* 541 */       if (!ArrayUtils.isEmpty((Object[])storageDisks)) {
/*     */ 
/*     */ 
/*     */         
/* 545 */         ScsiDisk firstDisk = storageDisks[0];
/* 546 */         if (firstDisk.ssd.booleanValue()) {
/* 547 */           data.allFlashDiskGroupExist = true;
/* 548 */           data.numAllFlashGroups++;
/* 549 */           data.numAllFlashCapacityDisks += storageDisks.length;
/*     */         } else {
/* 551 */           data.hybridDiskGroupExist = true;
/* 552 */           data.numHybridGroups++;
/* 553 */           data.numHybridCapacityDisks += storageDisks.length;
/*     */         } 
/* 555 */         totalCapacity += BaseUtils.lbaToBytes(firstDisk.capacity);
/* 556 */         for (int j = 1; j < storageDisks.length; j++)
/* 557 */           totalCapacity += BaseUtils.lbaToBytes((storageDisks[j]).capacity); 
/*     */       }  b++; }
/*     */     
/* 560 */     data.claimedCapacity = totalCapacity;
/* 561 */     data.claimedCache = totalCache;
/*     */   }
/*     */ 
/*     */   
/*     */   private ClaimOption[] getPossibleClaimingOptions(boolean isAllFlashAvailable, boolean isFlashDisk) {
/* 566 */     List<ClaimOption> claimOptions = new ArrayList<>();
/*     */     
/* 568 */     if (isFlashDisk) {
/* 569 */       claimOptions.add(ClaimOption.ClaimForCache);
/*     */     }
/*     */ 
/*     */     
/* 573 */     if (!isFlashDisk || isAllFlashAvailable) {
/* 574 */       claimOptions.add(ClaimOption.ClaimForStorage);
/*     */     }
/*     */     
/* 577 */     claimOptions.add(ClaimOption.DoNotClaim);
/*     */     
/* 579 */     return claimOptions.<ClaimOption>toArray(new ClaimOption[claimOptions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanDiskAndGroupData getHostDisksAndGroupsData(ManagedObjectReference hostRef, HashMap<String, Object> hostProps) throws Exception {
/*     */     VsanDiskAndGroupData disksGroupsData;
/* 590 */     if (hostProps == null) {
/* 591 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 595 */     VsanRuntimeInfo vsanRuntimeInfo = 
/* 596 */       (VsanRuntimeInfo)hostProps.get("runtime.vsanRuntimeInfo");
/* 597 */     HashMap<String, ArrayList<String>> disksIssues = 
/* 598 */       getDisksIssuesOnHost(vsanRuntimeInfo);
/*     */ 
/*     */     
/* 601 */     ArrayList<VsanDiskData> vsanDisksData = new ArrayList<>();
/*     */     
/* 603 */     ArrayList<VsanDiskData> disksNotInUseData = new ArrayList<>();
/*     */     
/* 605 */     ArrayList<VsanDiskData> ineligibleDisksData = new ArrayList<>();
/*     */     
/* 607 */     HashMap<String, VsanDiskData> connectedDisksData = 
/* 608 */       new HashMap<>();
/*     */ 
/*     */     
/* 611 */     HashMap<String, Boolean> isDiskInDiskGroupMap = new HashMap<>();
/*     */     
/* 613 */     Exception exception1 = null, exception2 = null; try { VcConnection conn = this._vcClient.getConnection(hostRef.getServerGuid()); 
/* 614 */       try { VsanSystem vsanSystem = VsanProviderUtils.getHostVsanSystem(hostRef, conn);
/* 615 */         populateDisksData(vsanSystem, connectedDisksData, disksIssues); }
/* 616 */       finally { if (conn != null) conn.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 626 */     disksGroupsData.connectedDisks = (VsanDiskData[])connectedDisksData.values().toArray(
/* 627 */         (Object[])new VsanDiskData[connectedDisksData.size()]);
/*     */ 
/*     */     
/* 630 */     DiskMapInfoEx[] diskMappingData = (DiskMapInfoEx[])hostProps.get("vsanDiskMapData");
/* 631 */     disksGroupsData.vsanGroups = retrieveDiskGroups(connectedDisksData, isDiskInDiskGroupMap, diskMappingData);
/*     */ 
/*     */     
/* 634 */     for (VsanDiskData diskData : connectedDisksData.values()) {
/* 635 */       if (diskData == null || diskData.disk == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 639 */       String diskId = diskData.disk.uuid;
/*     */ 
/*     */ 
/*     */       
/* 643 */       if (!isDiskInDiskGroupMap.containsKey(diskId)) {
/* 644 */         if (diskData.ineligible) {
/* 645 */           ineligibleDisksData.add(diskData); continue;
/*     */         } 
/* 647 */         if (diskData.inUse) {
/*     */           
/* 649 */           vsanDisksData.add(diskData);
/*     */           continue;
/*     */         } 
/* 652 */         disksNotInUseData.add(diskData);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 658 */     if (ineligibleDisksData.size() > 0)
/*     */     {
/* 660 */       disksGroupsData.ineligibleDisks = ineligibleDisksData.<VsanDiskData>toArray(
/* 661 */           new VsanDiskData[ineligibleDisksData.size()]);
/*     */     }
/*     */     
/* 664 */     if (disksNotInUseData.size() > 0)
/*     */     {
/* 666 */       disksGroupsData.disksNotInUse = disksNotInUseData.<VsanDiskData>toArray(
/* 667 */           new VsanDiskData[disksNotInUseData.size()]);
/*     */     }
/*     */     
/* 670 */     if (vsanDisksData.size() > 0)
/*     */     {
/*     */       
/* 673 */       disksGroupsData.vsanDisks = vsanDisksData.<VsanDiskData>toArray(
/* 674 */           new VsanDiskData[vsanDisksData.size()]);
/*     */     }
/*     */     
/* 677 */     return disksGroupsData;
/*     */   }
/*     */   
/*     */   private DiskMapInfoEx[] getDiskMappingData(ManagedObjectReference hostRef) {
/* 681 */     DiskMapInfoEx[] result = new DiskMapInfoEx[0];
/* 682 */     VsanVcDiskManagementSystem diskMgmtSystem = VsanProviderUtils.getVcDiskManagementSystem(hostRef); try {
/* 683 */       Exception exception2, exception1 = null;
/*     */     }
/* 685 */     catch (Exception ex) {
/* 686 */       _logger.warn("Cannot query host's disk mappings from VsanVcDiskManagementSystem", 
/* 687 */           ex);
/* 688 */       _logger.info("Trying to get disk mappings from host's storageInfo property");
/*     */       
/* 690 */       result = getDiskMapingFallback(hostRef);
/*     */     } 
/*     */     
/* 693 */     return result;
/*     */   }
/*     */   
/*     */   public static DiskMapInfoEx[] getDiskMapingFallback(ManagedObjectReference hostRef) {
/* 697 */     ConfigInfo.StorageInfo storageInfo = null;
/*     */     try {
/* 699 */       storageInfo = (ConfigInfo.StorageInfo)QueryUtil.getProperty(hostRef, "config.vsanHostConfig.storageInfo", null);
/* 700 */     } catch (Exception e) {
/* 701 */       _logger.error("Cannot get host's disk mappings", e);
/*     */     } 
/*     */     
/* 704 */     List<DiskMapInfoEx> resultList = new ArrayList<>();
/* 705 */     if (storageInfo != null && storageInfo.diskMapping != null) {
/* 706 */       byte b; int i; DiskMapping[] arrayOfDiskMapping; for (i = (arrayOfDiskMapping = storageInfo.diskMapping).length, b = 0; b < i; ) { DiskMapping mapping = arrayOfDiskMapping[b];
/* 707 */         DiskMapInfoEx info = new DiskMapInfoEx();
/* 708 */         info.mapping = mapping;
/* 709 */         info.isMounted = isDiskGroupMounted(mapping, storageInfo);
/*     */ 
/*     */ 
/*     */         
/* 713 */         info.isAllFlash = false;
/* 714 */         info.isDataEfficiency = Boolean.valueOf(false);
/* 715 */         info.encryptionInfo = null;
/* 716 */         info.unlockedEncrypted = Boolean.valueOf(false);
/*     */         
/* 718 */         resultList.add(info);
/*     */         b++; }
/*     */     
/*     */     } 
/* 722 */     return resultList.<DiskMapInfoEx>toArray(new DiskMapInfoEx[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HashMap<String, ArrayList<String>> getDisksIssuesOnHost(VsanRuntimeInfo runtimeInfo) {
/* 731 */     if (runtimeInfo == null || ArrayUtils.isEmpty((Object[])runtimeInfo.diskIssues)) {
/* 732 */       return null;
/*     */     }
/*     */     
/* 735 */     HashMap<String, ArrayList<String>> issuesMap = 
/* 736 */       new HashMap<>(); byte b; int i; VsanRuntimeInfo.DiskIssue[] arrayOfDiskIssue;
/* 737 */     for (i = (arrayOfDiskIssue = runtimeInfo.diskIssues).length, b = 0; b < i; ) { VsanRuntimeInfo.DiskIssue diskIssue = arrayOfDiskIssue[b];
/* 738 */       ArrayList<String> issues = null;
/* 739 */       String diskId = diskIssue.diskId;
/* 740 */       if (issuesMap.containsKey(diskId)) {
/*     */         
/* 742 */         issues = issuesMap.get(diskId);
/*     */       } else {
/* 744 */         issues = new ArrayList<>();
/* 745 */         issuesMap.put(diskId, issues);
/*     */       } 
/* 747 */       issues.add(diskIssue.issue); b++; }
/*     */     
/* 749 */     return issuesMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanDiskGroupData[] retrieveDiskGroups(HashMap<String, VsanDiskData> connectedDisksData, HashMap<String, Boolean> isDiskInDiskGroup, DiskMapInfoEx[] diskMappingData) {
/* 758 */     if (connectedDisksData == null || connectedDisksData.size() == 0) {
/* 759 */       return null;
/*     */     }
/*     */     
/* 762 */     if (ArrayUtils.isEmpty((Object[])diskMappingData)) {
/* 763 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 768 */     VsanDiskGroupData[] groups = new VsanDiskGroupData[diskMappingData.length];
/* 769 */     int i = 0; byte b; int j; DiskMapInfoEx[] arrayOfDiskMapInfoEx;
/* 770 */     for (j = (arrayOfDiskMapInfoEx = diskMappingData).length, b = 0; b < j; ) { DiskMapInfoEx diskMapInfoEx = arrayOfDiskMapInfoEx[b];
/* 771 */       DiskMapping mapping = diskMapInfoEx.mapping;
/*     */       
/* 773 */       String ssdId = mapping.ssd.uuid;
/*     */       
/* 775 */       VsanDiskGroupData groupData = new VsanDiskGroupData();
/* 776 */       groupData.ssd = connectedDisksData.get(ssdId);
/* 777 */       groupData.ssd.diskGroupUuid = ssdId;
/* 778 */       groupData.ssd.isCacheDisk = true;
/* 779 */       groupData.mounted = diskMapInfoEx.isMounted;
/* 780 */       groupData.encrypted = (diskMapInfoEx.encryptionInfo != null && diskMapInfoEx.encryptionInfo.encryptionEnabled);
/* 781 */       groupData.unlocked = (diskMapInfoEx.unlockedEncrypted != null && diskMapInfoEx.unlockedEncrypted.booleanValue());
/* 782 */       groupData.isAllFlash = diskMapInfoEx.isAllFlash;
/* 783 */       isDiskInDiskGroup.put(ssdId, Boolean.valueOf(true));
/*     */ 
/*     */ 
/*     */       
/* 787 */       ArrayList<VsanDiskData> disks = new ArrayList<>(); byte b1; int k; ScsiDisk[] arrayOfScsiDisk;
/* 788 */       for (k = (arrayOfScsiDisk = mapping.nonSsd).length, b1 = 0; b1 < k; ) { ScsiDisk disk = arrayOfScsiDisk[b1];
/* 789 */         String diskId = disk.uuid;
/* 790 */         VsanDiskData diskData = connectedDisksData.get(diskId);
/* 791 */         diskData.diskGroupUuid = ssdId;
/* 792 */         diskData.isCacheDisk = false;
/* 793 */         if (diskData != null) {
/* 794 */           disks.add(diskData);
/*     */         }
/* 796 */         isDiskInDiskGroup.put(diskId, Boolean.valueOf(true));
/*     */         
/*     */         b1++; }
/*     */       
/* 800 */       groupData.disks = disks.<VsanDiskData>toArray(new VsanDiskData[disks.size()]);
/*     */ 
/*     */       
/* 803 */       groups[i++] = groupData; b++; }
/*     */     
/* 805 */     return groups;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDiskGroupMounted(DiskMapping mapping, ConfigInfo.StorageInfo storageInfo) {
/* 814 */     if (mapping == null || mapping.ssd == null || 
/* 815 */       storageInfo.diskMapInfo == null)
/*     */     {
/* 817 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 821 */     String ssdId = mapping.ssd.uuid;
/* 822 */     if (ssdId == null) {
/* 823 */       return true;
/*     */     }
/*     */     byte b;
/*     */     int i;
/*     */     DiskMapInfo[] arrayOfDiskMapInfo;
/* 828 */     for (i = (arrayOfDiskMapInfo = storageInfo.diskMapInfo).length, b = 0; b < i; ) { DiskMapInfo diskMapInfo = arrayOfDiskMapInfo[b];
/* 829 */       if (diskMapInfo.mapping != null && diskMapInfo.mapping.ssd != null)
/*     */       {
/*     */ 
/*     */         
/* 833 */         if (ssdId.equals(diskMapInfo.mapping.ssd.uuid)) {
/* 834 */           return diskMapInfo.mounted;
/*     */         }
/*     */       }
/*     */       b++; }
/*     */     
/* 839 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateDisksData(VsanSystem vsanSystem, HashMap<String, VsanDiskData> connectedDisks, HashMap<String, ArrayList<String>> diskIssues) {
/* 849 */     DiskResult[] results = getHostDisksForVsan(vsanSystem);
/* 850 */     if (results == null)
/*     */       return;  byte b;
/*     */     int i;
/*     */     DiskResult[] arrayOfDiskResult1;
/* 854 */     for (i = (arrayOfDiskResult1 = results).length, b = 0; b < i; ) { DiskResult result = arrayOfDiskResult1[b];
/* 855 */       String resultState = result.state;
/* 856 */       DiskResult.State diskState = Enum.<DiskResult.State>valueOf(DiskResult.State.class, resultState);
/*     */       
/* 858 */       VsanDiskData diskData = new VsanDiskData();
/* 859 */       if (result.error != null) {
/* 860 */         diskData.stateReason = result.error.getLocalizedMessage();
/*     */       }
/*     */       
/* 863 */       diskData.disk = result.disk;
/* 864 */       diskData.vsanUuid = result.vsanUuid;
/* 865 */       if (diskState == DiskResult.State.ineligible) {
/* 866 */         diskData.inUse = false;
/* 867 */         diskData.ineligible = true;
/* 868 */       } else if (diskState == DiskResult.State.eligible) {
/* 869 */         diskData.inUse = false;
/* 870 */       } else if (diskState == DiskResult.State.inUse) {
/* 871 */         diskData.inUse = true;
/*     */       } 
/*     */       
/* 874 */       diskData.diskLocality = getDiskLocality(diskData.disk);
/*     */       
/* 876 */       String diskId = diskData.disk.uuid;
/*     */ 
/*     */       
/* 879 */       if (diskIssues != null && diskIssues.containsKey(diskId)) {
/* 880 */         ArrayList<String> issues = diskIssues.get(diskId);
/* 881 */         if (!CollectionUtils.isEmpty(issues)) {
/* 882 */           diskData.issues = issues.<String>toArray(new String[issues.size()]);
/*     */         }
/*     */       } 
/*     */       
/* 886 */       connectedDisks.put(diskId, diskData);
/*     */       b++; }
/*     */   
/*     */   }
/*     */   private DiskLocalityType getDiskLocality(ScsiDisk disk) {
/* 891 */     DiskLocalityType result = DiskLocalityType.Unknown;
/* 892 */     if (disk.localDisk != null) {
/* 893 */       result = disk.localDisk.booleanValue() ? DiskLocalityType.Local : DiskLocalityType.Remote;
/*     */     }
/* 895 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DiskResult[] getHostDisksForVsan(VsanSystem vsanSystem) {
/* 902 */     if (vsanSystem == null) {
/* 903 */       return null;
/*     */     }
/* 905 */     DiskResult[] disks = null; try {
/* 906 */       Exception exception2, exception1 = null;
/*     */     }
/* 908 */     catch (Exception ex) {
/* 909 */       _logger.error("Failed to get host disks for VSAN from " + vsanSystem._getRef().toString(), ex);
/*     */     } 
/* 911 */     return disks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<ManagedObjectReference> getHosts(Object[] hosts) {
/* 918 */     if (hosts == null || hosts.length == 0) {
/* 919 */       return Collections.emptySet();
/*     */     }
/* 921 */     Set<ManagedObjectReference> result = new HashSet<>(); byte b; int i;
/*     */     Object[] arrayOfObject;
/* 923 */     for (i = (arrayOfObject = hosts).length, b = 0; b < i; ) { Object o = arrayOfObject[b];
/* 924 */       if (o instanceof ManagedObjectReference) {
/*     */ 
/*     */         
/* 927 */         ManagedObjectReference host = (ManagedObjectReference)o;
/* 928 */         result.add(host);
/*     */       }  b++; }
/* 930 */      return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidRequest(PropertyRequestSpec propertyRequest) {
/* 937 */     if (propertyRequest == null) {
/* 938 */       return false;
/*     */     }
/* 940 */     if (ArrayUtils.isEmpty(propertyRequest.objects) || 
/* 941 */       ArrayUtils.isEmpty((Object[])propertyRequest.properties)) {
/* 942 */       _logger.error("Property provider adapter got a null or empty list of properties or objects");
/*     */       
/* 944 */       return false;
/*     */     } 
/* 946 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HashMap<ManagedObjectReference, HashMap<String, Object>> getMultipleProperties(Object[] hosts, String[] properties) throws Exception {
/* 956 */     HashMap<ManagedObjectReference, HashMap<String, Object>> result = new HashMap<>();
/* 957 */     if (properties == null || properties.length == 0 || hosts == null || 
/* 958 */       hosts.length == 0) {
/* 959 */       return result;
/*     */     }
/*     */     
/* 962 */     PropertyValue[] propValues = QueryUtil.getProperties(hosts, properties).getPropertyValues();
/*     */     
/* 964 */     if (propValues != null) {
/* 965 */       byte b; int i; PropertyValue[] arrayOfPropertyValue; for (i = (arrayOfPropertyValue = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b];
/*     */         
/* 967 */         ManagedObjectReference hostRef = (ManagedObjectReference)propValue.resourceObject;
/* 968 */         if (!result.containsKey(hostRef)) {
/* 969 */           result.put(hostRef, new HashMap<>());
/*     */         }
/* 971 */         HashMap<String, Object> propMap = result.get(hostRef);
/* 972 */         propMap.put(propValue.propertyName, propValue.value);
/*     */         b++; }
/*     */     
/*     */     } 
/* 976 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isHostOperational(ManagedObjectReference host, HashMap<String, Object> hostProps) {
/* 987 */     HostSystem.ConnectionState connectionState = (HostSystem.ConnectionState)hostProps.get("runtime.connectionState");
/* 988 */     HostSystem.PowerState powerState = (HostSystem.PowerState)hostProps.get("runtime.powerState");
/* 989 */     boolean isInMaintenanceMode = ((Boolean)hostProps.get("runtime.inMaintenanceMode")).booleanValue();
/* 990 */     List<String> disabledMethods = Arrays.asList((String[])hostProps.get("disabledMethod"));
/*     */     
/* 992 */     if (connectionState.equals(HostSystem.ConnectionState.connected) && 
/* 993 */       HostSystem.PowerState.poweredOn.equals(powerState) && 
/* 994 */       !isInMaintenanceMode)
/* 995 */       if (!disabledMethods.containsAll(
/* 996 */           Arrays.asList((Object[])new String[] { "EnterMaintenanceMode_Task", "ExitMaintenanceMode_Task" }))); 
/*     */     boolean isHostConnected = false;
/* 998 */     return isHostConnected;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/dataprovider/VsanHostPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */