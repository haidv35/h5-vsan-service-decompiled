/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.binding.sso.SessionManager;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObject;
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.Authenticator;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util.RequestContextUtil;
/*    */ import java.security.PrivateKey;
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
/*    */ public class SsoAdminServiceAuthenticator
/*    */   extends Authenticator
/*    */ {
/*    */   protected final PrivateKey privateKey;
/*    */   protected final SamlToken token;
/*    */   
/*    */   public SsoAdminServiceAuthenticator(PrivateKey privateKey, SamlToken token) {
/* 29 */     this.privateKey = privateKey;
/* 30 */     this.token = token;
/*    */   }
/*    */ 
/*    */   
/*    */   public void login(VlsiConnection connection) {
/* 35 */     SessionManager sessionMgr = getSessionManager(connection);
/*    */     
/* 37 */     RequestContextUtil.withSignInfo((ManagedObject)sessionMgr, this.privateKey, this.token);
/* 38 */     sessionMgr.login();
/*    */   }
/*    */ 
/*    */   
/*    */   public void logout(VlsiConnection connection) {
/* 43 */     getSessionManager(connection).logout();
/*    */   }
/*    */   
/*    */   protected SessionManager getSessionManager(VlsiConnection connection) {
/* 47 */     return (SessionManager)connection.createStub(
/* 48 */         SessionManager.class, (
/* 49 */         (SsoAdminConnection)connection).getContent().getSessionManager());
/*    */   }
/*    */   
/*    */   public PrivateKey getPrivateKey() {
/* 53 */     return this.privateKey;
/*    */   }
/*    */   
/*    */   public SamlToken getToken() {
/* 57 */     return this.token;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     31;
/* 63 */     int result = super.hashCode();
/* 64 */     result = 
/* 65 */       31 * result + ((this.privateKey == null) ? 0 : this.privateKey.hashCode());
/* 66 */     result = 31 * result + ((this.token == null) ? 0 : this.token.hashCode());
/* 67 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 72 */     if (this == obj)
/* 73 */       return true; 
/* 74 */     if (!super.equals(obj))
/* 75 */       return false; 
/* 76 */     if (getClass() != obj.getClass())
/* 77 */       return false; 
/* 78 */     SsoAdminServiceAuthenticator other = (SsoAdminServiceAuthenticator)obj;
/* 79 */     if (this.privateKey == null) {
/* 80 */       if (other.privateKey != null)
/* 81 */         return false; 
/* 82 */     } else if (!this.privateKey.equals(other.privateKey)) {
/* 83 */       return false;
/* 84 */     }  if (this.token == null) {
/* 85 */       if (other.token != null)
/* 86 */         return false; 
/* 87 */     } else if (!this.token.equals(other.token)) {
/* 88 */       return false;
/* 89 */     }  return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoAdminServiceAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */