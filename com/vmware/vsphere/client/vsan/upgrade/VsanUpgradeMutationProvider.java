/*    */ package com.vmware.vsphere.client.vsan.upgrade;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanUpgradeMutationProvider
/*    */ {
/*    */   public static final String TASK_TYPE = "Task";
/* 23 */   private static final Log _logger = LogFactory.getLog(VsanUpgradeMutationProvider.class);
/*    */   
/* 25 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanUpgradeMutationProvider.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public ManagedObjectReference performUpgrade(ManagedObjectReference clusterRef, VsanUpgradeSpec spec) throws Exception {
/* 35 */     boolean isUpgradeSystem2Supported = VsanCapabilityUtils.isUpgradeSystem2SupportedOnVc(clusterRef);
/*    */     
/* 37 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 53 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public ManagedObjectReference performUpgradePreflightAsyncCheck(ManagedObjectReference clusterRef, VsanUpgradePrecheckSpec spec) throws Exception {
/* 66 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 79 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   } private static ManagedObjectReference buildTaskMor(String taskId, String vcGuid) {
/* 83 */     ManagedObjectReference task = new ManagedObjectReference(
/* 84 */         "Task", taskId, vcGuid);
/* 85 */     return task;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanUpgradeMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */