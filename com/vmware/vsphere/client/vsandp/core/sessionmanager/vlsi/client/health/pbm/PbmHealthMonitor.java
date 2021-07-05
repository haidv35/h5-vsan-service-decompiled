/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.health.pbm;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.health.IHealthMonitor;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm.PbmConnection;
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
/*    */ public class PbmHealthMonitor
/*    */   implements IHealthMonitor<PbmConnection, Object>
/*    */ {
/*    */   public void onCreated(PbmConnection resource, Object settings) {}
/*    */   
/*    */   public void onDisposed(PbmConnection resource, Object settings) {}
/*    */   
/*    */   public void check(PbmConnection resource, Object settings) throws Exception {
/* 24 */     resource.getProfileManager().fetchResourceType();
/*    */   }
/*    */   
/*    */   public void onError(PbmConnection resource, Object settings, Throwable t) {}
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/health/pbm/PbmHealthMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */