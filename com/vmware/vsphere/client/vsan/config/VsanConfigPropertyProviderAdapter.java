/*     */ package com.vmware.vsphere.client.vsan.config;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsan.client.services.encryption.EncryptionStatus;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.data.EncryptionState;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ public class VsanConfigPropertyProviderAdapter implements PropertyProviderAdapter {
/*  26 */   private static final Log logger = LogFactory.getLog(VsanConfigPropertyProviderAdapter.class);
/*     */   
/*  28 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  29 */       VsanConfigPropertyProviderAdapter.class);
/*     */   
/*     */   public static final String VSAN_CONFIG_INFO = "vsanConfigInfo";
/*     */   public static final String VSAN_ENCRYPTION_STATUS = "vsanEncryptionStatus";
/*     */   public static final String VSAN_RESYNC_THROTTLING = "vsanResyncThrottling";
/*     */   
/*     */   public VsanConfigPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  36 */     Validate.notNull(registry);
/*     */     
/*  38 */     String[] properties = {
/*  39 */         "vsanConfigInfo", 
/*  40 */         "vsanEncryptionStatus", 
/*  41 */         "dataEfficiencyStatus", 
/*  42 */         "vsanResyncThrottling"
/*     */       };
/*  44 */     TypeInfo clusterInfo = 
/*  45 */       createTypeInfo(ClusterComputeResource.class.getSimpleName(), properties);
/*  46 */     TypeInfo datastoreInfo = createTypeInfo(Datastore.class.getSimpleName(), properties);
/*     */     
/*  48 */     TypeInfo[] providedProperties = { clusterInfo, datastoreInfo };
/*  49 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */   
/*     */   private TypeInfo createTypeInfo(String type, String[] properties) {
/*  53 */     TypeInfo typeInfo = new TypeInfo();
/*  54 */     typeInfo.type = type;
/*  55 */     typeInfo.properties = properties;
/*  56 */     return typeInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  61 */     if (!isValidRequest(propertyRequest)) {
/*  62 */       ResultSet result = new ResultSet();
/*  63 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/*  64 */       return result;
/*     */     } 
/*     */     
/*  67 */     List<ResultItem> resultItems = new ArrayList<>(); byte b; int i; Object[] arrayOfObject;
/*  68 */     for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object objectRef = arrayOfObject[b];
/*  69 */       ManagedObjectReference clusterRef = null;
/*  70 */       if (objectRef instanceof ManagedObjectReference) {
/*  71 */         clusterRef = BaseUtils.getCluster((ManagedObjectReference)objectRef);
/*     */       }
/*  73 */       if (clusterRef != null) {
/*     */ 
/*     */ 
/*     */         
/*  77 */         ConfigInfoEx config = null;
/*     */         
/*  79 */         if (shouldRequestConfigInfo(clusterRef, propertyRequest)) {
/*  80 */           VsanVcClusterConfigSystem vsanConfigSystem = 
/*  81 */             VsanProviderUtils.getVsanConfigSystem(clusterRef); try {
/*  82 */             Exception exception2, exception1 = null;
/*     */           }
/*  84 */           catch (Exception ex) {
/*  85 */             logger.error("Could not retrieve cluster's config info", ex);
/*  86 */             ResultSet resultSet1 = new ResultSet();
/*  87 */             resultSet1.error = ex;
/*  88 */             return resultSet1;
/*     */           } 
/*     */         } 
/*     */         
/*  92 */         List<PropertyValue> propValues = new ArrayList<>();
/*  93 */         if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanConfigInfo" })) {
/*  94 */           PropertyValue propValue = QueryUtil.newProperty(
/*  95 */               "vsanConfigInfo", getVsanConfigValue(clusterRef, config));
/*  96 */           propValue.resourceObject = objectRef;
/*  97 */           propValues.add(propValue);
/*     */         } 
/*     */         
/* 100 */         if (QueryUtil.isAnyPropertyRequested(
/* 101 */             propertyRequest.properties, new String[] { "dataEfficiencyStatus" })) {
/* 102 */           PropertyValue propValue = QueryUtil.newProperty(
/* 103 */               "dataEfficiencyStatus", 
/* 104 */               Boolean.valueOf(getDedupStatusValue(clusterRef, config)));
/* 105 */           propValue.resourceObject = objectRef;
/* 106 */           propValues.add(propValue);
/*     */         } 
/*     */         
/* 109 */         if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanEncryptionStatus" })) {
/* 110 */           PropertyValue propValue = QueryUtil.newProperty(
/* 111 */               "vsanEncryptionStatus", getEncryptionStatusValue(clusterRef, config));
/* 112 */           propValue.resourceObject = objectRef;
/* 113 */           propValues.add(propValue);
/*     */         } 
/*     */         
/* 116 */         if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanResyncThrottling" })) {
/* 117 */           PropertyValue propValue = QueryUtil.newProperty(
/* 118 */               "vsanResyncThrottling", Integer.valueOf(getResyncThrottlingStatusValue(clusterRef, config)));
/* 119 */           propValue.resourceObject = objectRef;
/* 120 */           propValues.add(propValue);
/*     */         } 
/*     */         
/* 123 */         ResultItem resultItem = QueryUtil.newResultItem(
/* 124 */             objectRef, propValues.<PropertyValue>toArray(new PropertyValue[propValues.size()]));
/* 125 */         resultItems.add(resultItem);
/*     */       }  b++; }
/*     */     
/* 128 */     ResultSet resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/* 129 */     return resultSet;
/*     */   }
/*     */   
/*     */   private ConfigInfoEx getVsanConfigValue(ManagedObjectReference clusterRef, ConfigInfoEx config) {
/* 133 */     return VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef) ? config : null;
/*     */   }
/*     */   
/*     */   private boolean getDedupStatusValue(ManagedObjectReference clusterRef, ConfigInfoEx config) {
/* 137 */     if (!VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef)) {
/* 138 */       return false;
/*     */     }
/*     */     
/* 141 */     boolean enabled = false;
/*     */     
/* 143 */     if (config != null && config.dataEfficiencyConfig != null) {
/* 144 */       enabled = config.dataEfficiencyConfig.isDedupEnabled();
/*     */     }
/*     */     
/* 147 */     return enabled;
/*     */   }
/*     */   
/*     */   private EncryptionStatus getEncryptionStatusValue(ManagedObjectReference clusterRef, ConfigInfoEx config) {
/* 151 */     EncryptionStatus status = new EncryptionStatus();
/* 152 */     status.state = EncryptionState.Disabled;
/*     */     
/* 154 */     if (!VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef) || 
/* 155 */       config == null || config.dataEncryptionConfig == null) {
/* 156 */       return status;
/*     */     }
/*     */     
/* 159 */     status.state = config.dataEncryptionConfig.encryptionEnabled ? EncryptionState.Enabled : EncryptionState.Disabled;
/* 160 */     status.kmipClusterId = (config.dataEncryptionConfig.kmsProviderId == null) ? 
/* 161 */       "" : config.dataEncryptionConfig.kmsProviderId.id;
/* 162 */     status.eraseDisksBeforeUse = config.dataEncryptionConfig.eraseDisksBeforeUse;
/*     */     
/* 164 */     if (status.state == EncryptionState.Enabled && "".equals(status.kmipClusterId)) {
/* 165 */       status.state = EncryptionState.EnabledNoKmip;
/*     */     }
/*     */     
/* 168 */     return status;
/*     */   }
/*     */   
/*     */   private int getResyncThrottlingStatusValue(ManagedObjectReference clusterRef, ConfigInfoEx config) {
/* 172 */     int result = -1;
/* 173 */     if (!VsanCapabilityUtils.isResyncThrottlingSupported(clusterRef)) {
/* 174 */       return result;
/*     */     }
/* 176 */     if (config != null && config.resyncIopsLimitConfig != null) {
/* 177 */       result = config.resyncIopsLimitConfig.resyncIops;
/*     */     }
/* 179 */     return result;
/*     */   }
/*     */   
/*     */   private boolean shouldRequestConfigInfo(ManagedObjectReference clusterRef, PropertyRequestSpec propertyRequest) {
/* 183 */     boolean shouldRequestVsanConfig = false;
/* 184 */     if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanConfigInfo"
/* 185 */         }) && VsanCapabilityUtils.isClusterConfigSystemSupportedOnVc(clusterRef)) {
/* 186 */       shouldRequestVsanConfig = true;
/* 187 */     } else if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] {
/* 188 */           "dataEfficiencyStatus"
/* 189 */         }) && VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef)) {
/* 190 */       shouldRequestVsanConfig = true;
/* 191 */     } else if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanEncryptionStatus"
/* 192 */         }) && VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef)) {
/* 193 */       shouldRequestVsanConfig = true;
/* 194 */     } else if (QueryUtil.isAnyPropertyRequested(propertyRequest.properties, new String[] { "vsanResyncThrottling"
/* 195 */         }) && VsanCapabilityUtils.isResyncThrottlingSupported(clusterRef)) {
/* 196 */       shouldRequestVsanConfig = true;
/*     */     } 
/* 198 */     return shouldRequestVsanConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidRequest(PropertyRequestSpec propertyRequest) {
/* 205 */     if (propertyRequest == null) {
/* 206 */       return false;
/*     */     }
/* 208 */     if (ArrayUtils.isEmpty(propertyRequest.objects) || 
/* 209 */       ArrayUtils.isEmpty((Object[])propertyRequest.properties)) {
/* 210 */       logger.error("Property provider adapter got a null or empty list of properties or objects");
/* 211 */       return false;
/*     */     } 
/* 213 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/config/VsanConfigPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */