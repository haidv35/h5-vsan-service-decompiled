/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.explorer;
/*    */ 
/*    */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*    */ import java.util.Map;
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
/*    */ public class PbmLsExplorer
/*    */   extends AbstractLsExplorer<PbmRegistration>
/*    */ {
/*    */   public PbmLsExplorer(ServiceRegistration lookupService) {
/* 19 */     super(lookupService);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PbmRegistration createRegistration(ServiceRegistration.Info registrationInfo) {
/* 24 */     return new PbmRegistration(registrationInfo);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void mapRegistration(PbmRegistration registration, Map<UUID, PbmRegistration> map) {
/* 29 */     map.put(registration.getUuid(), registration);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ServiceRegistration.Filter getFilter() {
/* 34 */     ServiceRegistration.ServiceType serviceType = new ServiceRegistration.ServiceType("com.vmware.vim.sms", "sms");
/* 35 */     ServiceRegistration.EndpointType endpointType = new ServiceRegistration.EndpointType("https", "com.vmware.vim.pbm");
/* 36 */     return new ServiceRegistration.Filter(null, null, serviceType, endpointType);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/PbmLsExplorer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */