/*    */ package com.vmware.vsphere.client.vsan.base.data;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanRaidConfig
/*    */   extends VsanComponent
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 15 */   public List<VsanComponent> children = new ArrayList<>();
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/data/VsanRaidConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */