/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.cert.X509Certificate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Token
/*    */ {
/*    */   protected final PrivateKey key;
/*    */   protected final X509Certificate cert;
/*    */   protected final SamlToken saml;
/*    */   
/*    */   public Token(PrivateKey key, X509Certificate cert, SamlToken saml) {
/* 18 */     this.key = key;
/* 19 */     this.cert = cert;
/* 20 */     this.saml = saml;
/*    */   }
/*    */   
/*    */   public PrivateKey getKey() {
/* 24 */     return this.key;
/*    */   }
/*    */   
/*    */   public X509Certificate getCert() {
/* 28 */     return this.cert;
/*    */   }
/*    */   
/*    */   public SamlToken getSaml() {
/* 32 */     return this.saml;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/Token.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */