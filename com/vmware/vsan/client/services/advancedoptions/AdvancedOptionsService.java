/*    */ package com.vmware.vsan.client.services.advancedoptions;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.VsanExtendedConfig;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class AdvancedOptionsService
/*    */ {
/*    */   private static final String IS_WITNESS = "isWitnessHost";
/*    */   
/*    */   @TsService
/*    */   public AdvancedOptionsInfo getAdvancedOptionsInfo(ManagedObjectReference clusterRef) throws Exception {
/* 26 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 27 */     ConfigInfoEx configInfoEx = vsanConfigSystem.getConfigInfoEx(clusterRef);
/* 28 */     VsanExtendedConfig extendedConfig = configInfoEx.getExtendedConfig();
/*    */     
/* 30 */     AdvancedOptionsInfo result = new AdvancedOptionsInfo();
/* 31 */     result.objectRepairTimer = extendedConfig.getObjectRepairTimer().longValue();
/* 32 */     result.isSiteReadLocalityEnabled = !extendedConfig.getDisableSiteReadLocality().booleanValue();
/* 33 */     result.isCustomizedSwapObjectEnabled = extendedConfig.getEnableCustomizedSwapObject().booleanValue();
/* 34 */     result.largeClusterSupportEnabled = extendedConfig.getLargeScaleClusterSupport().booleanValue();
/*    */     
/* 36 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public ManagedObjectReference configureAdvancedOptions(ManagedObjectReference clusterRef, AdvancedOptionsInfo advancedOptionsInfo) throws Exception {
/* 44 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*    */     
/* 46 */     ReconfigSpec spec = new ReconfigSpec();
/* 47 */     VsanExtendedConfig extendedConfig = new VsanExtendedConfig(
/* 48 */         Long.valueOf(advancedOptionsInfo.objectRepairTimer), 
/* 49 */         Boolean.valueOf(!advancedOptionsInfo.isSiteReadLocalityEnabled), 
/* 50 */         Boolean.valueOf(advancedOptionsInfo.isCustomizedSwapObjectEnabled), 
/* 51 */         Boolean.valueOf(advancedOptionsInfo.largeClusterSupportEnabled));
/* 52 */     spec.setExtendedConfig(extendedConfig);
/*    */     
/* 54 */     return vsanConfigSystem.reconfigureEx(clusterRef, spec);
/*    */   }
/*    */   
/*    */   @TsService
/*    */   public boolean getVsanStretchedCluster(ManagedObjectReference clusterRef) throws Exception {
/* 59 */     PropertyValue[] hostProps = QueryUtil.getPropertiesForRelatedObjects(
/* 60 */         clusterRef, 
/* 61 */         "allVsanHosts", 
/* 62 */         ClusterComputeResource.class.getSimpleName(), 
/* 63 */         new String[] { "isWitnessHost" }).getPropertyValues(); byte b; int i;
/*    */     PropertyValue[] arrayOfPropertyValue1;
/* 65 */     for (i = (arrayOfPropertyValue1 = hostProps).length, b = 0; b < i; ) { PropertyValue val = arrayOfPropertyValue1[b];
/* 66 */       if (val.propertyName.equals("isWitnessHost") && ((Boolean)val.value).booleanValue()) {
/* 67 */         return true;
/*    */       }
/*    */       b++; }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/advancedoptions/AdvancedOptionsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */