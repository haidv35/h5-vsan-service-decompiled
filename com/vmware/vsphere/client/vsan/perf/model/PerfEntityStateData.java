/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfMetricSeriesCSV;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ import org.apache.commons.lang.StringUtils;
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
/*    */ public class PerfEntityStateData
/*    */ {
/*    */   private static final String NONE = "None";
/*    */   public List<String> timeStamps;
/*    */   public List<PerfGraphMetricsData> metricsSeries;
/*    */   public String entityRefId;
/*    */   
/*    */   public static PerfEntityStateData parsePerfEntityMetricCSV(VsanPerfEntityMetricCSV metric) {
/* 34 */     PerfEntityStateData stateData = new PerfEntityStateData();
/* 35 */     stateData.entityRefId = metric.entityRefId;
/* 36 */     if (StringUtils.isNotBlank(metric.sampleInfo)) {
/* 37 */       stateData.timeStamps = Arrays.asList(StringUtils.split(metric.sampleInfo, ","));
/*    */     } else {
/* 39 */       stateData.timeStamps = new ArrayList<>();
/*    */     } 
/*    */     
/* 42 */     stateData.metricsSeries = new ArrayList<>();
/* 43 */     if (ArrayUtils.isEmpty((Object[])metric.value))
/* 44 */       return stateData;  byte b; int i;
/*    */     VsanPerfMetricSeriesCSV[] arrayOfVsanPerfMetricSeriesCSV;
/* 46 */     for (i = (arrayOfVsanPerfMetricSeriesCSV = metric.value).length, b = 0; b < i; ) { VsanPerfMetricSeriesCSV metricSeries = arrayOfVsanPerfMetricSeriesCSV[b];
/* 47 */       PerfGraphMetricsData metricsData = new PerfGraphMetricsData();
/* 48 */       metricsData.key = metricSeries.metricId.label;
/* 49 */       if (metricSeries.threshold != null) {
/* 50 */         metricsData.threshold = new PerfGraphThreshold();
/* 51 */         metricsData.threshold.direction = 
/* 52 */           PerfGraphThresholdDirection.fromVmodl(metricSeries.threshold.direction);
/* 53 */         metricsData.threshold.yellow = StringUtils.isBlank(metricSeries.threshold.yellow) ? 
/* 54 */           null : Long.valueOf(Long.parseLong(metricSeries.threshold.yellow));
/* 55 */         metricsData.threshold.red = StringUtils.isBlank(metricSeries.threshold.red) ? 
/* 56 */           null : Long.valueOf(Long.parseLong(metricSeries.threshold.red));
/*    */       } 
/* 58 */       if (StringUtils.isNotBlank(metricSeries.values)) {
/* 59 */         List<String> values = Arrays.asList(StringUtils.split(metricSeries.values, ","));
/* 60 */         metricsData.values = new ArrayList<>();
/* 61 */         for (String value : values) {
/*    */           
/* 63 */           if ("None".equalsIgnoreCase(value) || StringUtils.isBlank(value)) {
/* 64 */             metricsData.values.add(null); continue;
/*    */           } 
/* 66 */           metricsData.values.add(Double.valueOf(Double.parseDouble(value)));
/*    */         } 
/*    */       } 
/*    */       
/* 70 */       stateData.metricsSeries.add(metricsData); b++; }
/*    */     
/* 72 */     return stateData;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfEntityStateData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */