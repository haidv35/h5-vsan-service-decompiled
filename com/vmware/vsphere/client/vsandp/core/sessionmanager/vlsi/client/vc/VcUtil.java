/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.Extension;
/*    */ import com.vmware.vim.binding.vim.ExtensionManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VcUtil
/*    */ {
/*    */   public static Extension findExtension(VcConnection vc, String key) {
/* 14 */     ExtensionManager extMan = (ExtensionManager)vc.createStub(
/* 15 */         ExtensionManager.class, vc.getContent().getExtensionManager());
/* 16 */     return extMan.findExtension(key);
/*    */   }
/*    */   
/*    */   public static Extension.ServerInfo findServer(VcConnection vc, String key) {
/* 20 */     Extension ext = findExtension(vc, key);
/* 21 */     if (ext == null || ext.getServer() == null || (ext.getServer()).length < 1 || 
/* 22 */       ext.getServer()[0].getUrl() == null) {
/* 23 */       throw new RuntimeException("Bad or missing " + key + 
/* 24 */           " extension on VC: " + vc);
/*    */     }
/*    */     
/* 27 */     return ext.getServer()[0];
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/VcUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */