/*      */ package com.vmware.vsan.client.services.hci;
/*      */ 
/*      */ import com.vmware.proxygen.ts.TsService;
/*      */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*      */ import com.vmware.vim.binding.vim.EVCMode;
/*      */ import com.vmware.vim.binding.vim.HostSystem;
/*      */ import com.vmware.vim.binding.vim.NumericRange;
/*      */ import com.vmware.vim.binding.vim.SDDCBase;
/*      */ import com.vmware.vim.binding.vim.dvs.DistributedVirtualPortgroup;
/*      */ import com.vmware.vim.binding.vim.dvs.HostMember;
/*      */ import com.vmware.vim.binding.vim.dvs.VmwareDistributedVirtualSwitch;
/*      */ import com.vmware.vim.binding.vim.fault.InvalidState;
/*      */ import com.vmware.vim.binding.vim.host.CpuPackage;
/*      */ import com.vmware.vim.binding.vim.host.PhysicalNic;
/*      */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthGroup;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthSummary;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
/*      */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*      */ import com.vmware.vim.vsan.binding.vim.vsan.ConfigInfoEx;
/*      */ import com.vmware.vim.vsan.binding.vim.vsan.ReconfigSpec;
/*      */ import com.vmware.vim.vsan.binding.vim.vsan.VsanExtendedConfig;
/*      */ import com.vmware.vim.vsan.binding.vim.vsan.VsanHealthPerspective;
/*      */ import com.vmware.vise.data.Constraint;
/*      */ import com.vmware.vise.data.query.Comparator;
/*      */ import com.vmware.vise.data.query.ObjectReferenceService;
/*      */ import com.vmware.vise.data.query.PropertyConstraint;
/*      */ import com.vmware.vise.data.query.PropertyValue;
/*      */ import com.vmware.vise.data.query.ResultSet;
/*      */ import com.vmware.vsan.client.services.common.PermissionService;
/*      */ import com.vmware.vsan.client.services.common.TaskService;
/*      */ import com.vmware.vsan.client.services.encryption.EncryptionPropertyProvider;
/*      */ import com.vmware.vsan.client.services.encryption.EncryptionStatus;
/*      */ import com.vmware.vsan.client.services.hci.model.BasicClusterConfigData;
/*      */ import com.vmware.vsan.client.services.hci.model.ClusterConfigData;
/*      */ import com.vmware.vsan.client.services.hci.model.ConfigCardData;
/*      */ import com.vmware.vsan.client.services.hci.model.ConfigureWizardData;
/*      */ import com.vmware.vsan.client.services.hci.model.DrsAutoLevel;
/*      */ import com.vmware.vsan.client.services.hci.model.DvsData;
/*      */ import com.vmware.vsan.client.services.hci.model.EvcModeConfigData;
/*      */ import com.vmware.vsan.client.services.hci.model.EvcModeData;
/*      */ import com.vmware.vsan.client.services.hci.model.ExistingDvpgData;
/*      */ import com.vmware.vsan.client.services.hci.model.ExistingDvsData;
/*      */ import com.vmware.vsan.client.services.hci.model.HciWorkflowState;
/*      */ import com.vmware.vsan.client.services.hci.model.HostAdapter;
/*      */ import com.vmware.vsan.client.services.hci.model.HostInCluster;
/*      */ import com.vmware.vsan.client.services.hci.model.IpAddressesRequestSpec;
/*      */ import com.vmware.vsan.client.services.hci.model.QuickstartViewData;
/*      */ import com.vmware.vsan.client.services.hci.model.Service;
/*      */ import com.vmware.vsan.client.services.hci.model.ValidationData;
/*      */ import com.vmware.vsan.client.services.hci.model.VlanData;
/*      */ import com.vmware.vsan.client.services.hci.model.VlanType;
/*      */ import com.vmware.vsan.client.services.hci.model.VsanClusterType;
/*      */ import com.vmware.vsan.client.services.hci.model.VsanHealthCheck;
/*      */ import com.vmware.vsan.client.util.StringUtil;
/*      */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*      */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*      */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*      */ import com.vmware.vsphere.client.vsan.data.EncryptionState;
/*      */ import com.vmware.vsphere.client.vsan.data.VsanConfigSpec;
/*      */ import com.vmware.vsphere.client.vsan.health.VsanHealthStatus;
/*      */ import com.vmware.vsphere.client.vsan.health.VsanTestData;
/*      */ import com.vmware.vsphere.client.vsan.health.util.VsanHealthUtil;
/*      */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*      */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*      */ import com.vmware.vsphere.client.vsan.util.Utils;
/*      */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*      */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections.CollectionUtils;
/*      */ import org.apache.commons.collections.MapUtils;
/*      */ import org.apache.commons.lang.ArrayUtils;
/*      */ import org.apache.commons.lang.StringUtils;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.commons.net.util.SubnetUtils;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.stereotype.Component;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Component
/*      */ public class HciClusterService
/*      */ {
/*      */   private static final String HOST_NICS_PROPERTY = "config.network.pnic";
/*      */   private static final String HOST_IS_WITNESS_PROPERTY = "isWitnessHost";
/*      */   private static final String SUPPORTED_EVC_MODE_PROPERTY = "supportedEvcMode";
/*      */   private static final String HOST_MAX_EVC_MODE_KEY_PROPERTY = "summary.maxEVCModeKey";
/*      */   private static final String DATACENTER_HOST_FOLDER_PROPERTY = "hostFolder";
/*      */   private static final String HCI_CONFIG_PROPERTY = "hciConfig";
/*      */   private static final String HA_PROPERTY = "configurationEx.dasConfig.enabled";
/*      */   private static final String HA_FAILOVER_LEVEL_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.failoverLevel";
/*      */   private static final String HA_HOST_MONITORING_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.hostMonitoring";
/*      */   private static final String HA_ADMISSION_CONTROL_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.admissionControlEnabled";
/*      */   private static final String HA_VM_MONITORING_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.vmMonitoring";
/*      */   private static final String DRS_PROPERTY = "configurationEx.drsConfig.enabled";
/*      */   private static final String DRS_AUTOMATION_LEVEL_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.defaultVmBehavior";
/*  106 */   private static final String[] BASIC_CLUSTER_CONFIG_PROPERTIES = new String[] {
/*  107 */       "host._length", 
/*  108 */       "hciConfig", 
/*  109 */       "configurationEx.drsConfig.enabled", 
/*  110 */       "configurationEx.dasConfig.enabled", 
/*  111 */       "configurationEx.vsanConfigInfo.enabled" }; private static final String DRS_MIGRATION_THRESHOLD_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.vmotionRate"; private static final String VSAN_PROPERTY = "configurationEx.vsanConfigInfo.enabled"; private static final String DVPG_VLAN_PROPERTY = "config.defaultPortConfig.vlan"; private static final String CLUSTER_DC_RELATION = "dc"; private static final String CONFIGURE_HCI_DISABLED_METHOD = "configureHCI"; private static final String EXTEND_HCI_DISABLED_METHOD = "extendHCI"; private static final String VERSION_PROPERTY = "config.productInfo.version"; private static final String DVS_HOST_PROPERTY = "config.host"; private static final String NIOC_VERSION_PROPERTY = "lacpVersionColumnLabelDerived"; private static final String LACP_VERSION_PROPERTY = "niocVersionColumnLabel"; private static final String DVS_PORTGROUP_RELATION = "portgroup"; private static final String DVPG_UPLINK_PROPERTY = "config.uplink"; private static final int LARGE_SCALE_CLUSTER_SUPPORT_THRESHOLD = 32;
/*  112 */   private static final String[] CLUSTER_CONFIG_PROPERTIES = new String[] {
/*  113 */       "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.admissionControlEnabled", 
/*  114 */       "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.failoverLevel", 
/*  115 */       "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.hostMonitoring", 
/*  116 */       "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.vmMonitoring", 
/*  117 */       "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.defaultVmBehavior", 
/*  118 */       "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.vmotionRate"
/*      */     };
/*  120 */   private static final String[] EXISTING_DVS_PROPERTIES = new String[] {
/*  121 */       "name", 
/*  122 */       "config.productInfo.version", 
/*  123 */       "lacpVersionColumnLabelDerived", 
/*  124 */       "niocVersionColumnLabel", 
/*  125 */       "config.host"
/*      */     };
/*      */   
/*      */   private static final String BASIC_CARD_ACTION_ID = "vsphere.core.cluster.actions.edit";
/*      */   
/*      */   private static final String ADD_HOSTS_CARD_ACTION_ID = "vsphere.core.hci.addHosts";
/*      */   
/*      */   private static final String CONFIGURE_CARD_ACTION_ID = "com.vmware.vsan.client.h5vsanui.cluster.configureHciCluster";
/*      */   private static final String OBJECT_NAME_SEPARATOR = " ";
/*      */   private static final String GENERAL_ENABLED = "enabled";
/*      */   private static final String VM_MONITORING_DISABLED = "vmMonitoringDisabled";
/*      */   private static final String DEFAULT_VLAN = "0";
/*      */   private static final int MAX_DVS = 3;
/*  138 */   private static final Log logger = LogFactory.getLog(HciClusterService.class);
/*  139 */   private static final VsanProfiler _profiler = new VsanProfiler(HciClusterService.class);
/*      */   
/*      */   @Autowired
/*      */   private VcClient vcClient;
/*      */   
/*      */   @Autowired
/*      */   private ObjectReferenceService refService;
/*      */   
/*      */   @Autowired
/*      */   private TaskService taskService;
/*      */   
/*      */   @Autowired
/*      */   private PermissionService permissionService;
/*      */   
/*      */   @Autowired
/*      */   private EncryptionPropertyProvider encryptionPropertyProvider;
/*      */   
/*      */   @TsService
/*      */   public QuickstartViewData getGettingStartedData(ManagedObjectReference clusterRef) throws Exception {
/*  158 */     QuickstartViewData result = new QuickstartViewData();
/*      */     
/*  160 */     BasicClusterConfigData basicClusterData = getBasicClusterConfigData(clusterRef);
/*  161 */     boolean hasEditClusterPermission = 
/*  162 */       this.permissionService.hasPermissions(clusterRef, new String[] { "Host.Inventory.EditCluster" });
/*      */     
/*  164 */     populateQuickstartInfoContainer(result, basicClusterData, hasEditClusterPermission);
/*      */     
/*  166 */     result.configurationCards = new ConfigCardData[] {
/*  167 */         getBasicConfigCard(
/*  168 */           basicClusterData, 
/*  169 */           hasEditClusterPermission), 
/*  170 */         getAddHostsCard(
/*  171 */           basicClusterData, 
/*  172 */           hasAddHostsPermissions(clusterRef, basicClusterData.vsanEnabled, hasEditClusterPermission)), 
/*  173 */         getConfigureCard(
/*  174 */           basicClusterData, 
/*  175 */           hasEditClusterPermission, 
/*  176 */           clusterRef)
/*      */       };
/*  178 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void populateQuickstartInfoContainer(QuickstartViewData viewData, BasicClusterConfigData basicClusterData, boolean hasEditClusterPermission) {
/*  184 */     switch (basicClusterData.hciWorkflowState) {
/*      */       case IN_PROGRESS:
/*  186 */         viewData.header = Utils.getLocalizedString("vsan.hci.gettingStarted.createWorkflow.header");
/*  187 */         viewData.text = Utils.getLocalizedString("vsan.hci.gettingStarted.createWorkflow.text");
/*  188 */         viewData.showSendFeedbackLink = false;
/*      */         break;
/*      */       case null:
/*  191 */         if (basicClusterData.hciWorkflowState == HciWorkflowState.DONE) {
/*  192 */           if (basicClusterData.notConfiguredHosts == 0) {
/*  193 */             viewData.header = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.initial.header");
/*  194 */             viewData.text = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.initial.text");
/*  195 */             viewData.showSendFeedbackLink = true; break;
/*      */           } 
/*  197 */           viewData.header = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.inProgress.header");
/*  198 */           viewData.text = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.inProgress.text");
/*  199 */           viewData.showSendFeedbackLink = false;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case INVALID:
/*  205 */         viewData.header = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.abandoned.header");
/*  206 */         viewData.text = Utils.getLocalizedString("vsan.hci.gettingStarted.extendWorkflow.abandoned.text");
/*  207 */         viewData.showSendFeedbackLink = true;
/*      */         break;
/*      */       
/*      */       default:
/*  211 */         viewData.header = Utils.getLocalizedString("vsan.hci.gettingStarted.createWorkflow.header");
/*  212 */         viewData.text = Utils.getLocalizedString("vsan.hci.gettingStarted.createWorkflow.text");
/*  213 */         viewData.showSendFeedbackLink = false;
/*      */         break;
/*      */     } 
/*  216 */     viewData.showCloseQuickstartButton = (
/*  217 */       basicClusterData.hciWorkflowState == HciWorkflowState.IN_PROGRESS && hasEditClusterPermission);
/*  218 */     viewData.extendCard = (basicClusterData.hosts > 0);
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public BasicClusterConfigData getBasicClusterConfigData(ManagedObjectReference clusterRef) throws Exception {
/*  223 */     DataServiceResponse response = QueryUtil.getProperties(clusterRef, BASIC_CLUSTER_CONFIG_PROPERTIES);
/*  224 */     BasicClusterConfigData result = new BasicClusterConfigData();
/*  225 */     populateBasicClusterConfigData(clusterRef, result, response);
/*  226 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void populateBasicClusterConfigData(ManagedObjectReference clusterRef, BasicClusterConfigData basicConfig, DataServiceResponse response) throws Exception {
/*  234 */     if (response != null) {
/*  235 */       PropertyValue[] propertyValues = response.getPropertyValues();
/*  236 */       if (propertyValues != null) {
/*      */         
/*  238 */         ClusterComputeResource.HCIConfigInfo hciConfigInfo = null; byte b; int i; PropertyValue[] arrayOfPropertyValue;
/*  239 */         for (i = (arrayOfPropertyValue = propertyValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b];
/*  240 */           if (propValue.propertyName.equals("host._length")) {
/*  241 */             basicConfig.hosts = ((Integer)propValue.value).intValue();
/*  242 */           } else if (propValue.propertyName.equals("configurationEx.drsConfig.enabled")) {
/*  243 */             basicConfig.drsEnabled = ((Boolean)propValue.value).booleanValue();
/*  244 */           } else if (propValue.propertyName.equals("configurationEx.dasConfig.enabled")) {
/*  245 */             basicConfig.haEnabled = ((Boolean)propValue.value).booleanValue();
/*  246 */           } else if (propValue.propertyName.equals("configurationEx.vsanConfigInfo.enabled")) {
/*  247 */             basicConfig.vsanEnabled = ((Boolean)propValue.value).booleanValue();
/*  248 */           } else if (propValue.propertyName.equals("hciConfig")) {
/*  249 */             hciConfigInfo = (ClusterComputeResource.HCIConfigInfo)propValue.value;
/*      */           } 
/*      */           b++; }
/*      */         
/*  253 */         if (hciConfigInfo != null) {
/*  254 */           basicConfig.notConfiguredHosts = getNotConfiguredHostsCount(basicConfig.hosts, hciConfigInfo);
/*  255 */           basicConfig.hciWorkflowState = HciWorkflowState.fromString(hciConfigInfo.workflowState);
/*  256 */           if (basicConfig.hciWorkflowState == HciWorkflowState.DONE && 
/*  257 */             basicConfig.notConfiguredHosts > 0) {
/*  258 */             basicConfig.dvsDataByService = getDvsInfoData(hciConfigInfo);
/*      */           }
/*      */         } else {
/*  261 */           basicConfig.notConfiguredHosts = 0;
/*  262 */           basicConfig.hciWorkflowState = HciWorkflowState.NOT_IN_HCI_WORKFLOW;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getNotConfiguredHostsCount(int hosts, ClusterComputeResource.HCIConfigInfo hciConfigInfo) {
/*  269 */     ManagedObjectReference[] configuredHosts = (hciConfigInfo == null) ? null : hciConfigInfo.configuredHosts;
/*  270 */     return (configuredHosts == null) ? hosts : (hosts - configuredHosts.length);
/*      */   }
/*      */   
/*      */   private ManagedObjectReference[] getNotConfiguredHosts(ManagedObjectReference clusterRef) throws Exception {
/*  274 */     DataServiceResponse response = 
/*  275 */       QueryUtil.getProperties(clusterRef, new String[] { "host", "hciConfig" });
/*  276 */     ManagedObjectReference[] hosts = (ManagedObjectReference[])response.getProperty(clusterRef, "host");
/*  277 */     ClusterComputeResource.HCIConfigInfo hciConfigInfo = (ClusterComputeResource.HCIConfigInfo)response.getProperty(clusterRef, "hciConfig");
/*      */     
/*  279 */     ManagedObjectReference[] configuredHosts = (hciConfigInfo == null) ? null : hciConfigInfo.configuredHosts;
/*  280 */     ManagedObjectReference[] notConfiguredHosts = new ManagedObjectReference[0];
/*  281 */     if (hosts != null) {
/*  282 */       if (configuredHosts == null) {
/*  283 */         notConfiguredHosts = hosts;
/*  284 */       } else if (hosts.length == configuredHosts.length) {
/*  285 */         notConfiguredHosts = new ManagedObjectReference[0];
/*      */       } else {
/*  287 */         List<String> configuredHostsIds = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference1;
/*  288 */         for (i = (arrayOfManagedObjectReference1 = configuredHosts).length, b = 0; b < i; ) { ManagedObjectReference host = arrayOfManagedObjectReference1[b];
/*  289 */           configuredHostsIds.add(host.getValue()); b++; }
/*      */         
/*  291 */         List<ManagedObjectReference> hostsList = new ArrayList<>(); ManagedObjectReference[] arrayOfManagedObjectReference2;
/*  292 */         for (int j = (arrayOfManagedObjectReference2 = hosts).length; i < j; ) { ManagedObjectReference host = arrayOfManagedObjectReference2[i];
/*  293 */           if (!configuredHostsIds.contains(host.getValue()))
/*  294 */             hostsList.add(host); 
/*      */           i++; }
/*      */         
/*  297 */         notConfiguredHosts = hostsList.<ManagedObjectReference>toArray(new ManagedObjectReference[0]);
/*      */       } 
/*      */     }
/*      */     
/*  301 */     return notConfiguredHosts;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<HostInCluster> getClusterHosts(ManagedObjectReference clusterRef) throws Exception {
/*  307 */     List<HostInCluster> result = new ArrayList<>();
/*  308 */     PropertyValue[] hostNameValues = QueryUtil.getPropertyForRelatedObjects(
/*  309 */         clusterRef, 
/*  310 */         "host", 
/*  311 */         ClusterComputeResource.class.getSimpleName(), 
/*  312 */         "name").getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  313 */     for (i = (arrayOfPropertyValue1 = hostNameValues).length, b = 0; b < i; ) { PropertyValue nameValue = arrayOfPropertyValue1[b];
/*  314 */       result.add(HostInCluster.create(
/*  315 */             (ManagedObjectReference)nameValue.resourceObject, 
/*  316 */             this.refService.getUid(nameValue.resourceObject), 
/*  317 */             (String)nameValue.value));
/*      */       b++; }
/*      */     
/*  320 */     return result;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<HostInCluster> getNotConfiguredClusterHosts(ManagedObjectReference clusterRef) throws Exception {
/*  325 */     List<HostInCluster> result = new ArrayList<>();
/*  326 */     ManagedObjectReference[] notConfiguredHosts = getNotConfiguredHosts(clusterRef);
/*      */     
/*  328 */     PropertyValue[] hostNameValues = 
/*  329 */       QueryUtil.getProperties((Object[])notConfiguredHosts, new String[] { "name" }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  330 */     for (i = (arrayOfPropertyValue1 = hostNameValues).length, b = 0; b < i; ) { PropertyValue nameValue = arrayOfPropertyValue1[b];
/*  331 */       result.add(HostInCluster.create(
/*  332 */             (ManagedObjectReference)nameValue.resourceObject, 
/*  333 */             this.refService.getUid(nameValue.resourceObject), 
/*  334 */             (String)nameValue.value));
/*      */       b++; }
/*      */     
/*  337 */     Collections.sort(result, new Comparator<HostInCluster>()
/*      */         {
/*      */           public int compare(HostInCluster host1, HostInCluster host2) {
/*  340 */             if ((host1 == null || StringUtils.isEmpty(host1.name)) && (
/*  341 */               host2 == null || StringUtils.isEmpty(host2.name)))
/*  342 */               return 0; 
/*  343 */             if (host1 == null || StringUtils.isEmpty(host1.name))
/*  344 */               return 1; 
/*  345 */             if (host2 == null || StringUtils.isEmpty(host2.name)) {
/*  346 */               return -1;
/*      */             }
/*      */             
/*  349 */             return host1.name.compareTo(host2.name);
/*      */           }
/*      */         });
/*      */     
/*  353 */     return result;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public ConfigCardData validateNotConfiguredHosts(ManagedObjectReference clusterRef) throws Exception {
/*  358 */     boolean hasEditClusterPermission = 
/*  359 */       this.permissionService.hasPermissions(clusterRef, new String[] { "Host.Inventory.EditCluster" });
/*      */     
/*  361 */     BasicClusterConfigData basicClusterConfigData = getBasicClusterConfigData(clusterRef);
/*      */     
/*  363 */     ConfigCardData result = getBasicAddHostsCard();
/*  364 */     result.contentHeader = getHostsNumLabel(basicClusterConfigData);
/*  365 */     result.validateEnabled = true;
/*  366 */     result.enabled = hasAddHostsPermissions(clusterRef, basicClusterConfigData.vsanEnabled, hasEditClusterPermission);
/*  367 */     populateHealthChecksResult(result, 
/*  368 */         clusterRef, 
/*  369 */         VsanHealthPerspective.beforeConfigureHost.toString(), 
/*  370 */         Boolean.valueOf(false));
/*      */     
/*  372 */     return result;
/*      */   }
/*      */   
/*      */   private ConfigCardData getBasicAddHostsCard() {
/*  376 */     return new ConfigCardData(
/*  377 */         Utils.getLocalizedString("vsan.hci.gettingStarted.addHostsCard.title"), 
/*  378 */         "vsphere.core.hci.addHosts", 
/*  379 */         true, 
/*  380 */         false, 
/*  381 */         Utils.getLocalizedString("vsan.hci.gettingStarted.addHostsCard.launchButton.text"));
/*      */   }
/*      */ 
/*      */   
/*      */   private ConfigCardData getBasicConfigureCard() {
/*  386 */     return new ConfigCardData(
/*  387 */         Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.title"), 
/*  388 */         "com.vmware.vsan.client.h5vsanui.cluster.configureHciCluster", 
/*  389 */         true, 
/*  390 */         false, 
/*  391 */         Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.launchButton.text"));
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public ConfigCardData validateCluster(ManagedObjectReference clusterRef) throws Exception {
/*  397 */     BasicClusterConfigData basicClusterConfigData = getBasicClusterConfigData(clusterRef);
/*      */     
/*  399 */     ConfigCardData result = getBasicConfigureCard();
/*  400 */     result.enabled = (basicClusterConfigData.notConfiguredHosts > 0);
/*      */     
/*  402 */     if (basicClusterConfigData.hciWorkflowState.equals(HciWorkflowState.DONE)) {
/*  403 */       result.title = Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.titleInExtend");
/*  404 */       result.validateEnabled = (basicClusterConfigData.hosts > 0 && basicClusterConfigData.notConfiguredHosts == 0);
/*      */     } 
/*      */     
/*  407 */     populateHealthChecksResult(result, 
/*  408 */         clusterRef, 
/*  409 */         VsanHealthPerspective.defaultView.toString(), 
/*  410 */         Boolean.valueOf(true));
/*      */     
/*  412 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void populateHealthChecksResult(ConfigCardData card, ManagedObjectReference clusterRef, String perspective, Boolean showGroupsOnly) throws Exception {
/*      */     List<VsanHealthCheck> healthChecks;
/*  419 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*  420 */     String[] requiredFields = { "groups" };
/*      */     
/*  422 */     VsanClusterHealthSummary healthSummary = null;
/*  423 */     Exception exception1 = null, exception2 = null; try { VsanProfiler.Point point = _profiler.point("healthSystem.queryClusterHealthSummary");
/*      */       
/*  425 */       try { healthSummary = healthSystem.queryClusterHealthSummary(clusterRef, null, null, 
/*  426 */             Boolean.valueOf(false), requiredFields, Boolean.valueOf(false), perspective, 
/*  427 */             Boolean.valueOf(false), null); }
/*  428 */       finally { if (point != null) point.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */        }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  459 */     boolean vsanEnabled = ((Boolean)QueryUtil.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled", null)).booleanValue();
/*  460 */     card.validationData = new ValidationData(healthChecks, vsanEnabled);
/*  461 */     VsanHealthStatus overallStatus = VsanHealthStatus.valueOf(healthSummary.overallHealth);
/*  462 */     card.status = (overallStatus == VsanHealthStatus.red) ? 
/*  463 */       ConfigCardData.Status.ERROR : 
/*  464 */       ConfigCardData.Status.PASSED;
/*      */   }
/*      */ 
/*      */   
/*      */   private ConfigCardData getBasicConfigCard(BasicClusterConfigData basicClusterConfigData, boolean hasPermissions) throws Exception {
/*  469 */     ConfigCardData result = new ConfigCardData(
/*  470 */         Utils.getLocalizedString("vsan.hci.gettingStarted.basicConfigCard.title"), 
/*  471 */         "vsphere.core.cluster.actions.edit", 
/*  472 */         false, 
/*  473 */         false, 
/*  474 */         Utils.getLocalizedString("vsan.hci.gettingStarted.basicConfigCard.launchButton.text"));
/*      */ 
/*      */     
/*  477 */     result.enabled = (
/*  478 */       basicClusterConfigData.hciWorkflowState == HciWorkflowState.IN_PROGRESS && hasPermissions);
/*  479 */     result.status = ConfigCardData.Status.PASSED;
/*      */     
/*  481 */     result.listItems = getEnabledServices(basicClusterConfigData);
/*  482 */     result.contentHeader = Utils.getLocalizedString(
/*  483 */         (result.listItems.size() > 0) ? 
/*  484 */         "vsan.hci.gettingStarted.basicConfigCard.contentHeader" : 
/*  485 */         "vsan.hci.gettingStarted.basicConfigCard.contentHeader.noServices");
/*      */     
/*  487 */     return result;
/*      */   }
/*      */   
/*      */   private List<String> getEnabledServices(BasicClusterConfigData basicClusterConfigData) throws Exception {
/*  491 */     List<String> result = new ArrayList<>();
/*      */     
/*  493 */     if (basicClusterConfigData.drsEnabled) {
/*  494 */       result.add(Utils.getLocalizedString("vsan.hci.gettingStarted.basicConfigCard.services.drs"));
/*      */     }
/*      */     
/*  497 */     if (basicClusterConfigData.haEnabled) {
/*  498 */       result.add(Utils.getLocalizedString("vsan.hci.gettingStarted.basicConfigCard.services.ha"));
/*      */     }
/*      */     
/*  501 */     if (basicClusterConfigData.vsanEnabled) {
/*  502 */       result.add(Utils.getLocalizedString("vsan.hci.gettingStarted.basicConfigCard.services.vsan"));
/*      */     }
/*      */     
/*  505 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private ConfigCardData getAddHostsCard(BasicClusterConfigData basicClusterConfigData, boolean hasPermissions) throws Exception {
/*  510 */     ConfigCardData result = getBasicAddHostsCard();
/*  511 */     result.enabled = hasPermissions;
/*  512 */     result.validateEnabled = (basicClusterConfigData.hosts > 0 && 
/*  513 */       basicClusterConfigData.notConfiguredHosts > 0 && 
/*  514 */       basicClusterConfigData.hciWorkflowState != HciWorkflowState.INVALID);
/*      */ 
/*      */     
/*  517 */     result.nextStep = !result.validateEnabled;
/*      */     
/*  519 */     if (basicClusterConfigData.hosts == 0) {
/*  520 */       result.contentText = Utils.getLocalizedString("vsan.hci.gettingStarted.addHostsCard.contentText");
/*  521 */       result.status = ConfigCardData.Status.NOT_AVAILABLE;
/*      */     } else {
/*  523 */       result.contentHeader = getHostsNumLabel(basicClusterConfigData);
/*  524 */       result.status = ConfigCardData.Status.PASSED;
/*      */     } 
/*      */     
/*  527 */     return result;
/*      */   }
/*      */   
/*      */   private String getHostsNumLabel(BasicClusterConfigData basicClusterConfigData) {
/*  531 */     if (basicClusterConfigData.notConfiguredHosts != 0) {
/*  532 */       if (basicClusterConfigData.notConfiguredHosts == basicClusterConfigData.hosts)
/*  533 */         return Utils.getLocalizedString(
/*  534 */             "vsan.hci.gettingStarted.addHostsCard.notConfiguredHostsInTheCluster", new String[] {
/*  535 */               String.valueOf(basicClusterConfigData.notConfiguredHosts)
/*      */             }); 
/*  537 */       return Utils.getLocalizedString(
/*  538 */           "vsan.hci.gettingStarted.addHostsCard.hostsInTheClusterByType", new String[] {
/*  539 */             String.valueOf(basicClusterConfigData.hosts), 
/*  540 */             String.valueOf(basicClusterConfigData.notConfiguredHosts)
/*      */           });
/*      */     } 
/*  543 */     return Utils.getLocalizedString(
/*  544 */         "vsan.hci.gettingStarted.addHostsCard.hostsInTheCluster", new String[] {
/*  545 */           String.valueOf(basicClusterConfigData.hosts)
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   private ConfigCardData getConfigureCard(BasicClusterConfigData basicClusterConfigData, boolean hasEditClusterPermission, ManagedObjectReference clusterRef) throws Exception {
/*  551 */     ConfigCardData result = getBasicConfigureCard();
/*  552 */     result.contentText = Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.contentText");
/*  553 */     result.operationInProgress = isConfigureOperationInProgress(clusterRef);
/*  554 */     result.enabled = (!result.operationInProgress && 
/*  555 */       isConfigureCardEnabled(basicClusterConfigData, hasEditClusterPermission));
/*  556 */     result.nextStep = result.enabled;
/*  557 */     result.contentHeader = getNotConfiguredHostsLabel(basicClusterConfigData);
/*  558 */     result.status = ConfigCardData.Status.NOT_AVAILABLE;
/*  559 */     result.validateEnabled = (
/*  560 */       basicClusterConfigData.hosts > 0 && 
/*  561 */       basicClusterConfigData.notConfiguredHosts == 0 && 
/*  562 */       basicClusterConfigData.hciWorkflowState != HciWorkflowState.NOT_IN_HCI_WORKFLOW);
/*      */     
/*  564 */     if (basicClusterConfigData.hciWorkflowState.equals(HciWorkflowState.DONE)) {
/*  565 */       result.title = Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.titleInExtend");
/*      */     }
/*      */     
/*  568 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isConfigureCardEnabled(BasicClusterConfigData basicClusterConfigData, boolean hasPermission) {
/*  572 */     return (basicClusterConfigData.notConfiguredHosts > 0 && 
/*  573 */       !basicClusterConfigData.hciWorkflowState.equals(HciWorkflowState.INVALID) && 
/*  574 */       hasPermission);
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public boolean isConfigureOperationInProgress(ManagedObjectReference clusterRef) {
/*  579 */     Exception exception1 = null, exception2 = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getNotConfiguredHostsLabel(BasicClusterConfigData configData) {
/*  600 */     if (configData.hciWorkflowState == HciWorkflowState.DONE && configData.notConfiguredHosts > 0) {
/*  601 */       return (configData.notConfiguredHosts == 1) ? 
/*  602 */         Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.notConfiguredHostText") : 
/*  603 */         Utils.getLocalizedString("vsan.hci.gettingStarted.configureServicesCard.notConfiguredHostsText", new String[] {
/*  604 */             String.valueOf(configData.notConfiguredHosts)
/*      */           });
/*      */     }
/*  607 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public EvcModeConfigData getEvcModeConfigData(ManagedObjectReference clusterRef) throws Exception {
/*  615 */     EvcModeConfigData data = new EvcModeConfigData();
/*      */     
/*  617 */     PropertyValue[] hostProps = QueryUtil.getPropertiesForRelatedObjects(
/*  618 */         clusterRef, 
/*  619 */         "host", 
/*  620 */         HostSystem.class.getSimpleName(), 
/*  621 */         new String[] { "summary.maxEVCModeKey" }).getPropertyValues();
/*      */     
/*  623 */     EVCMode[] evcModes = (EVCMode[])QueryUtil.getProperty(clusterRef, 
/*  624 */         "supportedEvcMode", null);
/*      */     
/*  626 */     if (ArrayUtils.isEmpty((Object[])evcModes) || ArrayUtils.isEmpty((Object[])hostProps)) {
/*  627 */       return data;
/*      */     }
/*      */     
/*  630 */     List<EvcModeData> supportedAmdEvcMode = new ArrayList<>();
/*  631 */     List<EvcModeData> supportedIntelEvcMode = new ArrayList<>(); byte b1; int i;
/*      */     EVCMode[] arrayOfEVCMode1;
/*  633 */     for (i = (arrayOfEVCMode1 = evcModes).length, b1 = 0; b1 < i; ) { EVCMode evcMode = arrayOfEVCMode1[b1];
/*  634 */       EvcModeData modeData = new EvcModeData();
/*  635 */       modeData.id = evcMode.key;
/*  636 */       modeData.label = evcMode.label;
/*  637 */       if (CpuPackage.Vendor.amd.name().equals(evcMode.vendor)) {
/*  638 */         supportedAmdEvcMode.add(modeData);
/*  639 */       } else if (CpuPackage.Vendor.intel.name().equals(evcMode.vendor)) {
/*  640 */         supportedIntelEvcMode.add(modeData);
/*      */       } else {
/*  642 */         logger.warn("Unsupported vendor: " + evcMode.vendor);
/*      */       } 
/*      */       b1++; }
/*      */     
/*  646 */     List<Integer> intelSupportedIndex = new ArrayList<>();
/*  647 */     List<Integer> amdSupportedIndex = new ArrayList<>(); byte b2; int j;
/*      */     PropertyValue[] arrayOfPropertyValue1;
/*  649 */     for (j = (arrayOfPropertyValue1 = hostProps).length, b2 = 0; b2 < j; ) { PropertyValue propValue = arrayOfPropertyValue1[b2];
/*  650 */       String hostEvcMode = (String)propValue.value;
/*      */       
/*  652 */       if (StringUtils.isEmpty(hostEvcMode)) {
/*      */         break;
/*      */       }
/*      */       
/*  656 */       if (hostEvcMode.contains(CpuPackage.Vendor.amd.name())) {
/*  657 */         for (int k = 0; k < supportedAmdEvcMode.size(); k++) {
/*  658 */           if (hostEvcMode.equals(((EvcModeData)supportedAmdEvcMode.get(k)).id)) {
/*  659 */             amdSupportedIndex.add(Integer.valueOf(k));
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*  665 */       if (hostEvcMode.contains(CpuPackage.Vendor.intel.name())) {
/*  666 */         for (int k = 0; k < supportedIntelEvcMode.size(); k++) {
/*  667 */           if (hostEvcMode.equals(((EvcModeData)supportedIntelEvcMode.get(k)).id)) {
/*  668 */             intelSupportedIndex.add(Integer.valueOf(k));
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*      */       b2++; }
/*      */ 
/*      */     
/*  679 */     if ((intelSupportedIndex.size() != 0 && amdSupportedIndex.size() != 0) || (
/*  680 */       intelSupportedIndex.size() == 0 && amdSupportedIndex.size() == 0)) {
/*  681 */       data.unsupportedEvcStatus = true;
/*  682 */       return data;
/*  683 */     }  if (intelSupportedIndex.size() != 0) {
/*  684 */       Integer intelSupportedLength = Integer.valueOf(((Integer)Collections.<Integer>min(intelSupportedIndex)).intValue() + 1);
/*  685 */       data.supportedIntelEvcMode = supportedIntelEvcMode.subList(0, intelSupportedLength.intValue());
/*      */     } else {
/*  687 */       Integer amdSupportedLength = Integer.valueOf(((Integer)Collections.<Integer>min(amdSupportedIndex)).intValue() + 1);
/*  688 */       data.supportedAmdEvcMode = supportedAmdEvcMode.subList(0, amdSupportedLength.intValue());
/*      */     } 
/*      */     
/*  691 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public ClusterConfigData getClusterConfigData(ManagedObjectReference clusterRef) throws Exception {
/*  699 */     String[] properties = (String[])ArrayUtils.addAll((Object[])BASIC_CLUSTER_CONFIG_PROPERTIES, (Object[])CLUSTER_CONFIG_PROPERTIES);
/*  700 */     DataServiceResponse response = QueryUtil.getProperties(clusterRef, properties);
/*      */     
/*  702 */     ClusterConfigData configData = new ClusterConfigData();
/*      */     
/*  704 */     configData.basicConfig = new BasicClusterConfigData();
/*  705 */     populateBasicClusterConfigData(clusterRef, configData.basicConfig, response);
/*      */     
/*  707 */     configData.enableAdmissionControl = ((Boolean)response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.admissionControlEnabled")).booleanValue();
/*  708 */     configData.hostFTT = ((Integer)response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.failoverLevel")).intValue();
/*  709 */     String vmMonitorStr = (String)response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.vmMonitoring");
/*  710 */     configData.enableVmMonitoring = !"vmMonitoringDisabled".equals(vmMonitorStr);
/*  711 */     String hostMonitorStr = (String)response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].dasConfig.hostMonitoring");
/*  712 */     configData.enableHostMonitoring = "enabled".equals(hostMonitorStr);
/*      */     
/*  714 */     configData.automationLevel = DrsAutoLevel.fromString(
/*  715 */         response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.defaultVmBehavior").toString());
/*  716 */     configData.migrationThreshold = ((Integer)response.getProperty(clusterRef, "configurationEx[@type='ClusterConfigInfoEx'].drsConfig.vmotionRate")).intValue();
/*      */     
/*  718 */     configData.vsanConfigSpec = getVsanConfigSpec(clusterRef);
/*      */     
/*  720 */     return configData;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public Object getEvcModeValidationResult(ManagedObjectReference clusterRef, String evcModeKey) throws Exception {
/*  725 */     Exception exception1 = null, exception2 = null;
/*      */ 
/*      */     
/*      */     try {
/*      */     
/*      */     } finally {
/*  731 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService("configureHciClusterTask")
/*      */   public ManagedObjectReference configureCluster(ManagedObjectReference clusterRef, ClusterConfigData clusterConfigData) throws Exception {
/*  741 */     boolean vsanEnabled = clusterConfigData.basicConfig.vsanEnabled;
/*  742 */     HciWorkflowState state = clusterConfigData.basicConfig.hciWorkflowState;
/*      */     
/*  744 */     VcConnection vcConnection = getVcConnection(clusterRef, vsanEnabled); try {
/*  745 */       Exception exception1 = null, exception2 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  763 */       if (clusterConfigData.basicConfig.vsanEnabled) {
/*  764 */         vcConnection.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public ManagedObjectReference simpleClusterExtend(ManagedObjectReference clusterRef) throws Exception {
/*  771 */     ClusterConfigData clusterConfigData = getClusterConfigData(clusterRef);
/*  772 */     VcConnection vcConnection = getVcConnection(clusterRef, clusterConfigData.basicConfig.vsanEnabled);
/*  773 */     return extendWorkflow(
/*  774 */         vcConnection, 
/*  775 */         clusterRef, 
/*  776 */         clusterConfigData, 
/*  777 */         getNotConfiguredClusterHosts(clusterRef));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public void abandonHciWorkflowCluster(ManagedObjectReference clusterRef) throws Exception {
/*  786 */     VcConnection vcConnection = this.vcClient.getConnection(clusterRef.getServerGuid());
/*  787 */     ClusterComputeResource cluster = (ClusterComputeResource)vcConnection.createStub(ClusterComputeResource.class, clusterRef);
/*  788 */     if (cluster.getHciConfig() != null && HciWorkflowState.IN_PROGRESS == 
/*  789 */       HciWorkflowState.fromString((cluster.getHciConfig()).workflowState)) {
/*  790 */       cluster.AbandonHciWorkflow();
/*      */     }
/*      */   }
/*      */   
/*      */   private VcConnection getVcConnection(ManagedObjectReference clusterRef, boolean vsan) {
/*  795 */     return vsan ? 
/*  796 */       this.vcClient.getVsanVmodlVersionConnection(clusterRef.getServerGuid()) : 
/*  797 */       this.vcClient.getConnection(clusterRef.getServerGuid());
/*      */   }
/*      */ 
/*      */   
/*      */   private ManagedObjectReference createWorkflow(VcConnection vcConnection, ManagedObjectReference clusterRef, ClusterConfigData clusterConfigData, List<HostInCluster> hosts) throws Exception {
/*  802 */     ClusterComputeResource cluster = (ClusterComputeResource)vcConnection.createStub(ClusterComputeResource.class, clusterRef);
/*      */     
/*  804 */     boolean hasEncryptionPermissions = this.encryptionPropertyProvider.getEncryptionPermissions(clusterRef);
/*  805 */     ClusterComputeResource.HCIConfigSpec hciConfigSpec = clusterConfigData.getHciConfigSpec(clusterRef, hasEncryptionPermissions);
/*  806 */     ClusterComputeResource.HostConfigurationInput[] hostConfigurationInputs = 
/*  807 */       clusterConfigData.getHostConfigurationInputs(hosts);
/*      */     
/*  809 */     ManagedObjectReference taskRef = cluster.configureHCI(hciConfigSpec, hostConfigurationInputs);
/*  810 */     taskRef.setServerGuid(clusterRef.getServerGuid());
/*      */     
/*  812 */     return taskRef;
/*      */   }
/*      */   
/*      */   private ManagedObjectReference extendWorkflow(VcConnection vcConnection, ManagedObjectReference clusterRef, ClusterConfigData clusterConfigData, List<HostInCluster> hosts) throws Exception {
/*      */     ReconfigSpec reconfigSpec1;
/*  817 */     ClusterComputeResource cluster = (ClusterComputeResource)vcConnection.createStub(ClusterComputeResource.class, clusterRef);
/*      */     
/*  819 */     ClusterComputeResource.HostConfigurationInput[] hostConfigurationInputs = clusterConfigData.getHostConfigurationInputs(hosts);
/*  820 */     SDDCBase reconfigSpec = null;
/*  821 */     if (clusterConfigData.basicConfig.vsanEnabled) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  827 */       if (clusterConfigData.vsanConfigSpec.stretchedClusterConfig != null && 
/*  828 */         clusterConfigData.vsanConfigSpec.stretchedClusterConfig.witnessHost == null) {
/*  829 */         clusterConfigData.vsanConfigSpec.stretchedClusterConfig.witnessHost = 
/*  830 */           getStretchedClusterWitnessHost(clusterRef);
/*      */       }
/*      */ 
/*      */       
/*  834 */       reconfigSpec1 = clusterConfigData.vsanConfigSpec.getBasicReconfigSpec();
/*      */     } 
/*      */     
/*  837 */     ManagedObjectReference taskRef = cluster.extendHCI(hostConfigurationInputs, (SDDCBase)reconfigSpec1);
/*  838 */     taskRef.setServerGuid(clusterRef.getServerGuid());
/*  839 */     return taskRef;
/*      */   }
/*      */   
/*      */   private ManagedObjectReference getStretchedClusterWitnessHost(ManagedObjectReference clusterRef) throws Exception {
/*  843 */     DataServiceResponse propertyResponse = QueryUtil.getPropertyForRelatedObjects(
/*  844 */         clusterRef, 
/*  845 */         "allVsanHosts", 
/*  846 */         ClusterComputeResource.class.getSimpleName(), 
/*  847 */         "isWitnessHost");
/*      */     
/*  849 */     PropertyValue[] propertyValues = propertyResponse.getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  850 */     for (i = (arrayOfPropertyValue1 = propertyValues).length, b = 0; b < i; ) { PropertyValue value = arrayOfPropertyValue1[b];
/*  851 */       if (value.propertyName.equals("isWitnessHost")) {
/*  852 */         boolean isWitnessHost = ((Boolean)value.value).booleanValue();
/*  853 */         if (isWitnessHost) {
/*  854 */           return (ManagedObjectReference)value.resourceObject;
/*      */         }
/*      */       } 
/*      */       b++; }
/*      */     
/*  859 */     return null;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<HostAdapter> getPhysicalAdapters(ManagedObjectReference clusterRef) throws Exception {
/*  864 */     DataServiceResponse response = QueryUtil.getPropertiesForRelatedObjects(
/*  865 */         clusterRef, 
/*  866 */         "host", 
/*  867 */         ClusterComputeResource.class.getSimpleName(), 
/*  868 */         new String[] { "config.network.pnic" });
/*      */     
/*  870 */     PropertyValue[] propertyValues = response.getPropertyValues();
/*  871 */     int nicCount = getMaxCommonNicCount(propertyValues);
/*  872 */     List<String> names = getFirstNDeviceNames(propertyValues, nicCount);
/*  873 */     Collections.sort(names);
/*      */     
/*  875 */     return generateNHostAdapters(names, nicCount);
/*      */   }
/*      */   
/*      */   private int getMaxCommonNicCount(PropertyValue[] propertyValues) {
/*  879 */     int nicCount = Integer.MAX_VALUE;
/*      */     
/*  881 */     if (propertyValues.length != 0) {
/*  882 */       byte b; int i; PropertyValue[] arrayOfPropertyValue; for (i = (arrayOfPropertyValue = propertyValues).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue[b];
/*  883 */         PhysicalNic[] nics = (PhysicalNic[])propertyValue.value;
/*  884 */         nicCount = Math.min(nicCount, nics.length); b++; }
/*      */     
/*      */     } else {
/*  887 */       nicCount = 0;
/*      */     } 
/*      */     
/*  890 */     return nicCount;
/*      */   }
/*      */   
/*      */   private List<String> getFirstNDeviceNames(PropertyValue[] hostPropertyValues, int number) {
/*  894 */     List<String> result = new ArrayList<>();
/*      */ 
/*      */     
/*  897 */     Map<String, Integer> pnicNamesToHostCount = new HashMap<>(); byte b; int i;
/*      */     PropertyValue[] arrayOfPropertyValue;
/*  899 */     for (i = (arrayOfPropertyValue = hostPropertyValues).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue[b];
/*  900 */       PhysicalNic[] physicalNics = (PhysicalNic[])propertyValue.value; byte b1; int j; PhysicalNic[] arrayOfPhysicalNic1;
/*  901 */       for (j = (arrayOfPhysicalNic1 = physicalNics).length, b1 = 0; b1 < j; ) { PhysicalNic physicalNic = arrayOfPhysicalNic1[b1];
/*  902 */         String pnicName = physicalNic.device;
/*      */ 
/*      */         
/*  905 */         Integer hostCount = Integer.valueOf(1);
/*  906 */         if (pnicNamesToHostCount.containsKey(pnicName)) {
/*  907 */           hostCount = Integer.valueOf(((Integer)pnicNamesToHostCount.get(pnicName)).intValue() + 1);
/*      */         }
/*      */         
/*  910 */         pnicNamesToHostCount.put(pnicName, hostCount);
/*      */         b1++; }
/*      */       
/*      */       b++; }
/*      */     
/*  915 */     for (Map.Entry<String, Integer> pnicNameToHostCount : pnicNamesToHostCount.entrySet()) {
/*  916 */       if (((Integer)pnicNameToHostCount.getValue()).intValue() == hostPropertyValues.length) {
/*  917 */         result.add(pnicNameToHostCount.getKey());
/*      */       }
/*      */       
/*  920 */       if (result.size() == number) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/*  925 */     return result;
/*      */   }
/*      */   
/*      */   private List<HostAdapter> generateNHostAdapters(List<String> names, int number) {
/*  929 */     List<HostAdapter> result = new ArrayList<>(number);
/*      */ 
/*      */     
/*  932 */     if (names.size() != number) {
/*  933 */       logger.warn("Inconsistent physical adapter naming across the hosts is found. Only suitable physical adapters are shown.");
/*      */     }
/*      */     
/*  936 */     for (int i = 0; i < names.size(); i++) {
/*  937 */       result.add(HostAdapter.create(
/*  938 */             Utils.getLocalizedString(
/*  939 */               "vsan.hci.configureCluster.longAdapterNamePattern", new String[] {
/*  940 */                 String.valueOf(i), 
/*  941 */                 names.get(i)
/*  942 */               }), names.get(i)));
/*      */     } 
/*      */ 
/*      */     
/*  946 */     return result;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<String> getUniqueNewDvsNames(ManagedObjectReference clusterRef) throws Exception {
/*  951 */     List<String> dvsNames = getExistingDvsNames(clusterRef);
/*      */     
/*  953 */     List<String> result = new ArrayList<>();
/*  954 */     for (int i = 0; i < 3; i++) {
/*  955 */       String newName = StringUtil.getIndexedString(
/*  956 */           dvsNames, 
/*  957 */           Utils.getLocalizedString("vsan.hci.configureCluster.dvs.defaultName"), 
/*  958 */           " ");
/*  959 */       dvsNames.add(newName);
/*  960 */       result.add(newName);
/*      */     } 
/*      */     
/*  963 */     return result;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<String> getExistingDvsNames(ManagedObjectReference clusterRef) throws Exception {
/*  968 */     PropertyConstraint id = QueryUtil.createPropertyConstraint(
/*  969 */         VmwareDistributedVirtualSwitch.class.getSimpleName(), 
/*  970 */         "serverGuid", 
/*  971 */         Comparator.EQUALS, 
/*  972 */         clusterRef.getServerGuid());
/*      */     
/*  974 */     String[] properties = { "name" };
/*  975 */     ResultSet resultSet = QueryUtil.getData(QueryUtil.buildQuerySpec((Constraint)id, properties));
/*  976 */     DataServiceResponse response = QueryUtil.getDataServiceResponse(resultSet, properties);
/*      */     
/*  978 */     ArrayList<String> dvsNames = new ArrayList<>(); byte b; int i; PropertyValue[] arrayOfPropertyValue;
/*  979 */     for (i = (arrayOfPropertyValue = response.getPropertyValues()).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue[b];
/*  980 */       dvsNames.add((String)propertyValue.value);
/*      */       b++; }
/*      */     
/*  983 */     return dvsNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private VsanConfigSpec getVsanConfigSpec(ManagedObjectReference clusterRef) throws Exception {
/*  992 */     VsanConfigSpec vsanConfigSpec = new VsanConfigSpec();
/*  993 */     List<String> propertiesToRequest = new ArrayList<>();
/*      */     
/*  995 */     vsanConfigSpec.largeScaleClusterSupport = getLargeScaleClusterSupport(clusterRef);
/*      */     
/*  997 */     if (VsanCapabilityUtils.isDeduplicationAndCompressionSupportedOnVc(clusterRef) && 
/*  998 */       VsanCapabilityUtils.isDeduplicationAndCompressionSupported(clusterRef)) {
/*  999 */       propertiesToRequest.add("dataEfficiencyStatus");
/*      */     }
/*      */     
/* 1002 */     if (VsanCapabilityUtils.isEncryptionSupportedOnVc(clusterRef) && 
/* 1003 */       VsanCapabilityUtils.isEncryptionSupported(clusterRef)) {
/* 1004 */       propertiesToRequest.add("vsanEncryptionStatus");
/*      */     }
/*      */     
/* 1007 */     if (CollectionUtils.isEmpty(propertiesToRequest)) {
/* 1008 */       return vsanConfigSpec;
/*      */     }
/*      */     
/* 1011 */     PropertyValue[] propertyValues = QueryUtil.getProperties(clusterRef, propertiesToRequest.<String>toArray(new String[0]))
/* 1012 */       .getPropertyValues(); byte b; int i;
/*      */     PropertyValue[] arrayOfPropertyValue1;
/* 1014 */     for (i = (arrayOfPropertyValue1 = propertyValues).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue1[b];
/* 1015 */       if ("dataEfficiencyStatus".equals(propertyValue.propertyName)) {
/* 1016 */         vsanConfigSpec.enabledDedup = ((Boolean)propertyValue.value).booleanValue();
/* 1017 */       } else if ("vsanEncryptionStatus".equals(propertyValue.propertyName)) {
/* 1018 */         EncryptionStatus config = (EncryptionStatus)propertyValue.value;
/* 1019 */         if (config == null || config.state == null) {
/* 1020 */           vsanConfigSpec.enableEncryption = false;
/*      */         }
/*      */         else {
/*      */           
/* 1024 */           switch (config.state) {
/*      */             case Enabled:
/* 1026 */               vsanConfigSpec.kmipClusterId = config.kmipClusterId;
/*      */             case EnabledNoKmip:
/* 1028 */               vsanConfigSpec.enableEncryption = true;
/*      */               break;
/*      */             
/*      */             default:
/* 1032 */               vsanConfigSpec.enableEncryption = false; break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       b++; }
/*      */     
/* 1038 */     return vsanConfigSpec;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public ConfigureWizardData getConfigureWizardData(ManagedObjectReference clusterRef) throws Exception {
/* 1043 */     ConfigureWizardData result = new ConfigureWizardData();
/* 1044 */     result.isStandalone = false;
/*      */     
/* 1046 */     BasicClusterConfigData basicClusterConfigData = getBasicClusterConfigData(clusterRef);
/*      */     
/* 1048 */     if (basicClusterConfigData.hciWorkflowState == HciWorkflowState.IN_PROGRESS) {
/* 1049 */       result.isExtend = false;
/* 1050 */       result.openYesNoDialog = false;
/* 1051 */       result.openWarningDialog = false;
/*      */       
/* 1053 */       boolean hasNetPermissions = hasHostConfigurePermissions(clusterRef);
/* 1054 */       result.optOutOfNetworking = !hasNetPermissions;
/* 1055 */       result.optOutOfNetworkingDisabled = !hasNetPermissions;
/*      */       
/* 1057 */       result.enableFaultDomainForSingleSiteCluster = basicClusterConfigData.vsanEnabled;
/*      */ 
/*      */       
/* 1060 */       result.showDvsPage = true;
/* 1061 */       result.showVmotionTrafficPage = basicClusterConfigData.drsEnabled;
/* 1062 */       result.showVsanTrafficPage = basicClusterConfigData.vsanEnabled;
/* 1063 */       result.showAdvancedOptionsPage = true;
/* 1064 */       result.showClaimDisksPage = basicClusterConfigData.vsanEnabled;
/*      */       
/* 1066 */       result.selectedVsanClusterType = 
/* 1067 */         basicClusterConfigData.vsanEnabled ? 
/* 1068 */         VsanClusterType.SINGLE_SITE_CLUSTER : 
/* 1069 */         VsanClusterType.NO_VSAN;
/*      */       
/* 1071 */       result.showFaultDomainsPageComponent = false;
/* 1072 */       result.showSingleSiteFaultDomainsPage = basicClusterConfigData.vsanEnabled;
/* 1073 */       result.showWitnessHostPageComponent = false;
/* 1074 */       result.showClaimDisksWitnessHostPage = false;
/*      */ 
/*      */ 
/*      */       
/* 1078 */       if (basicClusterConfigData.hosts > 32) {
/* 1079 */         result.largeScaleClusterSupport = true;
/*      */       } else {
/* 1081 */         result.largeScaleClusterSupport = getLargeScaleClusterSupport(clusterRef);
/*      */       } 
/*      */     } else {
/*      */       
/* 1085 */       result.isExtend = true;
/* 1086 */       result.optOutOfNetworking = true;
/*      */       
/* 1088 */       ClusterComputeResource.HCIConfigInfo hciConfig = 
/* 1089 */         (ClusterComputeResource.HCIConfigInfo)QueryUtil.getProperty(clusterRef, "hciConfig", null);
/*      */       
/* 1091 */       boolean drsEnabledInCreate = false;
/* 1092 */       boolean vsanEnabledInCreate = false;
/* 1093 */       if (hciConfig != null) {
/* 1094 */         ClusterComputeResource.DVSSetting[] dvsSettings = hciConfig.dvsSetting;
/* 1095 */         if (dvsSettings != null) {
/* 1096 */           result.optOutOfNetworking = false; byte b; int i; ClusterComputeResource.DVSSetting[] arrayOfDVSSetting;
/* 1097 */           for (i = (arrayOfDVSSetting = dvsSettings).length, b = 0; b < i; ) { ClusterComputeResource.DVSSetting dvsSetting = arrayOfDVSSetting[b];
/* 1098 */             ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping[] mappings = 
/* 1099 */               dvsSetting.dvPortgroupSetting;
/* 1100 */             if (mappings != null) {
/* 1101 */               byte b1; int j; ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping[] arrayOfDVPortgroupToServiceMapping; for (j = (arrayOfDVPortgroupToServiceMapping = mappings).length, b1 = 0; b1 < j; ) { ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping mapping = arrayOfDVPortgroupToServiceMapping[b1];
/* 1102 */                 Service service = Service.fromString(mapping.service);
/* 1103 */                 switch (service) {
/*      */                   case VMOTION:
/* 1105 */                     drsEnabledInCreate = true;
/*      */                     break;
/*      */                   case VSAN:
/* 1108 */                     vsanEnabledInCreate = true; break;
/*      */                 } 
/*      */                 b1++; }
/*      */             
/*      */             } 
/*      */             b++; }
/*      */         
/*      */         } 
/*      */       } 
/* 1117 */       String[] networkValidationMessages = null;
/* 1118 */       if (!result.optOutOfNetworking) {
/* 1119 */         networkValidationMessages = getNetworkValidationMessages(clusterRef, 
/* 1120 */             null, 
/* 1121 */             getNotConfiguredHosts(clusterRef), 
/* 1122 */             basicClusterConfigData.vsanEnabled);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1127 */       if (!result.optOutOfNetworking && !hasExtendNetworkingPermissions(clusterRef, hciConfig)) {
/* 1128 */         result.openWarningDialog = true;
/* 1129 */         result.dialogText = Utils.getLocalizedString("vsan.hci.dialog.configureHostsWarning.title");
/* 1130 */         return result;
/* 1131 */       }  if (ArrayUtils.isNotEmpty((Object[])networkValidationMessages)) {
/* 1132 */         result.openWarningDialog = true;
/* 1133 */         result.dialogText = Utils.getLocalizedString(
/* 1134 */             "vsan.hci.dialog.configureHostsWarning.networkConfigurationError.title");
/* 1135 */         result.warningDialogContent = networkValidationMessages;
/* 1136 */         return result;
/*      */       } 
/*      */       
/* 1139 */       result.showDvsPage = false;
/* 1140 */       result.showVmotionTrafficPage = drsEnabledInCreate;
/* 1141 */       result.showVsanTrafficPage = vsanEnabledInCreate;
/* 1142 */       result.showAdvancedOptionsPage = false;
/*      */ 
/*      */       
/* 1145 */       result.showClaimDisksPage = basicClusterConfigData.vsanEnabled;
/*      */       
/* 1147 */       if (basicClusterConfigData.vsanEnabled) {
/* 1148 */         result.selectedVsanClusterType = getVsanClusterType(clusterRef);
/* 1149 */         result.enableFaultDomainForSingleSiteCluster = true;
/* 1150 */         result.showFaultDomainsPageComponent = 
/* 1151 */           (result.selectedVsanClusterType == VsanClusterType.STRETCHED_CLUSTER);
/* 1152 */         result.showSingleSiteFaultDomainsPage = 
/* 1153 */           (result.selectedVsanClusterType == VsanClusterType.SINGLE_SITE_CLUSTER);
/*      */       } else {
/* 1155 */         result.selectedVsanClusterType = VsanClusterType.NO_VSAN;
/* 1156 */         result.enableFaultDomainForSingleSiteCluster = false;
/* 1157 */         result.showFaultDomainsPageComponent = false;
/* 1158 */         result.showSingleSiteFaultDomainsPage = false;
/*      */         
/* 1160 */         if (result.optOutOfNetworking) {
/*      */           
/* 1162 */           result.openYesNoDialog = true;
/* 1163 */           result.dialogText = Utils.getLocalizedString("vsan.hci.dialog.configureHostsConfirmation.title");
/*      */         } 
/*      */       } 
/*      */       
/* 1167 */       result.showWitnessHostPageComponent = false;
/* 1168 */       result.showClaimDisksWitnessHostPage = false;
/*      */     } 
/*      */     
/* 1171 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public boolean hasNetworkingModifyPermissions(ManagedObjectReference[] dvSwitches, ManagedObjectReference[] dvPortgroups) throws Exception {
/* 1180 */     boolean hasDvsCreatePermission = true;
/* 1181 */     boolean hasDvpgCreatePermission = true;
/* 1182 */     if (ArrayUtils.isNotEmpty((Object[])dvSwitches)) {
/* 1183 */       hasDvsCreatePermission = this.permissionService.havePermissions(dvSwitches, new String[] { "DVSwitch.HostOp" });
/*      */     }
/*      */     
/* 1186 */     if (ArrayUtils.isNotEmpty((Object[])dvPortgroups)) {
/* 1187 */       hasDvpgCreatePermission = this.permissionService.havePermissions(dvPortgroups, new String[] { "Network.Assign" });
/*      */     }
/*      */     
/* 1190 */     return (hasDvsCreatePermission && hasDvpgCreatePermission);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public boolean hasNetworkingCreatePermissions(ManagedObjectReference clusterRef, boolean checkDvsCreatePermission, boolean checkDvpgCreatePermission) throws Exception {
/* 1199 */     ManagedObjectReference datacenter = (ManagedObjectReference)QueryUtil.getProperty(clusterRef, "dc", null);
/* 1200 */     List<String> permissionsToCheck = new ArrayList<>();
/*      */     
/* 1202 */     if (checkDvsCreatePermission) {
/* 1203 */       permissionsToCheck.add("DVSwitch.Create");
/*      */     }
/*      */     
/* 1206 */     if (checkDvpgCreatePermission) {
/* 1207 */       permissionsToCheck.add("DVPortgroup.Create");
/*      */     }
/*      */     
/* 1210 */     return this.permissionService.hasPermissions(datacenter, permissionsToCheck.<String>toArray(new String[0]));
/*      */   }
/*      */   
/*      */   private boolean getLargeScaleClusterSupport(ManagedObjectReference clusterRef) throws Exception {
/* 1214 */     VsanVcClusterConfigSystem vsanConfigSystem = VsanProviderUtils.getVsanConfigSystem(clusterRef);
/* 1215 */     ConfigInfoEx configInfoEx = vsanConfigSystem.getConfigInfoEx(clusterRef);
/* 1216 */     VsanExtendedConfig originalExtendedConfig = configInfoEx.getExtendedConfig();
/* 1217 */     return originalExtendedConfig.largeScaleClusterSupport.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasHostConfigurePermissions(ManagedObjectReference clusterRef) throws Exception {
/* 1224 */     ManagedObjectReference[] hosts = (ManagedObjectReference[])QueryUtil.getProperty(clusterRef, "host", null);
/*      */     
/* 1226 */     boolean hasHostNetworkConfig = true; byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 1227 */     for (i = (arrayOfManagedObjectReference1 = hosts).length, b = 0; b < i; ) { ManagedObjectReference host = arrayOfManagedObjectReference1[b];
/* 1228 */       if (!this.permissionService.hasPermissions(host, new String[] { "Host.Config.Network" })) {
/* 1229 */         hasHostNetworkConfig = false;
/*      */         break;
/*      */       } 
/*      */       b++; }
/*      */     
/* 1234 */     return hasHostNetworkConfig;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasExtendNetworkingPermissions(ManagedObjectReference clusterRef, ClusterComputeResource.HCIConfigInfo hciConfig) throws Exception {
/* 1242 */     boolean hasDvsModify = true;
/* 1243 */     if (hciConfig.dvsSetting != null) {
/* 1244 */       byte b1; int j; ClusterComputeResource.DVSSetting[] arrayOfDVSSetting; for (j = (arrayOfDVSSetting = hciConfig.dvsSetting).length, b1 = 0; b1 < j; ) { ClusterComputeResource.DVSSetting dvsSetting = arrayOfDVSSetting[b1];
/* 1245 */         if (!this.permissionService.hasPermissions(dvsSetting.dvSwitch, new String[] { "DVSwitch.Modify" })) {
/* 1246 */           hasDvsModify = false;
/*      */           break;
/*      */         } 
/*      */         b1++; }
/*      */     
/*      */     } 
/* 1252 */     List<ManagedObjectReference> dvPortgroups = getDvPortgroups(hciConfig);
/* 1253 */     boolean hasNetworkAssign = true;
/* 1254 */     for (ManagedObjectReference dvPortgroup : dvPortgroups) {
/* 1255 */       if (!this.permissionService.hasPermissions(dvPortgroup, new String[] { "Network.Assign" })) {
/* 1256 */         hasNetworkAssign = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1261 */     ManagedObjectReference[] notConfiguredHosts = getNotConfiguredHosts(clusterRef);
/* 1262 */     boolean hasHostNetworkConfig = true; byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 1263 */     for (i = (arrayOfManagedObjectReference1 = notConfiguredHosts).length, b = 0; b < i; ) { ManagedObjectReference host = arrayOfManagedObjectReference1[b];
/* 1264 */       if (!this.permissionService.hasPermissions(host, new String[] { "Host.Config.Network" })) {
/* 1265 */         hasHostNetworkConfig = false;
/*      */         break;
/*      */       } 
/*      */       b++; }
/*      */     
/* 1270 */     return (hasDvsModify && hasNetworkAssign && hasHostNetworkConfig);
/*      */   }
/*      */   
/*      */   private VsanClusterType getVsanClusterType(ManagedObjectReference clusterRef) throws Exception {
/* 1274 */     PropertyValue[] witnessHostValues = QueryUtil.getPropertyForRelatedObjects(
/* 1275 */         clusterRef, 
/* 1276 */         "allVsanHosts", 
/* 1277 */         ClusterComputeResource.class.getSimpleName(), 
/* 1278 */         "isWitnessHost").getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 1279 */     for (i = (arrayOfPropertyValue1 = witnessHostValues).length, b = 0; b < i; ) { PropertyValue witnessHostValue = arrayOfPropertyValue1[b];
/* 1280 */       if (((Boolean)witnessHostValue.value).booleanValue()) {
/* 1281 */         return VsanClusterType.STRETCHED_CLUSTER;
/*      */       }
/*      */       b++; }
/*      */     
/* 1285 */     return VsanClusterType.SINGLE_SITE_CLUSTER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Service, DvsData> getDvsInfoData(ClusterComputeResource.HCIConfigInfo hciConfigInfo) throws Exception {
/* 1292 */     Map<Service, DvsData> dvsDataByService = new HashMap<>();
/* 1293 */     Map<Service, ManagedObjectReference> dvpgMorByService = new HashMap<>();
/* 1294 */     Map<Service, ManagedObjectReference> dvsMorByService = new HashMap<>();
/*      */     
/* 1296 */     if (hciConfigInfo == null) {
/* 1297 */       return dvsDataByService;
/*      */     }
/*      */     
/* 1300 */     ClusterComputeResource.DVSSetting[] dvsSettings = hciConfigInfo.getDvsSetting();
/* 1301 */     if (ArrayUtils.isEmpty((Object[])dvsSettings))
/* 1302 */       return dvsDataByService;  byte b;
/*      */     int i;
/*      */     ClusterComputeResource.DVSSetting[] arrayOfDVSSetting1;
/* 1305 */     for (i = (arrayOfDVSSetting1 = dvsSettings).length, b = 0; b < i; ) { ClusterComputeResource.DVSSetting dvsSetting = arrayOfDVSSetting1[b];
/* 1306 */       ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping[] dvPortgroupSetting = 
/* 1307 */         dvsSetting.dvPortgroupSetting;
/* 1308 */       if (ArrayUtils.isNotEmpty((Object[])dvPortgroupSetting)) {
/*      */         ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping[] arrayOfDVPortgroupToServiceMapping;
/* 1310 */         int j = (arrayOfDVPortgroupToServiceMapping = dvPortgroupSetting).length; byte b1 = 0; for (; b1 < j; b1++) { ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping dvpgSetting = arrayOfDVPortgroupToServiceMapping[b1];
/* 1311 */           if (dvpgSetting != null && (
/* 1312 */             Service.fromString(dvpgSetting.service) == Service.VSAN || 
/* 1313 */             Service.fromString(dvpgSetting.service) == Service.VMOTION)) {
/* 1314 */             dvsMorByService.put(Service.fromString(dvpgSetting.service), dvsSetting.dvSwitch);
/* 1315 */             dvpgMorByService.put(Service.fromString(dvpgSetting.service), dvpgSetting.dvPortgroup);
/*      */           }  }
/*      */       
/*      */       } 
/*      */       b++; }
/*      */     
/* 1321 */     if (CollectionUtils.isNotEmpty(dvsMorByService.values())) {
/* 1322 */       DataServiceResponse responseForDvs = 
/* 1323 */         QueryUtil.getProperties(dvsMorByService.values().toArray(), new String[] { "name" });
/* 1324 */       setDvsDataByService(dvsDataByService, dvsMorByService, responseForDvs);
/*      */     } 
/*      */     
/* 1327 */     if (CollectionUtils.isNotEmpty(dvpgMorByService.values())) {
/* 1328 */       DataServiceResponse responseForDvpg = QueryUtil.getProperties(
/* 1329 */           dvpgMorByService.values().toArray(), new String[] { "config.defaultPortConfig.vlan" });
/* 1330 */       setDvsDataByService(dvsDataByService, dvpgMorByService, responseForDvpg);
/*      */     } 
/*      */     
/* 1333 */     return dvsDataByService;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public String[] validateNetworkSpecification(ManagedObjectReference clusterRef, ClusterConfigData clusterConfigData) throws Exception {
/* 1338 */     return getNetworkValidationMessages(clusterRef, 
/* 1339 */         clusterConfigData.getDvsProfiles(), 
/* 1340 */         null, 
/* 1341 */         clusterConfigData.basicConfig.vsanEnabled);
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<String> getConsequentHostAddresses(IpAddressesRequestSpec ipsRequestSpec) {
/* 1346 */     SubnetUtils subnetUtils = new SubnetUtils(ipsRequestSpec.ipAddress, ipsRequestSpec.subnetMask);
/* 1347 */     SubnetUtils.SubnetInfo subnetInfo = subnetUtils.getInfo();
/*      */ 
/*      */     
/* 1350 */     long addressCount = (ipsRequestSpec.hostsNumber - 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1356 */     int initialAddressInt = subnetInfo.asInteger(ipsRequestSpec.ipAddress) + 1;
/* 1357 */     int highestAddressInt = subnetInfo.asInteger(subnetInfo.getHighAddress());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1362 */     List<String> result = new ArrayList<>();
/* 1363 */     int currentAddressInt = initialAddressInt, idx = 0;
/* 1364 */     for (; currentAddressInt <= highestAddressInt && idx < addressCount; 
/* 1365 */       currentAddressInt++, idx++) {
/* 1366 */       result.add(formatIpv4Address(ipv4IntToArray(currentAddressInt)));
/*      */     }
/*      */     
/* 1369 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] ipv4IntToArray(int value) {
/* 1376 */     int[] result = new int[4];
/* 1377 */     for (int j = 3; j >= 0; j--) {
/* 1378 */       result[j] = result[j] | value >>> 8 * (3 - j) & 0xFF;
/*      */     }
/* 1380 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String formatIpv4Address(int[] octets) {
/* 1387 */     StringBuilder result = new StringBuilder();
/* 1388 */     for (int i = 0; i < octets.length; i++) {
/* 1389 */       result.append(octets[i]);
/* 1390 */       if (i != octets.length - 1) {
/* 1391 */         result.append(".");
/*      */       }
/*      */     } 
/*      */     
/* 1395 */     return result.toString();
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public List<String> getExistingPgNames(ManagedObjectReference clusterRef) throws Exception {
/* 1400 */     PropertyConstraint id = QueryUtil.createPropertyConstraint(
/* 1401 */         DistributedVirtualPortgroup.class.getSimpleName(), 
/* 1402 */         "serverGuid", 
/* 1403 */         Comparator.EQUALS, 
/* 1404 */         clusterRef.getServerGuid());
/*      */ 
/*      */     
/* 1407 */     String[] properties = { "name" };
/* 1408 */     ResultSet resultSet = QueryUtil.getData(QueryUtil.buildQuerySpec((Constraint)id, properties));
/* 1409 */     DataServiceResponse response = QueryUtil.getDataServiceResponse(resultSet, properties);
/*      */     
/* 1411 */     ArrayList<String> pgNames = new ArrayList<>(); byte b; int i; PropertyValue[] arrayOfPropertyValue;
/* 1412 */     for (i = (arrayOfPropertyValue = response.getPropertyValues()).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue[b];
/* 1413 */       pgNames.add((String)propertyValue.value);
/*      */       b++; }
/*      */     
/* 1416 */     return pgNames;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<ExistingDvsData> getExistingDvs(ManagedObjectReference clusterRef, String selectedDvsName) throws Exception {
/* 1422 */     List<ExistingDvsData> result = new ArrayList<>();
/* 1423 */     List<ManagedObjectReference> hostsInCluster = new ArrayList<>();
/*      */     
/* 1425 */     PropertyValue[] hostValues = QueryUtil.getPropertyForRelatedObjects(
/* 1426 */         clusterRef, 
/* 1427 */         "host", 
/* 1428 */         ClusterComputeResource.class.getSimpleName(), 
/* 1429 */         "config.product.version").getPropertyValues();
/*      */     
/* 1431 */     if (hostValues == null) {
/* 1432 */       return result;
/*      */     }
/*      */ 
/*      */     
/* 1436 */     int lowestHostVersion = Integer.MAX_VALUE; byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 1437 */     for (i = (arrayOfPropertyValue1 = hostValues).length, b = 0; b < i; ) { PropertyValue hostValue = arrayOfPropertyValue1[b];
/* 1438 */       hostsInCluster.add((ManagedObjectReference)hostValue.resourceObject);
/*      */       
/* 1440 */       int currentHostVersion = Integer.parseInt(((String)hostValue.value).replaceAll("\\.", ""));
/* 1441 */       if (currentHostVersion < lowestHostVersion) {
/* 1442 */         lowestHostVersion = currentHostVersion;
/*      */       }
/*      */       b++; }
/*      */     
/* 1446 */     Map<Object, Map<String, Object>> responseForDvs = queryDvsProperties(clusterRef);
/*      */     
/* 1448 */     if (responseForDvs == null) {
/* 1449 */       return result;
/*      */     }
/*      */     
/* 1452 */     for (Map.Entry<Object, Map<String, Object>> dvsEntry : responseForDvs.entrySet()) {
/* 1453 */       Map<String, Object> dvsProperties = dvsEntry.getValue();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1458 */       if (isDvsVersionIncompatible((String)dvsProperties.get("config.productInfo.version"), lowestHostVersion) || 
/* 1459 */         isDvsConnectedToHostInCluster(hostsInCluster, (HostMember[])dvsProperties.get("config.host"))) {
/*      */         continue;
/*      */       }
/*      */       
/* 1463 */       ExistingDvsData existingDvsData = new ExistingDvsData();
/* 1464 */       existingDvsData.dvsRef = (ManagedObjectReference)dvsEntry.getKey();
/* 1465 */       existingDvsData.name = (String)dvsProperties.get("name");
/* 1466 */       existingDvsData.version = (String)dvsProperties.get("config.productInfo.version");
/* 1467 */       existingDvsData.niocVersion = (String)dvsProperties.get("lacpVersionColumnLabelDerived");
/* 1468 */       existingDvsData.lacpVersion = (String)dvsProperties.get("niocVersionColumnLabel");
/*      */ 
/*      */       
/* 1471 */       if (existingDvsData.name.equals(selectedDvsName)) {
/* 1472 */         existingDvsData.isSelected = true;
/* 1473 */         result.add(0, existingDvsData); continue;
/*      */       } 
/* 1475 */       result.add(existingDvsData);
/*      */     } 
/*      */ 
/*      */     
/* 1479 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public List<ExistingDvpgData> getExistingDvpg(ManagedObjectReference dvsRef, String selectedDvpgName) throws Exception {
/* 1485 */     List<ExistingDvpgData> result = new ArrayList<>();
/*      */     
/* 1487 */     Map<Object, Map<String, Object>> dvpgResponse = QueryUtil.getPropertiesForRelatedObjects(
/* 1488 */         dvsRef, 
/* 1489 */         "portgroup", 
/* 1490 */         DistributedVirtualPortgroup.class.getSimpleName(), 
/* 1491 */         new String[] { "name", "config.uplink" }).getMap();
/*      */     
/* 1493 */     if (MapUtils.isEmpty(dvpgResponse)) {
/* 1494 */       return result;
/*      */     }
/*      */     
/* 1497 */     for (Map.Entry<Object, Map<String, Object>> dvpgEntry : dvpgResponse.entrySet()) {
/* 1498 */       Map<String, Object> dvpgProperties = dvpgEntry.getValue();
/*      */       
/* 1500 */       if (((Boolean)dvpgProperties.get("config.uplink")).booleanValue()) {
/*      */         continue;
/*      */       }
/*      */       
/* 1504 */       ExistingDvpgData existingDvpgData = new ExistingDvpgData();
/* 1505 */       existingDvpgData.dvpgRef = (ManagedObjectReference)dvpgEntry.getKey();
/* 1506 */       existingDvpgData.name = (String)dvpgProperties.get("name");
/*      */ 
/*      */       
/* 1509 */       if (existingDvpgData.name.equals(selectedDvpgName)) {
/* 1510 */         existingDvpgData.isSelected = true;
/* 1511 */         result.add(0, existingDvpgData); continue;
/*      */       } 
/* 1513 */       result.add(existingDvpgData);
/*      */     } 
/*      */ 
/*      */     
/* 1517 */     return result;
/*      */   }
/*      */   
/*      */   @TsService
/*      */   public VlanData getDvpgVlan(ManagedObjectReference dvpgRef) throws Exception {
/* 1522 */     VmwareDistributedVirtualSwitch.VlanSpec dvpgVlanSpec = 
/* 1523 */       (VmwareDistributedVirtualSwitch.VlanSpec)QueryUtil.getProperty(dvpgRef, "config.defaultPortConfig.vlan", null);
/*      */     
/* 1525 */     if (dvpgVlanSpec == null) {
/* 1526 */       return null;
/*      */     }
/*      */     
/* 1529 */     return getVlanData(dvpgVlanSpec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String[] getNetworkValidationMessages(ManagedObjectReference clusterRef, ClusterComputeResource.DvsProfile[] dvsProfiles, ManagedObjectReference[] notConfiguredHosts, boolean vsanEnabled) throws Exception {
/* 1536 */     VcConnection vcConnection = getVcConnection(clusterRef, vsanEnabled);
/* 1537 */     List<String> messages = new ArrayList<>(); try {
/* 1538 */       Exception exception2, exception1 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1564 */     catch (InvalidState invalidState) {
/* 1565 */       return new String[] {
/* 1566 */           Utils.getLocalizedString("vsan.hci.configureCluster.dvsVerification.clusterNotInHCI")
/*      */         };
/*      */     } 
/* 1569 */     return CollectionUtils.isNotEmpty(messages) ? messages.<String>toArray(new String[0]) : null;
/*      */   }
/*      */   private void setDvsDataByService(Map<Service, DvsData> dvsDataByService, Map<Service, ManagedObjectReference> morByService, DataServiceResponse response) {
/*      */     byte b;
/*      */     int i;
/*      */     PropertyValue[] arrayOfPropertyValue;
/* 1575 */     for (i = (arrayOfPropertyValue = response.getPropertyValues()).length, b = 0; b < i; ) { PropertyValue propertyValue = arrayOfPropertyValue[b];
/* 1576 */       if ("name".equals(propertyValue.propertyName)) {
/* 1577 */         String switchName = (String)propertyValue.value;
/* 1578 */         for (Map.Entry<Service, ManagedObjectReference> entry : morByService.entrySet()) {
/* 1579 */           if (((ManagedObjectReference)entry.getValue()).equals(propertyValue.resourceObject)) {
/* 1580 */             if (dvsDataByService.get(entry.getKey()) == null) {
/* 1581 */               dvsDataByService.put(entry.getKey(), new DvsData());
/*      */             }
/*      */             
/* 1584 */             ((DvsData)dvsDataByService.get(entry.getKey())).dvsName = switchName;
/*      */           } 
/*      */         } 
/* 1587 */       } else if ("config.defaultPortConfig.vlan".equals(propertyValue.propertyName)) {
/* 1588 */         VmwareDistributedVirtualSwitch.VlanSpec dvpgVlanSpec = 
/* 1589 */           (VmwareDistributedVirtualSwitch.VlanSpec)propertyValue.value;
/*      */         
/* 1591 */         VlanData vlanData = getVlanData(dvpgVlanSpec);
/*      */         
/* 1593 */         for (Map.Entry<Service, ManagedObjectReference> entry : morByService.entrySet()) {
/* 1594 */           if (((ManagedObjectReference)entry.getValue()).equals(propertyValue.resourceObject)) {
/* 1595 */             if (dvsDataByService.get(entry.getKey()) == null) {
/* 1596 */               dvsDataByService.put(entry.getKey(), new DvsData());
/*      */             }
/*      */             
/* 1599 */             ((DvsData)dvsDataByService.get(entry.getKey())).vlan = vlanData.vlan;
/* 1600 */             ((DvsData)dvsDataByService.get(entry.getKey())).vlanType = vlanData.vlanType;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       b++; }
/*      */   
/*      */   }
/*      */   private VlanData getVlanData(VmwareDistributedVirtualSwitch.VlanSpec dvpgVlanSpec) {
/* 1608 */     VlanData vlanData = new VlanData();
/* 1609 */     vlanData.vlan = "0";
/* 1610 */     vlanData.vlanType = VlanType.NONE;
/* 1611 */     if (dvpgVlanSpec != null) {
/* 1612 */       if (dvpgVlanSpec instanceof VmwareDistributedVirtualSwitch.VlanIdSpec) {
/* 1613 */         vlanData.vlan = String.valueOf(((VmwareDistributedVirtualSwitch.VlanIdSpec)dvpgVlanSpec).vlanId);
/* 1614 */         vlanData.vlanType = VlanType.VLAN_ID;
/* 1615 */       } else if (dvpgVlanSpec instanceof VmwareDistributedVirtualSwitch.TrunkVlanSpec) {
/* 1616 */         NumericRange[] trunkRanges = ((VmwareDistributedVirtualSwitch.TrunkVlanSpec)dvpgVlanSpec).vlanId;
/* 1617 */         vlanData.vlan = StringUtil.parseNumericRange(trunkRanges);
/* 1618 */         vlanData.vlanType = VlanType.VLAN_TRUNK;
/* 1619 */       } else if (dvpgVlanSpec instanceof VmwareDistributedVirtualSwitch.PvlanSpec) {
/* 1620 */         vlanData.vlan = String.valueOf(((VmwareDistributedVirtualSwitch.PvlanSpec)dvpgVlanSpec).pvlanId);
/* 1621 */         vlanData.vlanType = VlanType.PVLAN;
/*      */       } 
/*      */     }
/*      */     
/* 1625 */     return vlanData;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean hasAddHostsPermissions(ManagedObjectReference clusterRef, boolean vsanEnabled, boolean hasEditClusterPermission) throws Exception {
/* 1630 */     ManagedObjectReference hostFolder = 
/*      */ 
/*      */ 
/*      */       
/* 1634 */       (ManagedObjectReference)(QueryUtil.getPropertyForRelatedObjects(clusterRef, "dc", ClusterComputeResource.class.getSimpleName(), "hostFolder").getPropertyValues()[0]).value;
/*      */     
/* 1636 */     boolean hasHostPermissions = this.permissionService.hasPermissions(hostFolder, 
/* 1637 */         new String[] { "Host.Inventory.AddStandaloneHost", "Host.Inventory.MoveHost" });
/*      */ 
/*      */     
/* 1640 */     if (vsanEnabled) {
/* 1641 */       return (hasHostPermissions && hasEditClusterPermission);
/*      */     }
/*      */     
/* 1644 */     return hasHostPermissions;
/*      */   }
/*      */   
/*      */   private List<ManagedObjectReference> getDvPortgroups(ClusterComputeResource.HCIConfigInfo hciConfig) {
/* 1648 */     List<ManagedObjectReference> result = new ArrayList<>();
/* 1649 */     if (hciConfig.dvsSetting != null) {
/* 1650 */       byte b; int i; ClusterComputeResource.DVSSetting[] arrayOfDVSSetting; for (i = (arrayOfDVSSetting = hciConfig.dvsSetting).length, b = 0; b < i; ) { ClusterComputeResource.DVSSetting dvsSetting = arrayOfDVSSetting[b];
/* 1651 */         if (dvsSetting.dvPortgroupSetting != null) {
/* 1652 */           byte b1; int j; ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping[] arrayOfDVPortgroupToServiceMapping; for (j = (arrayOfDVPortgroupToServiceMapping = dvsSetting.dvPortgroupSetting).length, b1 = 0; b1 < j; ) { ClusterComputeResource.DVSSetting.DVPortgroupToServiceMapping mapping = arrayOfDVPortgroupToServiceMapping[b1];
/* 1653 */             result.add(mapping.dvPortgroup); b1++; }
/*      */         
/*      */         } 
/*      */         b++; }
/*      */     
/*      */     } 
/* 1659 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @TsService
/*      */   public VsanTestData getHealthGroupData(ManagedObjectReference clusterRef, String perspective, String group) throws Exception {
/* 1666 */     VsanClusterHealthSummary healthSummary = getHealthSummary(clusterRef, perspective);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1671 */     Set<ManagedObjectReference> allMoRefs = new HashSet<>();
/* 1672 */     if (healthSummary != null && healthSummary.groups != null) {
/* 1673 */       byte b1; int j; VsanClusterHealthGroup[] arrayOfVsanClusterHealthGroup1; for (j = (arrayOfVsanClusterHealthGroup1 = healthSummary.groups).length, b1 = 0; b1 < j; ) { VsanClusterHealthGroup healthGroup = arrayOfVsanClusterHealthGroup1[b1];
/* 1674 */         VsanHealthUtil.addToTestMoRefs(healthGroup, allMoRefs, clusterRef.getServerGuid()); b1++; }
/*      */     
/*      */     }  byte b; int i;
/*      */     VsanClusterHealthGroup[] arrayOfVsanClusterHealthGroup;
/* 1678 */     for (i = (arrayOfVsanClusterHealthGroup = healthSummary.groups).length, b = 0; b < i; ) { VsanClusterHealthGroup currentGroup = arrayOfVsanClusterHealthGroup[b];
/* 1679 */       if (currentGroup.groupName.equals(group)) {
/* 1680 */         return new VsanTestData(currentGroup, VsanHealthUtil.getNamesForMoRefs(allMoRefs));
/*      */       }
/*      */       b++; }
/*      */     
/* 1684 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private VsanClusterHealthSummary getHealthSummary(ManagedObjectReference clusterRef, String perspective) throws Exception {
/* 1689 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(clusterRef);
/*      */     
/* 1691 */     Exception exception1 = null, exception2 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*      */     
/*      */     } finally {
/* 1702 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*      */     
/*      */     } 
/*      */   } private Map<Object, Map<String, Object>> queryDvsProperties(ManagedObjectReference clusterRef) throws Exception {
/* 1706 */     ManagedObjectReference datacenter = (ManagedObjectReference)QueryUtil.getProperty(clusterRef, "dc", null);
/*      */     
/* 1708 */     PropertyConstraint id = QueryUtil.createPropertyConstraint(
/* 1709 */         VmwareDistributedVirtualSwitch.class.getSimpleName(), 
/* 1710 */         "dc", 
/* 1711 */         Comparator.EQUALS, 
/* 1712 */         datacenter);
/*      */ 
/*      */     
/* 1715 */     ResultSet resultSet = QueryUtil.getData(QueryUtil.buildQuerySpec((Constraint)id, EXISTING_DVS_PROPERTIES));
/* 1716 */     DataServiceResponse dvsRefResponse = QueryUtil.getDataServiceResponse(resultSet, EXISTING_DVS_PROPERTIES);
/*      */     
/* 1718 */     return dvsRefResponse.getMap();
/*      */   }
/*      */   
/*      */   private boolean isDvsVersionIncompatible(String dvsVersion, int lowestHostVersion) {
/* 1722 */     int parsedDvsVersion = Integer.parseInt(dvsVersion.replaceAll("\\.", ""));
/* 1723 */     return (lowestHostVersion < parsedDvsVersion);
/*      */   }
/*      */   
/*      */   private boolean isDvsConnectedToHostInCluster(List<ManagedObjectReference> hostsInCluster, HostMember[] hostMembers) {
/* 1727 */     if (CollectionUtils.isNotEmpty(hostsInCluster) && ArrayUtils.isNotEmpty((Object[])hostMembers)) {
/* 1728 */       byte b; int i; HostMember[] arrayOfHostMember; for (i = (arrayOfHostMember = hostMembers).length, b = 0; b < i; ) { HostMember hostMember = arrayOfHostMember[b];
/* 1729 */         if (hostMember != null && hostMember.config != null && hostMember.config.host != null && 
/* 1730 */           hostsInCluster.contains(hostMember.config.host)) {
/* 1731 */           return true;
/*      */         }
/*      */         b++; }
/*      */     
/*      */     } 
/* 1736 */     return false;
/*      */   }
/*      */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/HciClusterService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */