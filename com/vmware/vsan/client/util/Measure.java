/*     */ package com.vmware.vsan.client.util;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Measure
/*     */   implements AutoCloseable
/*     */ {
/*  22 */   private static final Log log = LogFactory.getLog(Measure.class);
/*     */   
/*     */   private static final boolean loggingEnabled = true;
/*     */   
/*     */   private static final int REPRESENTATION_LENGTH = 180;
/*     */   protected final String task;
/*  28 */   protected final long startTime = System.currentTimeMillis();
/*  29 */   protected volatile long endTime = -1L;
/*     */   
/*  31 */   protected final Multimap<String, Measure> subtasks = Multimaps.synchronizedMultimap((Multimap)ArrayListMultimap.create());
/*  32 */   protected final List<String> taskOrder = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   public Measure(String task) {
/*  35 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Measure start(String task) {
/*  43 */     if (!this.taskOrder.contains(task)) {
/*  44 */       this.taskOrder.add(task);
/*     */     }
/*  46 */     Measure subtask = new Measure(task)
/*     */       {
/*     */         public void close() {
/*  49 */           markClosed();
/*     */         }
/*     */         
/*     */         public Measure start(String task) {
/*  53 */           throw new UnsupportedOperationException("Nested tasks unsupported");
/*     */         }
/*     */       };
/*  56 */     this.subtasks.put(task, subtask);
/*  57 */     return subtask;
/*     */   }
/*     */   
/*     */   protected void markClosed() {
/*  61 */     if (this.endTime != -1L) {
/*  62 */       throw new IllegalStateException("Measure already closed.");
/*     */     }
/*  64 */     this.endTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   protected String format(long parentStartTime, long millisPerSymbol) {
/*  68 */     if (millisPerSymbol <= 0L) {
/*  69 */       return "!";
/*     */     }
/*     */     
/*  72 */     int offset = (int)((this.startTime - parentStartTime) / millisPerSymbol);
/*  73 */     int length = (int)((((this.endTime != -1L) ? this.endTime : System.currentTimeMillis()) - this.startTime) / millisPerSymbol);
/*     */ 
/*     */     
/*  76 */     String line = "";
/*  77 */     for (int i = 0; i < offset && line.length() < 180; i++) {
/*  78 */       line = String.valueOf(line) + " ";
/*     */     }
/*     */     
/*  81 */     int regionStart = line.length();
/*  82 */     line = String.valueOf(line) + "*";
/*     */     
/*  84 */     for (int j = 1; j < length && line.length() < 180; j++) {
/*  85 */       line = String.valueOf(line) + "-";
/*     */     }
/*  87 */     int regionEnd = line.length();
/*     */     
/*  89 */     if (this.endTime != -1L) {
/*  90 */       line = String.valueOf(line.substring(0, line.length() - 1)) + "*";
/*     */     }
/*  92 */     while (line.length() < 180) {
/*  93 */       line = String.valueOf(line) + " ";
/*     */     }
/*     */     
/*  96 */     if (regionEnd - regionStart >= this.task.length() + 4) {
/*  97 */       line = String.valueOf(line.substring(0, regionStart)) + "*" + 
/*  98 */         " " + this.task + " " + 
/*  99 */         line.substring(regionStart + this.task.length() + 3);
/*     */     }
/*     */     
/* 102 */     return line;
/*     */   }
/*     */   
/*     */   public <T> Future<T> newFuture(String task) {
/* 106 */     return (Future<T>)new MeasurableFuture(this, task);
/*     */   }
/*     */   
/*     */   public long getDuration() {
/* 110 */     if (this.endTime == -1L) {
/* 111 */       return -1L;
/*     */     }
/* 113 */     return this.endTime - this.startTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 118 */     markClosed();
/*     */     
/* 120 */     log.info(String.valueOf(this.task) + " (" + (new DecimalFormat("0.00")).format(getDuration() / 1000.0D) + " s):\n" + toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 128 */     long duration = ((this.endTime != -1L) ? this.endTime : System.currentTimeMillis()) - this.startTime;
/* 129 */     long millisPerSymbol = duration / 180L;
/*     */     
/* 131 */     List<String> subtaskIds = this.taskOrder;
/* 132 */     for (int st = 0; st < subtaskIds.size(); st++) {
/* 133 */       for (Measure subtask : this.subtasks.get(subtaskIds.get(st))) {
/* 134 */         String durationStr = (subtask.getDuration() != -1L) ? (String.valueOf(subtask.getDuration()) + "ms") : "ongoing";
/*     */         
/* 136 */         String line = subtask.format(this.startTime, millisPerSymbol);
/* 137 */         builder.append("[" + line + "] ");
/* 138 */         builder.append(subtaskIds.get(st));
/* 139 */         builder.append(" (" + durationStr + ")");
/* 140 */         builder.append("\n");
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/Measure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */