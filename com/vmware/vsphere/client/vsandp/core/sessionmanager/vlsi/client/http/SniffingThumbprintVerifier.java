/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Arrays;
/*    */ import javax.net.ssl.SSLException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class SniffingThumbprintVerifier
/*    */   implements ThumbprintVerifier
/*    */ {
/* 26 */   private static final Logger logger = LoggerFactory.getLogger(SniffingThumbprintVerifier.class);
/*    */   
/*    */   private volatile Certificate sniffedCertificate;
/*    */   private final boolean passVerification;
/*    */   
/*    */   public SniffingThumbprintVerifier() {
/* 32 */     this(false);
/*    */   }
/*    */   
/*    */   public SniffingThumbprintVerifier(boolean passVerification) {
/* 36 */     this.passVerification = passVerification;
/*    */   }
/*    */ 
/*    */   
/*    */   public ThumbprintVerifier.Result verify(String thumbprint) {
/* 41 */     return ThumbprintVerifier.Result.MATCH;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSuccess(X509Certificate[] chain, String thumbprint, ThumbprintVerifier.Result verifyResult, boolean trustedChain, boolean verifiedAssertions) throws SSLException {
/* 47 */     if (chain == null || chain.length < 1) {
/* 48 */       throw new SSLException("Bad certificate chain: " + Arrays.toString(chain));
/*    */     }
/*    */     
/* 51 */     this.sniffedCertificate = chain[0];
/*    */ 
/*    */ 
/*    */     
/* 55 */     if (!this.passVerification) {
/* 56 */       throw new SSLException("Certificate thumbprint verification mismatch");
/*    */     }
/*    */   }
/*    */   
/*    */   public Certificate getSniffedCertificate() {
/* 61 */     if (this.sniffedCertificate == null) {
/* 62 */       logger.error("Sniffed certificate is null. Probably a network call using the sniffing verifier hasn't been made yet?");
/*    */       
/* 64 */       throw new IllegalStateException("Sniffed certificate not available.");
/*    */     } 
/* 66 */     return this.sniffedCertificate;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return String.valueOf(getClass().getName()) + hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/SniffingThumbprintVerifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */