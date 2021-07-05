/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.explorer;
/*    */ 
/*    */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*    */ import java.net.URI;
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
/*    */ 
/*    */ public abstract class AbstractLsRegistration
/*    */ {
/*    */   protected final ServiceRegistration.Info info;
/*    */   
/*    */   public AbstractLsRegistration(ServiceRegistration.Info info) {
/* 26 */     this.info = info;
/*    */   }
/*    */   
/*    */   public UUID getUuid() {
/* 30 */     return UUID.fromString(this.info.getServiceId());
/*    */   }
/*    */   
/*    */   public String getOwnerId() {
/* 34 */     return this.info.getOwnerId();
/*    */   }
/*    */   
/*    */   public String getVersion() {
/* 38 */     return this.info.getServiceVersion();
/*    */   }
/*    */   
/*    */   public URI getServiceUrl() {
/* 42 */     return getDefaultEndpoint().getUrl();
/*    */   }
/*    */   
/*    */   public String[] getSslTrust() {
/* 46 */     return getDefaultEndpoint().getSslTrust();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return String.format("%s [uuid=%s]", new Object[] {
/* 52 */           getClass().getSimpleName(), this.info.getServiceId() }); }
/*    */   protected ServiceRegistration.Attribute findAttribute(String attrName) { byte b;
/*    */     int i;
/*    */     ServiceRegistration.Attribute[] arrayOfAttribute;
/* 56 */     for (i = (arrayOfAttribute = this.info.getServiceAttributes()).length, b = 0; b < i; ) { ServiceRegistration.Attribute a = arrayOfAttribute[b];
/* 57 */       if (a.key.equals(attrName))
/* 58 */         return a; 
/*    */       b++; }
/*    */     
/* 61 */     throw new IllegalStateException("Attribute not found: " + attrName); } protected ServiceRegistration.Endpoint findEndpoint(String type) {
/*    */     byte b;
/*    */     int i;
/*    */     ServiceRegistration.Endpoint[] arrayOfEndpoint;
/* 65 */     for (i = (arrayOfEndpoint = this.info.getServiceEndpoints()).length, b = 0; b < i; ) { ServiceRegistration.Endpoint e = arrayOfEndpoint[b];
/* 66 */       if (e.getEndpointType().getType().equals(type))
/* 67 */         return e; 
/*    */       b++; }
/*    */     
/* 70 */     throw new IllegalStateException("Endpoint not found: " + type);
/*    */   }
/*    */   
/*    */   protected ServiceRegistration.Endpoint getDefaultEndpoint() {
/* 74 */     if (this.info.serviceEndpoints.length == 1) {
/* 75 */       return this.info.getServiceEndpoints()[0];
/*    */     }
/* 77 */     throw new IllegalStateException("Could not determine default endpoint, only one expected in query result.");
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/AbstractLsRegistration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */