/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.sso.client.util.codec.Base64;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.CertificateFactory;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CertificateHelper
/*    */ {
/* 20 */   protected static CertificateFactory cf = getFactory();
/*    */   
/*    */   private static final String HEX = "0123456789ABCDEF";
/*    */   
/*    */   public static final String MD_ALGO = "SHA-1";
/*    */   
/*    */   protected static CertificateFactory getFactory() {
/*    */     try {
/* 28 */       return CertificateFactory.getInstance("X.509");
/* 29 */     } catch (CertificateException e) {
/* 30 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static X509Certificate pem2cert(String pem) throws CertificateException {
/* 36 */     return (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(
/* 37 */           Base64.decodeBase64(pem)));
/*    */   }
/*    */ 
/*    */   
/*    */   public static X509Certificate[] getCerts(String[] certs) throws CertificateException {
/* 42 */     List<X509Certificate> result = new ArrayList<>(); byte b; int i; String[] arrayOfString;
/* 43 */     for (i = (arrayOfString = certs).length, b = 0; b < i; ) { String pem = arrayOfString[b];
/* 44 */       result.add(pem2cert(pem));
/*    */       b++; }
/*    */     
/* 47 */     return result.<X509Certificate>toArray(new X509Certificate[result.size()]);
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
/*    */   public static String calcThumbprint(byte[] cert) throws NoSuchAlgorithmException {
/* 74 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 75 */     byte[] digest = md.digest(cert);
/*    */     
/* 77 */     StringBuilder thumbprint = new StringBuilder();
/* 78 */     for (int i = 0, len = digest.length; i < len; i++) {
/* 79 */       if (i > 0) {
/* 80 */         thumbprint.append(':');
/*    */       }
/* 82 */       byte b = digest[i];
/* 83 */       thumbprint.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4));
/* 84 */       thumbprint.append("0123456789ABCDEF".charAt(b & 0xF));
/*    */     } 
/* 86 */     return thumbprint.toString();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/CertificateHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */