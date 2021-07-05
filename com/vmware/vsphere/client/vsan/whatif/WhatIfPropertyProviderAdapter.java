/*     */ package com.vmware.vsphere.client.vsan.whatif;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DecommissionMode;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.ParameterSpec;
/*     */ import com.vmware.vise.data.PropertySpec;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ public class WhatIfPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*     */   private static final String VSAN_HOST_WHAT_IF_RESULT = "hostWhatIfResult";
/*  30 */   private static final Log _logger = LogFactory.getLog(WhatIfPropertyProviderAdapter.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private WhatIfPropertyProvider whatIfProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WhatIfPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  41 */     Validate.notNull(registry);
/*     */     
/*  43 */     TypeInfo hostInfo = new TypeInfo();
/*  44 */     hostInfo.type = HostSystem.class.getSimpleName();
/*  45 */     hostInfo.properties = new String[] { "hostWhatIfResult" };
/*     */     
/*  47 */     TypeInfo[] providedProperties = { hostInfo };
/*  48 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  53 */     if (!isValidRequest(propertyRequest)) {
/*  54 */       ResultSet result = new ResultSet();
/*  55 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/*  56 */       return result;
/*     */     } 
/*     */     
/*  59 */     ResultSet resultSet = null;
/*  60 */     List<ResultItem> resultItems = new ArrayList<>();
/*     */ 
/*     */     
/*  63 */     WhatIfSpec spec = new WhatIfSpec(); byte b; int i; PropertySpec[] arrayOfPropertySpec;
/*  64 */     for (i = (arrayOfPropertySpec = propertyRequest.properties).length, b = 0; b < i; ) { PropertySpec propertySpec = arrayOfPropertySpec[b];
/*  65 */       if (ArrayUtils.isNotEmpty((Object[])propertySpec.parameters)) {
/*  66 */         byte b1; int j; ParameterSpec[] arrayOfParameterSpec; for (j = (arrayOfParameterSpec = propertySpec.parameters).length, b1 = 0; b1 < j; ) { ParameterSpec parameterSpec = arrayOfParameterSpec[b1];
/*  67 */           if ("hostWhatIfResult".equals(parameterSpec.propertyName))
/*  68 */             spec.clusterRef = (ManagedObjectReference)parameterSpec.parameter;  b1++; }
/*     */       
/*     */       } 
/*     */       b++; }
/*     */     
/*     */     Object[] arrayOfObject;
/*  74 */     for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object host = arrayOfObject[b];
/*     */       try {
/*  76 */         ManagedObjectReference hostRef = (ManagedObjectReference)host;
/*  77 */         WhatIfResult whatIfResult = this.whatIfProvider.getWhatIfResult(hostRef, spec);
/*     */         
/*  79 */         Map<Object, Object> result = new HashMap<>();
/*     */         
/*  81 */         if (whatIfResult.isWhatIfSupported.booleanValue()) {
/*  82 */           result = createWhatIfResultObject(whatIfResult);
/*     */         } else {
/*  84 */           result.put("isWhatIfSupported", Boolean.valueOf(false));
/*     */         } 
/*  86 */         PropertyValue resultPropValue = 
/*  87 */           QueryUtil.newProperty("hostWhatIfResult", result);
/*  88 */         ResultItem resultItem = QueryUtil.newResultItem(hostRef, new PropertyValue[] { resultPropValue });
/*  89 */         resultItems.add(resultItem);
/*  90 */       } catch (Exception ex) {
/*  91 */         _logger.error("Failed to retrieve hostWhatIfResult property. ", ex);
/*  92 */         resultSet = new ResultSet();
/*  93 */         resultSet.error = ex;
/*  94 */         return resultSet;
/*     */       } 
/*     */       b++; }
/*     */     
/*  98 */     resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/*     */     
/* 100 */     return resultSet;
/*     */   }
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
/*     */   private Map<Object, Object> createWhatIfResultObject(WhatIfResult whatIfResult) {
/* 114 */     Map<Object, Object> result = new HashMap<>();
/*     */     
/* 116 */     List<Object> ensureAccessibilityMap = new ArrayList();
/* 117 */     ensureAccessibilityMap.add(whatIfResult.ensureAccessibility.summary);
/* 118 */     ensureAccessibilityMap.add(Boolean.valueOf(whatIfResult.ensureAccessibility.successWithoutDataLoss));
/* 119 */     ensureAccessibilityMap.add(Boolean.valueOf(whatIfResult.ensureAccessibility.successWithInaccessibleOrNonCompliantObjects));
/* 120 */     ensureAccessibilityMap.add(Long.valueOf(whatIfResult.ensureAccessibility.repairTime));
/*     */     
/* 122 */     List<Object> fullDataMigrationMap = new ArrayList();
/* 123 */     fullDataMigrationMap.add(whatIfResult.fullDataMigration.summary);
/* 124 */     fullDataMigrationMap.add(Boolean.valueOf(whatIfResult.fullDataMigration.successWithoutDataLoss));
/* 125 */     fullDataMigrationMap.add(Boolean.valueOf(whatIfResult.fullDataMigration.successWithInaccessibleOrNonCompliantObjects));
/* 126 */     fullDataMigrationMap.add(Long.valueOf(whatIfResult.fullDataMigration.repairTime));
/*     */     
/* 128 */     List<Object> noDataMigrationMap = new ArrayList();
/* 129 */     noDataMigrationMap.add(whatIfResult.noDataMigration.summary);
/* 130 */     noDataMigrationMap.add(Boolean.valueOf(whatIfResult.noDataMigration.successWithoutDataLoss));
/* 131 */     noDataMigrationMap.add(Boolean.valueOf(whatIfResult.noDataMigration.successWithInaccessibleOrNonCompliantObjects));
/* 132 */     noDataMigrationMap.add(Long.valueOf(whatIfResult.noDataMigration.repairTime));
/*     */     
/* 134 */     result.put("isWhatIfSupported", Boolean.valueOf(true));
/* 135 */     result.put(DecommissionMode.ObjectAction.ensureObjectAccessibility, ensureAccessibilityMap);
/* 136 */     result.put(DecommissionMode.ObjectAction.evacuateAllData, fullDataMigrationMap);
/* 137 */     result.put(DecommissionMode.ObjectAction.noAction, noDataMigrationMap);
/*     */     
/* 139 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidRequest(PropertyRequestSpec propertyRequest) {
/* 146 */     if (propertyRequest == null) {
/* 147 */       return false;
/*     */     }
/* 149 */     if (ArrayUtils.isEmpty(propertyRequest.objects) || 
/* 150 */       ArrayUtils.isEmpty((Object[])propertyRequest.properties)) {
/* 151 */       _logger.error("Property provider adapter got a null or empty list of properties or objects");
/*     */       
/* 153 */       return false;
/*     */     } 
/* 155 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/whatif/WhatIfPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */