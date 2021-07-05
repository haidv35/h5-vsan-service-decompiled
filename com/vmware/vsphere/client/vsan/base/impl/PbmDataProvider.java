/*     */ package com.vmware.vsphere.client.vsan.base.impl;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.pbm.ServerObjectRef;
/*     */ import com.vmware.vim.binding.pbm.placement.CapabilityProfileRequirement;
/*     */ import com.vmware.vim.binding.pbm.placement.CompatibilityResult;
/*     */ import com.vmware.vim.binding.pbm.placement.PlacementHub;
/*     */ import com.vmware.vim.binding.pbm.placement.PlacementSolver;
/*     */ import com.vmware.vim.binding.pbm.profile.CapabilityBasedProfile;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.pbm.profile.ProfileId;
/*     */ import com.vmware.vim.binding.pbm.profile.ProfileManager;
/*     */ import com.vmware.vim.binding.pbm.profile.ResourceType;
/*     */ import com.vmware.vim.binding.pbm.profile.ResourceTypeEnum;
/*     */ import com.vmware.vim.binding.vim.Datastore;
/*     */ import com.vmware.vim.binding.vim.VirtualMachine;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vmomi.core.impl.BlockingFuture;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlType;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlTypeMap;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.data.StoragePolicyData;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.MessageBundle;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.PbmClient;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm.PbmConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ public class PbmDataProvider {
/*     */   @Autowired
/*     */   private PbmClient pbmClient;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   @Autowired
/*     */   private MessageBundle messages;
/*  45 */   private static final Logger logger = LoggerFactory.getLogger(PbmDataProvider.class);
/*     */   private static final String DATASTORE_WSDL_NAME;
/*     */   
/*     */   static {
/*  49 */     VmodlTypeMap vmodlTypes = VmodlTypeMap.Factory.getTypeMap();
/*  50 */     VmodlType dsVmodlType = vmodlTypes.getVmodlType(Datastore.class);
/*  51 */     DATASTORE_WSDL_NAME = dsVmodlType.getWsdlName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<StoragePolicyData> getStoragePolicies(ManagedObjectReference clusterRef) throws Exception {
/*  60 */     return getStoragePolicies(clusterRef, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<StoragePolicyData> getObjectCompatibleStoragePolicies(ManagedObjectReference objectRef) throws Exception {
/*  70 */     ManagedObjectReference clusterRef = BaseUtils.getCluster(objectRef);
/*  71 */     return getStoragePolicies(clusterRef, true); } private List<StoragePolicyData> getStoragePolicies(ManagedObjectReference clusterRef, boolean compatibleOnly) throws Exception { Profile[] profiles;
/*     */     String defaultProfileId;
/*     */     byte b;
/*     */     int i;
/*     */     Profile[] arrayOfProfile1;
/*  76 */     List<StoragePolicyData> result = new ArrayList<>();
/*  77 */     Map<String, ProfileId> compatibleProfiles = new HashMap<>();
/*  78 */     ManagedObjectReference vsanDatastore = getVsanDatastore(clusterRef);
/*     */     
/*  80 */     Exception exception1 = null, exception2 = null; try { PbmConnection pbmConn = this.pbmClient.getConnection(clusterRef.getServerGuid()); 
/*  81 */       try { ProfileManager profileManager = pbmConn.getProfileManager();
/*  82 */         PlacementSolver placementSolver = pbmConn.getPlacementSolver();
/*     */         
/*  84 */         ResourceType resource = new ResourceType(ResourceTypeEnum.STORAGE.name());
/*  85 */         String category = CapabilityBasedProfile.ProfileCategoryEnum.REQUIREMENT.name();
/*  86 */         ProfileId[] profileIds = profileManager.queryProfile(resource, category);
/*     */         
/*  88 */         if (vsanDatastore != null) {
/*  89 */           Map<Future<CompatibilityResult[]>, ProfileId> requirementFutures = new HashMap<>();
/*  90 */           PlacementHub pbmHub = new PlacementHub(DATASTORE_WSDL_NAME, vsanDatastore.getValue()); byte b1; int j; ProfileId[] arrayOfProfileId;
/*  91 */           for (j = (arrayOfProfileId = profileIds).length, b1 = 0; b1 < j; ) { ProfileId profileId = arrayOfProfileId[b1];
/*  92 */             CapabilityProfileRequirement capabilityProfileRequirement = new CapabilityProfileRequirement();
/*  93 */             capabilityProfileRequirement.profileId = profileId;
/*  94 */             BlockingFuture blockingFuture = new BlockingFuture();
/*  95 */             placementSolver.checkCompatibility(new PlacementHub[] { pbmHub }, profileId, (Future)blockingFuture);
/*  96 */             requirementFutures.put(blockingFuture, profileId);
/*     */             b1++; }
/*     */           
/*  99 */           for (Future<CompatibilityResult[]> requirementFuture : requirementFutures.keySet()) {
/* 100 */             CompatibilityResult[] results = (CompatibilityResult[])requirementFuture.get();
/* 101 */             if (results != null && results.length > 0 && (results[0]).error == null) {
/* 102 */               ProfileId profileId = requirementFutures.get(requirementFuture);
/* 103 */               compatibleProfiles.put(profileId.getUniqueId(), profileId);
/*     */             } 
/*     */           } 
/*     */           
/* 107 */           if (compatibleOnly) {
/* 108 */             profileIds = (ProfileId[])compatibleProfiles.values().toArray((Object[])new ProfileId[compatibleProfiles.size()]);
/*     */           }
/*     */         } 
/* 111 */         profiles = profileManager.retrieveContent(profileIds); }
/* 112 */       finally { if (pbmConn != null) pbmConn.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
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
/* 126 */     Collections.sort(result, new Comparator<StoragePolicyData>()
/*     */         {
/*     */           public int compare(StoragePolicyData lhs, StoragePolicyData rhs) {
/* 129 */             return lhs.name.compareToIgnoreCase(rhs.name);
/*     */           }
/*     */         });
/*     */     
/* 133 */     return result; }
/*     */ 
/*     */   
/*     */   private String getServerObjectType(ManagedObjectReference moRef) {
/* 137 */     if (this.vmodlHelper.isOfType(moRef, Datastore.class))
/* 138 */       return ServerObjectRef.ObjectType.datastore.name(); 
/* 139 */     if (this.vmodlHelper.isOfType(moRef, VirtualMachine.class)) {
/* 140 */       return ServerObjectRef.ObjectType.virtualMachine.name();
/*     */     }
/* 142 */     throw new IllegalArgumentException("Unsupported managed object reference: " + moRef.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDefaultStorageProfileId(ManagedObjectReference clusterRef, ManagedObjectReference vsanDatastore) {
/* 148 */     String defaultProfileId = null; try {
/* 149 */       Exception exception2, exception1 = null;
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
/* 160 */     catch (Exception exception) {
/* 161 */       logger.error("Unable to find the default storage policy.", exception);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 166 */     return defaultProfileId;
/*     */   }
/*     */   
/*     */   private ManagedObjectReference getVsanDatastore(ManagedObjectReference vsanClusterRef) throws Exception {
/*     */     try {
/* 171 */       PropertyValue[] datastores = QueryUtil.getPropertyForRelatedObjects(vsanClusterRef, "datastore", 
/* 172 */           Datastore.class.getSimpleName(), "summary.type").getPropertyValues(); byte b; int i;
/*     */       PropertyValue[] arrayOfPropertyValue1;
/* 174 */       for (i = (arrayOfPropertyValue1 = datastores).length, b = 0; b < i; ) { PropertyValue datastore = arrayOfPropertyValue1[b];
/* 175 */         if (datastore.value.equals("vsan")) {
/* 176 */           return (ManagedObjectReference)datastore.resourceObject;
/*     */         }
/*     */ 
/*     */         
/*     */         b++; }
/*     */ 
/*     */       
/* 183 */       return null;
/* 184 */     } catch (Exception e) {
/* 185 */       logger.error("Unable to retrieve vSAN Datastore.", e);
/* 186 */       throw new Exception(this.messages.string("dataproviders.spbm.datastore"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/impl/PbmDataProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */