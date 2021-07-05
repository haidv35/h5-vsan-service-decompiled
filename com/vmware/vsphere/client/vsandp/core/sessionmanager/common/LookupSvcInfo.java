/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*     */ 
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*     */ import java.net.URI;
/*     */ import java.security.KeyStore;
/*     */ 
/*     */ 
/*     */ public class LookupSvcInfo
/*     */ {
/*     */   private final URI address;
/*     */   private final String thumbprint;
/*     */   private final ThumbprintVerifier thumbprintVerifier;
/*     */   private final KeyStore keyStore;
/*     */   
/*     */   public static LookupSvcInfo from(LsParams lsParams) {
/*  17 */     if (lsParams == null) {
/*  18 */       return null;
/*     */     }
/*  20 */     return lsParams.toLsInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupSvcInfo(URI address, String thumbprint) {
/*  29 */     this(address, thumbprint, null, null);
/*     */   }
/*     */   
/*     */   public LookupSvcInfo(URI address, KeyStore keyStore) {
/*  33 */     this(address, null, null, keyStore);
/*     */   }
/*     */   
/*     */   public LookupSvcInfo(URI address, ThumbprintVerifier thumbprintVerifier) {
/*  37 */     this(address, null, thumbprintVerifier, null);
/*     */   }
/*     */   
/*     */   private LookupSvcInfo(URI address, String thumbprint, ThumbprintVerifier thumbprintVerifier, KeyStore keyStore) {
/*  41 */     this.address = address;
/*  42 */     this.thumbprint = thumbprint;
/*  43 */     this.thumbprintVerifier = thumbprintVerifier;
/*  44 */     this.keyStore = keyStore;
/*     */   }
/*     */   
/*     */   public URI getAddress() {
/*  48 */     return this.address;
/*     */   }
/*     */   
/*     */   public LookupSvcInfo copyWithAddress(URI address) {
/*  52 */     return new LookupSvcInfo(address, this.thumbprint, this.thumbprintVerifier, this.keyStore);
/*     */   }
/*     */   
/*     */   public String getThumbprint() {
/*  56 */     return this.thumbprint;
/*     */   }
/*     */   
/*     */   public LookupSvcInfo copyWithThumbprint(String thumbprint) {
/*  60 */     return new LookupSvcInfo(this.address, thumbprint, this.thumbprintVerifier, this.keyStore);
/*     */   }
/*     */   
/*     */   public KeyStore getKeyStore() {
/*  64 */     return this.keyStore;
/*     */   }
/*     */   
/*     */   public LookupSvcInfo copyWithKeyStore(KeyStore keyStore) {
/*  68 */     return new LookupSvcInfo(this.address, this.thumbprint, this.thumbprintVerifier, keyStore);
/*     */   }
/*     */   
/*     */   public ThumbprintVerifier getThumbprintVerifier() {
/*  72 */     if (this.thumbprintVerifier != null) {
/*  73 */       return this.thumbprintVerifier;
/*     */     }
/*  75 */     if (this.thumbprint != null) {
/*  76 */       return (ThumbprintVerifier)new SingleThumbprintVerifier(this.thumbprint);
/*     */     }
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public LookupSvcInfo copyWithThumbprintVerifier(ThumbprintVerifier thumbprintVerifier) {
/*  82 */     return new LookupSvcInfo(this.address, this.thumbprint, thumbprintVerifier, this.keyStore);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  87 */     31;
/*  88 */     int result = 1;
/*  89 */     result = 31 * result + ((this.address == null) ? 0 : this.address.hashCode());
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  95 */     if (this == obj)
/*  96 */       return true; 
/*  97 */     if (obj == null)
/*  98 */       return false; 
/*  99 */     if (getClass() != obj.getClass())
/* 100 */       return false; 
/* 101 */     LookupSvcInfo other = (LookupSvcInfo)obj;
/* 102 */     if (this.address == null) {
/* 103 */       if (other.address != null)
/* 104 */         return false; 
/* 105 */     } else if (!this.address.equals(other.address)) {
/* 106 */       return false;
/* 107 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return String.format("LookupSvcInfo [address=%s]", new Object[] { this.address });
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/LookupSvcInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */