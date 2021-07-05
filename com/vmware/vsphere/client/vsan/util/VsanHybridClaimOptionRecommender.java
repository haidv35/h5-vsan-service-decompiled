/*    */ package com.vmware.vsphere.client.vsan.util;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsan.data.ClaimOption;
/*    */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*    */ import com.vmware.vsphere.client.vsan.data.VsanSemiAutoClaimDisksData;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanHybridClaimOptionRecommender
/*    */   extends VsanBaseClaimOptionRecommender
/*    */ {
/* 23 */   private static final Comparator<VsanDiskData> _comparator = new Comparator<VsanDiskData>()
/*    */     {
/*    */       public int compare(VsanDiskData o1, VsanDiskData o2) {
/* 26 */         long diff = VsanHybridClaimOptionRecommender.calculateSize(o1.disk.capacity) - VsanHybridClaimOptionRecommender.calculateSize(o2.disk.capacity);
/* 27 */         if (diff > 0L)
/* 28 */           return 1; 
/* 29 */         if (diff < 0L) {
/* 30 */           return -1;
/*    */         }
/* 32 */         return 0;
/*    */       }
/*    */     };
/*    */   
/*    */   public VsanHybridClaimOptionRecommender(VsanSemiAutoClaimDisksData data) {
/* 37 */     super(data);
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
/*    */   
/*    */   public void recommend() {
/* 51 */     if (ArrayUtils.isEmpty((Object[])(getData()).notInUseDisks)) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     makeHybridConfigRecommendation(getData());
/*    */   }
/*    */   
/*    */   private void makeHybridConfigRecommendation(VsanSemiAutoClaimDisksData data) {
/* 59 */     List<VsanDiskData> ssds = new ArrayList<>();
/* 60 */     List<VsanDiskData> hdds = new ArrayList<>(); byte b; int i;
/*    */     VsanDiskData[] arrayOfVsanDiskData;
/* 62 */     for (i = (arrayOfVsanDiskData = data.notInUseDisks).length, b = 0; b < i; ) { VsanDiskData disk = arrayOfVsanDiskData[b];
/* 63 */       if (disk.disk.ssd.booleanValue()) {
/* 64 */         ssds.add(disk);
/*    */       } else {
/* 66 */         hdds.add(disk);
/*    */       } 
/*    */       b++; }
/*    */     
/* 70 */     Collections.sort(ssds, _comparator);
/*    */ 
/*    */     
/* 73 */     if (ssds.size() > hdds.size()) {
/* 74 */       ssds = ssds.subList(0, hdds.size());
/*    */     }
/*    */     
/* 77 */     makeConfigRecommendation(ssds, hdds, data.numHybridGroups);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markDisksForClaimingOption(List<VsanDiskData> disks, ClaimOption option) {
/* 83 */     if (CollectionUtils.isEmpty(disks)) {
/*    */       return;
/*    */     }
/* 86 */     for (VsanDiskData disk : disks)
/* 87 */       disk.recommendedHybridClaimOption = option; 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/VsanHybridClaimOptionRecommender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */