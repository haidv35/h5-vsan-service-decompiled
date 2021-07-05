/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.ClientCfg;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util.MoRef;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util.RequestContextUtil;
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
/*    */ public class VlsiConnection
/*    */   extends Resource
/*    */ {
/*    */   protected Client client;
/*    */   protected ClientCfg clientCfg;
/*    */   protected VlsiSettings settings;
/*    */   
/*    */   protected Client getClient() {
/* 32 */     return this.client;
/*    */   }
/*    */   
/*    */   protected void setClient(Client client) {
/* 36 */     this.client = client;
/*    */   }
/*    */   
/*    */   public ClientCfg getClientConfig() {
/* 40 */     return this.clientCfg;
/*    */   }
/*    */   
/*    */   public void setClientConfig(ClientCfg clientCfg) {
/* 44 */     this.clientCfg = clientCfg;
/*    */   }
/*    */   
/*    */   public VlsiSettings getSettings() {
/* 48 */     return this.settings;
/*    */   }
/*    */   
/*    */   public <T extends com.vmware.vim.binding.vmodl.ManagedObject> T createStub(Class<T> clazz, String moId) {
/* 52 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 54 */       Thread.currentThread().setContextClassLoader(VlsiConnection.class.getClassLoader());
/* 55 */       return (T)RequestContextUtil.withOperationId(this.client.createStub(clazz, (ManagedObjectReference)new MoRef(clazz, moId)));
/*    */     } finally {
/* 57 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends com.vmware.vim.binding.vmodl.ManagedObject> T createStub(Class<T> clazz, ManagedObjectReference moRef) {
/* 63 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 65 */       Thread.currentThread().setContextClassLoader(VlsiConnection.class.getClassLoader());
/* 66 */       return (T)RequestContextUtil.withOperationId(this.client.createStub(clazz, moRef));
/*    */     } finally {
/* 68 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     String connectionType = getClass().getSimpleName();
/* 75 */     String host = (this.settings != null) ? this.settings.getHttpSettings().getHost() : "initializing";
/* 76 */     return String.format("%s(%s)", new Object[] { connectionType, host });
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/VlsiConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */