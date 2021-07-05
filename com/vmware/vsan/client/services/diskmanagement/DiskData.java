/*     */ package com.vmware.vsan.client.services.diskmanagement;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.host.PlugStoreTopology;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.host.ScsiLun;
/*     */ import com.vmware.vim.binding.vim.host.StorageDeviceInfo;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.data.DiskLocalityType;
/*     */ import com.vmware.vsphere.client.vsan.util.FormatUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ @data
/*     */ public class DiskData
/*     */ {
/*     */   private static final String ADAPTER_ID_PREFIX = "key-vim.host.PlugStoreTopology.Adapter-";
/*     */   private static final String DEVICE_ID_PREFIX = "key-vim.host.PlugStoreTopology.Device-";
/*     */   public String name;
/*     */   public boolean isFlash;
/*     */   public boolean isMappedAsCache;
/*     */   public long capacity;
/*     */   public DeviceState deviceState;
/*     */   public String uuid;
/*     */   public String vsanUuid;
/*     */   public String diskGroup;
/*     */   public DiskLocalityType driveLocality;
/*     */   public String[] physicalLocation;
/*     */   public String diskAdapter;
/*     */   public StorageDeviceTransport transportType;
/*     */   public String vendor;
/*     */   public ScsiDisk disk;
/*     */   
/*     */   public static DiskData fromScsiDisk(ScsiDisk disk, String diskGroup, boolean isMappedAsCache, String vsanUuid, PlugStoreTopology.Target target, PlugStoreTopology.Adapter adapter) {
/*  47 */     DiskData data = new DiskData();
/*  48 */     data.disk = disk;
/*  49 */     data.name = (disk.displayName != null) ? disk.displayName : disk.canonicalName;
/*  50 */     data.isFlash = Boolean.TRUE.equals(disk.ssd);
/*  51 */     data.isMappedAsCache = isMappedAsCache;
/*  52 */     data.capacity = disk.capacity.block * disk.capacity.blockSize;
/*  53 */     data.deviceState = DeviceState.fromScsiState(disk.operationalState);
/*     */     
/*  55 */     data.uuid = disk.uuid;
/*  56 */     if (adapter != null) {
/*  57 */       data.diskAdapter = extractDiskId(adapter.key, "key-vim.host.PlugStoreTopology.Adapter-");
/*     */     }
/*  59 */     if (target != null) {
/*  60 */       data.transportType = StorageDeviceTransport.getTransport(target);
/*     */     }
/*  62 */     data.vsanUuid = vsanUuid;
/*  63 */     data.diskGroup = diskGroup;
/*  64 */     data.physicalLocation = disk.physicalLocation;
/*  65 */     data.vendor = String.valueOf(disk.vendor) + disk.model + 
/*  66 */       FormatUtil.getStorageFormatted(Long.valueOf(data.capacity), 1L, 1073741824L);
/*  67 */     if (disk.localDisk != null) {
/*  68 */       data.driveLocality = disk.localDisk.booleanValue() ? DiskLocalityType.Local : DiskLocalityType.Remote;
/*     */     } else {
/*  70 */       data.driveLocality = DiskLocalityType.Unknown;
/*     */     } 
/*  72 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskData fromScsiDisk(ScsiDisk disk, String vsanUuid, PlugStoreTopology.Target target, PlugStoreTopology.Adapter adapter) {
/*  79 */     return fromScsiDisk(disk, null, false, vsanUuid, target, adapter);
/*     */   }
/*     */   
/*     */   private static String extractDiskId(String deviceId, String prefix) {
/*  83 */     if (!deviceId.startsWith(prefix)) {
/*  84 */       throw new IllegalStateException("illegal device ID: " + deviceId + ", should start with " + prefix);
/*     */     }
/*  86 */     return deviceId.substring(prefix.length());
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum StorageDeviceTransport
/*     */   {
/*  92 */     FCOETRANSPORT,
/*  93 */     FCTRANSPORT,
/*  94 */     ISCSITRANSPORT,
/*  95 */     PARALLELTRANSPORT,
/*  96 */     BLOCKTRANSPORT,
/*  97 */     SASTRANSPORT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static StorageDeviceTransport getTransport(PlugStoreTopology.Target target) {
/* 106 */       if (target == null || target.transport == null) {
/* 107 */         return null;
/*     */       }
/* 109 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.FibreChannelOverEthernetTargetTransport) {
/* 110 */         return FCOETRANSPORT;
/*     */       }
/* 112 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.FibreChannelTargetTransport) {
/* 113 */         return FCTRANSPORT;
/*     */       }
/* 115 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.InternetScsiTargetTransport) {
/* 116 */         return ISCSITRANSPORT;
/*     */       }
/* 118 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.ParallelScsiTargetTransport) {
/* 119 */         return PARALLELTRANSPORT;
/*     */       }
/* 121 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.BlockAdapterTargetTransport) {
/* 122 */         return BLOCKTRANSPORT;
/*     */       }
/* 124 */       if (target.transport instanceof com.vmware.vim.binding.vim.host.SerialAttachedTargetTransport) {
/* 125 */         return SASTRANSPORT;
/*     */       }
/* 127 */       throw new IllegalArgumentException("unknown target: " + target);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @data
/*     */   public enum DeviceState
/*     */   {
/* 135 */     OK, OFF, LOST, ERROR, UNKNOWN;
/*     */     
/*     */     public static DeviceState fromScsiState(String[] stateKeys) {
/* 138 */       Set<ScsiLun.State> states = new HashSet<>(); byte b; int i; String[] arrayOfString;
/* 139 */       for (i = (arrayOfString = stateKeys).length, b = 0; b < i; ) { String key = arrayOfString[b];
/* 140 */         states.add(ScsiLun.State.valueOf(key)); b++; }
/*     */       
/* 142 */       if (states.contains(ScsiLun.State.ok)) {
/* 143 */         return OK;
/*     */       }
/* 145 */       if (states.contains(ScsiLun.State.off)) {
/* 146 */         return OFF;
/*     */       }
/* 148 */       if (states.contains(ScsiLun.State.lostCommunication)) {
/* 149 */         return LOST;
/*     */       }
/* 151 */       if (states.contains(ScsiLun.State.error)) {
/* 152 */         return ERROR;
/*     */       }
/*     */ 
/*     */       
/* 156 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, PlugStoreTopology.Target> mapAvailableTargets(StorageDeviceInfo info) {
/* 165 */     Map<String, PlugStoreTopology.Target> result = new HashMap<>();
/* 166 */     if (info != null && info.plugStoreTopology != null && info.plugStoreTopology.target != null) {
/* 167 */       byte b; int i; PlugStoreTopology.Target[] arrayOfTarget; for (i = (arrayOfTarget = info.plugStoreTopology.target).length, b = 0; b < i; ) { PlugStoreTopology.Target target = arrayOfTarget[b];
/* 168 */         result.put(target.getKey(), target); b++; }
/*     */     
/*     */     } 
/* 171 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, PlugStoreTopology.Adapter> mapAvailableAdapters(StorageDeviceInfo info) {
/* 178 */     Map<String, PlugStoreTopology.Adapter> result = new HashMap<>();
/* 179 */     if (info != null && info.plugStoreTopology != null && info.plugStoreTopology.adapter != null) {
/* 180 */       byte b; int i; PlugStoreTopology.Adapter[] arrayOfAdapter; for (i = (arrayOfAdapter = info.plugStoreTopology.adapter).length, b = 0; b < i; ) { PlugStoreTopology.Adapter adapter = arrayOfAdapter[b];
/* 181 */         result.put(adapter.getKey(), adapter); b++; }
/*     */     
/*     */     } 
/* 184 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, PlugStoreTopology.Path> mapDiskPaths(StorageDeviceInfo info) {
/* 191 */     Map<String, PlugStoreTopology.Path> disksMap = new HashMap<>();
/* 192 */     if (info != null && info.plugStoreTopology != null) {
/* 193 */       byte b; int i; PlugStoreTopology.Path[] arrayOfPath; for (i = (arrayOfPath = info.plugStoreTopology.path).length, b = 0; b < i; ) { PlugStoreTopology.Path path = arrayOfPath[b];
/* 194 */         if (!StringUtils.isEmpty(path.device))
/*     */         {
/*     */ 
/*     */           
/* 198 */           disksMap.put(extractDiskId(path.device, "key-vim.host.PlugStoreTopology.Device-"), path); }  b++; }
/*     */     
/*     */     } 
/* 201 */     return disksMap;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskmanagement/DiskData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */