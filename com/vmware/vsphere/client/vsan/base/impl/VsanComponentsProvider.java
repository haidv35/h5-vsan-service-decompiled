/*     */ package com.vmware.vsphere.client.vsan.base.impl;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComponent;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComponentState;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanRaidConfig;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanRootConfig;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanComponentsProvider
/*     */ {
/*  30 */   public static final VsanProfiler _profiler = new VsanProfiler(VsanComponentsProvider.class);
/*  31 */   private static final Log _logger = LogFactory.getLog(VsanComponentsProvider.class);
/*     */   
/*     */   private final VcClient _vcClient;
/*     */   
/*     */   public VsanComponentsProvider(VcClient vcClient) {
/*  36 */     this._vcClient = vcClient;
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
/*     */   @TsService
/*     */   public List<VsanObject> getVsanComponents(ManagedObjectReference clusterRef, String[] vsanObjectUuids) throws Exception {
/*  50 */     if (ArrayUtils.isEmpty((Object[])vsanObjectUuids)) {
/*  51 */       return new ArrayList<>(0);
/*     */     }
/*     */     
/*  54 */     Exception exception1 = null, exception2 = null; try {
/*     */     
/*     */     } finally {
/*  57 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<String> getVsanComponentsAsync(ManagedObjectReference clusterRef, String[] vsanObjectUuids, Measure measure) throws Exception {
/*  64 */     Future<String> jsonFuture = measure.newFuture("VsanObject[]");
/*  65 */     Exception exception1 = null, exception2 = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VsanJsonParser
/*     */   {
/*     */     private static final int VSAN_CONFIG_INCOMPLETE = 8;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static List<VsanObject> parseVsanObjects(String vsanJson, List<String> objectUuids) throws Exception {
/*  80 */       ArrayList<VsanObject> result = new ArrayList<>();
/*     */       
/*  82 */       if (vsanJson == null || objectUuids == null || objectUuids.size() == 0) {
/*  83 */         return result;
/*     */       }
/*     */       
/*  86 */       JsonNode root = Utils.getJsonRootNode(vsanJson);
/*  87 */       if (root == null) {
/*  88 */         return result;
/*     */       }
/*     */       
/*  91 */       JsonNode domObjects = root.get("dom_objects");
/*  92 */       JsonNode lsomObjects = root.get("lsom_objects");
/*  93 */       JsonNode diskObjects = root.get("disk_objects");
/*     */       
/*  95 */       if (domObjects == null || lsomObjects == null || diskObjects == null) {
/*  96 */         return result;
/*     */       }
/*     */       
/*  99 */       for (String uuid : objectUuids) {
/* 100 */         VsanObject vsanObject = new VsanObject(uuid);
/*     */         
/* 102 */         JsonNode objNode = domObjects.findPath(uuid);
/* 103 */         JsonNode config = objNode.findPath("config");
/* 104 */         JsonNode content = config.findPath("content");
/* 105 */         if (content == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 109 */         VsanRootConfig rootConfig = new VsanRootConfig();
/* 110 */         Iterator<JsonNode> elements = content.getElements();
/* 111 */         while (elements.hasNext()) {
/* 112 */           JsonNode contentChild = elements.next();
/*     */           
/* 114 */           if (!contentChild.has("type")) {
/*     */             continue;
/*     */           }
/* 117 */           String type = contentChild.get("type").asText();
/*     */           
/* 119 */           if ("Witness".equals(type)) {
/* 120 */             VsanComponent witnessComponent = getVsanComponent(diskObjects, 
/* 121 */                 lsomObjects, contentChild, type);
/*     */             
/* 123 */             rootConfig.children.add(witnessComponent); continue;
/* 124 */           }  if ("Component".equals(type)) {
/*     */ 
/*     */             
/* 127 */             VsanRaidConfig raid0Config = new VsanRaidConfig();
/* 128 */             raid0Config.type = Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.raid0");
/* 129 */             raid0Config.children = getVsanComponents(diskObjects, lsomObjects, content);
/* 130 */             rootConfig.children.add(raid0Config);
/*     */             break;
/*     */           } 
/* 133 */           VsanRaidConfig raidConfig = new VsanRaidConfig();
/* 134 */           raidConfig.type = getLocalizedType(type);
/* 135 */           raidConfig.children = getVsanComponents(diskObjects, lsomObjects, contentChild);
/* 136 */           if (raidConfig.children.size() > 0) {
/* 137 */             rootConfig.children.add(raidConfig);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 142 */         vsanObject.rootConfig = rootConfig;
/* 143 */         result.add(vsanObject);
/*     */       } 
/*     */       
/* 146 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     private static VsanComponent getVsanComponent(JsonNode diskObjects, JsonNode lsomObjects, JsonNode contentChild, String type) {
/* 151 */       VsanComponent component = new VsanComponent(true);
/* 152 */       JsonNode componentAttribute = contentChild.get("attributes");
/* 153 */       if (componentAttribute == null)
/*     */       {
/* 155 */         return component;
/*     */       }
/*     */       
/* 158 */       component.type = getLocalizedType(type);
/* 159 */       component.componentUuid = getValueStringByKey(contentChild, "componentUuid");
/*     */       
/* 161 */       int stateNumber = getValueIntByKey(componentAttribute, "componentState");
/* 162 */       long bytesToSyncProp = getValueLongByKey(componentAttribute, "bytesToSync");
/* 163 */       int flagsProp = getValueIntByKey(componentAttribute, "flags");
/*     */       
/* 165 */       component.byteToSync = bytesToSyncProp;
/* 166 */       if (bytesToSyncProp > 0L) {
/* 167 */         long recoveryETA = getValueLongByKey(componentAttribute, "recoveryETA");
/* 168 */         component.recoveryEta = recoveryETA;
/*     */       } 
/*     */       
/* 171 */       component.state = getVirtualPhysicalComponentState(stateNumber, bytesToSyncProp, flagsProp);
/*     */       
/* 173 */       String diskUuid = getValueStringByKey(contentChild, "diskUuid");
/* 174 */       JsonNode disk = diskObjects.get(diskUuid);
/* 175 */       if (diskUuid != null && diskUuid != "") {
/* 176 */         component.capacityDiskUuid = diskUuid;
/*     */       }
/*     */       
/* 179 */       if (disk != null) {
/* 180 */         JsonNode diskContent = disk.get("content");
/* 181 */         if (diskContent != null) {
/* 182 */           String cacheDiskUuid = getValueStringByKey(diskContent, "ssdUuid");
/* 183 */           if (StringUtils.isNotEmpty(cacheDiskUuid)) {
/* 184 */             component.cacheDiskUuid = cacheDiskUuid;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 189 */       JsonNode lsomComponent = lsomObjects.get(component.componentUuid);
/* 190 */       if (lsomComponent != null) {
/* 191 */         component.hostUuid = getValueStringByKey(lsomComponent, "owner");
/*     */       }
/*     */       
/* 194 */       return component;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static List<VsanComponent> getVsanComponents(JsonNode diskObjects, JsonNode lsomObjects, JsonNode content) {
/* 203 */       List<VsanComponent> children = new ArrayList<>();
/* 204 */       Iterator<JsonNode> elements = content.getElements();
/* 205 */       while (elements.hasNext()) {
/* 206 */         JsonNode contentChild = elements.next();
/* 207 */         if (!contentChild.has("type")) {
/*     */           continue;
/*     */         }
/* 210 */         String type = contentChild.get("type").asText();
/*     */         
/* 212 */         if ("Component".equals(type)) {
/* 213 */           VsanComponent item = getVsanComponent(diskObjects, 
/* 214 */               lsomObjects, 
/* 215 */               contentChild, 
/* 216 */               type);
/* 217 */           children.add(item); continue;
/*     */         } 
/* 219 */         List<VsanComponent> childrenItems = getVsanComponents(diskObjects, 
/* 220 */             lsomObjects, 
/* 221 */             contentChild);
/* 222 */         if (childrenItems != null && childrenItems.size() > 0) {
/* 223 */           VsanRaidConfig raidItem = new VsanRaidConfig();
/* 224 */           raidItem.type = getLocalizedType(type);
/* 225 */           raidItem.children = childrenItems;
/* 226 */           children.add(raidItem);
/*     */         } 
/*     */       } 
/*     */       
/* 230 */       return children;
/*     */     }
/*     */     
/*     */     private static String getLocalizedType(String type) {
/*     */       String str;
/* 235 */       switch ((str = type).hashCode()) { case -1885104965: if (!str.equals("RAID_0")) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 241 */           return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.raid0");
/*     */         case -1885104964: if (!str.equals("RAID_1"))
/* 243 */             break;  return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.raid1");
/*     */         case -1885104960: if (!str.equals("RAID_5"))
/* 245 */             break;  return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.raid5");
/*     */         case -1885104959: if (!str.equals("RAID_6"))
/* 247 */             break;  return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.raid6");case -1274991335: if (!str.equals("Witness"))
/*     */             break;  return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.witness");case 604060893: if (!str.equals("Component"))
/* 249 */             break;  return Utils.getLocalizedString("vsan.monitor.virtualPhysicalMapping.component"); }  return "";
/*     */     }
/*     */     
/*     */     private static String getValueStringByKey(JsonNode node, String key) {
/* 253 */       if (node == null || node.isMissingNode() || !node.has(key)) {
/* 254 */         return "";
/*     */       }
/* 256 */       return node.get(key).asText();
/*     */     }
/*     */     
/*     */     private static long getValueLongByKey(JsonNode node, String key) {
/* 260 */       if (node == null || node.isMissingNode() || !node.has(key)) {
/* 261 */         return 0L;
/*     */       }
/* 263 */       return node.get(key).asLong();
/*     */     }
/*     */     
/*     */     private static int getValueIntByKey(JsonNode node, String key) {
/* 267 */       if (node == null || node.isMissingNode() || !node.has(key)) {
/* 268 */         return 0;
/*     */       }
/* 270 */       return node.get(key).asInt();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static VsanComponentState getVirtualPhysicalComponentState(int stateNumber, long bytesToSyncProp, int flagsProp) {
/* 278 */       switch (stateNumber) {
/*     */         case 5:
/* 280 */           if (flagsProp == 8) {
/* 281 */             return VsanComponentState.ACTIVE_STALE;
/*     */           }
/* 283 */           return VsanComponentState.ACTIVE;
/*     */         case 6:
/* 285 */           if (bytesToSyncProp > 0L) {
/* 286 */             return VsanComponentState.ABSENT_RESYNC;
/*     */           }
/* 288 */           return VsanComponentState.ABSENT;
/*     */         case 9:
/* 290 */           return VsanComponentState.DEGRADED;
/*     */         case 10:
/* 292 */           return VsanComponentState.RECONFIG;
/*     */       } 
/* 294 */       return VsanComponentState.UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/impl/VsanComponentsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */