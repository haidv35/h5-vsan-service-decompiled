/*    */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.vm.DefinedProfileSpec;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetAuthSpec;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiConfigEditSpec;
/*    */ import org.apache.commons.lang.Validate;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class VsanIscsiMutationProvider
/*    */ {
/* 29 */   private static final Log _logger = LogFactory.getLog(VsanIscsiMutationProvider.class);
/*    */   
/* 31 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanIscsiMutationProvider.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public ManagedObjectReference editIscsiConfig(ManagedObjectReference clusterRef, VsanIscsiConfigEditSpec spec) throws Exception {
/* 42 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 43 */       return null;
/*    */     }
/*    */     
/* 46 */     Validate.notNull(spec);
/*    */     
/* 48 */     Exception exception1 = null, exception2 = null;
/*    */   }
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
/*    */   private DefinedProfileSpec createPofileSpec(VsanIscsiConfigEditSpec spec) {
/* 74 */     DefinedProfileSpec profileSpec = null;
/* 75 */     if (spec.policy != null) {
/* 76 */       profileSpec = new DefinedProfileSpec();
/* 77 */       profileSpec.setProfileId(spec.policy.id);
/*    */     } 
/* 79 */     return profileSpec;
/*    */   }
/*    */   
/*    */   private VsanIscsiTargetAuthSpec createIscsiAuthSpec(VsanIscsiConfigEditSpec spec) {
/* 83 */     if (spec.authSpec == null) {
/* 84 */       return null;
/*    */     }
/*    */     
/* 87 */     VsanIscsiTargetAuthSpec authSpec = new VsanIscsiTargetAuthSpec();
/* 88 */     authSpec.setAuthType(spec.authSpec.authType);
/* 89 */     authSpec.setUserNameAttachToInitiator(spec.authSpec.initiatorUsername);
/* 90 */     authSpec.setUserNameAttachToTarget(spec.authSpec.targetUsername);
/* 91 */     authSpec.setUserSecretAttachToInitiator(spec.authSpec.initiatorSecret);
/* 92 */     authSpec.setUserSecretAttachToTarget(spec.authSpec.targetSecret);
/* 93 */     return authSpec;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */