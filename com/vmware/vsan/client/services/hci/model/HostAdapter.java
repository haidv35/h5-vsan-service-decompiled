/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class HostAdapter
/*    */ {
/*    */   public String name;
/*    */   public String deviceName;
/*    */   public String dvsName;
/*    */   
/*    */   public static HostAdapter create(String name, String deviceName) {
/* 19 */     HostAdapter result = new HostAdapter();
/* 20 */     result.name = name;
/* 21 */     result.deviceName = deviceName;
/* 22 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/HostAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */