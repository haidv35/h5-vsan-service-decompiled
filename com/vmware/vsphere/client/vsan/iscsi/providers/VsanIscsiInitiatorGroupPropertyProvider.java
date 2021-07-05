/*     */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiInitiatorGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetBasicInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.InitiatorGroup;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.initiator.InitiatorGroupInitiator;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ public class VsanIscsiInitiatorGroupPropertyProvider
/*     */ {
/*  30 */   private static final Log _logger = LogFactory.getLog(VsanIscsiInitiatorGroupPropertyProvider.class);
/*     */   
/*  32 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  33 */       VsanIscsiInitiatorGroupPropertyProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public InitiatorGroup[] getVsanIscsiInitiatorGroupList(ManagedObjectReference clusterRef) throws Exception {
/*  44 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*  45 */       return null;
/*     */     }
/*     */     
/*  48 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*     */     
/*  50 */     VsanIscsiInitiatorGroup[] initiatorGroups = null;
/*  51 */     List<InitiatorGroup> groups = new ArrayList<>();
/*     */     try {
/*  53 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  60 */     catch (Exception e) {
/*  61 */       String errorMsg = Utils.getMethodFault(e).getMessage();
/*  62 */       if (!StringUtils.isBlank(errorMsg) && errorMsg.indexOf("vSAN iSCSI Target Service is not enabled or the enable task is in progress.") == -1) {
/*  63 */         Exception ex = new Exception(e.getLocalizedMessage(), e);
/*  64 */         throw ex;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  72 */       _logger.info("iscsi targets service enabling in progress, ignore the error");
/*     */     } 
/*     */ 
/*     */     
/*  76 */     return groups.<InitiatorGroup>toArray(new InitiatorGroup[0]);
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
/*     */   @TsService
/*     */   public VsanIscsiInitiatorGroup getVsanIscsiInitiatorGroup(ManagedObjectReference clusterRef, String name) throws Exception {
/*  90 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*  91 */       return null;
/*     */     }
/*     */     
/*  94 */     VsanIscsiTargetSystem vsanIscsiSystem = 
/*  95 */       VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*  96 */     VsanIscsiInitiatorGroup initiatorGroup = null;
/*  97 */     Exception exception1 = null, exception2 = null;
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
/*     */   public InitiatorGroupInitiator[] getInitiatorGroupInitiatorList(ManagedObjectReference clusterRef, String initiatorGroupIqn) throws Exception {
/*     */     InitiatorGroupInitiator[] initiators;
/* 115 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 116 */       return null;
/*     */     }
/*     */     
/* 119 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*     */     
/* 121 */     VsanIscsiInitiatorGroup vsanIscsiInitiatorGroup = null;
/* 122 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanIscsiSystem.getIscsiInitiatorGroup"); 
/* 123 */       try { vsanIscsiInitiatorGroup = vsanIscsiSystem.getIscsiInitiatorGroup(clusterRef, initiatorGroupIqn); }
/* 124 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/*     */ 
/*     */ 
/*     */     
/* 140 */     return initiators;
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
/*     */   public VsanIscsiTargetBasicInfo[] getVsanIscsiInitiatorGroupTargetList(ManagedObjectReference clusterRef, String initiatorGroupIqn) throws Exception {
/* 156 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     VsanIscsiTargetSystem vsanIscsiSystem = 
/* 161 */       VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*     */     
/* 163 */     VsanIscsiInitiatorGroup vsanIscsiInitiatorGroup = null;
/* 164 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiInitiatorGroupPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */