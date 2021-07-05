/*    */ package com.vmware.vsphere.client.vsan.iscsi.models.config;
/*    */ 
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetAuthSpec;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanIscsiAuthSpec
/*    */ {
/*    */   public String authType;
/*    */   public String initiatorUsername;
/*    */   public String initiatorSecret;
/*    */   public String targetUsername;
/*    */   public String targetSecret;
/*    */   
/*    */   public static VsanIscsiTargetAuthSpec getVsanIscsiTargetAuthSpec(VsanIscsiAuthSpec spec) {
/* 20 */     if (spec == null) {
/* 21 */       return null;
/*    */     }
/* 23 */     VsanIscsiTargetAuthSpec authSpec = new VsanIscsiTargetAuthSpec();
/* 24 */     authSpec.authType = spec.authType;
/* 25 */     if (VsanIscsiTargetAuthSpec.VsanIscsiTargetAuthType.CHAP.toString().equals(spec.authType)) {
/* 26 */       authSpec.userNameAttachToTarget = spec.targetUsername;
/* 27 */       authSpec.userSecretAttachToTarget = spec.targetSecret;
/* 28 */     } else if (VsanIscsiTargetAuthSpec.VsanIscsiTargetAuthType.CHAP_Mutual.toString().equals(spec.authType)) {
/* 29 */       authSpec.userNameAttachToTarget = spec.targetUsername;
/* 30 */       authSpec.userSecretAttachToTarget = spec.targetSecret;
/* 31 */       authSpec.userNameAttachToInitiator = spec.initiatorUsername;
/* 32 */       authSpec.userSecretAttachToInitiator = spec.initiatorSecret;
/*    */     } 
/* 34 */     return authSpec;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/config/VsanIscsiAuthSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */