/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.SessionManager;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObject;
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
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
/*    */ public class SsoVcAuth
/*    */   extends VcAuthenticator
/*    */ {
/*    */   protected final PrivateKey privateKey;
/*    */   protected final SamlToken token;
/*    */   
/*    */   public SsoVcAuth(PrivateKey privateKey, SamlToken token, String locale) {
/* 27 */     super(locale);
/* 28 */     this.privateKey = privateKey;
/* 29 */     this.token = token;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void loginVc(final VcConnection connection) {
/* 35 */     CheckedRunnable.withoutChecked(new CheckedRunnable()
/*    */         {
/*    */           public void run() throws Exception {
/* 38 */             SessionManager sm = connection.getSessionManager();
/* 39 */             RequestContextUtil.withSignInfo((ManagedObject)sm, SsoVcAuth.this.privateKey, SsoVcAuth.this.token);
/*    */             
/* 41 */             connection.setSession(sm.loginByToken(SsoVcAuth.this.locale));
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public PrivateKey getPrivateKey() {
/* 47 */     return this.privateKey;
/*    */   }
/*    */   
/*    */   public SamlToken getToken() {
/* 51 */     return this.token;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     31;
/* 58 */     int result = super.hashCode();
/* 59 */     result = 
/* 60 */       31 * result + ((this.privateKey == null) ? 0 : this.privateKey.hashCode());
/* 61 */     result = 31 * result + ((this.token == null) ? 0 : this.token.hashCode());
/* 62 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 68 */     if (this == obj) {
/* 69 */       return true;
/*    */     }
/* 71 */     if (!super.equals(obj)) {
/* 72 */       return false;
/*    */     }
/* 74 */     if (getClass() != obj.getClass()) {
/* 75 */       return false;
/*    */     }
/* 77 */     SsoVcAuth other = (SsoVcAuth)obj;
/* 78 */     if (this.privateKey == null) {
/* 79 */       if (other.privateKey != null) {
/* 80 */         return false;
/*    */       }
/* 82 */     } else if (!this.privateKey.equals(other.privateKey)) {
/* 83 */       return false;
/*    */     } 
/* 85 */     if (this.token == null) {
/* 86 */       if (other.token != null) {
/* 87 */         return false;
/*    */       }
/* 89 */     } else if (!this.token.equals(other.token)) {
/* 90 */       return false;
/*    */     } 
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/SsoVcAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */