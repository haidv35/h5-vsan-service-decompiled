/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashUtil
/*     */ {
/*     */   public final String defaultAlgo;
/*     */   public final List<String> algos;
/*     */   public static final String SHA1_ALGO = "SHA-1";
/*     */   public static final String SHA256_ALGO = "SHA-256";
/*     */   public static final String SHA384_ALGO = "SHA-384";
/*     */   public static final String SHA512_ALGO = "SHA-512";
/*     */   
/*     */   public HashUtil() {
/*  26 */     this("SHA-256", Arrays.asList(new String[] { "SHA-1", "SHA-256", "SHA-384", "SHA-512" }));
/*     */   }
/*     */   public HashUtil(String defaultAlgo, List<String> algos) {
/*  29 */     this.defaultAlgo = defaultAlgo;
/*  30 */     this.algos = Collections.unmodifiableList(algos);
/*     */   }
/*     */   
/*     */   public String sha(byte[] data, String algo) {
/*     */     MessageDigest md;
/*     */     try {
/*  36 */       md = MessageDigest.getInstance(algo);
/*  37 */     } catch (NoSuchAlgorithmException e) {
/*  38 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/*  41 */     md.update(data);
/*  42 */     byte[] hash = md.digest();
/*     */     
/*  44 */     StringBuilder sb = new StringBuilder(String.valueOf(algo) + ":");
/*  45 */     for (int i = 0; i < hash.length; i++) {
/*  46 */       String hex = Integer.toHexString(0xFF & hash[i]);
/*  47 */       if (hex.length() == 1) {
/*  48 */         sb.append('0');
/*     */       }
/*  50 */       sb.append(hex);
/*  51 */       if (i < hash.length - 1) {
/*  52 */         sb.append(':');
/*     */       }
/*     */     } 
/*     */     
/*  56 */     return parseSha(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String sha256(byte[] data) {
/*  67 */     return sha(data, "SHA-256");
/*     */   }
/*     */   
/*     */   public String parseSha(String sha) {
/*  71 */     sha = sha.toUpperCase();
/*     */     
/*  73 */     boolean hasAlgo = false;
/*  74 */     for (String algo : this.algos) {
/*  75 */       if (sha.startsWith(algo)) {
/*  76 */         hasAlgo = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  81 */     if (!hasAlgo) {
/*  82 */       sha = String.valueOf(this.defaultAlgo) + ":" + sha;
/*     */     }
/*     */     
/*  85 */     validateSha(sha);
/*     */     
/*  87 */     return sha;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateSha(String sha) {}
/*     */ 
/*     */   
/*     */   public String extractAlgo(String sha) {
/*  95 */     String[] terms = sha.split(":");
/*  96 */     if (terms.length < 1) {
/*  97 */       throw new IllegalArgumentException(
/*  98 */           "Algorithm prefix missing from checksum: " + sha);
/*     */     }
/*     */     
/* 101 */     if (!this.algos.contains(terms[0])) {
/* 102 */       throw new IllegalArgumentException(
/* 103 */           "Unknown checksum algorithm: " + terms[0]);
/*     */     }
/*     */     
/* 106 */     return terms[0];
/*     */   }
/*     */   public boolean verify(String sha, X509Certificate cert) {
/*     */     String computedSha;
/* 110 */     sha = parseSha(sha);
/*     */     
/* 112 */     String algo = extractAlgo(sha);
/*     */     
/*     */     try {
/* 115 */       computedSha = sha(cert.getEncoded(), algo);
/* 116 */     } catch (CertificateEncodingException certificateEncodingException) {
/* 117 */       throw new IllegalArgumentException("Invalid certificate");
/*     */     } 
/*     */     
/* 120 */     return sha.equals(computedSha);
/*     */   }
/*     */   
/*     */   public String withoutAlgo(String sha) {
/* 124 */     validateSha(sha);
/* 125 */     String algo = extractAlgo(sha);
/* 126 */     return sha.substring(algo.length() + 1);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/util/HashUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */