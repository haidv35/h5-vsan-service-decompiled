/*    */ package com.vmware.vsan.client.services;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.vmware.vise.data.query.DataException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.apache.commons.lang.exception.ExceptionUtils;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*    */ import org.springframework.web.bind.annotation.ResponseBody;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RestControllerBase
/*    */ {
/* 23 */   protected final Log _logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String CLIENT_ABORT_EXCEPTION = "org.apache.catalina.connector.ClientAbortException";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ExceptionHandler({Exception.class})
/*    */   @ResponseBody
/*    */   public Map<String, String> handleException(Exception ex, HttpServletResponse response) {
/* 38 */     response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
/*    */     
/* 40 */     Map<String, String> errorMap = new HashMap<>();
/* 41 */     errorMap.put("message", ex.getMessage());
/*    */     
/* 43 */     Throwable cause = null;
/* 44 */     if (ex.getCause() != null) {
/* 45 */       cause = ex.getCause();
/*    */       
/* 47 */       if (cause.getCause() != null) {
/* 48 */         cause = cause.getCause();
/*    */       }
/* 50 */       errorMap.put("cause", cause.toString());
/*    */     } 
/* 52 */     String[] rootCauseStack = ExceptionUtils.getRootCauseStackTrace(ex);
/* 53 */     if (rootCauseStack.length > 0) {
/* 54 */       String stackTrace = rootCauseStack[0];
/* 55 */       for (int i = 1; i < rootCauseStack.length; i++) {
/* 56 */         stackTrace = String.valueOf(stackTrace) + '\n' + rootCauseStack[i];
/*    */       }
/* 58 */       errorMap.put("stackTrace", stackTrace);
/* 59 */       if (ex instanceof java.io.IOException && "org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName()))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 65 */         return null;
/*    */       }
/* 67 */       this._logger.error("Exception handled in a controller: ", ex);
/*    */     } 
/*    */     
/* 70 */     if (ex instanceof DataException) {
/* 71 */       DataException de = (DataException)ex;
/* 72 */       Gson gson = new Gson();
/* 73 */       if (de.objects != null && de.objects.length > 0) {
/* 74 */         errorMap.put("de_objects", gson.toJson(de.objects));
/*    */       }
/* 76 */       if (de.properties != null && de.properties.length > 0) {
/* 77 */         errorMap.put("de_properties", gson.toJson(de.properties));
/*    */       }
/*    */     } 
/* 80 */     return errorMap;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/RestControllerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */