/*     */ package com.vmware.vsphere.client.vsan.whatif;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.VsanWhatIfEvacDetail;
/*     */ import com.vmware.vsan.client.services.virtualobjects.VirtualObjectsService;
/*     */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectModel;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.util.FormatUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WhatIfPropertyProvider
/*     */ {
/*     */   public static final String IS_WHAT_IF_SUPPORTED = "isWhatIfSupported";
/*     */   @Autowired
/*     */   private VirtualObjectsService _virtualObjectsService;
/*  44 */   private static final VsanProfiler _profiler = new VsanProfiler(WhatIfPropertyProvider.class);
/*     */   
/*  46 */   private static final Log _logger = LogFactory.getLog(WhatIfPropertyProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getIsWhatIfSupported(ManagedObjectReference host) {
/*  53 */     return VsanCapabilityUtils.isWhatIfSupported(host);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public WhatIfResult getWhatIfResult(ManagedObjectReference hostRef, WhatIfSpec spec) throws Exception {
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
/*     */     try {
/*     */     
/*     */     } finally {
/* 119 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WhatIfData getWhatIfData(VsanWhatIfEvacDetail whatIfEvacDetail, Boolean detailed, List<VirtualObjectModel> virtualObjects, boolean isForNoAction, boolean isForHost, long repairTime) {
/* 129 */     WhatIfData result = new WhatIfData();
/* 130 */     result.success = whatIfEvacDetail.success.booleanValue();
/* 131 */     result.successWithoutDataLoss = (whatIfEvacDetail.success.booleanValue() && ArrayUtils.isEmpty((Object[])whatIfEvacDetail.inaccessibleObjects));
/* 132 */     result.bytesToSync = (whatIfEvacDetail.bytesToSync == null) ? 
/* 133 */       0L : whatIfEvacDetail.bytesToSync.longValue();
/* 134 */     result.extraSpaceNeeded = (whatIfEvacDetail.extraSpaceNeeded == null) ? 
/* 135 */       0L : whatIfEvacDetail.extraSpaceNeeded.longValue();
/* 136 */     result.failedDueToInaccessibleObjects = (whatIfEvacDetail.failedDueToInaccessibleObjects == null) ? false : 
/* 137 */       whatIfEvacDetail.failedDueToInaccessibleObjects.booleanValue();
/* 138 */     result.successWithInaccessibleOrNonCompliantObjects = !((!whatIfEvacDetail.success.booleanValue() || 
/* 139 */       ArrayUtils.isEmpty((Object[])whatIfEvacDetail.inaccessibleObjects)) && 
/* 140 */       ArrayUtils.isEmpty((Object[])whatIfEvacDetail.incompliantObjects));
/* 141 */     if (detailed.booleanValue()) {
/* 142 */       result.objects = new ArrayList<>();
/* 143 */       result.objects.addAll(getVsanObjects(
/* 144 */             whatIfEvacDetail.inaccessibleObjects, virtualObjects, VsanWhatIfComplianceStatus.INACCESSIBLE));
/* 145 */       result.objects.addAll(getVsanObjects(
/* 146 */             whatIfEvacDetail.incompliantObjects, virtualObjects, VsanWhatIfComplianceStatus.NOT_COMPLIANT));
/*     */     } 
/*     */     
/* 149 */     result.summary = getSummary(whatIfEvacDetail, detailed.booleanValue(), Boolean.valueOf(isForNoAction), isForHost);
/* 150 */     if (ArrayUtils.isNotEmpty((Object[])whatIfEvacDetail.incompliantObjects)) {
/* 151 */       result.repairTime = repairTime;
/*     */     }
/*     */     
/* 154 */     return result;
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
/*     */   private List<VirtualObjectModel> getVsanObjects(String[] objectUUIDs, List<VirtualObjectModel> virtualObjects, VsanWhatIfComplianceStatus status) {
/* 166 */     List<VirtualObjectModel> result = new ArrayList<>();
/* 167 */     if (objectUUIDs == null) {
/* 168 */       return result;
/*     */     }
/*     */     
/* 171 */     Set<String> uuids = new HashSet<>(Arrays.asList(objectUUIDs));
/*     */ 
/*     */     
/* 174 */     for (VirtualObjectModel virtualObject : virtualObjects) {
/*     */       
/* 176 */       if (ArrayUtils.isEmpty((Object[])virtualObject.children)) {
/* 177 */         if (uuids.contains(virtualObject.uid)) {
/* 178 */           virtualObject.whatIfComplianceStatus = status;
/* 179 */           result.add(virtualObject.cloneWithoutChildren());
/*     */         } 
/*     */         continue;
/*     */       } 
/* 183 */       if (virtualObject.vmRef != null) {
/* 184 */         virtualObject.healthState = null;
/* 185 */         virtualObject.storagePolicy = null;
/*     */       } 
/*     */       
/* 188 */       List<VirtualObjectModel> children = new ArrayList<>(); byte b; int i; VirtualObjectModel[] arrayOfVirtualObjectModel;
/* 189 */       for (i = (arrayOfVirtualObjectModel = virtualObject.children).length, b = 0; b < i; ) { VirtualObjectModel child = arrayOfVirtualObjectModel[b];
/* 190 */         if (uuids.contains(child.uid)) {
/* 191 */           child.whatIfComplianceStatus = status;
/* 192 */           children.add(child.cloneWithoutChildren());
/*     */         } 
/*     */         b++; }
/*     */       
/* 196 */       VirtualObjectModel clone = virtualObject.cloneWithoutChildren();
/* 197 */       if (uuids.contains(clone.uid)) {
/* 198 */         clone.whatIfComplianceStatus = status;
/*     */       }
/*     */       
/* 201 */       if (!children.isEmpty()) {
/* 202 */         clone.children = children.<VirtualObjectModel>toArray(new VirtualObjectModel[children.size()]);
/* 203 */         result.add(clone); continue;
/* 204 */       }  if (uuids.contains(virtualObject.uid)) {
/* 205 */         result.add(clone);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 210 */     return result;
/*     */   }
/*     */   
/*     */   private String getSummary(VsanWhatIfEvacDetail detail, boolean detailed, Boolean isForNoAction, boolean isForHost) {
/* 214 */     String result = "";
/* 215 */     long bytesToSynch = (detail.bytesToSync == null) ? 0L : detail.bytesToSync.longValue();
/* 216 */     String formattedBytesToSynch = FormatUtil.getStorageFormatted(
/* 217 */         Long.valueOf(bytesToSynch), 1L, -1L);
/*     */     
/* 219 */     if (detail.success.booleanValue()) {
/*     */ 
/*     */ 
/*     */       
/* 223 */       if (!isForNoAction.booleanValue()) {
/* 224 */         if (bytesToSynch > 0L) {
/*     */           
/* 226 */           if (ArrayUtils.isEmpty((Object[])detail.incompliantObjects)) {
/* 227 */             result = Utils.getLocalizedString("vsan.whatIf.summary.common.sufficientCapacity", new String[] { " " });
/*     */           }
/*     */           
/* 230 */           result = Utils.getLocalizedString("vsan.whatIf.summary.common.storageMoved", new String[] {
/* 231 */                 result, formattedBytesToSynch, " " });
/*     */         } else {
/* 233 */           result = Utils.getLocalizedString("vsan.whatIf.summary.common.noDataMoved", new String[] { " " });
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 238 */       if (!ArrayUtils.isEmpty((Object[])detail.inaccessibleObjects)) {
/* 239 */         String inaccessibleObjectsCount = (detail.inaccessibleObjects == null) ? 
/* 240 */           String.valueOf(0) : String.valueOf(detail.inaccessibleObjects.length);
/* 241 */         result = Utils.getLocalizedString("vsan.whatIf.summary.success.inaccessibleObjects", new String[] {
/* 242 */               result, inaccessibleObjectsCount, " "
/*     */             });
/*     */       } 
/*     */       
/* 246 */       if (!ArrayUtils.isEmpty((Object[])detail.incompliantObjects)) {
/* 247 */         String incompliantObjectsCount = (detail.incompliantObjects == null) ? 
/* 248 */           String.valueOf(0) : String.valueOf(detail.incompliantObjects.length);
/* 249 */         result = Utils.getLocalizedString("vsan.whatIf.summary.success.nonCompliant", new String[] { result, 
/* 250 */               incompliantObjectsCount, " " });
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 255 */       if ((!ArrayUtils.isEmpty((Object[])detail.inaccessibleObjects) || 
/* 256 */         !ArrayUtils.isEmpty((Object[])detail.incompliantObjects)) && !detailed && 
/* 257 */         !isForHost) {
/* 258 */         result = Utils.getLocalizedString("vsan.whatIf.summary.seeDetails", new String[] { result });
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 265 */     else if (detail.extraSpaceNeeded != null && detail.extraSpaceNeeded.longValue() > 0L) {
/* 266 */       String formatedExtraStorage = FormatUtil.getStorageFormatted(
/* 267 */           detail.extraSpaceNeeded, 1L, -1L);
/* 268 */       result = Utils.getLocalizedString("vsan.whatIf.summary.failure.extraStorageNeeded", new String[] {
/* 269 */             formatedExtraStorage });
/* 270 */     } else if (detail.failedDueToInaccessibleObjects != null && detail.failedDueToInaccessibleObjects.booleanValue()) {
/*     */       
/* 272 */       result = Utils.getLocalizedString("vsan.whatIf.summary.failure.dueToInaccessibleObjects");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     if (result.equals("")) {
/* 279 */       if (detail.success.booleanValue()) {
/* 280 */         result = Utils.getLocalizedString("vsan.whatIf.summary.common.success");
/*     */       } else {
/* 282 */         result = Utils.getLocalizedString("vsan.whatIf.summary.common.error");
/*     */       } 
/*     */     }
/* 285 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getClusterRepairTime(ManagedObjectReference clusterRef) throws Exception {
/* 295 */     Long objectRepairTime = null; try {
/* 296 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 305 */     catch (Exception e) {
/* 306 */       _logger.error("Failed to retrieve repair time value on cluster.", e);
/* 307 */       throw new Exception(e.getLocalizedMessage(), e);
/*     */     } 
/*     */     
/* 310 */     return (objectRepairTime != null) ? objectRepairTime.longValue() : 0L;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/whatif/WhatIfPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */