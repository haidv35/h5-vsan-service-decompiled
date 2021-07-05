/*     */ package com.vmware.vsphere.client.vsan.guardrail;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.RuntimeStats;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanSyncingObjectQueryResult;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsan.client.services.resyncing.VsanResyncingComponentsProvider;
/*     */ import com.vmware.vsan.client.services.resyncing.data.RepairTimerData;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.FormatUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuardRailPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*     */   private static final String VSAN_GUARD_RAIL_RESULT = "clusterGuardRailResult";
/*     */   private static final String VSAN_GUARD_RAIL_MESSAGES = "clusterGuardRailMessages";
/*     */   private static final int TWO_MINUTES_IN_SECONDS = 120;
/*     */   public static final String HOST_NAME_SEPARATOR = ", ";
/*  51 */   private static final String[] HOST_PROPERTIES = new String[] {
/*  52 */       "name", 
/*  53 */       "runtime.connectionState", 
/*  54 */       "runtime.inMaintenanceMode"
/*     */     };
/*     */   
/*  57 */   private static final Log _logger = LogFactory.getLog(GuardRailPropertyProviderAdapter.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public VsanResyncingComponentsProvider resyncingComponentsProvider;
/*     */ 
/*     */ 
/*     */   
/*     */   public GuardRailPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  67 */     Validate.notNull(registry);
/*     */     
/*  69 */     TypeInfo clusterInfo = new TypeInfo();
/*  70 */     clusterInfo.type = ClusterComputeResource.class.getSimpleName();
/*  71 */     clusterInfo.properties = new String[] { "clusterGuardRailResult", "clusterGuardRailMessages" };
/*  72 */     TypeInfo[] providedProperties = { clusterInfo };
/*  73 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  78 */     if (ArrayUtils.isEmpty(propertyRequest.objects)) {
/*  79 */       _logger.warn("Empty propertyRequest.objects is passed");
/*  80 */       return null;
/*     */     } 
/*     */     
/*  83 */     List<ResultItem> resultItems = new ArrayList<>();
/*     */     try {
/*  85 */       String[] propertyNames = QueryUtil.getPropertyNames(propertyRequest.properties); byte b; int i; Object[] arrayOfObject;
/*  86 */       for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object obj = arrayOfObject[b];
/*  87 */         ManagedObjectReference clusterRef = (ManagedObjectReference)obj;
/*  88 */         List<PropertyValue> propertyValues = new ArrayList<>();
/*     */         
/*  90 */         GuardRailResult guardRailResult = getGuardRailResult(clusterRef); byte b1; int j;
/*     */         String[] arrayOfString;
/*  92 */         for (j = (arrayOfString = propertyNames).length, b1 = 0; b1 < j; ) { String property = arrayOfString[b1];
/*  93 */           PropertyValue propertyValue = null;
/*  94 */           if ("clusterGuardRailMessages".equals(property)) {
/*  95 */             Map<GuardRailMessageStatus, List<String>> guardRailMessages = 
/*  96 */               convertToGuardRailMessages(guardRailResult);
/*  97 */             propertyValue = QueryUtil.newProperty("clusterGuardRailMessages", guardRailMessages);
/*  98 */           } else if ("clusterGuardRailResult".equals(property)) {
/*  99 */             propertyValue = QueryUtil.newProperty("clusterGuardRailResult", guardRailResult);
/*     */           } else {
/* 101 */             _logger.warn("Unknown property: " + property);
/*     */           } 
/*     */           
/* 104 */           if (propertyValue != null) {
/* 105 */             propertyValues.add(propertyValue);
/*     */           }
/*     */           b1++; }
/*     */         
/* 109 */         ResultItem resultItem = new ResultItem();
/* 110 */         resultItem.properties = propertyValues.<PropertyValue>toArray(new PropertyValue[propertyValues.size()]);
/* 111 */         resultItem.resourceObject = clusterRef;
/* 112 */         resultItems.add(resultItem); b++; }
/*     */     
/* 114 */     } catch (Exception ex) {
/* 115 */       _logger.error("Failed to retrieve ClusterGuardRailResult property. ", ex);
/* 116 */       ResultSet resultSet = new ResultSet();
/* 117 */       resultSet.error = new Exception(Utils.getLocalizedString("vsan.guardRail.providerGeneralError"));
/* 118 */       return resultSet;
/*     */     } 
/*     */     
/* 121 */     ResultSet result = new ResultSet();
/* 122 */     result.items = resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]);
/* 123 */     result.totalMatchedObjectCount = Integer.valueOf(resultItems.size());
/* 124 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<GuardRailMessageStatus, List<String>> convertToGuardRailMessages(GuardRailResult guardRailResult) {
/* 132 */     Map<GuardRailMessageStatus, List<String>> messages = new HashMap<>();
/* 133 */     List<String> warningMessages = new ArrayList<>();
/* 134 */     List<String> infoMessages = new ArrayList<>();
/*     */     
/* 136 */     if (ArrayUtils.isNotEmpty((Object[])guardRailResult.hostsInMaintenanceMode)) {
/* 137 */       String hostNames = StringUtils.join((Object[])guardRailResult.hostsInMaintenanceMode, ", ");
/* 138 */       warningMessages.add(Utils.getLocalizedString("vsan.guardRail.hostInMaintenanceMode", new String[] { hostNames }));
/*     */     } 
/*     */ 
/*     */     
/* 142 */     if (guardRailResult.isClusterInResync && guardRailResult.recoveryETA.longValue() > 120L) {
/* 143 */       String objectsToSync = (guardRailResult.objectsToSyncCount.longValue() > 0L) ? 
/* 144 */         String.valueOf(guardRailResult.objectsToSyncCount) : 
/* 145 */         Utils.getLocalizedString("vsan.guardRail.none");
/*     */       
/* 147 */       String message = Utils.getLocalizedString("vsan.guardRail.clusterInResync", new String[] {
/* 148 */             objectsToSync });
/* 149 */       warningMessages.add(message);
/* 150 */     } else if (guardRailResult.repairTimerData.objectsCount > 0L) {
/*     */       
/* 152 */       long minutesToRepair = FormatUtil.getMinutesFromNow(guardRailResult.repairTimerData.minTimer);
/* 153 */       String message = Utils.getLocalizedString("vsan.guardRail.scheduledResync", new String[] {
/* 154 */             String.valueOf(minutesToRepair) });
/* 155 */       warningMessages.add(message);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (!guardRailResult.resyncCollected) {
/* 161 */       infoMessages.add(Utils.getLocalizedString("vsan.guardRail.legacyClusterInResync"));
/*     */     }
/*     */     
/* 164 */     messages.put(GuardRailMessageStatus.WARNING, warningMessages);
/* 165 */     messages.put(GuardRailMessageStatus.INFO, infoMessages);
/*     */     
/* 167 */     return messages;
/*     */   }
/*     */   
/*     */   private GuardRailResult getGuardRailResult(ManagedObjectReference clusterRef) throws Exception {
/* 171 */     Measure measure = new Measure("Retrieving guard rail data");
/*     */     
/* 173 */     DataServiceResponse response = QueryUtil.getPropertiesForRelatedObjects(
/* 174 */         clusterRef, 
/* 175 */         "allVsanHosts", 
/* 176 */         ClusterComputeResource.class.getSimpleName(), 
/* 177 */         HOST_PROPERTIES);
/*     */     
/* 179 */     List<String> hostsInMM = new ArrayList<>();
/* 180 */     null;
/*     */ 
/*     */     
/* 183 */     Future<VsanSyncingObjectQueryResult> vsanSyncingObjectQueryResultFuture = null;
/* 184 */     for (Object obj : response.getResourceObjects()) {
/* 185 */       ManagedObjectReference hostRef = (ManagedObjectReference)obj;
/* 186 */       boolean isInMaintenanceMode = ((Boolean)response.getProperty(hostRef, "runtime.inMaintenanceMode")).booleanValue();
/* 187 */       HostSystem.ConnectionState connectionState = (HostSystem.ConnectionState)response.getProperty(hostRef, "runtime.connectionState");
/* 188 */       boolean isConnected = (HostSystem.ConnectionState.connected == connectionState);
/*     */       
/* 190 */       if (isInMaintenanceMode) {
/* 191 */         hostsInMM.add((String)response.getProperty(hostRef, "name")); continue;
/* 192 */       }  if (vsanSyncingObjectQueryResultFuture == null && 
/* 193 */         VsanCapabilityUtils.isResyncEnhancedApiSupported(hostRef) && 
/* 194 */         isConnected)
/*     */       {
/*     */         
/* 197 */         vsanSyncingObjectQueryResultFuture = querySyncingVsanObjects(measure, hostRef);
/*     */       }
/*     */     } 
/*     */     
/* 201 */     Future[] repairTimerDataFutures = this.resyncingComponentsProvider.getRepairTimerDataFutures(clusterRef, measure);
/*     */     
/* 203 */     return getGuardRailResultProperties(hostsInMM, vsanSyncingObjectQueryResultFuture, (Future<RuntimeStats>[])repairTimerDataFutures);
/*     */   }
/*     */   
/*     */   private Future<VsanSyncingObjectQueryResult> querySyncingVsanObjects(Measure measure, ManagedObjectReference hostRef) {
/* 207 */     Future<VsanSyncingObjectQueryResult> future = null;
/*     */     
/* 209 */     VsanSystemEx vsanSystemEx = VsanProviderUtils.getVsanSystemEx(hostRef);
/*     */     
/* 211 */     if (vsanSystemEx != null) {
/* 212 */       future = measure.newFuture("vsanSystemEx.querySyncingVsanObjects");
/* 213 */       vsanSystemEx.querySyncingVsanObjects(null, Integer.valueOf(0), Integer.valueOf(1), Boolean.valueOf(true), future);
/*     */     } 
/*     */     
/* 216 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private GuardRailResult getGuardRailResultProperties(List<String> hostsInMM, Future<VsanSyncingObjectQueryResult> vsanSyncingObjectFuture, Future[] repairTimerDataFutures) throws ExecutionException, InterruptedException {
/* 222 */     GuardRailResult guardRailResult = new GuardRailResult();
/*     */     
/* 224 */     if (CollectionUtils.isNotEmpty(hostsInMM)) {
/* 225 */       guardRailResult.hostsInMaintenanceMode = hostsInMM.<String>toArray(new String[hostsInMM.size()]);
/*     */     }
/*     */     
/* 228 */     guardRailResult.resyncCollected = (vsanSyncingObjectFuture != null);
/* 229 */     if (guardRailResult.resyncCollected) {
/* 230 */       VsanSyncingObjectQueryResult vsanSyncingObject = (VsanSyncingObjectQueryResult)vsanSyncingObjectFuture.get();
/* 231 */       if (ArrayUtils.isNotEmpty((Object[])vsanSyncingObject.objects)) {
/* 232 */         guardRailResult.isClusterInResync = true;
/* 233 */         guardRailResult.recoveryETA = vsanSyncingObject.totalRecoveryETA;
/* 234 */         guardRailResult.objectsToSyncCount = vsanSyncingObject.totalObjectsToSync;
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     RepairTimerData repairTimerData = this.resyncingComponentsProvider.getRepairTimerData(repairTimerDataFutures);
/* 239 */     guardRailResult.repairTimerData = repairTimerData;
/*     */     
/* 241 */     return guardRailResult;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/guardrail/GuardRailPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */