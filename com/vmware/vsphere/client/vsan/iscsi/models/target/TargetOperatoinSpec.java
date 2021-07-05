/*    */ package com.vmware.vsphere.client.vsan.iscsi.models.target;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.vm.DefinedProfileSpec;
/*    */ import com.vmware.vim.binding.vim.vm.ProfileSpec;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSpec;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiAuthSpec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class TargetOperatoinSpec
/*    */ {
/*    */   public String alias;
/*    */   public VsanIscsiAuthSpec authSpec;
/*    */   public String iqn;
/*    */   public String networkInterface;
/*    */   public String newAlias;
/*    */   public int port;
/*    */   public String policyId;
/*    */   
/*    */   public TargetOperatoinSpec() {}
/*    */   
/*    */   public TargetOperatoinSpec(String alias, VsanIscsiAuthSpec authSpec, String iqn, String networkInterface, String newAlias, int port, String policyId) {
/* 26 */     this.alias = alias;
/* 27 */     this.authSpec = authSpec;
/* 28 */     this.iqn = iqn;
/* 29 */     this.networkInterface = networkInterface;
/* 30 */     this.newAlias = newAlias;
/* 31 */     this.port = port;
/* 32 */     this.policyId = policyId;
/*    */   }
/*    */   
/*    */   public VsanIscsiTargetSpec toVmodlVsanIscsiTargetSpec() {
/* 36 */     VsanIscsiTargetSpec targetSpec = new VsanIscsiTargetSpec();
/* 37 */     targetSpec.alias = this.alias;
/* 38 */     targetSpec.authSpec = VsanIscsiAuthSpec.getVsanIscsiTargetAuthSpec(this.authSpec);
/* 39 */     targetSpec.iqn = this.iqn;
/* 40 */     targetSpec.networkInterface = this.networkInterface;
/* 41 */     targetSpec.newAlias = this.newAlias;
/* 42 */     targetSpec.port = Integer.valueOf(this.port);
/* 43 */     targetSpec.storagePolicy = (ProfileSpec)getDefinedProfileSpec();
/* 44 */     return targetSpec;
/*    */   }
/*    */   
/*    */   private DefinedProfileSpec getDefinedProfileSpec() {
/* 48 */     DefinedProfileSpec profile = new DefinedProfileSpec();
/* 49 */     profile.setProfileId(this.policyId);
/* 50 */     return profile;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/target/TargetOperatoinSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */