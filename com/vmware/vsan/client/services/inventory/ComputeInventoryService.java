/*     */ package com.vmware.vsan.client.services.inventory;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.ComputeResource;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.ResourcePool;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class ComputeInventoryService
/*     */   extends InventoryBrowserService
/*     */ {
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/*  40 */     return super.getNodeInfo(nodeRefs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/*  47 */     return super.getNodeChildren(parentNode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isLeafNode(ManagedObjectReference item) {
/*  52 */     return (this.vmodlHelper.getTypeClass(item) == HostSystem.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parentRef) {
/*  57 */     Exception exception1 = null, exception2 = null;
/*     */   }
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
/*     */   
/*     */   private List<ManagedObjectReference> filterChildren(ManagedObjectReference[] allChildren) {
/*  89 */     List<ManagedObjectReference> result = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/*  90 */     for (i = (arrayOfManagedObjectReference = allChildren).length, b = 0; b < i; ) { ManagedObjectReference childRef = arrayOfManagedObjectReference[b];
/*  91 */       if (this.vmodlHelper.isOfType(childRef, HostSystem.class) || 
/*  92 */         this.vmodlHelper.isOfType(childRef, ClusterComputeResource.class) || 
/*  93 */         this.vmodlHelper.isOfType(childRef, ResourcePool.class) || 
/*  94 */         this.vmodlHelper.isHostFolder(childRef)) {
/*  95 */         result.add(childRef);
/*  96 */       } else if (this.vmodlHelper.isOfType(childRef, ComputeResource.class)) {
/*  97 */         Exception exception2, exception1 = null;
/*     */       } 
/*     */ 
/*     */       
/*     */       b++; }
/*     */ 
/*     */ 
/*     */     
/* 105 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/ComputeInventoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */