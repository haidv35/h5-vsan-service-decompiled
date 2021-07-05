/*     */ package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.RuntimeFault;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.CgInfo;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.GroupInstanceData;
/*     */ import com.vmware.vim.vsandp.binding.vim.vsandp.cluster.ProtectionService;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.MessageBundle;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.PitProvider;
/*     */ import com.vmware.vsphere.client.vsandp.data.ProtectionType;
/*     */ import com.vmware.vsphere.client.vsandp.dataproviders.vm.VmConsistencyGroupPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*     */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.GetClosestSyncPointsSpec;
/*     */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.MultiRestoreVmSpec;
/*     */ import com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model.VmInventoryModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class MultiVmRestoreBacking
/*     */ {
/*  36 */   private static final Logger logger = LoggerFactory.getLogger(MultiVmRestoreBacking.class);
/*     */   
/*     */   @Autowired
/*     */   private RestoreWorkflowBacking commonBacking;
/*     */   
/*     */   @Autowired
/*     */   private VmConsistencyGroupPropertyProvider cgProvider;
/*     */   
/*     */   @Autowired
/*     */   private PitProvider pitProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanDpInventoryHelper inventoryHelper;
/*     */   
/*     */   @Autowired
/*     */   private MessageBundle messages;
/*     */   
/*     */   @TsService
/*     */   public String validateTargetVms(ManagedObjectReference[] vmRefs) throws Exception {
/*  55 */     if (vmRefs.length > 1 && !validateMultipleVirtualMachines(vmRefs)) {
/*  56 */       return Utils.getLocalizedString("vsan.restore.validation.vmsFromDifferentClusters");
/*     */     }
/*     */     
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   private boolean validateMultipleVirtualMachines(ManagedObjectReference[] targetObjects) throws Exception {
/*  63 */     boolean result = true;
/*     */ 
/*     */     
/*  66 */     PropertyValue[] values = QueryUtil.getProperties((Object[])targetObjects, new String[] { "cluster" }).getPropertyValues();
/*  67 */     ManagedObjectReference vmCluster = null; byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  68 */     for (i = (arrayOfPropertyValue1 = values).length, b = 0; b < i; ) { PropertyValue item = arrayOfPropertyValue1[b];
/*  69 */       if (vmCluster == null) {
/*  70 */         vmCluster = (ManagedObjectReference)item.value;
/*  71 */       } else if (!vmCluster.equals(item.value)) {
/*     */         
/*  73 */         result = false;
/*     */         break;
/*     */       } 
/*     */       b++; }
/*     */     
/*  78 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<VmProtectionInstance> getClosestSyncPointsToDate(ManagedObjectReference firstVmRef, GetClosestSyncPointsSpec spec) throws Exception {
/*  85 */     List<VmProtectionInstance> result = new ArrayList<>();
/*     */ 
/*     */     
/*  88 */     ManagedObjectReference sourceCluster = this.inventoryHelper.getVmCluster(firstVmRef);
/*     */ 
/*     */     
/*  91 */     if (!VsanCapabilityUtils.isArchiveDataProtectionSupported(sourceCluster)) {
/*  92 */       spec.restoreOnlyFromLocal = true;
/*     */     }
/*     */     
/*  95 */     Map<ManagedObjectReference, VmData> vmDataMap = new HashMap<>();
/*  96 */     collectLocalData(spec.vmRefs, sourceCluster, vmDataMap);
/*     */ 
/*     */     
/*  99 */     if (!spec.restoreOnlyFromLocal)
/* 100 */       collectArchiveData(spec.vmRefs, sourceCluster, vmDataMap); 
/*     */     byte b;
/*     */     int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 104 */     for (i = (arrayOfManagedObjectReference = spec.vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 105 */       result.add(findClosestPit(spec.restoreOnlyFromQuiesced, spec.targetTime, vmDataMap.get(vmRef)));
/*     */       b++; }
/*     */     
/* 108 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public String getValidateTargetInventory(ManagedObjectReference[] vmRefs, VmInventoryModel targetInventory) throws Exception {
/*     */     byte b;
/*     */     int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 118 */     for (i = (arrayOfManagedObjectReference = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 119 */       String result = this.commonBacking.getValidatePermissions(vmRef, targetInventory);
/* 120 */       if (result != null)
/*     */       {
/* 122 */         return result;
/*     */       }
/*     */       
/*     */       b++; }
/*     */     
/* 127 */     if (targetInventory.computeSameAsSource) {
/*     */       
/* 129 */       List<ManagedObjectReference> sourceComputeRefs = new ArrayList<>(); ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 130 */       for (int j = (arrayOfManagedObjectReference1 = vmRefs).length; i < j; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference1[i];
/* 131 */         sourceComputeRefs.add(this.inventoryHelper.getVmResourcePool(vmRef)); i++; }
/*     */       
/* 133 */       if (!this.commonBacking.checkHostConnectionState(sourceComputeRefs.<ManagedObjectReference>toArray(new ManagedObjectReference[0]))) {
/* 134 */         return Utils.getLocalizedString("vsan.restore.validation.compute.sameassource.error");
/*     */       }
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<ManagedObjectReference> getMultiRestoreVm(ManagedObjectReference[] vmRefs, MultiRestoreVmSpec spec) throws Exception {
/* 148 */     ManagedObjectReference clusterRef = this.inventoryHelper.getVmCluster(vmRefs[0]);
/*     */     
/* 150 */     List<ManagedObjectReference> resultTasks = new ArrayList<>();
/* 151 */     for (int i = 0; i < vmRefs.length; i++) {
/* 152 */       ManagedObjectReference vmReference = vmRefs[i];
/* 153 */       VmProtectionInstance instance = spec.selectedSyncPoints[i];
/* 154 */       String vmName = spec.vmName[i];
/*     */       
/* 156 */       ManagedObjectReference selectedNetwork = spec.selectedNetwork;
/* 157 */       if (spec.keepNetworkAsSource)
/*     */       {
/* 159 */         selectedNetwork = this.inventoryHelper.getVmNetwork(vmReference);
/*     */       }
/*     */       
/* 162 */       ManagedObjectReference selectedVmFolder = spec.selectedVmFolder;
/* 163 */       if (spec.keepFolderAsSource) {
/* 164 */         selectedVmFolder = this.inventoryHelper.getVmFolder(vmReference);
/*     */       }
/*     */       
/* 167 */       ManagedObjectReference vmFolder = this.inventoryHelper.getVmFolderOfDataCenter(selectedVmFolder);
/*     */       
/* 169 */       ManagedObjectReference selectedCompute = spec.selectedResourcePool;
/* 170 */       if (spec.keepComputeAsSource) {
/* 171 */         selectedCompute = this.inventoryHelper.getVmResourcePool(vmReference);
/*     */       }
/*     */       
/* 174 */       ManagedObjectReference taskRef = (ManagedObjectReference)this.commonBacking.restore(vmReference, instance, 
/* 175 */           Boolean.valueOf(spec.powerOn), Boolean.valueOf(spec.createIndependentVm), vmFolder, spec.storagePolicyId, vmName, selectedNetwork, 
/* 176 */           selectedCompute, clusterRef).get();
/*     */       
/* 178 */       ManagedObjectReference resultTask = new ManagedObjectReference(taskRef.getType(), taskRef.getValue(), 
/* 179 */           vmReference.getServerGuid());
/* 180 */       resultTasks.add(resultTask);
/*     */     } 
/*     */     
/* 183 */     return resultTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void collectLocalData(ManagedObjectReference[] vmRefs, ManagedObjectReference sourceCluster, Map<ManagedObjectReference, VmData> vmDataMap) throws Exception {
/* 190 */     Map<ManagedObjectReference, Future<ProtectionService.CgInfoQuery>> cgInfoQueryFutures = new HashMap<>(); byte b;
/*     */     int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 193 */     for (i = (arrayOfManagedObjectReference = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 194 */       cgInfoQueryFutures.put(vmRef, this.cgProvider.getCgInfo(vmRef, sourceCluster));
/*     */       
/*     */       b++; }
/*     */     
/* 198 */     for (i = (arrayOfManagedObjectReference = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 199 */       ProtectionService.CgInfoQuery query = (ProtectionService.CgInfoQuery)((Future)cgInfoQueryFutures.get(vmRef)).get();
/* 200 */       if (query.getResult() == null || (query.getResult()).length < 1) {
/* 201 */         logger.error("Incorrect result was received when CgInfo was queried: {}", query);
/* 202 */         throw new RuntimeFault(this.messages.string("dataproviders.vm.cg.cgInfoQueryFault"));
/*     */       } 
/*     */       
/* 205 */       CgInfo cgInfo = query.getResult()[0];
/* 206 */       vmDataMap.put(vmRef, new VmData(cgInfo));
/*     */       b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void collectArchiveData(ManagedObjectReference[] vmRefs, ManagedObjectReference sourceCluster, Map<ManagedObjectReference, VmData> vmDataMap) throws Exception {
/* 214 */     Map<ManagedObjectReference, Future<ProtectionService.InstanceQuery>> archiveFutures = new HashMap<>(); byte b;
/*     */     int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 217 */     for (i = (arrayOfManagedObjectReference = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 218 */       VmData vmData = vmDataMap.get(vmRef);
/* 219 */       archiveFutures.put(vmRef, this.cgProvider.getArchivalSeriesAsync(sourceCluster, vmData.cgInfo.getKey()));
/*     */       
/*     */       b++; }
/*     */     
/* 223 */     for (i = (arrayOfManagedObjectReference = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference[b];
/* 224 */       ProtectionService.InstanceQuery query = (ProtectionService.InstanceQuery)((Future)archiveFutures.get(vmRef)).get();
/* 225 */       if (query.getResult() == null || (query.getResult()).length < 1) {
/* 226 */         logger.error("Incorrect result was received when archival pits were queried: {}", query);
/* 227 */         throw new Exception(this.messages.string("dataproviders.vm.queryArchivalInstancesFault"));
/*     */       } 
/*     */       
/* 230 */       ProtectionService.InstanceQuery.Result.SeriesEntry[] allSeries = query.getResult()[0].getSeries();
/* 231 */       if (allSeries != null) {
/* 232 */         VmData vmData = vmDataMap.get(vmRef); byte b1; int j; ProtectionService.InstanceQuery.Result.SeriesEntry[] arrayOfSeriesEntry;
/* 233 */         for (j = (arrayOfSeriesEntry = allSeries).length, b1 = 0; b1 < j; ) { ProtectionService.InstanceQuery.Result.SeriesEntry series = arrayOfSeriesEntry[b1];
/* 234 */           vmData.archivePits.addAll(Arrays.asList(series.getInstance()));
/*     */           b1++; }
/*     */       
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   } private VmProtectionInstance findClosestPit(boolean useOnlyQuiesced, Long targetTime, VmData vmData) {
/* 241 */     List<GroupInstanceData> pitsData = vmData.getAllPits();
/*     */     
/* 243 */     GroupInstanceData closestInstance = null;
/* 244 */     if (targetTime == null) {
/*     */       
/* 246 */       for (GroupInstanceData instance : pitsData) {
/* 247 */         if (useOnlyQuiesced && instance.getQuiescedType().equalsIgnoreCase(
/* 248 */             VmProtectionInstance.QuiescingType.NONE.toString())) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 253 */         long syncTime = instance.getSnapshotTimestamp().getTime().getTime();
/* 254 */         if (closestInstance == null || syncTime > closestInstance.getSnapshotTimestamp().getTime().getTime()) {
/* 255 */           closestInstance = instance;
/*     */         }
/*     */       } 
/*     */     } else {
/* 259 */       for (GroupInstanceData instance : pitsData) {
/* 260 */         if (useOnlyQuiesced && instance.getQuiescedType().equalsIgnoreCase(
/* 261 */             VmProtectionInstance.QuiescingType.NONE.toString())) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 266 */         long syncTime = instance.getSnapshotTimestamp().getTime().getTime();
/* 267 */         if (closestInstance != null) {
/*     */           
/* 269 */           if (syncTime > closestInstance.getSnapshotTimestamp().getTime().getTime() && syncTime <= targetTime.longValue())
/* 270 */             closestInstance = instance;  continue;
/*     */         } 
/* 272 */         if (syncTime <= targetTime.longValue()) {
/* 273 */           closestInstance = instance;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 278 */     if (closestInstance == null)
/*     */     {
/* 280 */       return null;
/*     */     }
/*     */     
/* 283 */     if (vmData.isLocal(closestInstance)) {
/* 284 */       return this.pitProvider.createProtectionInstance(vmData.getLocalSeries(), ProtectionType.LOCAL, closestInstance);
/*     */     }
/* 286 */     return this.pitProvider.createProtectionInstance(vmData.getArchiveSeries(), ProtectionType.ARCHIVE, 
/* 287 */         closestInstance);
/*     */   }
/*     */   
/*     */   private class VmData
/*     */   {
/*     */     CgInfo cgInfo;
/*     */     List<GroupInstanceData> localPits;
/*     */     List<GroupInstanceData> archivePits;
/*     */     
/*     */     VmData(CgInfo cgInfo) {
/* 297 */       this.cgInfo = cgInfo;
/*     */       
/* 299 */       GroupInstanceData[] instances = cgInfo.getLocal().getInstance();
/* 300 */       if (instances == null) {
/* 301 */         this.localPits = new ArrayList<>();
/*     */       } else {
/* 303 */         this.localPits = Arrays.asList(cgInfo.getLocal().getInstance());
/*     */       } 
/*     */       
/* 306 */       this.archivePits = new ArrayList<>();
/*     */     }
/*     */     
/*     */     String getLocalSeries() {
/* 310 */       return (this.cgInfo.getLocal()).series.key;
/*     */     }
/*     */     
/*     */     String getArchiveSeries() {
/* 314 */       return (this.cgInfo.getArchive()[0]).series.key;
/*     */     }
/*     */     
/*     */     boolean isLocal(GroupInstanceData pit) {
/* 318 */       return (pit != null && this.localPits.contains(pit));
/*     */     }
/*     */     
/*     */     public boolean isArchive(GroupInstanceData pit) {
/* 322 */       return (pit != null && this.archivePits.contains(pit));
/*     */     }
/*     */     
/*     */     List<GroupInstanceData> getAllPits() {
/* 326 */       List<GroupInstanceData> newList = new ArrayList<>(this.localPits);
/* 327 */       newList.addAll(this.archivePits);
/* 328 */       return newList;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/MultiVmRestoreBacking.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */