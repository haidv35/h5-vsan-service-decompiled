/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vim.binding.pbm.capability.CapabilityInstance;
/*    */ import com.vmware.vim.binding.pbm.capability.ConstraintInstance;
/*    */ import com.vmware.vim.binding.pbm.capability.PropertyInstance;
/*    */ import com.vmware.vim.binding.pbm.profile.CapabilityBasedProfile;
/*    */ import com.vmware.vim.binding.pbm.profile.Profile;
/*    */ import com.vmware.vim.binding.pbm.profile.SubProfileCapabilityConstraints;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.lang.ArrayUtils;
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
/*    */ public class StoragePolicyData
/*    */ {
/*    */   private static final String LOCAL_PROTECTION = "localProtection";
/*    */   private static final String HOST_FAILURES_TO_TOLERATE = "hostFailuresToTolerate";
/*    */   public String id;
/*    */   public String name;
/*    */   public Boolean isDefault;
/*    */   public Boolean isDataProtection;
/*    */   public Integer ftt;
/*    */   public boolean isCompatible = false;
/*    */   public boolean hasVsanNamespace = false;
/*    */   
/*    */   public StoragePolicyData() {}
/*    */   
/*    */   public StoragePolicyData(String defaultProfileId, Profile profile) {
/* 49 */     this.id = profile.getProfileId().getUniqueId();
/* 50 */     this.isDefault = Boolean.valueOf((defaultProfileId != null && this.id.equals(defaultProfileId)));
/* 51 */     this.name = profile.getName();
/*    */     
/* 53 */     if (profile instanceof CapabilityBasedProfile) {
/* 54 */       CapabilityBasedProfile capabilityBasedProfile = (CapabilityBasedProfile)profile;
/*    */       
/* 56 */       if (capabilityBasedProfile.constraints instanceof SubProfileCapabilityConstraints) {
/* 57 */         SubProfileCapabilityConstraints constraints = 
/* 58 */           (SubProfileCapabilityConstraints)capabilityBasedProfile.constraints;
/* 59 */         if (ArrayUtils.isEmpty((Object[])constraints.subProfiles))
/*    */           return;  byte b; int i;
/*    */         SubProfileCapabilityConstraints.SubProfile[] arrayOfSubProfile;
/* 62 */         for (i = (arrayOfSubProfile = constraints.subProfiles).length, b = 0; b < i; ) { SubProfileCapabilityConstraints.SubProfile subProfile = arrayOfSubProfile[b];
/* 63 */           if (ArrayUtils.isEmpty((Object[])subProfile.capability))
/*    */             return;  byte b1; int j;
/*    */           CapabilityInstance[] arrayOfCapabilityInstance;
/* 66 */           for (j = (arrayOfCapabilityInstance = subProfile.capability).length, b1 = 0; b1 < j; ) { CapabilityInstance capability = arrayOfCapabilityInstance[b1];
/* 67 */             if ("VSAN".equals(capability.id.namespace)) {
/* 68 */               this.hasVsanNamespace = true;
/*    */             }
/* 70 */             if (capability.id.id.equals("localProtection"))
/* 71 */               this.isDataProtection = Boolean.valueOf(true);  byte b2; int k;
/*    */             ConstraintInstance[] arrayOfConstraintInstance;
/* 73 */             for (k = (arrayOfConstraintInstance = capability.constraint).length, b2 = 0; b2 < k; ) { ConstraintInstance constraintInstance = arrayOfConstraintInstance[b2];
/* 74 */               if (ArrayUtils.isEmpty((Object[])constraintInstance.propertyInstance))
/*    */                 return;  byte b3; int m;
/*    */               PropertyInstance[] arrayOfPropertyInstance;
/* 77 */               for (m = (arrayOfPropertyInstance = constraintInstance.propertyInstance).length, b3 = 0; b3 < m; ) { PropertyInstance propertyInstance = arrayOfPropertyInstance[b3];
/* 78 */                 if (propertyInstance.id.equals("hostFailuresToTolerate"))
/* 79 */                   this.ftt = (Integer)propertyInstance.value; 
/*    */                 b3++; }
/*    */               
/*    */               b2++; }
/*    */             
/*    */             b1++; }
/*    */           
/*    */           b++; }
/*    */       
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/StoragePolicyData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */