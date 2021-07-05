/*     */ package com.vmware.vsan.client.services.dataprotection;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.Datastore;
/*     */ import com.vmware.vim.binding.vim.host.MountInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterMgmtInternalSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsan.client.services.dataprotection.model.ClusterDpConfigData;
/*     */ import com.vmware.vsan.client.services.dataprotection.model.DatastoreData;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ @Component
/*     */ public class ClusterDpConfigService
/*     */ {
/*     */   public static final String DATASTORE_SUMMARY_PROPERTY = "summary";
/*     */   public static final String DATASTORE_SPECIFIC_TYPE_PROPERTY = "specificType";
/*     */   public static final String DATASTORE_HOST_PROPERTY = "host";
/*     */   public static final String CLUSTER_HOST_PROPRERTY = "host";
/*     */   public static final String DATASTORE_READ_WRITE_MOUNT_MODE = "readWrite";
/*  43 */   public static final DatastoreData.Type[] ARCHIVE_DP_DATASTORE_TYPES = new DatastoreData.Type[] { DatastoreData.Type.NFS_3 };
/*     */   
/*     */   private static final String VSAN_CONFIG_PROPERTY = "vsanConfigInfo";
/*     */   
/*     */   private static final String DATASTORE_PRIMARY_ICON_PROPERTY = "primaryIconId";
/*     */   
/*     */   private static final String DATASTORE_URL_PROPERTY = "summary.url";
/*     */   private static final String DATASTORE_NAME_PROPERTY = "name";
/*     */   private static final String CLUSTER_TYPE = "ClusterComputeResource";
/*     */   private static final String DATASTORE_RELATION = "datastore";
/*  53 */   private static final VsanProfiler _profiler = new VsanProfiler(ClusterDpConfigService.class);
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public DatastoreData[] getSuitableDpDatastores(ManagedObjectReference clusterRef) throws Exception {
/*  58 */     PropertyValue[] foundProperties = QueryUtil.getPropertiesForRelatedObjects(
/*  59 */         clusterRef, 
/*  60 */         "datastore", 
/*  61 */         "ClusterComputeResource", 
/*  62 */         new String[] {
/*  63 */           "primaryIconId", 
/*  64 */           "summary", 
/*  65 */           "specificType", 
/*  66 */           "host"
/*  67 */         }).getPropertyValues();
/*     */     
/*  69 */     if (foundProperties.length > 0) {
/*  70 */       return getArchiveDpDatastores(clusterRef, foundProperties).<DatastoreData>toArray(new DatastoreData[0]);
/*     */     }
/*     */     
/*  73 */     return new DatastoreData[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference remediateClusterConfiguration(ManagedObjectReference clusterRef) throws Exception {
/*  82 */     VsanClusterMgmtInternalSystem clusterMgmtInternalSystem = 
/*  83 */       VsanProviderUtils.getVsanClusterMgmtInternalSystem(clusterRef);
/*  84 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/*  91 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<DatastoreData> getArchiveDpDatastores(ManagedObjectReference clusterRef, PropertyValue[] properties) throws Exception {
/* 100 */     Map<ManagedObjectReference, List<PropertyValue>> propertiesByObject = 
/* 101 */       QueryUtil.groupPropertiesByObject(properties);
/* 102 */     List<ManagedObjectReference> vsanHosts = getVsanHosts(clusterRef);
/*     */ 
/*     */     
/* 105 */     List<DatastoreData> allDatastores = new ArrayList<>();
/* 106 */     for (ManagedObjectReference datastoreMor : propertiesByObject.keySet()) {
/* 107 */       List<PropertyValue> datastoreProperties = propertiesByObject.get(datastoreMor);
/*     */       
/* 109 */       boolean suitableForArchive = true;
/* 110 */       DatastoreData datastoreData = new DatastoreData();
/* 111 */       for (PropertyValue property : datastoreProperties) {
/* 112 */         Datastore.Summary summary; String str; switch ((str = property.propertyName).hashCode()) { case -1857640538: if (!str.equals("summary")) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/* 117 */             summary = (Datastore.Summary)property.value;
/* 118 */             datastoreData.mor = summary.datastore;
/* 119 */             datastoreData.name = summary.name;
/* 120 */             datastoreData.capacity = Long.valueOf(summary.capacity);
/* 121 */             datastoreData.freeSpace = Long.valueOf(summary.freeSpace);
/* 122 */             datastoreData.url = summary.url;
/*     */           case -1205140596:
/*     */             if (!str.equals("specificType"))
/* 125 */               continue;  datastoreData.type = DatastoreData.Type.fromString((String)property.value);
/* 126 */             if (!isDatastoreTypeSuitableForArchive(datastoreData))
/* 127 */               suitableForArchive = false; 
/*     */           case -826278890: if (!str.equals("primaryIconId"))
/*     */               continue;  datastoreData.primaryIconId = (String)property.value;
/*     */           case 3208616: if (!str.equals("host"))
/* 131 */               continue;  if (!isDatastoreMountSuitableForArchive((Datastore.HostMount[])property.value, vsanHosts)) {
/* 132 */               suitableForArchive = false;
/*     */             } }
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 138 */       if (suitableForArchive) {
/* 139 */         allDatastores.add(datastoreData);
/*     */       }
/*     */     } 
/*     */     
/* 143 */     return allDatastores;
/*     */   }
/*     */   
/*     */   private List<ManagedObjectReference> getVsanHosts(ManagedObjectReference clusterRef) throws Exception {
/* 147 */     ManagedObjectReference[] hostRefs = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/* 148 */     return Arrays.asList(hostRefs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDatastoreTypeSuitableForArchive(DatastoreData datastore) {
/* 155 */     return Arrays.<DatastoreData.Type>asList(ARCHIVE_DP_DATASTORE_TYPES).contains(datastore.type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDatastoreMountSuitableForArchive(Datastore.HostMount[] hostMounts, List<ManagedObjectReference> vsanHosts) {
/* 164 */     List<ManagedObjectReference> readWriteHosts = new ArrayList<>(); byte b; int i; Datastore.HostMount[] arrayOfHostMount;
/* 165 */     for (i = (arrayOfHostMount = hostMounts).length, b = 0; b < i; ) { Datastore.HostMount hostMount = arrayOfHostMount[b];
/* 166 */       MountInfo mountInfo = hostMount.getMountInfo();
/* 167 */       if (mountInfo.getAccessMode().equals("readWrite")) {
/* 168 */         readWriteHosts.add(hostMount.getKey());
/*     */       }
/*     */       b++; }
/*     */     
/* 172 */     return readWriteHosts.containsAll(vsanHosts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ClusterDpConfigData getCusterDpConfig(ManagedObjectReference clusterRef) throws Exception {
/* 180 */     QuerySpec dpConfigSpec = QueryUtil.buildQuerySpec(clusterRef, new String[] { "vsanConfigInfo" });
/* 181 */     ResultSet response = QueryUtil.getData(dpConfigSpec);
/* 182 */     return populateConfigData(clusterRef, response);
/*     */   }
/*     */ 
/*     */   
/*     */   private ClusterDpConfigData populateConfigData(ManagedObjectReference clusterRef, ResultSet clusterConfigData) throws Exception {
/* 187 */     ClusterDpConfigData result = new ClusterDpConfigData(); byte b; int i; ResultItem[] arrayOfResultItem;
/* 188 */     for (i = (arrayOfResultItem = clusterConfigData.items).length, b = 0; b < i; ) { ResultItem item = arrayOfResultItem[b]; byte b1; int j; PropertyValue[] arrayOfPropertyValue;
/* 189 */       for (j = (arrayOfPropertyValue = item.properties).length, b1 = 0; b1 < j; ) { PropertyValue prop = arrayOfPropertyValue[b1];
/* 190 */         if (prop.propertyName.equals("vsanConfigInfo")) {
/* 191 */           ConfigInfoEx vsanConfigEx = (ConfigInfoEx)prop.value;
/* 192 */           if (vsanConfigEx.dataProtectionConfig != null) {
/*     */ 
/*     */ 
/*     */             
/* 196 */             result.usageThreshold = vsanConfigEx.dataProtectionConfig.usageThreshold;
/*     */             
/* 198 */             if (vsanConfigEx.dataProtectionConfig.archivalTarget != null) {
/*     */ 
/*     */ 
/*     */               
/* 202 */               String datastoreUrl = vsanConfigEx.dataProtectionConfig.archivalTarget.datastoreUrl;
/* 203 */               if (StringUtils.isNotEmpty(datastoreUrl))
/* 204 */                 populateDatastoreData(result, clusterRef, datastoreUrl); 
/*     */             } 
/*     */           } 
/*     */         }  b1++; }
/*     */        b++; }
/*     */     
/* 210 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void populateDatastoreData(ClusterDpConfigData dpConfigData, ManagedObjectReference clusterRef, String datastoreUrl) throws Exception {
/* 215 */     PropertyValue[] datastoreProperties = QueryUtil.getPropertiesForRelatedObjects(
/* 216 */         clusterRef, 
/* 217 */         "datastore", "ClusterComputeResource", 
/* 218 */         new String[] { "summary.url", "name" }).getPropertyValues();
/*     */     
/* 220 */     Map<ManagedObjectReference, List<PropertyValue>> propertiesByObj = 
/* 221 */       QueryUtil.groupPropertiesByObject(datastoreProperties);
/*     */     
/* 223 */     for (List<PropertyValue> properties : propertiesByObj.values()) {
/* 224 */       boolean matchFound = false;
/* 225 */       for (PropertyValue property : properties) {
/* 226 */         String str; switch ((str = property.propertyName).hashCode()) { case -1193995481: if (!str.equals("summary.url"))
/*     */               continue; 
/* 228 */             if (property.value.equals(datastoreUrl))
/* 229 */               matchFound = true; 
/*     */           case 3373707:
/*     */             if (!str.equals("name"))
/*     */               continue; 
/* 233 */             dpConfigData.archivalDpDatastoreName = (String)property.value;
/* 234 */             dpConfigData.archivalDpDatastoreUrl = datastoreUrl;
/* 235 */             dpConfigData.archivalDpDatastoreRef = (ManagedObjectReference)property.resourceObject; }
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 240 */       if (matchFound)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/dataprotection/ClusterDpConfigService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */