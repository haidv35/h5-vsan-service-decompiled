/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*    */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class DiskGroup
/*    */ {
/*    */   public String diskGroupUuid;
/*    */   public String diskGroupName;
/*    */   public DiskInfo cacheDisk;
/*    */   public List<DiskInfo> capacityDisks;
/*    */   
/*    */   public static DiskGroup fromDiskMapping(DiskMapping diskMapping) {
/* 24 */     DiskGroup group = new DiskGroup();
/* 25 */     group.diskGroupName = diskMapping.ssd.uuid;
/* 26 */     group.diskGroupUuid = diskMapping.ssd.vsanDiskInfo.vsanUuid;
/* 27 */     group.cacheDisk = new DiskInfo();
/* 28 */     group.cacheDisk.diskUuid = diskMapping.ssd.vsanDiskInfo.vsanUuid;
/* 29 */     group.cacheDisk.diskName = diskMapping.ssd.displayName;
/* 30 */     List<DiskInfo> capacityDisks = new ArrayList<>(); byte b; int i; ScsiDisk[] arrayOfScsiDisk;
/* 31 */     for (i = (arrayOfScsiDisk = diskMapping.nonSsd).length, b = 0; b < i; ) { ScsiDisk disk = arrayOfScsiDisk[b];
/* 32 */       DiskInfo capacityDisk = new DiskInfo();
/* 33 */       capacityDisk.diskName = disk.displayName;
/* 34 */       capacityDisk.diskUuid = disk.vsanDiskInfo.vsanUuid;
/* 35 */       capacityDisks.add(capacityDisk); b++; }
/*    */     
/* 37 */     group.capacityDisks = capacityDisks;
/* 38 */     return group;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/DiskGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */