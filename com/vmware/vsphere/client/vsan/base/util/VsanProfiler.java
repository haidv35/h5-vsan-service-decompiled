/*    */ package com.vmware.vsphere.client.vsan.base.util;
/*    */ 
/*    */ import com.vmware.vsan.client.util.Measure;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanProfiler
/*    */ {
/* 17 */   private static final Log _logger = LogFactory.getLog(VsanProfiler.class);
/*    */   private final String _tag;
/* 19 */   private final Map<String, Long> _points = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   private static final long THRESHOLD = 10000L;
/*    */   
/*    */   private static final String MESSAGE_TIME = "%s[%s] - %d milliseconds";
/*    */   
/*    */   private static final String MESSAGE_NOT_STARTED = "%s[%s] - not started or already stopped";
/*    */ 
/*    */   
/*    */   public VsanProfiler(String tag) {
/* 30 */     this._tag = tag;
/*    */   }
/*    */   
/*    */   public VsanProfiler(Class<?> clazz) {
/* 34 */     this(clazz.getSimpleName());
/*    */   }
/*    */   
/*    */   public Point point(String name) {
/* 38 */     return new Point(name);
/*    */   }
/*    */   
/*    */   public static class Point
/*    */     extends Measure {
/*    */     public Point(String task) {
/* 44 */       super(task);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 49 */       return String.valueOf(this.task) + getDuration();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/VsanProfiler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */