/*    */ package com.vmware.vsan.client.services.inventory;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.util.VmodlHelper;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class DatacenterInventoryService
/*    */   extends InventoryBrowserService
/*    */ {
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */   @Autowired
/*    */   private VmodlHelper vmodlHelper;
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/* 32 */     return super.getNodeInfo(nodeRefs);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/* 39 */     return super.getNodeChildren(parentNode);
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parent) {
/* 44 */     List<ManagedObjectReference> result = new ArrayList<>();
/* 45 */     Exception exception1 = null, exception2 = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isLeafNode(ManagedObjectReference item) {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/DatacenterInventoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */