/*     */ package com.vmware.vsphere.client.vsan.iscsi.models.config;
/*     */ 
/*     */ import com.vmware.vim.binding.pbm.compliance.ComplianceResult;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StorageComplianceResult;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.data.StoragePolicyData;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.cluster.VsanDatastoreHostData;
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
/*     */ public class VsanIscsiTargetConfig
/*     */ {
/*     */   public boolean emptyCluster;
/*     */   public boolean iscsiSupportedOnCluster;
/*     */   public boolean hostsVersionValid;
/*     */   public boolean status;
/*     */   public String network;
/*     */   public Integer port;
/*     */   public IscsiTargetAuthType authType;
/*     */   public String homeObjectUuid;
/*     */   public StorageCompliance homeObjectStorageCompliance;
/*     */   public ObjectHealthStatus homeObjectHealthStatus;
/*     */   public StoragePolicyData homeObjectStorageProfile;
/*     */   public String homeObjectStorageProfileUuid;
/*     */   public String incomingUser;
/*     */   public String incomingSecret;
/*     */   public String outgoingUser;
/*     */   public String outgoingSecret;
/*     */   
/*     */   public VsanIscsiTargetConfig(VsanIscsiConfig config, VsanDatastoreHostData datastoreHostData, boolean iscsiSupportedOnCluster, boolean hostsVersionValid, Profile profile) {
/*  39 */     this.emptyCluster = false;
/*  40 */     if (datastoreHostData == null || 
/*  41 */       datastoreHostData.vsanDatastoreRef == null || 
/*  42 */       datastoreHostData.hostRef == null) {
/*  43 */       this.emptyCluster = true;
/*     */     }
/*     */     
/*  46 */     this.iscsiSupportedOnCluster = iscsiSupportedOnCluster;
/*  47 */     this.hostsVersionValid = hostsVersionValid;
/*     */     
/*  49 */     if (config == null) {
/*  50 */       this.iscsiSupportedOnCluster = false;
/*     */       
/*     */       return;
/*     */     } 
/*  54 */     if (config.vsanIscsiTargetServiceConfig != null) {
/*     */       
/*  56 */       this.status = config.vsanIscsiTargetServiceConfig.enabled.booleanValue();
/*     */       
/*  58 */       if (config.vsanIscsiTargetServiceConfig.defaultConfig != null) {
/*  59 */         this.network = config.vsanIscsiTargetServiceConfig.defaultConfig.networkInterface;
/*  60 */         this.port = config.vsanIscsiTargetServiceConfig.defaultConfig.port;
/*     */         
/*  62 */         if (config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec != null && 
/*  63 */           config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.authType != null) {
/*  64 */           this.authType = 
/*  65 */             IscsiTargetAuthType.valueOf(config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.authType);
/*  66 */           this.incomingUser = config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.userNameAttachToTarget;
/*  67 */           this.incomingSecret = config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.userSecretAttachToTarget;
/*  68 */           this.outgoingUser = config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.userNameAttachToInitiator;
/*  69 */           this.outgoingSecret = config.vsanIscsiTargetServiceConfig.defaultConfig.iscsiTargetAuthSpec.userSecretAttachToInitiator;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     if (config.vsanObjectInformation != null) {
/*  75 */       this.homeObjectUuid = config.vsanObjectInformation.vsanObjectUuid;
/*  76 */       this.homeObjectStorageCompliance = getStorageComplianceStatus(config.vsanObjectInformation.spbmComplianceResult);
/*  77 */       this.homeObjectHealthStatus = getHomeObjectHealthStatus(config.vsanObjectInformation.spbmComplianceResult);
/*  78 */       this.homeObjectStorageProfile = (profile == null) ? null : new StoragePolicyData(null, profile);
/*  79 */       this.homeObjectStorageProfileUuid = config.vsanObjectInformation.spbmProfileUuid;
/*     */     } 
/*     */   }
/*     */   
/*     */   private StorageCompliance getStorageComplianceStatus(StorageComplianceResult storageComplianceResult) {
/*  84 */     if (storageComplianceResult == null) {
/*  85 */       return StorageCompliance.unknown;
/*     */     }
/*  87 */     if (storageComplianceResult.profile == null) {
/*  88 */       return null;
/*     */     }
/*  90 */     if (storageComplianceResult.mismatch) {
/*  91 */       return StorageCompliance.outOfDate;
/*     */     }
/*  93 */     if (storageComplianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.compliant.toString()))
/*  94 */       return StorageCompliance.compliant; 
/*  95 */     if (storageComplianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.nonCompliant.toString()))
/*  96 */       return StorageCompliance.nonCompliant; 
/*  97 */     if (storageComplianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.notApplicable.toString())) {
/*  98 */       return StorageCompliance.notApplicable;
/*     */     }
/* 100 */     return StorageCompliance.unknown;
/*     */   }
/*     */   
/*     */   private ObjectHealthStatus getHomeObjectHealthStatus(StorageComplianceResult storageComplianceResult) {
/* 104 */     if (storageComplianceResult == null || storageComplianceResult.operationalStatus == null) {
/* 105 */       return ObjectHealthStatus.unknown;
/*     */     }
/* 107 */     boolean transitional = false;
/* 108 */     if (storageComplianceResult.operationalStatus.transitional != null) {
/* 109 */       transitional = storageComplianceResult.operationalStatus.transitional.booleanValue();
/*     */     }
/* 111 */     if (storageComplianceResult.operationalStatus.healthy.booleanValue()) {
/* 112 */       if (transitional) {
/* 113 */         return ObjectHealthStatus.transitionalHealthy;
/*     */       }
/* 115 */       return ObjectHealthStatus.healthy;
/*     */     } 
/*     */     
/* 118 */     if (transitional) {
/* 119 */       return ObjectHealthStatus.transitionalUnhealthy;
/*     */     }
/* 121 */     return ObjectHealthStatus.unhealthy;
/*     */   }
/*     */ 
/*     */   
/*     */   @data
/*     */   public enum StorageCompliance
/*     */   {
/* 128 */     outOfDate,
/* 129 */     compliant,
/* 130 */     nonCompliant,
/* 131 */     notApplicable,
/* 132 */     unknown;
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum IscsiTargetAuthType {
/* 137 */     NoAuth,
/* 138 */     CHAP,
/* 139 */     CHAP_Mutual;
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum ObjectHealthStatus {
/* 144 */     transitionalHealthy,
/* 145 */     healthy,
/* 146 */     transitionalUnhealthy,
/* 147 */     unhealthy,
/* 148 */     unknown;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/config/VsanIscsiTargetConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */