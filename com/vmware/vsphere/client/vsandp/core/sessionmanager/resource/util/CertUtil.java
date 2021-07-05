/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util;
/*    */ 
/*    */ import com.vmware.vim.sso.client.util.codec.Base64;
/*    */ import java.security.KeyStore;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.X509Certificate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CertUtil
/*    */ {
/*    */   public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
/*    */   public static final String END_CERT = "-----END CERTIFICATE-----";
/*    */   
/*    */   public static KeyStore create(String... certificates) {
/*    */     try {
/* 21 */       KeyStore keystore = KeyStore.getInstance("JKS");
/* 22 */       keystore.load(null, null);
/* 23 */       for (int i = 0; i < certificates.length; i++) {
/* 24 */         Certificate certificate = parseX509(certificates[i]);
/* 25 */         keystore.setEntry(
/* 26 */             "Cert_" + i, 
/* 27 */             new KeyStore.TrustedCertificateEntry(certificate), null);
/*    */       } 
/* 29 */       return keystore;
/* 30 */     } catch (RuntimeException e) {
/* 31 */       throw e;
/* 32 */     } catch (Exception e) {
/* 33 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String extractCert(String cert) {
/* 38 */     int idx = cert.indexOf("-----BEGIN CERTIFICATE-----");
/* 39 */     if (idx >= 0) {
/* 40 */       cert = cert.substring(idx + "-----BEGIN CERTIFICATE-----".length());
/*    */     }
/*    */     
/* 43 */     idx = cert.indexOf("-----END CERTIFICATE-----");
/* 44 */     if (idx >= 0) {
/* 45 */       cert = cert.substring(0, idx);
/*    */     }
/*    */     
/* 48 */     cert = cert.trim().replace("\n", "").replace("\r", "");
/*    */     
/* 50 */     return cert;
/*    */   }
/*    */   
/*    */   public static X509Certificate parseX509(String cert) {
/* 54 */     byte[] decoded = Base64.decodeBase64(extractCert(cert)); 
/* 55 */     try { Exception exception1 = null, exception2 = null; 
/*    */       try {  }
/*    */       finally
/* 58 */       { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }  }  } catch (RuntimeException e)
/* 59 */     { throw e; }
/* 60 */     catch (Exception e)
/* 61 */     { throw new RuntimeException(e); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/util/CertUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */