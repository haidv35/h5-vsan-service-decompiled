/*     */ package com.vmware.vsan.client.services.diskmanagement;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.host.StorageDeviceInfo;
/*     */ import com.vmware.vim.binding.vim.host.VsanInternalSystem;
/*     */ import com.vmware.vim.binding.vim.vsan.host.ClusterStatus;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vmomi.core.impl.BlockingFuture;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VSANWitnessHostInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapability;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilitySystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcDiskManagementSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*     */ import com.vmware.vsan.client.services.stretchedcluster.ConfigureStretchedClusterService;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsan.client.util.NoOpMeasure;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.service.VsanService;
/*     */ import com.vmware.vsphere.client.vsan.base.service.VsanServiceFactory;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.multithreading.VsanAsyncQueryUtils;
/*     */ import com.vmware.vsphere.client.vsan.dataprovider.VsanHostPropertyProviderAdapter;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
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
/*     */ public class DiskManagementService {
/*  41 */   private static final Logger logger = LoggerFactory.getLogger(DiskManagementService.class);
/*     */ 
/*     */   
/*     */   private static final String HOST_DISCONNECTED_MESSAGE_KEY = "com.vmware.vsan.diskmgmt.msg.hostnotconnected";
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   
/*     */   @Autowired
/*     */   private VsanServiceFactory vsanServiceFactory;
/*     */   
/*     */   @Autowired
/*     */   private VsanHostPropertyProviderAdapter vsanPropertyProvider;
/*     */   
/*     */   @Autowired
/*     */   private ConfigureStretchedClusterService stretchedClusterService;
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public List<HostData> listHosts(ManagedObjectReference clusterRef) throws Throwable {
/*  65 */     Exception exception1 = null, exception2 = null;
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
/* 133 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     }  } @TsService
/*     */   public boolean hasNetworkPartitions(ManagedObjectReference clusterRef) throws Exception {
/*     */     ManagedObjectReference[] hosts;
/* 138 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 139 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/* 140 */     BlockingFuture blockingFuture = new BlockingFuture();
/* 141 */     stretchedClusterSystem.getWitnessHosts(clusterRef, (Future)blockingFuture);
/*     */ 
/*     */     
/*     */     try {
/* 145 */       hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/* 146 */     } catch (Exception e) {
/* 147 */       logger.warn("Failed to list hosts, presumably empty cluster.", e);
/* 148 */       return false;
/*     */     } 
/* 150 */     VSANWitnessHostInfo[] witnesses = (VSANWitnessHostInfo[])await((Map<ManagedObjectReference, Future<?>>)ImmutableMap.of(clusterRef, blockingFuture)).get(clusterRef);
/* 151 */     List<ManagedObjectReference> allHosts = new ArrayList<>(Arrays.asList(hosts));
/* 152 */     if (witnesses != null) {
/* 153 */       byte b; int i; VSANWitnessHostInfo[] arrayOfVSANWitnessHostInfo; for (i = (arrayOfVSANWitnessHostInfo = witnesses).length, b = 0; b < i; ) { VSANWitnessHostInfo info = arrayOfVSANWitnessHostInfo[b];
/* 154 */         ManagedObjectReference witnessRef = new ManagedObjectReference(
/* 155 */             info.host.getType(), info.host.getValue(), clusterRef.getServerGuid());
/* 156 */         allHosts.add(witnessRef);
/*     */         b++; }
/*     */     
/*     */     } 
/* 160 */     Map<ManagedObjectReference, ClusterStatus> hostClusterStates = await(
/* 161 */         getHostClusterStatusAsync(Arrays.asList(hosts), (Measure)new NoOpMeasure()));
/*     */     
/* 163 */     for (ClusterStatus status : hostClusterStates.values()) {
/* 164 */       int members = (status.memberUuid != null) ? status.memberUuid.length : 0;
/* 165 */       if (members != allHosts.size()) {
/* 166 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public List<DiskData> listEligibleDisks(ManagedObjectReference hostRef, Boolean flashOnly) throws Throwable {
/* 175 */     Exception exception1 = null, exception2 = null;
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
/* 203 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } public Map<ManagedObjectReference, Future<DiskMapInfoEx[]>> getDiskMappingsAsync(List<ManagedObjectReference> allHosts, Measure measure) {
/* 207 */     Map<ManagedObjectReference, Future<DiskMapInfoEx[]>> result = new HashMap<>();
/* 208 */     for (ManagedObjectReference hostRef : allHosts) {
/* 209 */       VsanVcDiskManagementSystem diskSystem = VsanProviderUtils.getVcDiskManagementSystem(hostRef);
/* 210 */       Future<DiskMapInfoEx[]> future = measure.newFuture("DiskMapInfoEx[]");
/* 211 */       diskSystem.queryDiskMappings(hostRef, future);
/* 212 */       result.put(hostRef, future);
/*     */     } 
/* 214 */     return result;
/*     */   }
/*     */   
/*     */   private Map<ManagedObjectReference, Future<DiskResult[]>> getVsanDisksAsync(List<ManagedObjectReference> allHosts, Measure measure) {
/* 218 */     Map<ManagedObjectReference, Future<DiskResult[]>> tasks = Maps.newHashMap();
/* 219 */     for (ManagedObjectReference hostRef : allHosts) { try {
/* 220 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 225 */       catch (Exception exception) {
/*     */         
/* 227 */         logger.warn("Unable to extract disks data for host (probably witness): " + hostRef);
/*     */       }  }
/*     */ 
/*     */     
/* 231 */     return tasks;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<ManagedObjectReference, Future<ClusterStatus>> getHostClusterStatusAsync(List<ManagedObjectReference> allHosts, Measure measure) {
/* 236 */     Map<ManagedObjectReference, Future<ClusterStatus>> tasks = Maps.newHashMap();
/* 237 */     for (ManagedObjectReference hostRef : allHosts) { try {
/* 238 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 243 */       catch (Exception exception) {
/*     */         
/* 245 */         logger.warn("Unable to extract disks data for host (probably witness): " + hostRef);
/*     */       }  }
/*     */ 
/*     */     
/* 249 */     return tasks;
/*     */   }
/*     */   
/*     */   private Map<ManagedObjectReference, Future<String>> getDiskHealthAndVersionAsync(List<ManagedObjectReference> allHosts, Measure measure) {
/* 253 */     Map<ManagedObjectReference, Future<String>> tasks = new HashMap<>();
/* 254 */     for (ManagedObjectReference hostRef : allHosts) {
/* 255 */       ManagedObjectReference vsanInternalSystemRef = this.vmodlHelper.getVsanInternalSystem(hostRef);
/*     */       
/* 257 */       VsanService vsanService = this.vsanServiceFactory.getService(hostRef.getServerGuid());
/* 258 */       VsanInternalSystem vsanInternalSystem = (VsanInternalSystem)vsanService.getManagedObject(vsanInternalSystemRef);
/* 259 */       Future<String> future = measure.newFuture("_diskHealth");
/* 260 */       vsanInternalSystem.queryPhysicalVsanDisks(new String[] { "disk_health", "formatVersion", "publicFormatVersion", "self_only" }, future);
/* 261 */       tasks.put(hostRef, future);
/*     */     } 
/* 263 */     return tasks;
/*     */   }
/*     */   
/*     */   private Map<ManagedObjectReference, Future<StorageDeviceInfo>> getHostDeviceInfosAsync(List<ManagedObjectReference> allHosts, Measure measure) {
/* 267 */     Map<ManagedObjectReference, Future<StorageDeviceInfo>> tasks = new HashMap<>();
/* 268 */     for (ManagedObjectReference hostRef : allHosts) {
/* 269 */       Exception exception2, exception1 = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     return tasks;
/*     */   }
/*     */ 
/*     */   
/*     */   private Future<VsanCapability[]> getHostCapabilitiesAsync(ManagedObjectReference clusterRef, List<ManagedObjectReference> allHosts, Measure measure) {
/* 281 */     VsanCapabilitySystem capabilitySystem = VsanProviderUtils.getVsanCapabilitySystem(clusterRef);
/* 282 */     Future<VsanCapability[]> future = measure.newFuture("VsanCapability[]");
/* 283 */     capabilitySystem.getCapabilities(allHosts.<ManagedObjectReference>toArray(new ManagedObjectReference[allHosts.size()]), future);
/* 284 */     return future;
/*     */   }
/*     */   
/*     */   private static <T> Map<ManagedObjectReference, T> await(Map<ManagedObjectReference, Future<T>> tasks) {
/* 288 */     return VsanAsyncQueryUtils.awaitAll(tasks, new TaskAwaitor(null));
/*     */   }
/*     */   
/*     */   private static class TaskAwaitor<T> implements Function<Map.Entry<ManagedObjectReference, Future<T>>, T> { private TaskAwaitor() {}
/*     */     
/*     */     public T apply(Map.Entry<ManagedObjectReference, Future<T>> future) {
/*     */       try {
/* 295 */         return (T)((Future)future.getValue()).get();
/* 296 */       } catch (Exception e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 301 */         DiskManagementService.logger.warn("Cannot execute task: ", e);
/* 302 */         return null;
/*     */       } 
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
/*     */   private class DiskMappingsAwaitor
/*     */     implements Function<Map.Entry<ManagedObjectReference, Future<DiskMapInfoEx[]>>, DiskMapInfoEx[]>
/*     */   {
/*     */     private DiskMappingsAwaitor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DiskMapInfoEx[] apply(Map.Entry<ManagedObjectReference, Future<DiskMapInfoEx[]>> future) {
/*     */       try {
/* 327 */         return (DiskMapInfoEx[])((Future)future.getValue()).get();
/* 328 */       } catch (Exception ex) {
/* 329 */         DiskManagementService.logger.warn("Cannot query host's disk mappings from VsanVcDiskManagementSystem; Trying to get disk mappings from host's storageInfo property", 
/* 330 */             ex);
/* 331 */         return VsanHostPropertyProviderAdapter.getDiskMapingFallback(future.getKey());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskmanagement/DiskManagementService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */