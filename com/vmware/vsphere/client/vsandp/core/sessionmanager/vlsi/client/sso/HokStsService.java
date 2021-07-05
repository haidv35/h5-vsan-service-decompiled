/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import com.vmware.vim.sso.client.SecurityTokenServiceConfig;
/*    */ import com.vmware.vim.sso.client.TokenSpec;
/*    */ import java.security.PrivateKey;
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
/*    */ public class HokStsService
/*    */   extends StsService
/*    */ {
/*    */   protected final PrivateKey privateKey;
/*    */   protected final X509Certificate cert;
/*    */   
/*    */   public HokStsService(ServiceEndpoint endpoint, X509Certificate[] signingCerts, PrivateKey privateKey, X509Certificate cert) {
/* 36 */     super(endpoint, signingCerts, new SecurityTokenServiceConfig.HolderOfKeyConfig(privateKey, cert, null));
/* 37 */     this.privateKey = privateKey;
/* 38 */     this.cert = cert;
/*    */   }
/*    */   
/*    */   public PrivateKey getPrivateKey() {
/* 42 */     return this.privateKey;
/*    */   }
/*    */   
/*    */   public X509Certificate getCert() {
/* 46 */     return this.cert;
/*    */   }
/*    */   
/*    */   public SamlToken acquireSolutionToken() {
/* 50 */     return acquireSolutionToken(getDefaultTokenSpec());
/*    */   }
/*    */   
/*    */   public SamlToken acquireSolutionToken(TokenSpec tokenSpec) {
/*    */     try {
/* 55 */       return this.stsClient.acquireTokenByCertificate(tokenSpec);
/* 56 */     } catch (Exception e) {
/* 57 */       throw SsoException.toSsoEx(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/HokStsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */