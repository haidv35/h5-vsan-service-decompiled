/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NetUtils
/*     */ {
/*     */   public static final String HTTP_GET = "GET";
/*     */   public static final String CONTEXT_SSL = "SSL";
/*     */   
/*     */   public static TrustManager createTrustAllManager() {
/*  36 */     return new X509TrustManager()
/*     */       {
/*     */         public X509Certificate[] getAcceptedIssuers() {
/*  39 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SSLSocketFactory getDisableSSLCertificateCheckingSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
/*  61 */     TrustManager[] trustAllCerts = { createTrustAllManager() };
/*  62 */     SSLContext sc = SSLContext.getInstance("SSL");
/*  63 */     sc.init(null, trustAllCerts, new SecureRandom());
/*  64 */     return sc.getSocketFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostnameVerifier createAllTrustingHostnameVerifier() {
/*  71 */     return new HostnameVerifier()
/*     */       {
/*     */         public boolean verify(String hostname, SSLSession session) {
/*  74 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpsURLConnection createUntrustedConnection(String address) throws KeyManagementException, NoSuchAlgorithmException, MalformedURLException, IOException {
/*  86 */     return createUntrustedConnection(new URL(address));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpsURLConnection createUntrustedConnection(URL url) throws KeyManagementException, NoSuchAlgorithmException, IOException {
/*  95 */     HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
/*  96 */     conn.setSSLSocketFactory(getDisableSSLCertificateCheckingSocketFactory());
/*  97 */     conn.setHostnameVerifier(createAllTrustingHostnameVerifier());
/*  98 */     return conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSuccess(int responseCode) {
/* 105 */     return (responseCode >= 200 && responseCode < 300);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/NetUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */