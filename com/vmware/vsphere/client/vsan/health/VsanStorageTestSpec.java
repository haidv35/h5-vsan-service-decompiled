/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanStorageWorkloadType;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanStorageTestSpec
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String typeId;
/*    */   public String name;
/*    */   public Integer duration;
/*    */   public String description;
/*    */   public String profileId;
/*    */   
/*    */   public static VsanStorageTestSpec fromVmodl(VsanStorageWorkloadType model) {
/* 20 */     VsanStorageTestSpec type = new VsanStorageTestSpec();
/* 21 */     type.typeId = model.typeId;
/* 22 */     type.name = model.name;
/* 23 */     type.description = model.description;
/*    */     
/* 25 */     return type;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanStorageTestSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */