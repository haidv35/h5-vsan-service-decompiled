/*     */ package com.vmware.vsphere.client.vsan.stretched;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.ResourceSpec;
/*     */ import com.vmware.vise.data.query.DataException;
/*     */ import com.vmware.vise.data.query.DataProviderAdapter;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.ObjectIdentityConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.RelationalConstraint;
/*     */ import com.vmware.vise.data.query.RequestSpec;
/*     */ import com.vmware.vise.data.query.Response;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanWitnessHostProvider
/*     */   implements DataProviderAdapter
/*     */ {
/*     */   public static final String PREFERRED_FD_PROPERTY = "preferredFaultDomain";
/*     */   public static final String UNICAST_AGENT_ADDRESS = "unicastAgentAddress";
/*     */   public static final String IS_WITNESS_HOST_PROPERTY = "isWitnessHost";
/*  36 */   private static final Log _logger = LogFactory.getLog(VsanWitnessHostProvider.class);
/*     */   
/*     */   private final DataServiceExtensionRegistry _registry;
/*     */   @Autowired
/*     */   private VsanStretchedClusterPropertyProvider stretchedClusterPropertyProvider;
/*     */   
/*     */   public VsanWitnessHostProvider(DataServiceExtensionRegistry registry) {
/*  43 */     this._registry = registry;
/*  44 */     this._registry.registerDataAdapter(this, new String[] { ClusterComputeResource.class.getSimpleName() });
/*     */   }
/*     */ 
/*     */   
/*     */   public Response getData(RequestSpec requestSpec) {
/*  49 */     if (requestSpec == null || requestSpec.querySpec == null) {
/*  50 */       throw new IllegalArgumentException("requestSpec");
/*     */     }
/*  52 */     ArrayList<ResultSet> resultSets = new ArrayList<>(requestSpec.querySpec.length); byte b; int i;
/*     */     QuerySpec[] arrayOfQuerySpec;
/*  54 */     for (i = (arrayOfQuerySpec = requestSpec.querySpec).length, b = 0; b < i; ) { QuerySpec spec = arrayOfQuerySpec[b];
/*  55 */       ResultSet resultSet = new ResultSet();
/*  56 */       RequestData requestData = getClusterRefs(spec);
/*     */       
/*  58 */       if (!requestData.clusterRefs.isEmpty()) {
/*     */         try {
/*  60 */           resultSet = getHosts(requestData);
/*  61 */         } catch (Exception error) {
/*  62 */           _logger.error("Error retrieving witness hosts: ", error);
/*  63 */           resultSet.error = (Exception)DataException.newInstance(error);
/*     */         } 
/*     */       }
/*  66 */       resultSets.add(resultSet); b++; }
/*     */     
/*  68 */     Response response = new Response();
/*  69 */     response.resultSet = resultSets.<ResultSet>toArray(new ResultSet[resultSets.size()]);
/*  70 */     return response;
/*     */   }
/*     */   
/*     */   private RequestData getClusterRefs(QuerySpec spec) {
/*  74 */     if (spec == null) {
/*  75 */       return new RequestData();
/*     */     }
/*  77 */     ResourceSpec resourceSpec = spec.resourceSpec;
/*     */     
/*  79 */     if (resourceSpec == null) {
/*  80 */       return new RequestData();
/*     */     }
/*     */     
/*  83 */     if (resourceSpec.constraint == null) {
/*  84 */       return new RequestData();
/*     */     }
/*     */     
/*  87 */     return getClusterRefs(resourceSpec.constraint);
/*     */   }
/*     */   
/*     */   private RequestData getClusterRefs(Constraint constraint) {
/*  91 */     if (constraint instanceof RelationalConstraint) {
/*  92 */       RelationalConstraint relConstraint = (RelationalConstraint)constraint;
/*  93 */       if ("witnessHost".equals(relConstraint.relation) || 
/*  94 */         "allVsanHosts".equals(relConstraint.relation))
/*     */       {
/*     */ 
/*     */         
/*  98 */         if (relConstraint.constraintOnRelatedObject instanceof ObjectIdentityConstraint) {
/*  99 */           ObjectIdentityConstraint oiConstraint = 
/* 100 */             (ObjectIdentityConstraint)relConstraint.constraintOnRelatedObject;
/* 101 */           if (oiConstraint.target != null) {
/* 102 */             return new RequestData(
/* 103 */                 Arrays.asList(new ManagedObjectReference[] { (ManagedObjectReference)oiConstraint.target
/* 104 */                   }, ), "allVsanHosts".equals(relConstraint.relation));
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 109 */     return new RequestData();
/*     */   }
/*     */   
/*     */   private ResultSet getHosts(RequestData requestData) throws Exception {
/* 113 */     List<WitnessHostData> witnessHosts = getWitnessHosts(requestData.clusterRefs);
/* 114 */     List<ManagedObjectReference> regularHosts = getHostsInCluster(requestData);
/*     */     
/* 116 */     ResultItem[] resultItems = new ResultItem[witnessHosts.size() + regularHosts.size()];
/* 117 */     int index = 0;
/* 118 */     for (WitnessHostData witnessData : witnessHosts) {
/* 119 */       resultItems[index++] = createResultItem(witnessData.witnessHost, 
/* 120 */           witnessData.preferredFaultDomainName, witnessData.unicastAgentAddress, true);
/*     */     }
/* 122 */     for (ManagedObjectReference hostRef : regularHosts) {
/* 123 */       resultItems[index++] = createResultItem(hostRef, "", "", false);
/*     */     }
/*     */     
/* 126 */     return QueryUtil.newResultSet(resultItems);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ManagedObjectReference> getHostsInCluster(RequestData requestData) throws Exception {
/* 131 */     List<ManagedObjectReference> hosts = new ArrayList<>();
/* 132 */     if (requestData.isAllHostsRelation) {
/* 133 */       PropertyValue[] propValues = QueryUtil.getProperties(
/* 134 */           requestData.clusterRefs.toArray(), new String[] { "host" }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 135 */       for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 136 */         ManagedObjectReference[] clusterHosts = (ManagedObjectReference[])propValue.value;
/* 137 */         if (clusterHosts != null) {
/* 138 */           byte b1; int j; ManagedObjectReference[] arrayOfManagedObjectReference; for (j = (arrayOfManagedObjectReference = clusterHosts).length, b1 = 0; b1 < j; ) { ManagedObjectReference hostRef = arrayOfManagedObjectReference[b1];
/* 139 */             hosts.add(hostRef); b1++; }
/*     */         
/*     */         }  b++; }
/*     */     
/*     */     } 
/* 144 */     return hosts;
/*     */   }
/*     */   
/*     */   private List<WitnessHostData> getWitnessHosts(List<ManagedObjectReference> clusterRefs) {
/* 148 */     List<WitnessHostData> allWitnessHosts = new ArrayList<>();
/*     */     try {
/* 150 */       for (ManagedObjectReference clusterRef : clusterRefs) {
/* 151 */         List<WitnessHostData> witnessHosts = 
/* 152 */           this.stretchedClusterPropertyProvider.getWitnessHosts(clusterRef);
/* 153 */         if (witnessHosts == null) {
/*     */           continue;
/*     */         }
/* 156 */         allWitnessHosts.addAll(witnessHosts);
/*     */       } 
/* 158 */     } catch (Exception ex) {
/* 159 */       _logger.error("Could not retrieve witness hosts", ex);
/*     */     } 
/*     */     
/* 162 */     return allWitnessHosts;
/*     */   }
/*     */ 
/*     */   
/*     */   private ResultItem createResultItem(ManagedObjectReference moRef, String preferredFd, String unicastAgentAddress, boolean isWitnessHost) {
/* 167 */     return QueryUtil.newResultItem(
/* 168 */         moRef, new PropertyValue[] {
/* 169 */           QueryUtil.newProperty("preferredFaultDomain", getNotNull(preferredFd)), 
/* 170 */           QueryUtil.newProperty("unicastAgentAddress", getNotNull(unicastAgentAddress)), 
/* 171 */           QueryUtil.newProperty("isWitnessHost", Boolean.valueOf(isWitnessHost))
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getNotNull(String value) {
/* 178 */     return (value == null) ? "" : value;
/*     */   }
/*     */   
/*     */   private class RequestData {
/* 182 */     public List<ManagedObjectReference> clusterRefs = new ArrayList<>();
/*     */     public boolean isAllHostsRelation = false;
/*     */     
/*     */     public RequestData(List<ManagedObjectReference> clusterRefs, boolean isAllHostsRelation) {
/* 186 */       this.clusterRefs = clusterRefs;
/* 187 */       this.isAllHostsRelation = isAllHostsRelation;
/*     */     }
/*     */     
/*     */     public RequestData() {}
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/VsanWitnessHostProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */