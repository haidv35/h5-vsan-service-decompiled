/*     */ package com.vmware.vsan.client.services.proactivetests;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterCreateVmHealthTestResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterNetworkLoadTestResult;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.health.ProactiveTestData;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanTestData;
/*     */ import com.vmware.vsphere.client.vsan.health.util.VsanHealthUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ProactiveTestsService
/*     */ {
/*  30 */   private static final Log _logger = LogFactory.getLog(ProactiveTestsService.class);
/*     */   
/*  32 */   private static final VsanProfiler _profiler = new VsanProfiler(ProactiveTestsService.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CREATE_VM_TEST_HELP_ID = "com.vmware.vsan.health.test.createvmtest";
/*     */ 
/*     */   
/*     */   private static final String NETWORKPERFTEST_HELPID = "com.vmware.vsan.health.test.networkperftest";
/*     */ 
/*     */   
/*     */   private static final String UNICASTPERFTEST_HELPID = "com.vmware.vsan.health.test.unicastperftest";
/*     */ 
/*     */   
/*     */   private static final String UNSUPPORT_UNICAST_HOST_VERSION = "6.7.0";
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<ProactiveTestData> getProactiveTestResults(ManagedObjectReference clusterRef) throws Exception {
/*  51 */     List<ProactiveTestData> results = new ArrayList<>();
/*  52 */     ProactiveTestData vmCreationTestResult = getLastVmCreationTestResult(clusterRef);
/*  53 */     if (vmCreationTestResult != null) {
/*  54 */       results.add(vmCreationTestResult);
/*     */     }
/*     */     
/*  57 */     ProactiveTestData networkTestResult = null;
/*  58 */     if (VsanCapabilityUtils.isNetworkPerfTestSupportedOnCluster(clusterRef)) {
/*  59 */       networkTestResult = getLastNetworkTestResult(clusterRef);
/*     */     }
/*  61 */     if (networkTestResult != null) {
/*  62 */       results.add(networkTestResult);
/*     */     }
/*     */     
/*  65 */     return results;
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
/*     */   private ProactiveTestData getLastVmCreationTestResult(ManagedObjectReference clusterRef) throws Exception {
/*  77 */     ProactiveTestData result = null;
/*     */     try {
/*  79 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  89 */     catch (Exception exception) {
/*  90 */       _logger.error("Unable to get VM creation test history results.", exception);
/*  91 */       throw new Exception(Utils.getLocalizedString("vsan.proactive.tests.vmcreation.history.error"));
/*     */     } 
/*     */     
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   private ProactiveTestData getLastNetworkTestResult(ManagedObjectReference clusterRef) throws Exception {
/*  98 */     ProactiveTestData result = null; try {
/*  99 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 109 */     catch (Exception exception) {
/* 110 */       _logger.error("Unable to get network test history results.", exception);
/* 111 */       throw new Exception(Utils.getLocalizedString("vsan.proactive.tests.network.history.error"));
/*     */     } 
/* 113 */     return result;
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
/*     */   public ProactiveTestData getVMCreationTestResult(ManagedObjectReference clusterRef, int timeout) throws Exception {
/* 129 */     ProactiveTestData data = null;
/*     */     try {
/* 131 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 138 */     catch (Exception exception) {
/* 139 */       _logger.error("Unable to get the VM creation test result.", exception);
/* 140 */       throw new Exception(Utils.getLocalizedString("vsan.proactive.tests.vmcreation.test.result.error"));
/*     */     } 
/*     */     
/* 143 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ProactiveTestData getNetworkPerfTestResult(ManagedObjectReference clusterRef, boolean isMulticast) throws Exception {
/* 154 */     ProactiveTestData data = null; try {
/* 155 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 162 */     catch (Exception exception) {
/* 163 */       _logger.error("Unable to get the network load test result.", exception);
/* 164 */       throw new Exception(Utils.getLocalizedString("vsan.proactive.tests.network.test.result.error"));
/*     */     } 
/*     */     
/* 167 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   private ProactiveTestData createVMCreationTestResult(ManagedObjectReference clusterRef, VsanClusterCreateVmHealthTestResult vmHealthTestResult) {
/* 172 */     Set<ManagedObjectReference> moRefs = new HashSet<>();
/* 173 */     VsanHealthUtil.addToTestMoRefsFromBaseResults(vmHealthTestResult.clusterResult.healthTest.testDetails, moRefs, 
/* 174 */         clusterRef.getServerGuid());
/*     */     
/* 176 */     ProactiveTestData data = new ProactiveTestData();
/* 177 */     data.generalData = new VsanTestData(vmHealthTestResult.clusterResult.healthTest, 
/* 178 */         VsanHealthUtil.getNamesForMoRefs(moRefs));
/* 179 */     data.timestamp = Long.valueOf(vmHealthTestResult.clusterResult.timestamp.getTimeInMillis());
/* 180 */     data.perfTestType = ProactiveTestData.PerfTestType.vmCreation;
/* 181 */     data.helpId = "com.vmware.vsan.health.test.createvmtest";
/* 182 */     return data;
/*     */   }
/*     */   
/*     */   private ProactiveTestData createEmptyVMCreationTestResult() {
/* 186 */     ProactiveTestData result = new ProactiveTestData();
/* 187 */     result.generalData = new VsanTestData();
/* 188 */     result.perfTestType = ProactiveTestData.PerfTestType.vmCreation;
/* 189 */     result.helpId = "com.vmware.vsan.health.test.createvmtest";
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   private ProactiveTestData createEmptyNetworkLoadTestResult() {
/* 194 */     ProactiveTestData result = new ProactiveTestData();
/* 195 */     result.generalData = new VsanTestData();
/* 196 */     result.helpId = "com.vmware.vsan.health.test.unicastperftest";
/* 197 */     result.perfTestType = ProactiveTestData.PerfTestType.unicast;
/* 198 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private ProactiveTestData createNetworkLoadTestResult(ManagedObjectReference clusterRef, VsanClusterNetworkLoadTestResult networkLoadTestResult) {
/* 203 */     Set<ManagedObjectReference> moRefs = new HashSet<>();
/* 204 */     VsanHealthUtil.addToTestMoRefsFromBaseResults(networkLoadTestResult.clusterResult.healthTest.testDetails, 
/* 205 */         moRefs, 
/* 206 */         clusterRef.getServerGuid());
/*     */     
/* 208 */     ProactiveTestData data = new ProactiveTestData();
/* 209 */     data.generalData = new VsanTestData(networkLoadTestResult.clusterResult.healthTest, 
/* 210 */         VsanHealthUtil.getNamesForMoRefs(moRefs));
/* 211 */     data.timestamp = Long.valueOf(networkLoadTestResult.clusterResult.timestamp.getTimeInMillis());
/* 212 */     data.perfTestType = ProactiveTestData.PerfTestType.unicast;
/* 213 */     data.helpId = "com.vmware.vsan.health.test.unicastperftest";
/* 214 */     return data;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/proactivetests/ProactiveTestsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */