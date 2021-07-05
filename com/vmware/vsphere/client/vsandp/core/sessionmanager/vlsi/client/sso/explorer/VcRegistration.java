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
/*    */ 
/*    */ public class VcRegistration
/*    */   extends AbstractLsRegistration
/*    */ {
/*    */   public VcRegistration(ServiceRegistration.Info info) {
/* 18 */     super(info);
/*    */   }
/*    */   
/*    */   public UUID getVpxdUuid() {
/* 22 */     return UUID.fromString(findAttribute("com.vmware.cis.cm.HostId").getValue());
/*    */   }
/*    */   
/*    */   public String getVcName() {
/* 26 */     return findAttribute("com.vmware.vim.vcenter.instanceName").getValue();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/VcRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */