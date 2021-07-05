/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum DrsAutoLevel
/*    */ {
/* 17 */   FULLY_AUTOMATED("fullyAutomated"),
/* 18 */   MANUAL("manual"),
/* 19 */   PARTIALLY_AUTOMATED("partiallyAutomated"); private static final Log logger;
/*    */   static {
/* 21 */     logger = LogFactory.getLog(DrsAutoLevel.class);
/*    */   }
/*    */   private String text;
/*    */   DrsAutoLevel(String text) {
/* 25 */     this.text = text;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DrsAutoLevel fromString(String text) throws Exception {
/* 33 */     if (!StringUtils.isBlank(text)) {
/* 34 */       byte b; int i; DrsAutoLevel[] arrayOfDrsAutoLevel; for (i = (arrayOfDrsAutoLevel = values()).length, b = 0; b < i; ) { DrsAutoLevel level = arrayOfDrsAutoLevel[b];
/* 35 */         if (text.equals(level.valueOf()))
/* 36 */           return level; 
/*    */         b++; }
/*    */     
/*    */     } 
/* 40 */     throw new IllegalArgumentException("Unsupported automation level " + text);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/DrsAutoLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */