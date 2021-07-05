/*     */ package com.vmware.vsphere.client.vsan.base.data;
/*     */ 
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @data
/*     */ public class VsanComponent
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  16 */   public static final String VSAN_OBJECT_NOT_FOUND_STRING = Utils.getLocalizedString(
/*  17 */       "vsan.monitor.virtualPhysicalMapping.component.objectNotFound");
/*     */ 
/*     */   
/*     */   private static final String COMPONENT_UUID_KEY = "componentUuid";
/*     */ 
/*     */   
/*     */   private static final String COMPONENT_BYTES_TO_SYNC_KEY = "bytesToSync";
/*     */ 
/*     */   
/*     */   private static final String COMPONENT_RECOVERY_ETA_KEY = "recoveryETA";
/*     */ 
/*     */   
/*     */   public long byteToSync;
/*     */ 
/*     */   
/*     */   public String componentUuid;
/*     */ 
/*     */   
/*     */   public long recoveryEta;
/*     */   
/*     */   public VsanComponentState state;
/*     */   
/*     */   public String hostUuid;
/*     */   
/*     */   public String hostName;
/*     */   
/*     */   public String type;
/*     */   
/*     */   public String cacheDiskUuid;
/*     */   
/*     */   public String capacityDiskUuid;
/*     */   
/*     */   public ComponentIntent intent;
/*     */ 
/*     */   
/*     */   public VsanComponent() {}
/*     */ 
/*     */   
/*     */   public VsanComponent(boolean setDefaultValues) {
/*  56 */     if (setDefaultValues) {
/*  57 */       setDefaultValues();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanComponent(JsonNode node, JsonNode attributeNode, Map<String, String> componentUuidsToHostNames) {
/*  64 */     this.componentUuid = node.path("componentUuid").getTextValue();
/*  65 */     this.hostName = componentUuidsToHostNames.get(this.componentUuid);
/*     */     
/*  67 */     this.byteToSync = getLongValue(attributeNode, "bytesToSync", 0);
/*  68 */     this.recoveryEta = getIntValue(attributeNode, "recoveryETA", -1);
/*  69 */     this.intent = getIntent(attributeNode);
/*     */   }
/*     */   
/*     */   public void setDefaultValues() {
/*  73 */     this.cacheDiskUuid = VSAN_OBJECT_NOT_FOUND_STRING;
/*  74 */     this.capacityDiskUuid = VSAN_OBJECT_NOT_FOUND_STRING;
/*  75 */     this.hostUuid = VSAN_OBJECT_NOT_FOUND_STRING;
/*     */   }
/*     */   
/*     */   private ComponentIntent getIntent(JsonNode attributeNode) {
/*  79 */     int flags = getIntValue(attributeNode, "flags", 0);
/*  80 */     ComponentIntent result = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     if ((flags & ComponentIntent.DECOM.getValue()) == 
/*  89 */       ComponentIntent.DECOM.getValue()) {
/*  90 */       result = ComponentIntent.DECOM;
/*  91 */     } else if ((flags & ComponentIntent.MOVE.getValue()) == 
/*  92 */       ComponentIntent.MOVE.getValue()) {
/*  93 */       result = ComponentIntent.MOVE;
/*  94 */     } else if ((flags & ComponentIntent.REBALANCE.getValue()) == 
/*  95 */       ComponentIntent.REBALANCE.getValue()) {
/*  96 */       result = ComponentIntent.REBALANCE;
/*  97 */     } else if ((flags & ComponentIntent.REPAIR.getValue()) == 
/*  98 */       ComponentIntent.REPAIR.getValue()) {
/*  99 */       result = ComponentIntent.REPAIR;
/* 100 */     } else if ((flags & ComponentIntent.FIXCOMPLIANCE.getValue()) == 
/* 101 */       ComponentIntent.FIXCOMPLIANCE.getValue()) {
/* 102 */       result = ComponentIntent.FIXCOMPLIANCE;
/* 103 */     } else if ((flags & ComponentIntent.POLICYCHANGE.getValue()) == 
/* 104 */       ComponentIntent.POLICYCHANGE.getValue()) {
/* 105 */       result = ComponentIntent.POLICYCHANGE;
/* 106 */     } else if ((flags & ComponentIntent.STALE.getValue()) == 
/* 107 */       ComponentIntent.STALE.getValue()) {
/* 108 */       result = ComponentIntent.STALE;
/* 109 */     } else if ((flags & ComponentIntent.MERGE_CONTACT.getValue()) == 
/* 110 */       ComponentIntent.MERGE_CONTACT.getValue()) {
/* 111 */       result = ComponentIntent.MERGE_CONTACT;
/*     */     } else {
/* 113 */       result = ComponentIntent.FIXCOMPLIANCE;
/*     */     } 
/*     */     
/* 116 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getIntValue(JsonNode node, String field, int defaultValue) {
/* 124 */     if (node == null || node.isMissingNode() || !node.has(field)) {
/* 125 */       return defaultValue;
/*     */     }
/* 127 */     return node.get(field).getIntValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getLongValue(JsonNode node, String field, int defaultValue) {
/* 135 */     if (node == null || node.isMissingNode() || !node.has(field)) {
/* 136 */       return defaultValue;
/*     */     }
/* 138 */     return node.get(field).getLongValue();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */