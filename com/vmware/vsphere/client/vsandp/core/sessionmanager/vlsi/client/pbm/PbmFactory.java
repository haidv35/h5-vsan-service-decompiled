/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.ServiceInstance;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PbmFactory
/*    */   extends AbstractConnectionFactory<PbmConnection, VlsiSettings>
/*    */ {
/*    */   protected PbmConnection buildConnection(VlsiSettings settings) {
/* 15 */     return new PbmConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onConnect(VlsiSettings id, PbmConnection connection) {
/* 20 */     super.onConnect(id, connection);
/*    */     
/* 22 */     ServiceInstance vcSi = (ServiceInstance)connection.createStub(ServiceInstance.class, "ServiceInstance");
/* 23 */     connection.content = vcSi.getContent();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/pbm/PbmFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */