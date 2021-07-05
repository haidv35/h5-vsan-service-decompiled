/*     */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiInitiatorGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.InitiatorGroupAdditionSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.initiator.InitiatorGroupInitiatorAdditionSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.initiator.InitiatorGroupInitiatorRemoveSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.target.InitiatorGroupTargetAdditionSpec;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.target.InitiatorGroupTargetRemoveSpec;
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
/*     */ public class VsanIscsiInitiatorGroupMutationProvider
/*     */ {
/*  27 */   private static final Log _logger = LogFactory.getLog(VsanIscsiInitiatorGroupMutationProvider.class);
/*     */   
/*  29 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  30 */       VsanIscsiInitiatorGroupMutationProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void createInitiatorGroup(ManagedObjectReference clusterRef, InitiatorGroupAdditionSpec spec) throws Exception {
/*  38 */     Validate.notNull(spec);
/*  39 */     Validate.notEmpty(spec.initiatorGroupName);
/*     */     
/*  41 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*  42 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanIscsiSystem.addIscsiInitiatorGroup"); 
/*  43 */       try { vsanIscsiSystem.addIscsiInitiatorGroup(clusterRef, spec.initiatorGroupName); }
/*  44 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */   
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
/*     */   @TsService
/*     */   public void removeInitiatorGroup(ManagedObjectReference clusterRef, String name) throws Exception {
/*  62 */     Validate.notEmpty(name);
/*     */     
/*  64 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*  65 */     VsanIscsiInitiatorGroup vsanIscsiInitiatorGroup = null;
/*  66 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanIscsiSystem.getIscsiInitiatorGroup"); 
/*  67 */       try { vsanIscsiInitiatorGroup = vsanIscsiSystem.getIscsiInitiatorGroup(clusterRef, name); }
/*  68 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
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
/*  81 */     exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void addInitiators(ManagedObjectReference clusterRef, InitiatorGroupInitiatorAdditionSpec spec) throws Exception {
/*  92 */     Validate.notNull(spec);
/*  93 */     Validate.notEmpty((Object[])spec.initiatorNames);
/*  94 */     Validate.notEmpty(spec.initiatorGroupName);
/*     */     
/*  96 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public void removeInitiator(ManagedObjectReference clusterRef, InitiatorGroupInitiatorRemoveSpec spec) throws Exception {
/* 112 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     Validate.notNull(spec);
/* 117 */     Validate.notEmpty(spec.initiatorGroupName);
/* 118 */     Validate.notEmpty(spec.initiatorName);
/*     */     
/* 120 */     Exception exception1 = null, exception2 = null;
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
/*     */   public void addTarget(ManagedObjectReference clusterRef, InitiatorGroupTargetAdditionSpec spec) throws Exception {
/* 137 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*     */       return;
/*     */     }
/*     */     
/* 141 */     Validate.notNull(spec);
/* 142 */     Validate.notNull(spec.targetAliases);
/* 143 */     Validate.notEmpty(spec.initiatorGroupName);
/*     */     
/* 145 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public void removeTarget(ManagedObjectReference clusterRef, InitiatorGroupTargetRemoveSpec spec) throws Exception {
/* 166 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*     */       return;
/*     */     }
/*     */     
/* 170 */     Validate.notNull(spec);
/* 171 */     Validate.notEmpty(spec.initiatorGroupName);
/* 172 */     Validate.notEmpty(spec.targetAlias);
/*     */     
/* 174 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiInitiatorGroupMutationProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */