/*     */ package com.vmware.vsan.client.services.configurecluster;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.services.common.data.ConnectionState;
/*     */ import com.vmware.vsan.client.services.diskmanagement.DiskManagementService;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsan.client.util.NoOpMeasure;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanConfigSpec;
/*     */ import com.vmware.vsphere.client.vsan.impl.ConfigureVsanClusterMutationProvider;
/*     */ import com.vmware.vsphere.client.vsan.stretched.VsanStretchedClusterPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ConfigureClusterService
/*     */ {
/*  38 */   private static final Logger logger = LoggerFactory.getLogger(ConfigureClusterService.class);
/*     */   
/*     */   private static final String HOST_RELATION = "host";
/*  41 */   private static final String CLUSTER_TYPE = ClusterComputeResource.class.getSimpleName();
/*     */   
/*     */   private static final String HOST_NAME_PROPERTY = "name";
/*     */   
/*     */   private static final String HOST_PRIMARY_ICON_PROPERTY = "primaryIconId";
/*     */   
/*     */   private static final String HOSTS_NUMBER_PROPERTY = "host._length";
/*     */   
/*     */   private static final String HOST_CONNECTION_STATE_PROPERTY = "runtime.connectionState";
/*     */   
/*     */   private static final String HOST_VERSION_PROPERTY = "config.product.version";
/*     */   private static final String FAULT_DOMAIN_NAME_PROPERTY = "config.vsanHostConfig.faultDomainInfo.name";
/*     */   private static final String SEMI_AUTO_CLAIMING_DISK_DATA_PROPERTY = "vsanSemiAutoClaimDisksData";
/*     */   private static final String HA_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.enabled";
/*     */   private static final String DPM_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dpmConfigInfo.enabled";
/*     */   @Autowired
/*     */   private VsanStretchedClusterPropertyProvider stretchedClusterProvider;
/*     */   @Autowired
/*     */   private ObjectReferenceService refService;
/*     */   @Autowired
/*     */   private ConfigureVsanClusterMutationProvider mutationProvider;
/*     */   @Autowired
/*     */   private DiskManagementService diskMgmtService;
/*     */   
/*     */   @TsService
/*     */   public List<HostFaultDomainData> getClusterHostFaultDomainData(ManagedObjectReference clusterRef) throws Exception {
/*  67 */     PropertyValue[] props = QueryUtil.getPropertiesForRelatedObjects(
/*  68 */         clusterRef, 
/*  69 */         "host", 
/*  70 */         CLUSTER_TYPE, 
/*  71 */         new String[] {
/*  72 */           "name", 
/*  73 */           "primaryIconId", 
/*  74 */           "runtime.connectionState", 
/*  75 */           "config.product.version", 
/*  76 */           "config.vsanHostConfig.faultDomainInfo.name"
/*  77 */         }).getPropertyValues();
/*  78 */     Map<ManagedObjectReference, List<PropertyValue>> propsMap = QueryUtil.groupPropertiesByObject(props);
/*     */     
/*  80 */     List<HostFaultDomainData> result = new ArrayList<>();
/*  81 */     for (ManagedObjectReference mor : propsMap.keySet()) {
/*  82 */       List<PropertyValue> objectProps = propsMap.get(mor);
/*     */       
/*  84 */       HostFaultDomainData.Builder builder = new HostFaultDomainData.Builder();
/*  85 */       builder.hostUid(this.refService.getUid(mor));
/*  86 */       for (PropertyValue property : objectProps)
/*  87 */       { ConnectionState state; String str; switch ((str = property.propertyName).hashCode()) { case -826278890: if (!str.equals("primaryIconId")) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */             
/*  92 */             builder.primaryIconId((String)property.value);
/*     */           case 3373707:
/*     */             if (!str.equals("name")) {
/*     */               continue;
/*     */             }
/*     */             builder.name((String)property.value);
/*     */           case 707737491:
/*     */             if (!str.equals("config.vsanHostConfig.faultDomainInfo.name")) {
/*     */               continue;
/*     */             }
/* 102 */             builder.faultDomainName((String)property.value);
/*     */           case 1445673005: if (!str.equals("config.product.version"))
/*     */               continue;  builder.version((String)property.value);
/*     */           case 2004020797:
/*     */             if (!str.equals("runtime.connectionState"))
/* 107 */               continue;  state = ConnectionState.valueOf(((HostSystem.ConnectionState)property.value).name()); builder.connectionState(state); }  }  result.add(builder.createHostFaultDomainData());
/*     */     } 
/*     */     
/* 110 */     return result;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public String getPrerequisitesWarning(ManagedObjectReference clusterRef) throws Exception {
/* 115 */     Map<Object, Map<String, Object>> result = QueryUtil.getProperties(
/* 116 */         clusterRef, new String[] { "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.enabled", "configurationEx[@type='ClusterConfigInfoEx'].dpmConfigInfo.enabled" }).getMap();
/* 117 */     Map<String, Object> properties = result.get(clusterRef);
/* 118 */     boolean haEnabled = Boolean.valueOf((String)properties.get("configurationEx[@type='ClusterConfigInfoEx'].dasConfig.enabled")).booleanValue();
/* 119 */     boolean dpmEnabled = Boolean.valueOf((String)properties.get("configurationEx[@type='ClusterConfigInfoEx'].dpmConfigInfo.enabled")).booleanValue();
/*     */     
/* 121 */     if (haEnabled && dpmEnabled)
/* 122 */       return Utils.getLocalizedString("vsan.generalConfig.haAndDpm.enabled.warning"); 
/* 123 */     if (haEnabled)
/* 124 */       return Utils.getLocalizedString("vsan.generalConfig.ha.enabled.warning"); 
/* 125 */     if (dpmEnabled) {
/* 126 */       return Utils.getLocalizedString("vsan.generalConfig.dpm.enabled.warning");
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean getStretchClusterSupported(ManagedObjectReference clusterRef) throws Exception {
/* 134 */     return this.stretchedClusterProvider.getIsStretchedClusterSupported(clusterRef);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public int getHostsInCluster(ManagedObjectReference clusterRef) throws Exception {
/* 139 */     Number clusterHosts = null;
/*     */     try {
/* 141 */       clusterHosts = (Number)QueryUtil.getProperty(clusterRef, "host._length", null);
/* 142 */     } catch (Exception e) {
/* 143 */       logger.warn("Failed to get host count for cluster: " + clusterRef, e);
/*     */     } 
/* 145 */     if (clusterHosts != null) {
/* 146 */       return clusterHosts.intValue();
/*     */     }
/* 148 */     return 0;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean hasHybridDiskGroups(ManagedObjectReference clusterRef) {
/*     */     try {
/* 154 */       ManagedObjectReference[] hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/* 155 */       Map<ManagedObjectReference, Future<DiskMapInfoEx[]>> diskGroupsFutureByHost = 
/* 156 */         this.diskMgmtService.getDiskMappingsAsync(Arrays.asList(hosts), (Measure)new NoOpMeasure());
/* 157 */       for (ManagedObjectReference hostRef : diskGroupsFutureByHost.keySet()) {
/* 158 */         DiskMapInfoEx[] diskGroups = (DiskMapInfoEx[])((Future)diskGroupsFutureByHost.get(hostRef)).get();
/* 159 */         if (diskGroups != null) {
/* 160 */           byte b; int i; DiskMapInfoEx[] arrayOfDiskMapInfoEx; for (i = (arrayOfDiskMapInfoEx = diskGroups).length, b = 0; b < i; ) { DiskMapInfoEx info = arrayOfDiskMapInfoEx[b];
/* 161 */             if (!info.isAllFlash)
/* 162 */               return true; 
/*     */             b++; }
/*     */         
/*     */         } 
/*     */       } 
/* 167 */     } catch (Exception exception) {
/* 168 */       logger.warn("Failed to check disk groups for cluster: " + clusterRef);
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService("configureClusterTask")
/*     */   public ManagedObjectReference configureCluster(ManagedObjectReference clusterRef, VsanConfigSpec configSpec) throws Exception {
/* 177 */     return this.mutationProvider.configure(clusterRef, configSpec);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/configurecluster/ConfigureClusterService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */