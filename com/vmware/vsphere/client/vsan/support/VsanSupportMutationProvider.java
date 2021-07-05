/*    */ package com.vmware.vsphere.client.vsan.support;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
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
/*    */ public class VsanSupportMutationProvider
/*    */ {
/* 19 */   private static final Log _logger = LogFactory.getLog(VsanSupportMutationProvider.class);
/*    */   
/* 21 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanSupportMutationProvider.class);
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
/*    */   @TsService
/*    */   public ManagedObjectReference attachVsanSupportBundleToSr(ManagedObjectReference clusterRef, VsanSRAttachSpec spec) throws Exception {
/* 35 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 42 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/support/VsanSupportMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */