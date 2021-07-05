/*    */ package com.vmware.vsan.client.services.encryption;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*    */ import com.vmware.vsphere.client.vsan.data.EncryptionRekeySpec;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class EncryptionMutationProvider
/*    */ {
/* 17 */   private static final VsanProfiler _profiler = new VsanProfiler(EncryptionMutationProvider.class);
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public ManagedObjectReference rekeyEncryptedCluster(ManagedObjectReference clusterRef, EncryptionRekeySpec spec) throws Exception {
/* 23 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 24 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 30 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/encryption/EncryptionMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */