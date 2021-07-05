/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum PerformanceObjectType
/*    */ {
/*  9 */   clusterVmConsumption,
/* 10 */   clusterBackend,
/* 11 */   clusterDomOwner,
/* 12 */   hostBackend,
/* 13 */   hostVmConsumption,
/* 14 */   hostPnic,
/* 15 */   hostVnic,
/* 16 */   hostNet,
/* 17 */   diskGroup,
/* 18 */   cacheDisk,
/* 19 */   capacityDisk,
/* 20 */   vm,
/* 21 */   virtualDisk,
/* 22 */   vscsi,
/* 23 */   cmmds,
/* 24 */   clomDiskStats,
/* 25 */   clomHostStats;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerformanceObjectType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */