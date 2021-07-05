/*     */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUN;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTarget;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.TargetOperatoinSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.initiator.TargetInitiatorEditSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.initiator.TargetInitiatorRemoveSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.lun.LunOperationSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.lun.TargetLunRemoveSpec;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanIscsiTargetMutationProvider
/*     */ {
/*  34 */   private static final Log _logger = LogFactory.getLog(VsanIscsiTargetMutationProvider.class);
/*     */   
/*  36 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanIscsiTargetMutationProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference addTarget(ManagedObjectReference clusterRef, TargetOperatoinSpec spec) throws Exception {
/*  44 */     Validate.notNull(spec);
/*  45 */     validateTargetIQN(clusterRef, spec.iqn);
/*  46 */     validateTargetAlias(clusterRef, spec.alias);
/*     */     
/*  48 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateTargetIQN(ManagedObjectReference clusterRef, String iqn) throws Exception {
/*  64 */     if (StringUtils.isEmpty(StringUtils.trim(iqn))) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     VsanIscsiTarget[] targets = null; try {
/*  69 */       Exception exception2, exception1 = null;
/*     */     
/*     */     }
/*  72 */     catch (Exception e) {
/*  73 */       _logger.error("Failed to get the vSAN iSCSI target list.", e);
/*  74 */       throw new Exception(e.getLocalizedMessage(), e);
/*     */     } 
/*     */     
/*  77 */     if (targets != null) {
/*  78 */       byte b; int i; VsanIscsiTarget[] arrayOfVsanIscsiTarget; for (i = (arrayOfVsanIscsiTarget = targets).length, b = 0; b < i; ) { VsanIscsiTarget existingTarget = arrayOfVsanIscsiTarget[b];
/*  79 */         if (existingTarget != null)
/*     */         {
/*     */           
/*  82 */           if (iqn.equalsIgnoreCase(existingTarget.iqn)) {
/*  83 */             throw new Exception(Utils.getLocalizedString("error.target.iqn.duplicated", new String[] { iqn }));
/*     */           }
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void validateTargetAlias(ManagedObjectReference clusterRef, String newTargetAlias) throws Exception {
/*  93 */     VsanIscsiTarget target = null; try {
/*  94 */       Exception exception2, exception1 = null;
/*     */     
/*     */     }
/*  97 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     if (target != null) {
/* 103 */       throw new Exception(Utils.getLocalizedString("error.target.alias.duplicated", new String[] { newTargetAlias }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference editTarget(ManagedObjectReference clusterRef, TargetOperatoinSpec spec) throws Exception {
/* 113 */     Validate.notNull(spec);
/*     */     
/* 115 */     if (StringUtils.isNotEmpty(StringUtils.trim(spec.newAlias))) {
/* 116 */       validateTargetAlias(clusterRef, spec.newAlias);
/*     */     }
/*     */     
/* 119 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<ManagedObjectReference> batchPolicyReapply(ManagedObjectReference clusterRef, TargetOperatoinSpec[] specs) throws Exception {
/* 136 */     Validate.notEmpty((Object[])specs);
/*     */     
/* 138 */     List<ManagedObjectReference> tasksList = new ArrayList<>();
/* 139 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference removeTarget(ManagedObjectReference clusterRef, String targetAlias) throws Exception {
/* 162 */     Validate.notEmpty(targetAlias);
/*     */     
/* 164 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference createLun(ManagedObjectReference clusterRef, LunOperationSpec spec) throws Exception {
/* 194 */     validate(clusterRef, spec, spec.targetAlias);
/* 195 */     validateLunId(clusterRef, spec.targetAlias, spec.lunId);
/*     */     
/* 197 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference editLun(ManagedObjectReference clusterRef, LunOperationSpec spec) throws Exception {
/* 214 */     validate(clusterRef, spec, spec.targetAlias);
/* 215 */     validateLunId(clusterRef, spec.targetAlias, spec.newLunId);
/*     */     
/* 217 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateLunId(ManagedObjectReference clusterRef, String targetAlias, int newId) throws Exception {
/* 233 */     Validate.notEmpty(String.valueOf(newId));
/*     */     
/* 235 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/* 236 */     VsanIscsiLUN lun = null; try {
/* 237 */       Exception exception2, exception1 = null;
/*     */     }
/* 239 */     catch (Exception exception) {}
/*     */ 
/*     */     
/* 242 */     if (lun != null) {
/* 243 */       throw new Exception(Utils.getLocalizedString("error.lun.id.duplicated", new String[] { String.valueOf(newId) }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference[] removeLun(ManagedObjectReference clusterRef, TargetLunRemoveSpec spec) throws Exception {
/* 253 */     validate(clusterRef, spec, spec.targetAlias);
/* 254 */     Validate.notEmpty((Object[])spec.targetLunIds);
/*     */     
/* 256 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void allowIniatorAccess(ManagedObjectReference clusterRef, TargetInitiatorEditSpec spec) throws Exception {
/* 278 */     validate(clusterRef, spec, spec.targetAlias);
/* 279 */     Validate.notEmpty((Object[])spec.targetInitiatorNames);
/*     */     
/* 281 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void disallowInitiatorAccess(ManagedObjectReference clusterRef, TargetInitiatorRemoveSpec spec) throws Exception {
/* 294 */     validate(clusterRef, spec, spec.targetAlias);
/* 295 */     Validate.notEmpty((Object[])spec.targetInitiatorNames);
/*     */     
/* 297 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate(ManagedObjectReference clusterRef, Object spec, String targetAlias) throws Exception {
/* 304 */     Validate.notNull(spec);
/* 305 */     Validate.notEmpty(targetAlias);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiTargetMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */