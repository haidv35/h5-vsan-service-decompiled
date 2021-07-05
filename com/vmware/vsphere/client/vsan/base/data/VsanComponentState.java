/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum VsanComponentState
/*    */ {
/* 11 */   ACTIVE,
/* 12 */   ACTIVE_STALE,
/* 13 */   ABSENT,
/* 14 */   ABSENT_RESYNC,
/* 15 */   DEGRADED,
/* 16 */   RECONFIG,
/* 17 */   UNKNOWN;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanComponentState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */