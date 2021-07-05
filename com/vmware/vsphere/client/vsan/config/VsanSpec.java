/*    */ package com.vmware.vsphere.client.vsan.config;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vim.vsan.cluster.ConfigInfo;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.DataProtectionArchivalLocation;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.DataProtectionInfo;
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
/*    */ @data
/*    */ public class VsanSpec
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public boolean isEnabled;
/*    */   public boolean isAutoClaimMode;
/*    */   public DataEfficiencySpec dataEfficiency;
/*    */   public boolean allowReducedRedundancy;
/*    */   public boolean isEncryptionEnabled;
/*    */   public String kmipClusterId;
/*    */   public boolean eraseDisksBeforeUse;
/*    */   public Integer localDpUsageThreshold;
/*    */   public String archiveDpDatastoreUrl;
/*    */   
/*    */   public ConfigInfo toVmodlSpec() {
/* 73 */     ConfigInfo vmodlSpec = new ConfigInfo();
/* 74 */     vmodlSpec.setEnabled(Boolean.valueOf(this.isEnabled));
/*    */     
/* 76 */     ConfigInfo.HostDefaultInfo defaultInfo = new ConfigInfo.HostDefaultInfo();
/* 77 */     defaultInfo.setAutoClaimStorage(Boolean.valueOf(this.isAutoClaimMode));
/*    */     
/* 79 */     vmodlSpec.setDefaultConfig(defaultInfo);
/*    */     
/* 81 */     return vmodlSpec;
/*    */   }
/*    */   
/*    */   public DataProtectionInfo getDpConfigInfo(boolean isArchivalSupported) {
/* 85 */     DataProtectionInfo configInfo = new DataProtectionInfo();
/*    */     
/* 87 */     if (this.localDpUsageThreshold != null) {
/* 88 */       configInfo.setUsageThreshold(this.localDpUsageThreshold);
/*    */     }
/*    */ 
/*    */     
/* 92 */     if (isArchivalSupported && this.archiveDpDatastoreUrl != null) {
/* 93 */       DataProtectionArchivalLocation archivalLocation = new DataProtectionArchivalLocation();
/* 94 */       archivalLocation.setDatastoreUrl(this.archiveDpDatastoreUrl);
/* 95 */       configInfo.setArchivalTarget(archivalLocation);
/*    */     } 
/*    */     
/* 98 */     return configInfo;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/config/VsanSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */