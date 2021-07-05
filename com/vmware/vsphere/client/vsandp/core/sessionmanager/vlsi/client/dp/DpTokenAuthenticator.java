/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;
/*     */ 
/*     */ import com.vmware.vim.binding.sso.version.version1_5;
/*     */ import com.vmware.vim.sso.client.SamlToken;
/*     */ import com.vmware.vim.sso.client.exception.InvalidTokenException;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vim.vmomi.core.RequestContext;
/*     */ import com.vmware.vim.vmomi.core.Stub;
/*     */ import com.vmware.vim.vmomi.core.impl.RequestContextImpl;
/*     */ import com.vmware.vim.vmomi.core.security.SignInfo;
/*     */ import com.vmware.vim.vmomi.core.security.impl.SignInfoImpl;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.dps.SessionManager;
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.LookupSvcLocator;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.Authenticator;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.HttpSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.LookupSvcConnection;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.ServiceEndpoint;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.SsoAdminFactory;
/*     */ import java.net.URI;
/*     */ import java.security.PrivateKey;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ public final class DpTokenAuthenticator
/*     */   extends Authenticator
/*     */ {
/*  33 */   private static final Log logger = LogFactory.getLog(DpTokenAuthenticator.class);
/*     */   
/*     */   private final UserSessionService userSessionService;
/*     */   
/*     */   private final LookupSvcLocator lsLocator;
/*     */   
/*     */   private final SsoAdminFactory ssoAdminFactory;
/*     */   
/*     */   private final ResourceFactory<LookupSvcConnection, VlsiSettings> lsFactory;
/*     */   
/*     */   private final VlsiSettings lsSettings;
/*     */ 
/*     */   
/*     */   public DpTokenAuthenticator(UserSessionService userSessionService, LookupSvcLocator lsLocator, SsoAdminFactory ssoAdminFactory, ResourceFactory<LookupSvcConnection, VlsiSettings> lsFactory, VlsiSettings lsSettings) {
/*  47 */     this.userSessionService = userSessionService;
/*  48 */     this.lsLocator = lsLocator;
/*  49 */     this.ssoAdminFactory = ssoAdminFactory;
/*  50 */     this.lsFactory = lsFactory;
/*  51 */     this.lsSettings = lsSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void login(VlsiConnection connection) {
/*  56 */     doLogin((DpConnection)connection);
/*     */   }
/*     */   private void doLogin(DpConnection connection) {
/*  59 */     String samlTokenXml = (this.userSessionService.getUserSession()).samlTokenXml;
/*  60 */     PrivateKey privateKey = this.lsLocator.getPrivateKey();
/*  61 */     SamlToken hokToken = null;
/*     */     try {
/*  63 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  73 */     catch (InvalidTokenException e) {
/*  74 */       throw new IllegalStateException("Failed to deserialize token!", e);
/*     */     } 
/*     */     
/*  77 */     SignInfoImpl signInfoImpl = new SignInfoImpl(privateKey, hokToken);
/*     */     
/*  79 */     RequestContextImpl sessionContext = new RequestContextImpl();
/*  80 */     sessionContext.setSignInfo((SignInfo)signInfoImpl);
/*     */     
/*     */     try {
/*  83 */       SessionManager sessionManager = connection.getSessionManager();
/*  84 */       ((Stub)sessionManager)._setRequestContext((RequestContext)sessionContext);
/*  85 */       sessionManager.loginByToken(null);
/*  86 */       logger.info("Authenticated " + connection + ", token expiring: " + hokToken.getExpirationTime());
/*  87 */     } catch (Exception e) {
/*  88 */       logger.error("Failed to login with token: " + hokToken, e);
/*  89 */       throw new RuntimeException("Failed to login", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void logout(VlsiConnection connection) {
/*  95 */     doLogout((DpConnection)connection);
/*     */   }
/*     */   
/*     */   private void doLogout(DpConnection connection) {
/*  99 */     connection.getSessionManager().logout();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 103 */     return DpTokenAuthenticator.class.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 108 */     return o instanceof DpTokenAuthenticator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static VlsiSettings createSsoAdminSettings(ServiceEndpoint ssoAdminEndpoint, VlsiSettings lsSettings) {
/* 114 */     URI uri = ssoAdminEndpoint.getUri();
/* 115 */     HttpSettings lsHttpSettings = lsSettings.getHttpSettings();
/* 116 */     HttpSettings httpSettings = new HttpSettings(uri.getScheme(), 
/* 117 */         uri.getHost(), uri.getPort(), uri.getPath(), 
/* 118 */         null, 
/* 119 */         null, 
/* 120 */         -1, 
/* 121 */         lsHttpSettings.getMaxConn(), 
/* 122 */         lsHttpSettings.getTimeout(), 
/* 123 */         null, 
/* 124 */         null, 
/* 125 */         (ThumbprintVerifier)new SingleThumbprintVerifier(ssoAdminEndpoint.getThumbprint()), 
/* 126 */         lsHttpSettings.getExecutorFactory(), 
/* 127 */         lsHttpSettings.getExecutorSettings(), 
/* 128 */         version1_5.class, 
/* 129 */         lsSettings.getHttpSettings().getVmodlContext(), 
/* 130 */         null);
/*     */     
/* 132 */     return new VlsiSettings(lsSettings.getHttpFactory(), httpSettings, new Authenticator(), null);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpTokenAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */