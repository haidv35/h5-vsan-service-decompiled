/*     */ package com.vmware.vsphere.client.vsan.impl;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.Datastore;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.host.VsanInternalSystem;
/*     */ import com.vmware.vim.binding.vim.host.VsanSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ClusterStatus;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DecommissionMode;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.Version;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*     */ import com.vmware.vsphere.client.vsan.spec.VsanQueryDataEvacuationInfoSpec;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
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
/*     */ 
/*     */ public class VsanPropertyProvider
/*     */ {
/*     */   @Autowired
/*     */   private VcClient _vcClient;
/*     */   private static final String DATASTORE_TYPE_PROPERTY = "summary.type";
/*     */   private static final String VSAN_DATASTORE_TYPE = "vsan";
/*     */   private static final String DATASTORE_PROPERTY = "datastore";
/*     */   private static final String VC_CLUSTERS_PROPERTY = "allClusters";
/*     */   private static final String HOST_VERSION_OP = "5.5.0";
/*  60 */   private static final Log _logger = LogFactory.getLog(VsanPropertyProvider.class);
/*     */   
/*  62 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanPropertyProvider.class);
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference getAnyVsanCluster(ManagedObjectReference vcRef) throws Exception {
/*  67 */     DataServiceResponse propertiesForRelatedObjects = 
/*  68 */       QueryUtil.getPropertiesForRelatedObjects(vcRef, "allClusters", ClusterComputeResource.class.getSimpleName(), 
/*  69 */         new String[] { "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled" });
/*  70 */     PropertyValue[] properties = propertiesForRelatedObjects.getPropertyValues();
/*  71 */     if (ArrayUtils.isEmpty((Object[])properties))
/*     */     {
/*  73 */       return null; }  byte b;
/*     */     int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/*  76 */     for (i = (arrayOfPropertyValue1 = properties).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue1[b];
/*  77 */       Boolean vsanEnabled = (Boolean)propertyValue.value;
/*  78 */       if (vsanEnabled != null && vsanEnabled.booleanValue()) {
/*  79 */         return (ManagedObjectReference)propertyValue.resourceObject;
/*     */       }
/*     */       
/*     */       b++; }
/*     */     
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getIsVsanClusterPartitioned(ManagedObjectReference clusterRef) throws Exception {
/*  92 */     if (clusterRef == null || !isVsanEnabledOnCluster(clusterRef).booleanValue()) {
/*  93 */       _logger.warn("Null cluster reference or vsan not enabled on cluster, returning false.");
/*  94 */       return false;
/*     */     } 
/*  96 */     Collection<VsanHostResourceData> hostsInCluster = getVsanHostResourcesInCluster(clusterRef);
/*     */     
/*  98 */     if (hostsInCluster == null || hostsInCluster.isEmpty()) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     int numberHostsInUse = 0;
/*     */     
/* 104 */     int numberHostsInClusterNode = 0;
/*     */     
/* 106 */     for (VsanHostResourceData hostData : hostsInCluster) {
/* 107 */       if (hostData == null || hostData.isVsanEnabled == null || 
/* 108 */         !hostData.isVsanEnabled.booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 112 */       if (numberHostsInUse == 0 && 
/* 113 */         hostData.vsanHostClusterStatus != null && 
/* 114 */         hostData.vsanHostClusterStatus.memberUuid != null) {
/* 115 */         numberHostsInClusterNode = hostData.vsanHostClusterStatus.memberUuid.length;
/*     */       }
/*     */ 
/*     */       
/* 119 */       numberHostsInUse++;
/*     */     } 
/*     */ 
/*     */     
/* 123 */     numberHostsInUse += getNumberOfWitnessHosts(clusterRef);
/*     */ 
/*     */ 
/*     */     
/* 127 */     return (numberHostsInClusterNode != numberHostsInUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VsanDiskData[] getEligibleDisks(ManagedObjectReference hostRef) throws Exception {
/* 135 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 158 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private DiskResult[] getHostDisksForVsan(VsanSystem vsanSystem) {
/* 165 */     if (vsanSystem == null) {
/* 166 */       return null;
/*     */     }
/* 168 */     DiskResult[] disks = null; try {
/* 169 */       Exception exception2, exception1 = null;
/*     */     }
/* 171 */     catch (Exception exp) {
/* 172 */       _logger.error(exp.getMessage());
/*     */     } 
/* 174 */     return disks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Boolean getIsAllFlashAvailable(ManagedObjectReference hostRef) {
/* 182 */     return Boolean.valueOf(VsanCapabilityUtils.isAllFlashSupportedOnHost(hostRef));
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
/*     */   @TsService
/*     */   public long getVsanDataEvacuationInfo(ManagedObjectReference hostRef, VsanQueryDataEvacuationInfoSpec spec) throws Exception {
/* 197 */     if (spec.disks == null || spec.disks.length == 0) {
/* 198 */       return 0L;
/*     */     }
/*     */     
/* 201 */     Map<String, Object> hostProps = BaseUtils.getProperties(hostRef, 
/* 202 */         new String[] { "config.vsanHostConfig.clusterInfo.nodeUuid" });
/*     */     
/* 204 */     String vsanHostUuid = (String)hostProps.get("config.vsanHostConfig.clusterInfo.nodeUuid");
/*     */     
/* 206 */     if (vsanHostUuid == null) {
/* 207 */       _logger.warn("Failed to retrieve vsanHostUuid.");
/* 208 */       return 0L;
/*     */     } 
/* 210 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 239 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static VsanInternalSystem.DecommissioningBatch createNewDecommissioningBatch(ScsiDisk[] disks, String vsanHostUuid) {
/* 248 */     VsanInternalSystem.DecommissioningBatch batch = new VsanInternalSystem.DecommissioningBatch();
/*     */ 
/*     */     
/* 251 */     VsanInternalSystem.DecomParam[] decomParams = new VsanInternalSystem.DecomParam[disks.length];
/* 252 */     for (int i = 0; i < disks.length; i++) {
/* 253 */       decomParams[i] = new VsanInternalSystem.DecomParam();
/* 254 */       (decomParams[i]).scsiDisk = disks[i];
/* 255 */       (decomParams[i]).nodeUUID = vsanHostUuid;
/*     */     } 
/* 257 */     batch.dp = decomParams;
/* 258 */     batch.mode = new DecommissionMode();
/* 259 */     batch.mode.objectAction = DecommissionMode.ObjectAction.evacuateAllData.toString();
/*     */     
/* 261 */     return batch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Boolean getVsanEnabledOnHost(ManagedObjectReference hostRef) throws Exception {
/* 271 */     if (!getVsanSupportedForHost(hostRef)) {
/* 272 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 275 */     Boolean vsanEnabled = 
/* 276 */       (Boolean)QueryUtil.getProperty(hostRef, "config.vsanHostConfig.enabled", null);
/* 277 */     if (vsanEnabled == null) {
/* 278 */       return Boolean.valueOf(false);
/*     */     }
/* 280 */     return vsanEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getVsanSupportedForHost(ManagedObjectReference hostRef) throws Exception {
/* 289 */     if (!getIsEsxOPorLater(hostRef).booleanValue()) {
/* 290 */       return false;
/*     */     }
/*     */     
/* 293 */     Exception exception1 = null, exception2 = null;
/*     */ 
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
/* 305 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Boolean getIsEsxOPorLater(ManagedObjectReference hostRef) throws Exception {
/* 314 */     String version = (String)QueryUtil.getProperty(hostRef, "config.product.version", null);
/* 315 */     Version esxVersion = new Version(version);
/* 316 */     return (esxVersion.compareTo(new Version("5.5.0")) >= 0) ? Boolean.valueOf(true) : Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getIsWitnessHost(ManagedObjectReference hostRef) throws Exception {
/* 325 */     if (!getVsanSupportedForHost(hostRef)) {
/* 326 */       return false;
/*     */     }
/*     */     
/* 329 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 330 */       VsanProviderUtils.getVcStretchedClusterSystem(hostRef);
/*     */     
/* 332 */     boolean isWitnessHost = false;
/* 333 */     Exception exception1 = null, exception2 = null;
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
/*     */   public Boolean getIsVmOnVsanDatastore(ManagedObjectReference vmRef) throws Exception {
/* 345 */     Constraint vmDatastoresConstraint = 
/* 346 */       QueryUtil.createConstraintForRelationship(
/* 347 */         vmRef, "datastore", 
/* 348 */         Datastore.class.getSimpleName());
/* 349 */     QuerySpec query = QueryUtil.buildQuerySpec(vmDatastoresConstraint, new String[] {
/* 350 */           "summary.type"
/*     */         });
/* 352 */     ResultSet resultSet = null;
/*     */     try {
/* 354 */       resultSet = QueryUtil.getData(query);
/* 355 */     } catch (Exception exp) {
/* 356 */       _logger.error(exp.getMessage());
/* 357 */       throw exp;
/*     */     } 
/*     */     
/* 360 */     if (resultSet != null && resultSet.items != null) {
/* 361 */       byte b; int i; ResultItem[] arrayOfResultItem; for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem item = arrayOfResultItem[b];
/* 362 */         if (item != null && item.properties != null) {
/*     */           byte b1;
/*     */           int j;
/*     */           PropertyValue[] arrayOfPropertyValue;
/* 366 */           for (j = (arrayOfPropertyValue = item.properties).length, b1 = 0; b1 < j; ) { PropertyValue pv = arrayOfPropertyValue[b1];
/* 367 */             if ("summary.type".equals(pv.propertyName) && 
/* 368 */               "vsan".equalsIgnoreCase((String)pv.value))
/* 369 */               return Boolean.valueOf(true);  b1++; }
/*     */         
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 375 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public int getNumberOfWitnessHosts(ManagedObjectReference clusterRef) {
/* 383 */     if (clusterRef == null) {
/* 384 */       _logger.error("Null cluster reference encountered.");
/* 385 */       return 0;
/*     */     } 
/*     */     
/* 388 */     int witnessHosts = 0; try {
/* 389 */       Exception exception2, exception1 = null;
/*     */ 
/*     */     
/*     */     }
/* 393 */     catch (Exception ex) {
/* 394 */       _logger.error("Could not retrieve witness hosts for cluster " + 
/* 395 */           ex.getMessage());
/*     */     } 
/* 397 */     return witnessHosts;
/*     */   }
/*     */ 
/*     */   
/*     */   private Collection<VsanHostResourceData> getVsanHostResourcesInCluster(ManagedObjectReference clusterRef) {
/* 402 */     if (clusterRef == null) {
/* 403 */       _logger.error("Null cluster reference encountered.");
/* 404 */       return null;
/*     */     } 
/*     */     
/* 407 */     PropertyValue[] hostProperties = null;
/*     */     
/*     */     try {
/* 410 */       hostProperties = QueryUtil.getPropertiesForRelatedObjects(
/* 411 */           clusterRef, 
/* 412 */           "host", 
/* 413 */           HostSystem.class.getSimpleName(), 
/* 414 */           new String[] {
/* 415 */             "vsanHostClusterStatus", 
/* 416 */             "config.vsanHostConfig.enabled" }).getPropertyValues();
/*     */     }
/* 418 */     catch (Exception ex) {
/* 419 */       _logger.error("Failed to get hosts in cluster!", ex);
/*     */     } 
/*     */     
/* 422 */     if (hostProperties == null) {
/* 423 */       return null;
/*     */     }
/*     */     
/* 426 */     Map<ManagedObjectReference, VsanHostResourceData> hosts = 
/* 427 */       new HashMap<>(); byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/* 429 */     for (i = (arrayOfPropertyValue1 = hostProperties).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 430 */       ManagedObjectReference hostRef = 
/* 431 */         (ManagedObjectReference)propValue.resourceObject;
/* 432 */       VsanHostResourceData hostData = hosts.get(hostRef);
/* 433 */       if (hostData == null) {
/* 434 */         hostData = new VsanHostResourceData(null);
/* 435 */         hosts.put(hostRef, hostData);
/*     */       } 
/*     */       
/* 438 */       if ("vsanHostClusterStatus".equals(
/* 439 */           propValue.propertyName)) {
/* 440 */         hostData.vsanHostClusterStatus = (ClusterStatus)propValue.value;
/* 441 */       } else if ("config.vsanHostConfig.enabled".equals(propValue.propertyName)) {
/* 442 */         Boolean vsanEnabled = (Boolean)propValue.value;
/* 443 */         hostData.isVsanEnabled = Boolean.valueOf((vsanEnabled == null) ? false : vsanEnabled.booleanValue());
/*     */       } 
/*     */       b++; }
/*     */     
/* 447 */     return hosts.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Boolean isVsanEnabledOnCluster(ManagedObjectReference clusterRef) throws Exception {
/* 455 */     return (Boolean)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled", null);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public Boolean isVsanNonEmptyCluster(ManagedObjectReference clusterRef) throws Exception {
/* 460 */     int hostsNumber = ((Integer)QueryUtil.getProperty(clusterRef, "host._length", null)).intValue();
/* 461 */     return (hostsNumber > 0) ? Boolean.valueOf(true) : Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   private class VsanHostResourceData {
/*     */     public ClusterStatus vsanHostClusterStatus;
/*     */     public Boolean isVsanEnabled;
/*     */     
/*     */     private VsanHostResourceData() {}
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/impl/VsanPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */