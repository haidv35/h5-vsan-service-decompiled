/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class SingleDiagnosticIssueEntity
/*    */   extends DiagnosticIssueEntity
/*    */ {
/* 13 */   private static final Log _logger = LogFactory.getLog(SingleDiagnosticIssueEntity.class);
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */ 
/*    */   
/*    */   public String entityRefId;
/*    */ 
/*    */ 
/*    */   
/*    */   public String vsanUuid;
/*    */ 
/*    */ 
/*    */   
/*    */   public PerfEntityStateData metric;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SingleDiagnosticIssueEntity() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public SingleDiagnosticIssueEntity(PerfEntityStateData metric) {
/* 39 */     this("", metric);
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleDiagnosticIssueEntity(String recommendation, PerfEntityStateData metric) {
/* 44 */     super(recommendation);
/* 45 */     _logger.info("Creating perf diag issue for entityRefId = " + metric.entityRefId);
/*    */     
/* 47 */     this.metric = metric;
/* 48 */     this.entityRefId = metric.entityRefId;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/SingleDiagnosticIssueEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */