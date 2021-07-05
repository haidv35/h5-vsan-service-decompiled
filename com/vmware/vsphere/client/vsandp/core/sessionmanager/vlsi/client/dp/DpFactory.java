/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ public class DpFactory
/*    */   extends AbstractConnectionFactory<DpConnection, VlsiSettings>
/*    */ {
/*    */   protected DpConnection buildConnection(VlsiSettings id) {
/* 12 */     return new DpConnection();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */