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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class VmFolderInventorySerivce
/*    */   extends InventoryBrowserService
/*    */ {
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */   @Autowired
/*    */   private VmodlHelper vmodlHelper;
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/* 35 */     return super.getNodeInfo(nodeRefs);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/* 42 */     return super.getNodeChildren(parentNode);
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parent) {
/* 47 */     Exception exception1 = null, exception2 = null;
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
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   private List<ManagedObjectReference> filterChildren(ManagedObjectReference[] allChildren) {
/* 67 */     List<ManagedObjectReference> result = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/* 68 */     for (i = (arrayOfManagedObjectReference = allChildren).length, b = 0; b < i; ) { ManagedObjectReference childRef = arrayOfManagedObjectReference[b];
/* 69 */       if (this.vmodlHelper.isVmFolder(childRef))
/* 70 */         result.add(childRef); 
/*    */       b++; }
/*    */     
/* 73 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/VmFolderInventorySerivce.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */