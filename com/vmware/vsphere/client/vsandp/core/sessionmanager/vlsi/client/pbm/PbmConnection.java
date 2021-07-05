/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.ServiceInstanceContent;
/*    */ import com.vmware.vim.binding.pbm.auth.SessionManager;
/*    */ import com.vmware.vim.binding.pbm.compliance.ComplianceManager;
/*    */ import com.vmware.vim.binding.pbm.placement.PlacementSolver;
/*    */ import com.vmware.vim.binding.pbm.profile.ProfileManager;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PbmConnection
/*    */   extends VlsiConnection
/*    */ {
/*    */   protected ServiceInstanceContent content;
/*    */   
/*    */   public ServiceInstanceContent getContent() {
/* 19 */     return this.content;
/*    */   }
/*    */   
/*    */   public SessionManager getSessionManager() {
/* 23 */     return (SessionManager)createStub(SessionManager.class, this.content.getSessionManager());
/*    */   }
/*    */   
/*    */   public ProfileManager getProfileManager() {
/* 27 */     return (ProfileManager)createStub(ProfileManager.class, this.content.getProfileManager());
/*    */   }
/*    */   
/*    */   public ComplianceManager getComplianceManager() {
/* 31 */     return (ComplianceManager)createStub(ComplianceManager.class, this.content.getComplianceManager());
/*    */   }
/*    */   
/*    */   public PlacementSolver getPlacementSolver() {
/* 35 */     return (PlacementSolver)createStub(PlacementSolver.class, this.content.getPlacementSolver());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     if (this.settings != null && this.content != null)
/* 41 */       return String.format("PbmConnection(host=%s, uuid=%s)", new Object[] {
/* 42 */             this.settings.getHttpSettings().getHost(), 
/* 43 */             this.content.getAboutInfo().getInstanceUuid()
/*    */           }); 
/* 45 */     return "PbmConnection(initializing)";
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/pbm/PbmConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */