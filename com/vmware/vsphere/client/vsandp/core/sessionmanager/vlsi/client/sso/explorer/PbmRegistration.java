/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.explorer;
/*    */ 
/*    */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PbmRegistration
/*    */   extends AbstractLsRegistration
/*    */ {
/*    */   public PbmRegistration(ServiceRegistration.Info info) {
/* 17 */     super(info);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UUID getUuid() {
/* 25 */     return UUID.fromString(findAttribute("com.vmware.cis.cm.HostId").getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSolutionUser() {
/* 33 */     String result = getOwnerId();
/* 34 */     int separator = result.lastIndexOf('@');
/* 35 */     if (separator != -1) {
/* 36 */       result = result.substring(0, separator);
/*    */     }
/* 38 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/PbmRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */