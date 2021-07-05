/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.CachedResourceFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachedExecutorFactory
/*    */   extends CachedResourceFactory<CloseableExecutorService, ExecutorSettings>
/*    */ {
/*    */   public CachedExecutorFactory() {
/* 13 */     super(new ExecutorFactory());
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void shutdown() {
/* 18 */     super.shutdown();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/executor/CachedExecutorFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */