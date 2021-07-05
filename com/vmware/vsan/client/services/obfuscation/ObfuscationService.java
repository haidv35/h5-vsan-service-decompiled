/*    */ package com.vmware.vsan.client.services.obfuscation;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.services.common.CeipService;
/*    */ import com.vmware.vsan.client.services.obfuscation.model.ObfuscationData;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component
/*    */ public class ObfuscationService
/*    */ {
/* 24 */   private static final VsanProfiler _profiler = new VsanProfiler(ObfuscationService.class);
/*    */   
/*    */   @Autowired
/*    */   private CeipService ceipService;
/*    */   
/*    */   @TsService
/*    */   public ObfuscationData getObfuscationData(ManagedObjectReference clusterRef) throws Exception {
/* 31 */     ObfuscationData data = new ObfuscationData();
/*    */     try {
/* 33 */       Exception exception2, exception1 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 39 */     catch (Exception exception) {
/* 40 */       data.obfuscationSupported = false;
/*    */     } 
/*    */     
/* 43 */     data.ceipEnabled = this.ceipService.getCeipServiceEnabled(clusterRef);
/* 44 */     data.clusterVsanConfigUuid = (String)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.uuid", null);
/* 45 */     return data;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/obfuscation/ObfuscationService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */