/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.binding.vmodl.fault.InvalidType;
/*    */ import com.vmware.vim.binding.vmodl.wsdlName;
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
/*    */ public class MoRef
/*    */   extends ManagedObjectReference
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MoRef(String type, String moid) {
/* 25 */     setType(type);
/* 26 */     setValue(moid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MoRef(Class<?> clasz, String moid) {
/* 34 */     setType(getVmodlTypeName(clasz));
/* 35 */     setValue(moid);
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
/*    */   public static String getVmodlTypeName(Class<?> clasz) {
/* 48 */     wsdlName vmodlTypeName = clasz.<wsdlName>getAnnotation(wsdlName.class);
/* 49 */     if (vmodlTypeName == null) {
/* 50 */       InvalidType fault = new InvalidType();
/* 51 */       fault.setArgument(clasz.getName());
/* 52 */       throw fault;
/*    */     } 
/* 54 */     return vmodlTypeName.value();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/util/MoRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */