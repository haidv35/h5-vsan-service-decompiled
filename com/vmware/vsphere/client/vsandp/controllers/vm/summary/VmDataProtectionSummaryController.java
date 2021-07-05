/*     */ package com.vmware.vsphere.client.vsandp.controllers.vm.summary;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.ArchivalProtectionInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.CgInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.ProtectionInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.ProtectionState;
/*     */ import com.vmware.vsphere.client.vsan.util.MessageBundle;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.PitProvider;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.summary.model.VmArchiveDataProtectionData;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.summary.model.VmDataProtectionData;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.summary.model.VmDataProtectionStatus;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.summary.model.VmLocalDataProtectionData;
/*     */ import com.vmware.vsphere.client.vsandp.dataproviders.vm.VmConsistencyGroupPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VmDataProtectionSummaryController
/*     */ {
/*     */   @Autowired
/*     */   private VmConsistencyGroupPropertyProvider cgProvider;
/*     */   @Autowired
/*     */   private PitProvider pitProvider;
/*     */   @Autowired
/*     */   private VsanDpInventoryHelper inventoryHelper;
/*     */   @Autowired
/*     */   private MessageBundle messages;
/*     */   
/*     */   @TsService
/*     */   public VmDataProtectionData getVmProtectionData(ManagedObjectReference vmRef) throws Exception {
/*  41 */     CgInfo vmProtection = this.cgProvider.getVmDataProtection(vmRef);
/*     */     
/*  43 */     VmLocalDataProtectionData localData = (new VmLocalDataProtectionData()).setPitsInfo(this.pitProvider.getLocalPits(vmRef));
/*  44 */     setStatusInfo(localData, (ProtectionInfo)vmProtection.getLocal());
/*     */     
/*  46 */     VmArchiveDataProtectionData archiveData = null;
/*     */     
/*  48 */     ArchivalProtectionInfo[] archivalProtectionInfos = vmProtection.getArchive();
/*  49 */     if (archivalProtectionInfos != null && archivalProtectionInfos.length > 0) {
/*     */       
/*  51 */       archiveData = (new VmArchiveDataProtectionData()).setPitsInfo(this.pitProvider.getArchivePits(vmRef));
/*  52 */       setStatusInfo(archiveData, (ProtectionInfo)archivalProtectionInfos[0]);
/*     */     } 
/*     */     
/*  55 */     return new VmDataProtectionData(localData, archiveData, this.inventoryHelper.isVmRestoreAllowed(vmRef));
/*     */   }
/*     */   
/*     */   private void setStatusInfo(VmLocalDataProtectionData data, ProtectionInfo protectionInfo) {
/*  59 */     if (protectionInfo.getCurrentError() != null) {
/*  60 */       data.statusCode = VmDataProtectionStatus.NOK;
/*  61 */       data.statusDescription = 
/*  62 */         this.messages.string("controllers.vm.summary.protection.status.NOK.description");
/*     */     } else {
/*  64 */       switch (ProtectionState.valueOf(protectionInfo.getState())) {
/*     */         case healthOk:
/*  66 */           data.statusCode = VmDataProtectionStatus.OK;
/*  67 */           data.statusDescription = 
/*  68 */             this.messages.string("controllers.vm.summary.protection.status.OK.description");
/*     */           break;
/*     */         case protectionNotConfigured:
/*  71 */           data.statusCode = VmDataProtectionStatus.PROTECTION_NOT_CONFIGURED;
/*  72 */           data.statusDescription = 
/*  73 */             this.messages.string("controllers.vm.summary.protection.status.PROTECTION_NOT_CONFIGURED.description");
/*     */           break;
/*     */         case fullSyncInProgress:
/*  76 */           data.statusCode = VmDataProtectionStatus.FULL_SYNC_IN_PROGRESS;
/*  77 */           data.statusDescription = 
/*  78 */             this.messages.string("controllers.vm.summary.protection.status.FULL_SYNC_IN_PROGRESS.description");
/*     */           break;
/*     */         case protectionNotOwner:
/*  81 */           data.statusCode = VmDataProtectionStatus.PROTECTION_NOT_OWNER;
/*  82 */           data.statusDescription = 
/*  83 */             this.messages.string("controllers.vm.summary.protection.status.PROTECTION_NOT_OWNER.description");
/*     */           break;
/*     */         case invalidConfiguration:
/*  86 */           data.statusCode = VmDataProtectionStatus.INVALID_CONFIGURATION;
/*  87 */           data.statusDescription = 
/*  88 */             this.messages.string("controllers.vm.summary.protection.status.INVALID_CONFIGURATION.description");
/*     */           break;
/*     */         case vmQuiescingFailure:
/*  91 */           data.statusCode = VmDataProtectionStatus.VM_QUIESCING_FAILURE;
/*  92 */           data.statusDescription = 
/*  93 */             this.messages.string("controllers.vm.summary.protection.status.VM_QUIESCING_FAILURE.description");
/*     */           break;
/*     */         case unknown:
/*  96 */           data.statusCode = VmDataProtectionStatus.UNKNOWN;
/*  97 */           data.statusDescription = 
/*  98 */             this.messages.string("controllers.vm.summary.protection.status.UNKNOWN.description");
/*     */           break;
/*     */         case cgObjectUnavailable:
/* 101 */           data.statusCode = VmDataProtectionStatus.CG_OBJECT_UNAVAILABLE;
/* 102 */           data.statusDescription = 
/* 103 */             this.messages.string("controllers.vm.summary.protection.status.CG_OBJECT_UNAVAILABLE.description");
/*     */           break;
/*     */         case localRetentionFailure:
/* 106 */           data.statusCode = VmDataProtectionStatus.LOCAL_RETENTION_FAILURE;
/* 107 */           data.statusDescription = 
/* 108 */             this.messages.string("controllers.vm.summary.protection.status.LOCAL_RETENTION_FAILURE.description");
/*     */           break;
/*     */         case archiveStorageNotAccessible:
/* 111 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_STORAGE_NOT_ACCESSIBLE;
/* 112 */           data.statusDescription = 
/* 113 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_STORAGE_NOT_ACCESSIBLE.description");
/*     */           break;
/*     */         case null:
/* 116 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_STORAGE_NO_SPACE;
/* 117 */           data.statusDescription = 
/* 118 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_STORAGE_NO_SPACE.description");
/*     */           break;
/*     */         case archiveTargetNotConfigured:
/* 121 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_TARGET_NOT_CONFIGURED;
/* 122 */           data.statusDescription = 
/* 123 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_TARGET_NOT_CONFIGURED.description");
/*     */           break;
/*     */         case localStorageUsageExceededThreshold:
/* 126 */           data.statusCode = VmDataProtectionStatus.LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD;
/* 127 */           data.statusDescription = 
/* 128 */             this.messages.string("controllers.vm.summary.protection.status.LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD.description");
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setStatusInfo(VmArchiveDataProtectionData data, ProtectionInfo protectionInfo) {
/* 139 */     if (protectionInfo.getCurrentError() != null) {
/* 140 */       data.statusCode = VmDataProtectionStatus.NOK;
/* 141 */       data.statusDescription = 
/* 142 */         this.messages.string("controllers.vm.summary.protection.status.NOK.description");
/*     */     } else {
/* 144 */       switch (ProtectionState.valueOf(protectionInfo.getState())) {
/*     */         case healthOk:
/* 146 */           data.statusCode = VmDataProtectionStatus.OK;
/* 147 */           data.statusDescription = 
/* 148 */             this.messages.string("controllers.vm.summary.protection.status.OK.description");
/*     */           break;
/*     */         case protectionNotConfigured:
/* 151 */           data.statusCode = VmDataProtectionStatus.PROTECTION_NOT_CONFIGURED;
/* 152 */           data.statusDescription = 
/* 153 */             this.messages.string("controllers.vm.summary.protection.status.PROTECTION_NOT_CONFIGURED.description");
/*     */           break;
/*     */         case fullSyncInProgress:
/* 156 */           data.statusCode = VmDataProtectionStatus.FULL_SYNC_IN_PROGRESS;
/* 157 */           data.statusDescription = 
/* 158 */             this.messages.string("controllers.vm.summary.protection.status.FULL_SYNC_IN_PROGRESS.description");
/*     */           break;
/*     */         case protectionNotOwner:
/* 161 */           data.statusCode = VmDataProtectionStatus.PROTECTION_NOT_OWNER;
/* 162 */           data.statusDescription = 
/* 163 */             this.messages.string("controllers.vm.summary.protection.status.PROTECTION_NOT_OWNER.description");
/*     */           break;
/*     */         case invalidConfiguration:
/* 166 */           data.statusCode = VmDataProtectionStatus.INVALID_CONFIGURATION;
/* 167 */           data.statusDescription = 
/* 168 */             this.messages.string("controllers.vm.summary.protection.status.INVALID_CONFIGURATION.description");
/*     */           break;
/*     */         case vmQuiescingFailure:
/* 171 */           data.statusCode = VmDataProtectionStatus.VM_QUIESCING_FAILURE;
/* 172 */           data.statusDescription = 
/* 173 */             this.messages.string("controllers.vm.summary.protection.status.VM_QUIESCING_FAILURE.description");
/*     */           break;
/*     */         case unknown:
/* 176 */           data.statusCode = VmDataProtectionStatus.UNKNOWN;
/* 177 */           data.statusDescription = 
/* 178 */             this.messages.string("controllers.vm.summary.protection.status.UNKNOWN.description");
/*     */           break;
/*     */         case cgObjectUnavailable:
/* 181 */           data.statusCode = VmDataProtectionStatus.CG_OBJECT_UNAVAILABLE;
/* 182 */           data.statusDescription = 
/* 183 */             this.messages.string("controllers.vm.summary.protection.status.CG_OBJECT_UNAVAILABLE.description");
/*     */           break;
/*     */         case localRetentionFailure:
/* 186 */           data.statusCode = VmDataProtectionStatus.LOCAL_RETENTION_FAILURE;
/* 187 */           data.statusDescription = 
/* 188 */             this.messages.string("controllers.vm.summary.protection.status.LOCAL_RETENTION_FAILURE.description");
/*     */           break;
/*     */         case archiveStorageNotAccessible:
/* 191 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_STORAGE_NOT_ACCESSIBLE;
/* 192 */           data.statusDescription = 
/* 193 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_STORAGE_NOT_ACCESSIBLE.description");
/*     */           break;
/*     */         case null:
/* 196 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_STORAGE_NO_SPACE;
/* 197 */           data.statusDescription = 
/* 198 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_STORAGE_NO_SPACE.description");
/*     */           break;
/*     */         case archiveTargetNotConfigured:
/* 201 */           data.statusCode = VmDataProtectionStatus.ARCHIVE_TARGET_NOT_CONFIGURED;
/* 202 */           data.statusDescription = 
/* 203 */             this.messages.string("controllers.vm.summary.protection.status.ARCHIVE_TARGET_NOT_CONFIGURED.description");
/*     */           break;
/*     */         case localStorageUsageExceededThreshold:
/* 206 */           data.statusCode = VmDataProtectionStatus.LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD;
/* 207 */           data.statusDescription = 
/* 208 */             this.messages.string("controllers.vm.summary.protection.status.LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD.description");
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/summary/VmDataProtectionSummaryController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */