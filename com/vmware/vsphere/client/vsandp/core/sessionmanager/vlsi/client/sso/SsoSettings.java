/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import java.security.cert.X509Certificate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SsoSettings
/*    */ {
/*    */   protected final SsoEndpoints endpoints;
/*    */   protected final X509Certificate[] signingCerts;
/*    */   
/*    */   public SsoSettings(SsoEndpoints endpoints, X509Certificate[] signingCerts) {
/* 14 */     this.endpoints = endpoints;
/* 15 */     this.signingCerts = signingCerts;
/*    */   }
/*    */   
/*    */   public SsoEndpoints getEndpoints() {
/* 19 */     return this.endpoints;
/*    */   }
/*    */   
/*    */   public X509Certificate[] getSigningCerts() {
/* 23 */     return this.signingCerts;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */