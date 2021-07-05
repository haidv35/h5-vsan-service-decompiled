/*     */ package com.vmware.vsphere.client.vsan.iscsi.providers;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.vim.fault.VsanFault;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.MethodFault;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiLUN;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTarget;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiLun;
/*     */ import com.vmware.vsphere.client.vsan.base.data.IscsiTarget;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.multithreading.VsanAsyncQueryUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.VsanIscsiTargetProviderParameter;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.models.target.initiator.TargetInitiatorSpec;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanIscsiTargetPropertyProvider
/*     */ {
/*  43 */   private static final Log _logger = LogFactory.getLog(VsanIscsiTargetPropertyProvider.class);
/*     */   
/*  45 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  46 */       VsanIscsiTargetPropertyProvider.class);
/*     */ 
/*     */   
/*     */   private static final String HOST_TYPE = "HostSystem";
/*     */ 
/*     */   
/*     */   private static final String MANAGED_OBJECT_PREFIX = "urn:vmomi:";
/*     */ 
/*     */   
/*     */   private static final String COLON = ":";
/*     */   
/*     */   private static final String NAMESPACE_CAPABILITY_METADATA = "namespaceCapabilityMetadata";
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public IscsiTarget getIscsiTarget(ManagedObjectReference clusterRef, String targetAlias) throws Exception {
/*     */     List<Profile> profiles;
/*  63 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*  64 */       return null;
/*     */     }
/*     */     
/*  67 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*  68 */     VsanIscsiTarget target = null;
/*  69 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanIscsiSystem.getIscsiTarget"); 
/*  70 */       try { target = vsanIscsiSystem.getIscsiTarget(clusterRef, targetAlias); }
/*  71 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     IscsiTarget result = new IscsiTarget(target, null, profiles, null);
/*  78 */     return result;
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
/*     */   public IscsiLun[] getVsanIscsiTargetLunList(ManagedObjectReference clusterRef, String targetAlias) throws Exception {
/*  92 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/*  97 */     VsanIscsiLUN[] iscsiLuns = null; try {
/*  98 */       Exception exception2, exception1 = null;
/*     */     }
/* 100 */     catch (VsanFault e) {
/* 101 */       Exception ex = new Exception(e.getLocalizedMessage(), e.getCause());
/* 102 */       throw ex;
/*     */     } 
/* 104 */     if (iscsiLuns == null) {
/* 105 */       return null;
/*     */     }
/*     */     
/* 108 */     int i = 0;
/* 109 */     IscsiLun[] luns = new IscsiLun[iscsiLuns.length];
/* 110 */     List<Profile> profiles = BaseUtils.getStorageProfiles(clusterRef); byte b; int j; VsanIscsiLUN[] arrayOfVsanIscsiLUN1;
/* 111 */     for (j = (arrayOfVsanIscsiLUN1 = iscsiLuns).length, b = 0; b < j; ) { VsanIscsiLUN lun = arrayOfVsanIscsiLUN1[b];
/* 112 */       luns[i++] = new IscsiLun(lun, profiles); b++; }
/*     */     
/* 114 */     return luns;
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
/*     */   public TargetInitiatorSpec[] getVsanIscsiTargetInitiatorList(ManagedObjectReference clusterRef, String targetIqn) throws Exception {
/*     */     List<TargetInitiatorSpec> targetInitiatorList;
/* 128 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 129 */       return null;
/*     */     }
/*     */     
/* 132 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/* 133 */     VsanIscsiTarget vsanIscsiTarget = null;
/* 134 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = _profiler.point("vsanIscsiSystem.getIscsiTarget"); 
/* 135 */       try { vsanIscsiTarget = vsanIscsiSystem.getIscsiTarget(clusterRef, targetIqn); }
/* 136 */       finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     return targetInitiatorList.<TargetInitiatorSpec>toArray(new TargetInitiatorSpec[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public IscsiTarget[] getIscsiTargets(ManagedObjectReference clusterRef, VsanIscsiTargetProviderParameter param) throws Exception {
/* 168 */     if (!VsanCapabilityUtils.isIscsiTargetsSupportedOnVc(clusterRef)) {
/* 169 */       return new IscsiTarget[0];
/*     */     }
/*     */     
/* 172 */     List<Callable<VsanAsyncQueryUtils.RequestResult>> requestTasks = new ArrayList<>();
/*     */     
/* 174 */     if (param == null || param.requestNamespaceCapabilityMetadata) {
/* 175 */       requestTasks.add(getNamespaceCapabilityMetadata(clusterRef));
/*     */     }
/*     */ 
/*     */     
/* 179 */     if (param == null || param.requestStorageProfiles) {
/* 180 */       requestTasks.add(VsanAsyncQueryUtils.getStorageProfiles(clusterRef));
/*     */     }
/* 182 */     requestTasks.add(getIscsiObjects(clusterRef));
/*     */     
/* 184 */     ResultSet resultSet = VsanAsyncQueryUtils.getProperties(requestTasks);
/*     */     
/* 186 */     VsanIscsiTarget[] targets = new VsanIscsiTarget[0];
/* 187 */     Map<String, List<VsanIscsiLUN>> targetToLuns = new HashMap<>();
/* 188 */     Object namespaceMetadata = null;
/* 189 */     List<Profile> storageProfiles = new ArrayList<>();
/*     */     
/* 191 */     if (resultSet.error != null)
/* 192 */       throw resultSet.error;  byte b1;
/*     */     int j;
/*     */     ResultItem[] arrayOfResultItem;
/* 195 */     for (j = (arrayOfResultItem = resultSet.items).length, b1 = 0; b1 < j; ) { ResultItem item = arrayOfResultItem[b1]; byte b; int m; PropertyValue[] arrayOfPropertyValue;
/* 196 */       for (m = (arrayOfPropertyValue = item.properties).length, b = 0; b < m; ) { IscsiTargetsTaskResult iscsiResult; PropertyValue prop = arrayOfPropertyValue[b]; String str;
/* 197 */         switch ((str = prop.propertyName).hashCode()) { case -1991465083: if (!str.equals("storageProfiles")) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 202 */             storageProfiles = (List<Profile>)prop.value; break;case -1099694814: if (!str.equals("namespaceCapabilityMetadata"))
/*     */               break;  namespaceMetadata = prop.value; break;
/*     */           case -1063158413: if (!str.equals("iscsiTargets"))
/* 205 */               break;  iscsiResult = (IscsiTargetsTaskResult)prop.value;
/* 206 */             targets = iscsiResult.targets;
/* 207 */             targetToLuns = iscsiResult.targetToLuns;
/*     */             break; }
/*     */ 
/*     */         
/*     */         b++; }
/*     */       
/*     */       b1++; }
/*     */     
/* 215 */     IscsiTarget[] result = new IscsiTarget[targets.length];
/* 216 */     int i = 0; byte b2; int k; VsanIscsiTarget[] arrayOfVsanIscsiTarget1;
/* 217 */     for (k = (arrayOfVsanIscsiTarget1 = targets).length, b2 = 0; b2 < k; ) { VsanIscsiTarget target = arrayOfVsanIscsiTarget1[b2];
/* 218 */       IscsiTarget item = new IscsiTarget(target, targetToLuns.get(target.alias), storageProfiles, namespaceMetadata);
/* 219 */       item.ioOwnerHost = buildHostMor(item.ioOwnerHost, clusterRef.getServerGuid());
/* 220 */       result[i++] = item;
/*     */       b2++; }
/*     */     
/* 223 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private Callable<VsanAsyncQueryUtils.RequestResult> getIscsiObjects(final ManagedObjectReference clusterRef) {
/* 228 */     return new Callable<VsanAsyncQueryUtils.RequestResult>()
/*     */       {
/*     */         public VsanAsyncQueryUtils.RequestResult call() {
/* 231 */           Exception error = null;
/* 232 */           VsanIscsiTargetPropertyProvider.IscsiTargetsTaskResult iscsiResult = new VsanIscsiTargetPropertyProvider.IscsiTargetsTaskResult(null);
/*     */           try {
/* 234 */             iscsiResult = VsanIscsiTargetPropertyProvider.this.getIscsiTargetsResult(clusterRef);
/* 235 */           } catch (Exception ex) {
/* 236 */             error = ex;
/*     */           } 
/* 238 */           return new VsanAsyncQueryUtils.RequestResult(
/* 239 */               iscsiResult, 
/* 240 */               (error != null) ? error : iscsiResult.error, 
/* 241 */               clusterRef, 
/* 242 */               "iscsiTargets");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private Callable<VsanAsyncQueryUtils.RequestResult> getNamespaceCapabilityMetadata(final ManagedObjectReference clusterRef) {
/* 249 */     return new Callable<VsanAsyncQueryUtils.RequestResult>() {
/*     */         public VsanAsyncQueryUtils.RequestResult call() {
/*     */           MethodFault methodFault;
/* 252 */           Exception error = null;
/* 253 */           Object namespaceMetadata = null;
/*     */           try {
/* 255 */             PropertyValue[] resultset = 
/* 256 */               QueryUtil.getProperties(clusterRef, new String[] { "namespaceCapabilityMetadata" }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 257 */             for (i = (arrayOfPropertyValue1 = resultset).length, b = 0; b < i; ) { PropertyValue propVal = arrayOfPropertyValue1[b];
/* 258 */               namespaceMetadata = propVal.value; b++; }
/*     */           
/* 260 */           } catch (Exception ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 266 */             Throwable cause = ex;
/* 267 */             boolean isNoPermission = false;
/*     */             do {
/* 269 */               if (cause instanceof com.vmware.vim.binding.vim.fault.NoPermission) {
/* 270 */                 isNoPermission = true;
/*     */                 break;
/*     */               } 
/* 273 */               cause = cause.getCause();
/* 274 */             } while (cause != null);
/*     */             
/* 276 */             if (!isNoPermission) {
/* 277 */               methodFault = Utils.getMethodFault(ex);
/*     */             }
/*     */           } 
/* 280 */           return new VsanAsyncQueryUtils.RequestResult(namespaceMetadata, (Exception)methodFault, clusterRef, "namespaceCapabilityMetadata");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private IscsiTargetsTaskResult getIscsiTargetsResult(ManagedObjectReference clusterRef) throws Exception {
/* 286 */     IscsiTargetsTaskResult iscsiResult = new IscsiTargetsTaskResult(null);
/*     */     
/* 288 */     VsanIscsiTargetSystem vsanIscsiSystem = VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/* 289 */     VsanIscsiTarget[] targets = null; try {
/* 290 */       Exception exception2, exception1 = null;
/*     */     }
/* 292 */     catch (Exception e) {
/* 293 */       Exception ex = new Exception(e.getLocalizedMessage(), e);
/* 294 */       throw ex;
/*     */     } 
/* 296 */     if (ArrayUtils.isEmpty((Object[])targets)) {
/* 297 */       return iscsiResult;
/*     */     }
/* 299 */     iscsiResult.targets = targets;
/*     */     
/* 301 */     VsanIscsiLUN[] luns = new VsanIscsiLUN[0]; try {
/* 302 */       Exception exception2, exception1 = null;
/*     */     
/*     */     }
/* 305 */     catch (VsanFault e) {
/* 306 */       iscsiResult.error = new Exception(e.getLocalizedMessage(), e.getCause());
/* 307 */       _logger.warn("Cannot get iSCSI LUNs: " + e.getLocalizedMessage());
/*     */     } 
/*     */     
/* 310 */     if (luns != null) {
/* 311 */       byte b; int i; VsanIscsiLUN[] arrayOfVsanIscsiLUN; for (i = (arrayOfVsanIscsiLUN = luns).length, b = 0; b < i; ) { VsanIscsiLUN lun = arrayOfVsanIscsiLUN[b];
/* 312 */         if (!iscsiResult.targetToLuns.containsKey(lun.targetAlias)) {
/* 313 */           iscsiResult.targetToLuns.put(lun.targetAlias, new ArrayList<>());
/*     */         }
/*     */         
/* 316 */         ((List<VsanIscsiLUN>)iscsiResult.targetToLuns.get(lun.targetAlias)).add(lun); b++; }
/*     */     
/*     */     } 
/* 319 */     return iscsiResult;
/*     */   }
/*     */   private static class IscsiTargetsTaskResult { public VsanIscsiTarget[] targets; public Map<String, List<VsanIscsiLUN>> targetToLuns; public Exception error;
/*     */     private IscsiTargetsTaskResult() {
/* 323 */       this.targets = new VsanIscsiTarget[0];
/* 324 */       this.targetToLuns = new HashMap<>();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildHostMor(String hostStr, String vcGuid) {
/* 335 */     if (StringUtils.isEmpty(hostStr) || (hostStr.split(":")).length == 1) {
/* 336 */       return null;
/*     */     }
/* 338 */     String[] values = hostStr.split(":");
/* 339 */     String hostValue = values[values.length - 1];
/* 340 */     return "urn:vmomi:HostSystem:" + hostValue + ":" + vcGuid;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/providers/VsanIscsiTargetPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */