/*    */ package com.vmware.vsan.client.services.health;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSystem;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*    */ import com.vmware.vsphere.client.vsan.util.Utils;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class PurgeInaccessibleVmSwapObjectsProvider
/*    */ {
/* 22 */   private static final Log logger = LogFactory.getLog(PurgeInaccessibleVmSwapObjectsProvider.class);
/* 23 */   private static final VsanProfiler profiler = new VsanProfiler(PurgeInaccessibleVmSwapObjectsProvider.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public String[] getInaccessibleVmSwapObjects(ManagedObjectReference clusterRef) throws Exception {
/* 30 */     validateIfApiIsSupported(clusterRef);
/*    */     
/* 32 */     VsanObjectSystem vsanObjectSystem = VsanProviderUtils.getVsanObjectSystem(clusterRef);
/* 33 */     Exception exception1 = null, exception2 = null; try {
/*    */     
/*    */     } finally {
/* 36 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   } @TsService
/*    */   public ManagedObjectReference purgeInaccessibleVmSwapObjects(ManagedObjectReference clusterRef, String[] objUuids) throws Exception {
/* 41 */     validateIfApiIsSupported(clusterRef);
/*    */     
/* 43 */     VsanObjectSystem vsanObjectSystem = VsanProviderUtils.getVsanObjectSystem(clusterRef);
/* 44 */     Exception exception1 = null, exception2 = null; try {
/*    */     
/*    */     } finally {
/* 47 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   } private void validateIfApiIsSupported(ManagedObjectReference clusterRef) throws Exception {
/* 51 */     if (!VsanCapabilityUtils.isPurgeInaccessibleVmSwapObjectsSupported(clusterRef)) {
/* 52 */       String message = Utils.getLocalizedString("vsan.common.error.notSupported");
/* 53 */       throw new Exception(message);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/health/PurgeInaccessibleVmSwapObjectsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */