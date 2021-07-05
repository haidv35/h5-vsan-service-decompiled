/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.binding.sso.admin.ServiceInstance;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SsoAdminFactory
/*    */   extends AbstractConnectionFactory<SsoAdminConnection, VlsiSettings>
/*    */ {
/*    */   protected SsoAdminConnection buildConnection(VlsiSettings settings) {
/* 16 */     return new SsoAdminConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onConnect(VlsiSettings settings, SsoAdminConnection connection) {
/* 21 */     super.onConnect(settings, connection);
/*    */     
/* 23 */     ServiceInstance si = (ServiceInstance)connection.createStub(ServiceInstance.class, "SsoAdminServiceInstance");
/* 24 */     connection.content = si.retrieveServiceContent();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoAdminFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */