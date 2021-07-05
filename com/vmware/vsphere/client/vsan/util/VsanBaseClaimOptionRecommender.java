/*    */ package com.vmware.vsphere.client.vsan.util;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.host.DiskDimensions;
/*    */ import com.vmware.vsphere.client.vsan.data.ClaimOption;
/*    */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*    */ import com.vmware.vsphere.client.vsan.data.VsanSemiAutoClaimDisksData;
/*    */ import java.util.List;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class VsanBaseClaimOptionRecommender
/*    */ {
/*    */   private VsanSemiAutoClaimDisksData _data;
/*    */   
/*    */   public VsanBaseClaimOptionRecommender(VsanSemiAutoClaimDisksData data) {
/* 22 */     this._data = data;
/*    */   }
/*    */   
/*    */   protected VsanSemiAutoClaimDisksData getData() {
/* 26 */     return this._data;
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
/*    */   public abstract void recommend();
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
/*    */   protected void makeConfigRecommendation(List<VsanDiskData> cacheDisks, List<VsanDiskData> storageDisks, int numExistingGroups) {
/* 50 */     if (CollectionUtils.isEmpty(cacheDisks)) {
/*    */       
/* 52 */       if (numExistingGroups > 0) {
/* 53 */         markDisksForClaimingOption(storageDisks, 
/* 54 */             ClaimOption.ClaimForStorage);
/*    */       
/*    */       }
/*    */     }
/* 58 */     else if (!CollectionUtils.isEmpty(storageDisks)) {
/* 59 */       markDisksForClaimingOption(storageDisks, ClaimOption.ClaimForStorage);
/* 60 */       markDisksForClaimingOption(cacheDisks, ClaimOption.ClaimForCache);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void markDisksForClaimingOption(List<VsanDiskData> paramList, ClaimOption paramClaimOption);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static long calculateSize(DiskDimensions.Lba size) {
/* 81 */     return size.block * size.blockSize;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/VsanBaseClaimOptionRecommender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */