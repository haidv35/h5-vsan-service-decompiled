/*    */ package com.vmware.vsan.client.services.common;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.TaskInfo;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.services.common.data.TaskInfoData;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import org.apache.commons.lang.Validate;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class TaskService
/*    */ {
/* 26 */   private Logger logger = LoggerFactory.getLogger(TaskService.class);
/*    */ 
/*    */   
/*    */   private static final int MAX_TRIES = 100;
/*    */ 
/*    */   
/*    */   private static final int POLL_DELAY = 1000;
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getResult(ManagedObjectReference taskRef) throws TimeoutException, InterruptedException {
/* 41 */     Validate.notNull(taskRef);
/*    */     
/* 43 */     Exception exception1 = null, exception2 = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/*    */     
/*    */     } finally {
/* 60 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public TaskInfoData getInfo(ManagedObjectReference taskRef) {
/* 72 */     Validate.notNull(taskRef);
/*    */     
/* 74 */     Exception exception1 = null, exception2 = null; try {
/*    */     
/*    */     } finally {
/* 77 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/TaskService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */