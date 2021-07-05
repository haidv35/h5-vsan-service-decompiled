/*     */ package com.vmware.vsan.client.services.diskmanagement;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.PlugStoreTopology;
/*     */ import com.vmware.vim.binding.vim.host.StorageDeviceInfo;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ClusterStatus;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vim.vsan.host.HealthState;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapability;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsan.client.services.common.data.ConnectionState;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanCapabilityData;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ @data
/*     */ public class HostData {
/*  27 */   private static final Log logger = LogFactory.getLog(HostData.class);
/*     */   
/*  29 */   public static final String[] DS_HOST_PROPERTIES = new String[] {
/*  30 */       "name", 
/*  31 */       "primaryIconId", 
/*  32 */       "config.vsanHostConfig.faultDomainInfo.name", 
/*  33 */       "runtime.connectionState", 
/*  34 */       "runtime.inMaintenanceMode", 
/*  35 */       "config.product.version"
/*     */     };
/*     */   
/*     */   private static final String UNKNOWN_KEY = "vsan.common.unknown";
/*     */   
/*     */   public String name;
/*     */   public ConnectionState state;
/*     */   public boolean isInMaintenanceMode;
/*     */   public ManagedObjectReference hostRef;
/*     */   public DiskData[] disksInUse;
/*     */   public DiskData[] eligibleDisks;
/*     */   public DiskData[] ineligibleDisks;
/*     */   public String iconId;
/*     */   public String faultDomain;
/*     */   public Integer networkPartitionGroup;
/*     */   public boolean isWitnessHost;
/*     */   public DiskGroupData[] diskGroups;
/*     */   public String disksHealthAndVersionsJson;
/*     */   public HealthStatus healthStatus;
/*     */   public String version;
/*     */   public VsanCapabilityData capabilities;
/*     */   
/*     */   private static HostData parseHostProperties(Map<String, Object> hostProperties) {
/*  58 */     HostData hostData = new HostData();
/*  59 */     hostData.name = getStringProperty(hostProperties, "name");
/*  60 */     hostData.iconId = getStringProperty(hostProperties, "primaryIconId");
/*  61 */     hostData.faultDomain = getStringProperty(hostProperties, "config.vsanHostConfig.faultDomainInfo.name");
/*  62 */     hostData.isInMaintenanceMode = getBooleanProperty(hostProperties, "runtime.inMaintenanceMode").booleanValue();
/*  63 */     hostData.version = getStringProperty(hostProperties, "config.product.version");
/*     */     
/*  65 */     HostSystem.ConnectionState hostState = (HostSystem.ConnectionState)hostProperties.get("runtime.connectionState");
/*  66 */     hostData.state = ConnectionState.fromHostState(hostState);
/*  67 */     return hostData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HostData create(ManagedObjectReference hostRef, boolean isWitness, Map<String, Object> hostProperties, DiskMapInfoEx[] diskGroups, DiskResult[] allDisks, String disksHealthAndVersionJson, StorageDeviceInfo hostDeviceInfo, ClusterStatus healthStatus, Integer networkPartition, VsanCapabilityData capabilities) {
/*  75 */     HostData hostData = parseHostProperties(hostProperties);
/*  76 */     hostData.hostRef = hostRef;
/*  77 */     hostData.isWitnessHost = isWitness;
/*  78 */     hostData.disksHealthAndVersionsJson = disksHealthAndVersionJson;
/*  79 */     hostData.networkPartitionGroup = networkPartition;
/*  80 */     hostData.healthStatus = HealthStatus.fromVmodl(healthStatus);
/*  81 */     hostData.capabilities = capabilities;
/*     */     
/*  83 */     Map<String, PlugStoreTopology.Target> targetsMap = DiskData.mapAvailableTargets(hostDeviceInfo);
/*  84 */     Map<String, PlugStoreTopology.Adapter> adaptersMap = DiskData.mapAvailableAdapters(hostDeviceInfo);
/*  85 */     Map<String, PlugStoreTopology.Path> disksMap = DiskData.mapDiskPaths(hostDeviceInfo);
/*     */     
/*  87 */     Map<String, DiskData> claimedDisksByUuid = new HashMap<>();
/*  88 */     List<DiskData> eligibleDisks = new ArrayList<>();
/*  89 */     List<DiskData> ineligibleDisks = new ArrayList<>();
/*     */     
/*  91 */     if (allDisks != null) {
/*  92 */       byte b; int i; DiskResult[] arrayOfDiskResult; for (i = (arrayOfDiskResult = allDisks).length, b = 0; b < i; ) { DiskResult vsanDisk = arrayOfDiskResult[b];
/*  93 */         DiskResult.State state = DiskResult.State.valueOf(vsanDisk.state);
/*  94 */         DiskData disk = createDiskData(targetsMap, adaptersMap, disksMap, vsanDisk);
/*  95 */         String vsanUuid = disk.vsanUuid;
/*  96 */         if (StringUtils.isNotEmpty(vsanUuid)) {
/*  97 */           claimedDisksByUuid.put(vsanDisk.disk.uuid, disk);
/*     */         } else {
/*  99 */           switch (state) {
/*     */             case ineligible:
/* 101 */               ineligibleDisks.add(disk);
/*     */               break;
/*     */             case null:
/* 104 */               eligibleDisks.add(disk);
/*     */               break;
/*     */             case inUse:
/* 107 */               claimedDisksByUuid.put(vsanDisk.disk.uuid, disk);
/*     */               break;
/*     */             default:
/* 110 */               logger.warn("Unknown disk status: " + vsanDisk.state); break;
/*     */           } 
/*     */         }  b++; }
/*     */     
/*     */     } 
/* 115 */     hostData.eligibleDisks = eligibleDisks.<DiskData>toArray(new DiskData[eligibleDisks.size()]);
/* 116 */     hostData.ineligibleDisks = ineligibleDisks.<DiskData>toArray(new DiskData[ineligibleDisks.size()]);
/*     */     
/* 118 */     if (diskGroups != null) {
/* 119 */       DiskGroupData[] children = new DiskGroupData[diskGroups.length];
/* 120 */       for (int i = 0; i < diskGroups.length; i++) {
/* 121 */         DiskGroupData groupData = DiskGroupData.fromMapping(hostRef, diskGroups[i], claimedDisksByUuid);
/* 122 */         children[i] = groupData;
/*     */       } 
/* 124 */       hostData.diskGroups = children;
/*     */     } else {
/* 126 */       hostData.diskGroups = new DiskGroupData[0];
/*     */     } 
/*     */     
/* 129 */     hostData.disksInUse = (DiskData[])claimedDisksByUuid.values().toArray((Object[])new DiskData[claimedDisksByUuid.size()]);
/*     */     
/* 131 */     return hostData;
/*     */   }
/*     */   
/*     */   public static Integer getNetworkPartitionGroup(String nodeUuid, Collection<Set<String>> partitionGroups) {
/* 135 */     Iterator<Set<String>> iterator = partitionGroups.iterator();
/* 136 */     int index = 0;
/* 137 */     while (iterator.hasNext()) {
/* 138 */       Set<String> group = iterator.next();
/* 139 */       if (group.contains(nodeUuid)) {
/* 140 */         return Integer.valueOf(index + 1);
/*     */       }
/* 142 */       index++;
/*     */     } 
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<ManagedObjectReference, VsanCapabilityData> mapCapabilities(VsanCapability[] vsanCapabilities, ManagedObjectReference clusterRef) {
/* 149 */     Map<ManagedObjectReference, VsanCapabilityData> hostCapabilities = new HashMap<>();
/* 150 */     if (vsanCapabilities != null) {
/* 151 */       byte b; int i; VsanCapability[] arrayOfVsanCapability; for (i = (arrayOfVsanCapability = vsanCapabilities).length, b = 0; b < i; ) { VsanCapability data = arrayOfVsanCapability[b];
/* 152 */         VsanCapabilityData capabilityData = VsanCapabilityData.fromVsanCapability(data);
/* 153 */         ManagedObjectReference hostRef = new ManagedObjectReference(
/* 154 */             data.target.getType(), data.target.getValue(), clusterRef.getServerGuid());
/* 155 */         hostCapabilities.put(hostRef, capabilityData); b++; }
/*     */     
/*     */     } 
/* 158 */     return hostCapabilities;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DiskData createDiskData(Map<String, PlugStoreTopology.Target> targetsMap, Map<String, PlugStoreTopology.Adapter> adaptersMap, Map<String, PlugStoreTopology.Path> disksMap, DiskResult vsanDisk) {
/* 165 */     PlugStoreTopology.Target target = null;
/* 166 */     PlugStoreTopology.Adapter adapter = null;
/* 167 */     if (disksMap.containsKey(vsanDisk.disk.uuid)) {
/* 168 */       PlugStoreTopology.Path path = disksMap.get(vsanDisk.disk.uuid);
/* 169 */       if (targetsMap.containsKey(path.target)) {
/* 170 */         target = targetsMap.get(path.target);
/*     */       }
/* 172 */       if (adaptersMap.containsKey(path.adapter)) {
/* 173 */         adapter = adaptersMap.get(path.adapter);
/*     */       }
/*     */     } 
/* 176 */     String vsanUuid = vsanDisk.vsanUuid;
/* 177 */     return DiskData.fromScsiDisk(vsanDisk.disk, 
/* 178 */         vsanUuid, 
/* 179 */         target, 
/* 180 */         adapter);
/*     */   }
/*     */   
/*     */   private static String getStringProperty(Map<String, Object> properties, String propertyName) {
/*     */     try {
/* 185 */       return properties.get(propertyName).toString();
/* 186 */     } catch (Exception ex) {
/* 187 */       logger.warn("Unable to extract '" + propertyName + "' property: ", ex);
/* 188 */       return Utils.getLocalizedString("vsan.common.unknown");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Boolean getBooleanProperty(Map<String, Object> properties, String propertyName) {
/*     */     try {
/* 194 */       return Boolean.valueOf(Boolean.parseBoolean(properties.get(propertyName).toString()));
/* 195 */     } catch (Exception ex) {
/* 196 */       logger.warn("Unable to extract '" + propertyName + "' property: ", ex);
/* 197 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum HealthStatus
/*     */   {
/* 204 */     HEALTHY, UNHEALTHY, UNKNOWN;
/*     */     
/*     */     public static HealthStatus fromVmodl(ClusterStatus status) {
/* 207 */       if (status == null || status.health == null) {
/* 208 */         return UNKNOWN;
/*     */       }
/* 210 */       switch (HealthState.valueOf(status.health)) {
/*     */         case null:
/* 212 */           return HEALTHY;
/*     */         case unhealthy:
/* 214 */           return UNHEALTHY;
/*     */       } 
/* 216 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskmanagement/HostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */