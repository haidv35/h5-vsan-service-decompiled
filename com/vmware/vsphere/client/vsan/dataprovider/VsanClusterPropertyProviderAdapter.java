/*     */ package com.vmware.vsphere.client.vsan.dataprovider;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.PropertySpec;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class VsanClusterPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*  33 */   private static final Log _logger = LogFactory.getLog(VsanClusterPropertyProviderAdapter.class);
/*     */   
/*     */   private static final String PROPERTY_IS_FILE_SERVICE_SUPPORTED = "isFileServiceSupported";
/*     */   private static final String PROPERTY_IS_VSAN_ENABLED_AND_FILE_SERVICE_SUPPORTED = "isVsanEnabledAndFileServiceSupported";
/*     */   private static final String PROPERTY_HAS_ELIGIBLE_HOSTS = "hasVsanEligibleHosts";
/*     */   private static final String PROPERTY_IS_ISCSI_TARGETS_SUPPORTED_ON_VC = "isIscsiTargetsSupportedOnVc";
/*     */   private static final String PROPERTY_IS_VSAN_ENABLED_AND_ISCSI_TARGETS_SUPPORTED = "isVsanEnabledAndIscsiTargetsSupported";
/*     */   
/*     */   public VsanClusterPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  42 */     Validate.notNull(registry);
/*     */     
/*  44 */     String[] clusterProperties = {
/*  45 */         "isFileServiceSupported", 
/*  46 */         "hasVsanEligibleHosts", 
/*  47 */         "isIscsiTargetsSupportedOnVc", 
/*  48 */         "isVsanEnabledAndFileServiceSupported", 
/*  49 */         "isVsanEnabledAndIscsiTargetsSupported"
/*     */       };
/*  51 */     TypeInfo clusterInfo = new TypeInfo();
/*  52 */     clusterInfo.type = ClusterComputeResource.class.getSimpleName();
/*  53 */     clusterInfo.properties = clusterProperties;
/*     */     
/*  55 */     TypeInfo[] providedProperties = { clusterInfo };
/*  56 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  61 */     if (!QueryUtil.isValidRequest(propertyRequest)) {
/*  62 */       ResultSet result = new ResultSet();
/*  63 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/*  64 */       return result;
/*     */     } 
/*     */     
/*  67 */     List<ResultItem> resultItems = new ArrayList<>(); byte b; int i; Object[] arrayOfObject;
/*  68 */     for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object objectRef = arrayOfObject[b];
/*  69 */       ManagedObjectReference moRef = (ManagedObjectReference)objectRef;
/*  70 */       if (objectRef != null) {
/*     */ 
/*     */ 
/*     */         
/*  74 */         ResultItem resultItem = null;
/*  75 */         if (ClusterComputeResource.class.getSimpleName().equals(moRef.getType())) {
/*  76 */           PropertyValue[] clusterProperties = getClusterProperties(propertyRequest.properties, objectRef);
/*  77 */           resultItem = QueryUtil.newResultItem(objectRef, clusterProperties);
/*     */         } 
/*     */         
/*  80 */         resultItems.add(resultItem);
/*     */       }  b++; }
/*     */     
/*  83 */     ResultSet resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/*  84 */     return resultSet;
/*     */   }
/*     */   
/*     */   private PropertyValue[] getClusterProperties(PropertySpec[] properties, Object objectRef) {
/*  88 */     List<PropertyValue> propValues = new ArrayList<>();
/*  89 */     ManagedObjectReference clusterRef = (ManagedObjectReference)objectRef;
/*     */     
/*  91 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isFileServiceSupported" })) {
/*  92 */       PropertyValue propValue = QueryUtil.newProperty(
/*  93 */           "isFileServiceSupported", Boolean.valueOf(VsanCapabilityUtils.isFileServiceSupported(clusterRef)));
/*  94 */       propValue.resourceObject = objectRef;
/*  95 */       propValues.add(propValue);
/*     */     } 
/*     */     
/*  98 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "hasVsanEligibleHosts" })) {
/*  99 */       Number hostCount = Integer.valueOf(0);
/*     */       try {
/* 101 */         hostCount = (Number)QueryUtil.getProperty(clusterRef, "host._length", null);
/* 102 */       } catch (Exception e) {
/* 103 */         _logger.warn("Failed to check hosts in cluster, assuming empty: " + clusterRef, e);
/*     */       } 
/*     */       
/* 106 */       PropertyValue propValue = QueryUtil.newProperty("hasVsanEligibleHosts", Boolean.valueOf((hostCount.intValue() > 0)));
/* 107 */       propValue.resourceObject = objectRef;
/* 108 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 111 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isIscsiTargetsSupportedOnVc" })) {
/* 112 */       PropertyValue propValue = QueryUtil.newProperty("isIscsiTargetsSupportedOnVc", 
/* 113 */           Boolean.valueOf(VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)));
/* 114 */       propValue.resourceObject = objectRef;
/*     */       
/* 116 */       propValues.add(propValue);
/*     */     } 
/*     */     
/* 119 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isVsanEnabledAndFileServiceSupported", 
/* 120 */           "isVsanEnabledAndIscsiTargetsSupported" })) {
/* 121 */       boolean isVsanEnabled = isVsanEnabled(clusterRef);
/*     */       
/* 123 */       if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isVsanEnabledAndFileServiceSupported" })) {
/* 124 */         boolean result = (isVsanEnabled && VsanCapabilityUtils.isFileServiceSupported(clusterRef));
/*     */         
/* 126 */         PropertyValue propertyValue = QueryUtil.newProperty(
/* 127 */             "isVsanEnabledAndFileServiceSupported", Boolean.valueOf(result));
/* 128 */         propertyValue.resourceObject = clusterRef;
/*     */         
/* 130 */         propValues.add(propertyValue);
/*     */       } 
/*     */       
/* 133 */       if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isVsanEnabledAndIscsiTargetsSupported" })) {
/* 134 */         boolean result = (isVsanEnabled && VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef));
/*     */         
/* 136 */         PropertyValue propertyValue = QueryUtil.newProperty(
/* 137 */             "isVsanEnabledAndIscsiTargetsSupported", Boolean.valueOf(result));
/* 138 */         propertyValue.resourceObject = clusterRef;
/*     */         
/* 140 */         propValues.add(propertyValue);
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return propValues.<PropertyValue>toArray(new PropertyValue[0]);
/*     */   }
/*     */   
/*     */   private boolean isVsanEnabled(ManagedObjectReference clusterRef) {
/* 148 */     boolean vsanEnabled = false;
/*     */     try {
/* 150 */       vsanEnabled = ((Boolean)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled", null)).booleanValue();
/* 151 */     } catch (Exception ex) {
/* 152 */       _logger.error("Unable to get vSAN enabled property. ", ex);
/*     */     } 
/*     */     
/* 155 */     return vsanEnabled;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/dataprovider/VsanClusterPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */