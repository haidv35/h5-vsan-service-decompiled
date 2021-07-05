/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class Version
/*     */ {
/*  20 */   private static final Log _logger = LogFactory.getLog(Version.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public final int major;
/*     */ 
/*     */   
/*     */   public final int minor;
/*     */ 
/*     */   
/*     */   public final int revision;
/*     */ 
/*     */   
/*     */   public final int buildNumber;
/*     */ 
/*     */ 
/*     */   
/*     */   public Version(String version) {
/*  38 */     if (version == null || version.length() == 0) {
/*     */       
/*  40 */       this.major = 0;
/*  41 */       this.minor = 0;
/*  42 */       this.revision = 0;
/*  43 */       this.buildNumber = 0;
/*     */       
/*     */       return;
/*     */     } 
/*  47 */     String[] parts = version.split("\\.");
/*     */     
/*  49 */     if (parts.length > 4) {
/*  50 */       throw new IllegalArgumentException("Invalid version: " + version);
/*     */     }
/*     */ 
/*     */     
/*  54 */     int major = Integer.parseInt(parts[0]);
/*  55 */     if (major < 0) {
/*  56 */       major = 0;
/*     */     }
/*  58 */     this.major = major;
/*     */     
/*  60 */     int minorValue = 0;
/*  61 */     int revisionValue = 0;
/*  62 */     int buildNumberValue = 0;
/*     */     
/*  64 */     if (parts.length > 1) {
/*     */       
/*  66 */       minorValue = Integer.parseInt(parts[1]);
/*  67 */       if (minorValue < 0) {
/*  68 */         minorValue = 0;
/*     */       }
/*     */       
/*  71 */       if (parts.length > 2) {
/*     */         
/*  73 */         revisionValue = Integer.parseInt(parts[2]);
/*  74 */         if (revisionValue < 0) {
/*  75 */           revisionValue = 0;
/*     */         }
/*     */         
/*  78 */         if (parts.length > 3) {
/*     */           
/*  80 */           buildNumberValue = Integer.parseInt(parts[3]);
/*  81 */           if (buildNumberValue < 0) {
/*  82 */             buildNumberValue = 0;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     this.minor = minorValue;
/*  89 */     this.revision = revisionValue;
/*  90 */     this.buildNumber = buildNumberValue;
/*     */   }
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
/*     */ 
/*     */   
/*     */   public int compareTo(Version value) {
/* 109 */     if (value == null) {
/* 110 */       return 1;
/*     */     }
/*     */     
/* 113 */     if (this.major != value.major) {
/* 114 */       if (this.major > value.major) {
/* 115 */         return 1;
/*     */       }
/* 117 */       return -1;
/*     */     } 
/*     */     
/* 120 */     if (this.minor != value.minor) {
/* 121 */       if (this.minor > value.minor) {
/* 122 */         return 1;
/*     */       }
/* 124 */       return -1;
/*     */     } 
/*     */     
/* 127 */     if (this.revision != value.revision) {
/* 128 */       if (this.revision > value.revision) {
/* 129 */         return 1;
/*     */       }
/* 131 */       return -1;
/*     */     } 
/*     */     
/* 134 */     if (this.buildNumber == value.buildNumber) {
/* 135 */       return 0;
/*     */     }
/*     */     
/* 138 */     if (this.buildNumber > value.buildNumber) {
/* 139 */       return 1;
/*     */     }
/* 141 */     return -1;
/*     */   }
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
/*     */   
/*     */   public static int compare(Version v1, Version v2) {
/* 159 */     if (v1 == null && v2 == null) {
/* 160 */       return 0;
/*     */     }
/*     */     
/* 163 */     if (v1 == null) {
/* 164 */       return -1;
/*     */     }
/*     */     
/* 167 */     if (v2 == null) {
/* 168 */       return 1;
/*     */     }
/*     */     
/* 171 */     return v1.compareTo(v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidVersion(String versionStr) {
/* 179 */     Version version = null;
/*     */     try {
/* 181 */       version = new Version(versionStr);
/* 182 */     } catch (Exception exception) {}
/*     */     
/* 184 */     return (version != null);
/*     */   }
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
/*     */   
/*     */   public static boolean isSupportedVersion(String[] supportedVersions, String version) {
/* 202 */     if (supportedVersions == null) {
/* 203 */       return false;
/*     */     }
/* 205 */     Version v = new Version(version); byte b; int i; String[] arrayOfString;
/* 206 */     for (i = (arrayOfString = supportedVersions).length, b = 0; b < i; ) { String supportedVersion = arrayOfString[b];
/* 207 */       if (matchMajorMinorVersions(supportedVersion, v))
/* 208 */         return true; 
/*     */       b++; }
/*     */     
/* 211 */     return false;
/*     */   }
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
/*     */   private static boolean matchMajorMinorVersions(String supportedVersion, Version v) {
/*     */     try {
/* 229 */       Version sv = new Version(supportedVersion);
/* 230 */       return (sv.major == v.major && sv.minor == v.minor);
/* 231 */     } catch (Exception e) {
/* 232 */       _logger.error("Error when comparing versions", e);
/* 233 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 244 */     return String.format("%s.%s.%s.%s", new Object[] { Integer.valueOf(this.major), Integer.valueOf(this.minor), Integer.valueOf(this.revision), Integer.valueOf(this.buildNumber) });
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/Version.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */