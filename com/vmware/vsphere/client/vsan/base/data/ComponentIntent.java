/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum ComponentIntent
/*    */ {
/* 12 */   REPAIR(
/* 13 */     65536),
/* 14 */   DECOM(131072),
/* 15 */   REBALANCE(262144),
/* 16 */   FIXCOMPLIANCE(524288),
/* 17 */   POLICYCHANGE(1048576),
/* 18 */   MOVE(2097152),
/* 19 */   STALE(16777216),
/* 20 */   MERGE_CONTACT(67108864);
/*    */   
/*    */   private int value;
/*    */   
/*    */   ComponentIntent(int value) {
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 29 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/ComponentIntent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */