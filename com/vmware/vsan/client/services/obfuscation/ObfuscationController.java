/*    */ package com.vmware.vsan.client.services.obfuscation;
/*    */ 
/*    */ import com.vmware.vise.data.query.ObjectReferenceService;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.web.bind.annotation.PathVariable;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*    */ @Controller
/*    */ @RequestMapping(value = {"/support/obfuscation"}, method = {RequestMethod.GET})
/*    */ public class ObfuscationController
/*    */ {
/* 32 */   private Logger logger = LoggerFactory.getLogger(ObfuscationController.class);
/* 33 */   private static final VsanProfiler _profiler = new VsanProfiler(ObfuscationController.class);
/*    */   
/*    */   @Autowired
/*    */   private ObjectReferenceService objRefService;
/*    */ 
/*    */   
/*    */   @RequestMapping(value = {"/{operationType}/{objectId}"}, method = {RequestMethod.GET})
/*    */   public void downloadObfuscationMap(@PathVariable("operationType") String operationType, @PathVariable("objectId") String objectId, HttpServletResponse response) throws Exception {
/*    */     try {
/* 42 */       Exception exception2, exception1 = null;
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
/*    */     }
/* 60 */     catch (Exception e) {
/* 61 */       this.logger.error("Failed to download the obfuscation map data.", e);
/* 62 */       throw e;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/obfuscation/ObfuscationController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */