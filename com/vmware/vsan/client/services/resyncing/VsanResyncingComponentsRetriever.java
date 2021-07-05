/*     */ package com.vmware.vsan.client.services.resyncing;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ConfigInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.fault.InvalidArgument;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanComponentSyncState;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanObjectSyncState;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanSyncingObjectQueryResult;
/*     */ import com.vmware.vsan.client.services.resyncing.data.ResyncComponent;
/*     */ import com.vmware.vsan.client.services.resyncing.data.ResyncMonitorData;
/*     */ import com.vmware.vsan.client.services.resyncing.data.VsanSyncingObjectsQuerySpec;
/*     */ import com.vmware.vsphere.client.vsan.base.data.ComponentIntent;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComponent;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.util.Version;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VsanResyncingComponentsRetriever
/*     */ {
/*  43 */   private static final Version HOST_VERSION_2015 = new Version("6.0.0");
/*     */   
/*  45 */   private static final String[] HOST_PROPERTIES = new String[] {
/*  46 */       "name", 
/*  47 */       "runtime.connectionState", 
/*  48 */       "config.vsanHostConfig", 
/*  49 */       "config.product.version"
/*     */     };
/*  51 */   private static final Log _logger = LogFactory.getLog(VsanResyncingComponentsRetriever.class);
/*     */   
/*  53 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanResyncingComponentsRetriever.class);
/*     */   
/*     */   @Autowired
/*     */   VcClient vcClient;
/*     */ 
/*     */   
/*     */   public ResyncMonitorData getVsanResyncObjects(ManagedObjectReference clusterRef, VsanSyncingObjectsQuerySpec spec) throws Exception {
/*  60 */     HostsData hostsData = getHostsData(clusterRef);
/*     */ 
/*     */ 
/*     */     
/*  64 */     List<ManagedObjectReference> hostRefsEnhancedApi = new ArrayList<>();
/*  65 */     List<ManagedObjectReference> hostRefsLegacy = new ArrayList<>();
/*  66 */     for (ManagedObjectReference hostRef : hostsData.hostConnectionStates.keySet()) {
/*  67 */       if (!HostSystem.ConnectionState.connected.equals(hostsData.hostConnectionStates.get(hostRef))) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  72 */       if (VsanCapabilityUtils.isResyncEnhancedApiSupported(hostRef)) {
/*     */         
/*  74 */         hostRefsEnhancedApi.add(hostRef);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*  79 */       Version esxVersion = hostsData.hostVersions.get(hostRef);
/*  80 */       if (esxVersion != null && esxVersion.compareTo(HOST_VERSION_2015) >= 0)
/*     */       {
/*  82 */         hostRefsLegacy.add(hostRef);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if (!CollectionUtils.isEmpty(hostRefsEnhancedApi)) {
/*  88 */       ResyncMonitorData result = getResyncData(hostRefsEnhancedApi, spec, hostsData.hostNodeUuidToHostNames);
/*  89 */       result.isResyncFilterApiSupported = true;
/*  90 */       return result;
/*  91 */     }  if (!CollectionUtils.isEmpty(hostRefsLegacy)) {
/*  92 */       return getLegacyVsanResyncObjects(hostRefsLegacy, hostsData.hostNodeUuidToHostNames);
/*     */     }
/*     */     
/*  95 */     return new ResyncMonitorData();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ResyncMonitorData getResyncData(List<ManagedObjectReference> hostRefs, VsanSyncingObjectsQuerySpec spec, Map<String, String> hostNodeUuidToHostNames) throws Exception {
/* 101 */     for (ManagedObjectReference hostRef : hostRefs) {
/* 102 */       VsanSystemEx vsanSystemEx = VsanProviderUtils.getVsanSystemEx(hostRef);
/* 103 */       if (vsanSystemEx == null)
/*     */         continue; 
/*     */       
/* 106 */       try { Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {  }
/*     */         finally
/* 115 */         { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }  }  } catch (Exception ex)
/*     */       
/* 117 */       { _logger.error("Failed to retrieve syncing objects", ex); }
/*     */     
/*     */     } 
/*     */     
/* 121 */     return new ResyncMonitorData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResyncMonitorData getLegacyVsanResyncObjects(List<ManagedObjectReference> hostRefs, Map<String, String> hostNodeUuidToHostNames) throws Exception {
/* 130 */     Exception exception1 = null, exception2 = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 157 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   private VsanSyncingObjectQueryResult convertLegacyDataToVsanSyncingObjects(Set<VsanObject> resyncObjects) {
/* 162 */     VsanSyncingObjectQueryResult syncingObjectQueryResult = new VsanSyncingObjectQueryResult(
/* 163 */         Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(-1L), new VsanObjectSyncState[0]);
/* 164 */     if (CollectionUtils.isEmpty(resyncObjects)) {
/* 165 */       return syncingObjectQueryResult;
/*     */     }
/*     */     
/* 168 */     long componentsCount = 0L;
/* 169 */     long componentsBytesToSync = 0L;
/* 170 */     long recoveryEta = -1L;
/* 171 */     List<VsanObjectSyncState> syncObjects = new ArrayList<>();
/* 172 */     for (VsanObject resyncObject : resyncObjects) {
/* 173 */       List<VsanComponentSyncState> components = new ArrayList<>();
/* 174 */       for (VsanComponent vsanComponent : resyncObject.rootConfig.children) {
/* 175 */         if (vsanComponent.byteToSync <= 0L) {
/*     */           continue;
/*     */         }
/* 178 */         VsanComponentSyncState vsanResyncComponent = new VsanComponentSyncState(
/* 179 */             vsanComponent.componentUuid, vsanComponent.capacityDiskUuid, vsanComponent.hostUuid, 
/* 180 */             vsanComponent.byteToSync, Long.valueOf(vsanComponent.recoveryEta), 
/* 181 */             new String[] { intentToResyncReason(vsanComponent.intent) });
/* 182 */         components.add(vsanResyncComponent);
/* 183 */         componentsCount++;
/* 184 */         componentsBytesToSync += vsanComponent.byteToSync;
/* 185 */         recoveryEta = Math.max(vsanComponent.recoveryEta, recoveryEta);
/*     */       } 
/*     */       
/* 188 */       VsanObjectSyncState vsanSyncObject = new VsanObjectSyncState(
/* 189 */           resyncObject.vsanObjectUuid, components.<VsanComponentSyncState>toArray(new VsanComponentSyncState[components.size()]));
/* 190 */       syncObjects.add(vsanSyncObject);
/*     */     } 
/* 192 */     syncingObjectQueryResult.objects = syncObjects.<VsanObjectSyncState>toArray(new VsanObjectSyncState[syncObjects.size()]);
/* 193 */     syncingObjectQueryResult.setTotalObjectsToSync(Long.valueOf(componentsCount));
/* 194 */     syncingObjectQueryResult.setTotalBytesToSync(Long.valueOf(componentsBytesToSync));
/* 195 */     syncingObjectQueryResult.setTotalRecoveryETA(Long.valueOf(recoveryEta));
/* 196 */     return syncingObjectQueryResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String intentToResyncReason(ComponentIntent intent) {
/* 204 */     switch (intent) {
/*     */       case REPAIR:
/*     */       case FIXCOMPLIANCE:
/* 207 */         return ResyncComponent.ResyncReasonCode.repair.toString();
/*     */       case null:
/* 209 */         return ResyncComponent.ResyncReasonCode.evacuate.toString();
/*     */       case REBALANCE:
/* 211 */         return ResyncComponent.ResyncReasonCode.rebalance.toString();
/*     */       case POLICYCHANGE:
/* 213 */         return ResyncComponent.ResyncReasonCode.reconfigure.toString();
/*     */       case MOVE:
/* 215 */         return ResyncComponent.ResyncReasonCode.dying_evacuate.toString();
/*     */       case STALE:
/* 217 */         return ResyncComponent.ResyncReasonCode.stale.toString();
/*     */       case MERGE_CONTACT:
/* 219 */         return ResyncComponent.ResyncReasonCode.merge_concat.toString();
/*     */     } 
/* 221 */     throw new InvalidArgument("Invalid intent code received from server side:" + intent.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HostsData getHostsData(ManagedObjectReference clusterRef) throws Exception {
/* 232 */     HostsData hostsData = new HostsData(null);
/*     */     
/* 234 */     Map<ManagedObjectReference, ConfigInfo> hostToVsanHostConfigInfo = new HashMap<>();
/*     */ 
/*     */     
/* 237 */     Map<ManagedObjectReference, String> hostNames = new HashMap<>();
/*     */     
/*     */     try {
/* 240 */       DataServiceResponse response = QueryUtil.getPropertiesForRelatedObjects(
/* 241 */           clusterRef, 
/* 242 */           "host", 
/* 243 */           HostSystem.class.getSimpleName(), 
/* 244 */           HOST_PROPERTIES);
/* 245 */       if (response == null) {
/* 246 */         return hostsData;
/*     */       }
/*     */       
/* 249 */       for (Object resourceObject : response.getResourceObjects()) {
/* 250 */         ManagedObjectReference hostRef = (ManagedObjectReference)resourceObject;
/*     */         
/* 252 */         hostNames.put(hostRef, (String)response.getProperty(hostRef, "name"));
/*     */         
/* 254 */         hostsData.hostConnectionStates.put(hostRef, 
/* 255 */             (HostSystem.ConnectionState)response.getProperty(hostRef, "runtime.connectionState"));
/*     */         
/* 257 */         hostToVsanHostConfigInfo.put(hostRef, 
/* 258 */             (ConfigInfo)response.getProperty(hostRef, "config.vsanHostConfig"));
/*     */         
/* 260 */         hostsData.hostVersions.put(hostRef, 
/* 261 */             new Version((String)response.getProperty(hostRef, "config.product.version")));
/*     */       } 
/*     */ 
/*     */       
/* 265 */       for (ManagedObjectReference hostRef : hostNames.keySet()) {
/* 266 */         ConfigInfo vsanConfig = hostToVsanHostConfigInfo.get(hostRef);
/* 267 */         if (vsanConfig == null || !vsanConfig.enabled.booleanValue() || 
/* 268 */           vsanConfig.clusterInfo == null || 
/* 269 */           vsanConfig.clusterInfo.nodeUuid == null) {
/*     */ 
/*     */           
/* 272 */           hostsData.hostConnectionStates.remove(hostRef); continue;
/*     */         } 
/* 274 */         String nodeUuid = vsanConfig.clusterInfo.nodeUuid;
/* 275 */         String hostName = hostNames.get(hostRef);
/* 276 */         hostsData.hostNodeUuidToHostNames.put(nodeUuid, hostName);
/*     */       }
/*     */     
/* 279 */     } catch (Exception ex) {
/* 280 */       _logger.error("Failed to retrieve host names: ", ex);
/*     */     } 
/* 282 */     return hostsData;
/*     */   }
/*     */   
/*     */   private static class HostsData {
/*     */     private HostsData() {}
/*     */     
/* 288 */     Map<String, String> hostNodeUuidToHostNames = new HashMap<>();
/*     */     
/* 290 */     Map<ManagedObjectReference, HostSystem.ConnectionState> hostConnectionStates = new HashMap<>();
/*     */     
/* 292 */     Map<ManagedObjectReference, Version> hostVersions = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VsanJsonParser
/*     */   {
/*     */     private static final String DOM_OBJECTS_KEY = "dom_objects";
/*     */ 
/*     */     
/*     */     private static final String LSOM_OBJECTS_KEY = "lsom_objects";
/*     */ 
/*     */     
/*     */     private static final String CONFIG_KEY = "config";
/*     */ 
/*     */     
/*     */     private static final String CONTENT_KEY = "content";
/*     */ 
/*     */     
/*     */     private static final String TYPE_KEY = "type";
/*     */ 
/*     */     
/*     */     private static final String COMPONENT_TYPE = "Component";
/*     */     
/*     */     private static final String ATTRIBUTE_KEY = "attributes";
/*     */     
/*     */     private static final String OWNER_KEY = "owner";
/*     */ 
/*     */     
/*     */     public static Set<VsanObject> parseVsanResyncObjects(String resyncDataJsonStr, Map<String, String> hostNodeUuidsToHostNames) throws Exception {
/* 322 */       Set<VsanObject> result = new HashSet<>();
/*     */       try {
/* 324 */         Exception exception2, exception1 = null;
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
/*     */ 
/*     */       
/*     */       }
/* 364 */       catch (Exception ex) {
/* 365 */         VsanResyncingComponentsRetriever._logger.error("Failed to parse vsan resyncing data JSON string. ", ex);
/* 366 */         throw ex;
/*     */       } 
/* 368 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private static Map<String, String> getComponentUuidToHostNameMap(JsonNode root, Map<String, String> hostNodeUuidsToHostNames) {
/* 373 */       Map<String, String> result = new HashMap<>();
/*     */       
/* 375 */       JsonNode lsomObjects = root.get("lsom_objects");
/* 376 */       if (lsomObjects == null) {
/* 377 */         return result;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 383 */       Iterator<String> fnIterator = lsomObjects.getFieldNames();
/* 384 */       while (fnIterator.hasNext()) {
/* 385 */         String vsanObjectUuid = fnIterator.next();
/*     */         
/* 387 */         JsonNode vsanObjectNode = lsomObjects.get(vsanObjectUuid);
/* 388 */         if (vsanObjectNode == null || vsanObjectNode.isMissingNode()) {
/*     */           continue;
/*     */         }
/*     */         
/* 392 */         String ownerUuid = vsanObjectNode.path("owner").getTextValue();
/* 393 */         if (ownerUuid != null && hostNodeUuidsToHostNames.containsKey(ownerUuid)) {
/* 394 */           result.put(vsanObjectUuid, hostNodeUuidsToHostNames.get(ownerUuid));
/*     */         }
/*     */       } 
/*     */       
/* 398 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static List<VsanComponent> getComponentObjects(JsonNode node, Map<String, String> componentUuidsToHostNames) {
/* 406 */       List<VsanComponent> components = new ArrayList<>();
/* 407 */       if (node == null || node.isMissingNode() || !node.has("type"))
/*     */       {
/* 409 */         return components;
/*     */       }
/*     */ 
/*     */       
/* 413 */       if ("Component".equals(node.get("type").getTextValue())) {
/* 414 */         JsonNode attributeNode = node.get("attributes");
/* 415 */         if (attributeNode == null) {
/* 416 */           VsanResyncingComponentsRetriever._logger.warn("Missing attributes field for component node.");
/*     */           
/* 418 */           return components;
/*     */         } 
/*     */ 
/*     */         
/* 422 */         VsanComponent componentData = new VsanComponent(node, attributeNode, componentUuidsToHostNames);
/*     */         
/* 424 */         components.add(componentData);
/*     */       }
/*     */       else {
/*     */         
/* 428 */         Iterator<JsonNode> childNodes = node.getElements();
/* 429 */         while (childNodes.hasNext()) {
/* 430 */           List<VsanComponent> childComponents = 
/* 431 */             getComponentObjects(childNodes.next(), componentUuidsToHostNames);
/* 432 */           components.addAll(childComponents);
/*     */         } 
/*     */       } 
/* 435 */       return components;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/VsanResyncingComponentsRetriever.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */