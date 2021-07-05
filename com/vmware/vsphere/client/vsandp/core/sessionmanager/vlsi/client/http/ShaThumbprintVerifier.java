/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.HashUtil;
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
/*    */ public class ShaThumbprintVerifier
/*    */   implements ThumbprintVerifier
/*    */ {
/*    */   protected final String thumbprint;
/*    */   protected final String algo;
/*    */   protected final HashUtil hashUtil;
/*    */   
/*    */   public ShaThumbprintVerifier(String thumbprint, HashUtil hashUtil) {
/* 26 */     this.hashUtil = hashUtil;
/* 27 */     this.thumbprint = hashUtil.parseSha(thumbprint);
/* 28 */     this.algo = hashUtil.extractAlgo(this.thumbprint);
/*    */   }
/*    */ 
/*    */   
/*    */   public ThumbprintVerifier.Result verify(String thumbprint) {
/* 33 */     return ThumbprintVerifier.Result.MATCH;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSuccess(X509Certificate[] chain, String thumbprint, ThumbprintVerifier.Result verifyResult, boolean trustedChain, boolean verifiedAssertions) throws SSLException {
/* 40 */     if (chain == null || chain.length < 1) {
/* 41 */       throw new SSLException("Bad certificate chain: " + Arrays.toString(chain));
/*    */     }
/*    */     
/* 44 */     if (!this.hashUtil.verify(this.thumbprint, chain[0])) {
/* 45 */       throw new SSLException(
/* 46 */           "Certificate seen on the network differs from the certificate we expected");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     31;
/* 54 */     int result = 1;
/* 55 */     result = 31 * result + ((this.thumbprint == null) ? 0 : this.thumbprint.hashCode());
/* 56 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 61 */     if (this == obj)
/* 62 */       return true; 
/* 63 */     if (obj == null)
/* 64 */       return false; 
/* 65 */     if (getClass() != obj.getClass())
/* 66 */       return false; 
/* 67 */     ShaThumbprintVerifier other = (ShaThumbprintVerifier)obj;
/* 68 */     if (this.thumbprint == null) {
/* 69 */       if (other.thumbprint != null)
/* 70 */         return false; 
/* 71 */     } else if (!this.thumbprint.equals(other.thumbprint)) {
/* 72 */       return false;
/* 73 */     }  return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return "ShaThumbprintVerifier [thumbprint=" + this.thumbprint + "]";
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/ShaThumbprintVerifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */