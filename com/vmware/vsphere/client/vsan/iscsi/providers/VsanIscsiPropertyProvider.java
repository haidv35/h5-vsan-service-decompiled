/*     */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.VirtualNic;
/*     */ import com.vmware.vim.binding.vim.vm.DatastoreInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectInformation;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.query.Comparator;
/*     */ import com.vmware.vise.data.query.Conjoiner;
/*     */ import com.vmware.vise.data.query.PropertyConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.util.Version;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.cluster.VsanDatastoreHostData;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiConfig;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.config.VsanIscsiTargetConfig;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanIscsiPropertyProvider
/*     */ {
/*  48 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanIscsiPropertyProvider.class);
/*     */   private static final String HOST_KEY = "hostKey";
/*  50 */   private static final Version HOST_VERSION_2015 = new Version("6.0.0");
/*     */   private static final String VNIC_NAME_PROPERTY = "config.network.vnic";
/*     */   private VcClient vcClient;
/*     */   
/*     */   public void setVcClient(VcClient vcClient) {
/*  55 */     this.vcClient = vcClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Boolean getIsIscsiTargetsSupported(ManagedObjectReference clusterRef) throws Exception {
/*  66 */     return Boolean.valueOf(VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef));
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
/*     */   @TsService
/*     */   public VsanIscsiConfig getVsanIscsiConfig(ManagedObjectReference clusterRef) throws Exception {
/*  79 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*  80 */       return null;
/*     */     }
/*     */     
/*  83 */     VsanIscsiConfig vsanIscsiConfig = new VsanIscsiConfig();
/*     */     
/*  85 */     VsanVcClusterConfigSystem vsanConfigSystem = 
/*  86 */       VsanProviderUtils.getVsanConfigSystem(clusterRef);
/*  87 */     ConfigInfoEx config = null;
/*  88 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("vsanConfigSystem.getConfigInfoEx"); 
/*  89 */       try { config = vsanConfigSystem.getConfigInfoEx(clusterRef); }
/*  90 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
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
/* 102 */     if (config.enabled.booleanValue() && config.defaultConfig != null && 
/* 103 */       vsanIscsiConfig.vsanIscsiTargetServiceConfig.enabled.booleanValue()) {
/* 104 */       VsanIscsiTargetSystem vsanIscsiSystem = 
/* 105 */         VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/* 106 */       VsanObjectInformation vsanObjectInformation = null; try {
/* 107 */         Exception exception4, exception3 = null;
/*     */       }
/* 109 */       catch (Exception exception) {}
/*     */       
/* 111 */       vsanIscsiConfig.vsanObjectInformation = vsanObjectInformation;
/*     */     } 
/*     */     
/* 114 */     return vsanIscsiConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VsanIscsiTargetConfig getVsanIscsiTargetConfig(ManagedObjectReference clusterRef) throws Exception {
/* 122 */     VsanIscsiConfig config = getVsanIscsiConfig(clusterRef);
/* 123 */     if (config == null) {
/* 124 */       return null;
/*     */     }
/*     */     
/* 127 */     Profile spbmProfile = null;
/* 128 */     if (config.vsanObjectInformation != null && config.vsanObjectInformation.vsanObjectUuid != null && 
/* 129 */       config.vsanObjectInformation.spbmProfileUuid != null) {
/* 130 */       ManagedObjectReference vcRootRef = VmodlHelper.getRootFolder(clusterRef.getServerGuid());
/*     */       try {
/* 132 */         PropertyValue[] resultset = QueryUtil.getPropertiesForRelatedObjects(
/* 133 */             vcRootRef, 
/* 134 */             "pbmProfiles", 
/* 135 */             "PbmRequirementStorageProfile", 
/* 136 */             new String[] { "profileContent" }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 137 */         for (i = (arrayOfPropertyValue1 = resultset).length, b = 0; b < i; ) { PropertyValue profileContent = arrayOfPropertyValue1[b];
/* 138 */           Profile profile = (Profile)profileContent.value;
/*     */           
/* 140 */           if (profile.profileId.uniqueId.equals(config.vsanObjectInformation.spbmProfileUuid)) {
/* 141 */             spbmProfile = profile; break;
/*     */           } 
/*     */           b++; }
/*     */       
/* 145 */       } catch (Exception ex) {
/* 146 */         throw Utils.getMethodFault(ex);
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     VsanDatastoreHostData datastoreHostData = getVsanDatastoreHostData(clusterRef);
/* 151 */     VsanIscsiTargetConfig targetConfig = new VsanIscsiTargetConfig(
/* 152 */         config, 
/* 153 */         datastoreHostData, 
/* 154 */         VsanCapabilityUtils.isIscsiTargetsSupportedOnCluster(clusterRef), 
/* 155 */         getIsHostsVersionValid(clusterRef).booleanValue(), 
/* 156 */         spbmProfile);
/* 157 */     return targetConfig;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean getIsVsanIscsiEnabledOnHost(ManagedObjectReference hostRef) throws Exception {
/* 162 */     Boolean result = Boolean.valueOf(false);
/*     */     
/* 164 */     PropertyValue[] values = QueryUtil.getPropertyForRelatedObjects(
/* 165 */         hostRef, 
/* 166 */         "parent", 
/* 167 */         "ClusterComputeResource", 
/* 168 */         "isVsanIscsiEnabled").getPropertyValues();
/*     */     
/* 170 */     if (values.length > 0) {
/* 171 */       result = (Boolean)(values[0]).value;
/*     */     }
/*     */     
/* 174 */     return result.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getIsVsanIscsiEnabled(ManagedObjectReference clusterRef) throws Exception {
/* 180 */     VsanIscsiConfig config = getVsanIscsiConfig(clusterRef);
/* 181 */     return config.vsanIscsiTargetServiceConfig.enabled.booleanValue();
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
/*     */   @TsService
/*     */   public VsanDatastoreHostData getVsanDatastoreHostData(ManagedObjectReference clusterRef) throws Exception {
/* 197 */     DatastoreInfo[] vsanDatastoresByCluster = 
/* 198 */       getVsanDatastoresByCluster(clusterRef); DatastoreInfo[] arrayOfDatastoreInfo1;
/* 199 */     if (vsanDatastoresByCluster != null && (
/* 200 */       arrayOfDatastoreInfo1 = vsanDatastoresByCluster).length != 0) { DatastoreInfo datastoreInfo = arrayOfDatastoreInfo1[0];
/* 201 */       VsanDatastoreHostData data = new VsanDatastoreHostData();
/* 202 */       data.vsanDatastoreRef = datastoreInfo.datastore.datastore;
/* 203 */       data.hostRef = getConnectedHosts(data.vsanDatastoreRef);
/* 204 */       return data; }
/*     */ 
/*     */ 
/*     */     
/* 208 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public DatastoreInfo[] getVsanDatastoresByCluster(ManagedObjectReference clusterRef) throws Exception {
/* 214 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public Map getHostSystemObjects(ManagedObjectReference clusterRef) throws Exception {
/* 255 */     PropertyValue[] props = QueryUtil.getPropertiesForRelatedObjects(
/* 256 */         clusterRef, "host", 
/* 257 */         HostSystem.class.getSimpleName(), 
/* 258 */         new String[] { "name" }).getPropertyValues();
/* 259 */     if (ArrayUtils.isNotEmpty((Object[])props)) {
/* 260 */       Map<String, String> hostMap = new HashMap<>(); byte b; int i; PropertyValue[] arrayOfPropertyValue;
/* 261 */       for (i = (arrayOfPropertyValue = props).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b];
/* 262 */         hostMap.put(((ManagedObjectReference)propValue.resourceObject).getValue(), 
/* 263 */             (String)propValue.value); b++; }
/*     */       
/* 265 */       return hostMap;
/*     */     } 
/* 267 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference[] getClusterConnectedHosts(ManagedObjectReference clusterRef) throws Exception {
/* 273 */     if (clusterRef == null) {
/* 274 */       return null;
/*     */     }
/*     */     
/* 277 */     ResultSet resultSet = queryConnectedHosts(clusterRef, "host");
/*     */     
/* 279 */     if (resultSet == null || resultSet.items == null) {
/* 280 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 284 */     List<ManagedObjectReference> list = new ArrayList<>(); byte b; int i; ResultItem[] arrayOfResultItem;
/* 285 */     for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem resultItem = arrayOfResultItem[b];
/* 286 */       if (resultItem.properties != null && resultItem.properties.length != 0) {
/*     */ 
/*     */         
/* 289 */         ManagedObjectReference connectedHostRef = 
/* 290 */           (ManagedObjectReference)resultItem.resourceObject;
/* 291 */         list.add(connectedHostRef);
/*     */       }  b++; }
/* 293 */      ManagedObjectReference[] hosts = new ManagedObjectReference[list.size()];
/* 294 */     return list.<ManagedObjectReference>toArray(hosts);
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
/*     */   public Boolean getIsHostsVersionValid(ManagedObjectReference clusterRef) throws Exception {
/* 308 */     ManagedObjectReference[] hosts = getClusterConnectedHosts(clusterRef);
/*     */     
/* 310 */     if (ArrayUtils.isNotEmpty((Object[])hosts)) {
/* 311 */       byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference; for (i = (arrayOfManagedObjectReference = hosts).length, b = 0; b < i; ) { ManagedObjectReference hostRef = arrayOfManagedObjectReference[b];
/* 312 */         boolean iscsiSupportedOnHost = 
/* 313 */           VsanCapabilityUtils.isIscsiTargetsSupportedOnHost(hostRef);
/* 314 */         if (!iscsiSupportedOnHost) {
/* 315 */           return Boolean.valueOf(iscsiSupportedOnHost);
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } else {
/* 320 */       return Boolean.valueOf(false);
/*     */     } 
/*     */     
/* 323 */     return Boolean.valueOf(true);
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
/*     */   @TsService
/*     */   public String[] getHostsCommonVnicList(ManagedObjectReference clusterRef) throws Exception {
/* 336 */     Constraint hostsForCluster = 
/* 337 */       QueryUtil.createConstraintForRelationship(clusterRef, 
/* 338 */         "host", HostSystem.class.getSimpleName());
/*     */     
/* 340 */     QuerySpec qSpec = 
/* 341 */       QueryUtil.buildQuerySpec(hostsForCluster, new String[] {
/* 342 */           "name", "config.network.vnic"
/*     */         });
/* 344 */     ResultSet resultSet = QueryUtil.getData(qSpec);
/* 345 */     if (resultSet == null) {
/* 346 */       return null;
/*     */     }
/*     */     
/* 349 */     Set<String> vnicSet = new HashSet<>();
/* 350 */     for (int i = 0; i < resultSet.items.length; i++) {
/* 351 */       ResultItem resultItem = resultSet.items[i];
/*     */       
/* 353 */       if (resultItem.properties != null && resultItem.properties.length > 1) {
/*     */ 
/*     */ 
/*     */         
/* 357 */         Set<String> currentVnicSet = new HashSet<>();
/* 358 */         PropertyValue propertyValue = resultItem.properties[1];
/* 359 */         if (propertyValue != null) {
/* 360 */           Object value = propertyValue.value;
/* 361 */           if (value != null) {
/* 362 */             if (value instanceof VirtualNic) {
/* 363 */               VirtualNic vnic = (VirtualNic)value;
/* 364 */               if (vnic != null && 
/* 365 */                 !StringUtils.isWhitespace(vnic.device)) {
/* 366 */                 currentVnicSet.add(vnic.device);
/*     */               }
/* 368 */             } else if (value instanceof VirtualNic[]) {
/* 369 */               VirtualNic[] vnicArray = (VirtualNic[])value;
/* 370 */               if (vnicArray != null) {
/* 371 */                 byte b; int j; VirtualNic[] arrayOfVirtualNic; for (j = (arrayOfVirtualNic = vnicArray).length, b = 0; b < j; ) { VirtualNic everyVnic = arrayOfVirtualNic[b];
/* 372 */                   if (everyVnic != null && 
/* 373 */                     !StringUtils.isWhitespace(everyVnic.device)) {
/* 374 */                     currentVnicSet.add(everyVnic.device);
/*     */                   }
/*     */                   b++; }
/*     */               
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 382 */         if (i == 0) {
/* 383 */           vnicSet.addAll(currentVnicSet);
/*     */         } else {
/* 385 */           vnicSet.retainAll(currentVnicSet);
/*     */         } 
/*     */       } 
/* 388 */     }  return vnicSet.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private ResultSet queryConnectedHosts(ManagedObjectReference mor, String relationShip) throws Exception {
/* 393 */     if (mor == null) {
/* 394 */       return null;
/*     */     }
/* 396 */     Constraint dsHostsConstraint = 
/* 397 */       QueryUtil.createConstraintForRelationship(mor, relationShip, 
/* 398 */         HostSystem.class.getSimpleName());
/*     */     
/* 400 */     PropertyConstraint propertyConstraint = 
/* 401 */       QueryUtil.createPropertyConstraint(
/* 402 */         HostSystem.class.getSimpleName(), 
/* 403 */         "runtime.connectionState", Comparator.EQUALS, 
/* 404 */         HostSystem.ConnectionState.connected.name());
/*     */     
/* 406 */     Constraint dsConnectedHosts = 
/* 407 */       QueryUtil.combineIntoSingleConstraint(new Constraint[] {
/* 408 */           dsHostsConstraint, (Constraint)propertyConstraint }, Conjoiner.AND);
/*     */ 
/*     */     
/* 411 */     QuerySpec qSpec = QueryUtil.buildQuerySpec(dsConnectedHosts, new String[] { "config.product.version" });
/* 412 */     qSpec.name = mor.getValue();
/*     */     
/* 414 */     ResultSet resultSet = QueryUtil.getData(qSpec);
/*     */     
/* 416 */     return resultSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ManagedObjectReference getConnectedHosts(ManagedObjectReference datastore) throws Exception {
/* 426 */     if (datastore == null) {
/* 427 */       return null;
/*     */     }
/*     */     
/* 430 */     ResultSet resultSet = queryConnectedHosts(datastore, "hostKey");
/*     */     
/* 432 */     if (resultSet == null || resultSet.items == null)
/* 433 */       return null; 
/*     */     byte b;
/*     */     int i;
/*     */     ResultItem[] arrayOfResultItem;
/* 437 */     for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem resultItem = arrayOfResultItem[b];
/* 438 */       if (resultItem.properties != null && resultItem.properties.length != 0) {
/*     */ 
/*     */ 
/*     */         
/* 442 */         String version = (String)(resultItem.properties[0]).value;
/* 443 */         Version esxVersion = new Version(version);
/*     */         
/* 445 */         if (esxVersion.compareTo(HOST_VERSION_2015) >= 0) {
/* 446 */           ManagedObjectReference connectedHostRef = 
/* 447 */             (ManagedObjectReference)(resultSet.items[0]).resourceObject;
/*     */           
/* 449 */           return connectedHostRef;
/*     */         } 
/*     */       }  b++; }
/*     */     
/* 453 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */