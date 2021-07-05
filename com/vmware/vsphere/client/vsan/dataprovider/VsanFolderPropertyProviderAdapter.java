/*    */ package com.vmware.vsphere.client.vsan.dataprovider;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.Folder;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.data.PropertySpec;
/*    */ import com.vmware.vise.data.query.DataServiceExtensionRegistry;
/*    */ import com.vmware.vise.data.query.PropertyProviderAdapter;
/*    */ import com.vmware.vise.data.query.PropertyRequestSpec;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import com.vmware.vise.data.query.ResultItem;
/*    */ import com.vmware.vise.data.query.ResultSet;
/*    */ import com.vmware.vise.data.query.TypeInfo;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang.Validate;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ public class VsanFolderPropertyProviderAdapter
/*    */   implements PropertyProviderAdapter
/*    */ {
/* 25 */   private static final Log _logger = LogFactory.getLog(VsanFolderPropertyProviderAdapter.class);
/*    */   
/*    */   private static final String PROPERTY_IS_VSAN_NESTED_FDS_SUPPORTED = "isVsanNestedFdsSupported";
/*    */   
/*    */   public VsanFolderPropertyProviderAdapter(DataServiceExtensionRegistry registry) {
/* 30 */     Validate.notNull(registry);
/*    */     
/* 32 */     TypeInfo folderInfo = new TypeInfo();
/* 33 */     folderInfo.type = Folder.class.getSimpleName();
/* 34 */     folderInfo.properties = new String[] { "isVsanNestedFdsSupported" };
/*    */     
/* 36 */     TypeInfo[] providedProperties = { folderInfo };
/* 37 */     registry.registerDataAdapter(this, providedProperties);
/*    */   }
/*    */ 
/*    */   
/*    */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/* 42 */     if (!QueryUtil.isValidRequest(propertyRequest)) {
/* 43 */       ResultSet result = new ResultSet();
/* 44 */       result.totalMatchedObjectCount = Integer.valueOf(0);
/* 45 */       return result;
/*    */     } 
/*    */     
/* 48 */     List<ResultItem> resultItems = new ArrayList<>(); byte b; int i; Object[] arrayOfObject;
/* 49 */     for (i = (arrayOfObject = propertyRequest.objects).length, b = 0; b < i; ) { Object objectRef = arrayOfObject[b];
/* 50 */       ManagedObjectReference moRef = (ManagedObjectReference)objectRef;
/* 51 */       if (objectRef != null) {
/*    */ 
/*    */ 
/*    */         
/* 55 */         ResultItem resultItem = null;
/* 56 */         if (Folder.class.getSimpleName().equals(moRef.getType())) {
/* 57 */           PropertyValue[] folderProperties = getFolderProperties(propertyRequest.properties, objectRef);
/* 58 */           resultItem = QueryUtil.newResultItem(objectRef, folderProperties);
/*    */         } 
/*    */         
/* 61 */         resultItems.add(resultItem);
/*    */       }  b++; }
/*    */     
/* 64 */     ResultSet resultSet = QueryUtil.newResultSet(resultItems.<ResultItem>toArray(new ResultItem[resultItems.size()]));
/* 65 */     return resultSet;
/*    */   }
/*    */   
/*    */   private PropertyValue[] getFolderProperties(PropertySpec[] properties, Object objectRef) {
/* 69 */     List<PropertyValue> propValues = new ArrayList<>();
/*    */     
/* 71 */     if (QueryUtil.isAnyPropertyRequested(properties, new String[] { "isVsanNestedFdsSupported" })) {
/* 72 */       PropertyValue propValue = QueryUtil.newProperty("isVsanNestedFdsSupported", 
/* 73 */           Boolean.valueOf(VsanCapabilityUtils.isVsanNestedFdsSupportedOnVc((ManagedObjectReference)objectRef)));
/* 74 */       propValue.resourceObject = objectRef;
/* 75 */       propValues.add(propValue);
/*    */     } 
/*    */     
/* 78 */     return propValues.<PropertyValue>toArray(new PropertyValue[0]);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/dataprovider/VsanFolderPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */