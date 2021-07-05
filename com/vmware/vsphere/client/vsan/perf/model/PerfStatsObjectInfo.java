/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.profile.Profile;
/*    */ import com.vmware.vim.binding.vim.KeyValue;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.StorageComplianceResult;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectInformation;
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ @data
/*    */ public class PerfStatsObjectInfo
/*    */ {
/*    */   public boolean serviceEnabled;
/*    */   public String directoryName;
/*    */   public String vsanObjectUuid;
/*    */   public String vsanHealth;
/*    */   public KeyValue[] policyAttributes;
/*    */   public String spbmProfileUuid;
/*    */   public Profile spbmProfile;
/*    */   public StorageComplianceResult spbmComplianceResult;
/*    */   public String spbmProfileGenerationId;
/*    */   public boolean verboseModeEnabled;
/*    */   public boolean networkDiagnosticModeEnabled;
/*    */   
/*    */   public static PerfStatsObjectInfo fromVmodl(VsanObjectInformation vmodl) {
/* 76 */     PerfStatsObjectInfo info = null;
/*    */     
/* 78 */     if (vmodl != null) {
/* 79 */       info = new PerfStatsObjectInfo();
/* 80 */       info.directoryName = vmodl.directoryName;
/* 81 */       info.policyAttributes = vmodl.policyAttributes;
/* 82 */       info.spbmProfileGenerationId = vmodl.spbmProfileGenerationId;
/* 83 */       info.spbmProfileUuid = vmodl.spbmProfileUuid;
/* 84 */       info.vsanHealth = vmodl.vsanHealth;
/* 85 */       info.vsanObjectUuid = vmodl.vsanObjectUuid;
/* 86 */       info.spbmComplianceResult = vmodl.spbmComplianceResult;
/*    */     } 
/*    */     
/* 89 */     return info;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfStatsObjectInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */