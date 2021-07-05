/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ @data
/*    */ public enum VsanObjectType
/*    */ {
/*    */   private static final Log _logger;
/* 12 */   vmswap,
/* 13 */   vdisk,
/* 14 */   namespace,
/* 15 */   vmem,
/* 16 */   statsdb,
/* 17 */   other,
/* 18 */   vdisk_snapshot,
/* 19 */   iscsiTarget,
/* 20 */   iscsiLun,
/* 21 */   iscsiHome,
/* 22 */   improvedVirtualDisk;
/*    */   static {
/* 24 */     _logger = LogFactory.getLog(VsanObjectType.class);
/*    */   }
/*    */   public static VsanObjectType parse(String value) {
/*    */     try {
/* 28 */       return valueOf(value);
/* 29 */     } catch (Exception ex) {
/* 30 */       _logger.warn("Unknown vSAN Object Type: " + value, ex);
/* 31 */       return other;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanObjectType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */