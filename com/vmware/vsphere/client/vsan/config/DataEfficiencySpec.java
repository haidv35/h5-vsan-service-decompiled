/*    */ package com.vmware.vsphere.client.vsan.config;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.DataEfficiencyConfig;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class DataEfficiencySpec
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public boolean deduplicationState;
/*    */   public boolean compressionState;
/*    */   
/*    */   public DataEfficiencyConfig toVmodlSpec() {
/* 34 */     DataEfficiencyConfig vmodlSpec = new DataEfficiencyConfig();
/* 35 */     vmodlSpec.setDedupEnabled(this.deduplicationState);
/* 36 */     vmodlSpec.setCompressionEnabled(Boolean.valueOf(this.compressionState));
/* 37 */     return vmodlSpec;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/config/DataEfficiencySpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */