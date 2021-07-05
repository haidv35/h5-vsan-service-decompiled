/*    */ package com.vmware.vsphere.client.vsan.iscsi.utils;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
/*    */ import java.net.URLEncoder;
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
/*    */ public class VsanIscsiTargetUriUtil
/*    */ {
/*    */   private static final char PLUS_CHAR = '+';
/*    */   private static final char PERCENTAGE_CHAR = '%';
/*    */   private static final String PLUS_FLAG = "\"plus\"";
/*    */   private static final String PERCENTAGE_FLAG = "\"percentage\"";
/*    */   
/*    */   public static String encode(String data) throws UnsupportedEncodingException {
/* 30 */     StringBuffer tempBuffer = new StringBuffer();
/* 31 */     int incrementor = 0;
/* 32 */     int dataLength = data.length();
/* 33 */     while (incrementor < dataLength) {
/* 34 */       char charecterAt = data.charAt(incrementor);
/* 35 */       if (charecterAt == '%') {
/* 36 */         tempBuffer.append("\"percentage\"");
/* 37 */       } else if (charecterAt == '+') {
/* 38 */         tempBuffer.append("\"plus\"");
/*    */       } else {
/* 40 */         tempBuffer.append(charecterAt);
/*    */       } 
/* 42 */       incrementor++;
/*    */     } 
/* 44 */     data = tempBuffer.toString();
/* 45 */     data = URLEncoder.encode(data, "UTF-8");
/* 46 */     return data;
/*    */   }
/*    */   
/*    */   public static String decode(String data) throws UnsupportedEncodingException {
/* 50 */     data = URLDecoder.decode(data, "UTF-8");
/* 51 */     data = 
/* 52 */       data.replaceAll("\"percentage\"", 
/* 53 */         Character.toString('%'));
/* 54 */     data = data.replaceAll("\"plus\"", Character.toString('+'));
/* 55 */     return data;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/utils/VsanIscsiTargetUriUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */