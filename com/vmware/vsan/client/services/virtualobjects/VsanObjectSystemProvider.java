/*     */ package com.vmware.vsan.client.services.virtualobjects;
/*     */ 
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectQuerySpec;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VsanObjectHealthData;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class VsanObjectSystemProvider {
/*  24 */   private static final Log _logger = LogFactory.getLog(VsanObjectSystemProvider.class);
/*  25 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanObjectSystemProvider.class);
/*     */ 
/*     */   
/*     */   public VsanObjectIdentity[] getObjectIdentities(ManagedObjectReference clusterRef, Set<String> vsanObjectUuids) {
/*  29 */     if (vsanObjectUuids == null || vsanObjectUuids.size() == 0) {
/*  30 */       return new VsanObjectIdentity[0];
/*     */     }
/*     */     
/*  33 */     try { Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {  }
/*     */       finally
/*  41 */       { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }  }  } catch (Exception ex)
/*  42 */     { _logger.error("Failed to retrieve VSAN object identities: ", ex);
/*     */       
/*  44 */       return new VsanObjectIdentity[0]; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, VsanObjectHealthData> getVsanHealthData(ManagedObjectReference clusterRef, Set<String> vsanObjectIds) {
/*  55 */     Map<String, VsanObjectHealthData> result = new HashMap<>();
/*  56 */     if (!VsanCapabilityUtils.isObjectSystemSupported(clusterRef) || 
/*  57 */       vsanObjectIds == null || vsanObjectIds.size() == 0) {
/*  58 */       return result;
/*     */     }
/*     */     try {
/*  61 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  81 */     catch (Exception e) {
/*  82 */       _logger.error("Cannot retrieve VsanObjectSystem: ", e);
/*     */     } 
/*  84 */     return result;
/*     */   }
/*     */   
/*     */   public static VsanObjectQuerySpec[] buildQuerySpecs(Set<String> vsanObjectIds) {
/*  88 */     List<VsanObjectQuerySpec> vsanQuerySpecs = new ArrayList<>();
/*  89 */     for (String objectId : vsanObjectIds) {
/*  90 */       vsanQuerySpecs.add(new VsanObjectQuerySpec(objectId, ""));
/*     */     }
/*  92 */     VsanObjectQuerySpec[] result = vsanQuerySpecs.<VsanObjectQuerySpec>toArray(new VsanObjectQuerySpec[vsanQuerySpecs.size()]);
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   private String getStoragePolicyName(String spbmPolicyId, List<Profile> storageProfiles) {
/*  97 */     if (StringUtils.isEmpty(spbmPolicyId)) {
/*  98 */       return Utils.getLocalizedString("vsan.monitor.physicalVirtualMapping.noneProfileLabel");
/*     */     }
/* 100 */     return BaseUtils.getProfileNameByUuid(storageProfiles, spbmPolicyId, true);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/VsanObjectSystemProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */