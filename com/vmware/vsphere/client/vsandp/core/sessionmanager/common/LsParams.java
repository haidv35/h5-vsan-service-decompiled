/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.AsModel;
/*    */ import java.net.URI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @AsModel
/*    */ public class LsParams
/*    */ {
/*    */   private String address;
/*    */   private String thumbprint;
/*    */   
/*    */   public LsParams() {}
/*    */   
/*    */   public LsParams(String address, String thumbprint) {
/* 23 */     this.address = address;
/* 24 */     this.thumbprint = thumbprint;
/*    */   }
/*    */   
/*    */   public String getAddress() {
/* 28 */     return this.address;
/*    */   }
/*    */   
/*    */   public void setAddress(String address) {
/* 32 */     this.address = address;
/*    */   }
/*    */   
/*    */   public String getThumbprint() {
/* 36 */     return this.thumbprint;
/*    */   }
/*    */   
/*    */   public void setThumbprint(String thumbprint) {
/* 40 */     this.thumbprint = thumbprint;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     StringBuilder sb = new StringBuilder("LsParams{");
/* 46 */     sb.append("address=").append(this.address);
/* 47 */     sb.append(", thumbprint='").append(this.thumbprint).append('\'');
/* 48 */     sb.append('}');
/* 49 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public LookupSvcInfo toLsInfo() {
/* 53 */     if (this.address == null || this.thumbprint == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     return new LookupSvcInfo(URI.create(this.address), this.thumbprint);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/LsParams.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */