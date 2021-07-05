/*    */ package com.vmware.vsphere.client.vsan.iscsi.utils;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.util.Utils;
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
/*    */ 
/*    */ 
/*    */ public class VsanIscsiUtil
/*    */ {
/*    */   public static final String iscsiEnableInProgressErrMsg = "vSAN iSCSI Target Service is not enabled or the enable task is in progress.";
/*    */   public static final String TASK_TYPE = "Task";
/*    */   
/*    */   public static String getLocalizedString(String key) {
/* 26 */     return Utils.getLocalizedString(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ManagedObjectReference buildTaskMor(String taskId, String vcGuid) {
/* 36 */     ManagedObjectReference task = new ManagedObjectReference(
/* 37 */         "Task", taskId, vcGuid);
/* 38 */     return task;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/utils/VsanIscsiUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */