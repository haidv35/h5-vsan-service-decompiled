/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.ExtensionManager;
/*    */ import com.vmware.vim.binding.vim.ServiceInstanceContent;
/*    */ import com.vmware.vim.binding.vim.SessionManager;
/*    */ import com.vmware.vim.binding.vim.UserSession;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VcConnection
/*    */   extends VlsiConnection
/*    */ {
/*    */   protected UserSession session;
/*    */   protected ServiceInstanceContent content;
/*    */   
/*    */   public UserSession getSession() {
/* 19 */     return this.session;
/*    */   }
/*    */   
/*    */   public void setSession(UserSession session) {
/* 23 */     this.session = session;
/*    */   }
/*    */   
/*    */   public SessionManager getSessionManager() {
/* 27 */     return (SessionManager)createStub(SessionManager.class, this.content.getSessionManager());
/*    */   }
/*    */   
/*    */   public ExtensionManager getExtensionManager() {
/* 31 */     return (ExtensionManager)createStub(ExtensionManager.class, this.content.getExtensionManager());
/*    */   }
/*    */   
/*    */   public ServiceInstanceContent getContent() {
/* 35 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     if (this.settings != null && this.content != null)
/* 41 */       return String.format("VcConnection(host=%s, uuid=%s)", new Object[] {
/* 42 */             this.settings.getHttpSettings().getHost(), 
/* 43 */             this.content.getAbout().getInstanceUuid()
/*    */           }); 
/* 45 */     return "VcConnection(initializing)";
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/VcConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */