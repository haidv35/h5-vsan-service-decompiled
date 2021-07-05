/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum Service
/*    */ {
/* 14 */   MANAGEMENT("management"),
/* 15 */   VMOTION("vmotion"),
/* 16 */   VSAN("vsan");
/*    */   
/*    */   private String text;
/*    */   
/*    */   Service(String text) {
/* 21 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 25 */     return this.text;
/*    */   }
/*    */   
/*    */   public static Service fromString(String text) throws Exception {
/* 29 */     if (!StringUtils.isBlank(text)) {
/* 30 */       byte b; int i; Service[] arrayOfService; for (i = (arrayOfService = values()).length, b = 0; b < i; ) { Service level = arrayOfService[b];
/* 31 */         if (text.equals(level.getText()))
/* 32 */           return level; 
/*    */         b++; }
/*    */     
/*    */     } 
/* 36 */     throw new IllegalArgumentException("Unsupported service " + text);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/Service.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */