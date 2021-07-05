/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum VsanObjectDataProtectionHealthState
/*    */ {
/* 14 */   UNKNOWN(
/* 15 */     "unknown"),
/* 16 */   NOT_CONFIGURED("notconfigured"),
/* 17 */   PROTECTION_OK("protectionok"),
/* 18 */   FULL_SYNC_IN_PROGRESS("fullsyncinprogress"),
/* 19 */   VM_QUIESCING_FAILED("vmquiescingfailed"),
/* 20 */   INVALID_PROTECTION_CONFIGURATION("invalidprotectionconfiguration"),
/* 21 */   ARCHIVE_INACCESSIBLE("archivestoreinaccessible"),
/* 22 */   ARCHIVE_NO_SPACE("archivestorenospace"),
/* 23 */   ARCHIVE_NOT_CONFIGURED("archivetargetnotconfigured"),
/* 24 */   CG_OBJECT_NOT_AVAILABLE("cgobjectnotavailable"),
/* 25 */   RETENTION_FAILURES("retentionfailures"),
/* 26 */   LOCAL_STORAGE_USAGE_EXCEEDED_THRESHOLD("localstorageusageexceededthreshold"),
/* 27 */   CG_CONTAINS_UNPROMOTED_OBJECTS("cgcontainsunpromotedobjects"); private static final Log _logger;
/*    */   static {
/* 29 */     _logger = LogFactory.getLog(VsanObjectDataProtectionHealthState.class);
/*    */   }
/*    */   private String text;
/*    */   
/*    */   VsanObjectDataProtectionHealthState(String text) {
/* 34 */     this.text = text;
/*    */   }
/*    */   
/*    */   public static VsanObjectDataProtectionHealthState fromString(String text) {
/* 38 */     if (text != null) {
/* 39 */       byte b; int i; VsanObjectDataProtectionHealthState[] arrayOfVsanObjectDataProtectionHealthState; for (i = (arrayOfVsanObjectDataProtectionHealthState = values()).length, b = 0; b < i; ) { VsanObjectDataProtectionHealthState val = arrayOfVsanObjectDataProtectionHealthState[b];
/* 40 */         if (text.equalsIgnoreCase(val.text))
/* 41 */           return val; 
/*    */         b++; }
/*    */     
/*    */     } 
/* 45 */     _logger.warn("Unknown VsanObjectDataProtectionHealthState detected: " + text);
/* 46 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public static VsanObjectDataProtectionHealthState fromServerLocalizedString(String text) {
/* 50 */     if (text != null) {
/*    */ 
/*    */       
/* 53 */       text = text.replaceAll("-", "");
/* 54 */       return fromString(text);
/*    */     } 
/* 56 */     _logger.warn("Empty VsanObjectDataProtectionHealthState text detected!");
/* 57 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanObjectDataProtectionHealthState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */