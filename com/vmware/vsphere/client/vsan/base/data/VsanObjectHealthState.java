/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum VsanObjectHealthState
/*    */ {
/* 12 */   HEALTHY("healthy"),
/* 13 */   DATA_MOVE("datamove"),
/* 14 */   NON_AVAILABILITY_RELATED_RECONFIG("nonavailabilityrelatedreconfig"),
/* 15 */   NON_AVAILABILITY_RELATED_INCOMPLIANCE("nonavailabilityrelatedincompliance"),
/* 16 */   REDUCED_AVAILABILITY_WITH_ACTIVE_REBUILD("reducedavailabilitywithactiverebuild"),
/* 17 */   INACCESSIBLE("inaccessible"),
/* 18 */   REDUCED_AVAILABILITY_WITH_NO_REBUILD("reducedavailabilitywithnorebuild"),
/* 19 */   REDUCED_AVAILABILITY_WITH_NO_REBUILD_DELAY_TIMER("reducedavailabilitywithnorebuilddelaytimer"),
/* 20 */   UNKNOWN("unknown"); private static final Log _logger;
/*    */   static {
/* 22 */     _logger = LogFactory.getLog(VsanObjectHealthState.class);
/*    */   }
/*    */   private String text;
/*    */   
/*    */   VsanObjectHealthState(String text) {
/* 27 */     this.text = text;
/*    */   }
/*    */   
/*    */   public static VsanObjectHealthState fromString(String text) {
/* 31 */     if (text != null) {
/* 32 */       text = text.replace("-", ""); byte b; int i; VsanObjectHealthState[] arrayOfVsanObjectHealthState;
/* 33 */       for (i = (arrayOfVsanObjectHealthState = values()).length, b = 0; b < i; ) { VsanObjectHealthState val = arrayOfVsanObjectHealthState[b];
/* 34 */         if (text.equalsIgnoreCase(val.text))
/* 35 */           return val; 
/*    */         b++; }
/*    */     
/*    */     } 
/* 39 */     _logger.warn("Unknown VsanObjectHealthState detected: " + text);
/* 40 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public static VsanObjectHealthState fromServerLocalizedString(String text) {
/* 44 */     if (text != null) {
/*    */ 
/*    */       
/* 47 */       text = text.replaceAll("-", "");
/* 48 */       return fromString(text);
/*    */     } 
/* 50 */     _logger.warn("Empty VsanObjectHealthState text detected!");
/* 51 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanObjectHealthState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */