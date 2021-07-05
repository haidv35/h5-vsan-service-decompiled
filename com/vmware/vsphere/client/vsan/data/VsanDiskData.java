/*    */ package com.vmware.vsphere.client.vsan.data;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vim.host.ScsiDisk;
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
/*    */ public class VsanDiskData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ScsiDisk disk;
/*    */   public boolean inUse;
/*    */   public boolean ineligible;
/*    */   public String stateReason;
/*    */   public String[] issues;
/*    */   public String vsanUuid;
/*    */   public String diskGroupUuid;
/*    */   public boolean isCacheDisk;
/*    */   public boolean markedAsCapacityFlash;
/*    */   public DiskLocalityType diskLocality;
/* 73 */   public ClaimOption recommendedAllFlashClaimOption = ClaimOption.DoNotClaim;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   public ClaimOption recommendedHybridClaimOption = ClaimOption.DoNotClaim;
/*    */   public ClaimOption[] possibleClaimOptions;
/*    */   public ClaimOption[] possibleClaimOptionsIfMarkedAsOppositeType;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/VsanDiskData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */