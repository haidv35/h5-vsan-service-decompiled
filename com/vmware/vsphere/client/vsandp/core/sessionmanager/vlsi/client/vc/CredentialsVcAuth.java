/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CredentialsVcAuth
/*    */   extends VcAuthenticator
/*    */ {
/*    */   protected final String user;
/*    */   protected final String pass;
/*    */   
/*    */   public CredentialsVcAuth(String user, String pass, String locale, int id) {
/* 15 */     super(locale, id);
/* 16 */     this.user = user;
/* 17 */     this.pass = pass;
/*    */   }
/*    */   
/*    */   public CredentialsVcAuth(String user, String pass, String locale) {
/* 21 */     super(locale);
/* 22 */     this.user = user;
/* 23 */     this.pass = pass;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loginVc(final VcConnection connection) {
/* 28 */     CheckedRunnable.withoutChecked(new CheckedRunnable()
/*    */         {
/*    */           public void run() throws Exception {
/* 31 */             connection.setSession(connection.getSessionManager().login(CredentialsVcAuth.this.user, 
/* 32 */                   CredentialsVcAuth.this.pass, CredentialsVcAuth.this.locale));
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public String getUser() {
/* 38 */     return this.user;
/*    */   }
/*    */   
/*    */   public String getPass() {
/* 42 */     return this.pass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 48 */     31;
/* 49 */     int result = super.hashCode();
/* 50 */     result = 31 * result + ((this.pass == null) ? 0 : this.pass.hashCode());
/* 51 */     result = 31 * result + ((this.user == null) ? 0 : this.user.hashCode());
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (!super.equals(obj)) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (getClass() != obj.getClass()) {
/* 65 */       return false;
/*    */     }
/* 67 */     CredentialsVcAuth other = (CredentialsVcAuth)obj;
/* 68 */     if (this.pass == null) {
/* 69 */       if (other.pass != null) {
/* 70 */         return false;
/*    */       }
/* 72 */     } else if (!this.pass.equals(other.pass)) {
/* 73 */       return false;
/*    */     } 
/* 75 */     if (this.user == null) {
/* 76 */       if (other.user != null) {
/* 77 */         return false;
/*    */       }
/* 79 */     } else if (!this.user.equals(other.user)) {
/* 80 */       return false;
/*    */     } 
/* 82 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/CredentialsVcAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */