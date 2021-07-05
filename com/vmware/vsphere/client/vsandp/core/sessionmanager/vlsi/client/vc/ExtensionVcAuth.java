/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
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
/*    */ public class ExtensionVcAuth
/*    */   extends VcAuthenticator
/*    */ {
/*    */   protected String extKey;
/*    */   
/*    */   public ExtensionVcAuth(String extKey, String locale) {
/* 22 */     super(locale);
/* 23 */     this.extKey = extKey;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loginVc(final VcConnection connection) {
/* 28 */     CheckedRunnable.withoutChecked(new CheckedRunnable()
/*    */         {
/*    */           public void run() throws Exception {
/* 31 */             connection.setSession(connection.getSessionManager()
/* 32 */                 .loginExtensionByCertificate(ExtensionVcAuth.this.extKey, ExtensionVcAuth.this.locale));
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public String getExtKey() {
/* 38 */     return this.extKey;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 44 */     31;
/* 45 */     int result = super.hashCode();
/* 46 */     result = 31 * result + ((this.extKey == null) ? 0 : this.extKey.hashCode());
/* 47 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 53 */     if (this == obj) {
/* 54 */       return true;
/*    */     }
/* 56 */     if (!super.equals(obj)) {
/* 57 */       return false;
/*    */     }
/* 59 */     if (getClass() != obj.getClass()) {
/* 60 */       return false;
/*    */     }
/* 62 */     ExtensionVcAuth other = (ExtensionVcAuth)obj;
/* 63 */     if (this.extKey == null) {
/* 64 */       if (other.extKey != null) {
/* 65 */         return false;
/*    */       }
/* 67 */     } else if (!this.extKey.equals(other.extKey)) {
/* 68 */       return false;
/*    */     } 
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/ExtensionVcAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */