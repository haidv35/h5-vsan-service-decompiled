/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*     */ 
/*     */ import com.vmware.vim.binding.sso.version.version2;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.HttpSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.LookupSvcConnection;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.LookupSvcFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.ServiceEndpoint;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.SsoAdminConnection;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.SsoAdminFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.SsoEndpoints;
/*     */ import java.net.URI;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SsoInfo
/*     */ {
/*     */   protected final ResourceFactory<LookupSvcConnection, VlsiSettings> lsFactory;
/*     */   protected final ResourceFactory<SsoAdminConnection, VlsiSettings> ssoFactory;
/*     */   protected final VlsiSettings lsSettings;
/*     */   protected final VlsiSettings ssoSettings;
/*     */   protected final ServiceEndpoint stsEndpoint;
/*     */   protected final ServiceEndpoint ssoAdminEndpoint;
/*     */   protected final X509Certificate[] stsCerts;
/*     */   
/*     */   public SsoInfo(VlsiSettings lsSettings) {
/*  31 */     this((ResourceFactory<LookupSvcConnection, VlsiSettings>)new LookupSvcFactory(), (ResourceFactory<SsoAdminConnection, VlsiSettings>)new SsoAdminFactory(), lsSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SsoInfo(ResourceFactory<LookupSvcConnection, VlsiSettings> lsFactory, ResourceFactory<SsoAdminConnection, VlsiSettings> ssoFactory, VlsiSettings lsSettings) {
/*  38 */     this.lsFactory = lsFactory;
/*  39 */     this.ssoFactory = ssoFactory;
/*  40 */     this.lsSettings = lsSettings;
/*     */     
/*  42 */     Exception exception1 = null, exception2 = null; try { LookupSvcConnection conn = (LookupSvcConnection)lsFactory.acquire(lsSettings); 
/*  43 */       try { SsoEndpoints endpoints = conn.getSsoEndpoints();
/*  44 */         this.stsEndpoint = endpoints.getSts();
/*  45 */         this.ssoAdminEndpoint = endpoints.getAdmin(); }
/*  46 */       finally { if (conn != null) conn.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */     
/*  50 */     exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceFactory<LookupSvcConnection, VlsiSettings> getLsFactory() {
/*  56 */     return this.lsFactory;
/*     */   }
/*     */   
/*     */   public ResourceFactory<SsoAdminConnection, VlsiSettings> getSsoFactory() {
/*  60 */     return this.ssoFactory;
/*     */   }
/*     */   
/*     */   public VlsiSettings getLsSettings() {
/*  64 */     return this.lsSettings;
/*     */   }
/*     */   
/*     */   public VlsiSettings getSsoSettings() {
/*  68 */     return this.ssoSettings;
/*     */   }
/*     */   
/*     */   public ServiceEndpoint getStsEndpoint() {
/*  72 */     return this.stsEndpoint;
/*     */   }
/*     */   
/*     */   public ServiceEndpoint getSsoAdminEndpoint() {
/*  76 */     return this.ssoAdminEndpoint;
/*     */   }
/*     */   
/*     */   public X509Certificate[] getStsCerts() {
/*  80 */     return this.stsCerts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SsoInfo refresh() {
/*  89 */     return new SsoInfo(this.lsFactory, this.ssoFactory, this.lsSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VlsiSettings mkAdminSettings(ServiceEndpoint ssoAdminEndpoint, VlsiSettings lsSettings) {
/*  95 */     URI uri = ssoAdminEndpoint.getUri();
/*  96 */     HttpSettings lsHttpSettings = lsSettings.getHttpSettings();
/*  97 */     HttpSettings httpSettings = new HttpSettings(uri.getScheme(), 
/*  98 */         uri.getHost(), uri.getPort(), uri.getPath(), 
/*  99 */         null, 
/* 100 */         null, 
/* 101 */         -1, 
/* 102 */         lsHttpSettings.getMaxConn(), 
/* 103 */         lsHttpSettings.getTimeout(), 
/* 104 */         null, 
/* 105 */         null, 
/* 106 */         (ThumbprintVerifier)new SingleThumbprintVerifier(ssoAdminEndpoint.getThumbprint()), 
/* 107 */         lsHttpSettings.getExecutorFactory(), 
/* 108 */         lsHttpSettings.getExecutorSettings(), 
/* 109 */         version2.class, 
/* 110 */         lsSettings.getHttpSettings().getVmodlContext(), null);
/*     */     
/* 112 */     return new VlsiSettings(
/* 113 */         lsSettings.getHttpFactory(), httpSettings, new Authenticator(), null);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/SsoInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */