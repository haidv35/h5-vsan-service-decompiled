/*    */ package com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.target;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ @data
/*    */ public class InitiatorGroupTarget
/*    */ {
/*    */   public String alias;
/*    */   public String iqn;
/*    */   
/*    */   public InitiatorGroupTarget(String targetAlias, String targetIqn) {
/* 12 */     this.alias = targetAlias;
/* 13 */     this.iqn = targetIqn;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/initiatorgroup/target/InitiatorGroupTarget.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */