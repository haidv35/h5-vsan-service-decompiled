/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.summary.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum VmDataProtectionStatus
/*    */ {
/* 11 */   OK,
/* 12 */   PROTECTION_NOT_CONFIGURED,
/* 13 */   FULL_SYNC_IN_PROGRESS,
/* 14 */   PROTECTION_NOT_OWNER,
/* 15 */   INVALID_CONFIGURATION,
/* 16 */   VM_QUIESCING_FAILURE,
/* 17 */   UNKNOWN,
/* 18 */   CG_OBJECT_UNAVAILABLE,
/* 19 */   LOCAL_RETENTION_FAILURE,
/* 20 */   ARCHIVE_STORAGE_NOT_ACCESSIBLE,
/* 21 */   ARCHIVE_STORAGE_NO_SPACE,
/* 22 */   ARCHIVE_TARGET_NOT_CONFIGURED,
/* 23 */   LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD,
/* 24 */   CG_CONTAINS_UNPROMOTED_OBJECTS,
/* 25 */   WARNING,
/* 26 */   NOK;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/summary/model/VmDataProtectionStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */