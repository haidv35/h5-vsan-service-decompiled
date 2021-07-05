/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService;
/*    */ import com.vmware.vim.vsandp.binding.vim.vsandp.cluster.ProtectionService;
/*    */ import com.vmware.vim.vsandp.binding.vim.vsandp.cluster.VsanDataProtectionRecoverySystem;
/*    */ import com.vmware.vim.vsandp.binding.vim.vsandp.dps.SessionManager;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DpConnection
/*    */   extends VlsiConnection
/*    */ {
/*    */   public InventoryService getInventoryService() {
/* 19 */     return (InventoryService)createStub(InventoryService.class, 
/* 20 */         new ManagedObjectReference("VsanDpClusterInventoryService", "vsan-dp-inventory-service"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtectionService getProtectionService() {
/* 29 */     return (ProtectionService)createStub(ProtectionService.class, 
/* 30 */         new ManagedObjectReference("VsanDpClusterProtectionService", "vsan-dp-protection-service"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VsanDataProtectionRecoverySystem getRecoveryService() {
/* 39 */     return (VsanDataProtectionRecoverySystem)createStub(VsanDataProtectionRecoverySystem.class, 
/* 40 */         new ManagedObjectReference("VsanDataProtectionRecoverySystem", "vsan-dp-recovery-system"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SessionManager getSessionManager() {
/* 49 */     return (SessionManager)createStub(SessionManager.class, 
/* 50 */         new ManagedObjectReference("VsanDpDpsSessionManager", "vsan-dp-session-manager"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     if (this.settings != null)
/* 56 */       return String.format("DpConnection(host=%s)", new Object[] {
/* 57 */             this.settings.getHttpSettings().getHost()
/*    */           }); 
/* 59 */     return "DpConnection(initializing)";
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */