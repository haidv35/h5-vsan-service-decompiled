/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vim.vmomi.client.http.HttpClientConfiguration;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
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
/*    */ public class ClientCfg
/*    */   extends Resource
/*    */ {
/*    */   protected HttpClientConfiguration clientConfig;
/*    */   protected Client extraClient;
/*    */   
/*    */   public ClientCfg(HttpClientConfiguration clientConfig, Client extraClient) {
/* 26 */     this.clientConfig = clientConfig;
/* 27 */     this.extraClient = extraClient;
/*    */   }
/*    */   
/*    */   public HttpClientConfiguration getClientConfig() {
/* 31 */     return this.clientConfig;
/*    */   }
/*    */   
/*    */   public void setClientConfig(HttpClientConfiguration clientConfig) {
/* 35 */     this.clientConfig = clientConfig;
/*    */   }
/*    */   
/*    */   public Client getExtraClient() {
/* 39 */     return this.extraClient;
/*    */   }
/*    */   
/*    */   public void setExtraClient(Client extraClient) {
/* 43 */     this.extraClient = extraClient;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/ClientCfg.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */