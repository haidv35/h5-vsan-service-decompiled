/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class ConfigCardData
/*    */ {
/*    */   public String title;
/*    */   public String actionId;
/*    */   public Status status;
/*    */   public boolean enabled;
/*    */   public boolean nextStep;
/*    */   public String launchButtonText;
/*    */   public String contentHeader;
/*    */   public String contentText;
/*    */   public List<String> listItems;
/*    */   public boolean validatePresent;
/*    */   public boolean validateEnabled;
/*    */   public boolean operationInProgress;
/*    */   public int progress;
/*    */   public ValidationData validationData;
/*    */   
/*    */   public ConfigCardData(String title, String actionId, boolean validatePresent, boolean operationInProgress, String launchButtonText) {
/* 33 */     this.title = title;
/* 34 */     this.actionId = actionId;
/* 35 */     this.validatePresent = validatePresent;
/* 36 */     this.operationInProgress = operationInProgress;
/* 37 */     this.status = Status.NOT_AVAILABLE;
/* 38 */     this.enabled = false;
/* 39 */     this.launchButtonText = launchButtonText;
/* 40 */     this.nextStep = false;
/* 41 */     this.validateEnabled = false;
/*    */   }
/*    */   
/*    */   @data
/*    */   public enum Status {
/* 46 */     NOT_AVAILABLE,
/* 47 */     PASSED,
/* 48 */     ERROR;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/ConfigCardData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */