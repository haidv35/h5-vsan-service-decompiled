/*     */ package com.vmware.vsphere.client.vsan.upgrade;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.VsanUpgradeSystem;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanUpgradeStatusEx;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */ public class VsanUpgradeStatusData
/*     */ {
/*     */   public boolean isAsyncPrecheckSupported;
/*  32 */   public Boolean isPrecheck = Boolean.valueOf(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public Boolean aborted = Boolean.valueOf(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public Boolean completed = Boolean.valueOf(false);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Boolean inProgress = Boolean.valueOf(false);
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> issues;
/*     */ 
/*     */ 
/*     */   
/*     */   public int progress;
/*     */ 
/*     */ 
/*     */   
/*     */   public String currentOperationName;
/*     */ 
/*     */ 
/*     */   
/*     */   public Date lastOperationDate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanUpgradeStatusData() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanUpgradeStatusData(Boolean isAsyncPrecheckSupported) {
/*  74 */     this.isAsyncPrecheckSupported = isAsyncPrecheckSupported.booleanValue();
/*     */   }
/*     */   
/*     */   public VsanUpgradeStatusData(VsanUpgradeStatusEx statusEx) {
/*  78 */     this.isAsyncPrecheckSupported = true;
/*  79 */     this.isPrecheck = statusEx.isPrecheck;
/*  80 */     this.aborted = statusEx.aborted;
/*  81 */     this.completed = statusEx.completed;
/*  82 */     this.inProgress = Boolean.valueOf(statusEx.inProgress);
/*  83 */     this.progress = statusEx.progress.intValue();
/*  84 */     if (statusEx.precheckResult != null && 
/*  85 */       statusEx.precheckResult.issues != null) {
/*  86 */       this.issues = new ArrayList<>(); byte b; int i; VsanUpgradeSystem.PreflightCheckIssue[] arrayOfPreflightCheckIssue;
/*  87 */       for (i = (arrayOfPreflightCheckIssue = statusEx.precheckResult.issues).length, b = 0; b < i; ) { VsanUpgradeSystem.PreflightCheckIssue issue = arrayOfPreflightCheckIssue[b];
/*  88 */         this.issues.add(issue.msg); b++; }
/*     */     
/*     */     } 
/*  91 */     populateLastOperationNameAndTime(statusEx.history);
/*     */   }
/*     */   
/*     */   public VsanUpgradeStatusData(VsanUpgradeSystem.UpgradeStatus upgradeStatus) {
/*  95 */     this.isAsyncPrecheckSupported = false;
/*  96 */     this.isPrecheck = Boolean.valueOf(false);
/*  97 */     this.aborted = upgradeStatus.aborted;
/*  98 */     this.completed = upgradeStatus.completed;
/*  99 */     this.inProgress = Boolean.valueOf(upgradeStatus.inProgress);
/* 100 */     this.progress = upgradeStatus.progress.intValue();
/* 101 */     populateLastOperationNameAndTime(upgradeStatus.history);
/*     */   }
/*     */   
/*     */   private void populateLastOperationNameAndTime(VsanUpgradeSystem.UpgradeHistoryItem[] history) {
/* 105 */     if (history == null || history.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     Calendar lastTimestamp = null;
/* 110 */     String lastOperation = ""; byte b; int i; VsanUpgradeSystem.UpgradeHistoryItem[] arrayOfUpgradeHistoryItem;
/* 111 */     for (i = (arrayOfUpgradeHistoryItem = history).length, b = 0; b < i; ) { VsanUpgradeSystem.UpgradeHistoryItem historyItem = arrayOfUpgradeHistoryItem[b];
/* 112 */       if (lastTimestamp == null || 
/* 113 */         lastTimestamp.compareTo(historyItem.timestamp) < 0) {
/* 114 */         lastTimestamp = historyItem.timestamp;
/* 115 */         lastOperation = historyItem.message;
/*     */       }  b++; }
/*     */     
/* 118 */     if (lastTimestamp != null) {
/* 119 */       this.lastOperationDate = lastTimestamp.getTime();
/*     */     }
/* 121 */     if (this.inProgress.booleanValue())
/* 122 */       this.currentOperationName = lastOperation; 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanUpgradeStatusData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */