/*  1 */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;public class DpHealthMonitor implements IHealthMonitor<DpConnection, Object> { static { ajc$preClinit(); } private static final JoinPoint.StaticPart ajc$tjp_0; private static void ajc$preClinit() { Factory factory = new Factory("DpHealthMonitor.java", DpHealthMonitor.class); ajc$tjp_0 = factory.makeSJP("method-call", (Signature)factory.makeMethodSig("401", "queryCgByObject", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery$Spec", "arg0", "com.vmware.vim.vsandp.binding.vim.vsandp.fault.VsanClusterNotFound", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery"), 15); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void check(DpConnection resource, Object settings) throws Exception
/*    */   {
/* 12 */     DpExploratorySettings exploratorySettings = (DpExploratorySettings)settings;
/* 13 */     ManagedObjectReference acquisitionRef = exploratorySettings.getAcquisitionRef();
/* 14 */     InventoryService.CgMemberQuery.Spec dummySpec = new InventoryService.CgMemberQuery.Spec(acquisitionRef, new String[] { "dummyId" });
/* 15 */     InventoryService.CgMemberQuery.Spec spec1 = dummySpec; InventoryService inventoryService = resource.getInventoryService(); (InventoryService.CgMemberQuery)queryCgByObject_aroundBody1$advice(this, inventoryService, spec1, VsanDpTimingAspect.aspectOf(), null, ajc$tjp_0); } private static final InventoryService.CgMemberQuery queryCgByObject_aroundBody0(DpHealthMonitor paramDpHealthMonitor, InventoryService paramInventoryService, InventoryService.CgMemberQuery.Spec paramSpec) { return paramInventoryService.queryCgByObject(paramSpec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onCreated(DpConnection resource, Object settings) {}
/*    */ 
/*    */   
/*    */   private static final Object queryCgByObject_aroundBody1$advice(DpHealthMonitor ajc$this, InventoryService target, InventoryService.CgMemberQuery.Spec arg0, VsanDpTimingAspect ajc$aspectInstance, AroundClosure ajc$aroundClosure, JoinPoint.StaticPart thisJoinPointStaticPart) {
/* 23 */     long startTimeMs = System.currentTimeMillis();
/*    */     
/* 25 */     AroundClosure aroundClosure = ajc$aroundClosure; Object result = queryCgByObject_aroundBody0(ajc$this, target, arg0);
/*    */     
/* 27 */     long endTimeMs = System.currentTimeMillis();
/* 28 */     long execTimeMs = endTimeMs - startTimeMs;
/*    */     
/* 30 */     if (execTimeMs > 500L) {
/* 31 */       String name = thisJoinPointStaticPart.getSignature().toString();
/* 32 */       String msg = "Executing " + name + 
/* 33 */         " took too long: " + execTimeMs + " ms.";
/* 34 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().warn(msg);
/*    */     } else {
/* 36 */       String name = thisJoinPointStaticPart.getSignature().toString();
/* 37 */       String msg = "Executing " + name + " took : " + execTimeMs + " ms.";
/* 38 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().debug(msg);
/*    */     } 
/*    */     
/* 41 */     return result;
/*    */   }
/*    */   
/*    */   public void onDisposed(DpConnection resource, Object settings) {}
/*    */   
/*    */   public void onError(DpConnection resource, Object settings, Throwable t) {} }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpHealthMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */