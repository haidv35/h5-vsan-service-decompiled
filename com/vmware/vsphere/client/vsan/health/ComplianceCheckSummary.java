/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ @data
/*    */ public class ComplianceCheckSummary
/*    */   extends DataObjectImpl {
/*    */   private static final long serialVersionUID = 1L;
/* 10 */   public int originalFaultDomainCount = 0;
/* 11 */   public int newFaultDomainCount = 0;
/*    */   
/* 13 */   public int originalHostCount = 0;
/* 14 */   public int newHostCount = 0;
/*    */   
/* 16 */   public int originalDiskGroupCount = 0;
/* 17 */   public int newDiskGroupCount = 0;
/*    */   
/* 19 */   public int originalSSDCount = 0;
/* 20 */   public int newSSDCount = 0;
/*    */   
/* 22 */   public int originalCapacityDeviceCount = 0;
/* 23 */   public int newCapacityDeviceCount = 0;
/*    */   
/* 25 */   public long originalTotalCapacity = 0L;
/* 26 */   public long newFinalTotalCapacity = 0L;
/*    */   
/* 28 */   public long originalUsedCapacity = 0L;
/* 29 */   public long newFinalUsedCapacity = 0L;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/ComplianceCheckSummary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */