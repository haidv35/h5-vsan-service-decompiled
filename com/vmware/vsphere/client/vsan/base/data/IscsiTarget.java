/*     */ package com.vmware.vsphere.client.vsan.base.data;
/*     */ 
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StorageComplianceResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StorageOperationalStatus;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUN;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTarget;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetAuthSpec;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiAuthSpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections.CollectionUtils;
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
/*     */ @data
/*     */ public class IscsiTarget
/*     */   extends VsanObject
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public String iqn;
/*     */   public String alias;
/*     */   public Integer lunCount;
/*     */   public String networkInterface;
/*     */   public Integer port;
/*     */   public String ioOwnerHost;
/*     */   public String authType;
/*     */   public String vmStoragePolicyUuid;
/*     */   public Date lastChecked;
/*     */   public VsanOperationalStatus operationalStatus;
/*  46 */   public List<IscsiLun> luns = new ArrayList<>();
/*     */   
/*     */   public VsanIscsiAuthSpec authSpec;
/*     */   
/*     */   public IscsiTarget() {
/*  51 */     this.objectType = VsanObjectType.iscsiTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public IscsiTarget(VsanIscsiTarget iscsi, List<VsanIscsiLUN> iscsiLuns, List<Profile> storageProfiles, Object namespaceMetadata) {
/*  56 */     this.objectType = VsanObjectType.iscsiTarget;
/*  57 */     this.iqn = iscsi.iqn;
/*  58 */     this.alias = iscsi.alias;
/*  59 */     this.name = iscsi.alias;
/*  60 */     this.lunCount = iscsi.lunCount;
/*  61 */     this.networkInterface = iscsi.networkInterface;
/*  62 */     this.port = iscsi.port;
/*  63 */     this.ioOwnerHost = iscsi.ioOwnerHost;
/*  64 */     this.authType = iscsi.authSpec.authType;
/*  65 */     this.authSpec = getVsanIscsiAuthSpec(iscsi.authSpec);
/*  66 */     this.namespaceCapabilityMetadata = namespaceMetadata;
/*     */     
/*  68 */     if (iscsi.objectInformation != null) {
/*  69 */       this.vsanObjectUuid = iscsi.objectInformation.vsanObjectUuid;
/*  70 */       this.healthState = VsanObjectHealthState.fromServerLocalizedString(iscsi.objectInformation.vsanHealth);
/*     */       
/*  72 */       if (iscsi.objectInformation.spbmProfileUuid != null) {
/*  73 */         this.vmStoragePolicyUuid = iscsi.objectInformation.spbmProfileUuid;
/*  74 */         this.storagePolicy = BaseUtils.getProfileNameByUuid(storageProfiles, 
/*  75 */             this.vmStoragePolicyUuid, true);
/*     */       } 
/*     */       
/*  78 */       StorageComplianceResult storageStatus = iscsi.objectInformation.spbmComplianceResult;
/*  79 */       if (storageStatus != null) {
/*  80 */         this.complianceResult = BaseUtils.toComplianceResult(storageStatus);
/*  81 */         this.complianceStatus = BaseUtils.getComplianceStatus(this.complianceResult);
/*  82 */         this.lastChecked = storageStatus.checkTime.getTime();
/*     */         
/*  84 */         StorageOperationalStatus opStatus = storageStatus.operationalStatus;
/*  85 */         if (opStatus != null) {
/*  86 */           this.operationalStatus = BaseUtils.getOperationalState(opStatus);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     if (CollectionUtils.isEmpty(iscsiLuns)) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     for (VsanIscsiLUN lun : iscsiLuns) {
/*  96 */       this.luns.add(new IscsiLun(lun, storageProfiles, this.iqn, this.namespaceCapabilityMetadata));
/*     */     }
/*     */   }
/*     */   
/*     */   private VsanIscsiAuthSpec getVsanIscsiAuthSpec(VsanIscsiTargetAuthSpec sourceAuthSpec) {
/* 101 */     if (sourceAuthSpec == null) {
/* 102 */       return null;
/*     */     }
/* 104 */     VsanIscsiAuthSpec authSpec = new VsanIscsiAuthSpec();
/* 105 */     authSpec.authType = sourceAuthSpec.authType;
/* 106 */     authSpec.initiatorSecret = sourceAuthSpec.userSecretAttachToInitiator;
/* 107 */     authSpec.initiatorUsername = sourceAuthSpec.userNameAttachToInitiator;
/* 108 */     authSpec.targetSecret = sourceAuthSpec.userSecretAttachToTarget;
/* 109 */     authSpec.targetUsername = sourceAuthSpec.userNameAttachToTarget;
/* 110 */     return authSpec;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/IscsiTarget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */