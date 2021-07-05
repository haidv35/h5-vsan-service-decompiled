/*     */ package com.vmware.vsphere.client.vsan.util;
/*     */ 
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ public class FormatUtil
/*     */ {
/*     */   private static UserSessionService _userSessionService;
/*     */   public static final long AUTO = -1L;
/*     */   public static final long B = 1L;
/*     */   public static final long KB = 1024L;
/*     */   public static final long MB = 1048576L;
/*     */   public static final long GB = 1073741824L;
/*     */   public static final long TB = 1099511627776L;
/*     */   
/*     */   public static void setUserSessionService(UserSessionService userSessionService) {
/*  22 */     _userSessionService = userSessionService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStorageFormatted(Long value, long valueUnit, long targetUnit) {
/*  61 */     String formattedString = 
/*  62 */       getDataSizeFormatted(value, 2, valueUnit, targetUnit, true);
/*  63 */     return formattedString;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDataSizeFormatted(Number value, int precision, long valueUnit, long targetUnit, boolean includeUnitLabel) {
/*  90 */     if (value == null || valueUnit == -1L) {
/*  91 */       return null;
/*     */     }
/*     */     
/*  94 */     if (targetUnit != -1L && 
/*  95 */       targetUnit != 1L && 
/*  96 */       targetUnit != 1024L && 
/*  97 */       targetUnit != 1048576L && 
/*  98 */       targetUnit != 1073741824L && 
/*  99 */       targetUnit != 1099511627776L) {
/* 100 */       targetUnit = -1L;
/*     */     }
/*     */     
/* 103 */     BigDecimal inBytes = BigDecimal.valueOf(value.doubleValue())
/* 104 */       .multiply(BigDecimal.valueOf(valueUnit));
/* 105 */     BigDecimal inBytesAbs = inBytes.abs();
/*     */     
/* 107 */     if (targetUnit == -1L) {
/* 108 */       if (inBytesAbs.compareTo(BigDecimal.valueOf(1099511627776L)) >= 0) {
/* 109 */         targetUnit = 1099511627776L;
/* 110 */       } else if (inBytesAbs.compareTo(BigDecimal.valueOf(1073741824L)) >= 0) {
/* 111 */         targetUnit = 1073741824L;
/* 112 */       } else if (inBytesAbs.compareTo(BigDecimal.valueOf(1048576L)) >= 0) {
/* 113 */         targetUnit = 1048576L;
/* 114 */       } else if (inBytesAbs.compareTo(BigDecimal.valueOf(1024L)) >= 0) {
/* 115 */         targetUnit = 1024L;
/*     */       } else {
/* 117 */         targetUnit = 1L;
/*     */       } 
/*     */     }
/*     */     
/* 121 */     BigDecimal targetUnitBD = BigDecimal.valueOf(targetUnit);
/* 122 */     BigDecimal val = inBytes.divide(targetUnitBD, precision, RoundingMode.HALF_UP);
/*     */     
/* 124 */     String formattedNumber = getLocalizedNumber(val);
/* 125 */     String formattedString = String.valueOf(formattedNumber) + (includeUnitLabel ? (" " + getStorageUnit(targetUnit)) : "");
/* 126 */     return formattedString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStorageUnit(long value) {
/* 136 */     if (value == 1024L) {
/* 137 */       return Utils.getLocalizedString("MEM_KB");
/*     */     }
/*     */     
/* 140 */     if (value == 1048576L) {
/* 141 */       return Utils.getLocalizedString("MEM_MB");
/*     */     }
/*     */     
/* 144 */     if (value == 1073741824L) {
/* 145 */       return Utils.getLocalizedString("MEM_GB");
/*     */     }
/*     */     
/* 148 */     if (value == 1099511627776L) {
/* 149 */       return Utils.getLocalizedString("MEM_TB");
/*     */     }
/* 151 */     return Utils.getLocalizedString("MEM_B");
/*     */   }
/*     */   
/*     */   public static String parseSecondsToLocalizedTimeUnit(long timeInSeconds) {
/* 155 */     if (timeInSeconds < 0L) {
/* 156 */       return "";
/*     */     }
/*     */     
/* 159 */     long seconds = timeInSeconds % 60L;
/* 160 */     timeInSeconds = (timeInSeconds - seconds) / 60L;
/* 161 */     long minutes = timeInSeconds % 60L;
/* 162 */     timeInSeconds = (timeInSeconds - minutes) / 60L;
/* 163 */     long hours = timeInSeconds % 24L;
/* 164 */     timeInSeconds = (timeInSeconds - hours) / 24L;
/* 165 */     long days = timeInSeconds;
/*     */ 
/*     */     
/* 168 */     if (minutes < 2L && hours < 1L && days < 1L) {
/* 169 */       seconds += minutes * 60L;
/* 170 */       if (seconds > 1L) {
/* 171 */         return Utils.getLocalizedString("time.common.seconds", new String[] { String.valueOf(seconds) });
/*     */       }
/*     */       
/* 174 */       return Utils.getLocalizedString("time.common.second", new String[] { String.valueOf(seconds) });
/*     */     } 
/*     */     
/* 177 */     if (hours < 2L && days < 1L) {
/* 178 */       return Utils.getLocalizedString("time.common.minutes", new String[] { String.valueOf(minutes + hours * 60L) });
/*     */     }
/*     */     
/* 181 */     if (days < 1L) {
/* 182 */       return Utils.getLocalizedString("time.common.hours", new String[] { String.valueOf(hours + days * 24L) });
/*     */     }
/*     */     
/* 185 */     return Utils.getLocalizedString("time.common.days", new String[] { String.valueOf(days) });
/*     */   }
/*     */   
/*     */   public static long getMinutesFromNow(long dateInMilliseconds) {
/* 189 */     Date now = new Date();
/* 190 */     return (dateInMilliseconds - now.getTime()) / 60000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLocalizedNumber(BigDecimal val) {
/* 200 */     String formattedNumber = NumberFormat.getInstance(
/* 201 */         new Locale((_userSessionService.getUserSession()).locale)).format(val.doubleValue());
/* 202 */     return formattedNumber;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/FormatUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */