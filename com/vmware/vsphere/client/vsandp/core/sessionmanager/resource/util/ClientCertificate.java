/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util;
/*     */ 
/*     */ import com.vmware.vim.sso.client.util.codec.Base64;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateFactory;
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
/*     */ 
/*     */ 
/*     */ public class ClientCertificate
/*     */ {
/*     */   protected final String keystorePath;
/*     */   protected final String keystorePass;
/*     */   protected final String keyPass;
/*     */   protected final String keystoreAlias;
/*     */   protected final KeyStore keystore;
/*     */   
/*     */   public ClientCertificate(String keystorePath, String keystorePass, String keyPass, String keystoreType, String keystoreAlias) {
/*  32 */     this.keystorePath = keystorePath;
/*  33 */     this.keystore = load(keystorePath, keystorePass, keystoreType);
/*  34 */     this.keystorePass = keystorePass;
/*  35 */     this.keyPass = keyPass;
/*  36 */     this.keystoreAlias = keystoreAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientCertificate(String keystorePath, KeyStore keystore, String keystorePass, String keyPass, String keystoreAlias) {
/*  41 */     this.keystorePath = keystorePath;
/*  42 */     this.keystore = keystore;
/*  43 */     this.keystorePass = keystorePass;
/*  44 */     this.keyPass = keyPass;
/*  45 */     this.keystoreAlias = keystoreAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientCertificate(String keystorePath, String[] certificates, String keystorePass, String keyPass, String keystoreAlias) {
/*  50 */     this.keystorePath = keystorePath;
/*  51 */     this.keystore = create(certificates);
/*  52 */     this.keystorePass = keystorePass;
/*  53 */     this.keyPass = keyPass;
/*  54 */     this.keystoreAlias = keystoreAlias;
/*     */   }
/*     */   
/*     */   public String getKeystorePath() {
/*  58 */     return this.keystorePath;
/*     */   }
/*     */   
/*     */   public KeyStore getKeystore() {
/*  62 */     return this.keystore;
/*     */   }
/*     */   
/*     */   public String getKeystorePass() {
/*  66 */     return this.keystorePass;
/*     */   }
/*     */   
/*     */   public String getKeyPass() {
/*  70 */     return this.keyPass;
/*     */   }
/*     */   
/*     */   public String getKeystoreAlias() {
/*  74 */     return this.keystoreAlias;
/*     */   }
/*     */ 
/*     */   
/*     */   private static KeyStore load(String keystorePath, String keystorePass, String keystoreType) {
/*  79 */     boolean hasPass = (keystorePass != null && 
/*  80 */       !"".equals(keystorePass.trim()));
/*     */     
/*     */     try {
/*  83 */       KeyStore keyStore = KeyStore.getInstance(keystoreType);
/*  84 */       keyStore.load(new FileInputStream(keystorePath), 
/*  85 */           hasPass ? keystorePass.toCharArray() : null);
/*  86 */       return keyStore;
/*  87 */     } catch (Exception e) {
/*  88 */       CheckedRunnable.handle(e);
/*     */ 
/*     */       
/*  91 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static KeyStore create(String[] certificates) {
/*     */     try {
/*  97 */       CertificateFactory factory = CertificateFactory.getInstance("X.509");
/*  98 */       KeyStore keystore = KeyStore.getInstance("JKS");
/*  99 */       keystore.load(null, null);
/* 100 */       for (int i = 0; i < certificates.length; i++) {
/* 101 */         byte[] decoded = Base64.decodeBase64(certificates[i]
/* 102 */             .replaceAll("-----BEGIN CERTIFICATE-----", "")
/* 103 */             .replaceAll("-----END CERTIFICATE-----", ""));
/* 104 */         ByteArrayInputStream in = new ByteArrayInputStream(decoded);
/* 105 */         Certificate certificate = factory.generateCertificate(in);
/* 106 */         keystore.setEntry("Cert_" + i, new KeyStore.TrustedCertificateEntry(certificate), null);
/*     */       } 
/* 108 */       return keystore;
/* 109 */     } catch (Exception e) {
/* 110 */       CheckedRunnable.handle(e);
/* 111 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     31;
/* 118 */     int result = 1;
/* 119 */     result = 31 * result + ((this.keyPass == null) ? 0 : this.keyPass.hashCode());
/* 120 */     result = 31 * result + (
/* 121 */       (this.keystoreAlias == null) ? 0 : this.keystoreAlias.hashCode());
/* 122 */     result = 31 * result + (
/* 123 */       (this.keystorePass == null) ? 0 : this.keystorePass.hashCode());
/* 124 */     result = 31 * result + (
/* 125 */       (this.keystorePath == null) ? 0 : this.keystorePath.hashCode());
/* 126 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (this == obj)
/* 132 */       return true; 
/* 133 */     if (obj == null)
/* 134 */       return false; 
/* 135 */     if (getClass() != obj.getClass())
/* 136 */       return false; 
/* 137 */     ClientCertificate other = (ClientCertificate)obj;
/* 138 */     if (this.keyPass == null) {
/* 139 */       if (other.keyPass != null)
/* 140 */         return false; 
/* 141 */     } else if (!this.keyPass.equals(other.keyPass)) {
/* 142 */       return false;
/* 143 */     }  if (this.keystoreAlias == null) {
/* 144 */       if (other.keystoreAlias != null)
/* 145 */         return false; 
/* 146 */     } else if (!this.keystoreAlias.equals(other.keystoreAlias)) {
/* 147 */       return false;
/* 148 */     }  if (this.keystorePass == null) {
/* 149 */       if (other.keystorePass != null)
/* 150 */         return false; 
/* 151 */     } else if (!this.keystorePass.equals(other.keystorePass)) {
/* 152 */       return false;
/* 153 */     }  if (this.keystorePath == null) {
/* 154 */       if (other.keystorePath != null)
/* 155 */         return false; 
/* 156 */     } else if (!this.keystorePath.equals(other.keystorePath)) {
/* 157 */       return false;
/* 158 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     return String.format(
/* 164 */         "ClientCertificate [keystorePath=%s, keystorePass=%s, keyPass=%s, keystoreAlias=%s]", new Object[] {
/* 165 */           this.keystorePath, this.keystorePass, this.keyPass, this.keystoreAlias
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/util/ClientCertificate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */