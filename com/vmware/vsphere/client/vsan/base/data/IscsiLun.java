/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.profile.Profile;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.StorageComplianceResult;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.StorageOperationalStatus;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUN;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*    */ import com.vmware.vsphere.client.vsan.util.Utils;
/*    */ import java.util.Date;
/*    */ import java.util.List;
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
/*    */ public class IscsiLun
/*    */   extends VsanObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public Integer lunId;
/*    */   public String alias;
/*    */   public String targetAlias;
/*    */   public String targetIqn;
/*    */   public long lunSize;
/*    */   public long actualSize;
/*    */   public IscsiLunStatus status;
/*    */   public String vmStoragePolicyUuid;
/*    */   public Date lastChecked;
/*    */   public VsanOperationalStatus operationalStatus;
/*    */   
/*    */   public IscsiLun() {
/* 43 */     this.objectType = VsanObjectType.iscsiLun;
/*    */   }
/*    */   
/*    */   public IscsiLun(VsanIscsiLUN lun, List<Profile> storageProfiles) {
/* 47 */     this.objectType = VsanObjectType.iscsiLun;
/* 48 */     this.lunId = lun.lunId;
/* 49 */     this.alias = lun.alias;
/* 50 */     this.targetAlias = lun.targetAlias;
/* 51 */     this.name = Utils.getLocalizedString("vsan.virtualObjects.iscsiLun", new String[] { this.alias, this.lunId.toString() }).trim();
/* 52 */     this.lunSize = lun.lunSize;
/* 53 */     this.actualSize = lun.actualSize;
/* 54 */     if (lun.status.equals("Online")) {
/* 55 */       this.status = IscsiLunStatus.Online;
/*    */     } else {
/* 57 */       this.status = IscsiLunStatus.Offline;
/*    */     } 
/* 59 */     if (lun.objectInformation != null) {
/* 60 */       this.vsanObjectUuid = lun.objectInformation.vsanObjectUuid;
/* 61 */       this.healthState = VsanObjectHealthState.fromServerLocalizedString(lun.objectInformation.vsanHealth);
/*    */       
/* 63 */       if (lun.objectInformation.spbmProfileUuid != null) {
/* 64 */         this.vmStoragePolicyUuid = lun.objectInformation.spbmProfileUuid;
/* 65 */         this.storagePolicy = BaseUtils.getProfileNameByUuid(storageProfiles, 
/* 66 */             this.vmStoragePolicyUuid, true);
/*    */       } 
/*    */       
/* 69 */       StorageComplianceResult storageStatus = lun.objectInformation.spbmComplianceResult;
/* 70 */       if (storageStatus != null) {
/* 71 */         this.complianceResult = BaseUtils.toComplianceResult(storageStatus);
/* 72 */         this.complianceStatus = BaseUtils.getComplianceStatus(this.complianceResult);
/* 73 */         this.lastChecked = storageStatus.checkTime.getTime();
/*    */         
/* 75 */         StorageOperationalStatus opStatus = storageStatus.operationalStatus;
/* 76 */         if (opStatus != null) {
/* 77 */           this.operationalStatus = BaseUtils.getOperationalState(opStatus);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public IscsiLun(VsanIscsiLUN lun, List<Profile> storageProfiles, String targetIqnString, Object namespaceMetadata) {
/* 84 */     this(lun, storageProfiles);
/* 85 */     this.targetIqn = targetIqnString;
/* 86 */     this.namespaceCapabilityMetadata = namespaceMetadata;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/IscsiLun.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */