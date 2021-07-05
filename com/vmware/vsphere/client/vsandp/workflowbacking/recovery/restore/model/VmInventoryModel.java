/*    */ package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VmInventoryModel
/*    */ {
/* 24 */   public InventoryData folder = new InventoryData();
/* 25 */   public InventoryData compute = new InventoryData();
/* 26 */   public InventoryData network = new InventoryData();
/*    */   public boolean folderSameAsSource;
/*    */   public boolean computeSameAsSource;
/*    */   public boolean networkSameAsSource;
/*    */   public ManagedObjectReference rootDc;
/*    */   public ManagedObjectReference rootVsanCluster;
/*    */   
/*    */   @data
/*    */   public static class InventoryData {
/*    */     public String name;
/*    */     public String iconId;
/*    */     public ManagedObjectReference ref;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/model/VmInventoryModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */