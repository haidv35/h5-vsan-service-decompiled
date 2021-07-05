/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.binding.lookup.ServiceInstance;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LookupSvcFactory
/*    */   extends AbstractConnectionFactory<LookupSvcConnection, VlsiSettings>
/*    */ {
/*    */   protected LookupSvcConnection buildConnection(VlsiSettings id) {
/* 16 */     return new LookupSvcConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onConnect(VlsiSettings id, LookupSvcConnection connection) {
/* 21 */     super.onConnect(id, connection);
/*    */     
/* 23 */     ServiceInstance si = (ServiceInstance)connection.createStub(ServiceInstance.class, "ServiceInstance");
/* 24 */     connection.content = si.retrieveServiceContent();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/LookupSvcFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */