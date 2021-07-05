/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
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
/*    */ public class Authenticator
/*    */ {
/*    */   protected final int id;
/*    */   
/*    */   public Authenticator() {
/* 17 */     this(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Authenticator(int id) {
/* 22 */     this.id = id;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void login(VlsiConnection connection) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void logout(VlsiConnection connection) {}
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 35 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 40 */     if (this == obj)
/* 41 */       return true; 
/* 42 */     if (obj == null)
/* 43 */       return false; 
/* 44 */     if (getClass() != obj.getClass())
/* 45 */       return false; 
/* 46 */     Authenticator other = (Authenticator)obj;
/* 47 */     if (this.id != other.id)
/* 48 */       return false; 
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/Authenticator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */