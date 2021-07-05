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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VcLsExplorer
/*    */   extends AbstractLsExplorer<VcRegistration>
/*    */ {
/* 23 */   public static final ServiceRegistration.ServiceType VC_SERVICE_TYPE = new ServiceRegistration.ServiceType("com.vmware.cis", "vcenterserver");
/*    */ 
/*    */   
/* 26 */   public static final ServiceRegistration.EndpointType VC_ENDPOINT_TYPE = new ServiceRegistration.EndpointType("vmomi", "com.vmware.vim");
/*    */ 
/*    */   
/* 29 */   public static final ServiceRegistration.Filter ALL_VCS = new ServiceRegistration.Filter(null, null, VC_SERVICE_TYPE, VC_ENDPOINT_TYPE);
/*    */   
/*    */   public VcLsExplorer(ServiceRegistration lookupService) {
/* 32 */     super(lookupService);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VcRegistration createRegistration(ServiceRegistration.Info registrationInfo) {
/* 37 */     return new VcRegistration(registrationInfo);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void mapRegistration(VcRegistration registration, Map<UUID, VcRegistration> map) {
/* 43 */     map.put(registration.getUuid(), registration);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ServiceRegistration.Filter getFilter() {
/* 48 */     return ALL_VCS;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/VcLsExplorer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */