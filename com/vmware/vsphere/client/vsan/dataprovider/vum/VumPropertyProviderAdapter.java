/*     */ package com.vmware.vsphere.client.vsan.dataprovider.vum;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHclInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanHealthPerspective;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.VsanVibSpec;
/*     */ import com.vmware.vise.data.ParameterSpec;
/*     */ import com.vmware.vise.data.PropertySpec;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsan.client.services.ProxygenSerializer;
/*     */ import com.vmware.vsphere.client.vsan.base.impl.VsanComponentsProvider;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VumPropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*  42 */   private static final Logger logger = LoggerFactory.getLogger(VumPropertyProviderAdapter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UPDATES = "updates";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String AVAILABLE = "vumVsanIntegrationAvailable";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String IMPORT = "vumVsanImportFirmware";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VENDOR_INSTALL = "vumVsanInstallVendorTool";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VENDOR_DOWNLOAD_AND_INSTALL = "vumVsanDownloadInstallVendorTool";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VumPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  70 */     TypeInfo clusterType = new TypeInfo();
/*  71 */     clusterType.type = ClusterComputeResource.class.getSimpleName();
/*  72 */     clusterType.properties = new String[] {
/*  73 */         "updates", 
/*  74 */         "vumVsanImportFirmware", 
/*  75 */         "vumVsanIntegrationAvailable", 
/*  76 */         "vumVsanInstallVendorTool", 
/*  77 */         "vumVsanDownloadInstallVendorTool"
/*     */       };
/*     */     
/*  80 */     registry.registerDataAdapter(this, new TypeInfo[] { clusterType });
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  85 */     ResultSet result = new ResultSet();
/*  86 */     ManagedObjectReference[] targetObjects = 
/*  87 */       Arrays.<ManagedObjectReference, Object>copyOf(propertyRequest.objects, propertyRequest.objects.length, ManagedObjectReference[].class); byte b; int i; PropertySpec[] arrayOfPropertySpec;
/*  88 */     for (i = (arrayOfPropertySpec = propertyRequest.properties).length, b = 0; b < i; ) { PropertySpec propertySpec = arrayOfPropertySpec[b]; byte b1; int j; String[] arrayOfString;
/*  89 */       for (j = (arrayOfString = propertySpec.propertyNames).length, b1 = 0; b1 < j; ) { String propertyName = arrayOfString[b1]; try {
/*     */           String str;
/*  91 */           switch ((str = propertyName).hashCode()) { case -1526316595: if (str.equals("vumVsanIntegrationAvailable")) {
/*     */                 
/*  93 */                 ArrayList<ResultItem> items = new ArrayList<>(); byte b2; int k; ManagedObjectReference[] arrayOfManagedObjectReference;
/*  94 */                 for (k = (arrayOfManagedObjectReference = targetObjects).length, b2 = 0; b2 < k; ) { ManagedObjectReference clusterRef = arrayOfManagedObjectReference[b2];
/*  95 */                   boolean available = VsanCapabilityUtils.isVsanVumIntegrationSupported(clusterRef);
/*  96 */                   items.add(QueryUtil.createResultItem("vumVsanIntegrationAvailable", Boolean.valueOf(available), clusterRef));
/*     */                   b2++; }
/*     */                 
/*  99 */                 result.items = items.<ResultItem>toArray(new ResultItem[targetObjects.length]);
/*     */                 break;
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case -1206112381:
/*     */               if (str.equals("vumVsanInstallVendorTool"))
/* 108 */               { result.items = installVib(targetObjects, propertySpec.parameters); break; } 
/*     */             case -234430262: if (str.equals("updates")) { result.items = getUpdates(targetObjects); break; } 
/*     */             case 79019995:
/* 111 */               if (str.equals("vumVsanDownloadInstallVendorTool")) { result.items = downloadAndInstallTools(targetObjects); break; } 
/*     */             case 1413649656: if (str.equals("vumVsanImportFirmware")) { result.items = importFirmware(targetObjects, propertySpec.parameters); break; } 
/*     */             default:
/* 114 */               throw new UnsupportedOperationException(); }
/*     */         
/* 116 */         } catch (Exception exception) {
/* 117 */           result.error = exception;
/*     */         }  b1++; }
/*     */       
/*     */       b++; }
/*     */     
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResultItem[] importFirmware(ManagedObjectReference[] refs, ParameterSpec[] parameterSpecs) throws Exception {
/* 129 */     if (parameterSpecs == null || parameterSpecs.length == 0) {
/* 130 */       logger.warn("Missing importFirmware parameter spec.");
/* 131 */       return new ResultItem[0];
/*     */     } 
/*     */     
/* 134 */     List<String> checksums = null; byte b; int i; ParameterSpec[] arrayOfParameterSpec;
/* 135 */     for (i = (arrayOfParameterSpec = parameterSpecs).length, b = 0; b < i; ) { ParameterSpec spec = arrayOfParameterSpec[b];
/* 136 */       if (spec.propertyName.equals("vumVsanImportFirmware")) {
/* 137 */         checksums = (List<String>)spec.parameter;
/*     */         break;
/*     */       } 
/*     */       b++; }
/*     */     
/* 142 */     if (checksums == null) {
/* 143 */       logger.warn("Unable to find supplied checksums for the update importFirmware.");
/* 144 */       return new ResultItem[0];
/*     */     } 
/*     */     
/* 147 */     ArrayList<ResultItem> result = new ArrayList<>(); ManagedObjectReference[] arrayOfManagedObjectReference;
/* 148 */     for (int j = (arrayOfManagedObjectReference = refs).length; i < j; ) { ManagedObjectReference taskRef, clusterRef = arrayOfManagedObjectReference[i];
/* 149 */       VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*     */ 
/*     */       
/* 152 */       Exception exception1 = null, exception2 = null; try { VsanProfiler.Point p = VsanComponentsProvider._profiler.point("healthSystem.downloadHclFile"); 
/* 153 */         try { taskRef = healthSystem.downloadHclFile(checksums.<String>toArray(new String[checksums.size()])); }
/* 154 */         finally { if (p != null) p.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */          }
/*     */ 
/*     */ 
/*     */       
/*     */       i++; }
/*     */ 
/*     */ 
/*     */     
/* 163 */     return result.<ResultItem>toArray(new ResultItem[refs.length]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResultItem[] installVib(ManagedObjectReference[] refs, ParameterSpec[] parameterSpecs) throws Exception {
/* 173 */     if (parameterSpecs == null || parameterSpecs.length == 0) {
/* 174 */       logger.warn("Missing vib spec parameter spec.");
/* 175 */       return new ResultItem[0];
/*     */     } 
/*     */     
/* 178 */     Map vendorVibSpec = null; byte b; int i; ParameterSpec[] arrayOfParameterSpec;
/* 179 */     for (i = (arrayOfParameterSpec = parameterSpecs).length, b = 0; b < i; ) { ParameterSpec spec = arrayOfParameterSpec[b];
/* 180 */       if (spec.propertyName.equals("vumVsanInstallVendorTool")) {
/* 181 */         vendorVibSpec = (Map)spec.parameter;
/*     */         break;
/*     */       } 
/*     */       b++; }
/*     */     
/* 186 */     if (vendorVibSpec == null) {
/* 187 */       logger.warn("Unable to find supplied vendor vib specs.");
/* 188 */       return new ResultItem[0];
/*     */     } 
/*     */     
/* 191 */     ArrayList<ResultItem> result = new ArrayList<>();
/* 192 */     Exception exception1 = null, exception2 = null;
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
/*     */   private ResultItem[] downloadAndInstallTools(ManagedObjectReference[] refs) throws Exception {
/* 220 */     ArrayList<ResultItem> result = new ArrayList<>();
/* 221 */     Exception exception1 = null, exception2 = null;
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
/*     */   private ResultItem[] getUpdates(ManagedObjectReference[] refs) throws Exception {
/* 239 */     ProxygenSerializer serializer = new ProxygenSerializer();
/*     */     
/* 241 */     ArrayList<ResultItem> result = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/* 242 */     for (i = (arrayOfManagedObjectReference = refs).length, b = 0; b < i; ) { ManagedObjectReference clusterRef = arrayOfManagedObjectReference[b];
/* 243 */       VsanClusterHclInfo hclInfo = getHclInfo(clusterRef);
/*     */       
/* 245 */       Map data = (Map)serializer.serialize(hclInfo);
/* 246 */       result.add(QueryUtil.createResultItem("updates", data, clusterRef));
/*     */       b++; }
/*     */     
/* 249 */     return result.<ResultItem>toArray(new ResultItem[refs.length]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanClusterHclInfo getHclInfo(ManagedObjectReference clusterRef) throws Exception {
/* 256 */     String perspective = VsanHealthPerspective.vsanUpgradePreCheck.toString();
/* 257 */     String[] properties = { "hclInfo" }; 
/* 258 */     try { Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */       
/*     */       try {  }
/*     */       finally
/* 264 */       { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }  }  } catch (Exception ex)
/* 265 */     { logger.error("Could not retrieve update items for the cluster", ex);
/* 266 */       throw ex; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanVibSpec toVibSpec(Map specMap) {
/* 274 */     VsanVibSpec spec = new VsanVibSpec();
/*     */     
/* 276 */     Map morefMap = (Map)specMap.get("host");
/*     */     
/* 278 */     spec.host = new ManagedObjectReference(
/* 279 */         (String)morefMap.get("type"), 
/* 280 */         (String)morefMap.get("value"), 
/* 281 */         (String)morefMap.get("serverGuid"));
/*     */     
/* 283 */     spec.metaUrl = (String)specMap.get("metaUrl");
/* 284 */     spec.metaSha1Sum = (String)specMap.get("metaSha1Sum");
/* 285 */     spec.vibUrl = (String)specMap.get("vibUrl");
/* 286 */     spec.vibSha1Sum = (String)specMap.get("vibSha1Sum");
/*     */     
/* 288 */     return spec;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/dataprovider/vum/VumPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */