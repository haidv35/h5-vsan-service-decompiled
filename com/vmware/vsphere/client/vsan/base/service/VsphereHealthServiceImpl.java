/*    */ package com.vmware.vsphere.client.vsan.base.service;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObject;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vim.vmomi.core.RequestContext;
/*    */ import com.vmware.vim.vmomi.core.Stub;
/*    */ import com.vmware.vim.vmomi.core.types.VmodlType;
/*    */ import com.vmware.vim.vmomi.core.types.VmodlTypeMap;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsphereHealthServiceImpl
/*    */   implements VsphereHealthService
/*    */ {
/* 19 */   private static final Log _logger = LogFactory.getLog(VsphereHealthServiceImpl.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   private static final ManagedObjectReference VSPHERE_HEALTH_SYSTEM_MO_REF = new ManagedObjectReference(
/* 26 */       "VsanVcClusterHealthSystem", "cloud-health", null);
/*    */   
/*    */   private final Client _vmomiClient;
/*    */   
/*    */   private final VmodlTypeMap _vmodlTypeMap;
/*    */   
/*    */   private final RequestContext _sessionContext;
/*    */   
/*    */   private VsanVcClusterHealthSystem _vsanVcClusterHealthSystem;
/*    */   
/*    */   public VsphereHealthServiceImpl(Client vmomiClient, VmodlTypeMap vmodlTypeMap, RequestContext sessionContext) {
/* 37 */     this._vmomiClient = vmomiClient;
/* 38 */     this._vmodlTypeMap = vmodlTypeMap;
/* 39 */     this._sessionContext = sessionContext;
/*    */   }
/*    */ 
/*    */   
/*    */   public VsanVcClusterHealthSystem getVsphereHealthSystem() {
/* 44 */     if (this._vsanVcClusterHealthSystem == null) {
/* 45 */       this._vsanVcClusterHealthSystem = createManagedObject(VSPHERE_HEALTH_SYSTEM_MO_REF);
/*    */     }
/* 47 */     return this._vsanVcClusterHealthSystem;
/*    */   }
/*    */ 
/*    */   
/*    */   public void logout() {
/*    */     try {
/* 53 */       if (this._vmomiClient != null) {
/* 54 */         this._vmomiClient.shutdown();
/*    */       }
/* 56 */     } catch (Exception ex) {
/* 57 */       _logger.error("Failed to shutdown vlsi client: " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <T extends ManagedObject> T createManagedObject(ManagedObjectReference moRef) {
/* 70 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 72 */       Thread.currentThread().setContextClassLoader(
/* 73 */           VsphereHealthServiceImpl.class.getClassLoader());
/*    */       
/* 75 */       VmodlType vmodlType = this._vmodlTypeMap.getVmodlType(moRef.getType());
/* 76 */       Class<T> typeClass = vmodlType.getTypeClass();
/*    */       
/* 78 */       ManagedObject managedObject = this._vmomiClient.createStub(typeClass, moRef);
/*    */ 
/*    */       
/* 81 */       ((Stub)managedObject)._setRequestContext(this._sessionContext);
/* 82 */       return (T)managedObject;
/*    */     } finally {
/* 84 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsphereHealthServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */