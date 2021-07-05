/*     */ package com.vmware.vsphere.client.vsan.upgrade;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.VsanUpgradeSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanUpgradeSystemEx;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class VsanUpgradePropertyProvider
/*     */ {
/*     */   private static final String PROP_OBJECT_HEALTH = "objectHealth";
/*  34 */   private static final Log _logger = LogFactory.getLog(VsanUpgradePropertyProvider.class);
/*     */   
/*  36 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanUpgradePropertyProvider.class);
/*     */   
/*     */   private final LegacyVsanObjectVersionProvider _legacyVsanObjectVersionProvider;
/*     */   
/*     */   public VsanUpgradePropertyProvider(LegacyVsanObjectVersionProvider legacyVsanObjectVersionProvider) {
/*  41 */     this._legacyVsanObjectVersionProvider = legacyVsanObjectVersionProvider;
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
/*     */   public VsanUpgradeStatusData getVsanUpgradeStatus(ManagedObjectReference clusterRef) throws Exception {
/*  56 */     VsanUpgradeStatusData result = null; try {
/*  57 */       Exception exception2, exception1 = null;
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
/*  68 */     catch (Exception exception) {
/*  69 */       boolean isUpgradeSystem2Supported = 
/*  70 */         VsanCapabilityUtils.isUpgradeSystem2SupportedOnVc(clusterRef);
/*     */       
/*  72 */       VsanUpgradeSystem upgradeSystem = isUpgradeSystem2Supported ? 
/*  73 */         VsanProviderUtils.getVsanUpgradeSystem(clusterRef) : 
/*  74 */         VsanProviderUtils.getVsanLegacyUpgradeSystem(clusterRef);
/*     */       try {
/*  76 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  84 */       catch (Exception exception1) {
/*  85 */         result = new VsanUpgradeStatusData(Boolean.valueOf(false));
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VsanVersionInfoPerHost[] getVsanHostVersions(ManagedObjectReference clusterRef) throws Exception {
/* 100 */     List<VsanVersionInfoPerHost> result = new ArrayList<>();
/* 101 */     PropertyValue[] hostsVersionValues = QueryUtil.getPropertyForRelatedObjects(
/* 102 */         clusterRef, 
/* 103 */         "allVsanHosts", 
/* 104 */         ClusterComputeResource.class.getSimpleName(), 
/* 105 */         "vsanDiskVersionsData").getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 106 */     for (i = (arrayOfPropertyValue1 = hostsVersionValues).length, b = 0; b < i; ) { PropertyValue value = arrayOfPropertyValue1[b];
/* 107 */       VsanDiskVersionData[] vsanDiskVersionData = (VsanDiskVersionData[])value.value;
/* 108 */       result.add(new VsanVersionInfoPerHost(vsanDiskVersionData)); b++; }
/*     */     
/* 110 */     return result.<VsanVersionInfoPerHost>toArray(new VsanVersionInfoPerHost[result.size()]);
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
/*     */   public VsanUpgradePreflightCheckIssue[] getVsanUpgradePreflightCheckResult(ManagedObjectReference clusterRef) throws Exception {
/* 122 */     boolean isUpgradeSystemExSupported = 
/* 123 */       VsanCapabilityUtils.isUpgradeSystemExSupportedOnVc(clusterRef);
/*     */     
/* 125 */     VsanUpgradeSystem.PreflightCheckIssue[] result = null;
/*     */     
/* 127 */     if (isUpgradeSystemExSupported) {
/* 128 */       Exception exception2; VsanUpgradeSystemEx upgradeSystem = 
/* 129 */         VsanProviderUtils.getVsanUpgradeSystemEx(clusterRef);
/* 130 */       Exception exception1 = null;
/*     */     } else {
/*     */       Exception exception2;
/*     */       
/* 134 */       VsanUpgradeSystem upgradeSystem = 
/* 135 */         VsanProviderUtils.getVsanLegacyUpgradeSystem(clusterRef);
/*     */ 
/*     */       
/* 138 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     return convertIssues(result);
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
/*     */   private static VsanUpgradePreflightCheckIssue[] convertIssues(VsanUpgradeSystem.PreflightCheckIssue[] originalIssues) {
/* 156 */     if (ArrayUtils.isEmpty((Object[])originalIssues)) {
/* 157 */       return new VsanUpgradePreflightCheckIssue[0];
/*     */     }
/*     */     
/* 160 */     List<VsanUpgradePreflightCheckIssue> issues = new ArrayList<>(
/* 161 */         originalIssues.length); byte b; int i; VsanUpgradeSystem.PreflightCheckIssue[] arrayOfPreflightCheckIssue;
/* 162 */     for (i = (arrayOfPreflightCheckIssue = originalIssues).length, b = 0; b < i; ) { VsanUpgradeSystem.PreflightCheckIssue originalIssue = arrayOfPreflightCheckIssue[b];
/* 163 */       VsanUpgradePreflightCheckIssue issue = new VsanUpgradePreflightCheckIssue();
/* 164 */       issue.message = originalIssue.msg;
/*     */       
/* 166 */       if (originalIssue instanceof VsanUpgradeSystem.NotEnoughFreeCapacityIssue) {
/* 167 */         VsanUpgradeSystem.NotEnoughFreeCapacityIssue nefcIssue = (VsanUpgradeSystem.NotEnoughFreeCapacityIssue)originalIssue;
/*     */ 
/*     */ 
/*     */         
/* 171 */         if (nefcIssue.reducedRedundancyUpgradePossible) {
/* 172 */           issue.type = VsanUpgradePreflightCheckIssue.IssueType.WARNING;
/*     */         } else {
/* 174 */           issue.type = VsanUpgradePreflightCheckIssue.IssueType.ERROR;
/*     */         } 
/*     */       } else {
/* 177 */         issue.type = VsanUpgradePreflightCheckIssue.IssueType.ERROR;
/*     */       } 
/*     */       
/* 180 */       issues.add(issue);
/*     */       b++; }
/*     */     
/* 183 */     return issues
/* 184 */       .<VsanUpgradePreflightCheckIssue>toArray(new VsanUpgradePreflightCheckIssue[issues.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public int getLatestVsanVersion(ManagedObjectReference clusterRef) throws Exception {
/* 195 */     int latestVersion = 2;
/*     */     
/* 197 */     boolean isUpgradeSystemExSupported = 
/* 198 */       VsanCapabilityUtils.isUpgradeSystemExSupportedOnVc(clusterRef);
/*     */     
/* 200 */     if (isUpgradeSystemExSupported) {
/* 201 */       try { Exception exception2, exception1 = null;
/*     */         
/*     */          }
/*     */       
/* 205 */       catch (Exception ex)
/* 206 */       { _logger.error("Could not retrieve latest available disk format version", ex); }
/*     */     
/*     */     }
/*     */     
/* 210 */     return latestVersion;
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
/*     */   public boolean getHasOldVsanObject(ManagedObjectReference clusterRef) throws Exception {
/* 223 */     Boolean hasOldVsanObject = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 228 */       Exception exception2, exception1 = null;
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
/* 239 */     catch (Exception ex) {
/* 240 */       _logger.warn("Cannot retrieve object version compliance data from the health system.", ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (hasOldVsanObject == null) {
/* 246 */       hasOldVsanObject = Boolean.valueOf(checkForOldVsanObjects(clusterRef));
/*     */     }
/*     */     
/* 249 */     return hasOldVsanObject.booleanValue();
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
/*     */   private boolean checkForOldVsanObjects(ManagedObjectReference clusterRef) {
/* 261 */     boolean hasOldVsanObject = false;
/*     */     
/*     */     try {
/* 264 */       hasOldVsanObject = 
/* 265 */         this._legacyVsanObjectVersionProvider.getHasOldObject(clusterRef);
/* 266 */     } catch (Exception ex) {
/* 267 */       _logger.warn("Cannot retrieve hasOldVsanObject property from hosts' VsanInternalSystems.", 
/* 268 */           ex);
/*     */     } 
/*     */     
/* 271 */     return hasOldVsanObject;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanUpgradePropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */