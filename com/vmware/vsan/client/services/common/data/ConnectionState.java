/*    */ package com.vmware.vsan.client.services.common.data;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.HostSystem;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ @data
/*    */ public enum ConnectionState
/*    */ {
/*  9 */   connected,
/* 10 */   notResponding,
/* 11 */   disconnected;
/*    */   
/*    */   public static ConnectionState fromHostState(HostSystem.ConnectionState state) {
/* 14 */     switch (state) { case null:
/* 15 */         return connected;
/* 16 */       case notResponding: return notResponding; }
/*    */     
/* 18 */     return disconnected;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/data/ConnectionState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */