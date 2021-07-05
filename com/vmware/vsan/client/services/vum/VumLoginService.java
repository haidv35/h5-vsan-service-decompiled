/*    */ package com.vmware.vsan.client.services.vum;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class VumLoginService
/*    */ {
/* 16 */   private static final VsanProfiler _profiler = new VsanProfiler(VumLoginService.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public boolean loginToVum(ManagedObjectReference clusterRef, String username, String password) throws Exception {
/* 25 */     Exception exception1 = null, exception2 = null;
/*    */     try {
/*    */     
/*    */     } finally {
/* 29 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/vum/VumLoginService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */