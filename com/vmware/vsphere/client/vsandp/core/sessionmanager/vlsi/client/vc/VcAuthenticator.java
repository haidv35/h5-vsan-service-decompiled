/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.Authenticator;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class VcAuthenticator
/*    */   extends Authenticator
/*    */ {
/*    */   protected final String locale;
/*    */   
/*    */   public VcAuthenticator(String locale) {
/* 16 */     this.locale = locale;
/*    */   }
/*    */   
/*    */   public VcAuthenticator(String locale, int id) {
/* 20 */     super(id);
/* 21 */     this.locale = locale;
/*    */   }
/*    */ 
/*    */   
/*    */   public void login(VlsiConnection connection) {
/* 26 */     loginVc((VcConnection)connection);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void loginVc(VcConnection paramVcConnection);
/*    */   
/*    */   public void logout(VlsiConnection connection) {
/* 33 */     logoutVc((VcConnection)connection);
/*    */   }
/*    */   
/*    */   protected void logoutVc(VcConnection connection) {
/* 37 */     connection.getSessionManager().logout();
/*    */   }
/*    */   
/*    */   public String getLocale() {
/* 41 */     return this.locale;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     31;
/* 48 */     int result = super.hashCode();
/* 49 */     result = 31 * result + ((this.locale == null) ? 0 : this.locale.hashCode());
/* 50 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 56 */     if (this == obj) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (!super.equals(obj)) {
/* 60 */       return false;
/*    */     }
/* 62 */     if (getClass() != obj.getClass()) {
/* 63 */       return false;
/*    */     }
/* 65 */     VcAuthenticator other = (VcAuthenticator)obj;
/* 66 */     if (this.locale == null) {
/* 67 */       if (other.locale != null) {
/* 68 */         return false;
/*    */       }
/* 70 */     } else if (!this.locale.equals(other.locale)) {
/* 71 */       return false;
/*    */     } 
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/VcAuthenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */