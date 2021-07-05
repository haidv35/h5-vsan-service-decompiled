/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ public class PerformanceDiagnosticData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public List<DiagnosticException> issues;
/*    */   public List<String> entityRefIds;
/*    */   
/*    */   public PerformanceDiagnosticData() {}
/*    */   
/*    */   public PerformanceDiagnosticData(List<DiagnosticException> issues, List<String> entityRefIds) {
/* 37 */     this.issues = issues;
/* 38 */     this.entityRefIds = entityRefIds;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerformanceDiagnosticData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */