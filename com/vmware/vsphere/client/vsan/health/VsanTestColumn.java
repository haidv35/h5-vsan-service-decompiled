/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
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
/*    */ @data
/*    */ public class VsanTestColumn
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String columnLabel;
/*    */   public ColumnType columnType;
/*    */   
/*    */   public VsanTestColumn() {}
/*    */   
/*    */   public VsanTestColumn(String columnLabel, ColumnType columnType) {
/* 31 */     this.columnLabel = columnLabel;
/* 32 */     this.columnType = columnType;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanTestColumn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */