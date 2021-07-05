/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.Client;
/*    */ import com.vmware.vim.vmomi.client.ClientConfiguration;
/*    */ import com.vmware.vim.vmomi.client.http.HttpClientConfiguration;
/*    */ import com.vmware.vim.vmomi.client.http.HttpConfiguration;
/*    */ import com.vmware.vim.vmomi.core.types.VmodlVersion;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.AbstractConnectionFactory;
/*    */ import java.io.Closeable;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpFactory
/*    */   implements ResourceFactory<ClientCfg, HttpSettings>
/*    */ {
/* 22 */   private static Logger logger = LoggerFactory.getLogger(AbstractConnectionFactory.class);
/*    */ 
/*    */   
/*    */   public ClientCfg acquire(HttpSettings id) {
/* 26 */     HttpConfiguration result = HttpConfiguration.Factory.newInstance();
/* 27 */     if (id.isViaProxy()) {
/* 28 */       result.setDefaultProxy(id.getProxyHost(), id.getProxyPort(), 
/* 29 */           id.getProxyProto());
/*    */     }
/*    */     
/* 32 */     if (id.getMaxConn() > 0) {
/* 33 */       result.setMaxConnections(id.getMaxConn());
/* 34 */       result.setDefaultMaxConnectionsPerRoute(id.getMaxConn());
/*    */     } 
/*    */     
/* 37 */     if (id.getTimeout() > 0) {
/* 38 */       result.setTimeoutMs(id.getTimeout());
/* 39 */       result.setConnectTimeoutMs(id.getTimeout());
/*    */     } 
/*    */     
/* 42 */     if (id.getTrustStore() != null) {
/* 43 */       result.getKeyStoreConfig().setTrustStorePassword(
/* 44 */           id.getTrustStore().getKeystorePass());
/* 45 */       result.getKeyStoreConfig().setKeyStorePath(
/* 46 */           id.getTrustStore().getKeystorePath());
/* 47 */       result.setTrustStore(id.getTrustStore().getKeystore());
/*    */     } 
/*    */     
/* 50 */     if (id.getClientCert() != null) {
/* 51 */       ClientCertificate cert = id.getClientCert();
/* 52 */       result.getKeyStoreConfig().setKeyAlias(cert.getKeystoreAlias());
/* 53 */       result.getKeyStoreConfig().setKeyPassword(cert.getKeyPass());
/* 54 */       result.setKeyStore(cert.getKeystore());
/*    */     } 
/*    */     
/* 57 */     if (id.getThumbprintVerifier() != null) {
/* 58 */       result.setThumbprintVerifier(id.getThumbprintVerifier());
/*    */     }
/*    */     
/* 61 */     HttpClientConfiguration red = 
/* 62 */       HttpClientConfiguration.Factory.newInstance();
/* 63 */     red.setHttpConfiguration(result);
/* 64 */     red.setExecutor((Executor)id.getExecutorFactory().acquire(id.getExecutorSettings()));
/* 65 */     if (id.getRequestProperties() != null) {
/* 66 */       red.setRequestContextProvider(new HttpRequestContextProvider(id.getRequestProperties()));
/*    */     }
/*    */     
/* 69 */     VmodlVersion vmodlVersion = 
/* 70 */       id.getVmodlContext().getVmodlVersionMap().getVersion(id.getVersion());
/* 71 */     Client cl = Client.Factory.createClient(
/* 72 */         id.makeUri(), 
/* 73 */         vmodlVersion.getVersionClass(), 
/* 74 */         id.getVmodlContext(), 
/* 75 */         (ClientConfiguration)red);
/*    */     
/* 77 */     final ClientCfg res = new ClientCfg(red, cl);
/* 78 */     res.setCloseHandler(new Runnable()
/*    */         {
/*    */           public void run() {
/* 81 */             HttpFactory.this.release(res);
/*    */           }
/*    */         });
/* 84 */     return res;
/*    */   }
/*    */   
/*    */   private void release(ClientCfg resource) {
/* 88 */     resource.getExtraClient().shutdown();
/*    */     
/* 90 */     Closeable pool = (Closeable)resource.getClientConfig().getExecutor();
/* 91 */     if (pool != null)
/*    */       try {
/* 93 */         pool.close();
/* 94 */       } catch (Exception e) {
/* 95 */         logger.warn("Ignoring problem when releasing thread pool", e);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/HttpFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */