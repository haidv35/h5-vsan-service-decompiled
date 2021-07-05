/*    */ package com.vmware.vsphere.client.vsandp.helper;
/*    */ 
/*    */ import com.vmware.vim.vmomi.core.types.VmodlContext;
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
/*    */ public final class VmodlContextInitializer
/*    */ {
/*    */   public static VmodlContext createContext(String[] vmodls) {
/* 21 */     ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
/* 22 */     VmodlContext vmodlContext = null;
/*    */     try {
/* 24 */       Thread.currentThread().setContextClassLoader(VmodlContextInitializer.class.getClassLoader());
/* 25 */       vmodlContext = VmodlContext.createContext(vmodls);
/*    */     } finally {
/* 27 */       Thread.currentThread().setContextClassLoader(originalClassLoader);
/*    */     } 
/*    */     
/* 30 */     return vmodlContext;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/helper/VmodlContextInitializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */