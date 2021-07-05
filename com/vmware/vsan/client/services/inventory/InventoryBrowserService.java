/*     */ package com.vmware.vsan.client.services.inventory;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.cluster.DrsConfigInfo;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ public abstract class InventoryBrowserService
/*     */ {
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/*  23 */     Collection<String> dsProps = getDataServiceProperties();
/*  24 */     dsProps.add("configuration.drsConfig");
/*  25 */     QuerySpec querySpec = QueryUtil.buildQuerySpec((Object[])nodeRefs, dsProps.<String>toArray(new String[0]));
/*     */     
/*  27 */     ResultSet response = QueryUtil.getData(querySpec);
/*  28 */     List<InventoryEntryData> result = extractNodeItems(response);
/*  29 */     return result.<InventoryEntryData>toArray(new InventoryEntryData[result.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/*  38 */     List<ManagedObjectReference> childrenRefs = listChildrenRefs(parentNode);
/*  39 */     if (childrenRefs.isEmpty()) {
/*  40 */       return new InventoryEntryData[0];
/*     */     }
/*     */     
/*  43 */     Collection<String> dsProps = getDataServiceProperties();
/*  44 */     dsProps.add("runtime.connectionState");
/*  45 */     dsProps.add("runtime.inMaintenanceMode");
/*  46 */     dsProps.add("runtime.inQuarantineMode");
/*  47 */     QuerySpec query = QueryUtil.buildQuerySpec(childrenRefs.toArray(), dsProps.<String>toArray(new String[0]));
/*     */     
/*  49 */     ResultSet response = QueryUtil.getData(query);
/*  50 */     List<InventoryEntryData> result = extractNodeItems(response);
/*     */     
/*  52 */     return result.<InventoryEntryData>toArray(new InventoryEntryData[result.size()]);
/*     */   }
/*     */   
/*     */   protected Set<String> getDataServiceProperties() {
/*  56 */     Set<String> result = new HashSet<>();
/*  57 */     result.add("name");
/*  58 */     result.add("primaryIconId");
/*  59 */     return result;
/*     */   }
/*     */   
/*     */   protected abstract List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference paramManagedObjectReference);
/*     */   
/*     */   protected abstract boolean isLeafNode(ManagedObjectReference paramManagedObjectReference);
/*     */   
/*     */   protected List<InventoryEntryData> extractNodeItems(ResultSet nodeData) {
/*  67 */     List<InventoryEntryData> result = new ArrayList<>(); byte b; int i; ResultItem[] arrayOfResultItem;
/*  68 */     for (i = (arrayOfResultItem = nodeData.items).length, b = 0; b < i; ) { ResultItem item = arrayOfResultItem[b];
/*  69 */       InventoryEntryData model = createModel(item);
/*  70 */       if (model != null)
/*  71 */         result.add(model); 
/*     */       b++; }
/*     */     
/*  74 */     return result;
/*     */   }
/*     */   
/*     */   protected InventoryEntryData createModel(ResultItem item) {
/*  78 */     InventoryEntryData model = new InventoryEntryData();
/*  79 */     model.nodeRef = (ManagedObjectReference)item.resourceObject;
/*  80 */     model.connected = true; byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue;
/*  82 */     for (i = (arrayOfPropertyValue = item.properties).length, b = 0; b < i; ) { PropertyValue prop = arrayOfPropertyValue[b];
/*  83 */       if (prop.propertyName.equals("name")) {
/*  84 */         model.name = (String)prop.value;
/*  85 */       } else if (prop.propertyName.equals("primaryIconId")) {
/*  86 */         model.iconShape = (String)prop.value;
/*  87 */       } else if (prop.propertyName.equals("runtime.connectionState")) {
/*  88 */         boolean hostConnected = prop.value.toString().equals(HostSystem.ConnectionState.connected.toString());
/*  89 */         if (!hostConnected) {
/*  90 */           model.connected = false;
/*     */         }
/*  92 */       } else if (prop.propertyName.equals("configuration.drsConfig")) {
/*  93 */         DrsConfigInfo drsConfig = (DrsConfigInfo)prop.value;
/*  94 */         model.isDrsEnabled = (drsConfig != null) ? drsConfig.enabled.booleanValue() : false;
/*  95 */       } else if (prop.propertyName.equals("runtime.inMaintenanceMode")) {
/*  96 */         boolean isInMaintenanceMode = ((Boolean)prop.value).booleanValue();
/*  97 */         if (isInMaintenanceMode) {
/*  98 */           model.connected = false;
/*     */         }
/* 100 */       } else if (prop.propertyName.equals("runtime.inQuarantineMode")) {
/* 101 */         boolean isInQuarantine = ((Boolean)prop.value).booleanValue();
/* 102 */         if (isInQuarantine)
/* 103 */           model.connected = false; 
/*     */       } 
/*     */       b++; }
/*     */     
/* 107 */     model.isLeafNode = isLeafNode(model.nodeRef);
/* 108 */     return model;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/InventoryBrowserService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */