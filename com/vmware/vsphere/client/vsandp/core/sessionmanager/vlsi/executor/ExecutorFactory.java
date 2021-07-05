/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutorFactory
/*    */   implements ResourceFactory<CloseableExecutorService, ExecutorSettings>
/*    */ {
/*    */   public CloseableExecutorService acquire(ExecutorSettings config) {
/* 17 */     ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
/* 18 */         config.getInitialThreads(), 
/* 19 */         config.getMaxThreads(), 
/* 20 */         config.getKeepAliveTime(), 
/* 21 */         config.getKeepAliveUnit(), 
/* 22 */         new LinkedBlockingQueue<>());
/*    */     
/* 24 */     final CloseableExecutorService result = new CloseableExecutorService(threadPoolExecutor);
/* 25 */     result.setCloseHandler(new Runnable()
/*    */         {
/*    */           public void run() {
/* 28 */             result.shutdown();
/*    */           }
/*    */         });
/* 31 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/executor/ExecutorFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */