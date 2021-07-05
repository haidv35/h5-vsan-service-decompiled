/*     */ package com.vmware.vsphere.client.vsan.health;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ManagedEntity;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthConfigs;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultKeyValuePair;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthSummary;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthSystemVersionResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthTest;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterTelemetryProxyConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterVMsHealthSummaryResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanHealthExtMgmtPreCheckResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanHostHealthSystemVersionResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSpaceSummary;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanSpaceUsage;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanPhysicalDiskHealth;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanPhysicalDiskHealthSummary;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanDiskComplianceResourceCheck;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanDiskGroupComplianceResourceCheck;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanFaultDomainComplianceResourceCheck;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanHostComplianceResourceCheck;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ 
/*     */ public class VsanHealthPropertyProvider {
/*  34 */   private static final Log _logger = LogFactory.getLog(VsanHealthPropertyProvider.class);
/*     */   
/*  36 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanHealthPropertyProvider.class);
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
/*     */   public VsanHealthServicePreCheckResult getVsanHealthServiceEnablePreCheckResult(ManagedObjectReference clusterRef) throws Exception {
/*  48 */     return getPreFlightCheckResult(clusterRef, true);
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
/*     */   @TsService
/*     */   public VsanHealthServicePreCheckResult getVsanHealthServiceDisablePreCheckResult(ManagedObjectReference clusterRef) throws Exception {
/*  61 */     return getPreFlightCheckResult(clusterRef, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean isSilentCheckSupported(ManagedObjectReference clusterRef) throws Exception {
/*  69 */     return VsanCapabilityUtils.isSilentCheckSupportedOnVc(clusterRef);
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
/*     */   public List<String> getVsanSilentChecks(ManagedObjectReference clusterRef) throws Exception {
/*  81 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*  82 */     List<String> silentChecks = new ArrayList<>();
/*  83 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public VsanHealthServiceStatus getVsanHealthServiceStatus(ManagedObjectReference clusterRef) throws Exception {
/*     */     VsanHealthServiceStatus vhss;
/*     */     VsanClusterHealthSystemVersionResult versionResult;
/* 102 */     VsanVcClusterHealthSystem healthSystem = 
/* 103 */       VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/* 104 */     String clusterStatus = null;
/* 105 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("mgmtSystem.getClusterStatus"); 
/* 106 */       try { clusterStatus = healthSystem.getClusterStatus(clusterRef, null); }
/* 107 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     Exception exception3 = null, exception4 = null; try { VsanProfiler.Point point = _profiler.point("mgmtSystem.queryVerifyClusterHealthSystemVersions"); 
/* 115 */       try { versionResult = healthSystem.queryVerifyClusterHealthSystemVersions(clusterRef); }
/* 116 */       finally { if (point != null) point.close();  }  } finally { exception4 = null; if (exception3 == null) { exception3 = exception4; } else if (exception3 != exception4) { exception3.addSuppressed(exception4); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     throw new Exception(
/* 123 */         Utils.getLocalizedString("vsan.health.status.error"));
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
/*     */   private void addVersionCheckResult(VsanHealthServiceStatus vhss, VsanClusterHealthSystemVersionResult versionResult) {
/* 136 */     if (versionResult != null) {
/* 137 */       vhss.versionCheck = new VsanHealthServiceVersionCheck();
/* 138 */       vhss.versionCheck.latestVersiobNumber = versionResult.vcVersion;
/*     */       
/* 140 */       String hostVersionStr = "";
/* 141 */       boolean upgradePossible = (versionResult.upgradePossible == null) ? false : versionResult.upgradePossible.booleanValue();
/* 142 */       if (versionResult.hostResults != null) {
/* 143 */         vhss.versionCheck.canBeUpgraded = upgradePossible; byte b; int i; VsanHostHealthSystemVersionResult[] arrayOfVsanHostHealthSystemVersionResult;
/* 144 */         for (i = (arrayOfVsanHostHealthSystemVersionResult = versionResult.hostResults).length, b = 0; b < i; ) { VsanHostHealthSystemVersionResult hostVersion = arrayOfVsanHostHealthSystemVersionResult[b];
/* 145 */           if (hostVersion != null && hostVersion.version != null && !hostVersion.version.equals("0.0")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 151 */             String currentHostVersionStr = hostVersion.version;
/* 152 */             if (!hostVersionStr.isEmpty() && 
/* 153 */               !hostVersionStr.equals(currentHostVersionStr)) {
/* 154 */               hostVersionStr = Utils.getLocalizedString("vsan.health.service.version.mixed");
/*     */             } else {
/* 156 */               hostVersionStr = currentHostVersionStr;
/*     */             } 
/*     */           } 
/*     */           b++; }
/*     */       
/*     */       } 
/* 162 */       if (hostVersionStr.isEmpty()) {
/* 163 */         vhss.versionCheck.canBeUpgraded = upgradePossible;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 170 */       if (versionResult.hostResults == null || versionResult.hostResults.length == 0) {
/* 171 */         vhss.versionCheck.canBeUpgraded = false;
/*     */       }
/*     */       
/* 174 */       vhss.versionCheck.versionNumber = hostVersionStr;
/*     */     } 
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
/*     */   @TsService
/*     */   public VsanHealthData getVsanHealthFromCache(ManagedObjectReference clusterRef, Boolean isDefaultPerspective, boolean isVsphereHealth) throws Exception {
/* 188 */     boolean includeObjUuid = true;
/* 189 */     boolean useCache = true;
/* 190 */     return getClusterHealthSummary(
/* 191 */         clusterRef, includeObjUuid, useCache, isDefaultPerspective, isVsphereHealth);
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
/*     */   @TsService
/*     */   public VsanHealthData getVsanHealth(ManagedObjectReference clusterRef, Boolean isDefaultPerspective, boolean isVsphereHealth) throws Exception {
/* 207 */     boolean includeObjUuid = true;
/* 208 */     boolean useCache = false;
/* 209 */     return getClusterHealthSummary(
/* 210 */         clusterRef, includeObjUuid, useCache, isDefaultPerspective, isVsphereHealth);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void setVsanSilentChecks(ManagedObjectReference clusterRef, List<String> addedSilenceChecks, List<String> removedSilenceChecks) throws Exception {
/* 217 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public VsanHealthData getVsanHealthSummaryFromCache(ManagedObjectReference clusterRef) throws Exception {
/* 236 */     boolean includeObjUuid = false;
/* 237 */     boolean useCache = true;
/* 238 */     return getClusterHealthSummary(clusterRef, includeObjUuid, useCache, 
/* 239 */         Boolean.valueOf(true), false);
/*     */   }
/*     */   
/*     */   private VsanHealthData getClusterHealthSummary(ManagedObjectReference clusterRef, boolean includeObjUuids, boolean fetchFromCache, Boolean isDefaultPerspective, boolean isVsphereHealth) throws Exception {
/*     */     String perspective;
/*     */     boolean includeDataProtection;
/*     */     Set<ManagedObjectReference> allMoRefs;
/* 246 */     String[] requiredFields = { "groups", "timestamp" };
/*     */     
/* 248 */     VsanVcClusterHealthSystem healthSystem = null;
/*     */ 
/*     */ 
/*     */     
/* 252 */     if (isVsphereHealth) {
/* 253 */       healthSystem = VsphereHealthProviderUtils.getVsphereHealthSystem(clusterRef);
/* 254 */       includeDataProtection = false;
/* 255 */       perspective = null;
/*     */     } else {
/*     */       
/* 258 */       healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*     */       
/* 260 */       includeDataProtection = VsanCapabilityUtils.isLocalDataProtectionSupportedOnVc(clusterRef);
/*     */       
/* 262 */       perspective = isDefaultPerspective.booleanValue() ? 
/* 263 */         VsanHealthPerspective.defaultView.toString() : 
/* 264 */         VsanHealthPerspective.deployAssist.toString();
/*     */     } 
/*     */     
/* 267 */     VsanClusterHealthSummary healthSummary = null;
/* 268 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("healthSystem.queryClusterHealthSummary");
/*     */ 
/*     */ 
/*     */       
/* 272 */       try { healthSummary = healthSystem.queryClusterHealthSummary(clusterRef, null, null, 
/* 273 */             Boolean.valueOf(includeObjUuids), requiredFields, Boolean.valueOf(fetchFromCache), perspective, 
/* 274 */             Boolean.valueOf(includeDataProtection), null); }
/* 275 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/* 288 */     VsanHealthData healthData = getVsanHealthData(healthSummary, VsanHealthUtil.getNamesForMoRefs(allMoRefs));
/* 289 */     healthData.timeStamp = healthSummary.getTimestamp();
/* 290 */     return healthData;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public AggregatedVsanHealthSummary getCachedClusterHealthSummary(ManagedObjectReference clusterRef) throws Exception {
/*     */     HardwareOverallHealth physicalDisksHealth;
/* 296 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*     */     
/* 298 */     VsanClusterHealthSummary summary = null;
/* 299 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("mgmtSystem.queryClusterDataEfficiencyCapacityState"); 
/* 300 */       try { boolean includeDataProtection = VsanCapabilityUtils.isLocalDataProtectionSupportedOnVc(clusterRef);
/* 301 */         summary = healthSystem.queryClusterHealthSummary(clusterRef, null, null, Boolean.valueOf(true), null, Boolean.valueOf(true), 
/* 302 */             VsanHealthPerspective.defaultView.toString(), Boolean.valueOf(includeDataProtection), null); }
/* 303 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     int redCount = 0, yellowCount = 0;
/* 310 */     VsanPhysicalDiskHealthSummary[] results = summary.physicalDisksHealth;
/* 311 */     ArrayList<String> statusList = new ArrayList<>(); byte b; int i; VsanPhysicalDiskHealthSummary[] arrayOfVsanPhysicalDiskHealthSummary1;
/* 312 */     for (i = (arrayOfVsanPhysicalDiskHealthSummary1 = results).length, b = 0; b < i; ) { VsanPhysicalDiskHealthSummary pdSummary = arrayOfVsanPhysicalDiskHealthSummary1[b];
/* 313 */       statusList.add(pdSummary.overallHealth);
/* 314 */       total += (pdSummary.disks == null) ? 0 : pdSummary.disks.length;
/* 315 */       if (pdSummary.disks != null) {
/* 316 */         byte b1; int j; VsanPhysicalDiskHealth[] arrayOfVsanPhysicalDiskHealth; for (j = (arrayOfVsanPhysicalDiskHealth = pdSummary.disks).length, b1 = 0; b1 < j; ) { VsanPhysicalDiskHealth h = arrayOfVsanPhysicalDiskHealth[b1];
/* 317 */           if (h.summaryHealth.equals(VsanHealthStatus.red.toString())) {
/* 318 */             redCount++;
/* 319 */           } else if (h.summaryHealth.equals(VsanHealthStatus.yellow.toString())) {
/* 320 */             yellowCount++;
/*     */           }  b1++; }
/*     */       
/*     */       }  b++; }
/*     */     
/* 325 */     physicalDisksHealth.total = Integer.valueOf(total);
/* 326 */     if (statusList.contains(VsanHealthStatus.red.toString())) {
/* 327 */       physicalDisksHealth.overallStatus = VsanHealthStatus.red.toString();
/* 328 */       physicalDisksHealth.issueCount = Integer.valueOf(redCount);
/* 329 */     } else if (statusList.contains(VsanHealthStatus.yellow.toString())) {
/* 330 */       physicalDisksHealth.overallStatus = VsanHealthStatus.yellow.toString();
/* 331 */       physicalDisksHealth.issueCount = Integer.valueOf(yellowCount);
/*     */     } else {
/* 333 */       physicalDisksHealth.overallStatus = VsanHealthStatus.green.toString();
/* 334 */       physicalDisksHealth.issueCount = Integer.valueOf(0);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     HardwareOverallHealth hostsHealth = new HardwareOverallHealth();
/* 341 */     ManagedObjectReference[] hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/* 342 */     hostsHealth.total = Integer.valueOf((hosts != null) ? hosts.length : 0);
/* 343 */     yellowCount = 0;
/* 344 */     redCount = 0;
/* 345 */     if (hosts != null) {
/* 346 */       byte b1; int j; ManagedObjectReference[] arrayOfManagedObjectReference; for (j = (arrayOfManagedObjectReference = hosts).length, b1 = 0; b1 < j; ) { ManagedObjectReference h = arrayOfManagedObjectReference[b1];
/* 347 */         HostSystem.ConnectionState state = (HostSystem.ConnectionState)QueryUtil.getProperty(h, "runtime.connectionState", null);
/* 348 */         ManagedEntity.Status status = (ManagedEntity.Status)QueryUtil.getProperty(h, "overallStatus", null);
/* 349 */         if (HostSystem.ConnectionState.connected.equals(state)) {
/* 350 */           if (ManagedEntity.Status.red.equals(status)) {
/* 351 */             redCount++;
/* 352 */             hostsHealth.overallStatus = VsanHealthStatus.red.toString();
/* 353 */           } else if (ManagedEntity.Status.yellow.equals(status)) {
/* 354 */             yellowCount++;
/* 355 */             if (!VsanHealthStatus.red.toString().equals(hostsHealth.overallStatus)) {
/* 356 */               hostsHealth.overallStatus = VsanHealthStatus.yellow.toString();
/*     */             }
/*     */           } 
/*     */         } else {
/* 360 */           redCount++;
/* 361 */           hostsHealth.overallStatus = VsanHealthStatus.red.toString();
/*     */         }  b1++; }
/*     */     
/*     */     } 
/* 365 */     if (VsanHealthStatus.red.toString().equals(hostsHealth.overallStatus)) {
/* 366 */       hostsHealth.issueCount = Integer.valueOf(redCount);
/* 367 */     } else if (VsanHealthStatus.yellow.toString().equals(hostsHealth.overallStatus)) {
/* 368 */       hostsHealth.issueCount = Integer.valueOf(yellowCount);
/*     */     } else {
/* 370 */       hostsHealth.overallStatus = VsanHealthStatus.green.toString();
/* 371 */       hostsHealth.issueCount = Integer.valueOf(0);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     int total = 0;
/* 378 */     yellowCount = 0;
/* 379 */     redCount = 0;
/* 380 */     VsanClusterVMsHealthOverallResult vmResult = summary.getVmHealth();
/* 381 */     HardwareOverallHealth vmsHealth = new HardwareOverallHealth();
/* 382 */     vmsHealth.overallStatus = vmResult.overallHealthState;
/* 383 */     if (vmResult.healthStateList != null) {
/* 384 */       byte b1; int j; VsanClusterVMsHealthSummaryResult[] arrayOfVsanClusterVMsHealthSummaryResult; for (j = (arrayOfVsanClusterVMsHealthSummaryResult = vmResult.healthStateList).length, b1 = 0; b1 < j; ) { VsanClusterVMsHealthSummaryResult r = arrayOfVsanClusterVMsHealthSummaryResult[b1];
/* 385 */         total += r.numVMs;
/* 386 */         if (r.state.equalsIgnoreCase(VsanHealthStatus.red.toString())) {
/* 387 */           redCount += r.numVMs;
/* 388 */         } else if (r.state.equalsIgnoreCase(VsanHealthStatus.yellow.toString())) {
/* 389 */           yellowCount += r.numVMs;
/*     */         }  b1++; }
/*     */     
/*     */     } 
/* 393 */     vmsHealth.total = Integer.valueOf(total);
/* 394 */     if (VsanHealthStatus.red.toString().equalsIgnoreCase(vmsHealth.overallStatus)) {
/* 395 */       vmsHealth.issueCount = Integer.valueOf(redCount);
/* 396 */     } else if (VsanHealthStatus.yellow.toString().equalsIgnoreCase(vmsHealth.overallStatus)) {
/* 397 */       vmsHealth.issueCount = Integer.valueOf(yellowCount);
/*     */     } 
/*     */     
/* 400 */     AggregatedVsanHealthSummary aggregatedSummary = new AggregatedVsanHealthSummary();
/* 401 */     aggregatedSummary.hostSummary = hostsHealth;
/* 402 */     aggregatedSummary.physicalDiskSummary = physicalDisksHealth;
/* 403 */     aggregatedSummary.networkIssueDetected = summary.networkHealth.issueFound;
/* 404 */     aggregatedSummary.vmSummary = vmsHealth;
/* 405 */     return aggregatedSummary;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getConfigAssistSupported(ManagedObjectReference clusterRef) throws Exception {
/* 411 */     return VsanCapabilityUtils.isConfigAssistSupportedOnVc(clusterRef);
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getUpdatesMgmtSupported(ManagedObjectReference clusterRef) throws Exception {
/* 417 */     return VsanCapabilityUtils.isUpdatesMgmtSupportedOnVc(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsSupportWhatIfCompliance(ManagedObjectReference clusterRef) {
/* 422 */     return VsanCapabilityUtils.isWhatIfComplianceSupported(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsCloudHealthSupported(ManagedObjectReference clusterRef) {
/* 427 */     return VsanCapabilityUtils.isCloudHealthSupportedOnVc(clusterRef);
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
/*     */   @TsService
/*     */   public ManagedObjectReference getCloudHealthCheckResult(ManagedObjectReference clusterRef) throws Exception {
/* 440 */     ManagedObjectReference taskMoRef = null;
/* 441 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ExternalProxySettingsConfig getExternalProxySettings(ManagedObjectReference clusterRef) throws Exception {
/* 452 */     if (clusterRef == null) {
/* 453 */       String errorMessage = Utils.getLocalizedString("vsan.internet.error.nocluster");
/* 454 */       throw new Exception(errorMessage);
/*     */     } 
/*     */     
/* 457 */     VsanVcClusterHealthSystem healthSystem = 
/* 458 */       VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/* 459 */     VsanClusterHealthConfigs configs = null; try {
/* 460 */       Exception exception2, exception1 = null;
/*     */     }
/* 462 */     catch (Exception ex) {
/* 463 */       _logger.error(ex);
/* 464 */       String errorMessage = Utils.getLocalizedString("vsan.internet.error.remotecall");
/* 465 */       throw new Exception(errorMessage);
/*     */     } 
/*     */     
/* 468 */     VsanClusterTelemetryProxyConfig proxy = null;
/* 469 */     if (configs == null) {
/* 470 */       return null;
/*     */     }
/* 472 */     proxy = configs.getVsanTelemetryProxy();
/*     */     
/* 474 */     ExternalProxySettingsConfig result = new ExternalProxySettingsConfig();
/*     */     
/* 476 */     VsanClusterHealthResultKeyValuePair[] pairs = configs.getConfigs();
/* 477 */     if (pairs != null && pairs.length > 0) {
/* 478 */       byte b; int i; VsanClusterHealthResultKeyValuePair[] arrayOfVsanClusterHealthResultKeyValuePair; for (i = (arrayOfVsanClusterHealthResultKeyValuePair = configs.getConfigs()).length, b = 0; b < i; ) { VsanClusterHealthResultKeyValuePair pair = arrayOfVsanClusterHealthResultKeyValuePair[b];
/* 479 */         if ("enableInternetAccess".equalsIgnoreCase(pair.getKey()))
/* 480 */           result.enableInternetAccess = Boolean.valueOf("true".equalsIgnoreCase(pair.getValue())); 
/*     */         b++; }
/*     */     
/*     */     } 
/* 484 */     if (proxy != null && proxy.host != null && !proxy.host.isEmpty()) {
/*     */ 
/*     */       
/* 487 */       result.isAutoDiscovered = 
/* 488 */         (proxy.autoDiscovered != null) ? proxy.autoDiscovered
/* 489 */         .booleanValue() : false;
/* 490 */       result.hostName = proxy.getHost();
/* 491 */       result.port = proxy.getPort();
/* 492 */       result.userName = proxy.getUser();
/* 493 */       result.password = proxy.getPassword();
/*     */     } 
/* 495 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getVsanSystemOverhead(VsanObjectSpaceSummary[] objectsByType) {
/* 505 */     String[] requiredKeys = { "fileSystemOverhead", "checksumOverhead", "dedupOverhead" };
/* 506 */     if (objectsByType != null && objectsByType.length > 0) {
/* 507 */       long vsanSystemOverhead = 0L;
/* 508 */       List<String> keyList = Arrays.asList(requiredKeys); byte b; int i; VsanObjectSpaceSummary[] arrayOfVsanObjectSpaceSummary;
/* 509 */       for (i = (arrayOfVsanObjectSpaceSummary = objectsByType).length, b = 0; b < i; ) { VsanObjectSpaceSummary object = arrayOfVsanObjectSpaceSummary[b];
/* 510 */         if (keyList.contains(object.objType))
/* 511 */           vsanSystemOverhead += (object.overheadB == null) ? 0L : object.overheadB.longValue(); 
/*     */         b++; }
/*     */       
/* 514 */       return vsanSystemOverhead;
/*     */     } 
/* 516 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VsanWhatIfCapacityModel getWhatIfCapacity(ManagedObjectReference objectRef, String profileId) throws Exception {
/* 525 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/* 526 */     Validate.notNull(clusterRef);
/*     */     
/* 528 */     VsanWhatIfCapacityModel whatIfCapacity = new VsanWhatIfCapacityModel();
/* 529 */     whatIfCapacity.isWhatIfCapacitySupported = VsanCapabilityUtils.isWhatIfCapacitySupported(clusterRef);
/* 530 */     if (whatIfCapacity.isWhatIfCapacitySupported) {
/* 531 */       DefinedProfileSpec profileSpec = new DefinedProfileSpec();
/* 532 */       profileSpec.profileId = profileId;
/* 533 */       ProfileSpec[] profiles = { (ProfileSpec)profileSpec };
/*     */       
/* 535 */       VsanSpaceUsage spaceUsage = null;
/* 536 */       VsanSpaceReportSystem capacitySystem = VsanProviderUtils.getVsanSpaceReportSystem(clusterRef); try {
/* 537 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 542 */       catch (Exception e) {
/* 543 */         _logger.error("Unable to get what-if capacity: " + e);
/*     */       } 
/*     */     } 
/*     */     
/* 547 */     return whatIfCapacity;
/*     */   }
/*     */   @TsService
/*     */   public VsanSpaceUsageDataModel getSpaceUsage(ManagedObjectReference objectRef) throws Exception {
/*     */     VsanSpaceUsageDataModel usage;
/* 552 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/* 553 */     Validate.notNull(clusterRef);
/*     */     
/* 555 */     VsanSpaceReportSystem capacitySystem = VsanProviderUtils.getVsanSpaceReportSystem(clusterRef);
/* 556 */     VsanSpaceUsage spaceUsage = null;
/* 557 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("capacitySystem.querySpaceUsage"); 
/* 558 */       try { spaceUsage = capacitySystem.querySpaceUsage(clusterRef, null, Boolean.valueOf(false)); }
/* 559 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 567 */     VsanObjectSpaceSummary summary = spaceUsage.spaceOverview;
/* 568 */     CapacityOverviewData data = new CapacityOverviewData();
/* 569 */     usage.overview = data;
/*     */     
/* 571 */     data.totalSpace = usage.totalCapacityB = spaceUsage.getTotalCapacityB();
/*     */     
/* 573 */     data.provisionedSpace = toLong(summary.provisionCapacityB);
/* 574 */     data.usedSpace = toLong(summary.usedB);
/* 575 */     data.physicalUsedSpace = toLong(summary.physicalUsedB);
/* 576 */     data.reservedSpace = toLong(summary.reservedCapacityB);
/* 577 */     data.vsanOverheadSpace = (spaceUsage.spaceDetail != null) ? 
/* 578 */       getVsanSystemOverhead(spaceUsage.spaceDetail.spaceUsageByObjectType) : 0L;
/* 579 */     data.overReservedSpace = toLong(summary.overReservedB);
/* 580 */     data.freeSpace = toLong(spaceUsage.freeCapacityB);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 592 */     data.vsanDpOverheadSpace = 0L;
/* 593 */     data.vsanDpFragmentationOverheadSpace = 0L;
/*     */     
/* 595 */     VsanObjectSpaceSummary[] objs = (spaceUsage.spaceDetail == null) ? 
/* 596 */       null : spaceUsage.spaceDetail.spaceUsageByObjectType;
/* 597 */     usage.spaceDetail = new ArrayList<>();
/* 598 */     if (objs != null && objs.length > 0) {
/* 599 */       byte b; int i; VsanObjectSpaceSummary[] arrayOfVsanObjectSpaceSummary; for (i = (arrayOfVsanObjectSpaceSummary = objs).length, b = 0; b < i; ) { VsanObjectSpaceSummary object = arrayOfVsanObjectSpaceSummary[b];
/* 600 */         VsanObjectSpaceSummaryDataModel detailObject = new VsanObjectSpaceSummaryDataModel();
/* 601 */         usage.spaceDetail.add(detailObject);
/* 602 */         detailObject.objectType = object.objType;
/* 603 */         detailObject.overheadSpace = object.overheadB.longValue();
/* 604 */         detailObject.tempOverheadSpace = object.temporaryOverheadB.longValue();
/* 605 */         detailObject.physicalUsedSpace = object.usedB.longValue();
/* 606 */         detailObject.primaryCapacitySpace = object.primaryCapacityB.longValue();
/* 607 */         detailObject.reservedSpace = object.reservedCapacityB.longValue(); b++; }
/*     */     
/*     */     } 
/* 610 */     return usage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHealthData getVsanHealthData(VsanClusterHealthSummary healthSummary, Map<ManagedObjectReference, String> moRefToNameMap) {
/* 617 */     VsanHealthData healthData = new VsanHealthData();
/*     */     
/* 619 */     healthData.description = healthSummary.overallHealthDescription;
/*     */     
/* 621 */     healthData.status = VsanHealthStatus.valueOf(healthSummary.overallHealth);
/* 622 */     healthData.testsData = new ArrayList<>(); byte b; int i;
/*     */     VsanClusterHealthGroup[] arrayOfVsanClusterHealthGroup;
/* 624 */     for (i = (arrayOfVsanClusterHealthGroup = healthSummary.groups).length, b = 0; b < i; ) { VsanClusterHealthGroup healthGroup = arrayOfVsanClusterHealthGroup[b];
/* 625 */       healthData.testsData.add(new VsanTestData(healthGroup, moRefToNameMap));
/*     */       b++; }
/*     */     
/* 628 */     return healthData;
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
/*     */   private VsanHealthServicePreCheckResult getPreFlightCheckResult(ManagedObjectReference clusterRef, boolean enable) throws Exception {
/* 643 */     if (clusterRef == null) {
/* 644 */       return null;
/*     */     }
/* 646 */     VsanVcClusterHealthSystem healthSystem = 
/* 647 */       VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/* 648 */     VsanHealthExtMgmtPreCheckResult result = null;
/* 649 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanHealthServicePreCheckResult getPreCheckResult(VsanHealthExtMgmtPreCheckResult preCheckResult) {
/* 657 */     if (preCheckResult == null) {
/* 658 */       return null;
/*     */     }
/* 660 */     VsanHealthServicePreCheckResult result = 
/* 661 */       new VsanHealthServicePreCheckResult();
/* 662 */     result.passed = preCheckResult.overallResult;
/* 663 */     if (preCheckResult.vumRegistered != null) {
/* 664 */       result.vumRegistered = preCheckResult.vumRegistered.booleanValue();
/*     */     }
/* 666 */     result.testsData = new ArrayList<>(); byte b; int i; VsanClusterHealthTest[] arrayOfVsanClusterHealthTest;
/* 667 */     for (i = (arrayOfVsanClusterHealthTest = preCheckResult.results).length, b = 0; b < i; ) { VsanClusterHealthTest test = arrayOfVsanClusterHealthTest[b];
/* 668 */       result.testsData.add(new VsanTestData(test, null)); b++; }
/*     */     
/* 670 */     return result;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VsanDataEfficiencyData getClusterDataEfficiency(ManagedObjectReference objectRef) throws Exception {
/* 675 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/* 676 */     Validate.notNull(clusterRef);
/*     */     
/* 678 */     boolean dedupEnabled = ((Boolean)QueryUtil.getProperty(clusterRef, "dataEfficiencyStatus", null)).booleanValue();
/* 679 */     VsanDataEfficiencyData efficiency = new VsanDataEfficiencyData();
/* 680 */     efficiency.dedupEnabled = dedupEnabled;
/* 681 */     if (dedupEnabled) {
/* 682 */       VsanVcDiskManagementSystem mgmtSystem = VsanProviderUtils.getVcDiskManagementSystem(clusterRef);
/* 683 */       DataEfficiencyCapacityState state = null;
/* 684 */       Exception exception1 = null, exception2 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 692 */     return efficiency;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference getCompliancePrecheckTask(ManagedObjectReference clusterRef) throws Exception {
/* 698 */     VsanVcPrecheckerSystem precheckerSystem = VsanProviderUtils.getVsanPrecheckerSystem(clusterRef);
/* 699 */     ManagedObjectReference taskMoRef = null;
/* 700 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("precheckerSystem.queryComplianceResourceCheckAsync"); 
/* 701 */       try { taskMoRef = precheckerSystem.queryComplianceResourceCheckAsync(clusterRef); }
/* 702 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ComplianceCheckResultData getCompliancePrecheckResult(ManagedObjectReference clusterRef) throws Exception {
/* 714 */     VsanVcPrecheckerSystem precheckerSystem = 
/* 715 */       VsanProviderUtils.getVsanPrecheckerSystem(clusterRef); try {
/* 716 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 724 */     catch (Exception ex) {
/* 725 */       _logger.error("error in parse the compliance result", ex);
/*     */     } 
/* 727 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckResultObj transformFaultDomainData(VsanFaultDomainComplianceResourceCheck faultDomain) {
/* 732 */     ComplianceCheckResultObj faultDomainDataObj = new ComplianceCheckResultObj();
/* 733 */     faultDomainDataObj.objectType = "FaultDomain";
/* 734 */     faultDomainDataObj.uuid = faultDomain.uuid;
/* 735 */     faultDomainDataObj.name = faultDomain.fdName;
/* 736 */     faultDomainDataObj.isNew = faultDomain.isNew.booleanValue();
/* 737 */     faultDomainDataObj.hasChanged = faultDomainDataObj.isNew;
/*     */     
/* 739 */     if (!ArrayUtils.isEmpty((Object[])faultDomain.hosts)) {
/* 740 */       List<ComplianceCheckResultObj> hostList = new ArrayList<>(); byte b; int i; VsanHostComplianceResourceCheck[] arrayOfVsanHostComplianceResourceCheck;
/* 741 */       for (i = (arrayOfVsanHostComplianceResourceCheck = faultDomain.hosts).length, b = 0; b < i; ) { VsanHostComplianceResourceCheck host = arrayOfVsanHostComplianceResourceCheck[b];
/* 742 */         ComplianceCheckResultObj hostDataObj = transformHostData(host);
/* 743 */         hostList.add(hostDataObj);
/*     */         
/* 745 */         faultDomainDataObj.originalCacheCapacity += hostDataObj.originalCacheCapacity;
/* 746 */         faultDomainDataObj.finalCacheCapacity += hostDataObj.finalCacheCapacity;
/* 747 */         faultDomainDataObj.initCacheCapacity += hostDataObj.initCacheCapacity;
/* 748 */         faultDomainDataObj.finalUsedCacheCapacity += hostDataObj.finalUsedCacheCapacity;
/*     */         
/* 750 */         faultDomainDataObj.finalUsedCapacity += hostDataObj.finalUsedCapacity;
/* 751 */         faultDomainDataObj.initCapacity += hostDataObj.initCapacity;
/* 752 */         faultDomainDataObj.originalCapacity += hostDataObj.originalCapacity;
/* 753 */         faultDomainDataObj.finalCapacity += hostDataObj.finalCapacity;
/*     */         
/* 755 */         if (!faultDomainDataObj.hasChanged && hostDataObj.hasChanged) {
/* 756 */           faultDomainDataObj.hasChanged = hostDataObj.hasChanged;
/*     */         }
/*     */         b++; }
/*     */       
/* 760 */       faultDomainDataObj.childDevices = parseListToArray(hostList);
/*     */     } 
/*     */     
/* 763 */     return faultDomainDataObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckResultObj transformHostData(VsanHostComplianceResourceCheck host) {
/* 768 */     ComplianceCheckResultObj hostDataObj = new ComplianceCheckResultObj();
/* 769 */     hostDataObj.objectType = "host";
/* 770 */     hostDataObj.uuid = host.uuid;
/* 771 */     hostDataObj.name = (host.host == null) ? null : host.host.getValue();
/* 772 */     hostDataObj.isNew = host.isNew.booleanValue();
/* 773 */     hostDataObj.hasChanged = hostDataObj.isNew;
/*     */     
/* 775 */     if (!ArrayUtils.isEmpty((Object[])host.diskGroups)) {
/* 776 */       List<ComplianceCheckResultObj> diskGroupList = new ArrayList<>(); byte b; int i; VsanDiskGroupComplianceResourceCheck[] arrayOfVsanDiskGroupComplianceResourceCheck;
/* 777 */       for (i = (arrayOfVsanDiskGroupComplianceResourceCheck = host.diskGroups).length, b = 0; b < i; ) { VsanDiskGroupComplianceResourceCheck diskgroup = arrayOfVsanDiskGroupComplianceResourceCheck[b];
/* 778 */         ComplianceCheckResultObj diskGroupDataObj = transformDiskGroupData(diskgroup);
/* 779 */         diskGroupList.add(diskGroupDataObj);
/*     */         
/* 781 */         hostDataObj.finalCacheCapacity += diskGroupDataObj.finalCacheCapacity;
/* 782 */         hostDataObj.originalCacheCapacity += diskGroupDataObj.originalCacheCapacity;
/*     */         
/* 784 */         hostDataObj.initCacheCapacity += diskGroupDataObj.initCacheCapacity;
/* 785 */         hostDataObj.finalUsedCacheCapacity += diskGroupDataObj.finalUsedCacheCapacity;
/*     */         
/* 787 */         hostDataObj.finalUsedCapacity += diskGroupDataObj.finalUsedCapacity;
/* 788 */         hostDataObj.initCapacity += diskGroupDataObj.initCapacity;
/*     */         
/* 790 */         hostDataObj.originalCapacity += diskGroupDataObj.originalCapacity;
/* 791 */         hostDataObj.finalCapacity += diskGroupDataObj.finalCapacity;
/*     */         
/* 793 */         if (!hostDataObj.hasChanged && diskGroupDataObj.hasChanged)
/* 794 */           hostDataObj.hasChanged = diskGroupDataObj.hasChanged; 
/*     */         b++; }
/*     */       
/* 797 */       hostDataObj.childDevices = parseListToArray(diskGroupList);
/*     */     } 
/* 799 */     return hostDataObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckResultObj transformDiskData(VsanDiskComplianceResourceCheck disk) {
/* 804 */     ComplianceCheckResultObj diskDataObj = new ComplianceCheckResultObj();
/* 805 */     diskDataObj.objectType = "Disk";
/* 806 */     diskDataObj.uuid = disk.uuid;
/* 807 */     diskDataObj.name = disk.deviceName;
/* 808 */     diskDataObj.initCapacity = disk.initCapacity.longValue();
/* 809 */     diskDataObj.finalUsedCapacity = disk.finalCapacity.longValue();
/* 810 */     diskDataObj.isNew = disk.isNew.booleanValue();
/* 811 */     diskDataObj.hasChanged = !(!diskDataObj.isNew && 
/* 812 */       diskDataObj.initCapacity == diskDataObj.finalUsedCapacity);
/*     */     
/* 814 */     if (!diskDataObj.isNew) {
/* 815 */       diskDataObj.originalCapacity = disk.capacity.longValue();
/*     */     }
/* 817 */     diskDataObj.finalCapacity = disk.capacity.longValue();
/* 818 */     return diskDataObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckResultObj transformDiskGroupData(VsanDiskGroupComplianceResourceCheck diskgroup) {
/* 823 */     ComplianceCheckResultObj diskGroupDataObj = new ComplianceCheckResultObj();
/* 824 */     diskGroupDataObj.objectType = "Diskgroup";
/*     */     
/* 826 */     List<ComplianceCheckResultObj> diskList = new ArrayList<>();
/* 827 */     if (diskgroup.ssd != null) {
/* 828 */       ComplianceCheckResultObj ssdObj = new ComplianceCheckResultObj();
/* 829 */       ssdObj.objectType = "SSD";
/* 830 */       ssdObj.uuid = diskgroup.ssd.uuid;
/* 831 */       ssdObj.name = diskgroup.ssd.deviceName;
/* 832 */       ssdObj.initCacheCapacity = diskgroup.ssd.initCapacity.longValue();
/* 833 */       ssdObj.finalUsedCacheCapacity = diskgroup.ssd.finalCapacity.longValue();
/* 834 */       ssdObj.isNew = diskgroup.ssd.isNew.booleanValue();
/* 835 */       ssdObj.hasChanged = !(!ssdObj.isNew && 
/* 836 */         ssdObj.initCacheCapacity == ssdObj.finalCacheCapacity);
/* 837 */       if (!ssdObj.isNew) {
/* 838 */         ssdObj.originalCacheCapacity = diskgroup.ssd.capacity.longValue();
/*     */       }
/* 840 */       ssdObj.finalCacheCapacity = diskgroup.ssd.capacity.longValue();
/* 841 */       diskList.add(ssdObj);
/*     */       
/* 843 */       diskGroupDataObj.finalUsedCacheCapacity = diskgroup.ssd.finalCapacity.longValue();
/* 844 */       diskGroupDataObj.initCacheCapacity = diskgroup.ssd.initCapacity.longValue();
/* 845 */       diskGroupDataObj.isNew = diskgroup.ssd.isNew.booleanValue();
/* 846 */       diskGroupDataObj.hasChanged = ssdObj.hasChanged;
/* 847 */       if (!diskGroupDataObj.isNew) {
/* 848 */         diskGroupDataObj.originalCacheCapacity = diskgroup.ssd.capacity.longValue();
/*     */       }
/* 850 */       diskGroupDataObj.finalCacheCapacity = diskgroup.ssd.capacity.longValue();
/*     */     } 
/*     */     
/* 853 */     if (!ArrayUtils.isEmpty((Object[])diskgroup.capacityDevices)) {
/* 854 */       byte b; int i; VsanDiskComplianceResourceCheck[] arrayOfVsanDiskComplianceResourceCheck; for (i = (arrayOfVsanDiskComplianceResourceCheck = diskgroup.capacityDevices).length, b = 0; b < i; ) { VsanDiskComplianceResourceCheck disk = arrayOfVsanDiskComplianceResourceCheck[b];
/* 855 */         ComplianceCheckResultObj diskDataObj = transformDiskData(disk);
/* 856 */         diskList.add(diskDataObj);
/*     */         
/* 858 */         diskGroupDataObj.originalCapacity += diskDataObj.originalCapacity;
/* 859 */         diskGroupDataObj.finalCapacity += diskDataObj.finalCapacity;
/*     */         
/* 861 */         diskGroupDataObj.initCapacity += diskDataObj.initCapacity;
/* 862 */         diskGroupDataObj.finalUsedCapacity += diskDataObj.finalUsedCapacity;
/*     */         
/* 864 */         if (!diskGroupDataObj.hasChanged && diskDataObj.hasChanged)
/* 865 */           diskGroupDataObj.hasChanged = diskDataObj.hasChanged; 
/*     */         b++; }
/*     */     
/*     */     } 
/* 869 */     if (diskList.size() > 0) {
/* 870 */       diskGroupDataObj.childDevices = parseListToArray(diskList);
/*     */     }
/* 872 */     return diskGroupDataObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckSummary parseComplianceCheckSummary(List<ComplianceCheckResultObj> fdList) {
/* 877 */     ComplianceCheckSummary resultSummary = new ComplianceCheckSummary();
/* 878 */     if (!CollectionUtils.isEmpty(fdList))
/* 879 */       for (ComplianceCheckResultObj fd : fdList) {
/* 880 */         resultSummary.newFinalTotalCapacity += fd.finalCapacity;
/* 881 */         resultSummary.newFinalUsedCapacity += fd.finalUsedCapacity;
/* 882 */         resultSummary.originalTotalCapacity += fd.originalCapacity;
/* 883 */         resultSummary.originalUsedCapacity += fd.finalUsedCapacity;
/*     */         
/* 885 */         if (fd.isNew) {
/* 886 */           resultSummary.newFaultDomainCount++;
/*     */         } else {
/* 888 */           resultSummary.originalFaultDomainCount++;
/*     */         } 
/*     */         
/* 891 */         if (ArrayUtils.isEmpty((Object[])fd.childDevices))
/* 892 */           return resultSummary;  byte b; int i;
/*     */         ComplianceCheckResultObj[] arrayOfComplianceCheckResultObj;
/* 894 */         for (i = (arrayOfComplianceCheckResultObj = fd.childDevices).length, b = 0; b < i; ) { ComplianceCheckResultObj host = arrayOfComplianceCheckResultObj[b];
/* 895 */           if (host.isNew) {
/* 896 */             resultSummary.newHostCount++;
/*     */           } else {
/* 898 */             resultSummary.originalHostCount++;
/*     */           } 
/*     */           
/* 901 */           if (!ArrayUtils.isEmpty((Object[])host.childDevices)) {
/*     */             byte b1;
/*     */             int j;
/*     */             ComplianceCheckResultObj[] arrayOfComplianceCheckResultObj1;
/* 905 */             for (j = (arrayOfComplianceCheckResultObj1 = host.childDevices).length, b1 = 0; b1 < j; ) { ComplianceCheckResultObj diskgroup = arrayOfComplianceCheckResultObj1[b1];
/* 906 */               if (diskgroup.isNew) {
/* 907 */                 resultSummary.newDiskGroupCount++;
/* 908 */                 resultSummary.newSSDCount++;
/*     */               } else {
/* 910 */                 resultSummary.originalDiskGroupCount++;
/* 911 */                 resultSummary.originalSSDCount++;
/*     */               } 
/*     */               
/* 914 */               if (!ArrayUtils.isEmpty((Object[])diskgroup.childDevices)) {
/* 915 */                 byte b2; int k; ComplianceCheckResultObj[] arrayOfComplianceCheckResultObj2; for (k = (arrayOfComplianceCheckResultObj2 = diskgroup.childDevices).length, b2 = 0; b2 < k; ) { ComplianceCheckResultObj disk = arrayOfComplianceCheckResultObj2[b2];
/* 916 */                   if (!disk.objectType.equals("SSD"))
/*     */                   {
/* 918 */                     if (disk.isNew) {
/* 919 */                       resultSummary.newCapacityDeviceCount++;
/*     */                     } else {
/* 921 */                       resultSummary.originalCapacityDeviceCount++;
/*     */                     }  }  b2++; }
/*     */               
/*     */               }  b1++; }
/*     */           
/*     */           } 
/*     */           b++; }
/*     */       
/*     */       }  
/* 930 */     return resultSummary;
/*     */   }
/*     */ 
/*     */   
/*     */   private ComplianceCheckResultData parseComplianceCheck(VsanFaultDomainComplianceResourceCheck[] faultDomains) {
/* 935 */     if (ArrayUtils.isEmpty((Object[])faultDomains)) {
/* 936 */       return null;
/*     */     }
/*     */     
/* 939 */     List<ComplianceCheckResultObj> fdList = new ArrayList<>(); byte b; int i; VsanFaultDomainComplianceResourceCheck[] arrayOfVsanFaultDomainComplianceResourceCheck;
/* 940 */     for (i = (arrayOfVsanFaultDomainComplianceResourceCheck = faultDomains).length, b = 0; b < i; ) { VsanFaultDomainComplianceResourceCheck faultDomain = arrayOfVsanFaultDomainComplianceResourceCheck[b];
/* 941 */       ComplianceCheckResultObj faultDomainDataObj = transformFaultDomainData(faultDomain);
/* 942 */       fdList.add(faultDomainDataObj);
/*     */       b++; }
/*     */     
/* 945 */     ComplianceCheckResultData result = new ComplianceCheckResultData();
/* 946 */     result.summary = parseComplianceCheckSummary(fdList);
/* 947 */     result.details = parseListToArray(fdList);
/* 948 */     return result;
/*     */   }
/*     */   
/*     */   private ComplianceCheckResultObj[] parseListToArray(List<ComplianceCheckResultObj> dataList) {
/* 952 */     if (CollectionUtils.isEmpty(dataList)) {
/* 953 */       return null;
/*     */     }
/* 955 */     ComplianceCheckResultObj[] arr = new ComplianceCheckResultObj[dataList.size()];
/* 956 */     return dataList.<ComplianceCheckResultObj>toArray(arr);
/*     */   }
/*     */   
/*     */   private static long toLong(Long value) {
/* 960 */     return (value != null) ? value.longValue() : 0L;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanHealthPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */