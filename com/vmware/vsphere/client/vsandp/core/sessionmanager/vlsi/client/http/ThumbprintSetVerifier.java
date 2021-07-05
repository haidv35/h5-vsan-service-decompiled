/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.net.ssl.SSLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThumbprintSetVerifier
/*    */   implements ThumbprintVerifier
/*    */ {
/*    */   protected final Set<String> thumbprints;
/*    */   
/*    */   public ThumbprintSetVerifier(Set<String> thumbprints) {
/* 21 */     this.thumbprints = thumbprints;
/*    */   }
/*    */   
/*    */   public ThumbprintSetVerifier(String... strings) {
/* 25 */     this.thumbprints = new HashSet<>(); byte b; int i; String[] arrayOfString;
/* 26 */     for (i = (arrayOfString = strings).length, b = 0; b < i; ) { String thumbprint = arrayOfString[b];
/* 27 */       this.thumbprints.add(thumbprint.toLowerCase());
/*    */       b++; }
/*    */   
/*    */   }
/*    */   
/*    */   public ThumbprintVerifier.Result verify(String thumbprint) {
/* 33 */     if (this.thumbprints.contains(thumbprint.toLowerCase())) {
/* 34 */       return ThumbprintVerifier.Result.MATCH;
/*    */     }
/*    */     
/* 37 */     return ThumbprintVerifier.Result.MISMATCH;
/*    */   }
/*    */   
/*    */   public void onSuccess(X509Certificate[] chain, String thumbprint, ThumbprintVerifier.Result verifyResult, boolean trustedChain, boolean verifiedAssertions) throws SSLException {}
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/ThumbprintSetVerifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */