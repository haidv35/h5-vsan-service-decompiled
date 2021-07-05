/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ @data
/*    */ public enum PerfGraphThresholdDirection
/*    */ {
/*  8 */   upper, lower;
/*    */   public static PerfGraphThresholdDirection fromVmodl(String direction) {
/*    */     String str;
/* 11 */     switch ((str = direction).hashCode()) { case 103164673: if (!str.equals("lower"))
/*    */           break; 
/* 13 */         return lower;
/*    */       case 111499426: if (!str.equals("upper"))
/* 15 */           break;  return upper; }  return null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfGraphThresholdDirection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */