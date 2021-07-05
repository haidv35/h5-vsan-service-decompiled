/*    */ package com.vmware.vsan.client.services.hci.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.host.HostAccessManager;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum LockdownMode
/*    */ {
/* 14 */   DISABLED(HostAccessManager.LockdownMode.lockdownDisabled),
/* 15 */   NORMAL(HostAccessManager.LockdownMode.lockdownNormal),
/* 16 */   STRICT(HostAccessManager.LockdownMode.lockdownStrict);
/*    */   
/*    */   private HostAccessManager.LockdownMode vmodlLockdownMode;
/*    */   
/*    */   LockdownMode(HostAccessManager.LockdownMode lockdownMode) {
/* 21 */     this.vmodlLockdownMode = lockdownMode;
/*    */   }
/*    */   
/*    */   public HostAccessManager.LockdownMode getVmodlLockdownMode() {
/* 25 */     return this.vmodlLockdownMode;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/LockdownMode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */