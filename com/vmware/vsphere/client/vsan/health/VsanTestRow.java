/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ @data
/*    */ public class VsanTestRow
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public VsanTestCell[] rowValues;
/*    */   public List<VsanTestRow> nestedRows;
/*    */   
/*    */   public VsanTestRow() {}
/*    */   
/*    */   public VsanTestRow(VsanTestCell[] rowValues, List<VsanTestRow> nestedRows) {
/* 33 */     this.rowValues = rowValues;
/* 34 */     this.nestedRows = nestedRows;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanTestRow.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */