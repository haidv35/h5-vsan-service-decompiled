/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
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
/*    */ @data
/*    */ public class ObjectWithName
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String objectName;
/*    */   public ManagedObjectReference object;
/*    */   
/*    */   public ObjectWithName() {}
/*    */   
/*    */   public ObjectWithName(String objectName, ManagedObjectReference object) {
/* 30 */     this.objectName = objectName;
/* 31 */     this.object = object;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/ObjectWithName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */