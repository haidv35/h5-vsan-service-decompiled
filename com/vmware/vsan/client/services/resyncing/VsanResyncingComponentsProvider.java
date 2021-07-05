/*     */ package com.vmware.vsan.client.services.resyncing;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.RepairTimerInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.RuntimeStatsHostMap;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.RuntimeStats;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.StatsType;
/*     */ import com.vmware.vsan.client.services.common.data.VmData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.DelayTimerData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.HostResyncTrafficData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.RepairTimerData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.ResyncMonitorData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.VsanSyncingObjectsQuerySpec;
/*     */ import com.vmware.vsan.client.services.virtualobjects.VsanObjectSystemProvider;
/*     */ import com.vmware.vsan.client.services.virtualobjects.VsanVirtualDisksDataProvider;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectsFilter;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ @Component
/*     */ public class VsanResyncingComponentsProvider {
/*  45 */   private static final String[] RUNTIME_STATS = new String[] { "repairTimerInfo" };
/*     */   
/*     */   private static final String RUNTIME_STAT_REPAIR_TIMER = "repairTimerInfo";
/*     */   
/*     */   private static final int TIMER_DEFAULT_VALUE = 0;
/*     */   
/*     */   @Autowired
/*     */   private VsanObjectSystemProvider vsanObjectSystemProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanVirtualDisksDataProvider virtualDisksDataProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanResyncingIscsiTargetComponentsProvider iscsiTargetComponentsProvider;
/*     */   @Autowired
/*     */   private VsanResyncingComponentsRetriever vsanSyncComponentsRetriever;
/*     */   @Autowired
/*     */   private VsanPropertyProvider vsanPropertyProvider;
/*  63 */   private static final Log _logger = LogFactory.getLog(VsanResyncingComponentsProvider.class);
/*     */   
/*     */   private static final String RESYNC_THROTTLING_PROPERTY = "vsanResyncThrottling";
/*  66 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  67 */       VsanResyncingComponentsProvider.class);
/*     */   
/*     */   @TsService
/*     */   public HostResyncTrafficData[] getHostsResyncTraffic(ManagedObjectReference clusterRef) throws Exception {
/*  71 */     Map<ManagedObjectReference, HostResyncTrafficData> hostsToResyncTrafficMap = 
/*  72 */       getHostsToResyncTrafficMap(clusterRef);
/*  73 */     if (hostsToResyncTrafficMap == null || hostsToResyncTrafficMap.size() == 0) {
/*  74 */       return new HostResyncTrafficData[0];
/*     */     }
/*     */     
/*  77 */     DataServiceResponse response = QueryUtil.getProperties(
/*  78 */         hostsToResyncTrafficMap.keySet().toArray(), 
/*  79 */         new String[] { "name", "primaryIconId" });
/*  80 */     if (response == null) {
/*  81 */       return new HostResyncTrafficData[0];
/*     */     }
/*  83 */     for (Object resourceObject : response.getResourceObjects()) {
/*  84 */       HostResyncTrafficData data = hostsToResyncTrafficMap.get(resourceObject);
/*  85 */       data.name = (String)response.getProperty(resourceObject, "name");
/*  86 */       data.primaryIconId = (String)response.getProperty(resourceObject, "primaryIconId");
/*     */     } 
/*     */     
/*  89 */     return (HostResyncTrafficData[])hostsToResyncTrafficMap.values().toArray((Object[])new HostResyncTrafficData[hostsToResyncTrafficMap.size()]);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsResyncThrottlingSupported(ManagedObjectReference clusterRef) {
/*  94 */     boolean resyncThrottlingSupported = VsanCapabilityUtils.isResyncThrottlingSupported(clusterRef);
/*  95 */     return resyncThrottlingSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<ManagedObjectReference, HostResyncTrafficData> getHostsToResyncTrafficMap(ManagedObjectReference clusterRef) throws Exception {
/*     */     Map<ManagedObjectReference, HostResyncTrafficData> hostToStatsMap;
/* 101 */     VsanVcClusterConfigSystem vsanConfigSystem = 
/* 102 */       VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 103 */     RuntimeStatsHostMap[] runtimeStats = null;
/* 104 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("vsanConfigSystem.getRuntimeStats"); 
/* 105 */       try { runtimeStats = vsanConfigSystem.getRuntimeStats(clusterRef, 
/* 106 */             new String[] { StatsType.resyncIopsInfo.toString() }); }
/* 107 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
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
/* 124 */     return hostToStatsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ResyncMonitorData getVsanDatastoreResyncingData(ManagedObjectReference clusterRef, int limit) throws Exception {
/*     */     Map<VirtualObjectsFilter, List<VsanObjectIdentity>> vsanObjectIdentitiesData;
/* 134 */     if (clusterRef == null || !isVsanEnabledOnCluster(clusterRef).booleanValue()) {
/* 135 */       return new ResyncMonitorData();
/*     */     }
/*     */     
/* 138 */     _logger.debug("Getting resyncing components on the vsan datastore.");
/*     */ 
/*     */     
/* 141 */     VsanSyncingObjectsQuerySpec spec = new VsanSyncingObjectsQuerySpec();
/*     */ 
/*     */     
/* 144 */     if (limit >= 0) {
/* 145 */       spec.limit = limit;
/*     */     }
/*     */     
/* 148 */     ResyncMonitorData resyncMonitorData = this.vsanSyncComponentsRetriever.getVsanResyncObjects(clusterRef, spec);
/*     */ 
/*     */     
/* 151 */     resyncMonitorData.isVsanClusterPartitioned = this.vsanPropertyProvider.getIsVsanClusterPartitioned(clusterRef);
/* 152 */     resyncMonitorData.isResyncThrottlingSupported = getIsResyncThrottlingSupported(clusterRef);
/* 153 */     resyncMonitorData.resyncThrottlingValue = ((Integer)QueryUtil.getProperty(clusterRef, "vsanResyncThrottling", null)).intValue();
/*     */     
/* 155 */     Exception exception1 = null, exception2 = null; try { Measure measure = new Measure("Retrieving delay/repair timer data"); 
/* 156 */       try { Future[] repairTimerDataFutures = (Future[])getRepairTimerDataFutures(clusterRef, measure);
/* 157 */         Future<ConfigInfoEx> configInfoExFuture = getConfigInfoExFuture(clusterRef, measure);
/*     */         
/* 159 */         resyncMonitorData.repairTimerData = getRepairTimerData((Future<RuntimeStats>[])repairTimerDataFutures);
/* 160 */         resyncMonitorData.delayTimerData = getDelayTimerData(configInfoExFuture); }
/* 161 */       finally { if (measure != null) measure.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
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
/* 173 */     Map<String, VsanObjectHealthData> vsanHealthData = this.vsanObjectSystemProvider.getVsanHealthData(
/* 174 */         clusterRef, resyncMonitorData.getVsanObjectUuids());
/*     */     
/* 176 */     if (!CollectionUtils.isEmpty(vsanObjectIdentitiesData.get(VirtualObjectsFilter.VMS))) {
/*     */       
/* 178 */       Map<ManagedObjectReference, VmData> vmDataMap = this.virtualDisksDataProvider.getVmData(clusterRef, 
/* 179 */           vsanObjectIdentitiesData.get(VirtualObjectsFilter.VMS));
/*     */ 
/*     */       
/* 182 */       resyncMonitorData.processVmObjects(
/* 183 */           vsanObjectIdentitiesData.get(VirtualObjectsFilter.VMS), 
/* 184 */           vmDataMap, 
/* 185 */           vsanHealthData);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 190 */     List<VsanObjectIdentity> iscsiIdentityData = vsanObjectIdentitiesData.get(VirtualObjectsFilter.ISCSI_TARGETS);
/* 191 */     if (!CollectionUtils.isEmpty(iscsiIdentityData)) {
/* 192 */       Map<String, VsanObject> iscsiObjects = this.iscsiTargetComponentsProvider.getIscsiResyncObjects(
/* 193 */           clusterRef, resyncMonitorData.getVsanObjectUuids());
/* 194 */       vsanHealthData.putAll(getIscsiExtraObjectsHealth(clusterRef, iscsiIdentityData, iscsiObjects));
/* 195 */       resyncMonitorData.processIscsiObjects(
/* 196 */           iscsiIdentityData, 
/* 197 */           vsanHealthData, 
/* 198 */           iscsiObjects);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 203 */     List<String> orphanedSyncObjects = getOrphanedSyncObjects(resyncMonitorData, vsanObjectIdentitiesData);
/* 204 */     resyncMonitorData.processOtherObjects(
/* 205 */         vsanObjectIdentitiesData.get(VirtualObjectsFilter.OTHERS), 
/* 206 */         orphanedSyncObjects, 
/* 207 */         vsanHealthData);
/*     */     
/* 209 */     return resyncMonitorData;
/*     */   }
/*     */   
/*     */   private DelayTimerData getDelayTimerData(Future<ConfigInfoEx> configInfoExFuture) {
/* 213 */     DelayTimerData delayTimerData = new DelayTimerData();
/* 214 */     if (configInfoExFuture == null) {
/* 215 */       delayTimerData.isSupported = false;
/*     */     } else {
/* 217 */       delayTimerData.isSupported = true;
/*     */       try {
/* 219 */         ConfigInfoEx configInfoEx = (ConfigInfoEx)configInfoExFuture.get();
/* 220 */         if (configInfoEx == null || configInfoEx.getExtendedConfig() == null) {
/* 221 */           delayTimerData.errorMessage = Utils.getLocalizedString("vsan.resyncing.delayTimer.error");
/* 222 */           _logger.error("Cannot retrieve the Delay Timer value because the configuration is null!");
/*     */         } else {
/* 224 */           delayTimerData.delayTimer = (configInfoEx.getExtendedConfig()).objectRepairTimer.longValue();
/*     */         } 
/* 226 */       } catch (Exception ex) {
/* 227 */         delayTimerData.errorMessage = Utils.getLocalizedString("vsan.resyncing.delayTimer.error");
/* 228 */         _logger.error("Cannot retrieve Delay Timer information: ", ex);
/*     */       } 
/*     */     } 
/* 231 */     return delayTimerData;
/*     */   }
/*     */   
/*     */   public RepairTimerData getRepairTimerData(Future[] repairTimerDataFutures) {
/* 235 */     RepairTimerData repairTimerData = new RepairTimerData();
/*     */     
/* 237 */     if (repairTimerDataFutures == null) {
/* 238 */       repairTimerData.isSupported = false;
/* 239 */     } else if (ArrayUtils.isEmpty((Object[])repairTimerDataFutures)) {
/* 240 */       repairTimerData.isSupported = true;
/*     */     } else {
/* 242 */       repairTimerData.isSupported = true;
/* 243 */       long maxTimer = Long.MIN_VALUE;
/* 244 */       long minTimer = Long.MAX_VALUE;
/* 245 */       long objectsCount = 0L;
/* 246 */       long todayInMilliseconds = (new Date()).getTime(); byte b; int i;
/*     */       Future[] arrayOfFuture;
/* 248 */       for (i = (arrayOfFuture = repairTimerDataFutures).length, b = 0; b < i; ) { Future<RuntimeStats> repairTimerDataFuture = arrayOfFuture[b];
/*     */         try {
/* 250 */           RuntimeStats runtimeStats = (RuntimeStats)repairTimerDataFuture.get();
/* 251 */           RepairTimerInfo repairTimerInfo = runtimeStats.repairTimerInfo;
/*     */           
/* 253 */           if (repairTimerInfo == null)
/* 254 */           { _logger.warn("No runtime stats received for host!");
/*     */             
/*     */              }
/*     */           
/* 258 */           else if (repairTimerInfo.objectCount <= 0)
/* 259 */           { _logger.debug("No objects scheduled for resyncing on the host");
/*     */              }
/*     */           
/*     */           else
/*     */           
/*     */           { 
/* 265 */             if (repairTimerInfo.maxTimeToRepair >= 0) {
/* 266 */               maxTimer = Math.max(maxTimer, todayInMilliseconds + (repairTimerInfo.maxTimeToRepair * 1000));
/*     */             }
/* 268 */             if (repairTimerInfo.minTimeToRepair >= 0) {
/* 269 */               minTimer = Math.min(minTimer, todayInMilliseconds + (repairTimerInfo.minTimeToRepair * 1000));
/*     */             }
/*     */             
/* 272 */             objectsCount += repairTimerInfo.objectCount; } 
/* 273 */         } catch (Exception ex) {
/* 274 */           _logger.error("Cannot retrieve Repair Timer Data: ", ex);
/*     */         } 
/*     */         b++; }
/*     */       
/* 278 */       repairTimerData.maxTimer = maxTimer;
/* 279 */       repairTimerData.minTimer = minTimer;
/* 280 */       repairTimerData.objectsCount = objectsCount;
/*     */     } 
/*     */     
/* 283 */     return repairTimerData;
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
/*     */   public Future<RuntimeStats>[] getRepairTimerDataFutures(ManagedObjectReference clusterRef, Measure measure) throws Exception {
/* 297 */     ManagedObjectReference[] hosts = null;
/*     */     try {
/* 299 */       hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host");
/* 300 */     } catch (Exception ex) {
/*     */       
/* 302 */       _logger.warn("Cannot retrieve clusterRef.host", ex);
/*     */     } 
/*     */ 
/*     */     
/* 306 */     if (ArrayUtils.isEmpty((Object[])hosts)) {
/* 307 */       return (Future<RuntimeStats>[])new Future[0];
/*     */     }
/*     */     
/* 310 */     List<Future> futures = new ArrayList<>(hosts.length); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 311 */     for (i = (arrayOfManagedObjectReference1 = hosts).length, b = 0; b < i; ) { ManagedObjectReference hostRef = arrayOfManagedObjectReference1[b];
/* 312 */       if (VsanCapabilityUtils.isRepairTimerInResyncStatsSupported(hostRef)) {
/*     */ 
/*     */ 
/*     */         
/* 316 */         VsanSystemEx vsanSystemEx = VsanProviderUtils.getVsanSystemEx(hostRef);
/* 317 */         Future<RuntimeStats> future = measure.newFuture("vsanSystemEx.getRuntimeStats");
/* 318 */         vsanSystemEx.getRuntimeStats(RUNTIME_STATS, future);
/* 319 */         futures.add(future);
/*     */       }  b++; }
/*     */     
/* 322 */     if (futures.isEmpty())
/*     */     {
/* 324 */       return null;
/*     */     }
/*     */     
/* 327 */     return futures.<Future<RuntimeStats>>toArray((Future<RuntimeStats>[])new Future[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private Future<ConfigInfoEx> getConfigInfoExFuture(ManagedObjectReference clusterRef, Measure measure) throws Exception {
/* 332 */     if (VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/* 333 */       Future<ConfigInfoEx> result = measure.newFuture("VsanVcClusterConfigSystem.getConfigInfoEx");
/* 334 */       VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 335 */       vsanConfigSystem.getConfigInfoEx(clusterRef, result);
/* 336 */       return result;
/*     */     } 
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, VsanObjectHealthData> getIscsiExtraObjectsHealth(ManagedObjectReference clusterRef, List<VsanObjectIdentity> iscsiIdentityData, Map<String, VsanObject> iscsiObjects) {
/* 346 */     Map<String, VsanObjectHealthData> extraHealthData = new HashMap<>();
/* 347 */     if (iscsiObjects != null) {
/* 348 */       Set<String> iscsiExtraIdentities = new HashSet<>();
/*     */       
/* 350 */       for (String iscsiObjectUuid : iscsiObjects.keySet()) {
/* 351 */         boolean matchFound = false;
/* 352 */         for (VsanObjectIdentity iscsiIdentity : iscsiIdentityData) {
/* 353 */           if (iscsiObjectUuid.equals(iscsiIdentity.uuid)) {
/*     */             
/* 355 */             matchFound = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 359 */         if (!matchFound) {
/* 360 */           iscsiExtraIdentities.add(iscsiObjectUuid);
/*     */         }
/*     */       } 
/* 363 */       if (iscsiExtraIdentities.size() > 0)
/*     */       {
/* 365 */         extraHealthData = this.vsanObjectSystemProvider.getVsanHealthData(
/* 366 */             clusterRef, iscsiExtraIdentities);
/*     */       }
/*     */     } 
/* 369 */     return extraHealthData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> getOrphanedSyncObjects(ResyncMonitorData resyncMonitorData, Map<VirtualObjectsFilter, List<VsanObjectIdentity>> vsanObjectIdentitiesData) {
/* 378 */     List<String> orphanedObjects = new ArrayList<>();
/* 379 */     for (String syncObjectUuid : resyncMonitorData.getVsanObjectUuids()) {
/* 380 */       boolean identityFound = false; byte b; int i; VirtualObjectsFilter[] arrayOfVirtualObjectsFilter;
/* 381 */       for (i = (arrayOfVirtualObjectsFilter = VirtualObjectsFilter.values()).length, b = 0; b < i; ) { VirtualObjectsFilter filter = arrayOfVirtualObjectsFilter[b];
/* 382 */         if (filter != null && !CollectionUtils.isEmpty(vsanObjectIdentitiesData.get(filter))) {
/*     */ 
/*     */           
/* 385 */           for (VsanObjectIdentity identity : vsanObjectIdentitiesData.get(filter)) {
/* 386 */             if (syncObjectUuid.equals(identity.uuid)) {
/* 387 */               identityFound = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 391 */           if (identityFound)
/*     */             break; 
/*     */         } 
/*     */         b++; }
/*     */       
/* 396 */       if (!identityFound) {
/* 397 */         orphanedObjects.add(syncObjectUuid);
/*     */       }
/*     */     } 
/* 400 */     return orphanedObjects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Boolean isVsanEnabledOnCluster(ManagedObjectReference clusterRef) throws Exception {
/* 407 */     return (Boolean)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled", null);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/VsanResyncingComponentsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */