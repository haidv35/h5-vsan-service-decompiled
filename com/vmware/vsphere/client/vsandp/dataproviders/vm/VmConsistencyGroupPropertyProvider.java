/*   1 */ package com.vmware.vsphere.client.vsandp.dataproviders.vm;@Component public class VmConsistencyGroupPropertyProvider { private static void ajc$preClinit() { Factory factory = new Factory("VmConsistencyGroupPropertyProvider.java", VmConsistencyGroupPropertyProvider.class); ajc$tjp_0 = factory.makeSJP("method-call", (Signature)factory.makeMethodSig("401", "queryCgInfo", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.ProtectionService", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.ProtectionService$CgInfoQuery$Spec:com.vmware.vim.vmomi.core.Future", "arg0:arg1", "", "void"), 82); ajc$tjp_1 = factory.makeSJP("method-call", (Signature)factory.makeMethodSig("401", "queryCgByObject", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery$Spec", "arg0", "com.vmware.vim.vsandp.binding.vim.vsandp.fault.VsanClusterNotFound", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery"), 119); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Object queryCgInfo_aroundBody1$advice(VmConsistencyGroupPropertyProvider ajc$this, ProtectionService target, ProtectionService.CgInfoQuery.Spec arg0, Future arg1, VsanDpTimingAspect ajc$aspectInstance, AroundClosure ajc$aroundClosure, JoinPoint.StaticPart thisJoinPointStaticPart) {
/*  23 */     long startTimeMs = System.currentTimeMillis();
/*     */     
/*  25 */     AroundClosure aroundClosure = ajc$aroundClosure; queryCgInfo_aroundBody0(ajc$this, target, arg0, arg1); Object result = null;
/*     */     
/*  27 */     long endTimeMs = System.currentTimeMillis();
/*  28 */     long execTimeMs = endTimeMs - startTimeMs;
/*     */     
/*  30 */     if (execTimeMs > 500L) {
/*  31 */       String name = thisJoinPointStaticPart.getSignature().toString();
/*  32 */       String msg = "Executing " + name + 
/*  33 */         " took too long: " + execTimeMs + " ms.";
/*  34 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().warn(msg);
/*     */     } else {
/*  36 */       String name = thisJoinPointStaticPart.getSignature().toString();
/*  37 */       String msg = "Executing " + name + " took : " + execTimeMs + " ms.";
/*  38 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().debug(msg);
/*     */     } 
/*     */     
/*  41 */     return result; } private static final Object queryCgByObject_aroundBody3$advice(VmConsistencyGroupPropertyProvider ajc$this, InventoryService target, InventoryService.CgMemberQuery.Spec arg0, VsanDpTimingAspect ajc$aspectInstance, AroundClosure ajc$aroundClosure, JoinPoint.StaticPart thisJoinPointStaticPart) { long startTimeMs = System.currentTimeMillis(); AroundClosure aroundClosure = ajc$aroundClosure; Object result = queryCgByObject_aroundBody2(ajc$this, target, arg0); long endTimeMs = System.currentTimeMillis(); long execTimeMs = endTimeMs - startTimeMs; if (execTimeMs > 500L) { String name = thisJoinPointStaticPart.getSignature().toString(); String msg = "Executing " + name + " took too long: " + execTimeMs + " ms."; VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().warn(msg); } else { String name = thisJoinPointStaticPart.getSignature().toString(); String msg = "Executing " + name + " took : " + execTimeMs + " ms."; VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().debug(msg); }  return result; } private static final Logger logger = LoggerFactory.getLogger(VmConsistencyGroupPropertyProvider.class); @Autowired private DpClient dpClient; @Autowired private VsanDpInventoryHelper inventoryHelper; @Autowired private MessageBundle messages; private static final JoinPoint.StaticPart ajc$tjp_0; private static final JoinPoint.StaticPart ajc$tjp_1; static { ajc$preClinit(); } public CgInfo getVmDataProtection(ManagedObjectReference vmRef) throws Exception { ManagedObjectReference clusterRef = this.inventoryHelper.getVmCluster(vmRef);
/*  42 */     if (clusterRef == null || !VsanCapabilityUtils.isLocalDataProtectionSupported(clusterRef)) {
/*  43 */       return null;
/*     */     }
/*     */     
/*  46 */     return getSingleCgInfoSync(vmRef, clusterRef); }
/*     */ 
/*     */   
/*     */   private CgInfo getSingleCgInfoSync(ManagedObjectReference vmRef, ManagedObjectReference clusterRef) throws Exception {
/*  50 */     ProtectionService.CgInfoQuery cgInfoResult = (ProtectionService.CgInfoQuery)getCgInfo(vmRef, clusterRef).get();
/*  51 */     if (cgInfoResult.getResult() == null || (cgInfoResult.getResult()).length < 1) {
/*  52 */       logger.error("Incorrect result was received when CgInfo was queried: {}", cgInfoResult);
/*  53 */       throw new Exception(this.messages.string("dataproviders.vm.cg.cgInfoQueryFault"));
/*     */     } 
/*     */     
/*  56 */     return cgInfoResult.getResult()[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ProtectionService.CgInfoQuery> getCgInfo(ManagedObjectReference vmRef, ManagedObjectReference clusterRef) throws Exception {
/*  63 */     InventoryService.CgMemberQuery cgBasicInfoResult = queryCgByObject(vmRef, clusterRef);
/*  64 */     if (cgBasicInfoResult == null)
/*     */     {
/*  66 */       return (Future<ProtectionService.CgInfoQuery>)new BlockingFuture<ProtectionService.CgInfoQuery>()
/*     */         {
/*     */           public ProtectionService.CgInfoQuery get() throws InterruptedException, ExecutionException {
/*  69 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*  74 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/*  84 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } private static final void queryCgInfo_aroundBody0(VmConsistencyGroupPropertyProvider paramVmConsistencyGroupPropertyProvider, ProtectionService paramProtectionService, ProtectionService.CgInfoQuery.Spec paramSpec, Future paramFuture) { paramProtectionService.queryCgInfo(paramSpec, paramFuture); } private String getVmCgId(InventoryService.CgMemberQuery cgBasicInfoResult, ManagedObjectReference vmRef) throws Exception {
/*  88 */     if (cgBasicInfoResult.getError() != null && (cgBasicInfoResult.getError()).length > 0) {
/*  89 */       logger.error("Unable to find consistency groups for VM {} due to:\n", 
/*  90 */           vmRef, (cgBasicInfoResult.getError()[0]).fault.getMessage());
/*     */       
/*  92 */       throw (cgBasicInfoResult.getError()[0]).fault;
/*     */     } 
/*     */     
/*  95 */     if (cgBasicInfoResult.getResult() == null || (cgBasicInfoResult.getResult()).length < 1 || 
/*  96 */       cgBasicInfoResult.getResult()[0].getCg() == null) {
/*  97 */       logger.error("Incorrect result was received when CgInfo was queried: {}", cgBasicInfoResult);
/*  98 */       throw new Exception(this.messages.string("dataproviders.vm.cg.cgInfoQueryFault"));
/*     */     } 
/*     */     
/* 101 */     logger.debug("Found {} consistency groups for VM {}", Integer.valueOf((cgBasicInfoResult.getResult()).length), vmRef);
/*     */ 
/*     */     
/* 104 */     return cgBasicInfoResult.getResult()[0].getCg().getKey();
/*     */   }
/*     */   
/*     */   private InventoryService.CgMemberQuery queryCgByObject(ManagedObjectReference vmRef, ManagedObjectReference clusterRef) throws Exception {
/* 108 */     String vmStorageObjectId = getVmStorageObjectId(vmRef);
/* 109 */     if (vmStorageObjectId == null)
/*     */     {
/* 111 */       return null;
/*     */     }
/*     */     
/* 114 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 120 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     }  } private static final InventoryService.CgMemberQuery queryCgByObject_aroundBody2(VmConsistencyGroupPropertyProvider paramVmConsistencyGroupPropertyProvider, InventoryService paramInventoryService, InventoryService.CgMemberQuery.Spec paramSpec) {
/*     */     return paramInventoryService.queryCgByObject(paramSpec);
/*     */   } private String getVmStorageObjectId(ManagedObjectReference vmRef) throws Exception {
/*     */     try {
/* 126 */       return (String)QueryUtil.getProperty(vmRef, "config.vmStorageObjectId", null);
/* 127 */     } catch (Exception e) {
/* 128 */       logger.error("Unable to determine the VM's storage object ID for {}", vmRef);
/* 129 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtectionService.InstanceQuery.Result.SeriesEntry[] getArchivalSeries(ManagedObjectReference clusterRef, String cgKey) throws Exception {
/* 139 */     ProtectionService.InstanceQuery query = (ProtectionService.InstanceQuery)getArchivalSeriesAsync(clusterRef, cgKey).get();
/*     */     
/* 141 */     if (query.getResult() == null || (query.getResult()).length < 1) {
/* 142 */       logger.error("Incorrect result was received when archival pits were queried: {}", query);
/* 143 */       throw new Exception(this.messages.string("dataproviders.vm.queryArchivalInstancesFault"));
/*     */     } 
/*     */     
/* 146 */     return query.getResult()[0].getSeries();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ProtectionService.InstanceQuery> getArchivalSeriesAsync(ManagedObjectReference clusterRef, String cgKey) throws Exception {
/* 155 */     Exception exception1 = null, exception2 = null;
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
/*     */   private ProtectionService.InstanceQuery.Spec getArchivalQueryInstanceSpec(ManagedObjectReference clusterRef, String cgInfoKey) {
/* 169 */     ProtectionService.TargetFilterSpec targetFilterSpec = new ProtectionService.TargetFilterSpec();
/* 170 */     targetFilterSpec.setArchiveRequested(true);
/*     */     
/* 172 */     InstanceQuerySpecBase.InstanceSet instanceSet = new InstanceQuerySpecBase.InstanceSet();
/*     */     
/* 174 */     ProtectionService.InstanceQuery.Spec.Entry entry = new ProtectionService.InstanceQuery.Spec.Entry();
/* 175 */     entry.setCg(cgInfoKey);
/* 176 */     entry.setTargetFilter(targetFilterSpec);
/* 177 */     entry.setInstancesSpec(instanceSet);
/*     */     
/* 179 */     ProtectionService.InstanceQuery.Spec spec = new ProtectionService.InstanceQuery.Spec();
/* 180 */     spec.setCluster(clusterRef);
/* 181 */     spec.setEntry(new ProtectionService.InstanceQuery.Spec.Entry[] { entry });
/*     */     
/* 183 */     return spec;
/*     */   } }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/dataproviders/vm/VmConsistencyGroupPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */