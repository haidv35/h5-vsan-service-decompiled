/*    */ package com.vmware.vsan.client.services.common.data;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.TaskInfo;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class TaskInfoData
/*    */ {
/*    */   public String status;
/*    */   public Object result;
/*    */   public Exception exception;
/*    */   
/*    */   public static TaskInfoData fromTaskInfo(TaskInfo taskInfo) {
/* 20 */     TaskInfoData taskInfoData = new TaskInfoData();
/*    */     
/* 22 */     taskInfoData.status = taskInfo.state.toString();
/* 23 */     taskInfoData.result = taskInfo.result;
/* 24 */     taskInfoData.exception = taskInfo.error;
/*    */     
/* 26 */     return taskInfoData;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/data/TaskInfoData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */