/*    */ package com.vmware.vsphere.client.vsan.base.service;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vim.vmomi.core.RequestContext;
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
/*    */ public class VsanServiceFactoryImpl
/*    */   extends VsanServiceFactoryBase<VsanService>
/*    */   implements VsanServiceFactory
/*    */ {
/*    */   private static final String VSAN_HEALTH_SERVICE_SUBDIR = "/vsanHealth";
/*    */   
/*    */   protected VsanService create(String vcGuid) {
/* 25 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 27 */       Thread.currentThread().setContextClassLoader(VsanServiceFactoryImpl.class.getClassLoader());
/*    */       
/* 29 */       Client client = createClient(vcGuid, "/vsanHealth");
/* 30 */       RequestContext sessionContext = prepareSessionContext(vcGuid, client);
/*    */       
/* 32 */       VsanServiceImpl vsanService = new VsanServiceImpl(
/* 33 */           client, 
/* 34 */           getBundleActivator().getVmodlContext().getVmodlTypeMap(), 
/* 35 */           sessionContext, 
/* 36 */           vcGuid);
/*    */       
/* 38 */       return vsanService;
/*    */     } finally {
/* 40 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void destroy(VsanService service) {
/* 46 */     service.logout();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanServiceFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */