/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PbmExploratoryFactory
/*    */   implements ResourceFactory<PbmConnection, VlsiExploratorySettings>
/*    */ {
/*    */   private final ResourceFactory<PbmConnection, VlsiSettings> pbmFactory;
/*    */   
/*    */   public PbmExploratoryFactory(ResourceFactory<PbmConnection, VlsiSettings> pbmFactory) {
/* 25 */     this.pbmFactory = pbmFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public PbmConnection acquire(VlsiExploratorySettings settings) {
/*    */     VlsiSettings vlsiSettings;
/* 31 */     Exception exception1 = null, exception2 = null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/pbm/PbmExploratoryFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */