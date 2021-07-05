/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ @data
/*    */ public class ComplianceCheckResultObj
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public boolean isNew;
/*    */   public boolean hasChanged;
/* 13 */   public long originalCapacity = 0L;
/* 14 */   public long finalCapacity = 0L;
/* 15 */   public long initCapacity = 0L;
/* 16 */   public long finalUsedCapacity = 0L;
/*    */   
/* 18 */   public long originalCacheCapacity = 0L;
/* 19 */   public long finalCacheCapacity = 0L;
/* 20 */   public long initCacheCapacity = 0L;
/* 21 */   public long finalUsedCacheCapacity = 0L;
/*    */   public String uuid;
/*    */   public String name;
/*    */   public String objectType;
/*    */   public ComplianceCheckResultObj[] childDevices;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/ComplianceCheckResultObj.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */