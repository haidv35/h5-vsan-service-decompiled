/*     */ package com.vmware.vsphere.client.vsan.perf.model;
/*     */ 
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfMetricSeriesCSV;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfThreshold;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @data
/*     */ public class AggregatedDiagnosticIssueEntity
/*     */   extends DiagnosticIssueEntity
/*     */ {
/*     */   private static final String AGGREGATED_REF_ID_SEPARATOR = "/";
/*     */   private static final long serialVersionUID = 1L;
/*     */   public String[] aggregatedRefIds;
/*     */   public List<SingleDiagnosticIssueEntity> entities;
/*     */   public SingleDiagnosticIssueEntity aggregatedEntity;
/*     */   public boolean hasSingleEntityInside;
/*     */   public boolean usingSingleMetricForAllEntities = false;
/*     */   public String metricIdLabel;
/*     */   public VsanPerfThreshold aggregationThreshold;
/*     */   
/*     */   public AggregatedDiagnosticIssueEntity() {}
/*     */   
/*     */   public AggregatedDiagnosticIssueEntity(String recommendation, VsanPerfEntityMetricCSV aggregationData, VsanPerfEntityMetricCSV[] metrics) {
/*  72 */     super(recommendation);
/*     */ 
/*     */     
/*  75 */     this.hasSingleEntityInside = isAggregatingMetricsOnSingleEntity(metrics);
/*     */     
/*  77 */     if (this.hasSingleEntityInside) {
/*  78 */       this.aggregatedRefIds = new String[] { aggregationData.entityRefId };
/*     */     } else {
/*  80 */       this.aggregatedRefIds = aggregationData.entityRefId.split("/");
/*  81 */       for (int i = 0; i < this.aggregatedRefIds.length; i++) {
/*  82 */         String[] parts = this.aggregatedRefIds[i].split(":");
/*  83 */         this.aggregatedRefIds[i] = parts[0];
/*     */       } 
/*     */       
/*  86 */       this.metricIdLabel = getCommonMetricLabelIfUsingCommonMetric(metrics);
/*  87 */       this.usingSingleMetricForAllEntities = StringUtils.isNotEmpty(this.metricIdLabel);
/*     */     } 
/*     */     
/*  90 */     this.aggregatedEntity = new SingleDiagnosticIssueEntity(PerfEntityStateData.parsePerfEntityMetricCSV(aggregationData));
/*  91 */     this.entities = createChildEntities(metrics);
/*     */ 
/*     */     
/*  94 */     this.aggregationThreshold = (aggregationData.value[0]).threshold;
/*     */   }
/*     */   
/*     */   private boolean isAggregatingMetricsOnSingleEntity(VsanPerfEntityMetricCSV[] metrics) {
/*  98 */     boolean isSingleEntity = true;
/*  99 */     String entityRefId = ""; byte b; int i; VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/* 100 */     for (i = (arrayOfVsanPerfEntityMetricCSV = metrics).length, b = 0; b < i; ) { VsanPerfEntityMetricCSV metric = arrayOfVsanPerfEntityMetricCSV[b];
/* 101 */       if (StringUtils.isNotEmpty(entityRefId) && !entityRefId.equals(metric.entityRefId)) {
/* 102 */         isSingleEntity = false;
/*     */         break;
/*     */       } 
/* 105 */       entityRefId = metric.entityRefId; b++; }
/*     */     
/* 107 */     return isSingleEntity;
/*     */   }
/*     */   
/*     */   private List<SingleDiagnosticIssueEntity> createChildEntities(VsanPerfEntityMetricCSV[] metrics) {
/* 111 */     List<SingleDiagnosticIssueEntity> _entities = new ArrayList<>(); byte b; int i; VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/* 112 */     for (i = (arrayOfVsanPerfEntityMetricCSV = metrics).length, b = 0; b < i; ) { VsanPerfEntityMetricCSV entityMetric = arrayOfVsanPerfEntityMetricCSV[b];
/* 113 */       SingleDiagnosticIssueEntity entity = 
/* 114 */         new SingleDiagnosticIssueEntity("", PerfEntityStateData.parsePerfEntityMetricCSV(entityMetric));
/* 115 */       _entities.add(entity); b++; }
/*     */     
/* 117 */     return _entities;
/*     */   }
/*     */   
/*     */   private String getCommonMetricLabelIfUsingCommonMetric(VsanPerfEntityMetricCSV[] metrics) {
/* 121 */     String metricIdLabel = null;
/* 122 */     boolean usingSingleMetricForAllEntities = true; byte b;
/*     */     int i;
/*     */     VsanPerfEntityMetricCSV[] arrayOfVsanPerfEntityMetricCSV;
/* 125 */     for (i = (arrayOfVsanPerfEntityMetricCSV = metrics).length, b = 0; b < i; ) { VsanPerfEntityMetricCSV entityMetric = arrayOfVsanPerfEntityMetricCSV[b]; byte b1; int j; VsanPerfMetricSeriesCSV[] arrayOfVsanPerfMetricSeriesCSV;
/* 126 */       for (j = (arrayOfVsanPerfMetricSeriesCSV = entityMetric.value).length, b1 = 0; b1 < j; ) { VsanPerfMetricSeriesCSV metricSeries = arrayOfVsanPerfMetricSeriesCSV[b1];
/* 127 */         if (!StringUtils.isEmpty(metricIdLabel) && 
/* 128 */           !metricIdLabel.equals(metricSeries.metricId.label)) {
/* 129 */           usingSingleMetricForAllEntities = false;
/*     */           break;
/*     */         } 
/* 132 */         metricIdLabel = metricSeries.metricId.label; b1++; }
/*     */       
/* 134 */       if (!usingSingleMetricForAllEntities) {
/* 135 */         metricIdLabel = null;
/*     */         break;
/*     */       } 
/*     */       b++; }
/*     */     
/* 140 */     return metricIdLabel;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/AggregatedDiagnosticIssueEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */