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
/*    */ public enum HciWorkflowState
/*    */ {
/* 14 */   IN_PROGRESS("in_progress"),
/* 15 */   DONE("done"),
/* 16 */   INVALID("invalid"),
/* 17 */   NOT_IN_HCI_WORKFLOW("not_in_hci_workflow");
/*    */   
/*    */   private String text;
/*    */   
/*    */   HciWorkflowState(String text) {
/* 22 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 26 */     return this.text;
/*    */   }
/*    */   
/*    */   public static HciWorkflowState fromString(String text) throws Exception {
/* 30 */     if (!StringUtils.isBlank(text)) {
/* 31 */       byte b; int i; HciWorkflowState[] arrayOfHciWorkflowState; for (i = (arrayOfHciWorkflowState = values()).length, b = 0; b < i; ) { HciWorkflowState level = arrayOfHciWorkflowState[b];
/* 32 */         if (text.equals(level.getText()))
/* 33 */           return level; 
/*    */         b++; }
/*    */     
/*    */     } 
/* 37 */     throw new IllegalArgumentException("Unsupported HCI workflow state " + text);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/HciWorkflowState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */