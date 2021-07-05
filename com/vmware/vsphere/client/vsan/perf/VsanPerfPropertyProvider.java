/*      */ package com.vmware.vsphere.client.vsan.perf;
/*      */ import com.vmware.proxygen.ts.TsService;
/*      */ import com.vmware.vim.binding.pbm.profile.Profile;
/*      */ import com.vmware.vim.binding.vim.HostSystem;
/*      */ import com.vmware.vim.binding.vim.KeyValue;
/*      */ import com.vmware.vim.binding.vim.dvs.HostMember;
/*      */ import com.vmware.vim.binding.vim.host.HostProxySwitch;
/*      */ import com.vmware.vim.binding.vim.host.NetworkInfo;
/*      */ import com.vmware.vim.binding.vim.host.PortGroup;
/*      */ import com.vmware.vim.binding.vim.host.VirtualNic;
/*      */ import com.vmware.vim.binding.vim.vm.device.VirtualController;
/*      */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*      */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*      */ import com.vmware.vim.binding.vim.vsan.host.ConfigInfo;
/*      */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*      */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityType;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfMetricSeriesCSV;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfNodeInformation;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*      */ import com.vmware.vise.data.query.PropertyValue;
/*      */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*      */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.ActiveVmnicDataSpec;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.CapacityHistoryBasicInfo;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.DiskGroup;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.EntityPerfStateObject;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.HostDiskGroupsData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.HostPnicsData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.HostVnicsData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfEntityStateData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfGraphMetricsData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfMonitorCommonPropsData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfPhysicalAdapterEntity;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfQuerySpec;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfStatsObjectInfo;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfTimeRangeData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfVirtualDiskEntity;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfVirtualMachineDiskData;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfVnicEntity;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.PerfVscsiEntity;
/*      */ import com.vmware.vsphere.client.vsan.perf.model.ServerObjectInfo;
/*      */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*      */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*      */ import com.vmware.vsphere.client.vsan.util.Utils;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.lang.ArrayUtils;
/*      */ import org.apache.commons.lang.StringUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ 
/*      */ public class VsanPerfPropertyProvider {
/*   59 */   private static final Log _logger = LogFactory.getLog(VsanPerfMutationProvider.class);
/*      */   
/*   61 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanPerfPropertyProvider.class);
/*      */   
/*      */   private static final String PBM_RELATION = "pbmProfiles";
/*      */   
/*      */   private static final String PBM_TARGET_TYPE = "PbmRequirementStorageProfile";
/*      */   
/*      */   private static final String PBM_PROFILE_PROP = "profileContent";
/*      */   
/*      */   private static final String DATASTORE_URL_PREFIX = "ds://";
/*      */   
/*      */   private static final String DATASTORE_URL_PROP = "summary.url";
/*      */   
/*      */   private static final String NAME_PROP = "name";
/*      */   
/*      */   private static final String HOST_CONFIG_NETWORK_PROPERTY = "config.network";
/*      */   
/*      */   private static final String NETWORK_PROPERTY = "network";
/*      */   
/*      */   private static final String ACTIVE_UPLINK_PORT_PROPERTY = "config.defaultPortConfig.uplinkTeamingPolicy.uplinkPortOrder.activeUplinkPort";
/*      */   private static final String DISTRIBUTED_VIRTUAL_SWITCH_PROPERTY = "config.distributedVirtualSwitch";
/*      */   private static final String HOST_VSANCONFIG_DISK_MAPPING_PROPERTY = "config.vsanHostConfig.storageInfo.diskMapping";
/*      */   private static final String CLUSTER_PROPERTY = "cluster";
/*      */   private static final String VSAN_HOST_CONFIG_NETWORKINFO_PORT_PROPERTY = "config.vsanHostConfig.networkInfo.port";
/*      */   private static final String HOST_CONFIG_NETWORK_VNIC_PROPERTY = "config.network.vnic";
/*      */   private static final String VM_CONFIG_UUID_PROPERTY = "config.instanceUuid";
/*      */   private static final long MILISECONDS_IN_HOUR = 3600000L;
/*      */   private static final long MILISECONDS_IN_5_MINUTES = 300000L;
/*      */   private static final String PERF_CHARTS_DATA_SEPARATOR = ",";
/*      */   private static final String CAPACITY_HISTORY_DEDUPLICATION_KEY = "savedByDedup";
/*      */   private static final String CAPACITY_HISTORY_DEDUPLICATION_RATIO_KEY = "dedupRatio";
/*      */   @Autowired
/*      */   private PermissionService permissionService;
/*      */   @Autowired
/*      */   private VsanConfigService vsanConfigService;
/*      */   
/*      */   @TsService
/*      */   public List<ServerObjectInfo> getEntitiesInfo(ManagedObjectReference clusterRef) throws Exception {
/*   98 */     List<ServerObjectInfo> entities = new ArrayList<>();
/*   99 */     Map<Object, Map<String, Object>> result = QueryUtil.getProperties(clusterRef, new String[] {
/*  100 */           "name", "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.uuid" }).getMap();
/*  101 */     Map<String, Object> properties = result.get(clusterRef);
/*  102 */     ServerObjectInfo clusterInfo = new ServerObjectInfo();
/*  103 */     clusterInfo.isCluster = true;
/*  104 */     clusterInfo.name = (String)properties.get("name");
/*  105 */     clusterInfo.vsanUuid = (String)properties.get("configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.uuid");
/*  106 */     entities.add(clusterInfo);
/*      */     
/*  108 */     DataServiceResponse response = QueryUtil.getPropertiesForRelatedObjects(
/*  109 */         clusterRef, 
/*  110 */         "host", 
/*  111 */         HostSystem.class.getSimpleName(), 
/*  112 */         new String[] { "name", "config.vsanHostConfig.clusterInfo.nodeUuid" });
/*  113 */     if (response == null) {
/*  114 */       return entities;
/*      */     }
/*      */     
/*  117 */     for (Object resourceObject : response.getResourceObjects()) {
/*  118 */       ManagedObjectReference hostRef = (ManagedObjectReference)resourceObject;
/*      */       
/*  120 */       ServerObjectInfo hostInfo = new ServerObjectInfo();
/*  121 */       hostInfo.isCluster = false;
/*  122 */       hostInfo.name = (String)response.getProperty(hostRef, "name");
/*  123 */       hostInfo.vsanUuid = (String)response.getProperty(hostRef, "config.vsanHostConfig.clusterInfo.nodeUuid");
/*  124 */       entities.add(hostInfo);
/*      */     } 
/*      */     
/*  127 */     return entities;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<PerfEntityStateData> getEntityPerfStateForWildcards(ManagedObjectReference clusterRef, PerfQuerySpec[] specs) throws Exception {
/*  138 */     Map<String, PerfEntityStateData> entitiesDataMap = new HashMap<>(); byte b; int i; PerfQuerySpec[] arrayOfPerfQuerySpec;
/*  139 */     for (i = (arrayOfPerfQuerySpec = specs).length, b = 0; b < i; ) { PerfQuerySpec spec = arrayOfPerfQuerySpec[b];
/*  140 */       List<PerfEntityStateData> stateDataList = new ArrayList<>();
/*  141 */       long startTime = spec.startTime.longValue();
/*  142 */       long endTime = startTime + 3600000L;
/*  143 */       while (endTime <= spec.endTime.longValue()) {
/*  144 */         PerfQuerySpec tempSpec = new PerfQuerySpec();
/*  145 */         tempSpec.startTime = Long.valueOf(startTime);
/*  146 */         tempSpec.endTime = Long.valueOf(endTime);
/*  147 */         tempSpec.entityType = spec.entityType;
/*  148 */         tempSpec.entityUuid = spec.entityUuid;
/*  149 */         stateDataList.addAll(getEntityPerfState(clusterRef, new PerfQuerySpec[] { tempSpec }, false));
/*      */         
/*  151 */         startTime = endTime + 300000L;
/*  152 */         endTime += 3600000L;
/*      */       } 
/*  154 */       if (!CollectionUtils.isEmpty(stateDataList))
/*      */       {
/*      */         
/*  157 */         entitiesDataMap = aggregateStateDataAndUpdateMap(stateDataList, entitiesDataMap); }  b++; }
/*      */     
/*  159 */     return new ArrayList<>(entitiesDataMap.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, PerfEntityStateData> aggregateStateDataAndUpdateMap(List<PerfEntityStateData> stateDataList, Map<String, PerfEntityStateData> entitiesDataMap) {
/*  168 */     for (PerfEntityStateData stateData : stateDataList) {
/*  169 */       PerfEntityStateData aggregatedStateData = entitiesDataMap.get(stateData.entityRefId);
/*  170 */       if (aggregatedStateData == null) {
/*  171 */         aggregatedStateData = new PerfEntityStateData();
/*  172 */         aggregatedStateData.metricsSeries = new ArrayList();
/*  173 */         aggregatedStateData.timeStamps = new ArrayList();
/*      */       } 
/*      */       
/*  176 */       aggregatedStateData.entityRefId = stateData.entityRefId;
/*  177 */       aggregatedStateData.metricsSeries = aggregateMetricsData(aggregatedStateData.metricsSeries, 
/*  178 */           stateData.metricsSeries);
/*  179 */       aggregatedStateData.timeStamps.addAll(stateData.timeStamps);
/*      */       
/*  181 */       entitiesDataMap.put(stateData.entityRefId, aggregatedStateData);
/*      */     } 
/*  183 */     return entitiesDataMap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<PerfGraphMetricsData> aggregateMetricsData(List<PerfGraphMetricsData> base, List<PerfGraphMetricsData> newMetrics) {
/*  191 */     if (CollectionUtils.isEmpty(base)) {
/*  192 */       return newMetrics;
/*      */     }
/*  194 */     for (PerfGraphMetricsData data : base) {
/*  195 */       for (PerfGraphMetricsData newData : newMetrics) {
/*  196 */         if (newData.key.equals(data.key)) {
/*  197 */           data.values.addAll(newData.values);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  202 */     return base;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<HostDiskGroupsData> getClusterDiskMappings(ManagedObjectReference clusterRef) throws Exception {
/*  207 */     List<HostDiskGroupsData> hostDiskgroups = new ArrayList<>();
/*  208 */     DataServiceResponse response = 
/*  209 */       QueryUtil.getPropertiesForRelatedObjects(clusterRef, 
/*  210 */         "host", 
/*  211 */         HostSystem.class.getSimpleName(), 
/*  212 */         new String[] { "name", "config.vsanHostConfig.storageInfo.diskMapping" });
/*  213 */     if (response == null) {
/*  214 */       return hostDiskgroups;
/*      */     }
/*  216 */     for (Object resourceObject : response.getResourceObjects()) {
/*  217 */       ManagedObjectReference hostRef = (ManagedObjectReference)resourceObject;
/*  218 */       DiskMapping[] diskMappings = (DiskMapping[])response.getProperty(hostRef, 
/*  219 */           "config.vsanHostConfig.storageInfo.diskMapping");
/*  220 */       if (ArrayUtils.isEmpty((Object[])diskMappings)) {
/*      */         continue;
/*      */       }
/*  223 */       List<DiskGroup> groups = new ArrayList<>(); byte b; int i; DiskMapping[] arrayOfDiskMapping1;
/*  224 */       for (i = (arrayOfDiskMapping1 = diskMappings).length, b = 0; b < i; ) { DiskMapping diskMapping = arrayOfDiskMapping1[b];
/*  225 */         groups.add(DiskGroup.fromDiskMapping(diskMapping)); b++; }
/*      */       
/*  227 */       HostDiskGroupsData diskgroupData = new HostDiskGroupsData();
/*  228 */       diskgroupData.hostName = (String)response.getProperty(hostRef, "name");
/*  229 */       diskgroupData.diskgroups = groups;
/*  230 */       hostDiskgroups.add(diskgroupData);
/*      */     } 
/*      */     
/*  233 */     return hostDiskgroups;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<PerfEntityStateData> getEntityPerfState(ManagedObjectReference clusterRef, PerfQuerySpec[] specs) throws Exception {
/*  239 */     return getEntityPerfState(clusterRef, specs, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public CapacityHistoryBasicInfo getCapacityHistoryBasicInfo(ManagedObjectReference objectRef) throws Exception {
/*  245 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/*  246 */     Validate.notNull(clusterRef);
/*      */     
/*  248 */     CapacityHistoryBasicInfo info = new CapacityHistoryBasicInfo();
/*  249 */     info.clusterRef = clusterRef;
/*  250 */     info.entityTypes = getPerfEntityTypes(clusterRef);
/*  251 */     info.isPerformanceServiceEnabled = getPerfServiceEnabled(clusterRef).booleanValue();
/*  252 */     info.hasEditPermission = this.permissionService.hasPermissions(clusterRef, 
/*  253 */         new String[] { "Host.Inventory.EditCluster" });
/*  254 */     info.hasReadPoliciesPermission = this.permissionService.hasVcPermissions(clusterRef, 
/*  255 */         new String[] { "StorageProfile.View" });
/*  256 */     return info;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public PerfEntityStateData getHistoricalSpaceReport(ManagedObjectReference objectRef, PerfQuerySpec[] specs) throws Exception {
/*  262 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/*  263 */     Validate.notNull(clusterRef);
/*      */     
/*  265 */     String entityUuid = (String)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.uuid", null); byte b; int i; PerfQuerySpec[] arrayOfPerfQuerySpec;
/*  266 */     for (i = (arrayOfPerfQuerySpec = specs).length, b = 0; b < i; ) { PerfQuerySpec spec = arrayOfPerfQuerySpec[b];
/*  267 */       spec.entityUuid = entityUuid; b++; }
/*      */     
/*  269 */     List<PerfEntityStateData> perfState = getEntityPerfState(clusterRef, specs, true);
/*  270 */     if (perfState == null || perfState.size() == 0) {
/*  271 */       return null;
/*      */     }
/*  273 */     PerfEntityStateData chartsData = perfState.get(0);
/*  274 */     if (chartsData == null || chartsData.metricsSeries == null || chartsData.metricsSeries.size() == 0 || 
/*  275 */       chartsData.timeStamps == null || chartsData.timeStamps.size() == 0) {
/*  276 */       return null;
/*      */     }
/*      */     
/*  279 */     boolean dedupEnabled = ((Boolean)QueryUtil.getProperty(clusterRef, "dataEfficiencyStatus", null)).booleanValue();
/*  280 */     if (!dedupEnabled) {
/*  281 */       List<PerfGraphMetricsData> metrics = chartsData.metricsSeries;
/*  282 */       Iterator<PerfGraphMetricsData> iterator = metrics.iterator();
/*  283 */       while (iterator.hasNext()) {
/*  284 */         PerfGraphMetricsData metric = iterator.next();
/*  285 */         if (metric.key.equalsIgnoreCase("savedByDedup") || 
/*  286 */           metric.key.equalsIgnoreCase("dedupRatio")) {
/*  287 */           iterator.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*  291 */     return chartsData;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<PerfEntityStateData> getEntityPerfState(ManagedObjectReference clusterRef, PerfQuerySpec[] specs, boolean sparseMetrics) throws Exception {
/*  296 */     EntityPerfStateObject perfState = new EntityPerfStateObject();
/*  297 */     VsanPerfEntityMetricCSV[] results = new VsanPerfEntityMetricCSV[0];
/*  298 */     List<VsanPerfQuerySpec> querySpecs = new ArrayList<>(specs.length); byte b; int i;
/*      */     PerfQuerySpec[] arrayOfPerfQuerySpec;
/*  300 */     for (i = (arrayOfPerfQuerySpec = specs).length, b = 0; b < i; ) { PerfQuerySpec spec = arrayOfPerfQuerySpec[b];
/*  301 */       querySpecs.add(PerfQuerySpec.toVmodl(spec)); b++; }
/*      */     
/*      */     try {
/*  304 */       Exception exception2, exception1 = null;
/*      */ 
/*      */     
/*      */     }
/*  308 */     catch (Timedout ex) {
/*  309 */       perfState.errorMessage = ex.getLocalizedMessage();
/*  310 */     } catch (VimFault ex) {
/*  311 */       throw ex;
/*  312 */     } catch (Exception ex) {
/*  313 */       throw Utils.getMethodFault(ex);
/*      */     } 
/*      */     
/*  316 */     return parseStateObjectChartData(perfState);
/*      */   }
/*      */   
/*      */   private List<PerfEntityStateData> parseStateObjectChartData(EntityPerfStateObject perfState) {
/*  320 */     List<PerfEntityStateData> stateDataList = new ArrayList<>(); byte b; int i; VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/*  321 */     for (i = (arrayOfVsanPerfEntityMetricCSV = perfState.metrics).length, b = 0; b < i; ) { VsanPerfEntityMetricCSV metric = arrayOfVsanPerfEntityMetricCSV[b];
/*  322 */       stateDataList.add(PerfEntityStateData.parsePerfEntityMetricCSV(metric)); b++; }
/*      */     
/*  324 */     return stateDataList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private VsanPerfEntityMetricCSV[] sparseChartPoints(VsanPerfEntityMetricCSV[] metrics) {
/*  331 */     if (metrics == null || metrics.length <= 1) {
/*  332 */       return metrics;
/*      */     }
/*      */     
/*  335 */     VsanPerfEntityMetricCSV metric = combineMetrics(metrics);
/*  336 */     if (!isValuableMetric(metric)) {
/*  337 */       return new VsanPerfEntityMetricCSV[0];
/*      */     }
/*      */ 
/*      */     
/*  341 */     int metricsInterval = (metric.value[0]).metricId.metricsCollectInterval.intValue();
/*      */     
/*  343 */     int interval = (int)Math.floor((3600 / metricsInterval));
/*      */ 
/*      */     
/*  346 */     List<String> newSampleInfos = new ArrayList<>();
/*  347 */     String[] sampleInfos = metric.sampleInfo.split(",");
/*  348 */     for (int infosIndex = 0; infosIndex < sampleInfos.length; infosIndex++) {
/*  349 */       if (infosIndex % interval == 0) {
/*  350 */         newSampleInfos.add(sampleInfos[infosIndex]);
/*      */       }
/*      */     } 
/*  353 */     metric.sampleInfo = StringUtils.join(newSampleInfos, ","); byte b; int i;
/*      */     VsanPerfMetricSeriesCSV[] arrayOfVsanPerfMetricSeriesCSV;
/*  355 */     for (i = (arrayOfVsanPerfMetricSeriesCSV = metric.value).length, b = 0; b < i; ) { VsanPerfMetricSeriesCSV value = arrayOfVsanPerfMetricSeriesCSV[b];
/*  356 */       List<String> newValues = new ArrayList<>();
/*  357 */       String[] values = value.values.split(",");
/*  358 */       for (int valuesIndex = 0; valuesIndex < values.length; valuesIndex++) {
/*  359 */         if (valuesIndex % interval == 0) {
/*  360 */           newValues.add(values[valuesIndex]);
/*      */         }
/*      */       } 
/*  363 */       value.values = StringUtils.join(newValues, ",");
/*      */       b++; }
/*      */     
/*  366 */     return new VsanPerfEntityMetricCSV[] { metric };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private VsanPerfEntityMetricCSV combineMetrics(VsanPerfEntityMetricCSV[] metrics) {
/*  373 */     VsanPerfEntityMetricCSV metric = new VsanPerfEntityMetricCSV();
/*      */     
/*  375 */     StringBuilder newSampleInfo = new StringBuilder();
/*  376 */     for (int metricsIndex = 0; metricsIndex < metrics.length; metricsIndex++) {
/*  377 */       VsanPerfEntityMetricCSV metricCSV = metrics[metricsIndex];
/*  378 */       if (isValuableMetric(metricCSV))
/*      */       {
/*      */ 
/*      */         
/*  382 */         if (ArrayUtils.isEmpty((Object[])metric.value)) {
/*  383 */           metric.value = metricCSV.value;
/*  384 */           newSampleInfo.append(metricCSV.sampleInfo);
/*      */         } else {
/*  386 */           for (int valueIndex = 0; valueIndex < metricCSV.value.length; valueIndex++) {
/*  387 */             StringBuilder newValues = new StringBuilder((metric.value[valueIndex]).values);
/*  388 */             newValues.append(",").append((metricCSV.value[valueIndex]).values);
/*  389 */             (metric.value[valueIndex]).values = newValues.toString();
/*      */           } 
/*  391 */           newSampleInfo.append(",").append(metricCSV.sampleInfo);
/*      */         }  } 
/*      */     } 
/*  394 */     metric.sampleInfo = newSampleInfo.toString();
/*      */     
/*  396 */     return metric;
/*      */   }
/*      */   
/*      */   private boolean isValuableMetric(VsanPerfEntityMetricCSV metric) {
/*  400 */     return (metric != null && !StringUtils.isEmpty(metric.sampleInfo) && !ArrayUtils.isEmpty((Object[])metric.value));
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public Boolean getPerfServiceEnabled(ManagedObjectReference clusterRef) {
/*  405 */     boolean isPerfSvcAutoConfigSupported = VsanCapabilityUtils.isPerfSvcAutoConfigSupportedOnVc(clusterRef);
/*      */     
/*  407 */     VsanPerfsvcConfig perfsvcConfig = null;
/*  408 */     if (isPerfSvcAutoConfigSupported) {
/*  409 */       VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef); try {
/*  410 */         Exception exception2, exception1 = null;
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  415 */       catch (Exception ex) {
/*  416 */         _logger.error("Failed to retrieve performance service status: ", ex);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  426 */     Boolean isEnabled = Boolean.valueOf(false); try {
/*  427 */       Exception exception2, exception1 = null;
/*      */ 
/*      */     
/*      */     }
/*  431 */     catch (Exception ex) {
/*  432 */       _logger.error("Failed to retrieve performance service information: ", ex);
/*      */     } 
/*  434 */     return isEnabled;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public PerfTimeRangeData[] getSavedTimeRanges(ManagedObjectReference clusterRef) throws Exception {
/*  439 */     List<PerfTimeRangeData> list = new ArrayList<>(); try {
/*  440 */       Exception exception2, exception1 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  453 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  458 */     PerfTimeRangeData[] t = new PerfTimeRangeData[list.size()];
/*  459 */     return list.<PerfTimeRangeData>toArray(t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public String getConfiguredPolicy(ManagedObjectReference clusterRef) throws Exception {
/*  470 */     boolean isPerfSvcAutoConfigSupported = VsanCapabilityUtils.isPerfSvcAutoConfigSupportedOnVc(clusterRef);
/*  471 */     if (isPerfSvcAutoConfigSupported) {
/*  472 */       Exception exception2; VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*  473 */       Exception exception1 = null;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  493 */     return (getStatesObjectInformation(clusterRef)).spbmProfileUuid;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public PerfStatsObjectInfo getStatesObjectInformation(ManagedObjectReference clusterRef) throws Exception {
/*  498 */     PerfStatsObjectInfo info = null;
/*      */     try {
/*  500 */       Exception exception2, exception1 = null;
/*      */ 
/*      */     
/*      */     }
/*  504 */     catch (Exception exception) {
/*      */ 
/*      */ 
/*      */       
/*  508 */       info = new PerfStatsObjectInfo();
/*      */     } 
/*      */     
/*  511 */     if (info.vsanObjectUuid != null && info.spbmProfileUuid != null) {
/*  512 */       ManagedObjectReference vcRootRef = VmodlHelper.getRootFolder(clusterRef.getServerGuid());
/*      */       try {
/*  514 */         PropertyValue[] resultset = QueryUtil.getPropertiesForRelatedObjects(
/*  515 */             vcRootRef, "pbmProfiles", "PbmRequirementStorageProfile", new String[] { "profileContent" }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  516 */         for (i = (arrayOfPropertyValue1 = resultset).length, b = 0; b < i; ) { PropertyValue profileContent = arrayOfPropertyValue1[b];
/*  517 */           Profile profile = (Profile)profileContent.value;
/*      */           
/*  519 */           if (profile.profileId.uniqueId.equals(info.spbmProfileUuid)) {
/*  520 */             info.spbmProfile = profile; break;
/*      */           } 
/*      */           b++; }
/*      */       
/*  524 */       } catch (Exception ex) {
/*  525 */         throw Utils.getMethodFault(ex);
/*      */       } 
/*      */     } 
/*  528 */     info.serviceEnabled = getPerfServiceEnabled(clusterRef).booleanValue();
/*  529 */     info.verboseModeEnabled = isPerfVerboseModeEnabled(clusterRef);
/*  530 */     info.networkDiagnosticModeEnabled = isPerfNetworkDiagnosticModeEnabled(clusterRef);
/*  531 */     return info;
/*      */   }
/*      */   
/*      */   private boolean isPerfVerboseModeEnabled(ManagedObjectReference clusterRef) throws Exception {
/*      */     try {
/*  536 */       VsanPerfNodeInformation node = getPerfStatsMasterNode(clusterRef);
/*  537 */       if (node != null && node.masterInfo != null && node.masterInfo.verboseMode != null) {
/*  538 */         return BooleanUtils.isTrue(node.masterInfo.verboseMode);
/*      */       }
/*  540 */     } catch (Exception ex) {
/*  541 */       throw Utils.getMethodFault(ex);
/*      */     } 
/*  543 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isPerfNetworkDiagnosticModeEnabled(ManagedObjectReference clusterRef) throws Exception {
/*  547 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*  548 */     VsanPerfsvcConfig perfConfig = null;
/*  549 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanConfigSystem.getConfigInfoEx"); 
/*  550 */       try { perfConfig = (vsanConfigSystem.getConfigInfoEx(clusterRef)).perfsvcConfig; }
/*  551 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */        }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public Map<String, VsanPerfEntityType> getPerfEntityTypes(ManagedObjectReference clusterRef) throws Exception {
/*  562 */     Map<String, VsanPerfEntityType> entitySpecMap = new HashMap<>(); try {
/*  563 */       Exception exception2, exception1 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  570 */     catch (Exception ex) {
/*  571 */       throw Utils.getMethodFault(ex);
/*      */     } 
/*  573 */     return entitySpecMap;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<DiskGroup> getDiskMappings(ManagedObjectReference host) throws Exception {
/*  578 */     List<DiskGroup> groups = new ArrayList<>();
/*  579 */     DiskMapping[] diskMappings = (DiskMapping[])QueryUtil.getProperty(host, 
/*  580 */         "config.vsanHostConfig.storageInfo.diskMapping", null);
/*  581 */     if (ArrayUtils.isEmpty((Object[])diskMappings))
/*  582 */       return groups;  byte b; int i;
/*      */     DiskMapping[] arrayOfDiskMapping1;
/*  584 */     for (i = (arrayOfDiskMapping1 = diskMappings).length, b = 0; b < i; ) { DiskMapping diskMapping = arrayOfDiskMapping1[b];
/*  585 */       groups.add(DiskGroup.fromDiskMapping(diskMapping)); b++; }
/*      */     
/*  587 */     return groups;
/*      */   }
/*      */ 
/*      */   
/*      */   private VsanPerfNodeInformation getPerfStatsMasterNode(ManagedObjectReference clusterRef) throws Exception {
/*  592 */     VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(clusterRef);
/*      */     
/*  594 */     VsanPerfNodeInformation[] nodes = null;
/*  595 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("perfMgr.queryNodeInformation"); 
/*  596 */       try { nodes = perfMgr.queryNodeInformation(clusterRef); }
/*  597 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */        }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public PerfMonitorCommonPropsData getPerfMonitorCommonPropsData(ManagedObjectReference serverObjRef) throws Exception {
/*  618 */     PerfMonitorCommonPropsData data = new PerfMonitorCommonPropsData();
/*      */     
/*  620 */     if (!ClusterComputeResource.class.getSimpleName().equals(serverObjRef.getType())) {
/*  621 */       data.clusterRef = (ManagedObjectReference)QueryUtil.getProperty(serverObjRef, "cluster", null);
/*      */     } else {
/*  623 */       data.clusterRef = serverObjRef;
/*      */     } 
/*      */     
/*  626 */     if (data.clusterRef != null) {
/*  627 */       Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("perfPropertyProvider.getCurrentTimeOnMasterNode"); 
/*  628 */         try { Calendar time = getCurrentTimeOnMasterNode(data.clusterRef);
/*  629 */           data.currentTimeOnMasterNode = (time == null) ? null : Long.valueOf(time.getTimeInMillis()); }
/*  630 */         finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */          }
/*      */ 
/*      */       
/*  634 */       data.hasReadPoliciesPermission = this.permissionService.hasVcPermissions(data.clusterRef, 
/*  635 */           new String[] { "StorageProfile.View" });
/*  636 */       data.isPerformanceServiceEnabled = getPerfServiceEnabled(data.clusterRef).booleanValue();
/*  637 */       VsanServiceData dpConfig = this.vsanConfigService.getDataProtectionConfig(data.clusterRef);
/*  638 */       data.isDataProtectionSupported = (dpConfig != null && dpConfig.status != VsanServiceStatus.NOT_SUPPORTED);
/*      */     } 
/*      */     
/*  641 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public Calendar getCurrentTimeOnMasterNode(ManagedObjectReference clusterRef) throws Exception {
/*  654 */     VsanPerfNodeInformation perfMasterNode = getPerfStatsMasterNode(clusterRef);
/*  655 */     if (perfMasterNode != null) {
/*  656 */       PropertyConstraint masterNode = QueryUtil.createPropertyConstraint(
/*  657 */           HostSystem.class.getSimpleName(), 
/*  658 */           "name", 
/*  659 */           Comparator.EQUALS, 
/*  660 */           perfMasterNode.hostname);
/*      */       
/*  662 */       String[] properties = { "currentTimeOnHost" };
/*  663 */       ResultSet resultSet = QueryUtil.getData(QueryUtil.buildQuerySpec((Constraint)masterNode, properties));
/*  664 */       DataServiceResponse response = QueryUtil.getDataServiceResponse(resultSet, properties);
/*      */       PropertyValue[] arrayOfPropertyValue;
/*  666 */       if ((arrayOfPropertyValue = response.getPropertyValues()).length != 0) { PropertyValue propertyValue = arrayOfPropertyValue[0];
/*  667 */         return (Calendar)propertyValue.value; }
/*      */     
/*      */     } 
/*  670 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<String> getActiveVmnicsForStandardNetworkConfiguration(NetworkInfo networkInfo, VirtualNic vn) {
/*  681 */     Set<String> activePnics = new HashSet<>();
/*  682 */     if (ArrayUtils.isEmpty((Object[])networkInfo.portgroup))
/*  683 */       return activePnics;  byte b; int i;
/*      */     PortGroup[] arrayOfPortGroup;
/*  685 */     for (i = (arrayOfPortGroup = networkInfo.portgroup).length, b = 0; b < i; ) { PortGroup pgroup = arrayOfPortGroup[b];
/*  686 */       if (!ArrayUtils.isEmpty((Object[])pgroup.port) && pgroup.computedPolicy != null && 
/*  687 */         pgroup.computedPolicy.nicTeaming != null) {
/*      */         byte b1; int j;
/*      */         PortGroup.Port[] arrayOfPort;
/*  690 */         for (j = (arrayOfPort = pgroup.port).length, b1 = 0; b1 < j; ) { PortGroup.Port p = arrayOfPort[b1];
/*  691 */           if (p.key != null && p.key.equals(vn.port)) {
/*      */ 
/*      */             
/*  694 */             NetworkPolicy.NicOrderPolicy nicOrder = pgroup.computedPolicy.nicTeaming.nicOrder;
/*  695 */             if (nicOrder != null) {
/*  696 */               activePnics.addAll(Arrays.asList(nicOrder.activeNic)); break;
/*      */             } 
/*      */           }  b1++; }
/*      */       
/*      */       }  b++; }
/*  701 */      return activePnics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<String> getActiveVmnicsFromDistributedSwitch(String switchUuid, NetworkInfo networkInfo, String[] uplinks) {
/*  712 */     Set<String> activePnics = new HashSet<>();
/*  713 */     if (ArrayUtils.isEmpty((Object[])networkInfo.proxySwitch) || 
/*  714 */       ArrayUtils.isEmpty((Object[])uplinks) || StringUtils.isBlank(switchUuid))
/*  715 */       return activePnics;  byte b; int i;
/*      */     HostProxySwitch[] arrayOfHostProxySwitch;
/*  717 */     for (i = (arrayOfHostProxySwitch = networkInfo.proxySwitch).length, b = 0; b < i; ) { HostProxySwitch proxySwitch = arrayOfHostProxySwitch[b];
/*  718 */       if (switchUuid.equals(proxySwitch.dvsUuid) && 
/*  719 */         !ArrayUtils.isEmpty((Object[])proxySwitch.uplinkPort)) {
/*      */ 
/*      */         
/*  722 */         List<String> activeUplinkKeys = new ArrayList<>(); byte b1; int j; String[] arrayOfString;
/*  723 */         for (j = (arrayOfString = uplinks).length, b1 = 0; b1 < j; ) { String uplink = arrayOfString[b1]; byte b2; int k; KeyValue[] arrayOfKeyValue;
/*  724 */           for (k = (arrayOfKeyValue = proxySwitch.uplinkPort).length, b2 = 0; b2 < k; ) { KeyValue kv = arrayOfKeyValue[b2];
/*  725 */             if (kv.value.equals(uplink))
/*      */             {
/*      */ 
/*      */ 
/*      */               
/*  730 */               activeUplinkKeys.add(kv.key);
/*      */             }
/*      */             b2++; }
/*      */           
/*  734 */           if (!ArrayUtils.isEmpty((Object[])proxySwitch.hostLag)) {
/*      */             HostProxySwitch.HostLagConfig[] arrayOfHostLagConfig;
/*      */             
/*  737 */             for (k = (arrayOfHostLagConfig = proxySwitch.hostLag).length, b2 = 0; b2 < k; ) { HostProxySwitch.HostLagConfig lagConfig = arrayOfHostLagConfig[b2];
/*  738 */               if (lagConfig.lagName.equals(uplink) && !ArrayUtils.isEmpty((Object[])lagConfig.uplinkPort)) {
/*      */                 byte b3; int m;
/*      */                 KeyValue[] arrayOfKeyValue1;
/*  741 */                 for (m = (arrayOfKeyValue1 = lagConfig.uplinkPort).length, b3 = 0; b3 < m; ) { KeyValue keyValue = arrayOfKeyValue1[b3];
/*  742 */                   activeUplinkKeys.add(keyValue.key); b3++; }
/*      */               
/*      */               }  b2++; }
/*      */           
/*      */           }  b1++; }
/*  747 */          if (!CollectionUtils.isEmpty(activeUplinkKeys) && 
/*  748 */           proxySwitch.spec != null && proxySwitch.spec.backing != null) {
/*      */ 
/*      */           
/*  751 */           HostMember.PnicBacking backing = (HostMember.PnicBacking)proxySwitch.spec.backing;
/*  752 */           if (!ArrayUtils.isEmpty((Object[])backing.pnicSpec)) {
/*      */             HostMember.PnicSpec[] arrayOfPnicSpec;
/*      */             
/*  755 */             for (int k = (arrayOfPnicSpec = backing.pnicSpec).length; j < k; ) { HostMember.PnicSpec spec = arrayOfPnicSpec[j];
/*  756 */               if (activeUplinkKeys.contains(spec.uplinkPortKey))
/*  757 */                 activePnics.add(spec.pnicDevice);  j++; } 
/*      */           } 
/*      */         } 
/*      */       }  b++; }
/*  761 */      return activePnics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ActiveVmnicDataSpec getDvsConfigurationsFromHostNetworks(ManagedObjectReference[] networks) throws Exception {
/*  773 */     ActiveVmnicDataSpec vmnicSpec = new ActiveVmnicDataSpec();
/*  774 */     List<ManagedObjectReference> switches = new ArrayList<>();
/*  775 */     Map<String, ManagedObjectReference> uuidSwitchMap = new HashMap<>();
/*  776 */     Map<ManagedObjectReference, ManagedObjectReference> switchNetworkMap = new HashMap<>();
/*  777 */     Map<ManagedObjectReference, String[]> networkUplinksMap = (Map)new HashMap<>();
/*  778 */     PropertyValue[] pv = QueryUtil.getProperties((Object[])networks, new String[] {
/*  779 */           "config.distributedVirtualSwitch", "config.defaultPortConfig.uplinkTeamingPolicy.uplinkPortOrder.activeUplinkPort" }).getPropertyValues();
/*  780 */     if (!ArrayUtils.isEmpty((Object[])pv)) {
/*  781 */       byte b; int i; PropertyValue[] arrayOfPropertyValue; for (i = (arrayOfPropertyValue = pv).length, b = 0; b < i; ) { ManagedObjectReference _switch; String[] activeUplinks; PropertyValue property = arrayOfPropertyValue[b];
/*  782 */         ManagedObjectReference _network = (ManagedObjectReference)property.resourceObject; String str;
/*  783 */         switch ((str = property.propertyName).hashCode()) { case -418780824: if (!str.equals("config.distributedVirtualSwitch"))
/*      */               break; 
/*  785 */             _switch = (ManagedObjectReference)property.value;
/*  786 */             if (_switch == null) {
/*      */               break;
/*      */             }
/*  789 */             switches.add(_switch);
/*  790 */             switchNetworkMap.put(_switch, _network); break;
/*      */           case 406754676:
/*      */             if (!str.equals("config.defaultPortConfig.uplinkTeamingPolicy.uplinkPortOrder.activeUplinkPort"))
/*  793 */               break;  activeUplinks = (String[])property.value;
/*  794 */             if (activeUplinks == null) {
/*      */               break;
/*      */             }
/*  797 */             networkUplinksMap.put(_network, activeUplinks); break; }
/*      */         
/*      */         b++; }
/*      */     
/*      */     } 
/*  802 */     if (!CollectionUtils.isEmpty(switches)) {
/*  803 */       PropertyValue[] props = QueryUtil.getProperties(switches.toArray(), 
/*  804 */           new String[] { "uuid" }).getPropertyValues();
/*  805 */       if (!ArrayUtils.isEmpty((Object[])props)) {
/*  806 */         byte b; int i; PropertyValue[] arrayOfPropertyValue; for (i = (arrayOfPropertyValue = props).length, b = 0; b < i; ) { PropertyValue prop = arrayOfPropertyValue[b];
/*  807 */           uuidSwitchMap.put((String)prop.value, 
/*  808 */               (ManagedObjectReference)prop.resourceObject);
/*      */           b++; }
/*      */       
/*      */       } 
/*      */     } 
/*  813 */     vmnicSpec.switches = switches;
/*  814 */     vmnicSpec.uuidSwitchMap = uuidSwitchMap;
/*  815 */     vmnicSpec.switchNetworkMap = switchNetworkMap;
/*  816 */     vmnicSpec.networkUplinksMap = networkUplinksMap;
/*      */     
/*  818 */     return vmnicSpec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<HostPnicsData> getHostPhysicalAdapterMapping(ManagedObjectReference serverObjRef) throws Exception {
/*  842 */     List<HostPnicsData> hostPnics = new ArrayList<>();
/*  843 */     DataServiceResponse response = getPnicQueryData(serverObjRef);
/*  844 */     if (response == null) {
/*  845 */       return hostPnics;
/*      */     }
/*  847 */     for (Object resourceObject : response.getResourceObjects()) {
/*  848 */       Set<String> activePnics = new HashSet<>();
/*  849 */       ManagedObjectReference hostRef = (ManagedObjectReference)resourceObject;
/*      */       
/*  851 */       NetworkInfo networkInfo = (NetworkInfo)response.getProperty(hostRef, "config.network");
/*  852 */       ManagedObjectReference[] networks = 
/*  853 */         (ManagedObjectReference[])response.getProperty(hostRef, "network");
/*  854 */       String hostUuid = (String)response.getProperty(hostRef, "config.vsanHostConfig.clusterInfo.nodeUuid");
/*      */       
/*  856 */       ConfigInfo.NetworkInfo.PortConfig[] portConfigs = 
/*  857 */         (ConfigInfo.NetworkInfo.PortConfig[])response.getProperty(hostRef, "config.vsanHostConfig.networkInfo.port");
/*  858 */       VirtualNic[] configuredVnics = 
/*  859 */         (VirtualNic[])response.getProperty(hostRef, "config.network.vnic");
/*      */       
/*  861 */       if (ArrayUtils.isEmpty((Object[])portConfigs) || ArrayUtils.isEmpty((Object[])configuredVnics) || 
/*  862 */         networkInfo == null || ArrayUtils.isEmpty((Object[])networkInfo.vnic)) {
/*      */         continue;
/*      */       }
/*      */       
/*  866 */       ActiveVmnicDataSpec vmnicSpec = null;
/*  867 */       if (networks != null) {
/*  868 */         vmnicSpec = getDvsConfigurationsFromHostNetworks(networks);
/*      */       }
/*  870 */       List<PerfVnicEntity> activeVnics = getVsanUsedVnicEntities(portConfigs, configuredVnics, hostUuid);
/*  871 */       for (PerfVnicEntity activeVnic : activeVnics) {
/*  872 */         byte b1; int j; VirtualNic[] arrayOfVirtualNic; for (j = (arrayOfVirtualNic = networkInfo.vnic).length, b1 = 0; b1 < j; ) { VirtualNic vnic = arrayOfVirtualNic[b1];
/*  873 */           if (vnic.device.equals(activeVnic.deviceName))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  883 */             if (!StringUtils.isEmpty(vnic.port)) {
/*  884 */               activePnics.addAll(getActiveVmnicsForStandardNetworkConfiguration(networkInfo, vnic));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*  895 */             else if (vmnicSpec != null) {
/*      */ 
/*      */               
/*  898 */               String switchUuid = (vnic.spec.distributedVirtualPort == null) ? null : 
/*  899 */                 vnic.spec.distributedVirtualPort.switchUuid;
/*      */               
/*  901 */               String[] uplinkArr = getActiveUplinkNamesOnHost(vmnicSpec, vnic);
/*      */               
/*  903 */               activePnics.addAll(getActiveVmnicsFromDistributedSwitch(
/*  904 */                     switchUuid, networkInfo, uplinkArr));
/*      */             }  }  b1++; }
/*      */       
/*  907 */       }  String[] activePnicArr = new String[activePnics.size()];
/*  908 */       activePnicArr = activePnics.<String>toArray(activePnicArr);
/*  909 */       Arrays.sort((Object[])activePnicArr);
/*      */       
/*  911 */       List<PerfPhysicalAdapterEntity> pnics = new ArrayList<>(); byte b; int i; String[] arrayOfString1;
/*  912 */       for (i = (arrayOfString1 = activePnicArr).length, b = 0; b < i; ) { String pnic = arrayOfString1[b];
/*  913 */         PerfPhysicalAdapterEntity entity = new PerfPhysicalAdapterEntity();
/*  914 */         entity.hostUuid = hostUuid;
/*  915 */         entity.deviceName = pnic;
/*  916 */         pnics.add(entity); b++; }
/*      */       
/*  918 */       HostPnicsData pnicData = new HostPnicsData();
/*  919 */       pnicData.hostName = (String)response.getProperty(hostRef, "name");
/*  920 */       pnicData.pnics = pnics;
/*  921 */       hostPnics.add(pnicData);
/*      */     } 
/*  923 */     return hostPnics;
/*      */   }
/*      */ 
/*      */   
/*      */   private DataServiceResponse getPnicQueryData(ManagedObjectReference serverObjRef) throws Exception {
/*  928 */     DataServiceResponse response = null;
/*  929 */     if (ClusterComputeResource.class.getSimpleName().equals(serverObjRef.getType())) {
/*  930 */       response = QueryUtil.getPropertiesForRelatedObjects(serverObjRef, 
/*  931 */           "host", 
/*  932 */           HostSystem.class.getSimpleName(), 
/*  933 */           new String[] { "name", 
/*  934 */             "config.vsanHostConfig.clusterInfo.nodeUuid", 
/*  935 */             "config.network", 
/*  936 */             "network", 
/*  937 */             "config.vsanHostConfig.networkInfo.port", 
/*  938 */             "config.network.vnic" });
/*  939 */     } else if (HostSystem.class.getSimpleName().equals(serverObjRef.getType())) {
/*  940 */       response = QueryUtil.getProperties(serverObjRef, 
/*  941 */           new String[] { "name", 
/*  942 */             "config.vsanHostConfig.clusterInfo.nodeUuid", 
/*  943 */             "config.network", 
/*  944 */             "network", 
/*  945 */             "config.vsanHostConfig.networkInfo.port", 
/*  946 */             "config.network.vnic" });
/*      */     } 
/*  948 */     return response;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<PerfVnicEntity> getVsanUsedVnicEntities(ConfigInfo.NetworkInfo.PortConfig[] portConfigs, VirtualNic[] configuredVnics, String hostUuid) {
/*  953 */     List<String> allActiveVnics = new ArrayList<>(); byte b; int i;
/*      */     ConfigInfo.NetworkInfo.PortConfig[] arrayOfPortConfig;
/*  955 */     for (i = (arrayOfPortConfig = portConfigs).length, b = 0; b < i; ) { ConfigInfo.NetworkInfo.PortConfig c = arrayOfPortConfig[b];
/*  956 */       allActiveVnics.add(c.getDevice());
/*      */       b++; }
/*      */     
/*  959 */     List<PerfVnicEntity> vnicEntities = new ArrayList<>();
/*      */ 
/*      */     
/*      */     VirtualNic[] arrayOfVirtualNic;
/*      */     
/*  964 */     for (int j = (arrayOfVirtualNic = configuredVnics).length; i < j; ) { VirtualNic vnic = arrayOfVirtualNic[i];
/*  965 */       if (vnic.device != null && allActiveVnics.contains(vnic.device)) {
/*  966 */         PerfVnicEntity vnicEntity = new PerfVnicEntity();
/*  967 */         vnicEntity.deviceName = vnic.device;
/*  968 */         vnicEntity.netStackInstanceKey = (vnic.getSpec()).netStackInstanceKey;
/*  969 */         vnicEntity.hostUuid = hostUuid;
/*  970 */         vnicEntities.add(vnicEntity);
/*      */       }  i++; }
/*      */     
/*  973 */     return vnicEntities;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<PerfPhysicalAdapterEntity> getHostPhysicalAdapters(ManagedObjectReference hostRef) throws Exception {
/*  979 */     List<HostPnicsData> result = getHostPhysicalAdapterMapping(hostRef);
/*  980 */     if (CollectionUtils.isEmpty(result)) {
/*  981 */       return null;
/*      */     }
/*      */     
/*  984 */     return ((HostPnicsData)result.get(0)).pnics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String[] getActiveUplinkNamesOnHost(ActiveVmnicDataSpec vmnicSpec, VirtualNic vn) throws Exception {
/*  998 */     String switchUuid = (vn.spec.distributedVirtualPort == null) ? null : 
/*  999 */       vn.spec.distributedVirtualPort.switchUuid;
/* 1000 */     if (vmnicSpec == null || StringUtils.isEmpty(switchUuid) || 
/* 1001 */       CollectionUtils.isEmpty(vmnicSpec.switches)) {
/* 1002 */       return new String[0];
/*      */     }
/*      */     
/* 1005 */     Set<String> uplinks = vmnicSpec.getUplinksBySwitchUuid(switchUuid);
/*      */     
/* 1007 */     String[] uplinkArr = new String[uplinks.size()];
/* 1008 */     uplinkArr = uplinks.<String>toArray(uplinkArr);
/* 1009 */     return uplinkArr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<HostVnicsData> getHostVnicsMapping(ManagedObjectReference serverObj) throws Exception {
/* 1021 */     List<HostVnicsData> hostVnics = new ArrayList<>();
/* 1022 */     DataServiceResponse response = getVnicQueryData(serverObj);
/* 1023 */     if (response == null) {
/* 1024 */       return hostVnics;
/*      */     }
/* 1026 */     for (Object resourceObject : response.getResourceObjects()) {
/* 1027 */       ManagedObjectReference hostRef = (ManagedObjectReference)resourceObject;
/*      */       
/* 1029 */       ConfigInfo.NetworkInfo.PortConfig[] portConfigs = 
/* 1030 */         (ConfigInfo.NetworkInfo.PortConfig[])response.getProperty(hostRef, "config.vsanHostConfig.networkInfo.port");
/* 1031 */       VirtualNic[] vnics = 
/* 1032 */         (VirtualNic[])response.getProperty(hostRef, "config.network.vnic");
/*      */       
/* 1034 */       if (ArrayUtils.isEmpty((Object[])portConfigs) || ArrayUtils.isEmpty((Object[])vnics)) {
/*      */         continue;
/*      */       }
/* 1037 */       String hostUuid = (String)response.getProperty(hostRef, "config.vsanHostConfig.clusterInfo.nodeUuid");
/* 1038 */       List<PerfVnicEntity> vnicEntities = getVsanUsedVnicEntities(portConfigs, vnics, hostUuid);
/*      */       
/* 1040 */       HostVnicsData vnicsData = new HostVnicsData();
/* 1041 */       vnicsData.hostName = (String)response.getProperty(hostRef, "name");
/* 1042 */       vnicsData.vnics = vnicEntities;
/* 1043 */       hostVnics.add(vnicsData);
/*      */     } 
/* 1045 */     return hostVnics;
/*      */   }
/*      */ 
/*      */   
/*      */   private DataServiceResponse getVnicQueryData(ManagedObjectReference serverObj) throws Exception {
/* 1050 */     DataServiceResponse response = null;
/* 1051 */     if (ClusterComputeResource.class.getSimpleName().equals(serverObj.getType())) {
/* 1052 */       response = QueryUtil.getPropertiesForRelatedObjects(serverObj, 
/* 1053 */           "host", 
/* 1054 */           HostSystem.class.getSimpleName(), 
/* 1055 */           new String[] { "name", 
/* 1056 */             "config.vsanHostConfig.clusterInfo.nodeUuid", 
/* 1057 */             "config.vsanHostConfig.networkInfo.port", 
/* 1058 */             "config.network.vnic" });
/* 1059 */     } else if (HostSystem.class.getSimpleName().equals(serverObj.getType())) {
/* 1060 */       response = QueryUtil.getProperties(
/* 1061 */           serverObj, 
/* 1062 */           new String[] { "name", 
/* 1063 */             "config.vsanHostConfig.clusterInfo.nodeUuid", 
/* 1064 */             "config.vsanHostConfig.networkInfo.port", 
/* 1065 */             "config.network.vnic" });
/*      */     } 
/* 1067 */     return response;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<PerfVnicEntity> getHostVirtualAdapters(ManagedObjectReference hostRef) throws Exception {
/* 1072 */     List<HostVnicsData> result = getHostVnicsMapping(hostRef);
/* 1073 */     if (CollectionUtils.isEmpty(result)) {
/* 1074 */       return null;
/*      */     }
/*      */     
/* 1077 */     return ((HostVnicsData)result.get(0)).vnics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public PerfVirtualMachineDiskData getVirtualMachineDiskData(ManagedObjectReference vmRef) throws Exception {
/* 1089 */     PerfVirtualMachineDiskData data = new PerfVirtualMachineDiskData();
/* 1090 */     DataServiceResponse response = QueryUtil.getProperties(
/* 1091 */         vmRef, new String[] { "config.instanceUuid", "config.hardware.device" });
/*      */     
/* 1093 */     if (response == null) {
/* 1094 */       return data;
/*      */     }
/* 1096 */     for (Object resourceObject : response.getResourceObjects()) {
/* 1097 */       data.vmUuid = (String)response.getProperty(resourceObject, "config.instanceUuid");
/* 1098 */       VirtualDevice[] vDevs = (VirtualDevice[])response.getProperty(resourceObject, "config.hardware.device");
/* 1099 */       data.vscsiEntities = getVscsiEntities(vDevs);
/* 1100 */       data.virtualDisks = getVirtualDiskEntities(vDevs);
/*      */     } 
/*      */     
/* 1103 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<PerfVscsiEntity> getVscsiEntities(VirtualDevice[] vDevs) throws Exception {
/* 1113 */     List<PerfVscsiEntity> vscsiEntities = new ArrayList<>();
/*      */     
/* 1115 */     if (vDevs.length > 0) {
/* 1116 */       List<VirtualDisk> vDisks = new ArrayList<>();
/* 1117 */       Map<Integer, VirtualController> vConts = new HashMap<>(); byte b;
/*      */       int i;
/*      */       VirtualDevice[] arrayOfVirtualDevice;
/* 1120 */       for (i = (arrayOfVirtualDevice = vDevs).length, b = 0; b < i; ) { VirtualDevice vDev = arrayOfVirtualDevice[b];
/* 1121 */         if (vDev instanceof VirtualDisk) {
/* 1122 */           vDisks.add((VirtualDisk)vDev);
/*      */         } else {
/*      */ 
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1129 */             if (vDev.getClass().getField("scsiCtlrUnitNumber") != null) {
/* 1130 */               vConts.put(Integer.valueOf(vDev.key), (VirtualController)vDev);
/*      */             }
/* 1132 */           } catch (Exception exception) {}
/*      */         } 
/*      */         
/*      */         b++; }
/*      */ 
/*      */       
/* 1138 */       Map<ManagedObjectReference, List<VirtualDisk>> datastoreMap = new HashMap<>();
/*      */       
/* 1140 */       for (VirtualDisk vdisk : vDisks) {
/* 1141 */         VirtualDevice.FileBackingInfo backing = (VirtualDevice.FileBackingInfo)vdisk.getBacking();
/*      */         
/* 1143 */         List<VirtualDisk> disksInSameStore = datastoreMap.get(backing.datastore);
/*      */         
/* 1145 */         if (disksInSameStore == null) {
/* 1146 */           disksInSameStore = new ArrayList<>();
/* 1147 */           datastoreMap.put(backing.datastore, disksInSameStore);
/*      */         } 
/*      */         
/* 1150 */         disksInSameStore.add(vdisk);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1155 */       Object[] keyArr = datastoreMap.keySet().toArray();
/* 1156 */       if (keyArr != null && keyArr.length > 0) {
/* 1157 */         PropertyValue[] dsTypeValues = QueryUtil.getProperties(
/* 1158 */             keyArr, new String[] { "summary.type" }).getPropertyValues();
/*      */         
/* 1160 */         List<ManagedObjectReference> vsanDsRefs = new ArrayList<>(); byte b1; int j;
/*      */         PropertyValue[] arrayOfPropertyValue1;
/* 1162 */         for (j = (arrayOfPropertyValue1 = dsTypeValues).length, b1 = 0; b1 < j; ) { PropertyValue dsType = arrayOfPropertyValue1[b1];
/* 1163 */           if (dsType.value.equals("vsan")) {
/* 1164 */             vsanDsRefs.add((ManagedObjectReference)dsType.resourceObject);
/*      */           }
/*      */           
/*      */           b1++; }
/*      */         
/* 1169 */         List<VirtualDisk> disksOnVsan = new ArrayList<>();
/*      */         
/* 1171 */         for (ManagedObjectReference dsRef : vsanDsRefs) {
/* 1172 */           List<VirtualDisk> disks = datastoreMap.get(dsRef);
/*      */           
/* 1174 */           if (disks != null) {
/* 1175 */             disksOnVsan.addAll(disks);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 1180 */         Map<Integer, List<VirtualDisk>> controllerMap = new HashMap<>();
/*      */         
/* 1182 */         for (VirtualDisk disk : disksOnVsan) {
/* 1183 */           List<VirtualDisk> disks = controllerMap.get(disk.controllerKey);
/*      */           
/* 1185 */           if (disks == null) {
/* 1186 */             disks = new ArrayList<>();
/* 1187 */             controllerMap.put(disk.controllerKey, disks);
/*      */           } 
/*      */           
/* 1190 */           disks.add(disk);
/*      */         } 
/*      */ 
/*      */         
/* 1194 */         for (Map.Entry<Integer, List<VirtualDisk>> entry : controllerMap.entrySet()) {
/* 1195 */           VirtualController controller = vConts.get(entry.getKey());
/*      */           
/* 1197 */           if (controller != null && !((List)entry.getValue()).isEmpty()) {
/* 1198 */             for (VirtualDisk disk : entry.getValue()) {
/* 1199 */               PerfVscsiEntity entity = new PerfVscsiEntity();
/* 1200 */               entity.busId = Integer.valueOf(controller.busNumber);
/* 1201 */               entity.controllerKey = controller.key;
/* 1202 */               entity.deviceName = disk.deviceInfo.label;
/* 1203 */               entity.vmdkName = ((VirtualDevice.FileBackingInfo)disk.backing).fileName;
/* 1204 */               entity.position = disk.unitNumber;
/*      */               
/* 1206 */               vscsiEntities.add(entity);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1214 */     return vscsiEntities;
/*      */   }
/*      */   
/*      */   private List<PerfVirtualDiskEntity> getVirtualDiskEntities(VirtualDevice[] vDevs) throws Exception {
/* 1218 */     List<PerfVirtualDiskEntity> virtualDiskEntities = new ArrayList<>();
/*      */     
/* 1220 */     Map<ManagedObjectReference, List<PerfVirtualDiskEntity>> dsMap = new HashMap<>(); byte b; int i;
/*      */     VirtualDevice[] arrayOfVirtualDevice;
/* 1222 */     for (i = (arrayOfVirtualDevice = vDevs).length, b = 0; b < i; ) { VirtualDevice dev = arrayOfVirtualDevice[b];
/* 1223 */       if (dev instanceof VirtualDisk) {
/* 1224 */         PerfVirtualDiskEntity entity = new PerfVirtualDiskEntity();
/*      */         
/* 1226 */         entity.diskName = dev.deviceInfo.label;
/* 1227 */         entity.controllerKey = dev.controllerKey.intValue();
/*      */         
/* 1229 */         VirtualDevice.FileBackingInfo backing = (VirtualDevice.FileBackingInfo)dev.getBacking();
/* 1230 */         ManagedObjectReference dsRef = backing.datastore;
/*      */         
/* 1232 */         Object type = QueryUtil.getProperty(dsRef, "summary.type", null);
/*      */ 
/*      */         
/* 1235 */         if (type != null && "vsan".equalsIgnoreCase(type.toString())) {
/* 1236 */           virtualDiskEntities.add(entity);
/* 1237 */           entity.vmdkPath = backing.getFileName();
/*      */ 
/*      */           
/* 1240 */           List<PerfVirtualDiskEntity> disds = dsMap.get(dsRef);
/*      */           
/* 1242 */           if (disds == null) {
/* 1243 */             disds = new ArrayList<>();
/* 1244 */             dsMap.put(dsRef, disds);
/*      */           } 
/*      */           
/* 1247 */           disds.add(entity);
/*      */         } 
/*      */       } 
/*      */       b++; }
/*      */     
/* 1252 */     Object[] keyArr = dsMap.keySet().toArray();
/* 1253 */     if (keyArr != null && keyArr.length > 0) {
/* 1254 */       PropertyValue[] dsPropsVals = QueryUtil.getProperties(
/* 1255 */           keyArr, new String[] { "name", "summary.url" }).getPropertyValues(); byte b1; int j;
/*      */       PropertyValue[] arrayOfPropertyValue1;
/* 1257 */       for (j = (arrayOfPropertyValue1 = dsPropsVals).length, b1 = 0; b1 < j; ) { PropertyValue propVal = arrayOfPropertyValue1[b1];
/* 1258 */         ManagedObjectReference moref = (ManagedObjectReference)propVal.resourceObject;
/* 1259 */         List<PerfVirtualDiskEntity> pvdes = dsMap.get(moref);
/*      */         String str;
/* 1261 */         switch ((str = propVal.propertyName).hashCode()) { case -1193995481: if (!str.equals("summary.url")) {
/*      */               break;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1268 */             for (PerfVirtualDiskEntity pvde : pvdes)
/* 1269 */               pvde.datastorePath = propVal.value.toString();  break;
/*      */           case 3373707:
/*      */             if (!str.equals("name"))
/*      */               break;  for (PerfVirtualDiskEntity pvde : pvdes)
/*      */               pvde.datastoreName = propVal.value.toString();  break; }
/*      */          b1++; }
/*      */     
/* 1276 */     }  for (PerfVirtualDiskEntity pvde : virtualDiskEntities) {
/* 1277 */       String dsNamePair = "[" + pvde.datastoreName + "] ";
/* 1278 */       int mark = pvde.vmdkPath.indexOf(dsNamePair);
/* 1279 */       pvde.vmdkPath = (mark != -1) ? pvde.vmdkPath.substring(mark + dsNamePair.length()) : pvde.vmdkPath;
/*      */       
/* 1281 */       mark = "ds://".length();
/* 1282 */       pvde.datastorePath = pvde.datastorePath.substring(mark);
/*      */     } 
/*      */     
/* 1285 */     return virtualDiskEntities;
/*      */   }
/*      */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/VsanPerfPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */