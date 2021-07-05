/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.health.vc;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.ServiceInstance;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.health.IHealthMonitor;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
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
/*    */ public class VcHealthMonitor
/*    */   implements IHealthMonitor<VcConnection, Object>
/*    */ {
/*    */   public static final String SERVICE_INSTANCE = "ServiceInstance";
/*    */   
/*    */   public void onCreated(VcConnection resource, Object settings) {}
/*    */   
/*    */   public void onDisposed(VcConnection resource, Object settings) {}
/*    */   
/*    */   public void check(VcConnection resource, Object settings) {
/* 27 */     ServiceInstance si = (ServiceInstance)resource.createStub(
/* 28 */         ServiceInstance.class, "ServiceInstance");
/*    */     
/* 30 */     si.getServerClock();
/*    */   }
/*    */   
/*    */   public void onError(VcConnection resource, Object settings, Throwable t) {}
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/health/vc/VcHealthMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */