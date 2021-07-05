/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.vmware.vim.vmomi.core.impl.SslUtil;
/*    */ import java.net.URL;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.HostnameVerifier;
/*    */ import javax.net.ssl.HttpsURLConnection;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import javax.net.ssl.TrustManager;
/*    */ import javax.net.ssl.X509TrustManager;
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
/*    */ public class CertificateUtils
/*    */ {
/*    */   public static String getServerThumbprint(String url) throws Exception {
/* 30 */     X509Certificate cert = getServerCert(url);
/*    */     
/* 32 */     return SslUtil.computeCertificateThumbprint(cert);
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
/*    */   public static X509Certificate getServerCert(String url) throws Exception {
/* 47 */     URL urlAddr = new URL(url);
/*    */     
/* 49 */     TrustManager[] trustAllTrustManager = { new X509TrustManager()
/*    */         {
/*    */           public X509Certificate[] getAcceptedIssuers() {
/* 52 */             return null;
/*    */           }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*    */         } };
/* 66 */     HostnameVerifier trustAllVerifier = new HostnameVerifier()
/*    */       {
/*    */         public boolean verify(String hostname, SSLSession session) {
/* 69 */           return true;
/*    */         }
/*    */       };
/*    */     
/* 73 */     SSLContext sslContext = SSLContext.getInstance("SSL");
/* 74 */     sslContext.init(null, trustAllTrustManager, null);
/*    */     
/* 76 */     HttpsURLConnection con = null;
/* 77 */     con = (HttpsURLConnection)urlAddr.openConnection();
/* 78 */     con.setSSLSocketFactory(sslContext.getSocketFactory());
/* 79 */     con.setHostnameVerifier(trustAllVerifier);
/* 80 */     con.connect();
/*    */     
/*    */     try {
/* 83 */       return (X509Certificate)con.getServerCertificates()[0];
/*    */     } finally {
/* 85 */       con.disconnect();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/CertificateUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */