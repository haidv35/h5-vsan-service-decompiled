/*     */ package com.vmware.vsphere.client.vsan.health.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultBase;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultRow;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultTable;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthTest;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.query.Comparator;
/*     */ import com.vmware.vise.data.query.Conjoiner;
/*     */ import com.vmware.vise.data.query.PropertyConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import com.vmware.vsphere.client.vsan.health.ColumnType;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanHealthServiceGoalState;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanHealthServiceStatus;
/*     */ import com.vmware.vsphere.client.vsan.health.VsanHealthServiceSubStatus;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.ObjectMapper;
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
/*     */ public class VsanHealthUtil
/*     */ {
/*     */   private static final String HEALTH_STATUS = "status";
/*     */   private static final String HEALTH_GOAL_STATE = "goalState";
/*     */   private static final String STATUS_ISSUE = "statusIssue";
/*     */   public static final String TASK_TYPE = "Task";
/*     */   public static final String VSAN_INTERNET_ACCESS_ENABLED = "enableInternetAccess";
/*     */   private static final String NAME_PROPERTY = "name";
/*     */   private static final String HOST_CONNECTION_STATE_PROPERTY = "runtime.connectionState";
/*     */   private static final String HOST_VERSION_PROPERTY = "config.product.version";
/*  56 */   private static final Log _logger = LogFactory.getLog(VsanHealthUtil.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanHealthServiceStatus getVsanHealthServiceStatus(String status) throws Exception {
/*  67 */     ObjectMapper objectMapper = new ObjectMapper();
/*  68 */     JsonNode root = objectMapper.readTree(status);
/*     */     
/*  70 */     if (root != null) {
/*  71 */       VsanHealthServiceStatus vhss = new VsanHealthServiceStatus();
/*  72 */       JsonNode statusNode = root.get("status");
/*  73 */       JsonNode goalStateNode = root.get("goalState");
/*  74 */       JsonNode statusIssueNode = root.get("statusIssue");
/*     */       
/*  76 */       if (statusNode != null) {
/*  77 */         vhss.status = 
/*  78 */           VsanHealthServiceSubStatus.valueOf(statusNode.getTextValue());
/*     */       }
/*     */       
/*  81 */       if (goalStateNode != null) {
/*  82 */         vhss.goalState = 
/*  83 */           VsanHealthServiceGoalState.valueOf(goalStateNode
/*  84 */             .getTextValue());
/*     */       }
/*     */       
/*  87 */       if (statusIssueNode != null) {
/*  88 */         vhss.statusIssue = statusIssueNode.getTextValue();
/*     */       }
/*     */       
/*  91 */       return vhss;
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ManagedObjectReference buildTaskMor(String taskId, String vcGuid) {
/* 104 */     ManagedObjectReference task = new ManagedObjectReference(
/* 105 */         "Task", taskId, vcGuid);
/* 106 */     return task;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addToTestMoRefs(VsanClusterHealthGroup healthGroup, Set<ManagedObjectReference> allMoRefs, String serverGuid) {
/* 112 */     addToTestMoRefsFromBaseResults(healthGroup.groupDetails, allMoRefs, serverGuid);
/* 113 */     if (healthGroup.groupTests != null) {
/* 114 */       byte b; int i; VsanClusterHealthTest[] arrayOfVsanClusterHealthTest; for (i = (arrayOfVsanClusterHealthTest = healthGroup.groupTests).length, b = 0; b < i; ) { VsanClusterHealthTest test = arrayOfVsanClusterHealthTest[b];
/* 115 */         addToTestMoRefsFromBaseResults(test.testDetails, allMoRefs, serverGuid);
/*     */         b++; }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addToTestMoRefsFromBaseResults(VsanClusterHealthResultBase[] baseResults, Set<ManagedObjectReference> allMoRefs, String serverGuid) {
/* 122 */     if (baseResults == null)
/*     */       return;  byte b; int i;
/*     */     VsanClusterHealthResultBase[] arrayOfVsanClusterHealthResultBase;
/* 125 */     for (i = (arrayOfVsanClusterHealthResultBase = baseResults).length, b = 0; b < i; ) { VsanClusterHealthResultBase baseResult = arrayOfVsanClusterHealthResultBase[b];
/* 126 */       if (baseResult instanceof VsanClusterHealthResultTable) {
/*     */ 
/*     */         
/* 129 */         VsanClusterHealthResultTable table = (VsanClusterHealthResultTable)baseResult;
/* 130 */         addToTestMoRefsFromTable(table, allMoRefs, serverGuid);
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   }
/*     */   private static void addToTestMoRefsFromTable(VsanClusterHealthResultTable table, Set<ManagedObjectReference> allMoRefs, String serverGuid) {
/* 136 */     if (table.columns == null || table.rows == null) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     for (int i = 0; i < table.columns.length; i++) {
/* 141 */       ColumnType columnType = ColumnType.valueOf((table.columns[i]).type);
/* 142 */       if (columnType.equals(ColumnType.mor) || 
/* 143 */         columnType.equals(ColumnType.listMor) || 
/* 144 */         columnType.equals(ColumnType.dynamic)) {
/*     */         byte b;
/*     */         int j;
/*     */         VsanClusterHealthResultRow[] arrayOfVsanClusterHealthResultRow;
/* 148 */         for (j = (arrayOfVsanClusterHealthResultRow = table.rows).length, b = 0; b < j; ) { VsanClusterHealthResultRow row = arrayOfVsanClusterHealthResultRow[b];
/* 149 */           if (row.values[i] != null && !"".equals(row.values[i])) {
/*     */             ManagedObjectReference moRef; byte b1;
/*     */             int k;
/*     */             String[] arrayOfString;
/* 153 */             ColumnType cellType = columnType;
/* 154 */             String cellValue = row.values[i];
/*     */             
/* 156 */             if (columnType.equals(ColumnType.dynamic)) {
/*     */ 
/*     */               
/* 159 */               cellType = ColumnType.valueOf(row.values[i].split(":")[0]);
/* 160 */               cellValue = cellValue.substring(cellType.toString().length() + 1);
/*     */             } 
/*     */             
/* 163 */             switch (cellType) {
/*     */               case mor:
/* 165 */                 moRef = BaseUtils.generateMor(cellValue, serverGuid);
/* 166 */                 if (moRef != null) {
/* 167 */                   allMoRefs.add(moRef);
/*     */                 }
/*     */                 break;
/*     */               case listMor:
/* 171 */                 for (k = (arrayOfString = cellValue.split(",")).length, b1 = 0; b1 < k; ) { String mofStr = arrayOfString[b1];
/* 172 */                   moRef = BaseUtils.generateMor(mofStr, serverGuid);
/* 173 */                   if (moRef != null)
/* 174 */                     allMoRefs.add(moRef); 
/*     */                   b1++; }
/*     */                 
/*     */                 break;
/*     */             } 
/*     */           } 
/*     */           b++; }
/*     */       
/*     */       } 
/*     */     } 
/*     */   } public static Map<ManagedObjectReference, String> getNamesForMoRefs(Set<ManagedObjectReference> objects) {
/* 185 */     if (objects.size() == 0) {
/* 186 */       return new HashMap<>();
/*     */     }
/*     */     
/* 189 */     PropertyValue[] propValues = null;
/*     */     try {
/* 191 */       propValues = QueryUtil.getProperties(objects.toArray((Object[])new ManagedObjectReference[objects.size()]), 
/* 192 */           new String[] { "name" }).getPropertyValues();
/* 193 */     } catch (Exception exception) {
/* 194 */       _logger.error("Invalid query parameters are passed." + exception);
/*     */     } 
/*     */     
/* 197 */     Map<ManagedObjectReference, String> moRefToNameMap = new HashMap<>();
/* 198 */     if (propValues == null)
/* 199 */       return moRefToNameMap;  byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue1;
/* 201 */     for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 202 */       moRefToNameMap.put((ManagedObjectReference)propValue.resourceObject, (String)propValue.value); b++; }
/*     */     
/* 204 */     return moRefToNameMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<ManagedObjectReference> getClusterConnectedHosts(ManagedObjectReference clusterRef) throws Exception {
/* 209 */     List<ManagedObjectReference> hosts = new ArrayList<>();
/* 210 */     Constraint dsHostsConstraint = 
/* 211 */       QueryUtil.createConstraintForRelationship(clusterRef, "host", 
/* 212 */         HostSystem.class.getSimpleName());
/*     */     
/* 214 */     PropertyConstraint propertyConstraint = 
/* 215 */       QueryUtil.createPropertyConstraint(
/* 216 */         HostSystem.class.getSimpleName(), 
/* 217 */         "runtime.connectionState", Comparator.EQUALS, 
/* 218 */         HostSystem.ConnectionState.connected.name());
/*     */     
/* 220 */     Constraint dsConnectedHosts = 
/* 221 */       QueryUtil.combineIntoSingleConstraint(new Constraint[] {
/* 222 */           dsHostsConstraint, (Constraint)propertyConstraint }, Conjoiner.AND);
/*     */ 
/*     */     
/* 225 */     QuerySpec qSpec = QueryUtil.buildQuerySpec(dsConnectedHosts, new String[] { "config.product.version" });
/* 226 */     qSpec.name = clusterRef.getValue();
/*     */     
/* 228 */     ResultItem[] resultItems = (QueryUtil.getData(qSpec)).items;
/* 229 */     if (resultItems == null)
/* 230 */       return hosts;  byte b; int i;
/*     */     ResultItem[] arrayOfResultItem1;
/* 232 */     for (i = (arrayOfResultItem1 = resultItems).length, b = 0; b < i; ) { ResultItem resultItem = arrayOfResultItem1[b];
/* 233 */       hosts.add((ManagedObjectReference)resultItem.resourceObject); b++; }
/*     */     
/* 235 */     return hosts;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/util/VsanHealthUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */