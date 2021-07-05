/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.Folder;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanCapabilityData;
/*     */ import com.vmware.vsphere.client.vsan.base.util.cache.Cacheable;
/*     */ import com.vmware.vsphere.client.vsan.base.util.cache.TimeBasedCacheEntry;
/*     */ import com.vmware.vsphere.client.vsan.base.util.cache.TimeBasedCacheManager;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class VsanCapabilityCacheManager
/*     */   extends TimeBasedCacheManager<ManagedObjectReference, VsanCapabilityData>
/*     */ {
/*  28 */   private static final Log _logger = LogFactory.getLog(VsanCapabilityCacheManager.class);
/*  29 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanCapabilityCacheManager.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final UserSessionService sessionService;
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanCapabilityCacheManager(UserSessionService sessionService, int expirationTimeMin, int expirationTimeMax, int cleanThreshold) {
/*  38 */     super(expirationTimeMin, expirationTimeMax, cleanThreshold);
/*  39 */     this.sessionService = sessionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(ManagedObjectReference moRef) {
/*  45 */     Validate.notNull(moRef);
/*     */ 
/*     */     
/*  48 */     String type = moRef.getType();
/*  49 */     if (!ClusterComputeResource.class.getSimpleName().equals(type) && 
/*  50 */       !HostSystem.class.getSimpleName().equals(type) && 
/*  51 */       !Folder.class.getSimpleName().equals(type)) {
/*  52 */       throw new IllegalArgumentException(
/*  53 */           "Unsupporter ManagedObjectReference type given: " + type);
/*     */     }
/*     */   }
/*     */   
/*     */   public VsanCapabilityData getVcCapabilities(ManagedObjectReference ref) {
/*  58 */     return (VsanCapabilityData)get(ref, VsanCacheType.VC);
/*     */   }
/*     */   
/*     */   public VsanCapabilityData getClusterCapabilities(ManagedObjectReference ref) {
/*  62 */     VsanCapabilityData capabilityData = (VsanCapabilityData)get(ref, VsanCacheType.CLUSTER);
/*  63 */     return capabilityData;
/*     */   }
/*     */   
/*     */   public VsanCapabilityData getHostCapabilities(ManagedObjectReference ref) {
/*  67 */     return (VsanCapabilityData)get(ref, VsanCacheType.HOST);
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
/*     */   protected String getKey(ManagedObjectReference moRef, TimeBasedCacheManager.CacheType type) {
/*  81 */     switch ((VsanCacheType)type) {
/*     */       
/*     */       case VC:
/*  84 */         return moRef.getServerGuid();
/*     */       
/*     */       case HOST:
/*     */       case null:
/*  88 */         return String.valueOf(moRef.getServerGuid()) + ":" + moRef.getValue();
/*     */     } 
/*  90 */     throw new UnsupportedOperationException("Unsupported cache manager type!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String sessionKey() {
/*  99 */     String key = (this.sessionService.getUserSession()).clientId;
/* 100 */     if (key == null) {
/* 101 */       throw new RuntimeException("Failed to retrieve the clientId from the session. Most probably, the threadlocal context is not correctly set. Session: " + 
/*     */           
/* 103 */           this.sessionService.getUserSession());
/*     */     }
/* 105 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TimeBasedCacheEntry<VsanCapabilityData> createEntry(ManagedObjectReference moRef, TimeBasedCacheManager.CacheType type) {
/* 113 */     switch ((VsanCacheType)type) {
/*     */       case VC:
/* 115 */         return new VcCapabilityTimeBasedCacheEntry(moRef);
/*     */       case null:
/* 117 */         return new ClusterCapabilityTimeBasedCacheEntry(moRef);
/*     */       case HOST:
/* 119 */         return new HostCapabilityTimeBasedCacheEntry(moRef);
/*     */     } 
/* 121 */     throw new UnsupportedOperationException("Unsupported cache manager type!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum VsanCacheType
/*     */     implements TimeBasedCacheManager.CacheType
/*     */   {
/* 131 */     VC,
/* 132 */     HOST,
/* 133 */     CLUSTER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class VsanTimeBasedCacheEntry
/*     */     extends TimeBasedCacheEntry<VsanCapabilityData>
/*     */   {
/*     */     protected final ManagedObjectReference _moRef;
/*     */ 
/*     */     
/*     */     public VsanTimeBasedCacheEntry(ManagedObjectReference moRef) {
/* 145 */       this._moRef = moRef;
/*     */     }
/*     */ 
/*     */     
/*     */     protected VsanCapabilityData load() {
/* 150 */       VsanCapabilityData result = new VsanCapabilityData();
/*     */       try {
/* 152 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 159 */       catch (Exception ex) {
/* 160 */         VsanCapabilityCacheManager._logger.error("Cannot retrieve capabilities", ex);
/*     */       } 
/*     */       
/* 163 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract ManagedObjectReference[] getArgs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class VcCapabilityTimeBasedCacheEntry
/*     */     extends VsanTimeBasedCacheEntry
/*     */   {
/*     */     public VcCapabilityTimeBasedCacheEntry(ManagedObjectReference moRef) {
/* 179 */       super(moRef);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ManagedObjectReference[] getArgs() {
/* 184 */       return new ManagedObjectReference[0];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ClusterCapabilityTimeBasedCacheEntry
/*     */     extends VsanTimeBasedCacheEntry
/*     */   {
/*     */     public ClusterCapabilityTimeBasedCacheEntry(ManagedObjectReference moRef) {
/* 196 */       super(moRef);
/*     */     }
/*     */ 
/*     */     
/*     */     protected VsanCapabilityData load() {
/* 201 */       VsanCapabilityData result = new VsanCapabilityData();
/*     */       try {
/* 203 */         Exception exception2, exception1 = null;
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
/*     */       }
/* 262 */       catch (Exception ex) {
/* 263 */         VsanCapabilityCacheManager._logger.error("Cannot retrieve capabilities", ex);
/*     */       } 
/*     */       
/* 266 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected ManagedObjectReference[] getArgs() {
/* 272 */       ManagedObjectReference clonedMoRef = new ManagedObjectReference(
/* 273 */           this._moRef.getType(), this._moRef.getValue());
/* 274 */       return new ManagedObjectReference[] { clonedMoRef };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class HostCapabilityTimeBasedCacheEntry
/*     */     extends VsanTimeBasedCacheEntry
/*     */   {
/*     */     public HostCapabilityTimeBasedCacheEntry(ManagedObjectReference moRef) {
/* 285 */       super(moRef);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected ManagedObjectReference[] getArgs() {
/* 291 */       ManagedObjectReference clonedMoRef = new ManagedObjectReference(
/* 292 */           this._moRef.getType(), this._moRef.getValue());
/* 293 */       return new ManagedObjectReference[] { clonedMoRef };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/VsanCapabilityCacheManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */