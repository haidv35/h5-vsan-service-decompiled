/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CloseableExecutorService
/*     */   extends Resource
/*     */   implements ExecutorService
/*     */ {
/*     */   protected final ExecutorService delegatedExecutor;
/*     */   
/*     */   public CloseableExecutorService(ExecutorService delegatedExecutor) {
/*  23 */     this.delegatedExecutor = delegatedExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable arg0) {
/*  28 */     this.delegatedExecutor.execute(arg0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  34 */     return this.delegatedExecutor.awaitTermination(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/*  40 */     return this.delegatedExecutor.invokeAll(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/*  47 */     return this.delegatedExecutor.invokeAll(tasks, timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/*  53 */     return this.delegatedExecutor.invokeAny(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  60 */     return this.delegatedExecutor.invokeAny(tasks, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/*  65 */     return this.delegatedExecutor.isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/*  70 */     return isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*  75 */     this.delegatedExecutor.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Runnable> shutdownNow() {
/*  80 */     return this.delegatedExecutor.shutdownNow();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/*  85 */     return this.delegatedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/*  90 */     return this.delegatedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result) {
/*  95 */     return this.delegatedExecutor.submit(task, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return String.valueOf(getClass().getSimpleName()) + "@" + hashCode();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/executor/CloseableExecutorService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */