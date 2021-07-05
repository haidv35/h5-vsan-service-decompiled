/*     */ package com.vmware.vsphere.client.vsan.base.util.multithreading;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanAsyncQueryUtils
/*     */ {
/*  27 */   private static final Log _logger = LogFactory.getLog(VsanAsyncQueryUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResultSet getProperties(List<Callable<RequestResult>> requestTasks) {
/*  34 */     Validate.notNull(requestTasks);
/*  35 */     ResultSet result = new ResultSet();
/*     */     
/*     */     try {
/*  38 */       List<RequestResult> requestResults = getResultsAsync(requestTasks);
/*  39 */       result = createResultSet(requestResults);
/*  40 */     } catch (InterruptedException interruptedException) {
/*  41 */       _logger.error("Interrupted while executing query.", interruptedException);
/*  42 */       result.error = interruptedException;
/*  43 */     } catch (TimeoutException timeOutException) {
/*  44 */       _logger.error("Task executor unexpectedly timed out.", timeOutException);
/*  45 */       result.error = timeOutException;
/*  46 */     } catch (Exception e) {
/*  47 */       _logger.error("Executing query failed", e);
/*  48 */       result.error = e;
/*     */     } 
/*  50 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> List<TaskResult<T>> executeTasks(List<Callable<T>> requestTasks) {
/*  55 */     Validate.notNull(requestTasks);
/*     */     
/*  57 */     List<TaskResult<T>> results = new ArrayList<>();
/*  58 */     for (Callable<T> task : requestTasks) {
/*     */       try {
/*  60 */         results.add(new TaskResult<>(task.call(), null, null));
/*  61 */       } catch (Exception e) {
/*  62 */         results.add(new TaskResult<>(null, e, null));
/*     */       } 
/*     */     } 
/*  65 */     return results;
/*     */   }
/*     */   
/*     */   public static Callable<RequestResult> getStorageProfiles(final ManagedObjectReference clusterRef) {
/*  69 */     return new Callable<RequestResult>()
/*     */       {
/*     */         public VsanAsyncQueryUtils.RequestResult call() {
/*  72 */           Exception error = null;
/*  73 */           List<Profile> storageProfiles = null;
/*     */           try {
/*  75 */             storageProfiles = 
/*  76 */               BaseUtils.getStorageProfiles(clusterRef);
/*  77 */           } catch (Exception ex) {
/*  78 */             error = ex;
/*     */           } 
/*  80 */           return new VsanAsyncQueryUtils.RequestResult(storageProfiles, error, clusterRef, "storageProfiles");
/*     */         }
/*     */       };
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
/*     */   public static <K, T> Map<K, T> awaitAll(Map<K, Future<T>> tasks) {
/*  94 */     return awaitAll(tasks, new Function<Map.Entry<K, Future<T>>, T>()
/*     */         {
/*     */           public T apply(Map.Entry<K, Future<T>> future) {
/*     */             try {
/*  98 */               return (T)((Future)future.getValue()).get();
/*  99 */             } catch (ExecutionException e) {
/* 100 */               throw new IllegalStateException("Failed to get result of task.", e.getCause());
/* 101 */             } catch (Exception e) {
/* 102 */               throw new IllegalStateException("Failed to get result of task.", e);
/*     */             } 
/*     */           }
/*     */         });
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
/*     */   public static <K, T> Map<K, T> awaitAll(Map<K, Future<T>> tasks, Function<Map.Entry<K, Future<T>>, T> awaitOne) {
/* 118 */     Map<K, T> result = Maps.newHashMap();
/* 119 */     for (Map.Entry<K, Future<T>> entry : tasks.entrySet()) {
/* 120 */       T taskResult = (T)awaitOne.apply(entry);
/* 121 */       if (taskResult != null) {
/* 122 */         result.put(entry.getKey(), taskResult);
/*     */       }
/*     */     } 
/* 125 */     return result;
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
/*     */   private static List<RequestResult> getResultsAsync(List<Callable<RequestResult>> requestTasks) throws InterruptedException, TimeoutException {
/* 146 */     List<TaskResult<RequestResult>> taskResults = executeTasks(requestTasks);
/*     */     
/* 148 */     List<RequestResult> requestResults = new ArrayList<>();
/* 149 */     for (TaskResult<RequestResult> taskResult : taskResults) {
/* 150 */       RequestResult result = taskResult.getResult();
/*     */ 
/*     */       
/* 153 */       if (taskResult.getException() != null && result.error == null) {
/* 154 */         result = new RequestResult(result.result, 
/* 155 */             taskResult.getException(), result.target, result.property);
/*     */       }
/* 157 */       requestResults.add(result);
/*     */     } 
/*     */     
/* 160 */     return requestResults;
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
/*     */   private static ResultSet createResultSet(List<RequestResult> requestResults) {
/* 172 */     assert requestResults != null : "requestResults is null";
/* 173 */     List<Exception> errors = new ArrayList<>();
/* 174 */     Map<ManagedObjectReference, ArrayList<PropertyValue>> items = 
/* 175 */       new HashMap<>();
/* 176 */     ResultSet result = new ResultSet();
/*     */     
/* 178 */     for (RequestResult requestResult : requestResults) {
/* 179 */       if (requestResult.error != null) {
/* 180 */         errors.add(requestResult.error);
/*     */       }
/* 182 */       if (!items.containsKey(requestResult.target)) {
/* 183 */         items.put(requestResult.target, new ArrayList<>());
/*     */       }
/*     */       
/* 186 */       ArrayList<PropertyValue> propertyResults = items.get(requestResult.target);
/* 187 */       propertyResults.add(requestResult.toPropertyValue());
/*     */     } 
/*     */     
/* 190 */     if (errors.size() > 0) {
/* 191 */       result.error = errors.get(0);
/*     */     }
/*     */     
/* 194 */     result.items = new ResultItem[items.size()];
/*     */     
/* 196 */     int resultItemIndex = 0;
/* 197 */     for (ManagedObjectReference moref : items.keySet()) {
/* 198 */       ResultItem resultItem = new ResultItem();
/* 199 */       resultItem.resourceObject = moref;
/* 200 */       ArrayList<PropertyValue> propValues = items.get(moref);
/* 201 */       resultItem.properties = propValues.<PropertyValue>toArray(new PropertyValue[propValues.size()]);
/* 202 */       result.items[resultItemIndex] = resultItem;
/* 203 */       resultItemIndex++;
/*     */     } 
/*     */     
/* 206 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class RequestResult
/*     */   {
/*     */     public final Object result;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Exception error;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ManagedObjectReference target;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String property;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RequestResult(Object result, Exception error, ManagedObjectReference target, String property) {
/* 236 */       Validate.notNull(target);
/* 237 */       this.result = result;
/* 238 */       this.error = error;
/* 239 */       this.target = target;
/* 240 */       this.property = property;
/*     */     }
/*     */     
/*     */     public PropertyValue toPropertyValue() {
/* 244 */       PropertyValue propValue = new PropertyValue();
/* 245 */       propValue.resourceObject = this.target;
/* 246 */       propValue.propertyName = this.property;
/* 247 */       propValue.value = this.result;
/* 248 */       return propValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class TaskResult<T>
/*     */   {
/*     */     private final T _result;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Exception _exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TaskResult(T result, Exception exception) {
/* 272 */       this._result = result;
/* 273 */       this._exception = exception;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T getResult() {
/* 280 */       return this._result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Exception getException() {
/* 287 */       return this._exception;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 292 */       return "[result: " + this._result + ", exception: " + this._exception + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/multithreading/VsanAsyncQueryUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */