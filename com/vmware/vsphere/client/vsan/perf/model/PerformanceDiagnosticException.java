/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vmodl.data;
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
/*    */ public class PerformanceDiagnosticException
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String message;
/*    */   public String description;
/*    */   public String exceptionUrl;
/*    */   
/*    */   public PerformanceDiagnosticException() {}
/*    */   
/*    */   public PerformanceDiagnosticException(String exceptionMessage, String exceptionDetails, String exceptionUrl) {
/* 27 */     this.message = exceptionMessage;
/* 28 */     this.description = exceptionDetails;
/* 29 */     this.exceptionUrl = exceptionUrl;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerformanceDiagnosticException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */