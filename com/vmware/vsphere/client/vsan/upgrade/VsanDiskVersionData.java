/*    */ package com.vmware.vsphere.client.vsan.upgrade;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vim.vsan.host.VsanDiskInfo;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanDiskVersionData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   public double version = 1.0D;
/*    */ 
/*    */   
/*    */   public String vsanUuid;
/*    */ 
/*    */ 
/*    */   
/*    */   public VsanDiskVersionData() {}
/*    */ 
/*    */   
/*    */   public VsanDiskVersionData(VsanDiskInfo vsanDiskInfo) {
/* 30 */     if (vsanDiskInfo != null) {
/* 31 */       this.version = vsanDiskInfo.formatVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 36 */       if (this.version == 3.0D) {
/* 37 */         this.version = 2.5D;
/* 38 */       } else if (this.version == 4.0D) {
/* 39 */         this.version = 3.0D;
/* 40 */       } else if (this.version == 0.0D) {
/* 41 */         this.version = 1.0D;
/*    */       } 
/* 43 */       this.vsanUuid = vsanDiskInfo.vsanUuid;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanDiskVersionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */