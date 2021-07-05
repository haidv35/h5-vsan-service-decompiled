/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CertUtil;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Arrays;
/*    */ import javax.net.ssl.SSLException;
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
/*    */ public class CertificateMatchVerifier
/*    */   implements ThumbprintVerifier
/*    */ {
/*    */   protected final X509Certificate certificate;
/*    */   
/*    */   public CertificateMatchVerifier(String certificate) {
/* 25 */     this.certificate = CertUtil.parseX509(certificate);
/*    */   }
/*    */   
/*    */   public CertificateMatchVerifier(X509Certificate certificate) {
/* 29 */     this.certificate = certificate;
/*    */   }
/*    */ 
/*    */   
/*    */   public ThumbprintVerifier.Result verify(String thumbprint) {
/* 34 */     return ThumbprintVerifier.Result.MATCH;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSuccess(X509Certificate[] chain, String thumbprint, ThumbprintVerifier.Result verifyResult, boolean trustedChain, boolean verifiedAssertions) throws SSLException {
/* 42 */     if (chain == null || chain.length < 1) {
/* 43 */       throw new SSLException("Bad certificate chain: " + Arrays.toString(chain));
/*    */     }
/*    */     
/* 46 */     if (!this.certificate.equals(chain[0])) {
/* 47 */       throw new SSLException(
/* 48 */           "Certificate seen on the network differs from the certificate we expected");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 55 */     31;
/* 56 */     int result = 1;
/* 57 */     result = 31 * result + ((this.certificate == null) ? 0 : this.certificate.hashCode());
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 63 */     if (this == obj)
/* 64 */       return true; 
/* 65 */     if (obj == null)
/* 66 */       return false; 
/* 67 */     if (getClass() != obj.getClass())
/* 68 */       return false; 
/* 69 */     CertificateMatchVerifier other = (CertificateMatchVerifier)obj;
/* 70 */     if (this.certificate == null) {
/* 71 */       if (other.certificate != null)
/* 72 */         return false; 
/* 73 */     } else if (!this.certificate.equals(other.certificate)) {
/* 74 */       return false;
/* 75 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/util/CertificateMatchVerifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */