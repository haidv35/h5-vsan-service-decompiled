/*  1 */ package com.vmware.vsphere.client.vsandp.core.logging;@Aspect public class VsanDpTimingAspect { private static final Log _logger; public static VsanDpTimingAspect aspectOf() { if (ajc$perSingletonInstance == null) throw new NoAspectBoundException("com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect", ajc$initFailureCause);  return ajc$perSingletonInstance; } private static final int THRESHOLD = 500; public static boolean hasAspect() { return (ajc$perSingletonInstance != null); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 11 */       _logger = 
/* 12 */         LogFactory.getLog(VsanDpTimingAspect.class);
/*    */       ajc$postClinit();
/*    */     } catch (Throwable throwable) {
/*    */       ajc$initFailureCause = throwable = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Around(value = "vsanDpVmodlInvocation()", argNames = "ajc$aroundClosure")
/*    */   public Object ajc$around$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$1$966e0d9b(AroundClosure ajc$aroundClosure, JoinPoint.StaticPart thisJoinPointStaticPart) {
/* 23 */     long startTimeMs = System.currentTimeMillis();
/*    */     
/* 25 */     Object result = ajc$around$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$1$966e0d9bproceed(ajc$aroundClosure);
/*    */     
/* 27 */     long endTimeMs = System.currentTimeMillis();
/* 28 */     long execTimeMs = endTimeMs - startTimeMs;
/*    */     
/* 30 */     if (execTimeMs > 500L) {
/* 31 */       String name = thisJoinPointStaticPart.getSignature().toString();
/* 32 */       String msg = "Executing " + name + 
/* 33 */         " took too long: " + execTimeMs + " ms.";
/* 34 */       ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().warn(msg);
/*    */     } else {
/* 36 */       String name = thisJoinPointStaticPart.getSignature().toString();
/* 37 */       String msg = "Executing " + name + " took : " + execTimeMs + " ms.";
/* 38 */       ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().debug(msg);
/*    */     } 
/*    */     
/* 41 */     return result;
/*    */   } }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/logging/VsanDpTimingAspect.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */