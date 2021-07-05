/*    */ package com.vmware.vsan.client.services.diskmanagement;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*    */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.vsan.host.DiskMapInfoEx;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class DiskGroupData
/*    */ {
/*    */   public String name;
/*    */   public boolean isMounted;
/*    */   public DiskData[] disks;
/*    */   public ManagedObjectReference ownerHostRef;
/*    */   public boolean isAllFlash;
/*    */   public boolean isLocked;
/*    */   
/*    */   public static DiskGroupData fromMapping(ManagedObjectReference hostRef, DiskMapInfoEx mapInfo, Map<String, DiskData> claimedDisks) {
/* 29 */     DiskMapping mapping = mapInfo.getMapping();
/*    */     
/* 31 */     DiskGroupData result = new DiskGroupData();
/* 32 */     result.ownerHostRef = hostRef;
/* 33 */     result.name = mapping.ssd.uuid;
/* 34 */     result.isAllFlash = mapInfo.isAllFlash;
/* 35 */     result.isMounted = mapInfo.isMounted;
/* 36 */     result.isLocked = (mapInfo.encryptionInfo != null && 
/* 37 */       mapInfo.encryptionInfo.encryptionEnabled && 
/* 38 */       Boolean.FALSE.equals(mapInfo.unlockedEncrypted));
/*    */     
/* 40 */     List<DiskData> children = new ArrayList<>();
/* 41 */     String ssdUuid = mapping.ssd.uuid;
/* 42 */     if (claimedDisks.containsKey(ssdUuid)) {
/* 43 */       DiskData cacheDisk = claimedDisks.get(ssdUuid);
/* 44 */       cacheDisk.diskGroup = result.name;
/* 45 */       cacheDisk.isMappedAsCache = true;
/* 46 */       children.add(cacheDisk);
/*    */     }  byte b; int i;
/*    */     ScsiDisk[] arrayOfScsiDisk;
/* 49 */     for (i = (arrayOfScsiDisk = mapping.getNonSsd()).length, b = 0; b < i; ) { ScsiDisk nonSsd = arrayOfScsiDisk[b];
/* 50 */       if (claimedDisks.containsKey(nonSsd.uuid)) {
/* 51 */         DiskData capacityDisk = claimedDisks.get(nonSsd.uuid);
/* 52 */         capacityDisk.isMappedAsCache = false;
/* 53 */         capacityDisk.diskGroup = result.name;
/* 54 */         children.add(capacityDisk);
/*    */       }  b++; }
/*    */     
/* 57 */     result.disks = children.<DiskData>toArray(new DiskData[children.size()]);
/* 58 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskmanagement/DiskGroupData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */