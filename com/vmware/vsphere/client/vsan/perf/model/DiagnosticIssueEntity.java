/*    */ package com.vmware.vsphere.client.vsan.perf.model;
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
/*    */ @data
/*    */ public class DiagnosticIssueEntity
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String recommendation;
/*    */   
/*    */   public DiagnosticIssueEntity() {}
/*    */   
/*    */   public DiagnosticIssueEntity(String recommendation) {
/* 24 */     this.recommendation = recommendation;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/DiagnosticIssueEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */