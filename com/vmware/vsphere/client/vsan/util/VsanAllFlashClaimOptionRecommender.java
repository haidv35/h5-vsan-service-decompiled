/*     */ package com.vmware.vsphere.client.vsan.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vsphere.client.vsan.data.ClaimOption;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanSemiAutoClaimDisksData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanAllFlashClaimOptionRecommender
/*     */   extends VsanBaseClaimOptionRecommender
/*     */ {
/*     */   private static final int CACHE_TO_CAPACITY_DIVIDER = 8;
/*     */   private static final int CACHE_TO_CAPACITY_SIZE_DIVIDER = 11;
/*     */   private boolean _isAllFlashAvailable = true;
/*  35 */   private Map<String, ClaimOption> _HCL = null;
/*     */ 
/*     */   
/*     */   public VsanAllFlashClaimOptionRecommender(VsanSemiAutoClaimDisksData data, Map<String, ClaimOption> HCL) {
/*  39 */     super(data);
/*  40 */     this._isAllFlashAvailable = data.isAllFlashAvailable;
/*  41 */     this._HCL = HCL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recommend() {
/*  70 */     if (ArrayUtils.isEmpty((Object[])(getData()).notInUseDisks)) {
/*     */       return;
/*     */     }
/*  73 */     if (this._isAllFlashAvailable && (
/*  74 */       this._HCL == null || this._HCL.size() <= 0))
/*     */     {
/*     */ 
/*     */       
/*  78 */       makeAllFlashConfigRecommendation(getData());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeAllFlashConfigRecommendation(VsanSemiAutoClaimDisksData data) {
/* 110 */     SortedMap<Long, List<VsanDiskData>> ssdsBySize = new TreeMap<>();
/*     */     
/* 112 */     List<VsanDiskData> storageSsdDisks = new ArrayList<>();
/*     */     
/* 114 */     long disksCapacity = 0L;
/* 115 */     int disksCount = 0; byte b; int i; VsanDiskData[] arrayOfVsanDiskData;
/* 116 */     for (i = (arrayOfVsanDiskData = data.notInUseDisks).length, b = 0; b < i; ) { VsanDiskData disk = arrayOfVsanDiskData[b];
/* 117 */       ScsiDisk scsiDisk = disk.disk;
/* 118 */       if (scsiDisk.ssd.booleanValue()) {
/* 119 */         disksCount++;
/* 120 */         Long capacity = Long.valueOf(calculateSize(scsiDisk.capacity));
/* 121 */         disksCapacity += capacity.longValue();
/* 122 */         if (disk.markedAsCapacityFlash) {
/* 123 */           storageSsdDisks.add(disk);
/*     */         } else {
/* 125 */           if (!ssdsBySize.containsKey(capacity)) {
/* 126 */             ssdsBySize.put(capacity, new ArrayList<>());
/*     */           }
/* 128 */           ((List<VsanDiskData>)ssdsBySize.get(capacity)).add(disk);
/*     */         } 
/*     */       } 
/*     */       
/*     */       b++; }
/*     */ 
/*     */     
/* 135 */     if (ssdsBySize.size() == 1) {
/* 136 */       makeConfigRecommendation(null, ssdsBySize.get(ssdsBySize.firstKey()), 
/* 137 */           data.numAllFlashGroups);
/* 138 */     } else if (ssdsBySize.size() > 1) {
/*     */ 
/*     */ 
/*     */       
/* 142 */       long minCacheCapacity = disksCapacity / 11L;
/*     */ 
/*     */       
/* 145 */       SortedDiskGroups<VsanDiskData> ssdGroupsBySize = new SortedDiskGroups<>(null);
/* 146 */       for (Map.Entry<Long, List<VsanDiskData>> entry : ssdsBySize.entrySet()) {
/* 147 */         ssdGroupsBySize.addDiskGroup(Long.valueOf(((Long)entry.getKey()).longValue() * ((List)entry.getValue()).size()), entry.getValue());
/*     */       }
/*     */       
/* 150 */       boolean moreCacheNeeded = true;
/* 151 */       int cacheDisksCount = 0;
/* 152 */       List<VsanDiskData> cacheSsdDisks = new LinkedList<>();
/* 153 */       long currentCache = 0L;
/*     */       
/* 155 */       while (ssdGroupsBySize.getDiskGroupsCount() != 0) {
/*     */ 
/*     */         
/* 158 */         Long size = ssdGroupsBySize.getSmallestDiskGroupCapacity();
/* 159 */         currentCache += size.longValue();
/* 160 */         if (currentCache >= minCacheCapacity)
/*     */         {
/* 162 */           moreCacheNeeded = false;
/*     */         }
/* 164 */         List<VsanDiskData> disks = ssdGroupsBySize.getSmallestGroupListWithLeastDisks();
/* 165 */         cacheSsdDisks.addAll(disks);
/* 166 */         cacheDisksCount += disks.size();
/* 167 */         if (!moreCacheNeeded) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 172 */       int maxCacheDisks = (disksCount - 1) / 8 + 1;
/* 173 */       while (maxCacheDisks > cacheDisksCount && 
/* 174 */         ssdGroupsBySize.getDiskGroupsCount() != 0) {
/*     */ 
/*     */ 
/*     */         
/* 178 */         List<VsanDiskData> disks = ssdGroupsBySize.getSmallestGroupListWithLeastDisks();
/* 179 */         cacheDisksCount += disks.size();
/* 180 */         cacheSsdDisks.addAll(disks);
/*     */       } 
/*     */       
/* 183 */       storageSsdDisks.addAll(ssdGroupsBySize.getAllDisks());
/*     */       
/* 185 */       if (storageSsdDisks.size() == 0 || 
/* 186 */         cacheSsdDisks.size() > storageSsdDisks.size()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 192 */       makeConfigRecommendation(cacheSsdDisks, storageSsdDisks, 
/* 193 */           data.numAllFlashGroups);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markDisksForClaimingOption(List<VsanDiskData> disks, ClaimOption option) {
/* 200 */     if (CollectionUtils.isEmpty(disks)) {
/*     */       return;
/*     */     }
/* 203 */     for (VsanDiskData disk : disks) {
/* 204 */       disk.recommendedAllFlashClaimOption = option;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SortedDiskGroups<T>
/*     */   {
/* 213 */     private SortedMap<Long, VsanAllFlashClaimOptionRecommender.DiskGroupsList<T>> _map = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addDiskGroup(Long groupSize, List<T> group) {
/* 219 */       if (!this._map.containsKey(groupSize)) {
/* 220 */         this._map.put(groupSize, new VsanAllFlashClaimOptionRecommender.DiskGroupsList<>(null));
/*     */       }
/*     */       
/* 223 */       VsanAllFlashClaimOptionRecommender.DiskGroupsList<T> list = this._map.get(groupSize);
/* 224 */       list.addDiskGroup(group);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<T> getSmallestGroupListWithLeastDisks() {
/* 232 */       return getGroupListWithLeastDisks(getSmallestDiskGroupCapacity());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<T> getGroupListWithLeastDisks(Long size) {
/* 239 */       List<T> result = null;
/* 240 */       VsanAllFlashClaimOptionRecommender.DiskGroupsList<T> currentItem = this._map.get(size);
/* 241 */       if (currentItem.getAllDiskGroups().size() > 1) {
/* 242 */         result = currentItem.getItemWithSmallestSize();
/*     */       }
/*     */       else {
/*     */         
/* 246 */         result = ((VsanAllFlashClaimOptionRecommender.DiskGroupsList<T>)this._map.remove(size)).getItemWithSmallestSize();
/*     */       } 
/* 248 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getDiskGroupsCount() {
/* 255 */       return this._map.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<T> getAllDisks() {
/* 262 */       List<T> result = new ArrayList<>();
/* 263 */       for (VsanAllFlashClaimOptionRecommender.DiskGroupsList<T> groups : this._map.values()) {
/* 264 */         for (List<T> diskGroup : groups.getAllDiskGroups()) {
/* 265 */           result.addAll(diskGroup);
/*     */         }
/*     */       } 
/* 268 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Long getSmallestDiskGroupCapacity() {
/* 275 */       return this._map.firstKey();
/*     */     }
/*     */     
/*     */     private SortedDiskGroups() {}
/*     */   }
/*     */   
/*     */   private static class DiskGroupsList<T>
/*     */   {
/* 283 */     private List<List<T>> _list = new ArrayList<>();
/*     */     
/*     */     public void addDiskGroup(List<T> group) {
/* 286 */       this._list.add(group);
/*     */     }
/*     */     
/*     */     public List<List<T>> getAllDiskGroups() {
/* 290 */       return this._list;
/*     */     }
/*     */     
/*     */     public List<T> getItemWithSmallestSize() {
/* 294 */       List<T> result = null;
/* 295 */       if (this._list.size() > 1) {
/* 296 */         int minimumDisks = ((List)this._list.get(0)).size();
/* 297 */         int indexMinimumSize = 0;
/* 298 */         for (int i = 0; i < this._list.size(); i++) {
/* 299 */           if (minimumDisks > ((List)this._list.get(i)).size()) {
/* 300 */             minimumDisks = ((List)this._list.get(i)).size();
/* 301 */             indexMinimumSize = i;
/*     */           } 
/*     */         } 
/* 304 */         result = this._list.remove(indexMinimumSize);
/* 305 */       } else if (this._list.size() == 1) {
/* 306 */         result = this._list.get(0);
/*     */       } 
/* 308 */       return result;
/*     */     }
/*     */     
/*     */     private DiskGroupsList() {}
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/VsanAllFlashClaimOptionRecommender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */