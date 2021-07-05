/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutorSettings
/*    */ {
/*    */   protected final int initialThreads;
/*    */   protected final int maxThreads;
/*    */   protected final long keepAliveTime;
/*    */   protected final TimeUnit keepAliveUnit;
/*    */   
/*    */   public ExecutorSettings(int initialThreads, int maxThreads) {
/* 21 */     this(initialThreads, maxThreads, 30L, TimeUnit.SECONDS);
/*    */   }
/*    */ 
/*    */   
/*    */   public ExecutorSettings(int initialThreads, int maxThreads, long keepAliveTime, TimeUnit keepAliveUnit) {
/* 26 */     this.initialThreads = initialThreads;
/* 27 */     this.maxThreads = maxThreads;
/* 28 */     this.keepAliveTime = keepAliveTime;
/* 29 */     this.keepAliveUnit = keepAliveUnit;
/*    */   }
/*    */   
/*    */   public int getInitialThreads() {
/* 33 */     return this.initialThreads;
/*    */   }
/*    */   
/*    */   public int getMaxThreads() {
/* 37 */     return this.maxThreads;
/*    */   }
/*    */   
/*    */   public long getKeepAliveTime() {
/* 41 */     return this.keepAliveTime;
/*    */   }
/*    */   
/*    */   public TimeUnit getKeepAliveUnit() {
/* 45 */     return this.keepAliveUnit;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return (int)((this.initialThreads + 43 * this.maxThreads) + 43L * this.keepAliveUnit
/* 51 */       .toMillis(this.keepAliveTime));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 56 */     if (obj == null || !(obj instanceof ExecutorSettings)) {
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     ExecutorSettings other = (ExecutorSettings)obj;
/*    */     
/* 62 */     if (this.initialThreads != other.initialThreads || 
/* 63 */       this.maxThreads != other.maxThreads || 
/* 64 */       this.keepAliveTime != other.keepAliveTime) {
/* 65 */       return false;
/*    */     }
/*    */     
/* 68 */     if ((this.keepAliveUnit == null && other.keepAliveUnit == null) || (
/* 69 */       this.keepAliveUnit != null && other.keepAliveUnit != null && this.keepAliveUnit
/* 70 */       .equals(other.keepAliveUnit))) {
/* 71 */       return true;
/*    */     }
/*    */     
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return String.format(
/* 80 */         "ExecutorSettings [initialThreads=%s, maxThreads=%s, keepAliveTime=%s, keepAliveUnit=%s]", new Object[] {
/* 81 */           Integer.valueOf(this.initialThreads), Integer.valueOf(this.maxThreads), Long.valueOf(this.keepAliveTime), this.keepAliveUnit
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/executor/ExecutorSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */