/*     */ package com.vmware.vsphere.client.vsan.base.service;
/*     */ import com.vmware.vim.binding.vim.VsanUpgradeSystem;
/*     */ import com.vmware.vim.binding.vim.host.VsanSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.client.Client;
/*     */ import com.vmware.vim.vmomi.core.RequestContext;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlTypeMap;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanPhoneHomeSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanUpgradeSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanVcPrecheckerSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilitySystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerformanceManager;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanUpdateManager;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanVdsSystem;
/*     */ 
/*     */ public class VsanServiceImpl implements VsanService {
/*     */   private static final String MO_ID_START_INDEX = "-";
/*     */   private final VmodlTypeMap _typeMap;
/*     */   private final RequestContext _sessionContext;
/*     */   private final Client _vmomiClient;
/*     */   private final String _serviceGuid;
/*     */   private VsanVcStretchedClusterSystem _vsanStretchedClusterSystem;
/*     */   private VsanVcDiskManagementSystem _vsanDiskManagementSystem;
/*     */   private VsanVcClusterConfigSystem _vsanConfigSystem;
/*     */   private VsanPerformanceManager _vsanPerformanceManager;
/*     */   private VsanUpgradeSystem _vsanUpgradeSystem;
/*     */   private VsanUpgradeSystem _vsanLegacyUpgradeSystem;
/*     */   private VsanUpgradeSystemEx _vsanupgradeSystemEx;
/*     */   private VsanVcClusterHealthSystem _vsanVcClusterHealthSystem;
/*     */   private VsanObjectSystem _vsanObjectSystem;
/*     */   private VsanIscsiTargetSystem _vsanIscsiSystem;
/*     */   private VsanSpaceReportSystem _vsanSpaceReportSystem;
/*     */   private VsanCapabilitySystem _vsanCapabilitySystem;
/*     */   private VsanUpdateManager _vsanUpdateManager;
/*  41 */   private static final Log _logger = LogFactory.getLog(VsanServiceImpl.class); private VsanVdsSystem _vsanVdsSystem; private VsanVcPrecheckerSystem _vsanPrecheckerSystem; private VsanPhoneHomeSystem _vsanPhoneHomeSystem;
/*     */   private VsanClusterMgmtInternalSystem _vsanClusterMgmtInternalSystem;
/*     */   private VsanVumSystem _vsanVumSystem;
/*     */   
/*  45 */   private enum VsanMO { VC_CLUSTER_HEALTH_SYSTEM("VsanVcClusterHealthSystem", "vsan-cluster-health-system"),
/*  46 */     UPGRADE_SYSTEM("VsanUpgradeSystem", "vsan-upgrade-system2"),
/*  47 */     LEGACY_UPGRADE_SYSTEM("VsanUpgradeSystem", "vsan-upgrade-system"),
/*  48 */     UPGRADE_SYSTEM_EX("VsanUpgradeSystemEx", "vsan-upgrade-systemex"),
/*  49 */     STRETCHED_CLUSTER("VimClusterVsanVcStretchedClusterSystem", "vsan-stretched-cluster-system"),
/*  50 */     CLUSTER_CONFIG_SYSTEM("VsanVcClusterConfigSystem", "vsan-cluster-config-system"),
/*  51 */     DISK_MANAGEMENT_SYSTEM("VimClusterVsanVcDiskManagementSystem", "vsan-disk-management-system"),
/*  52 */     PERFORMANCE_MANAGER("VsanPerformanceManager", "vsan-performance-manager"),
/*  53 */     CAPABILITY_SYSTEM("VsanCapabilitySystem", "vsan-vc-capability-system"),
/*  54 */     OBJECT_SYSTEM("VsanObjectSystem", "vsan-cluster-object-system"),
/*  55 */     ISCSI_TARGET_SYSTEM("VsanIscsiTargetSystem", "vsan-cluster-iscsi-target-system"),
/*  56 */     SPACE_REPORTING_SYSTEM("VsanSpaceReportSystem", "vsan-cluster-space-report-system"),
/*  57 */     SYSTEM_EX("VsanSystemEx", "vsanSystemEx"),
/*  58 */     VSAN_SYSTEM("HostVsanSystem", "vsanSystem"),
/*  59 */     VSAN_VDS_SYSTEM("VsanVdsSystem", "vsan-vds-system"),
/*  60 */     VSAN_VC_PRECHECKER_SYSTEM("VsanVcPrecheckerSystem", "ha-vsan-vc-prechecker-system"),
/*  61 */     VSAN_PHONEHOME_SYSTEM("VsanPhoneHomeSystem", "vsan-phonehome-system"),
/*  62 */     VSAN_UPDATE_MANAGER("VsanUpdateManager", "vsan-update-manager"),
/*  63 */     VSAN_CLUSTER_MGMT_INTERNAL_SYSTEM("VsanClusterMgmtInternalSystem", "vsan-cluster-mgmt-internal-system"),
/*  64 */     VSAN_VUM_SYSTEM("VsanVumSystem", "vsan-vum-system");
/*     */     
/*     */     private String type;
/*     */     
/*     */     private String id;
/*     */     
/*     */     VsanMO(String type, String id) {
/*  71 */       this.type = type;
/*  72 */       this.id = id;
/*     */     } }
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
/*     */   public VsanServiceImpl(Client vmomiClient, VmodlTypeMap vmodlTypeMap, RequestContext sessionContext, String serviceGuid) {
/* 102 */     this._typeMap = vmodlTypeMap;
/* 103 */     this._sessionContext = sessionContext;
/* 104 */     this._vmomiClient = vmomiClient;
/* 105 */     this._serviceGuid = serviceGuid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends ManagedObject> T getManagedObject(ManagedObjectReference moRef) {
/* 115 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 117 */       Thread.currentThread().setContextClassLoader(VsanServiceImpl.class.getClassLoader());
/* 118 */       VmodlType vmodlType = this._typeMap.getVmodlType(moRef.getType());
/* 119 */       Class<T> typeClass = vmodlType.getTypeClass();
/*     */       
/* 121 */       ManagedObject managedObject = this._vmomiClient.createStub(typeClass, moRef);
/*     */ 
/*     */       
/* 124 */       ((Stub)managedObject)._setRequestContext(this._sessionContext);
/* 125 */       return (T)managedObject;
/*     */     } finally {
/* 127 */       Thread.currentThread().setContextClassLoader(oldClassLoader);
/*     */     } 
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
/*     */   private <T extends ManagedObject> T createStub(VsanMO vsanMo) {
/* 140 */     return createStub(vsanMo.type, vsanMo.id, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends ManagedObject> T createStub(VsanMO vsanMo, ManagedObjectReference moRef) {
/* 158 */     if (moRef == null) {
/* 159 */       _logger.warn("The given Managed Object is null.");
/* 160 */       return createStub(vsanMo);
/*     */     } 
/*     */     
/* 163 */     String id = vsanMo.id;
/* 164 */     String moId = getMoId(moRef);
/* 165 */     if (!StringUtils.isEmpty(moId)) {
/* 166 */       id = String.valueOf(id) + moId;
/*     */     } else {
/* 168 */       _logger.warn("The ID cannot be extracted from this ManagedObject: " + moId);
/*     */     } 
/*     */     
/* 171 */     return createStub(vsanMo.type, id, moRef);
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
/*     */   private static String getMoId(ManagedObjectReference moRef) {
/* 183 */     int index = moRef.getValue().indexOf("-");
/* 184 */     return moRef.getValue().substring(index);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends ManagedObject> T createStub(String moRefType, String moRefId, ManagedObjectReference moRef) {
/* 189 */     String serverGuild = (moRef != null) ? moRef.getServerGuid() : null;
/* 190 */     return getManagedObject(new ManagedObjectReference(moRefType, moRefId, serverGuild));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getServiceGuid() {
/* 195 */     return this._serviceGuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public void logout() {
/*     */     try {
/* 201 */       if (this._vmomiClient != null) {
/* 202 */         this._vmomiClient.shutdown();
/*     */       }
/* 204 */     } catch (Exception ex) {
/* 205 */       _logger.error("Failed to shutdown vlsi client: " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVcStretchedClusterSystem getVsanStretchedClusterSystem() {
/* 211 */     if (this._vsanStretchedClusterSystem == null) {
/* 212 */       this._vsanStretchedClusterSystem = createStub(VsanMO.STRETCHED_CLUSTER);
/*     */     }
/* 214 */     return this._vsanStretchedClusterSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVcClusterConfigSystem getVsanConfigSystem() {
/* 219 */     if (this._vsanConfigSystem == null) {
/* 220 */       this._vsanConfigSystem = createStub(VsanMO.CLUSTER_CONFIG_SYSTEM);
/*     */     }
/* 222 */     return this._vsanConfigSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVcDiskManagementSystem getVsanDiskManagementSystem() {
/* 227 */     if (this._vsanDiskManagementSystem == null) {
/* 228 */       this._vsanDiskManagementSystem = createStub(VsanMO.DISK_MANAGEMENT_SYSTEM);
/*     */     }
/* 230 */     return this._vsanDiskManagementSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanPerformanceManager getVsanPerformanceManager() {
/* 235 */     if (this._vsanPerformanceManager == null) {
/* 236 */       this._vsanPerformanceManager = createStub(VsanMO.PERFORMANCE_MANAGER);
/*     */     }
/* 238 */     return this._vsanPerformanceManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanUpgradeSystem getVsanUpgradeSystem() {
/* 243 */     if (this._vsanUpgradeSystem == null) {
/* 244 */       this._vsanUpgradeSystem = createStub(VsanMO.UPGRADE_SYSTEM);
/*     */     }
/* 246 */     return this._vsanUpgradeSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanUpgradeSystem getVsanLegacyUpgradeSystem() {
/* 251 */     if (this._vsanLegacyUpgradeSystem == null) {
/* 252 */       this._vsanLegacyUpgradeSystem = createStub(VsanMO.LEGACY_UPGRADE_SYSTEM);
/*     */     }
/* 254 */     return this._vsanLegacyUpgradeSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanUpgradeSystemEx getVsanUpgradeSystemEx() {
/* 259 */     if (this._vsanupgradeSystemEx == null) {
/* 260 */       this._vsanupgradeSystemEx = createStub(VsanMO.UPGRADE_SYSTEM_EX);
/*     */     }
/* 262 */     return this._vsanupgradeSystemEx;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVcClusterHealthSystem getVsanVcClusterHealthSystem() {
/* 267 */     if (this._vsanVcClusterHealthSystem == null) {
/* 268 */       this._vsanVcClusterHealthSystem = createStub(VsanMO.VC_CLUSTER_HEALTH_SYSTEM);
/*     */     }
/* 270 */     return this._vsanVcClusterHealthSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanObjectSystem getVsanObjectSystem() {
/* 275 */     if (this._vsanObjectSystem == null) {
/* 276 */       this._vsanObjectSystem = createStub(VsanMO.OBJECT_SYSTEM);
/*     */     }
/* 278 */     return this._vsanObjectSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanIscsiTargetSystem getVsanIscsiSystem() {
/* 283 */     if (this._vsanIscsiSystem == null) {
/* 284 */       this._vsanIscsiSystem = createStub(VsanMO.ISCSI_TARGET_SYSTEM);
/*     */     }
/* 286 */     return this._vsanIscsiSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanSpaceReportSystem getVsanSpaceReportSystem() {
/* 291 */     if (this._vsanSpaceReportSystem == null) {
/* 292 */       this._vsanSpaceReportSystem = createStub(VsanMO.SPACE_REPORTING_SYSTEM);
/*     */     }
/*     */     
/* 295 */     return this._vsanSpaceReportSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanCapabilitySystem getVsanCapabilitySystem() {
/* 300 */     if (this._vsanCapabilitySystem == null) {
/* 301 */       this._vsanCapabilitySystem = createStub(VsanMO.CAPABILITY_SYSTEM);
/*     */     }
/* 303 */     return this._vsanCapabilitySystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanSystemEx getVsanSystemEx(ManagedObjectReference moRef) {
/* 308 */     VsanSystemEx vsanSystemEx = createStub(VsanMO.SYSTEM_EX, moRef);
/* 309 */     return vsanSystemEx;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanSystem getVsanSystem(ManagedObjectReference moRef) {
/* 314 */     VsanSystem vsanSystem = createStub(VsanMO.VSAN_SYSTEM, moRef);
/* 315 */     return vsanSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanUpdateManager getUpdateManager() {
/* 320 */     if (this._vsanUpdateManager == null) {
/* 321 */       this._vsanUpdateManager = createStub(VsanMO.VSAN_UPDATE_MANAGER);
/*     */     }
/* 323 */     return this._vsanUpdateManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVdsSystem getVdsSystem() {
/* 328 */     if (this._vsanVdsSystem == null) {
/* 329 */       this._vsanVdsSystem = createStub(VsanMO.VSAN_VDS_SYSTEM);
/*     */     }
/* 331 */     return this._vsanVdsSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVcPrecheckerSystem getVsanPreCheckerSystem() {
/* 336 */     if (this._vsanPrecheckerSystem == null) {
/* 337 */       this._vsanPrecheckerSystem = createStub(VsanMO.VSAN_VC_PRECHECKER_SYSTEM);
/*     */     }
/* 339 */     return this._vsanPrecheckerSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanPhoneHomeSystem getPhoneHomeSystem() {
/* 344 */     if (this._vsanPhoneHomeSystem == null) {
/* 345 */       this._vsanPhoneHomeSystem = createStub(VsanMO.VSAN_PHONEHOME_SYSTEM);
/*     */     }
/* 347 */     return this._vsanPhoneHomeSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanClusterMgmtInternalSystem getVsanClusterMgmtInternalSystem() {
/* 352 */     if (this._vsanClusterMgmtInternalSystem == null) {
/* 353 */       this._vsanClusterMgmtInternalSystem = createStub(VsanMO.VSAN_CLUSTER_MGMT_INTERNAL_SYSTEM);
/*     */     }
/* 355 */     return this._vsanClusterMgmtInternalSystem;
/*     */   }
/*     */ 
/*     */   
/*     */   public VsanVumSystem getVsanVumSystem() {
/* 360 */     if (this._vsanVumSystem == null) {
/* 361 */       this._vsanVumSystem = createStub(VsanMO.VSAN_VUM_SYSTEM);
/*     */     }
/* 363 */     return this._vsanVumSystem;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */