/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.cert.CertificateEncodingException;
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
/*    */ public class ServiceEndpoint
/*    */ {
/*    */   protected final URI uri;
/*    */   protected final X509Certificate[] certs;
/*    */   protected final String thumbprint;
/*    */   
/*    */   public ServiceEndpoint(URI uri, X509Certificate[] certs) {
/* 26 */     this.uri = uri;
/* 27 */     this.certs = certs;
/* 28 */     this.thumbprint = calcThumbprint();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X509Certificate[] getCerts() {
/* 38 */     return this.certs;
/*    */   }
/*    */   
/*    */   public URI getUri() {
/* 42 */     return this.uri;
/*    */   }
/*    */   
/*    */   public URL getUrl() throws MalformedURLException {
/* 46 */     return this.uri.toURL();
/*    */   }
/*    */   
/*    */   public String getThumbprint() {
/* 50 */     return this.thumbprint;
/*    */   }
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
/*    */   protected String calcThumbprint() {
/* 63 */     if (this.certs == null || this.certs.length < 1) {
/* 64 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 68 */       return CertificateHelper.calcThumbprint(this.certs[0].getEncoded());
/* 69 */     } catch (CertificateEncodingException e) {
/* 70 */       throw new SsoException("Could not decode certificate", e);
/* 71 */     } catch (NoSuchAlgorithmException e) {
/* 72 */       throw new SsoException("Invalid certificate algorithm", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/ServiceEndpoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */