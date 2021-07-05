/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiExploratorySettings;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VcExploratoryFactory
/*    */   implements ResourceFactory<VcConnection, VlsiExploratorySettings>
/*    */ {
/*    */   private final ResourceFactory<VcConnection, VlsiSettings> vcFactory;
/*    */   
/*    */   public VcExploratoryFactory(ResourceFactory<VcConnection, VlsiSettings> vcFactory) {
/* 22 */     this.vcFactory = vcFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public VcConnection acquire(VlsiExploratorySettings settings) {
/*    */     VlsiSettings vlsiSettings;
/* 28 */     Exception exception1 = null, exception2 = null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/VcExploratoryFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */