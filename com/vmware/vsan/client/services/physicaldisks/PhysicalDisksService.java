/*    */ package com.vmware.vsan.client.services.physicaldisks;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.services.virtualobjects.VirtualObjectsService;
/*    */ import com.vmware.vsan.client.services.virtualobjects.data.VirtualObjectModel;
/*    */ import com.vmware.vsphere.client.vsan.data.HostPhysicalMappingData;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class PhysicalDisksService
/*    */ {
/* 23 */   private static final Log logger = LogFactory.getLog(PhysicalDisksService.class);
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private VsanDiskMappingsProvider diskMappingsProvider;
/*    */   
/*    */   @Autowired
/*    */   private VirtualObjectsService virtualObjectsService;
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public List<PhysicalDisksHostData> getPhysicalDisksData(ManagedObjectReference clusterRef) throws Exception {
/* 35 */     List<HostPhysicalMappingData> vsanHostsPhysicalDiskData = this.diskMappingsProvider.getVsanHostsPhysicalDiskData(clusterRef);
/*    */ 
/*    */     
/* 38 */     List<VirtualObjectModel> diskItems = new ArrayList<>();
/*    */     try {
/* 40 */       diskItems = this.virtualObjectsService.listVirtualObjects(clusterRef);
/* 41 */     } catch (Exception e) {
/* 42 */       logger.error("Unable to extract physical disks virtual objects data: " + e);
/*    */     } 
/*    */ 
/*    */     
/* 46 */     List<PhysicalDisksHostData> result = new ArrayList<>(vsanHostsPhysicalDiskData.size());
/* 47 */     for (HostPhysicalMappingData hostDisksMappingData : vsanHostsPhysicalDiskData) {
/* 48 */       PhysicalDisksHostData hostData = new PhysicalDisksHostData(hostDisksMappingData);
/* 49 */       hostData.setVirtualObjectsData(diskItems);
/* 50 */       result.add(hostData);
/*    */     } 
/*    */     
/* 53 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/physicaldisks/PhysicalDisksService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */