/*     */ package com.vmware.vsphere.client.vsan.perf;
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfDiagnoseQuerySpec;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfDiagnosticException;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfDiagnosticResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityType;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerformanceManager;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.DiagnosticException;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.EntityRefData;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerfDiagnosticQuerySpec;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerformanceDiagnosticData;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerformanceDiagnosticException;
/*     */ import com.vmware.vsphere.client.vsan.perf.model.PerformanceExceptionsData;
/*     */ import java.text.DateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class PerformanceDiagnosticsPropertyProvider {
/*  30 */   private static final Log _logger = LogFactory.getLog(PerformanceDiagnosticsPropertyProvider.class);
/*     */   
/*  32 */   private static final VsanProfiler _profiler = new VsanProfiler(
/*  33 */       PerformanceDiagnosticsPropertyProvider.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NAME_PROPERTY = "name";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getPerfAnalysisSupported(ManagedObjectReference clusterRef) throws Exception {
/*  47 */     return VsanCapabilityUtils.isPerfAnalysisSupportedOnVc(clusterRef);
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
/*     */   public PerformanceExceptionsData getPerformanceExceptionsData(ManagedObjectReference clusterRef) throws Exception {
/*  63 */     VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(clusterRef);
/*  64 */     VsanPerfDiagnosticException[] exceptions = getExceptions(perfMgr, clusterRef);
/*     */     
/*  66 */     Map<String, PerformanceDiagnosticException> idToExceptionMap = new HashMap<>();
/*  67 */     if (exceptions != null) {
/*  68 */       byte b; int i; VsanPerfDiagnosticException[] arrayOfVsanPerfDiagnosticException; for (i = (arrayOfVsanPerfDiagnosticException = exceptions).length, b = 0; b < i; ) { VsanPerfDiagnosticException ex = arrayOfVsanPerfDiagnosticException[b];
/*  69 */         idToExceptionMap.put(ex.exceptionId, 
/*  70 */             new PerformanceDiagnosticException(ex.exceptionMessage, 
/*  71 */               ex.exceptionDetails, 
/*  72 */               ex.exceptionUrl));
/*     */         b++; }
/*     */     
/*     */     } 
/*  76 */     PerformanceExceptionsData exceptionsData = new PerformanceExceptionsData();
/*  77 */     exceptionsData.performanceExceptionIdToException = idToExceptionMap;
/*  78 */     EntityTypes types = getVsanPerfEntityTypes(perfMgr);
/*  79 */     exceptionsData.performanceEntityTypes = types.simpleTypes;
/*  80 */     exceptionsData.performanceAggregatedEntityTypes = types.aggregatedTypes;
/*     */     
/*  82 */     return exceptionsData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanPerfDiagnosticException[] getExceptions(VsanPerformanceManager perfMgr, ManagedObjectReference clusterRef) throws Exception {
/*  89 */     VsanPerfDiagnosticException[] exceptions = null;
/*  90 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public PerformanceDiagnosticData getPerformanceDiagnosticData(ManagedObjectReference clusterRef, PerfDiagnosticQuerySpec spec) throws Exception {
/* 110 */     VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(clusterRef);
/*     */     
/* 112 */     VsanPerfDiagnosticResult[] perfDiagnosticResults = 
/* 113 */       getDiagnosticResults(perfMgr, spec, clusterRef);
/* 114 */     int totalPerfDiagIssues = (perfDiagnosticResults == null) ? 0 : perfDiagnosticResults.length;
/* 115 */     _logger.info("Total received number of performance issues is " + totalPerfDiagIssues);
/*     */     
/* 117 */     if (perfDiagnosticResults == null || perfDiagnosticResults.length == 0) {
/* 118 */       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
/* 119 */       _logger.info(String.format(
/* 120 */             "No performance issues were detected for the period between %s and %s for perspective: %s", new Object[] {
/* 121 */               dateFormat.format(spec.startTime.getTime()), 
/* 122 */               dateFormat.format(spec.endTime.getTime()), 
/* 123 */               spec.queryType.toString() }));
/* 124 */       return new PerformanceDiagnosticData();
/*     */     } 
/*     */     
/* 127 */     List<DiagnosticException> issues = getIssues(perfDiagnosticResults);
/*     */     
/* 129 */     PerformanceDiagnosticData result = 
/* 130 */       new PerformanceDiagnosticData(issues, getAllEntityRefIds(perfDiagnosticResults));
/* 131 */     return result;
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
/*     */   public PerformanceEntitiesData getPerfEntitiesInfo(ManagedObjectReference clusterRef, List<String> entityRefIds) throws Exception {
/* 145 */     VsanPerformanceManager perfMgr = VsanProviderUtils.getVsanPerformanceManager(clusterRef);
/*     */     
/* 147 */     Map<String, EntityRefData> entityRefIdToEntityRefDataMap = 
/* 148 */       getEntityRefIdToEntityRefDataMap(entityRefIds.<String>toArray(new String[entityRefIds.size()]), perfMgr, clusterRef);
/* 149 */     return new PerformanceEntitiesData(entityRefIdToEntityRefDataMap);
/*     */   }
/*     */   
/*     */   private List<DiagnosticException> getIssues(VsanPerfDiagnosticResult[] perfDiagnosticResults) {
/* 153 */     List<DiagnosticException> issues = new ArrayList<>();
/*     */     
/* 155 */     Map<String, DiagnosticException> idToExceptionMap = new HashMap<>(); byte b; int i; VsanPerfDiagnosticResult[] arrayOfVsanPerfDiagnosticResult;
/* 156 */     for (i = (arrayOfVsanPerfDiagnosticResult = perfDiagnosticResults).length, b = 0; b < i; ) { VsanPerfDiagnosticResult diagnosticResult = arrayOfVsanPerfDiagnosticResult[b];
/* 157 */       _logger.info(String.format("Preparing perf diag issue for exceptionId: %s, and recommendation: %s", new Object[] {
/* 158 */               diagnosticResult.exceptionId, diagnosticResult.recommendation
/*     */             }));
/* 160 */       DiagnosticException diagEx = idToExceptionMap.get(diagnosticResult.exceptionId);
/* 161 */       if (diagEx == null) {
/* 162 */         diagEx = new DiagnosticException(diagnosticResult.exceptionId);
/* 163 */         idToExceptionMap.put(diagEx.exceptionId, diagEx);
/* 164 */         issues.add(diagEx);
/*     */       } 
/* 166 */       diagEx.addEntities(diagnosticResult);
/*     */       b++; }
/*     */     
/* 169 */     return issues;
/*     */   }
/*     */   
/*     */   private List<String> getAllEntityRefIds(VsanPerfDiagnosticResult[] perfDiagnosticResults) {
/* 173 */     List<String> entityRefIds = new ArrayList<>(); byte b; int i; VsanPerfDiagnosticResult[] arrayOfVsanPerfDiagnosticResult;
/* 174 */     for (i = (arrayOfVsanPerfDiagnosticResult = perfDiagnosticResults).length, b = 0; b < i; ) { VsanPerfDiagnosticResult diagResult = arrayOfVsanPerfDiagnosticResult[b];
/* 175 */       if (!ArrayUtils.isEmpty((Object[])diagResult.exceptionData)) {
/*     */         byte b1; int j;
/*     */         VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/* 178 */         for (j = (arrayOfVsanPerfEntityMetricCSV = diagResult.exceptionData).length, b1 = 0; b1 < j; ) { VsanPerfEntityMetricCSV metricCsv = arrayOfVsanPerfEntityMetricCSV[b1];
/* 179 */           if (!entityRefIds.contains(metricCsv.entityRefId))
/* 180 */             entityRefIds.add(metricCsv.entityRefId);  b1++; }
/*     */       
/*     */       }  b++; }
/*     */     
/* 184 */     return entityRefIds;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VsanPerfDiagnosticResult[] getDiagnosticResults(VsanPerformanceManager perfMgr, PerfDiagnosticQuerySpec spec, ManagedObjectReference clusterRef) throws Exception {
/* 189 */     VsanPerfDiagnoseQuerySpec querySpec = new VsanPerfDiagnoseQuerySpec();
/* 190 */     querySpec.queryType = spec.queryType.toString();
/* 191 */     querySpec.startTime = spec.startTime;
/* 192 */     BaseUtils.setUTCTimeZone(querySpec.startTime);
/* 193 */     querySpec.endTime = spec.endTime;
/* 194 */     BaseUtils.setUTCTimeZone(querySpec.endTime);
/*     */     
/* 196 */     VsanPerfDiagnosticResult[] perfDiagnosticResults = null;
/*     */     try {
/* 198 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 203 */     catch (NotFound notFound) {
/* 204 */       _logger.info("There is no diagnostic data in the selected time period.", (Throwable)notFound);
/* 205 */     } catch (Exception ex) {
/* 206 */       _logger.error("Could not retrieve performance diagnostic issues", ex);
/* 207 */       throw ex;
/*     */     } 
/*     */     
/* 210 */     return perfDiagnosticResults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, EntityRefData> getEntityRefIdToEntityRefDataMap(String[] entityRefIds, VsanPerformanceManager perfMgr, ManagedObjectReference clusterRef) throws Exception {
/* 217 */     Map<String, EntityRefData> result = new HashMap<>();
/* 218 */     if (ArrayUtils.isEmpty((Object[])entityRefIds)) {
/* 219 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 223 */     VsanPerfEntityInfo[] entityInfos = 
/* 224 */       perfMgr.getVcMoRefFromPerfEntityRefId(clusterRef, entityRefIds);
/*     */     
/* 226 */     if (entityInfos != null) {
/* 227 */       byte b; int i; VsanPerfEntityInfo[] arrayOfVsanPerfEntityInfo; for (i = (arrayOfVsanPerfEntityInfo = entityInfos).length, b = 0; b < i; ) { VsanPerfEntityInfo entityInfo = arrayOfVsanPerfEntityInfo[b];
/* 228 */         EntityRefData refData = new EntityRefData(entityInfo, clusterRef);
/* 229 */         if (refData.managedObjectRef == null)
/*     */         {
/* 231 */           _logger.info(String.format("Skipping entity with entityRefId %s as missing", new Object[] { entityInfo.entityRefId }));
/*     */         }
/*     */         
/* 234 */         result.put(entityInfo.entityRefId, refData);
/*     */         b++; }
/*     */     
/*     */     } 
/* 238 */     if (!result.isEmpty()) {
/*     */ 
/*     */       
/* 241 */       Map<ManagedObjectReference, String> refToNameMap = getMoRefToNameMap(result.values());
/*     */       
/* 243 */       for (EntityRefData refData : result.values()) {
/* 244 */         refData.managedObjectName = refToNameMap.get(refData.managedObjectRef);
/*     */       }
/*     */     } 
/*     */     
/* 248 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<ManagedObjectReference, String> getMoRefToNameMap(Collection<EntityRefData> refDatas) throws Exception {
/* 253 */     Set<ManagedObjectReference> mos = new HashSet<>();
/* 254 */     for (EntityRefData refData : refDatas) {
/* 255 */       if (refData.isEntityMissing) {
/*     */         continue;
/*     */       }
/* 258 */       mos.add(refData.managedObjectRef);
/*     */     } 
/* 260 */     Map<ManagedObjectReference, String> refToNameMap = new HashMap<>();
/*     */     
/* 262 */     if (mos.size() > 0) {
/* 263 */       PropertyValue[] propValues = QueryUtil.getProperties(mos.toArray(), new String[] { "name"
/* 264 */           }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 265 */       for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 266 */         refToNameMap.put((ManagedObjectReference)propValue.resourceObject, 
/* 267 */             (String)propValue.value); b++; }
/*     */     
/*     */     } 
/* 270 */     return refToNameMap;
/*     */   }
/*     */   
/*     */   private EntityTypes getVsanPerfEntityTypes(VsanPerformanceManager performanceManager) throws Exception {
/* 274 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 282 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } private Future<VsanPerfEntityType[]> getEntityTypesFuture(VsanPerformanceManager performanceManager, Measure measure) {
/* 286 */     Future<VsanPerfEntityType[]> future = measure.newFuture("VsanPerformanceManager.getSupportedEntityTypes");
/* 287 */     performanceManager.getSupportedEntityTypes(future);
/* 288 */     return future;
/*     */   }
/*     */   
/*     */   private Future<VsanPerfEntityType[]> getAggregatedEntityTypesFuture(VsanPerformanceManager perfMgr, Measure measure) {
/* 292 */     Future<VsanPerfEntityType[]> future = measure.newFuture("VsanPerformanceManager.getAggregatedEntityTypes");
/* 293 */     perfMgr.getAggregatedEntityTypes(future);
/* 294 */     return future;
/*     */   }
/*     */   
/*     */   private Map<String, VsanPerfEntityType> getEntityTypesFromFuture(Future<VsanPerfEntityType[]> future) {
/* 298 */     Map<String, VsanPerfEntityType> entityTypeMap = new HashMap<>();
/*     */     
/*     */     try {
/* 301 */       VsanPerfEntityType[] entityTypes = (VsanPerfEntityType[])future.get();
/* 302 */       entityTypeMap = createNameToTypeMap(entityTypes);
/* 303 */     } catch (Exception ex) {
/*     */       
/* 305 */       _logger.error("Cannot load supported entity types: ", ex);
/*     */     } 
/*     */     
/* 308 */     return entityTypeMap;
/*     */   }
/*     */   
/*     */   private static Map<String, VsanPerfEntityType> createNameToTypeMap(VsanPerfEntityType[] types) {
/* 312 */     Map<String, VsanPerfEntityType> map = new HashMap<>();
/* 313 */     if (types != null) {
/* 314 */       byte b; int i; VsanPerfEntityType[] arrayOfVsanPerfEntityType; for (i = (arrayOfVsanPerfEntityType = types).length, b = 0; b < i; ) { VsanPerfEntityType perfEntityType = arrayOfVsanPerfEntityType[b];
/* 315 */         map.put(perfEntityType.name, perfEntityType); b++; }
/*     */     
/*     */     } 
/* 318 */     return map;
/*     */   }
/*     */   private static class EntityTypes { Map<String, VsanPerfEntityType> simpleTypes;
/*     */     Map<String, VsanPerfEntityType> aggregatedTypes;
/*     */     
/*     */     public EntityTypes(Map<String, VsanPerfEntityType> simpleTypes, Map<String, VsanPerfEntityType> aggregatedTyped) {
/* 324 */       this.simpleTypes = simpleTypes;
/* 325 */       this.aggregatedTypes = aggregatedTyped;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/PerformanceDiagnosticsPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */