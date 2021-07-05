/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.VsanUpgradeSystem;
/*     */ import com.vmware.vim.binding.vim.host.VsanInternalSystem;
/*     */ import com.vmware.vim.binding.vim.host.VsanSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanPhoneHomeSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanUpgradeSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.VsanVcPrecheckerSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilitySystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterMgmtInternalSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerformanceManager;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanSpaceReportSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcDiskManagementSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVumSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
/*     */ import com.vmware.vim.vsan.binding.vim.host.VsanUpdateManager;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanVdsSystem;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.service.VsanService;
/*     */ import com.vmware.vsphere.client.vsan.base.service.VsanServiceFactory;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ public class VsanProviderUtils
/*     */ {
/*     */   public static final String HOST_VSAN_INTERNAL_SYSTEM = "configManager.vsanInternalSystem";
/*     */   public static final String HOST_VSAN_SYSTEM = "configManager.vsanSystem";
/*     */   public static final String HOST_CONNECTION_STATE_PROPERTY = "runtime.connectionState";
/*  42 */   private static final Log _logger = LogFactory.getLog(VsanProviderUtils.class);
/*     */   private static VsanServiceFactory _vsanServiceFactory;
/*     */   private static VmodlHelper _vmodlHelper;
/*     */   
/*     */   public static void setVsanServiceFactory(VsanServiceFactory factory) {
/*  47 */     _vsanServiceFactory = factory;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setVmodlHelper(VmodlHelper vmodlHelper) {
/*  52 */     _vmodlHelper = vmodlHelper;
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
/*     */   public static VsanSystem getHostVsanSystem(ManagedObjectReference hostRef, VcConnection vcConnection) throws Exception {
/*  64 */     ManagedObjectReference vsanSystemRef = (ManagedObjectReference)QueryUtil.getProperty(hostRef, "configManager.vsanSystem", null);
/*  65 */     if (vsanSystemRef == null) {
/*  66 */       _logger.error("getHostVsanSystem: failed to retrieve host's vsan system.");
/*  67 */       return null;
/*     */     } 
/*  69 */     return (VsanSystem)vcConnection.createStub(VsanSystem.class, vsanSystemRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanInternalSystem getVsanInternalSystem(ManagedObjectReference hostOrClusterRef, VcConnection vcConnection) throws Exception {
/*     */     PropertyValue[] values;
/*  81 */     ManagedObjectReference vsanInternalSystemRef = null;
/*     */ 
/*     */ 
/*     */     
/*  85 */     if (_vmodlHelper.isOfType(hostOrClusterRef, HostSystem.class)) {
/*  86 */       values = QueryUtil.getProperties(
/*  87 */           hostOrClusterRef, 
/*  88 */           new String[] { "configManager.vsanInternalSystem", "runtime.connectionState" }).getPropertyValues();
/*     */     } else {
/*  90 */       values = QueryUtil.getPropertiesForRelatedObjects(
/*  91 */           hostOrClusterRef, "host", 
/*  92 */           HostSystem.class.getSimpleName(), new String[] {
/*  93 */             "configManager.vsanInternalSystem", "runtime.connectionState" }).getPropertyValues();
/*     */     } 
/*     */ 
/*     */     
/*  97 */     Set<ManagedObjectReference> hostRefs = filterConnectedHosts(values); byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/*  99 */     for (i = (arrayOfPropertyValue1 = values).length, b = 0; b < i; ) { PropertyValue val = arrayOfPropertyValue1[b];
/* 100 */       if (hostRefs.contains(val.resourceObject))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 105 */         if (val.propertyName == "configManager.vsanInternalSystem") {
/*     */ 
/*     */ 
/*     */           
/* 109 */           vsanInternalSystemRef = (ManagedObjectReference)val.value;
/* 110 */           if (vsanInternalSystemRef != null)
/*     */             break; 
/*     */         }  } 
/*     */       b++; }
/*     */     
/* 115 */     if (vsanInternalSystemRef == null) {
/* 116 */       _logger.error("getVsanInternalSystem: failed to retrieve VsanInternalSystem for host or cluster: " + 
/* 117 */           hostOrClusterRef);
/* 118 */       return null;
/*     */     } 
/*     */     
/* 121 */     return (VsanInternalSystem)vcConnection.createStub(VsanInternalSystem.class, vsanInternalSystemRef);
/*     */   }
/*     */   
/*     */   private static Set<ManagedObjectReference> filterConnectedHosts(PropertyValue[] values) {
/* 125 */     Set<ManagedObjectReference> result = new HashSet<>(); byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue;
/* 127 */     for (i = (arrayOfPropertyValue = values).length, b = 0; b < i; ) { PropertyValue val = arrayOfPropertyValue[b];
/* 128 */       if (val.propertyName == "runtime.connectionState" && 
/* 129 */         HostSystem.ConnectionState.connected
/* 130 */         .equals(val.value)) {
/* 131 */         result.add((ManagedObjectReference)val.resourceObject);
/*     */       }
/*     */       b++; }
/*     */     
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanVcStretchedClusterSystem getVcStretchedClusterSystem(ManagedObjectReference moRef) {
/* 144 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 145 */     if (vsanService == null) {
/* 146 */       return null;
/*     */     }
/* 148 */     return vsanService.getVsanStretchedClusterSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanVcClusterConfigSystem getVsanConfigSystem(ManagedObjectReference moRef) {
/* 158 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 159 */     if (vsanService == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     return vsanService.getVsanConfigSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanVcDiskManagementSystem getVcDiskManagementSystem(ManagedObjectReference moRef) {
/* 171 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 172 */     if (vsanService == null) {
/* 173 */       return null;
/*     */     }
/* 175 */     return vsanService.getVsanDiskManagementSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanPerformanceManager getVsanPerformanceManager(ManagedObjectReference moRef) {
/* 184 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 185 */     if (vsanService == null) {
/* 186 */       return null;
/*     */     }
/* 188 */     return vsanService.getVsanPerformanceManager();
/*     */   }
/*     */   
/*     */   public static VsanUpgradeSystem getVsanUpgradeSystem(ManagedObjectReference moRef) {
/* 192 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 193 */     if (vsanService == null) {
/* 194 */       return null;
/*     */     }
/* 196 */     return vsanService.getVsanUpgradeSystem();
/*     */   }
/*     */   
/*     */   public static VsanVcClusterHealthSystem getVsanVcClusterHealthSystem(ManagedObjectReference moRef) {
/* 200 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 201 */     if (vsanService == null) {
/* 202 */       return null;
/*     */     }
/* 204 */     return vsanService.getVsanVcClusterHealthSystem();
/*     */   }
/*     */   
/*     */   public static VsanUpgradeSystemEx getVsanUpgradeSystemEx(ManagedObjectReference moRef) {
/* 208 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 209 */     if (vsanService == null) {
/* 210 */       return null;
/*     */     }
/* 212 */     return vsanService.getVsanUpgradeSystemEx();
/*     */   }
/*     */   
/*     */   public static VsanObjectSystem getVsanObjectSystem(ManagedObjectReference moRef) {
/* 216 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 217 */     if (vsanService == null) {
/* 218 */       return null;
/*     */     }
/* 220 */     return vsanService.getVsanObjectSystem();
/*     */   }
/*     */   
/*     */   public static VsanIscsiTargetSystem getVsanIscsiSystem(ManagedObjectReference moRef) {
/* 224 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 225 */     if (vsanService == null) {
/* 226 */       return null;
/*     */     }
/* 228 */     return vsanService.getVsanIscsiSystem();
/*     */   }
/*     */   
/*     */   public static VsanSpaceReportSystem getVsanSpaceReportSystem(ManagedObjectReference moRef) {
/* 232 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 233 */     if (vsanService == null) {
/* 234 */       return null;
/*     */     }
/* 236 */     return vsanService.getVsanSpaceReportSystem();
/*     */   }
/*     */   
/*     */   public static VsanCapabilitySystem getVsanCapabilitySystem(ManagedObjectReference moRef) {
/* 240 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 241 */     if (vsanService == null) {
/* 242 */       return null;
/*     */     }
/* 244 */     return vsanService.getVsanCapabilitySystem();
/*     */   }
/*     */   
/*     */   public static VsanSystemEx getVsanSystemEx(ManagedObjectReference moRef) {
/* 248 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 249 */     if (vsanService == null) {
/* 250 */       return null;
/*     */     }
/* 252 */     return vsanService.getVsanSystemEx(moRef);
/*     */   }
/*     */   
/*     */   public static VsanSystem getVsanSystem(ManagedObjectReference moRef) {
/* 256 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 257 */     if (vsanService == null) {
/* 258 */       return null;
/*     */     }
/* 260 */     return vsanService.getVsanSystem(moRef);
/*     */   }
/*     */ 
/*     */   
/*     */   public static VsanUpgradeSystem getVsanLegacyUpgradeSystem(ManagedObjectReference moRef) {
/* 265 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 266 */     if (vsanService == null) {
/* 267 */       return null;
/*     */     }
/* 269 */     return vsanService.getVsanLegacyUpgradeSystem();
/*     */   }
/*     */   
/*     */   public static VsanUpdateManager getUpdateManager(ManagedObjectReference moRef) {
/* 273 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 274 */     VsanUpdateManager updateManager = vsanService.getUpdateManager();
/* 275 */     return updateManager;
/*     */   }
/*     */   
/*     */   public static VsanVdsSystem getVdsSystem(ManagedObjectReference moRef) {
/* 279 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 280 */     VsanVdsSystem vdsSystem = vsanService.getVdsSystem();
/* 281 */     return vdsSystem;
/*     */   }
/*     */   
/*     */   public static VsanVcPrecheckerSystem getVsanPrecheckerSystem(ManagedObjectReference moRef) {
/* 285 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 286 */     if (vsanService != null) {
/* 287 */       return vsanService.getVsanPreCheckerSystem();
/*     */     }
/* 289 */     return null;
/*     */   }
/*     */   
/*     */   public static VsanPhoneHomeSystem getVsanPhoneHomeSystem(ManagedObjectReference moRef) {
/* 293 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 294 */     if (vsanService != null) {
/* 295 */       return vsanService.getPhoneHomeSystem();
/*     */     }
/* 297 */     return null;
/*     */   }
/*     */   
/*     */   public static VsanClusterMgmtInternalSystem getVsanClusterMgmtInternalSystem(ManagedObjectReference moRef) {
/* 301 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 302 */     if (vsanService != null) {
/* 303 */       return vsanService.getVsanClusterMgmtInternalSystem();
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */   
/*     */   public static VsanVumSystem getVsanVumSystem(ManagedObjectReference moRef) {
/* 309 */     VsanService vsanService = _vsanServiceFactory.getService(moRef.getServerGuid());
/* 310 */     if (vsanService != null) {
/* 311 */       return vsanService.getVsanVumSystem();
/*     */     }
/* 313 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/VsanProviderUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */