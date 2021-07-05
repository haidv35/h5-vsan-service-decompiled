/*    */ package com.vmware.vsphere.client.vsan.iscsi.models;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanIscsiTargetProviderParameter
/*    */ {
/*    */   public boolean requestNamespaceCapabilityMetadata;
/*    */   public boolean requestStorageProfiles;
/*    */   
/*    */   public VsanIscsiTargetProviderParameter() {}
/*    */   
/*    */   public VsanIscsiTargetProviderParameter(boolean requestNamespaceCapabilityMetadata, boolean requestStoragePolicie) {
/* 15 */     this.requestNamespaceCapabilityMetadata = requestNamespaceCapabilityMetadata;
/* 16 */     this.requestStorageProfiles = requestStoragePolicie;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/VsanIscsiTargetProviderParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */