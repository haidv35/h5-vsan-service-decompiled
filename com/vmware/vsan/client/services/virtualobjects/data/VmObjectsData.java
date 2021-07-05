/*    */ package com.vmware.vsan.client.services.virtualobjects.data;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsan.client.services.common.data.VmData;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VmObjectsData
/*    */ {
/*    */   public ManagedObjectReference vmRef;
/*    */   public String name;
/*    */   public String primaryIconId;
/*    */   public List<VsanObject> vmObjects;
/*    */   
/*    */   public VmObjectsData() {}
/*    */   
/*    */   public VmObjectsData(VmData vmData) {
/* 38 */     this.vmRef = vmData.vmRef;
/* 39 */     this.name = vmData.name;
/* 40 */     this.primaryIconId = vmData.primaryIconId;
/* 41 */     this.vmObjects = new ArrayList<>();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VmObjectsData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */