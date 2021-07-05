/*    */ package com.vmware.vsphere.client.vsan.base.util;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*    */ import com.vmware.vsphere.client.vsan.base.service.VsanServiceFactoryBase;
/*    */ import com.vmware.vsphere.client.vsan.base.service.VsphereHealthService;
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
/*    */ public class VsphereHealthProviderUtils
/*    */ {
/*    */   private static VsanServiceFactoryBase<VsphereHealthService> _vsphereHealthServiceFactory;
/*    */   
/*    */   public static void setVsphereHealthServiceFactory(VsanServiceFactoryBase<VsphereHealthService> factory) {
/* 22 */     _vsphereHealthServiceFactory = factory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static VsanVcClusterHealthSystem getVsphereHealthSystem(ManagedObjectReference moRef) throws Exception {
/* 28 */     VsphereHealthService vsphereHealthService = 
/* 29 */       (VsphereHealthService)_vsphereHealthServiceFactory.getService(moRef.getServerGuid());
/*    */     
/* 31 */     if (vsphereHealthService == null) {
/* 32 */       return null;
/*    */     }
/* 34 */     return vsphereHealthService.getVsphereHealthSystem();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/VsphereHealthProviderUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */