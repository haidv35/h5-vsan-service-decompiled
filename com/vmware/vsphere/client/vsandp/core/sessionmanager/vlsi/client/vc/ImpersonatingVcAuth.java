/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.CheckedRunnable;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.Authenticator;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImpersonatingVcAuth
/*     */   extends Authenticator
/*     */ {
/*     */   protected Authenticator parentAuth;
/*     */   protected String userName;
/*     */   protected String locale;
/*     */   
/*     */   public ImpersonatingVcAuth(Authenticator parentAuth, String userName, String locale) {
/*  26 */     this.parentAuth = parentAuth;
/*  27 */     this.userName = userName;
/*  28 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void login(VlsiConnection connection) {
/*  33 */     this.parentAuth.login(connection);
/*  34 */     impersonate((VcConnection)connection);
/*     */   }
/*     */   
/*     */   protected void impersonate(final VcConnection connection) {
/*  38 */     CheckedRunnable.withoutChecked(new CheckedRunnable()
/*     */         {
/*     */           public void run() throws Exception {
/*  41 */             connection.setSession(connection.getSessionManager()
/*  42 */                 .impersonateUser(ImpersonatingVcAuth.this.userName, ImpersonatingVcAuth.this.locale));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void logout(VlsiConnection connection) {
/*  49 */     this.parentAuth.logout(connection);
/*     */   }
/*     */   
/*     */   public Authenticator getParentAuth() {
/*  53 */     return this.parentAuth;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/*  57 */     return this.userName;
/*     */   }
/*     */   
/*     */   public String getLocale() {
/*  61 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  67 */     31;
/*  68 */     int result = super.hashCode();
/*  69 */     result = 31 * result + ((this.locale == null) ? 0 : this.locale.hashCode());
/*  70 */     result = 
/*  71 */       31 * result + ((this.parentAuth == null) ? 0 : this.parentAuth.hashCode());
/*  72 */     result = 31 * result + ((this.userName == null) ? 0 : this.userName.hashCode());
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  79 */     if (this == obj) {
/*  80 */       return true;
/*     */     }
/*  82 */     if (!super.equals(obj)) {
/*  83 */       return false;
/*     */     }
/*  85 */     if (getClass() != obj.getClass()) {
/*  86 */       return false;
/*     */     }
/*  88 */     ImpersonatingVcAuth other = (ImpersonatingVcAuth)obj;
/*  89 */     if (this.locale == null) {
/*  90 */       if (other.locale != null) {
/*  91 */         return false;
/*     */       }
/*  93 */     } else if (!this.locale.equals(other.locale)) {
/*  94 */       return false;
/*     */     } 
/*  96 */     if (this.parentAuth == null) {
/*  97 */       if (other.parentAuth != null) {
/*  98 */         return false;
/*     */       }
/* 100 */     } else if (!this.parentAuth.equals(other.parentAuth)) {
/* 101 */       return false;
/*     */     } 
/* 103 */     if (this.userName == null) {
/* 104 */       if (other.userName != null) {
/* 105 */         return false;
/*     */       }
/* 107 */     } else if (!this.userName.equals(other.userName)) {
/* 108 */       return false;
/*     */     } 
/* 110 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/ImpersonatingVcAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */