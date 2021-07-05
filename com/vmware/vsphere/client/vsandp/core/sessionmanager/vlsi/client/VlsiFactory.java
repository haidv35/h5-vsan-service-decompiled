/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VlsiFactory
/*    */   extends AbstractConnectionFactory<VlsiConnection, VlsiSettings>
/*    */ {
/*    */   protected VlsiConnection buildConnection(VlsiSettings id) {
/* 20 */     return new VlsiConnection();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/VlsiFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */