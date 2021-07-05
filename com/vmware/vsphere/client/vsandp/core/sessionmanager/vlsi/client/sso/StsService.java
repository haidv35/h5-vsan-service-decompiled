/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.sso.client.DefaultSecurityTokenServiceFactory;
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import com.vmware.vim.sso.client.SecurityTokenService;
/*    */ import com.vmware.vim.sso.client.SecurityTokenServiceConfig;
/*    */ import com.vmware.vim.sso.client.TokenSpec;
/*    */ import java.net.MalformedURLException;
/*    */ import java.security.cert.X509Certificate;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StsService
/*    */ {
/*    */   protected final ServiceEndpoint endpoint;
/*    */   protected final SecurityTokenService stsClient;
/*    */   protected final int DEFAULT_TOKEN_LIFETIME = 600;
/*    */   
/*    */   public StsService(ServiceEndpoint endpoint, X509Certificate[] signingCerts) {
/* 40 */     this(endpoint, signingCerts, null);
/*    */   }
/*    */   
/*    */   protected StsService(ServiceEndpoint endpoint, X509Certificate[] signingCerts, SecurityTokenServiceConfig.HolderOfKeyConfig keyCfg) {
/*    */     SecurityTokenServiceConfig.ConnectionConfig connConfig;
/*    */     this.DEFAULT_TOKEN_LIFETIME = 600;
/* 46 */     this.endpoint = endpoint;
/*    */ 
/*    */     
/*    */     try {
/* 50 */       connConfig = new SecurityTokenServiceConfig.ConnectionConfig(endpoint.getUrl(), endpoint.getCerts(), 
/* 51 */           null, null);
/* 52 */     } catch (MalformedURLException mue) {
/* 53 */       throw SsoException.toSsoEx(mue);
/*    */     } 
/*    */     
/* 56 */     SecurityTokenServiceConfig config = new SecurityTokenServiceConfig(
/* 57 */         connConfig, signingCerts, null, keyCfg);
/*    */     
/* 59 */     this.stsClient = 
/* 60 */       DefaultSecurityTokenServiceFactory.getSecurityTokenService(config);
/*    */   }
/*    */   
/*    */   public ServiceEndpoint getEndpoint() {
/* 64 */     return this.endpoint;
/*    */   }
/*    */   
/*    */   public SecurityTokenService getStsClient() {
/* 68 */     return this.stsClient;
/*    */   }
/*    */   
/*    */   public TokenSpec getDefaultTokenSpec() {
/* 72 */     return (new TokenSpec.Builder(600L)).createTokenSpec();
/*    */   }
/*    */   
/*    */   public SamlToken acquireBearer(String user, String password) {
/* 76 */     return acquireBearerToken(user, password, getDefaultTokenSpec());
/*    */   }
/*    */ 
/*    */   
/*    */   public SamlToken acquireBearerToken(String user, String password, TokenSpec tokenSpec) {
/*    */     try {
/* 82 */       return this.stsClient.acquireToken(user, password, tokenSpec);
/* 83 */     } catch (Exception e) {
/* 84 */       throw SsoException.toSsoEx(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/StsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */