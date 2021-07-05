/*    */ package com.vmware.vsan.client.services.virtualobjects;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.data.Constraint;
/*    */ import com.vmware.vise.data.query.ObjectIdentityConstraint;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import com.vmware.vise.data.query.QuerySpec;
/*    */ import com.vmware.vise.data.query.RelationalConstraint;
/*    */ import com.vmware.vise.data.query.ResultItem;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ 
/*    */ @Component
/*    */ public class VsanVirtualObjectsProvider {
/* 18 */   private static final Log _logger = LogFactory.getLog(VsanVirtualObjectsProvider.class);
/*    */   
/*    */   private static final String COMPOSITE_UUID = "compositeUuid";
/*    */   
/*    */   public Set<String> getVirtualObjectsUuids(ManagedObjectReference clusterRef) throws Exception {
/* 23 */     startTimer("getVirtualObjectsUuids");
/* 24 */     String[] jsonProperties = { "vsanPhysicalDiskVirtualMapping" };
/*    */     
/* 26 */     QuerySpec querySpec = getClusterHostsQuerySpec(clusterRef, "host", jsonProperties);
/* 27 */     ResultItem[] resultItems = (QueryUtil.getData(querySpec)).items;
/* 28 */     stopTimer("getVirtualObjectsUuids");
/* 29 */     if (resultItems == null) {
/* 30 */       return Collections.emptySet();
/*    */     }
/*    */     
/* 33 */     Set<String> vsanUuids = new HashSet<>(); byte b; int i; ResultItem[] arrayOfResultItem1;
/* 34 */     for (i = (arrayOfResultItem1 = resultItems).length, b = 0; b < i; ) { ResultItem resultItem = arrayOfResultItem1[b];
/* 35 */       JsonNode hostJsonData = getHostJsonData(resultItem);
/* 36 */       if (hostJsonData != null) {
/* 37 */         List<String> hostVsanUuids = hostJsonData.findValuesAsText("compositeUuid");
/* 38 */         vsanUuids.addAll(hostVsanUuids);
/*    */       }  b++; }
/*    */     
/* 41 */     return vsanUuids;
/*    */   }
/*    */ 
/*    */   
/*    */   private QuerySpec getClusterHostsQuerySpec(ManagedObjectReference clusterRef, String relation, String[] properties) {
/* 46 */     ObjectIdentityConstraint clusterConstraint = QueryUtil.createObjectIdentityConstraint(clusterRef);
/* 47 */     RelationalConstraint clusterHostsConstraint = QueryUtil.createRelationalConstraint(
/* 48 */         relation, (Constraint)clusterConstraint, Boolean.valueOf(true), HostSystem.class.getSimpleName());
/* 49 */     QuerySpec querySpecHosts = QueryUtil.buildQuerySpec((Constraint)clusterHostsConstraint, properties);
/* 50 */     return querySpecHosts; } private JsonNode getHostJsonData(ResultItem resultItem) {
/*    */     byte b;
/*    */     int i;
/*    */     PropertyValue[] arrayOfPropertyValue;
/* 54 */     for (i = (arrayOfPropertyValue = resultItem.properties).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b];
/* 55 */       if ("vsanPhysicalDiskVirtualMapping".equals(propValue.propertyName))
/* 56 */         return Utils.getJsonRootNode((String)propValue.value); 
/*    */       b++; }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */   
/* 62 */   private final Map<String, Long> times = new HashMap<>();
/*    */   
/*    */   private void startTimer(String timerName) {
/* 65 */     long startTime = System.currentTimeMillis();
/* 66 */     if (!this.times.containsKey(timerName)) {
/* 67 */       this.times.remove(timerName);
/*    */     }
/* 69 */     this.times.put(timerName, Long.valueOf(startTime));
/*    */   }
/*    */   
/*    */   private void stopTimer(String timerName) {
/* 73 */     if (!this.times.containsKey(timerName)) {
/* 74 */       _logger.info("No start time for " + timerName);
/*    */       return;
/*    */     } 
/* 77 */     _logger.info(String.valueOf(timerName) + " total time: " + (System.currentTimeMillis() - ((Long)this.times.get(timerName)).longValue()));
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/VsanVirtualObjectsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */