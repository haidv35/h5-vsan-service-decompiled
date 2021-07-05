/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfQuerySpec;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class PerfQuerySpec
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String entityType;
/*    */   public String entityUuid;
/*    */   public String group;
/*    */   public Long startTime;
/*    */   public Long endTime;
/*    */   public Integer interval;
/*    */   public String[] labels;
/*    */   
/*    */   public static VsanPerfQuerySpec toVmodl(PerfQuerySpec spec) {
/* 26 */     VsanPerfQuerySpec querySpec = new VsanPerfQuerySpec();
/*    */     
/* 28 */     querySpec.endTime = getCalendarFromLong(spec.endTime);
/* 29 */     querySpec.startTime = getCalendarFromLong(spec.startTime);
/*    */     
/* 31 */     querySpec.group = spec.group;
/* 32 */     querySpec.interval = spec.interval;
/* 33 */     querySpec.labels = spec.labels;
/* 34 */     querySpec.entityRefId = String.valueOf(spec.entityType) + ":" + spec.entityUuid;
/* 35 */     return querySpec;
/*    */   }
/*    */   
/*    */   private static Calendar getCalendarFromLong(Long time) {
/* 39 */     Calendar calendar = Calendar.getInstance();
/* 40 */     calendar.setTimeInMillis(time.longValue());
/* 41 */     BaseUtils.setUTCTimeZone(calendar);
/* 42 */     return calendar;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfQuerySpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */