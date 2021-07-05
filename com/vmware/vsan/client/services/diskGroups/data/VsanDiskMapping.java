/*    */ package com.vmware.vsan.client.services.diskGroups.data;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*    */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanDiskMapping
/*    */ {
/*    */   public ScsiDisk ssd;
/*    */   public ScsiDisk[] nonSsd;
/*    */   
/*    */   public DiskMapping toVmodl() {
/* 18 */     if (this.ssd == null || ArrayUtils.isEmpty((Object[])this.nonSsd)) {
/* 19 */       return null;
/*    */     }
/* 21 */     DiskMapping result = new DiskMapping();
/* 22 */     result.ssd = this.ssd;
/* 23 */     result.nonSsd = this.nonSsd;
/* 24 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskGroups/data/VsanDiskMapping.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */