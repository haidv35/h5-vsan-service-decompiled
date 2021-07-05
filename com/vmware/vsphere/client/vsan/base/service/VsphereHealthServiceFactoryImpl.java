/*    */ package com.vmware.vsphere.client.vsan.base.service;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vim.vmomi.core.RequestContext;
/*    */ import com.vmware.vim.vsan.binding.vsan.version.version6;
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
/*    */ public class VsphereHealthServiceFactoryImpl
/*    */   extends VsanServiceFactoryBase<VsphereHealthService>
/*    */ {
/*    */   private static final String VSPHERE_HEALTH_SERVICE_SUBDIR = "/analytics/cloudhealth/sdk";
/*    */   
/*    */   protected VsphereHealthService create(String vcGuid) {
/* 23 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 25 */       Thread.currentThread().setContextClassLoader(VsphereHealthServiceFactoryImpl.class.getClassLoader());
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 30 */       Class<?> vmodlVersion = version6.class;
/* 31 */       Client client = 
/* 32 */         createClient(vcGuid, "/analytics/cloudhealth/sdk", vmodlVersion);
/*    */       
/* 34 */       RequestContext sessionContext = prepareSessionContext(vcGuid, client);
/*    */       
/* 36 */       VsphereHealthServiceImpl vsphereHealthService = new VsphereHealthServiceImpl(
/* 37 */           client, 
/* 38 */           getBundleActivator().getVmodlContext().getVmodlTypeMap(), 
/* 39 */           sessionContext);
/*    */       
/* 41 */       return vsphereHealthService;
/*    */     } finally {
/* 43 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void destroy(VsphereHealthService service) {
/* 49 */     service.logout();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsphereHealthServiceFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */