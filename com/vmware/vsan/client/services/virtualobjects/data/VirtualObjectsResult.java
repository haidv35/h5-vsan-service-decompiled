/*    */ package com.vmware.vsan.client.services.virtualobjects.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectDataProtectionHealthState;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectHealthState;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ @data
/*    */ public class VirtualObjectsResult {
/* 13 */   public Map<VsanObjectHealthState, Integer> countByObjectHealth = new HashMap<>();
/* 14 */   public Map<VsanObjectDataProtectionHealthState, Integer> countByDataProtectionHealth = new HashMap<>();
/*    */   
/* 16 */   public List<VirtualObjectModel> items = new ArrayList<>();
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/virtualobjects/data/VirtualObjectsResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */