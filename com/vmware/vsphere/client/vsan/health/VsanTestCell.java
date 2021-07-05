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
/*    */ 
/*    */ @data
/*    */ public class VsanTestCell
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ColumnType cellType;
/*    */   public Object cellValue;
/*    */   
/*    */   public VsanTestCell() {}
/*    */   
/*    */   public VsanTestCell(ColumnType cellType, Object cellValue) {
/* 32 */     this.cellType = cellType;
/* 33 */     this.cellValue = cellValue;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanTestCell.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */