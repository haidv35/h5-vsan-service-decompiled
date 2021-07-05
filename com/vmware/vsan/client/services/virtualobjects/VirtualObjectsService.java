/*     */ package com.vmware.vsan.client.services.virtualobjects;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.vim.host.VsanInternalSystem;
/*     */ import com.vmware.vim.binding.vim.vm.ConfigInfo;
/*     */ import com.vmware.vim.binding.vim.vm.SnapshotInfo;
/*     */ import com.vmware.vim.binding.vim.vm.SnapshotTree;
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDevice;
/*     */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.MethodFault;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentity;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectIdentityAndHealth;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectInformation;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vsan.client.services.stretchedcluster.ConfigureStretchedClusterService;
/*     */ import com.vmware.vsan.client.services.stretchedcluster.VsanHostsResult;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectModel;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectPlacementModel;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.dataprovider.VsanHostPropertyProviderAdapter;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class VirtualObjectsService {
/*  54 */   private static final Log logger = LogFactory.getLog(VirtualObjectsService.class);
/*     */   
/*     */   private static final int UUID_BATCH_SIZE = 2000;
/*     */   
/*     */   private static final int QUERY_VSAN_OBJECTS_CHUNK_SIZE = 500;
/*     */   
/*     */   private static final String PROP_SNAPSHOT = "snapshot";
/*     */   
/*     */   private static final String PROP_CONFIG = "config";
/*  63 */   private static final String[] PHYSICAL_PLACEMENT_HOST_PROPERTIES = new String[] {
/*  64 */       "name", 
/*  65 */       "primaryIconId", 
/*  66 */       "config.vsanHostConfig.clusterInfo.nodeUuid", 
/*  67 */       "config.vsanHostConfig.faultDomainInfo.name"
/*     */     };
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ConfigureStretchedClusterService stretchedClusterService;
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<VirtualObjectModel> listVirtualObjects(ManagedObjectReference clusterRef) throws Exception {
/*  85 */     Exception exception1 = null, exception2 = null;
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
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 138 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VirtualObjectModel> listVmVirtualObjects(ManagedObjectReference clusterRef, ManagedObjectReference vmRef, Set<String> vmObjectUuids) throws Exception {
/* 149 */     Exception exception1 = null, exception2 = null;
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
/* 163 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<VirtualObjectModel> listVirtualObjectsAsync(ManagedObjectReference clusterRef, Set<String> vsanUuids, VsanObjectIdentityAndHealth identities, Set<ManagedObjectReference> vmRefs, Measure measure) throws Exception {
/*     */     Map<Object, Map<String, Object>> vmProperties;
/*     */     Multimap<ManagedObjectReference, ConfigInfo> multimap;
/* 175 */     List<Future<VsanObjectInformation[]>> objectInfoFutures = new ArrayList<>();
/* 176 */     VsanObjectSystem objectSystem = VsanProviderUtils.getVsanObjectSystem(clusterRef);
/* 177 */     if (!vsanUuids.isEmpty()) {
/* 178 */       List<String> allUuids = new ArrayList<>(vsanUuids);
/* 179 */       int hop = 0;
/* 180 */       int from = 0;
/* 181 */       int to = 0;
/*     */       
/* 183 */       while (to < allUuids.size()) {
/* 184 */         from = hop * 2000;
/* 185 */         to = Math.min((hop + 1) * 2000, allUuids.size());
/* 186 */         Set<String> batch = new HashSet<>(allUuids.subList(from, to));
/*     */         
/* 188 */         Future<VsanObjectInformation[]> future = measure.newFuture("ObjectSystem.queryVsanObjectInformation");
/* 189 */         objectSystem.queryVsanObjectInformation(clusterRef, VsanObjectSystemProvider.buildQuerySpecs(batch), future);
/* 190 */         objectInfoFutures.add(future);
/*     */         
/* 192 */         hop++;
/*     */       } 
/*     */       
/* 195 */       logger.info("Requesting " + allUuids.size() + " UUIDs split into " + objectInfoFutures.size() + " separate calls.");
/*     */     } 
/*     */ 
/*     */     
/* 199 */     HashMultimap hashMultimap = HashMultimap.create();
/* 200 */     if (vmRefs.isEmpty()) {
/* 201 */       vmProperties = Collections.emptyMap();
/*     */     } else {
/*     */       Exception exception2;
/*     */       
/* 205 */       multimap = listVmSnapshots(vmRefs.<ManagedObjectReference>toArray(new ManagedObjectReference[0]), measure);
/*     */ 
/*     */       
/* 208 */       Exception exception1 = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     List<VsanObjectInformation> objInfosList = new ArrayList<>();
/* 217 */     for (Future<VsanObjectInformation[]> future : objectInfoFutures) {
/* 218 */       VsanObjectInformation[] result = (VsanObjectInformation[])future.get();
/* 219 */       if (ArrayUtils.isEmpty((Object[])result)) {
/* 220 */         logger.warn("Found an empty VsanObjectInformation result. Probably something is wrong with the server.");
/*     */         
/*     */         continue;
/*     */       } 
/* 224 */       List<VsanObjectInformation> resultList = Arrays.asList(result);
/* 225 */       objInfosList.addAll(resultList);
/*     */     } 
/* 227 */     VsanObjectInformation[] objInfos = objInfosList.<VsanObjectInformation>toArray(new VsanObjectInformation[0]);
/*     */ 
/*     */     
/* 230 */     List<VirtualObjectModel> models = new ArrayList<>();
/*     */     
/* 232 */     if (!vmRefs.isEmpty()) {
/* 233 */       models.addAll(VirtualObjectModel.buildVms(identities, vmRefs, objInfos, vmProperties, multimap));
/*     */     }
/*     */     
/* 236 */     models.addAll(VirtualObjectModel.buildFcds(identities, objInfos));
/*     */     
/* 238 */     models.addAll(VirtualObjectModel.buildOthers(vsanUuids, identities, objInfos));
/*     */     
/* 240 */     return models;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateVsanIdentities(VsanObjectIdentityAndHealth identities, ManagedObjectReference moRef) {
/* 245 */     if (!ArrayUtils.isEmpty((Object[])identities.identities)) {
/* 246 */       byte b; int i; VsanObjectIdentity[] arrayOfVsanObjectIdentity; for (i = (arrayOfVsanObjectIdentity = identities.identities).length, b = 0; b < i; ) { VsanObjectIdentity id = arrayOfVsanObjectIdentity[b];
/* 247 */         if (id.vm != null) {
/* 248 */           id.vm.setServerGuid(moRef.getServerGuid());
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<String> listVsanUuids(ManagedObjectReference clusterRef, Measure measure) {
/*     */     ManagedObjectReference[] hosts;
/*     */     Set<String> vsanUuids;
/*     */     try {
/* 260 */       Exception exception4, exception3 = null;
/*     */     }
/* 262 */     catch (Exception e) {
/* 263 */       logger.warn("Failed to obtain cluster hosts: " + clusterRef, e);
/* 264 */       return Collections.emptySet();
/*     */     } 
/*     */     
/* 267 */     Map<ManagedObjectReference, Future<String>> hostTasks = new HashMap<>();
/* 268 */     Exception exception1 = null, exception2 = null; try { VcConnection vcConnection = this.vcClient.getConnection(clusterRef.getServerGuid()); 
/* 269 */       try { byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference; for (i = (arrayOfManagedObjectReference = hosts).length, b = 0; b < i; ) { ManagedObjectReference host = arrayOfManagedObjectReference[b];
/*     */           
/* 271 */           ManagedObjectReference internalSystemRef = this.vmodlHelper.getVsanInternalSystem(host);
/* 272 */           VsanInternalSystem internalSystem = (VsanInternalSystem)vcConnection.createStub(VsanInternalSystem.class, internalSystemRef);
/*     */           
/* 274 */           Future<String> compositeUuidsFuture = measure.newFuture("vsanUuids(" + host.getValue() + ")");
/* 275 */           internalSystem.queryPhysicalVsanDisks(
/* 276 */               VsanHostPropertyProviderAdapter.PHYSICAL_DISK_VIRTUAL_MAPPING_PROPERTIES, compositeUuidsFuture);
/* 277 */           hostTasks.put(host, compositeUuidsFuture); b++; }
/*     */          }
/* 279 */       finally { if (vcConnection != null) vcConnection.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     return vsanUuids;
/*     */   }
/*     */   
/*     */   public Collection<ConfigInfo> listVmSnapshots(ManagedObjectReference vmRef, Measure measure) {
/* 300 */     return listVmSnapshots(new ManagedObjectReference[] { vmRef }, measure).get(vmRef);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<ManagedObjectReference, ConfigInfo> listVmSnapshots(ManagedObjectReference[] vmsArray, Measure measure) {
/* 305 */     HashMultimap hashMultimap = HashMultimap.create();
/*     */     
/* 307 */     if (ArrayUtils.isEmpty((Object[])vmsArray)) {
/* 308 */       logger.warn("No VMs given.");
/* 309 */       return (Multimap<ManagedObjectReference, ConfigInfo>)hashMultimap;
/*     */     } 
/*     */ 
/*     */     
/* 313 */     Map<ManagedObjectReference, SnapshotInfo> vmToSnapshot = new HashMap<>(); try {
/* 314 */       Exception exception2, exception1 = null;
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
/*     */     }
/* 327 */     catch (Exception e) {
/* 328 */       logger.error("Cannot retrieve snapshots for VMs: ", e);
/* 329 */       return (Multimap<ManagedObjectReference, ConfigInfo>)hashMultimap;
/*     */     } 
/*     */     
/* 332 */     if (vmToSnapshot.isEmpty()) {
/* 333 */       logger.debug("None of the VMs has snapshots");
/* 334 */       return (Multimap<ManagedObjectReference, ConfigInfo>)hashMultimap;
/*     */     } 
/*     */ 
/*     */     
/* 338 */     Map<ManagedObjectReference, ManagedObjectReference> snapshotToVm = new HashMap<>();
/*     */     
/* 340 */     for (ManagedObjectReference vm : vmToSnapshot.keySet()) {
/* 341 */       SnapshotInfo snapshotInfo = vmToSnapshot.get(vm);
/* 342 */       if (snapshotInfo.rootSnapshotList == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 346 */       Queue<SnapshotTree> snapshotTrees = new LinkedList<>(Arrays.asList(snapshotInfo.rootSnapshotList));
/*     */       
/* 348 */       while (!snapshotTrees.isEmpty()) {
/* 349 */         SnapshotTree tree = snapshotTrees.poll();
/* 350 */         snapshotToVm.put(tree.snapshot, vm);
/*     */         
/* 352 */         if (ArrayUtils.isNotEmpty((Object[])tree.childSnapshotList)) {
/* 353 */           snapshotTrees.addAll(Arrays.asList(tree.childSnapshotList));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 359 */     if (snapshotToVm.isEmpty()) {
/* 360 */       logger.debug("None of the VMs has snapshots");
/* 361 */       return (Multimap<ManagedObjectReference, ConfigInfo>)hashMultimap;
/*     */     } 
/*     */ 
/*     */     
/* 365 */     ManagedObjectReference[] snapshots = (ManagedObjectReference[])snapshotToVm.keySet().toArray((Object[])new ManagedObjectReference[0]); try {
/* 366 */       Exception exception2, exception1 = null;
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
/*     */     }
/* 383 */     catch (Exception e) {
/* 384 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 387 */     return (Multimap<ManagedObjectReference, ConfigInfo>)hashMultimap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Map<String, String> getProfilesInfo(ManagedObjectReference clusterRef) throws MethodFault {
/* 395 */     List<Profile> storageProfiles = BaseUtils.getStorageProfiles(clusterRef);
/* 396 */     Map<String, String> idToName = new HashMap<>();
/* 397 */     for (Profile profile : storageProfiles) {
/* 398 */       idToName.put(profile.profileId.uniqueId, profile.name);
/*     */     }
/* 400 */     return idToName;
/*     */   }
/*     */   
/*     */   private boolean isIscsiServiceEnabled(ManagedObjectReference clusterRef) throws Exception {
/* 404 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 405 */       return false;
/*     */     }
/* 407 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 408 */     ConfigInfoEx configInfoEx = vsanConfigSystem.getConfigInfoEx(clusterRef);
/* 409 */     return (configInfoEx.getIscsiConfig()).enabled.booleanValue();
/*     */   }
/*     */   @TsService
/*     */   public Map<String, Collection<VirtualObjectPlacementModel>> getPhysicalPlacement(ManagedObjectReference clusterRef, String[] objectIds) throws Exception {
/*     */     DataServiceResponse hostData;
/*     */     HashMultimap hashMultimap;
/*     */     VirtualObjectPlacementModel.Builder builder;
/* 416 */     List<VsanObject> virtualObjects = new ArrayList<>();
/* 417 */     List<DiskResult> vsanDisks = new ArrayList<>();
/*     */     
/* 419 */     Exception exception1 = null, exception2 = null; try { Measure measure = new Measure("Collecting placement details (" + objectIds.length + " objects)");
/*     */ 
/*     */       
/* 422 */       try { VsanHostsResult vsanHostsResult = 
/* 423 */           this.stretchedClusterService.collectVsanHosts(clusterRef, true, measure);
/*     */ 
/*     */         
/* 426 */         Map<ManagedObjectReference, Future<DiskResult[]>> hostDiskFutures = new HashMap<>();
/* 427 */         for (ManagedObjectReference hostRef : vsanHostsResult.getAll()) {
/* 428 */           Exception exception6, exception5 = null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 437 */         Set<ManagedObjectReference> hosts = vsanHostsResult.connectedMembers;
/* 438 */         populateVirtualObjectsFromInternalSystem(objectIds, virtualObjects, measure, hosts);
/*     */ 
/*     */         
/* 441 */         Exception exception3 = null, exception4 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */          }
/*     */       
/*     */       finally
/*     */       
/*     */       { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 458 */         if (measure != null) measure.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */     
/* 463 */     for (VsanObject vsanObject : virtualObjects) {
/* 464 */       hashMultimap.putAll(vsanObject.vsanObjectUuid, builder.build(vsanObject));
/*     */     }
/* 466 */     return hashMultimap.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateVirtualObjectsFromInternalSystem(String[] objectIds, List<VsanObject> virtualObjects, Measure measure, Set<ManagedObjectReference> hosts) throws Exception {
/* 474 */     Queue<List<String>> chunks = chunkify(objectIds);
/*     */ 
/*     */ 
/*     */     
/* 478 */     while (!chunks.isEmpty()) {
/* 479 */       Map<Future<String>, List<String>> futures = new HashMap<>();
/*     */ 
/*     */       
/* 482 */       for (ManagedObjectReference host : hosts) {
/* 483 */         Exception exception2; Future<String> future = measure.newFuture("VsanInternalSystem.queryVsanObjects[" + host + "]");
/* 484 */         List<String> chunk = chunks.poll();
/* 485 */         if (chunk == null) {
/*     */           break;
/*     */         }
/*     */         
/* 489 */         futures.put(future, chunk);
/*     */         
/* 491 */         logger.debug("Query UUIDs on " + host);
/*     */         
/* 493 */         Exception exception1 = null;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 501 */       logger.debug("Waiting for the started requests to finish.");
/*     */       
/* 503 */       for (Map.Entry<Future<String>, List<String>> entry : futures.entrySet()) {
/* 504 */         Future<String> future = entry.getKey();
/* 505 */         List<String> chunk = entry.getValue();
/*     */         
/*     */         try {
/* 508 */           String json = (String)future.get();
/* 509 */           List<VsanObject> vsanObjects = VsanComponentsProvider.VsanJsonParser.parseVsanObjects(json, chunk);
/* 510 */           virtualObjects.addAll(vsanObjects);
/* 511 */         } catch (SocketTimeoutException ste) {
/*     */ 
/*     */           
/* 514 */           logger.error(ste);
/* 515 */           String errorMessage = Utils.getLocalizedString("vsan.virtualObjects.error.timeout");
/* 516 */           throw new Exception(errorMessage);
/*     */         } 
/*     */       } 
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
/*     */   private Queue<List<String>> chunkify(String[] allUuids) {
/* 530 */     Queue<List<String>> chunks = new LinkedList<>();
/* 531 */     List<String> uuids = Arrays.asList(allUuids);
/* 532 */     int chunksCount = uuids.size() / 500;
/*     */     
/* 534 */     for (int i = 0; i < chunksCount; i++) {
/* 535 */       int startingIndex = i * 500;
/* 536 */       List<String> list = uuids.subList(startingIndex, startingIndex + 500);
/* 537 */       chunks.add(list);
/*     */     } 
/*     */ 
/*     */     
/* 541 */     List<String> subUuids = uuids.subList(chunksCount * 500, uuids.size());
/* 542 */     chunks.add(subUuids);
/*     */     
/* 544 */     logger.debug("Splitting the UUIDs into " + chunks.size() + " chunks.");
/*     */     
/* 546 */     return chunks;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public VirtualDisk getDiskDetails(ManagedObjectReference vmRef, String diskId) throws Exception {
/* 551 */     VirtualDevice[] virtualDevices = (VirtualDevice[])QueryUtil.getProperty(vmRef, "config.hardware.device", null);
/* 552 */     VirtualDisk result = VirtualObjectModel.findDisk(virtualDevices, diskId);
/* 553 */     if (result == null)
/*     */     {
/* 555 */       throw new IllegalArgumentException("Disk not found: " + diskId);
/*     */     }
/* 557 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/VirtualObjectsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */