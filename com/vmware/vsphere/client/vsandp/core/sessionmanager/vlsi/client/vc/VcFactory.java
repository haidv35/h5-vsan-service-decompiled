/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.ServiceInstance;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VcFactory
/*    */   extends AbstractConnectionFactory<VcConnection, VlsiSettings>
/*    */ {
/*    */   protected VcConnection buildConnection(VlsiSettings id) {
/* 16 */     return new VcConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onConnect(VlsiSettings id, VcConnection connection) {
/* 21 */     super.onConnect(id, connection);
/*    */     
/* 23 */     ServiceInstance vcSi = (ServiceInstance)connection.createStub(ServiceInstance.class, "ServiceInstance");
/* 24 */     connection.content = vcSi.getContent();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/VcFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */