/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.SSLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleThumbprintVerifier
/*    */   implements ThumbprintVerifier
/*    */ {
/*    */   protected final String thumbprint;
/*    */   
/*    */   public SingleThumbprintVerifier(String thumbprint) {
/* 18 */     this.thumbprint = thumbprint.toLowerCase();
/*    */   }
/*    */ 
/*    */   
/*    */   public ThumbprintVerifier.Result verify(String thumbprint) {
/* 23 */     if (thumbprint.toLowerCase().equals(this.thumbprint)) {
/* 24 */       return ThumbprintVerifier.Result.MATCH;
/*    */     }
/*    */     
/* 27 */     return ThumbprintVerifier.Result.MISMATCH;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onSuccess(X509Certificate[] chain, String thumbprint, ThumbprintVerifier.Result verifyResult, boolean trustedChain, boolean verifiedAssertions) throws SSLException {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 39 */     31;
/* 40 */     int result = 1;
/* 41 */     result = 31 * result + (
/* 42 */       (this.thumbprint == null) ? 0 : this.thumbprint.hashCode());
/* 43 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 48 */     if (this == obj)
/* 49 */       return true; 
/* 50 */     if (obj == null)
/* 51 */       return false; 
/* 52 */     if (getClass() != obj.getClass())
/* 53 */       return false; 
/* 54 */     SingleThumbprintVerifier other = (SingleThumbprintVerifier)obj;
/* 55 */     if (this.thumbprint == null) {
/* 56 */       if (other.thumbprint != null)
/* 57 */         return false; 
/* 58 */     } else if (!this.thumbprint.equals(other.thumbprint)) {
/* 59 */       return false;
/* 60 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/SingleThumbprintVerifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */