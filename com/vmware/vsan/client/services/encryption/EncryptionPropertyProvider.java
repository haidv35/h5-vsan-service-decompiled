/*    */ package com.vmware.vsan.client.services.encryption;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.services.common.PermissionService;
/*    */ import com.vmware.vsphere.client.vsan.data.KmipClusterData;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class EncryptionPropertyProvider
/*    */ {
/*    */   @Autowired
/*    */   private VcClient _vcClient;
/*    */   @Autowired
/*    */   private PermissionService permissionService;
/*    */   
/*    */   @TsService
/*    */   public KmipClusterData getKmipClusterData(ManagedObjectReference clusterRef) throws Exception {
/* 27 */     KmipClusterData result = new KmipClusterData();
/*    */ 
/*    */     
/* 30 */     result.hasManageKeyServersPermissions = this.permissionService.hasVcPermissions(
/* 31 */         clusterRef, new String[] { "Cryptographer.ManageKeyServers" });
/*    */     
/* 33 */     if (!result.hasManageKeyServersPermissions) {
/* 34 */       return result;
/*    */     }
/*    */     
/* 37 */     Exception exception1 = null, exception2 = null;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public boolean getEncryptionPermissions(ManagedObjectReference clusterRef) throws Exception {
/* 59 */     return this.permissionService.hasPermissions(clusterRef, 
/* 60 */         new String[] { "Cryptographer.ManageKeys", "Cryptographer.ManageEncryptionPolicy", "Cryptographer.ManageKeyServers" });
/*    */   }
/*    */   
/*    */   @TsService
/*    */   public boolean getReKeyPermissions(ManagedObjectReference clusterRef) throws Exception {
/* 65 */     return this.permissionService.hasPermissions(clusterRef, 
/* 66 */         new String[] { "Cryptographer.ManageKeys" });
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/encryption/EncryptionPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */