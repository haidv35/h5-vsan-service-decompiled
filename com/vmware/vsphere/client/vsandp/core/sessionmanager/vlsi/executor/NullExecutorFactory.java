/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NullExecutorFactory
/*    */   implements ResourceFactory<CloseableExecutorService, ExecutorSettings>
/*    */ {
/*    */   public CloseableExecutorService acquire(ExecutorSettings settings) {
/* 18 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/executor/NullExecutorFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */