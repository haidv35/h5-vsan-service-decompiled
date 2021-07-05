/*    */ package com.vmware.vsan.client.util;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.vm.device.VirtualDisk;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class VcPropertiesFacade
/*    */ {
/*    */   public boolean isNativeUnmanagedLinkedClone(VirtualDisk disk) {
/* 10 */     return (disk.getNativeUnmanagedLinkedClone() != null && disk.getNativeUnmanagedLinkedClone().booleanValue());
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/VcPropertiesFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */