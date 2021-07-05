/*    */ package com.vmware.vsphere.client.vsan.iscsi.models.target.lun;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.vm.DefinedProfileSpec;
/*    */ import com.vmware.vim.binding.vim.vm.ProfileSpec;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUNSpec;
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
/*    */ @data
/*    */ public class LunOperationSpec
/*    */ {
/*    */   public String targetAlias;
/*    */   public String lunAlias;
/*    */   public int lunId;
/*    */   public long lunSize;
/*    */   public int newLunId;
/*    */   public String status;
/*    */   public String policyId;
/*    */   
/*    */   public LunOperationSpec() {}
/*    */   
/*    */   public LunOperationSpec(String targetAlias, String lunAlias, int lunId, long lunSize, int newLunId, String status, String policyId) {
/* 33 */     this.targetAlias = targetAlias;
/* 34 */     this.lunAlias = lunAlias;
/* 35 */     this.lunId = lunId;
/* 36 */     this.lunSize = lunSize;
/* 37 */     this.newLunId = newLunId;
/* 38 */     this.status = status;
/* 39 */     this.policyId = policyId;
/*    */   }
/*    */   
/*    */   public VsanIscsiLUNSpec toVmodlVsanIscsiLUNSpec() {
/* 43 */     VsanIscsiLUNSpec lunSpec = new VsanIscsiLUNSpec();
/* 44 */     lunSpec.alias = this.lunAlias;
/* 45 */     lunSpec.lunId = Integer.valueOf(this.lunId);
/* 46 */     lunSpec.lunSize = this.lunSize;
/* 47 */     lunSpec.newLunId = Integer.valueOf(this.newLunId);
/* 48 */     lunSpec.status = this.status;
/* 49 */     lunSpec.storagePolicy = (ProfileSpec)getDefinedProfileSpec();
/* 50 */     return lunSpec;
/*    */   }
/*    */   
/*    */   private DefinedProfileSpec getDefinedProfileSpec() {
/* 54 */     DefinedProfileSpec profile = new DefinedProfileSpec();
/* 55 */     profile.profileId = this.policyId;
/* 56 */     return profile;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/target/lun/LunOperationSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */