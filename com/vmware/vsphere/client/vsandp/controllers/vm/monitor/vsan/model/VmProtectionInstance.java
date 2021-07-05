/*     */ package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.QuiescedType;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsandp.core.AsModel;
/*     */ import com.vmware.vsphere.client.vsandp.data.ProtectionType;
/*     */ import java.util.Date;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @data
/*     */ @AsModel
/*     */ public class VmProtectionInstance implements Comparable<VmProtectionInstance> {
/*     */   public String id;
/*     */   public String series;
/*     */   public ProtectionType type;
/*     */   public Date syncPoint;
/*     */   public long snapshotSize;
/*     */   public long transferDuration;
/*     */   public boolean hasRpoViolation;
/*     */   public QuiescingType quiescingType;
/*  20 */   private static final Logger logger = LoggerFactory.getLogger(VmProtectionInstance.class);
/*     */   public boolean isInProgress;
/*     */   public boolean isManual;
/*     */   
/*     */   @data
/*     */   @AsModel
/*     */   public enum QuiescingType
/*     */   {
/*  28 */     NONE,
/*  29 */     APP_CONSISTENCY,
/*  30 */     FILE_SYSTEM_CONSISTENCY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static QuiescingType fromQuescedType(String type) {
/*  38 */       switch (QuiescedType.valueOf(type)) {
/*     */         case none:
/*  40 */           return NONE;
/*     */         case null:
/*  42 */           return APP_CONSISTENCY;
/*     */         case fileSystemQuiesced:
/*  44 */           return FILE_SYSTEM_CONSISTENCY;
/*     */       } 
/*     */       
/*  47 */       VmProtectionInstance.logger.error("Received unsupported quiescing type from backend: {}", type);
/*  48 */       throw new IllegalArgumentException("Unknown quiescing type: " + type);
/*     */     }
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
/*     */   public VmProtectionInstance() {}
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
/*     */   public VmProtectionInstance(String id, String series, ProtectionType type, Date syncPoint, long snapshotSize, long transferDuration, boolean hasRpoViolation, QuiescingType quiescingType, boolean isInProgress, boolean isManual) {
/* 110 */     this.id = id;
/* 111 */     this.type = type;
/* 112 */     this.syncPoint = syncPoint;
/* 113 */     this.snapshotSize = snapshotSize;
/* 114 */     this.transferDuration = transferDuration;
/* 115 */     this.hasRpoViolation = hasRpoViolation;
/* 116 */     this.quiescingType = quiescingType;
/* 117 */     this.isInProgress = isInProgress;
/* 118 */     this.isManual = isManual;
/* 119 */     this.series = series;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(VmProtectionInstance o) {
/* 124 */     int result = this.syncPoint.compareTo(o.syncPoint);
/* 125 */     if (result == 0)
/*     */     {
/* 127 */       result = -this.type.compareTo((Enum)o.type);
/*     */     }
/*     */     
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VmProtectionInstance getEmptyInstance(Date date) {
/* 138 */     return new VmProtectionInstance(null, null, ProtectionType.LOCAL, date, 0L, 0L, false, 
/* 139 */         QuiescingType.NONE, false, false);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/model/VmProtectionInstance.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */