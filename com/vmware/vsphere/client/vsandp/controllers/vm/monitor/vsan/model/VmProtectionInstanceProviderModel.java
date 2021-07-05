/*    */ package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.filter.VmProtectionInstanceFilter;
/*    */ import com.vmware.vsphere.client.vsandp.core.AsModel;
/*    */ import com.vmware.vsphere.client.vsandp.data.ProtectionType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeSet;
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
/*    */ 
/*    */ @data
/*    */ @AsModel
/*    */ public class VmProtectionInstanceProviderModel
/*    */ {
/* 59 */   public List<ProtectionType> protectionTypes = new ArrayList<>();
/* 60 */   public Map<ProtectionType, TreeSet<VmProtectionInstance>> instances = new HashMap<>();
/* 61 */   public List<Date> headerInstances = new ArrayList<>();
/*    */   public Date lowestDate;
/*    */   public Date highestDate;
/*    */   public long instancesCount;
/*    */   public boolean hasRestorePermission;
/*    */   public VmProtectionInstanceFilter filterModel;
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/model/VmProtectionInstanceProviderModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */