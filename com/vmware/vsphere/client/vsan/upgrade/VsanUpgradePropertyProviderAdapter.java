/*     */ package com.vmware.vsphere.client.vsan.upgrade;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.HostSystem;
/*     */ import com.vmware.vim.binding.vim.host.ScsiDisk;
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*     */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.TypeInfo;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VsanUpgradePropertyProviderAdapter
/*     */   implements PropertyProviderAdapter
/*     */ {
/*     */   private static final String VSAN_DISK_VERSION_PROPERTY_NAME = "vsanDiskVersionsData";
/*     */   private static final String DISK_MAPPINGS = "config.vsanHostConfig.storageInfo.diskMapping";
/*  34 */   private static final Log _logger = LogFactory.getLog(VsanUpgradePropertyProviderAdapter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VsanUpgradePropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/*  42 */     Validate.notNull(registry);
/*     */     
/*  44 */     TypeInfo hostInfo = new TypeInfo();
/*  45 */     hostInfo.type = HostSystem.class.getSimpleName();
/*  46 */     hostInfo.properties = new String[] { "vsanDiskVersionsData" };
/*     */     
/*  48 */     TypeInfo[] providedProperties = { hostInfo };
/*  49 */     registry.registerDataAdapter(this, providedProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  54 */     if (!isValidRequest(propertyRequest)) {
/*  55 */       ResultSet result = new ResultSet();
/*  56 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/*  57 */       return result;
/*     */     } 
/*     */     
/*  60 */     ResultSet resultSet = null;
/*  61 */     List<ResultItem> resultItems = new ArrayList<>();
/*     */     try {
/*  63 */       PropertyValue[] propValues = QueryUtil.getProperties(propertyRequest.objects, new String[] { "config.vsanHostConfig.storageInfo.diskMapping"
/*  64 */           }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  65 */       for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/*  66 */         DiskMapping[] diskMappings = (DiskMapping[])propValue.value;
/*  67 */         ManagedObjectReference hostRef = 
/*  68 */           (ManagedObjectReference)propValue.resourceObject;
/*  69 */         VsanDiskVersionData[] hostDiskVersionData = 
/*  70 */           getHostDiskVersionsData(hostRef, diskMappings);
/*  71 */         PropertyValue resultPropValue = 
/*  72 */           QueryUtil.newProperty("vsanDiskVersionsData", hostDiskVersionData);
/*  73 */         resultPropValue.resourceObject = hostRef;
/*  74 */         ResultItem resultItem = QueryUtil.newResultItem(hostRef, new PropertyValue[] { resultPropValue });
/*  75 */         resultItems.add(resultItem); b++; }
/*     */     
/*  77 */     } catch (Exception ex) {
/*  78 */       _logger.error("Failed to retrieve properties from DS. ", ex);
/*  79 */       resultSet = new ResultSet();
/*  80 */       resultSet.error = ex;
/*  81 */       return resultSet;
/*     */     } 
/*     */     
/*  84 */     resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/*     */     
/*  86 */     return resultSet;
/*     */   }
/*     */   
/*     */   private VsanDiskVersionData getDiskVersionData(ScsiDisk scsiDisk) {
/*  90 */     if (scsiDisk.vsanDiskInfo == null) {
/*  91 */       return new VsanDiskVersionData();
/*     */     }
/*  93 */     return new VsanDiskVersionData(scsiDisk.vsanDiskInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanDiskVersionData[] getHostDiskVersionsData(ManagedObjectReference host, DiskMapping[] diskGroups) throws Exception {
/* 104 */     if (ArrayUtils.isEmpty((Object[])diskGroups)) {
/* 105 */       return null;
/*     */     }
/*     */     
/* 108 */     List<VsanDiskVersionData> disksData = new ArrayList<>(); byte b; int i; DiskMapping[] arrayOfDiskMapping;
/* 109 */     for (i = (arrayOfDiskMapping = diskGroups).length, b = 0; b < i; ) { DiskMapping diskGroup = arrayOfDiskMapping[b];
/* 110 */       disksData.add(getDiskVersionData(diskGroup.ssd)); byte b1; int j;
/*     */       ScsiDisk[] arrayOfScsiDisk;
/* 112 */       for (j = (arrayOfScsiDisk = diskGroup.nonSsd).length, b1 = 0; b1 < j; ) { ScsiDisk disk = arrayOfScsiDisk[b1];
/* 113 */         disksData.add(getDiskVersionData(disk)); b1++; }
/*     */       
/*     */       b++; }
/*     */     
/* 117 */     return disksData.<VsanDiskVersionData>toArray(new VsanDiskVersionData[disksData.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidRequest(PropertyRequestSpec propertyRequest) {
/* 124 */     if (propertyRequest == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (ArrayUtils.isEmpty(propertyRequest.objects) || 
/* 128 */       ArrayUtils.isEmpty((Object[])propertyRequest.properties)) {
/* 129 */       _logger.error("Property provider adapter got a null or empty list of properties or objects");
/*     */       
/* 131 */       return false;
/*     */     } 
/* 133 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanUpgradePropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */