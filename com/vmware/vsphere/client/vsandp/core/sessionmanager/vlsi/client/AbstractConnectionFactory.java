/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*     */ 
/*     */ import com.vmware.vim.vmomi.client.Client;
/*     */ import com.vmware.vim.vmomi.client.ClientConfiguration;
/*     */ import com.vmware.vim.vmomi.client.common.ProtocolBinding;
/*     */ import com.vmware.vim.vmomi.client.common.Session;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.ClientCfg;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractConnectionFactory<R extends VlsiConnection, S extends VlsiSettings>
/*     */   implements ResourceFactory<R, S>
/*     */ {
/*  28 */   private static Logger logger = LoggerFactory.getLogger(AbstractConnectionFactory.class);
/*     */ 
/*     */   
/*     */   public R acquire(S settings) {
/*  32 */     R result = buildConnection(settings);
/*     */     try {
/*  34 */       onPreConnect(settings, result);
/*     */       
/*  36 */       logger.trace("Opening HTTP connection.");
/*  37 */       result.setClientConfig((ClientCfg)settings.getHttpFactory().acquire(
/*  38 */             settings.getHttpSettings()));
/*     */       
/*  40 */       logger.trace("Initializing VLSI client.");
/*  41 */       result.setClient(makeClient(settings, (VlsiConnection)result));
/*  42 */       onConnect(settings, result);
/*     */       
/*  44 */       logger.trace("Authenticating connection.");
/*  45 */       settings.getAuthenticator().login((VlsiConnection)result);
/*  46 */     } catch (Exception e) {
/*  47 */       result.close();
/*  48 */       CheckedRunnable.handle(e);
/*     */     } 
/*     */     
/*  51 */     logger.debug("Created connection: {}", result);
/*  52 */     return result;
/*     */   }
/*     */   
/*     */   protected void release(S settings, R resource) {
/*     */     try {
/*  57 */       if (settings.getAuthenticator() != null) {
/*  58 */         settings.getAuthenticator().logout((VlsiConnection)resource);
/*     */       }
/*  60 */     } catch (Exception e) {
/*  61 */       logger.warn("Ignoring unsuccessful logout", e);
/*     */     } 
/*     */     
/*     */     try {
/*  65 */       resource.getClient().shutdown();
/*  66 */     } catch (Exception e) {
/*  67 */       logger.warn("Ignoring problem when releasing client", e);
/*     */     } 
/*     */     
/*     */     try {
/*  71 */       if (resource.getClientConfig() != null) {
/*  72 */         resource.getClientConfig().close();
/*     */       }
/*  74 */     } catch (Exception e) {
/*  75 */       logger.warn("Ignoring problem when releasing HttpConfig", e);
/*     */     } 
/*  77 */     logger.debug("Closed connection: {}", resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPreConnect(final S settings, final R connection) {
/*  92 */     connection.setCloseHandler(new Runnable()
/*     */         {
/*     */           public void run() {
/*  95 */             AbstractConnectionFactory.this.release(settings, connection);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void onConnect(S settings, R connection) {
/* 101 */     ((VlsiConnection)connection).settings = (VlsiSettings)settings;
/*     */   }
/*     */   
/*     */   protected Client makeClient(S settings, VlsiConnection connection) {
/* 105 */     Client client = Client.Factory.createClient(
/* 106 */         settings.getHttpSettings().makeUri(), 
/* 107 */         settings.getHttpSettings().getVersion(), 
/* 108 */         settings.getHttpSettings().getVmodlContext(), 
/* 109 */         (ClientConfiguration)connection.getClientConfig().getClientConfig());
/*     */     
/* 111 */     if (settings.getSessionCookie() != null) {
/* 112 */       ProtocolBinding binding = client.getBinding();
/* 113 */       Session session = binding.createSession(settings.getSessionCookie());
/* 114 */       binding.setSession(session);
/*     */     } 
/*     */     
/* 117 */     return client;
/*     */   }
/*     */   
/*     */   protected abstract R buildConnection(S paramS);
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/AbstractConnectionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */