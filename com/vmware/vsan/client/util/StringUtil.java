/*     */ package com.vmware.vsan.client.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.NumericRange;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*     */   private static final String RANGE_DELIMITER = ", ";
/*     */   private static final String RANGE_DIVIDER = "-";
/*     */   
/*     */   public static String getIndexedString(List<String> existingStrings, String baseString, String indexSeparator) {
/*  53 */     if (baseString == null || baseString.length() == 0) {
/*  54 */       throw new IllegalArgumentException("Default name cannot be null or empty.");
/*     */     }
/*     */ 
/*     */     
/*  58 */     if (existingStrings == null || existingStrings.size() == 0) {
/*  59 */       return baseString;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (indexSeparator == null) {
/*  66 */       indexSeparator = "";
/*     */     }
/*     */     
/*  69 */     String newName = baseString;
/*     */     
/*  71 */     Boolean isUnique = Boolean.valueOf(false);
/*  72 */     int index = 1;
/*  73 */     while (!isUnique.booleanValue()) {
/*  74 */       isUnique = Boolean.valueOf(true);
/*  75 */       for (String str : existingStrings) {
/*  76 */         if (str.equalsIgnoreCase(newName)) {
/*  77 */           newName = String.valueOf(baseString) + indexSeparator + index;
/*  78 */           index++;
/*  79 */           isUnique = Boolean.valueOf(false);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  84 */     return newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String parseNumericRange(NumericRange[] ranges) {
/*  93 */     if (ranges == null) {
/*  94 */       return null;
/*     */     }
/*  96 */     StringBuilder result = new StringBuilder(); byte b; int i; NumericRange[] arrayOfNumericRange;
/*  97 */     for (i = (arrayOfNumericRange = ranges).length, b = 0; b < i; ) { NumericRange range = arrayOfNumericRange[b];
/*  98 */       if (range.start == range.end) {
/*  99 */         result.append(Integer.toString(range.start));
/*     */       } else {
/* 101 */         result.append(String.valueOf(range.start) + "-" + range.end);
/*     */       } 
/* 103 */       result.append(", "); b++; }
/*     */     
/* 105 */     if (result.length() >= ", ".length()) {
/* 106 */       result.setLength(result.length() - ", ".length());
/*     */     }
/* 108 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/StringUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */