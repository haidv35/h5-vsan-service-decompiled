/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.Map;
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
/*    */ public class PerformanceEntitiesData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public Map<String, EntityRefData> entityRefIdToEntityRefData;
/*    */   
/*    */   public PerformanceEntitiesData() {}
/*    */   
/*    */   public PerformanceEntitiesData(Map<String, EntityRefData> entityRefIdToEntityRefData) {
/* 30 */     this.entityRefIdToEntityRefData = entityRefIdToEntityRefData;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerformanceEntitiesData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */