/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class HostInCluster
/*    */ {
/*    */   public ManagedObjectReference moRef;
/*    */   public String hostUid;
/*    */   public String name;
/*    */   
/*    */   public static HostInCluster create(ManagedObjectReference moRef, String hostUid, String name) {
/* 17 */     HostInCluster result = new HostInCluster();
/* 18 */     result.moRef = moRef;
/* 19 */     result.hostUid = hostUid;
/* 20 */     result.name = name;
/* 21 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/HostInCluster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */