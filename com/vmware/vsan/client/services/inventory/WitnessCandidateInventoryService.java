/*    */ package com.vmware.vsan.client.services.inventory;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import com.vmware.vise.data.query.ResultItem;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class WitnessCandidateInventoryService
/*    */   extends InventoryBrowserService
/*    */ {
/*    */   @Autowired
/*    */   private ComputeInventoryService computeInventoryService;
/*    */   
/*    */   protected Set<String> getDataServiceProperties() {
/* 27 */     Set<String> result = super.getDataServiceProperties();
/* 28 */     result.add("config.vsanHostConfig.enabled");
/* 29 */     result.add("isWitnessHost");
/* 30 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parent) {
/* 35 */     return this.computeInventoryService.listChildrenRefs(parent);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isLeafNode(ManagedObjectReference item) {
/* 40 */     return this.computeInventoryService.isLeafNode(item);
/*    */   } protected InventoryEntryData createModel(ResultItem item) {
/*    */     byte b;
/*    */     int i;
/*    */     PropertyValue[] arrayOfPropertyValue;
/* 45 */     for (i = (arrayOfPropertyValue = item.properties).length, b = 0; b < i; ) { PropertyValue prop = arrayOfPropertyValue[b];
/* 46 */       if ((prop.propertyName.equals("config.vsanHostConfig.enabled") || 
/* 47 */         prop.propertyName.equals("isWitnessHost")) && (
/* 48 */         (Boolean)prop.value).booleanValue()) {
/* 49 */         return null;
/*    */       }
/*    */       b++; }
/*    */     
/* 53 */     return this.computeInventoryService.createModel(item);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/* 60 */     return super.getNodeInfo(nodeRefs);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/* 67 */     return super.getNodeChildren(parentNode);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/WitnessCandidateInventoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */