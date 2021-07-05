/*    */ package com.vmware.vsan.client.services.inventory;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.VirtualMachine;
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
/*    */ public class VmInventoryService
/*    */   extends InventoryBrowserService
/*    */ {
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */   @Autowired
/*    */   private VmodlHelper vmodlHelper;
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/* 36 */     return super.getNodeInfo(nodeRefs);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/* 43 */     return super.getNodeChildren(parentNode);
/*    */   }
/*    */   
/*    */   private List<ManagedObjectReference> filterChildren(ManagedObjectReference[] allChildren) {
/* 47 */     List<ManagedObjectReference> result = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/* 48 */     for (i = (arrayOfManagedObjectReference = allChildren).length, b = 0; b < i; ) { ManagedObjectReference childRef = arrayOfManagedObjectReference[b];
/* 49 */       if (this.vmodlHelper.isVmFolder(childRef) || this.vmodlHelper.isOfType(childRef, VirtualMachine.class))
/* 50 */         result.add(childRef); 
/*    */       b++; }
/*    */     
/* 53 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parent) {
/* 58 */     Exception exception1 = null, exception2 = null;
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
/* 74 */     return this.vmodlHelper.isOfType(item, VirtualMachine.class);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/VmInventoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */