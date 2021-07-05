/*    */ package com.vmware.vsphere.client.vsan.base.service;
/*    */ 
/*    */ import com.vmware.vim.vmomi.core.types.VmodlContext;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanServiceBundleActivator
/*    */   implements ServiceBundleActivator
/*    */ {
/* 16 */   private static final Log _logger = LogFactory.getLog(VsanServiceBundleActivator.class);
/*    */ 
/*    */   
/*    */   private VmodlContext _vsanVmodlContext;
/*    */ 
/*    */ 
/*    */   
/*    */   public VmodlContext getVmodlContext() {
/* 24 */     return this._vsanVmodlContext;
/*    */   }
/*    */   
/*    */   public VsanServiceBundleActivator() {
/* 28 */     ClassLoader bundleClassLoader = VsanServiceBundleActivator.class.getClassLoader();
/* 29 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*    */     try {
/* 31 */       _logger.debug("Loading VSAN vmodl context.");
/* 32 */       Thread.currentThread().setContextClassLoader(bundleClassLoader);
/* 33 */       this._vsanVmodlContext = VmodlContext.createContext(
/* 34 */           new String[] {
/* 35 */             "com.vmware.vim.binding.vim", 
/* 36 */             "com.vmware.vim.vsan.binding.vim", 
/* 37 */             "com.vmware.vim.vsandp.binding.vim.vsandp"
/*    */           });
/* 39 */       _logger.debug("Successfully loaded VSAN vmodl context.");
/*    */     } finally {
/* 41 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanServiceBundleActivator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */