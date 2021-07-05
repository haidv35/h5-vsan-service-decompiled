/*     */ package com.vmware.vsphere.client.vsan.capability;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.Folder;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapability;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilitySystem;
/*     */ import com.vmware.vise.data.PropertySpec;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanCapabilityPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*  37 */   private static final Log _logger = LogFactory.getLog(VsanCapabilityPropertyProviderAdapter.class);
/*     */   
/*     */   private static final String PROPERTY_IS_FILE_SERVICE_SUPPORTED = "isFileServiceSupported";
/*     */   private static final String PROPERTY_IS_HISTORICAL_CAPACITY_SUPPORTED = "isHistoricalCapacitySupported";
/*     */   private static final String PROPERTY_HAS_ELIGIBLE_HOSTS = "hasVsanEligibleHosts";
/*     */   private static final String HISTORICAL_CAPACITY = "historicalcapacity";
/*     */   private static final String PROPERTY_IS_VSAN_NESTED_FDS_SUPPORTED = "isVsanNestedFdsSupported";
/*     */   private static final String PROPERTY_IS_ISCSI_TARGETS_SUPPORTED_ON_VC = "isIscsiTargetsSupportedOnVc";
/*     */   
/*     */   public VsanCapabilityPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  47 */     Validate.notNull(registry);
/*     */     
/*  49 */     String[] clusterProperties = {
/*  50 */         "isFileServiceSupported", 
/*  51 */         "hasVsanEligibleHosts", 
/*  52 */         "isHistoricalCapacitySupported", 
/*  53 */         "isIscsiTargetsSupportedOnVc"
/*     */       };
/*  55 */     TypeInfo clusterInfo = new TypeInfo();
/*  56 */     clusterInfo.type = ClusterComputeResource.class.getSimpleName();
/*  57 */     clusterInfo.properties = clusterProperties;
/*     */     
/*  59 */     TypeInfo folderInfo = new TypeInfo();
/*  60 */     folderInfo.type = Folder.class.getSimpleName();
/*  61 */     folderInfo.properties = new String[] { "isVsanNestedFdsSupported" };
/*     */     
/*  63 */     TypeInfo[] providedProperties = { clusterInfo, folderInfo };
/*  64 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  69 */     if (!QueryUtil.isValidRequest(propertyRequest)) {
/*  70 */       ResultSet result = new ResultSet();
/*  71 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/*  72 */       return result;
/*     */     } 
/*     */     
/*  75 */     List<ResultItem> resultItems = new ArrayList<>(); byte b; int i; Object[] arrayOfObject;
/*  76 */     for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object objectRef = arrayOfObject[b];
/*  77 */       ManagedObjectReference moRef = (ManagedObjectReference)objectRef;
/*  78 */       if (objectRef != null) {
/*     */ 
/*     */ 
/*     */         
/*  82 */         ResultItem resultItem = null;
/*  83 */         if (Folder.class.getSimpleName().equals(moRef.getType())) {
/*  84 */           PropertyValue[] folderProperties = getFolderProperties(propertyRequest.properties, objectRef);
/*  85 */           resultItem = QueryUtil.newResultItem(objectRef, folderProperties);
/*  86 */         } else if (ClusterComputeResource.class.getSimpleName().equals(moRef.getType())) {
/*  87 */           PropertyValue[] clusterProperties = getClusterProperties(propertyRequest.properties, objectRef);
/*  88 */           resultItem = QueryUtil.newResultItem(objectRef, clusterProperties);
/*     */         } 
/*     */         
/*  91 */         resultItems.add(resultItem);
/*     */       }  b++; }
/*     */     
/*  94 */     ResultSet resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/*  95 */     return resultSet;
/*     */   }
/*     */   
/*     */   private PropertyValue[] getFolderProperties(PropertySpec[] properties, Object objectRef) {
/*  99 */     List<PropertyValue> propValues = new ArrayList<>();
/*     */     
/* 101 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isVsanNestedFdsSupported" })) {
/* 102 */       PropertyValue propValue = QueryUtil.newProperty("isVsanNestedFdsSupported", 
/* 103 */           Boolean.valueOf(VsanCapabilityUtils.isVsanNestedFdsSupportedOnVc((ManagedObjectReference)objectRef)));
/* 104 */       propValue.resourceObject = objectRef;
/* 105 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 108 */     return propValues.<PropertyValue>toArray(new PropertyValue[0]);
/*     */   }
/*     */   
/*     */   private PropertyValue[] getClusterProperties(PropertySpec[] properties, Object objectRef) {
/* 112 */     List<PropertyValue> propValues = new ArrayList<>();
/* 113 */     ManagedObjectReference clusterRef = (ManagedObjectReference)objectRef;
/*     */     
/* 115 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isFileServiceSupported" })) {
/* 116 */       PropertyValue propValue = QueryUtil.newProperty(
/* 117 */           "isFileServiceSupported", Boolean.valueOf(VsanCapabilityUtils.isFileServiceSupported(clusterRef)));
/* 118 */       propValue.resourceObject = objectRef;
/* 119 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 122 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "hasVsanEligibleHosts" })) {
/* 123 */       Number hostCount = Integer.valueOf(0);
/*     */       try {
/* 125 */         hostCount = (Number)QueryUtil.getProperty(clusterRef, "host._length", null);
/* 126 */       } catch (Exception e) {
/* 127 */         _logger.warn("Failed to check hosts in cluster, assuming empty: " + clusterRef, e);
/*     */       } 
/*     */       
/* 130 */       PropertyValue propValue = QueryUtil.newProperty("hasVsanEligibleHosts", Boolean.valueOf((hostCount.intValue() > 0)));
/* 131 */       propValue.resourceObject = objectRef;
/* 132 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 135 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isHistoricalCapacitySupported" })) {
/* 136 */       Boolean isHistoricalCapacitySupproted = Boolean.valueOf(false);
/*     */       try {
/* 138 */         isHistoricalCapacitySupproted = Boolean.valueOf(getIsHistoricalCapacitySupported(clusterRef));
/* 139 */       } catch (Exception e) {
/* 140 */         _logger.error("Unable to get historical capacity capability status. ", e);
/*     */       } 
/*     */       
/* 143 */       PropertyValue propValue = QueryUtil.newProperty("isHistoricalCapacitySupported", 
/* 144 */           isHistoricalCapacitySupproted);
/* 145 */       propValue.resourceObject = objectRef;
/* 146 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 149 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isIscsiTargetsSupportedOnVc" })) {
/* 150 */       propValues.add(getIscsiTargetsPropertyValue(objectRef));
/*     */     }
/*     */     
/* 153 */     return propValues.<PropertyValue>toArray(new PropertyValue[0]);
/*     */   }
/*     */   
/*     */   private boolean getIsHistoricalCapacitySupported(ManagedObjectReference clusterRef) throws Exception {
/* 157 */     ManagedObjectReference[] hosts = 
/* 158 */       (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/*     */     
/* 160 */     if (ArrayUtils.isEmpty((Object[])hosts)) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     VsanCapabilitySystem capabilitySystem = VsanProviderUtils.getVsanCapabilitySystem(clusterRef);
/* 165 */     VsanCapability[] capabilities = capabilitySystem.getCapabilities(hosts);
/* 166 */     int supportedHosts = 0; byte b; int i; VsanCapability[] arrayOfVsanCapability1;
/* 167 */     for (i = (arrayOfVsanCapability1 = capabilities).length, b = 0; b < i; ) { VsanCapability capability = arrayOfVsanCapability1[b]; byte b1; int j; String[] arrayOfString;
/* 168 */       for (j = (arrayOfString = capability.capabilities).length, b1 = 0; b1 < j; ) { String capabilityName = arrayOfString[b1];
/* 169 */         if ("historicalcapacity".equalsIgnoreCase(capabilityName))
/* 170 */           supportedHosts++; 
/*     */         b1++; }
/*     */       
/*     */       b++; }
/*     */     
/* 175 */     return (supportedHosts == hosts.length);
/*     */   }
/*     */   
/*     */   private PropertyValue getIscsiTargetsPropertyValue(Object objectRef) {
/* 179 */     Boolean isIscsiTargetsSupportedOnVc = Boolean.valueOf(false);
/*     */     try {
/* 181 */       isIscsiTargetsSupportedOnVc = 
/* 182 */         Boolean.valueOf(VsanCapabilityUtils.isIscsiTargetsSupportedOnVc((ManagedObjectReference)objectRef));
/* 183 */     } catch (Exception e) {
/* 184 */       _logger.error("Unable to get iSCSI targets capability status. ", e);
/*     */     } 
/* 186 */     PropertyValue propValue = QueryUtil.newProperty("isIscsiTargetsSupportedOnVc", 
/* 187 */         isIscsiTargetsSupportedOnVc);
/* 188 */     propValue.resourceObject = objectRef;
/* 189 */     return propValue;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/capability/VsanCapabilityPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */