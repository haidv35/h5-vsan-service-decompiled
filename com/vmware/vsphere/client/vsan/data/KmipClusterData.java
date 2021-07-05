/*    */ package com.vmware.vsphere.client.vsan.data;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
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
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class KmipClusterData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 21 */   public List<String> availableKmipClusters = new ArrayList<>();
/*    */   public String defaultKmipCluster;
/*    */   public boolean hasManageKeyServersPermissions;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/KmipClusterData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */