/*     */ package com.vmware.vsan.client.services.stretchedcluster;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.ComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.core.Future;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VSANWitnessHostInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.services.diskGroups.data.VsanDiskMapping;
/*     */ import com.vmware.vsan.client.services.diskmanagement.DiskManagementService;
/*     */ import com.vmware.vsan.client.util.Measure;
/*     */ import com.vmware.vsan.client.util.NoOpMeasure;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.stretched.VsanStretchedClusterConfig;
/*     */ import com.vmware.vsphere.client.vsan.stretched.VsanStretchedClusterMutationProvider;
/*     */ import com.vmware.vsphere.client.vsan.stretched.VsanWitnessConfig;
/*     */ import com.vmware.vsphere.client.vsan.stretched.WitnessHostSpec;
/*     */ import com.vmware.vsphere.client.vsan.stretched.WitnessHostValidationResult;
/*     */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @Component
/*     */ public class ConfigureStretchedClusterService
/*     */ {
/*  49 */   private static final Log logger = LogFactory.getLog(ConfigureStretchedClusterService.class);
/*     */   
/*  51 */   private static final String[] DOMAIN_PROPERTIES = new String[] {
/*  52 */       "name", 
/*  53 */       "isWitnessHost", 
/*  54 */       "config.vsanHostConfig.faultDomainInfo.name", 
/*  55 */       "primaryIconId", 
/*  56 */       "runtime.inMaintenanceMode", 
/*  57 */       "preferredFaultDomain"
/*     */     };
/*     */   
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   
/*     */   @Autowired
/*     */   private ObjectReferenceService refService;
/*     */   
/*     */   @Autowired
/*     */   private DiskManagementService diskMgmtService;
/*     */   
/*     */   @Autowired
/*     */   private VsanStretchedClusterMutationProvider stretchedClusterMutationProvider;
/*     */   
/*     */   @TsService
/*     */   public List<DomainOrHostData> getAvailableDomains(ManagedObjectReference clusterRef) throws Exception {
/*  74 */     DataServiceResponse response = QueryUtil.getPropertiesForRelatedObjects(
/*  75 */         clusterRef, 
/*  76 */         "allVsanHosts", 
/*  77 */         ClusterComputeResource.class.getSimpleName(), 
/*  78 */         DOMAIN_PROPERTIES);
/*     */     
/*  80 */     List<DomainOrHostData> result = new ArrayList<>();
/*     */     
/*  82 */     String preferredFaultDomainName = null;
/*  83 */     Map<String, List<DomainOrHostData>> map = new HashMap<>();
/*  84 */     for (Object hostRef : response.getResourceObjects()) {
/*     */       
/*  86 */       String name = (String)response.getProperty(hostRef, "name");
/*  87 */       boolean isWitnessHost = Boolean.valueOf((String)response.getProperty(hostRef, "isWitnessHost")).booleanValue();
/*  88 */       String domainName = (String)response.getProperty(hostRef, "config.vsanHostConfig.faultDomainInfo.name");
/*  89 */       String iconId = (String)response.getProperty(hostRef, "primaryIconId");
/*  90 */       String hostUid = this.refService.getUid(hostRef);
/*  91 */       boolean maintenanceMode = ((Boolean)response.getProperty(hostRef, "runtime.inMaintenanceMode")).booleanValue();
/*  92 */       String hostPreferredFaultDomainName = 
/*  93 */         (String)response.getProperty(hostRef, "preferredFaultDomain");
/*     */       
/*  95 */       if (isWitnessHost) {
/*     */         
/*  97 */         preferredFaultDomainName = hostPreferredFaultDomainName;
/*     */         
/*     */         continue;
/*     */       } 
/* 101 */       if (domainName != null && domainName.length() == 0) {
/* 102 */         domainName = null;
/*     */       }
/*     */       
/* 105 */       if (domainName != null) {
/* 106 */         DomainOrHostData hostData = DomainOrHostData.createHostData(hostUid, name, iconId, maintenanceMode);
/*     */         
/* 108 */         List<DomainOrHostData> addTo = map.get(domainName);
/* 109 */         if (addTo == null) {
/* 110 */           addTo = new ArrayList<>();
/* 111 */           map.put(domainName, addTo);
/*     */         } 
/* 113 */         addTo.add(hostData); continue;
/*     */       } 
/* 115 */       result.add(DomainOrHostData.createHostData(hostUid, name, iconId, maintenanceMode));
/*     */     } 
/*     */ 
/*     */     
/* 119 */     for (String domainName : map.keySet()) {
/* 120 */       result.add(DomainOrHostData.createDomainData(
/* 121 */             domainName, 
/* 122 */             domainName, 
/* 123 */             domainName.equals(preferredFaultDomainName), 
/* 124 */             map.get(domainName)));
/*     */     }
/*     */     
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public boolean hasDiskGroups(ManagedObjectReference witnessHost) {
/*     */     try {
/* 133 */       Map<ManagedObjectReference, Future<DiskMapInfoEx[]>> diskGroupsFutureByHost = 
/* 134 */         this.diskMgmtService.getDiskMappingsAsync(Arrays.asList(new ManagedObjectReference[] { witnessHost }, ), (Measure)new NoOpMeasure());
/* 135 */       DiskMapInfoEx[] diskGroups = (DiskMapInfoEx[])((Future)diskGroupsFutureByHost.get(witnessHost)).get();
/* 136 */       return !ArrayUtils.isEmpty((Object[])diskGroups);
/* 137 */     } catch (Exception exception) {
/* 138 */       logger.warn("Failed to check disk groups for host: " + witnessHost);
/*     */       
/* 140 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @TsService("validateWitnessHost")
/*     */   public WitnessHostValidationResult getWitnessHostValidationError(ManagedObjectReference clusterRef, ManagedObjectReference witnessHost) throws Exception {
/* 146 */     WitnessHostSpec validationSpec = new WitnessHostSpec();
/* 147 */     validationSpec.witnessHost = getWitnessHostRef(witnessHost);
/* 148 */     return this.stretchedClusterMutationProvider.validateWitnessHost(clusterRef, validationSpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService("configureStretchedClusterTask")
/*     */   public ManagedObjectReference configureStretchedCluster(ManagedObjectReference clusterRef, String preferredName, DomainOrHostData[] preferredDomains, String secondaryName, DomainOrHostData[] secondaryDomains, ManagedObjectReference witnessHost, VsanDiskMapping witnessHostDiskMapping) throws Exception {
/* 160 */     VsanStretchedClusterConfig spec = new VsanStretchedClusterConfig();
/* 161 */     spec.isFaultDomainConfigurationChanged = !(
/* 162 */       !isDomainConfigChanged(preferredName, preferredDomains) && 
/* 163 */       !isDomainConfigChanged(secondaryName, secondaryDomains));
/*     */     
/* 165 */     spec.preferredSiteName = preferredName;
/* 166 */     spec.preferredSiteHosts = new ArrayList(); byte b; int i; DomainOrHostData[] arrayOfDomainOrHostData;
/* 167 */     for (i = (arrayOfDomainOrHostData = preferredDomains).length, b = 0; b < i; ) { DomainOrHostData domain = arrayOfDomainOrHostData[b];
/* 168 */       if (domain.isHost) {
/* 169 */         spec.preferredSiteHosts.add((ManagedObjectReference)this.refService.getReference(domain.uid));
/*     */       } else {
/* 171 */         byte b1; int j; DomainOrHostData[] arrayOfDomainOrHostData1; for (j = (arrayOfDomainOrHostData1 = domain.children).length, b1 = 0; b1 < j; ) { DomainOrHostData host = arrayOfDomainOrHostData1[b1];
/* 172 */           spec.preferredSiteHosts.add((ManagedObjectReference)this.refService.getReference(host.uid)); b1++; }
/*     */       
/*     */       } 
/*     */       b++; }
/*     */     
/* 177 */     spec.secondarySiteName = secondaryName;
/* 178 */     spec.secondarySiteHosts = new ArrayList();
/* 179 */     for (i = (arrayOfDomainOrHostData = secondaryDomains).length, b = 0; b < i; ) { DomainOrHostData domain = arrayOfDomainOrHostData[b];
/* 180 */       if (domain.isHost) {
/* 181 */         spec.secondarySiteHosts.add((ManagedObjectReference)this.refService.getReference(domain.uid));
/*     */       } else {
/* 183 */         byte b1; int j; DomainOrHostData[] arrayOfDomainOrHostData1; for (j = (arrayOfDomainOrHostData1 = domain.children).length, b1 = 0; b1 < j; ) { DomainOrHostData host = arrayOfDomainOrHostData1[b1];
/* 184 */           spec.secondarySiteHosts.add((ManagedObjectReference)this.refService.getReference(host.uid)); b1++; }
/*     */       
/*     */       } 
/*     */       b++; }
/*     */     
/* 189 */     spec.witnessHost = getWitnessHostRef(witnessHost);
/* 190 */     if (witnessHostDiskMapping != null) {
/* 191 */       spec.witnessHostDiskMapping = witnessHostDiskMapping.toVmodl();
/*     */     }
/*     */     
/* 194 */     return this.stretchedClusterMutationProvider.configureStretchedCluster(clusterRef, spec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService("changeWitnessHostTask")
/*     */   public ManagedObjectReference changeWitnessHost(ManagedObjectReference clusterRef, String preferredName, ManagedObjectReference witnessHost, VsanDiskMapping witnessHostDiskMapping) throws Exception {
/* 203 */     VsanWitnessConfig spec = new VsanWitnessConfig();
/* 204 */     spec.host = getWitnessHostRef(witnessHost);
/* 205 */     spec.preferredFaultDomain = preferredName;
/* 206 */     if (witnessHostDiskMapping != null) {
/* 207 */       spec.diskMapping = witnessHostDiskMapping.toVmodl();
/*     */     }
/*     */     
/* 210 */     return this.stretchedClusterMutationProvider.setWitnessHost(clusterRef, spec);
/*     */   }
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference getWitnessHostRef(ManagedObjectReference hostOrComputeResource) throws Exception {
/* 215 */     if (this.vmodlHelper.isOfType(hostOrComputeResource, ComputeResource.class)) {
/* 216 */       hostOrComputeResource = (ManagedObjectReference)QueryUtil.getProperty(
/* 217 */           hostOrComputeResource, "host", null);
/*     */     }
/* 219 */     return hostOrComputeResource;
/*     */   }
/*     */   
/*     */   private boolean isDomainConfigChanged(String name, DomainOrHostData[] domainsAndHosts) {
/* 223 */     return !(domainsAndHosts.length == 1 && !(domainsAndHosts[0]).isHost && (domainsAndHosts[0]).uid.equals(name));
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VsanHostsResult getVsanHosts(ManagedObjectReference clusterRef) throws Exception {
/* 229 */     return collectVsanHosts(clusterRef, true, (Measure)new NoOpMeasure());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanHostsResult collectVsanHosts(ManagedObjectReference clusterRef, boolean includeWitness, Measure measure) throws Exception {
/*     */     PropertyValue[] hostProps;
/*     */     VSANWitnessHostInfo[] witnessHostInfos;
/* 237 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 238 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/*     */ 
/*     */     
/* 241 */     Future<VSANWitnessHostInfo[]> witnessHostsFuture = null;
/* 242 */     if (includeWitness) {
/* 243 */       witnessHostsFuture = measure.newFuture("VSANWitnessHostInfo[]");
/* 244 */       stretchedClusterSystem.getWitnessHosts(clusterRef, witnessHostsFuture);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 249 */     Exception exception1 = null, exception2 = null; try { Measure hostsMeasure = measure.start("DS(hosts)"); 
/* 250 */       try { hostProps = QueryUtil.getPropertiesForRelatedObjects(
/* 251 */             clusterRef, 
/* 252 */             "host", 
/* 253 */             HostSystem.class.getSimpleName(), 
/* 254 */             new String[] { "runtime.connectionState" }).getPropertyValues(); }
/* 255 */       finally { if (hostsMeasure != null) hostsMeasure.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
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
/* 268 */     return new VsanHostsResult(hostProps, witnessHostInfos);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/stretchedcluster/ConfigureStretchedClusterService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */