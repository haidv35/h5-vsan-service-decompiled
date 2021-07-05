/*    */ package com.vmware.vsphere.client.vsan.data;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
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
/*    */ @data
/*    */ public class VsanSemiAutoClaimDisksData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public VsanDiskData[] notInUseDisks;
/* 22 */   public int numNotInUseSsdDisks = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public int numNotInUseDataDisks = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int numAllFlashGroups = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int numHybridGroups = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int numAllFlashCapacityDisks = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public int numHybridCapacityDisks = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hybridDiskGroupExist = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean allFlashDiskGroupExist = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAllFlashAvailable = true;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public long claimedCapacity = 0L;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 75 */   public long claimedCache = 0L;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/VsanSemiAutoClaimDisksData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */