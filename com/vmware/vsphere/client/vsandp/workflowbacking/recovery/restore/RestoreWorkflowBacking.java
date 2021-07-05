/*     */ package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.Datacenter;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.Network;
/*     */ import com.vmware.vim.binding.vim.ResourcePool;
/*     */ import com.vmware.vim.binding.vim.dvs.DistributedVirtualPortgroup;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.services.common.PermissionService;
/*     */ import com.vmware.vsan.client.services.common.data.ConnectionState;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.data.StoragePolicyData;
/*     */ import com.vmware.vsphere.client.vsan.base.impl.PbmDataProvider;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.PitProvider;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.DpClient;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*     */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.VmInventoryModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class RestoreWorkflowBacking
/*     */ {
/*     */   private static final String NAME_PROPERTY = "name";
/*     */   private static final String ICON_PROPERTY = "primaryIconId";
/*     */   @Autowired
/*     */   private PitProvider pitProvider;
/*     */   @Autowired
/*     */   private VsanDpInventoryHelper inventoryHelper;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   @Autowired
/*     */   private DpClient dpClient;
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   @Autowired
/*     */   private PbmDataProvider pbmDataProvider;
/*     */   @Autowired
/*     */   private PermissionService permissionService;
/*     */   
/*     */   @TsService
/*     */   public String getSourceVmName(ManagedObjectReference vmRef) {
/*  69 */     Exception exception1 = null, exception2 = null; try {  }
/*     */     finally
/*  71 */     { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */   
/*     */   } @TsService
/*     */   public ArrayList<VmProtectionInstance> getSyncPoints(ManagedObjectReference vmRef) throws Exception {
/*  76 */     ArrayList<VmProtectionInstance> result = new ArrayList<>();
/*  77 */     TreeSet<VmProtectionInstance> localPits = this.pitProvider.getLocalPits(vmRef);
/*  78 */     result.addAll(localPits);
/*     */     
/*  80 */     TreeSet<VmProtectionInstance> archivePits = this.pitProvider.getArchivePits(vmRef);
/*  81 */     if (archivePits != null) {
/*  82 */       result.addAll(archivePits);
/*     */     }
/*     */     
/*  85 */     Collections.sort(result, Collections.reverseOrder());
/*     */     
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VmInventoryModel getSourceVmInventory(ManagedObjectReference vmRef) throws Exception {
/*  92 */     VmInventoryModel inventoryModel = new VmInventoryModel();
/*  93 */     List<ManagedObjectReference> inventories = new ArrayList<>();
/*  94 */     ManagedObjectReference sourceVmFolder = this.inventoryHelper.getVmFolder(vmRef);
/*  95 */     if (sourceVmFolder != null) {
/*  96 */       inventories.add(sourceVmFolder);
/*     */     }
/*     */     
/*  99 */     ManagedObjectReference sourceVmResourcePool = this.inventoryHelper.getVmResourcePool(vmRef);
/* 100 */     if (sourceVmResourcePool != null) {
/* 101 */       inventories.add(sourceVmResourcePool);
/*     */     }
/*     */     
/* 104 */     ManagedObjectReference sourceVmNetwork = this.inventoryHelper.getVmNetwork(vmRef);
/* 105 */     if (sourceVmNetwork != null) {
/* 106 */       inventories.add(sourceVmNetwork);
/*     */     } else {
/* 108 */       inventoryModel.network = null;
/*     */     } 
/*     */     
/* 111 */     ManagedObjectReference[] objects = VmodlHelper.assignServerGuid(
/* 112 */         inventories.<ManagedObjectReference>toArray(new ManagedObjectReference[0]), vmRef.getServerGuid());
/*     */     
/* 114 */     PropertyValue[] values = QueryUtil.getProperties((Object[])objects, new String[] { "name", "primaryIconId"
/* 115 */         }).getPropertyValues(); byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/* 117 */     for (i = (arrayOfPropertyValue1 = values).length, b = 0; b < i; ) { PropertyValue property = arrayOfPropertyValue1[b];
/* 118 */       if (property.propertyName.equals("name")) {
/* 119 */         ManagedObjectReference nodeRef = (ManagedObjectReference)property.resourceObject;
/* 120 */         if (isFolder(nodeRef)) {
/* 121 */           inventoryModel.folder.name = (String)property.value;
/* 122 */           inventoryModel.folder.ref = nodeRef;
/* 123 */         } else if (isCompute(nodeRef)) {
/* 124 */           inventoryModel.compute.name = (String)property.value;
/* 125 */           inventoryModel.compute.ref = nodeRef;
/* 126 */         } else if (isNetwork(nodeRef)) {
/* 127 */           inventoryModel.network.name = (String)property.value;
/* 128 */           inventoryModel.network.ref = nodeRef;
/*     */         } 
/* 130 */       } else if (property.propertyName.equals("primaryIconId")) {
/* 131 */         ManagedObjectReference nodeRef = (ManagedObjectReference)property.resourceObject;
/* 132 */         if (isFolder(nodeRef)) {
/* 133 */           inventoryModel.folder.iconId = (String)property.value;
/* 134 */         } else if (isCompute(nodeRef)) {
/* 135 */           inventoryModel.compute.iconId = (String)property.value;
/* 136 */         } else if (isNetwork(nodeRef)) {
/* 137 */           inventoryModel.network.iconId = (String)property.value;
/*     */         } 
/*     */       }  b++; }
/*     */     
/* 141 */     inventoryModel.rootDc = this.inventoryHelper.getVmDc(vmRef);
/* 142 */     inventoryModel.rootVsanCluster = this.inventoryHelper.getVmCluster(vmRef);
/* 143 */     return inventoryModel;
/*     */   }
/*     */   
/*     */   private boolean isFolder(ManagedObjectReference moRef) {
/* 147 */     if (this.vmodlHelper.isOfType(moRef, Datacenter.class) || 
/* 148 */       this.vmodlHelper.isVmFolder(moRef)) {
/* 149 */       return true;
/*     */     }
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isCompute(ManagedObjectReference moRef) {
/* 155 */     if (this.vmodlHelper.isOfType(moRef, ClusterComputeResource.class) || 
/* 156 */       this.vmodlHelper.isOfType(moRef, HostSystem.class) || 
/* 157 */       this.vmodlHelper.isOfType(moRef, ResourcePool.class) || 
/* 158 */       this.vmodlHelper.isHostFolder(moRef)) {
/* 159 */       return true;
/*     */     }
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isNetwork(ManagedObjectReference moRef) {
/* 165 */     if (this.vmodlHelper.isOfType(moRef, Network.class) || 
/* 166 */       this.vmodlHelper.isOfType(moRef, DistributedVirtualPortgroup.class)) {
/* 167 */       return true;
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public List<StoragePolicyData> getProtectedVmStoragePolicies(ManagedObjectReference vmRef) throws Exception {
/* 174 */     ClassLoader classLoader = RestoreWorkflowBacking.class.getClassLoader();
/* 175 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 177 */       Thread.currentThread().setContextClassLoader(classLoader);
/* 178 */       return this.pbmDataProvider.getObjectCompatibleStoragePolicies(this.inventoryHelper.getVmCluster(vmRef));
/*     */     } finally {
/* 180 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public String getValidatePermissions(ManagedObjectReference vmRef, VmInventoryModel targetInventory) throws Exception {
/* 189 */     if (!isFolderPermissionValid(vmRef, targetInventory)) {
/* 190 */       return Utils.getLocalizedString("vsan.restore.validation.permission.folder");
/*     */     }
/* 192 */     if (!isComputePermissionValid(vmRef, targetInventory)) {
/* 193 */       return Utils.getLocalizedString("vsan.restore.validation.permission.compute");
/*     */     }
/* 195 */     if (!isNetworkPermissionValid(vmRef, targetInventory)) {
/* 196 */       return Utils.getLocalizedString("vsan.restore.validation.permission.network");
/*     */     }
/*     */     
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isFolderPermissionValid(ManagedObjectReference vmRef, VmInventoryModel targetInventory) throws Exception {
/*     */     ManagedObjectReference folderToCheck;
/* 205 */     if (targetInventory.folderSameAsSource)
/* 206 */     { folderToCheck = this.inventoryHelper.getVmFolder(vmRef); }
/* 207 */     else { if (targetInventory.folder == null) {
/* 208 */         return true;
/*     */       }
/* 210 */       folderToCheck = targetInventory.folder.ref; }
/*     */     
/* 212 */     return this.permissionService.hasPermissions(folderToCheck, new String[] { "Vsan.DataProtection.Management" });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isComputePermissionValid(ManagedObjectReference vmRef, VmInventoryModel targetInventory) throws Exception {
/*     */     ManagedObjectReference computeToCheck;
/* 218 */     if (targetInventory.computeSameAsSource)
/* 219 */     { computeToCheck = this.inventoryHelper.getVmResourcePool(vmRef); }
/* 220 */     else { if (targetInventory.compute == null) {
/* 221 */         return true;
/*     */       }
/* 223 */       computeToCheck = targetInventory.compute.ref; }
/*     */     
/* 225 */     return this.permissionService.hasPermissions(computeToCheck, new String[] { "Vsan.DataProtection.Management" });
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isNetworkPermissionValid(ManagedObjectReference vmRef, VmInventoryModel targetInventory) throws Exception {
/*     */     ManagedObjectReference networkToCkeck;
/* 231 */     if (targetInventory.networkSameAsSource)
/* 232 */     { networkToCkeck = this.inventoryHelper.getVmNetwork(vmRef); }
/* 233 */     else { if (targetInventory.network == null) {
/* 234 */         return true;
/*     */       }
/* 236 */       networkToCkeck = targetInventory.network.ref; }
/*     */     
/* 238 */     return this.permissionService.hasPermissions(networkToCkeck, new String[] { "Vsan.DataProtection.Management" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<ManagedObjectReference> restore(ManagedObjectReference vmRef, VmProtectionInstance selectedSyncPoint, Boolean powerOn, Boolean createIndependentVm, ManagedObjectReference selectedVmFolder, String storagePolicyId, String vmName, ManagedObjectReference selectedNetwork, ManagedObjectReference selectedResourcePool, ManagedObjectReference sourceCluster) throws Exception {
/* 246 */     Exception exception1 = null, exception2 = null;
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
/* 296 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   public boolean checkHostConnectionState(ManagedObjectReference[] sourceComputeRefs) throws Exception {
/* 301 */     List<ManagedObjectReference> hostRefs = new ArrayList<>(); byte b; int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 303 */     for (i = (arrayOfManagedObjectReference = sourceComputeRefs).length, b = 0; b < i; ) { ManagedObjectReference computeRef = arrayOfManagedObjectReference[b];
/* 304 */       if (computeRef.getType().equals(HostSystem.class.getSimpleName())) {
/* 305 */         hostRefs.add(computeRef);
/*     */       }
/*     */       b++; }
/*     */     
/* 309 */     if (hostRefs.size() == 0) {
/* 310 */       return true;
/*     */     }
/*     */     
/* 313 */     DataServiceResponse result = QueryUtil.getProperties(
/* 314 */         hostRefs, new String[] { "runtime.connectionState", 
/* 315 */           "runtime.inMaintenanceMode", 
/* 316 */           "runtime.inQuarantineMode" });
/* 317 */     Map<Object, Map<String, Object>> mappedProperties = result.getMap();
/* 318 */     for (Map<String, Object> properties : mappedProperties.values()) {
/* 319 */       HostSystem.ConnectionState connectionState = (HostSystem.ConnectionState)properties.get("runtime.connectionState");
/* 320 */       if (!ConnectionState.fromHostState(connectionState).equals(ConnectionState.connected)) {
/* 321 */         return false;
/*     */       }
/* 323 */       boolean isInMaintenanceMode = ((Boolean)properties.get("runtime.inMaintenanceMode")).booleanValue();
/* 324 */       if (isInMaintenanceMode) {
/* 325 */         return false;
/*     */       }
/* 327 */       boolean isQuarantineMode = ((Boolean)properties.get("runtime.inQuarantineMode")).booleanValue();
/* 328 */       if (isQuarantineMode) {
/* 329 */         return false;
/*     */       }
/*     */     } 
/* 332 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/RestoreWorkflowBacking.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */