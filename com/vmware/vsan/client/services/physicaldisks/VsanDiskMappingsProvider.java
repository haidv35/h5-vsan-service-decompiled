/*     */ package com.vmware.vsan.client.services.physicaldisks;
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.query.ObjectIdentityConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.RelationalConstraint;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vsphere.client.vsan.data.HostPhysicalMappingData;
/*     */ import com.vmware.vsphere.client.vsan.data.PhysicalDiskData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskAndGroupData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskData;
/*     */ import com.vmware.vsphere.client.vsan.data.VsanDiskGroupData;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class VsanDiskMappingsProvider {
/*  24 */   private static final Log logger = LogFactory.getLog(VsanDiskMappingsProvider.class);
/*     */   
/*  26 */   private static final String[] PHYSICAL_DISK_MAPPINGS_HOST_PROPERTIES = new String[] {
/*  27 */       "vsanDisksAndGroupsData", 
/*  28 */       "vsanPhysicalDiskVirtualMapping", 
/*  29 */       "vsanStorageAdapterDevices", 
/*  30 */       "name", 
/*  31 */       "config.vsanHostConfig.faultDomainInfo.name", 
/*  32 */       "primaryIconId" };
/*     */   
/*     */   public List<HostPhysicalMappingData> getVsanHostsPhysicalDiskData(ManagedObjectReference clusterRef) throws Exception {
/*  35 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/*  60 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } private QuerySpec getClusterHostsQuerySpec(ManagedObjectReference clusterRef, String[] properties) {
/*  64 */     ObjectIdentityConstraint clusterConstraint = 
/*  65 */       QueryUtil.createObjectIdentityConstraint(clusterRef);
/*  66 */     RelationalConstraint clusterHostsConstraint = 
/*  67 */       QueryUtil.createRelationalConstraint("host", (Constraint)clusterConstraint, 
/*  68 */         Boolean.valueOf(true), HostSystem.class.getSimpleName());
/*  69 */     QuerySpec querySpecHosts = QueryUtil.buildQuerySpec((Constraint)clusterHostsConstraint, properties);
/*  70 */     return querySpecHosts;
/*     */   }
/*     */   
/*     */   private HostMappingData getHostMappingData(ResultItem resultItem) {
/*  74 */     String jsonString = "";
/*  75 */     VsanDiskAndGroupData diskAndGroupData = null;
/*  76 */     Object[] vsanStorageAdapterDevices = null;
/*  77 */     String hostName = "";
/*  78 */     String primaryIconId = "";
/*  79 */     String faultDomain = ""; byte b; int i; PropertyValue[] arrayOfPropertyValue;
/*  80 */     for (i = (arrayOfPropertyValue = resultItem.properties).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b]; String str;
/*  81 */       switch ((str = propValue.propertyName).hashCode()) { case -826278890: if (str.equals("primaryIconId"))
/*     */           
/*     */           { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  95 */             primaryIconId = (String)propValue.value; break; } 
/*     */         case 3373707: if (str.equals("name")) { hostName = (String)propValue.value; break; } 
/*     */         case 260829889: if (str.equals("vsanPhysicalDiskVirtualMapping")) { jsonString = (String)propValue.value; break; } 
/*  98 */         case 707737491: if (str.equals("config.vsanHostConfig.faultDomainInfo.name")) { faultDomain = (String)propValue.value; break; } 
/*     */         case 1063446345: if (str.equals("vsanDisksAndGroupsData")) { diskAndGroupData = (VsanDiskAndGroupData)propValue.value; break; } 
/*     */         case 1496642655: if (str.equals("vsanStorageAdapterDevices")) { vsanStorageAdapterDevices = (Object[])propValue.value; break; } 
/* 101 */         default: logger.warn("Unknown property received: " + propValue.propertyName + " = " + propValue.value); break; }
/*     */       
/*     */       b++; }
/*     */     
/* 105 */     return new HostMappingData(jsonString, 
/* 106 */         diskAndGroupData, 
/* 107 */         vsanStorageAdapterDevices, 
/* 108 */         hostName, 
/* 109 */         primaryIconId, 
/* 110 */         faultDomain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<PhysicalDiskData> getHostDisks(VsanDiskAndGroupData diskAndGroupData, JsonNode json, ManagedObjectReference hostRef, ManagedObjectReference clusterRef) {
/* 117 */     List<PhysicalDiskData> hostDisks = new ArrayList<>();
/* 118 */     if (diskAndGroupData != null && diskAndGroupData.vsanGroups != null) {
/* 119 */       byte b; int i; VsanDiskGroupData[] arrayOfVsanDiskGroupData; for (i = (arrayOfVsanDiskGroupData = diskAndGroupData.vsanGroups).length, b = 0; b < i; ) { VsanDiskGroupData groupData = arrayOfVsanDiskGroupData[b];
/* 120 */         if (groupData.disks != null) {
/*     */ 
/*     */ 
/*     */           
/* 124 */           hostDisks.add(new PhysicalDiskData(groupData.ssd, hostRef, json, clusterRef)); byte b1; int j; VsanDiskData[] arrayOfVsanDiskData;
/* 125 */           for (j = (arrayOfVsanDiskData = groupData.disks).length, b1 = 0; b1 < j; ) { VsanDiskData diskData = arrayOfVsanDiskData[b1];
/* 126 */             hostDisks.add(new PhysicalDiskData(diskData, hostRef, json, clusterRef)); b1++; }
/*     */         
/*     */         }  b++; }
/*     */     
/* 130 */     }  return hostDisks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class HostMappingData
/*     */   {
/*     */     public VsanDiskAndGroupData diskAndGroupData;
/*     */     
/*     */     public Object[] vsanStorageAdapterDevices;
/*     */     
/*     */     public String hostName;
/*     */     
/*     */     public String primaryIconId;
/*     */     
/*     */     public String faultDomain;
/*     */     
/*     */     public JsonNode json;
/*     */ 
/*     */     
/*     */     public HostMappingData(String jsonString, VsanDiskAndGroupData diskAndGroupData, Object[] vsanStorageAdapterDevices, String hostName, String primaryIconId, String faultDomain) {
/* 151 */       this.json = Utils.getJsonRootNode(jsonString);
/* 152 */       this.diskAndGroupData = diskAndGroupData;
/* 153 */       this.vsanStorageAdapterDevices = vsanStorageAdapterDevices;
/* 154 */       this.hostName = hostName;
/* 155 */       this.primaryIconId = primaryIconId;
/* 156 */       this.faultDomain = faultDomain;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/physicaldisks/VsanDiskMappingsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */