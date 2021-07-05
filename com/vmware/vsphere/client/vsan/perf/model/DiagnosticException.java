/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfDiagnosticResult;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
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
/*    */ public class DiagnosticException
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final String AGGREGATED_REF_ID_SEPARATOR = "/";
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String exceptionId;
/* 38 */   public List<DiagnosticIssueEntity> exceptionEntities = new ArrayList<>();
/*    */   public DiagnosticException() {}
/*    */   
/*    */   public DiagnosticException(String exceptionId) {
/* 42 */     this();
/* 43 */     this.exceptionId = exceptionId;
/*    */   }
/*    */   
/*    */   public void addEntities(VsanPerfDiagnosticResult diagnosticResult) {
/* 47 */     if (diagnosticResult.aggregationData != null) {
/* 48 */       AggregatedDiagnosticIssueEntity aggregatedEntity = 
/* 49 */         createAggregatedEntity(diagnosticResult);
/* 50 */       if (!aggregatedEntity.entities.isEmpty())
/* 51 */         this.exceptionEntities.add(aggregatedEntity); 
/*    */     } else {
/*    */       byte b; int i; VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/* 54 */       for (i = (arrayOfVsanPerfEntityMetricCSV = diagnosticResult.exceptionData).length, b = 0; b < i; ) { VsanPerfEntityMetricCSV entityMetric = arrayOfVsanPerfEntityMetricCSV[b];
/* 55 */         SingleDiagnosticIssueEntity issueEntity = 
/* 56 */           new SingleDiagnosticIssueEntity(diagnosticResult.recommendation, 
/* 57 */             PerfEntityStateData.parsePerfEntityMetricCSV(entityMetric));
/* 58 */         this.exceptionEntities.add(issueEntity);
/*    */         b++; }
/*    */     
/*    */     } 
/*    */   }
/*    */   
/*    */   private AggregatedDiagnosticIssueEntity createAggregatedEntity(VsanPerfDiagnosticResult diagnosticResult) {
/* 65 */     String[] aggregatedEntityRefIds = 
/* 66 */       diagnosticResult.aggregationData.entityRefId.split("/");
/* 67 */     for (int i = 0; i < aggregatedEntityRefIds.length; i++) {
/* 68 */       String[] parts = aggregatedEntityRefIds[i].split(":");
/* 69 */       aggregatedEntityRefIds[i] = parts[0];
/*    */     } 
/*    */     
/* 72 */     AggregatedDiagnosticIssueEntity issue = new AggregatedDiagnosticIssueEntity(
/* 73 */         diagnosticResult.recommendation, 
/* 74 */         diagnosticResult.aggregationData, 
/* 75 */         diagnosticResult.exceptionData);
/* 76 */     return issue;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/DiagnosticException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */