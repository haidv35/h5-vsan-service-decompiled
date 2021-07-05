/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SsoEndpoints
/*    */ {
/*    */   protected final ServiceEndpoint sts;
/*    */   protected final ServiceEndpoint admin;
/*    */   protected final ServiceEndpoint groupCheck;
/*    */   
/*    */   public SsoEndpoints(ServiceEndpoint sts, ServiceEndpoint admin, ServiceEndpoint groupCheck) {
/* 14 */     this.sts = sts;
/* 15 */     this.admin = admin;
/* 16 */     this.groupCheck = groupCheck;
/*    */   }
/*    */   
/*    */   public ServiceEndpoint getSts() {
/* 20 */     return this.sts;
/*    */   }
/*    */   
/*    */   public ServiceEndpoint getAdmin() {
/* 24 */     return this.admin;
/*    */   }
/*    */   
/*    */   public ServiceEndpoint getGroupCheck() {
/* 28 */     return this.groupCheck;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoEndpoints.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */