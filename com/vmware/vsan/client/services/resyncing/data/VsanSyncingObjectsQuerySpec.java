/*    */ package com.vmware.vsan.client.services.resyncing.data;
/*    */ 
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
/*    */ @data
/*    */ public class VsanSyncingObjectsQuerySpec
/*    */ {
/* 30 */   public int start = 0;
/* 31 */   public int limit = Integer.MAX_VALUE;
/*    */   public boolean includeSummary = true;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/data/VsanSyncingObjectsQuerySpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */