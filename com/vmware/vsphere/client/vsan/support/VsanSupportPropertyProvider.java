/*    */ package com.vmware.vsphere.client.vsan.support;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanAttachToSrOperation;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanSupportPropertyProvider
/*    */ {
/* 21 */   private static final Log _logger = LogFactory.getLog(VsanSupportPropertyProvider.class);
/*    */   
/* 23 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanSupportPropertyProvider.class);
/*    */   private final VcClient _vcClient;
/*    */   
/*    */   public VsanSupportPropertyProvider(VcClient vcClient) {
/* 27 */     this._vcClient = vcClient;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public VsanAttachToSrOperation getVsanSRLastOperation(ManagedObjectReference clusterRef) throws Exception {
/* 39 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*    */     
/* 41 */     VsanAttachToSrOperation[] history = null;
/*    */     
/* 43 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("healthSystem.queryAttachToSrHistory"); 
/* 44 */       try { history = healthSystem.queryAttachToSrHistory(clusterRef, Integer.valueOf(10), null); }
/* 45 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */        }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/support/VsanSupportPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */