/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.compliance.ComplianceResult;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ @data
/*    */ public class VsanObject
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String vsanObjectUuid;
/*    */   public String name;
/*    */   public VsanObjectType objectType;
/*    */   public String storagePolicy;
/*    */   public VsanComplianceStatus complianceStatus;
/*    */   public ComplianceResult complianceResult;
/*    */   public VsanObjectHealthState healthState;
/*    */   public VsanObjectDataProtectionHealthState dataProtectionHealthState;
/*    */   public Object namespaceCapabilityMetadata;
/*    */   public VsanRootConfig rootConfig;
/*    */   
/*    */   public VsanObject() {}
/*    */   
/*    */   public VsanObject(String objectUuid) {
/* 48 */     this.vsanObjectUuid = objectUuid;
/*    */   }
/*    */   
/*    */   public VsanObject(String objectUuid, List<VsanComponent> components) {
/* 52 */     this.vsanObjectUuid = objectUuid;
/* 53 */     this.rootConfig = new VsanRootConfig();
/* 54 */     this.rootConfig.children = components;
/*    */   }
/*    */   
/*    */   public void updateHealthData(Map<String, VsanObjectHealthData> vsanHealthData) {
/* 58 */     if (!vsanHealthData.containsKey(this.vsanObjectUuid)) {
/*    */       return;
/*    */     }
/*    */     
/* 62 */     VsanObjectHealthData vsanHealthInfo = vsanHealthData.get(this.vsanObjectUuid);
/* 63 */     this.healthState = VsanObjectHealthState.fromString(vsanHealthInfo.vsanHealthState);
/*    */ 
/*    */     
/* 66 */     this.storagePolicy = vsanHealthInfo.policyName;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 71 */     if (this == o) {
/* 72 */       return true;
/*    */     }
/* 74 */     if (o == null || getClass() != o.getClass()) {
/* 75 */       return false;
/*    */     }
/*    */     
/* 78 */     VsanObject that = (VsanObject)o;
/*    */     
/* 80 */     return this.vsanObjectUuid.equals(that.vsanObjectUuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return this.vsanObjectUuid.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */