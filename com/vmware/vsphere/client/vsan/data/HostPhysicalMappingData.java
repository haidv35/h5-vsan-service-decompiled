/*    */ package com.vmware.vsphere.client.vsan.data;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.List;
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
/*    */ public class HostPhysicalMappingData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ManagedObjectReference hostRef;
/*    */   public ManagedObjectReference clusterRef;
/*    */   public String name;
/*    */   public String primaryIconId;
/*    */   public List<PhysicalDiskData> physicalDisks;
/*    */   public Object[] storageAdapterDevices;
/*    */   public String faultDomain;
/*    */   
/*    */   public HostPhysicalMappingData() {}
/*    */   
/*    */   public HostPhysicalMappingData(ManagedObjectReference clusterRef, ManagedObjectReference hostRef, String hostName, String primaryIconId, List<PhysicalDiskData> physicalDsks, Object[] storageAdapters, String faultDomain) {
/* 64 */     this.clusterRef = clusterRef;
/* 65 */     this.hostRef = hostRef;
/* 66 */     this.name = hostName;
/* 67 */     this.primaryIconId = primaryIconId;
/* 68 */     this.physicalDisks = physicalDsks;
/* 69 */     this.storageAdapterDevices = storageAdapters;
/* 70 */     this.faultDomain = faultDomain;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/HostPhysicalMappingData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */