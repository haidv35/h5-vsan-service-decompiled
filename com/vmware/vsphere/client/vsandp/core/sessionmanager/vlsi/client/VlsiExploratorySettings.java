/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.LookupSvcConnection;
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
/*    */ public class VlsiExploratorySettings
/*    */ {
/*    */   private final VlsiSettings serviceSettingsTemplate;
/*    */   private final ResourceFactory<LookupSvcConnection, VlsiSettings> lookupSvcFactory;
/*    */   private final VlsiSettings lookupSvcSettings;
/*    */   private final UUID serviceUuid;
/*    */   
/*    */   public VlsiExploratorySettings(VlsiSettings serviceSettingsTemplate, ResourceFactory<LookupSvcConnection, VlsiSettings> lookupSvcFactory, VlsiSettings lookupSvcSettings, UUID serviceUuid) {
/* 42 */     this.serviceSettingsTemplate = serviceSettingsTemplate;
/* 43 */     this.lookupSvcFactory = lookupSvcFactory;
/* 44 */     this.lookupSvcSettings = lookupSvcSettings;
/* 45 */     this.serviceUuid = serviceUuid;
/*    */   }
/*    */   
/*    */   public VlsiSettings getServiceSettingsTemplate() {
/* 49 */     return this.serviceSettingsTemplate;
/*    */   }
/*    */   
/*    */   public ResourceFactory<LookupSvcConnection, VlsiSettings> getLookupSvcFactory() {
/* 53 */     return this.lookupSvcFactory;
/*    */   }
/*    */   
/*    */   public VlsiSettings getLookupSvcSettings() {
/* 57 */     return this.lookupSvcSettings;
/*    */   }
/*    */   
/*    */   public UUID getServiceUuid() {
/* 61 */     return this.serviceUuid;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) return true; 
/* 67 */     if (!(o instanceof VlsiExploratorySettings)) return false;
/*    */     
/* 69 */     VlsiExploratorySettings that = (VlsiExploratorySettings)o;
/*    */     
/* 71 */     if (!this.lookupSvcFactory.equals(that.lookupSvcFactory)) return false; 
/* 72 */     if (!this.lookupSvcSettings.equals(that.lookupSvcSettings)) return false; 
/* 73 */     if (!this.serviceSettingsTemplate.equals(that.serviceSettingsTemplate)) return false; 
/* 74 */     if (!this.serviceUuid.equals(that.serviceUuid)) return false;
/*    */     
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 81 */     int result = this.serviceSettingsTemplate.hashCode();
/* 82 */     result = 31 * result + this.lookupSvcFactory.hashCode();
/* 83 */     result = 31 * result + this.lookupSvcSettings.hashCode();
/* 84 */     result = 31 * result + this.serviceUuid.hashCode();
/* 85 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     StringBuilder sb = new StringBuilder("VlsiExploratorySettings{");
/* 91 */     sb.append("serviceSettingsTemplate=").append(this.serviceSettingsTemplate);
/* 92 */     sb.append(", lookupSvcFactory=").append(this.lookupSvcFactory);
/* 93 */     sb.append(", lookupSvcSettings=").append(this.lookupSvcSettings);
/* 94 */     sb.append(", serviceUuid=").append(this.serviceUuid);
/* 95 */     sb.append('}');
/* 96 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/VlsiExploratorySettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */