/*     */ package com.vmware.vsphere.client.vsan.perf;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.vm.DefinedProfileSpec;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerformanceManager;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfsvcConfig;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerfStatesObjSpec;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerfTimeRangeData;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.lang.Validate;
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
/*     */ public class VsanPerfMutationProvider
/*     */ {
/*  31 */   private static final Log _logger = LogFactory.getLog(VsanPerfMutationProvider.class);
/*     */   
/*  33 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanPerfMutationProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference disablePerfService(ManagedObjectReference clusterRef) throws Exception {
/*  43 */     ManagedObjectReference taskRef = null;
/*  44 */     boolean isPerfSvcAutoConfigSupported = VsanCapabilityUtils.isPerfSvcAutoConfigSupportedOnVc(clusterRef);
/*  45 */     if (isPerfSvcAutoConfigSupported) {
/*  46 */       Exception exception2; VsanVcClusterConfigSystem vsanClusterConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*  47 */       Exception exception1 = null;
/*     */     }
/*     */     else {
/*     */       
/*  51 */       Exception exception2, exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (taskRef != null) {
/*  57 */       return new ManagedObjectReference(taskRef.getType(), taskRef.getValue(), clusterRef.getServerGuid());
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference enablePerfService(PerfStatesObjSpec spec) throws Exception {
/*  69 */     ManagedObjectReference taskRef = null;
/*  70 */     boolean isPerfSvcAutoConfigSupported = VsanCapabilityUtils.isPerfSvcAutoConfigSupportedOnVc(spec.clusterRef);
/*     */     
/*  72 */     if (isPerfSvcAutoConfigSupported) {
/*  73 */       Exception exception2; VsanVcClusterConfigSystem vsanClusterConfigSystem = VsanProviderUtils.getVsanConfigSystem(spec.clusterRef);
/*     */ 
/*     */       
/*  76 */       Exception exception1 = null;
/*     */     } else {
/*     */       Exception exception2;
/*     */ 
/*     */       
/*  81 */       VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(spec.clusterRef);
/*     */       
/*  83 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (taskRef != null) {
/*  91 */       return new ManagedObjectReference(taskRef.getType(), taskRef.getValue(), spec.clusterRef.getServerGuid());
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ReconfigSpec getClusterReconfigSpecForPerfService(ManagedObjectReference clusterRef, String profileId, boolean enabled) throws Exception {
/*  98 */     DefinedProfileSpec definedSpec = new DefinedProfileSpec();
/*  99 */     definedSpec.setProfileId(profileId);
/* 100 */     DefinedProfileSpec profileSpec = StringUtils.isBlank(profileId) ? null : definedSpec;
/*     */     
/* 102 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 103 */     VsanPerfsvcConfig perfConfig = null;
/* 104 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanConfigSystem.getConfigInfoEx"); 
/* 105 */       try { perfConfig = (vsanConfigSystem.getConfigInfoEx(clusterRef)).perfsvcConfig; }
/* 106 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     perfConfig.enabled = enabled;
/* 113 */     ReconfigSpec reconfigSpec = new ReconfigSpec();
/* 114 */     reconfigSpec.perfsvcConfig = perfConfig;
/*     */     
/* 116 */     return reconfigSpec;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference editPerfConfiguration(PerfStatesObjSpec spec) throws Exception {
/* 122 */     Validate.notNull(spec.clusterRef);
/*     */     
/* 124 */     ManagedObjectReference taskRef = null;
/* 125 */     boolean isPerfSvcAutoConfigSupported = VsanCapabilityUtils.isPerfSvcAutoConfigSupportedOnVc(spec.clusterRef);
/*     */     
/* 127 */     if (isPerfSvcAutoConfigSupported) {
/* 128 */       Exception exception2; VsanVcClusterConfigSystem vsanClusterConfigSystem = VsanProviderUtils.getVsanConfigSystem(spec.clusterRef);
/* 129 */       Exception exception1 = null;
/*     */     } else {
/*     */       Exception exception2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 139 */       VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(spec.clusterRef);
/* 140 */       DefinedProfileSpec definedSpec = new DefinedProfileSpec();
/* 141 */       definedSpec.setProfileId(spec.profileId);
/* 142 */       DefinedProfileSpec profileSpec = (spec.profileId == null) ? null : definedSpec;
/* 143 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     if (taskRef != null) {
/* 148 */       return new ManagedObjectReference(taskRef.getType(), taskRef.getValue(), spec.clusterRef.getServerGuid());
/*     */     }
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void toggleVerboseMode(ManagedObjectReference clusterRef, boolean enableVerboseMode) throws Exception {
/* 156 */     VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(clusterRef);
/* 157 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void setTimeRanges(PerfTimeRangeData range) throws Exception {
/* 164 */     Exception exception1 = null, exception2 = null;
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
/*     */   public void deleteTimeRange(ManagedObjectReference clusterRef, PerfTimeRangeData range) throws Exception {
/* 181 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/VsanPerfMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */