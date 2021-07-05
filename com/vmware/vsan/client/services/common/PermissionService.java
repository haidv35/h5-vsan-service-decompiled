/*     */ package com.vmware.vsan.client.services.common;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.ParameterSpec;
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class PermissionService
/*     */ {
/*  28 */   private Logger logger = LoggerFactory.getLogger(PermissionService.class);
/*     */ 
/*     */   
/*     */   private static final String HAS_PRIVILEGES = "hasPrivileges";
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   ObjectReferenceService objectReferenceService;
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Map<String, Boolean> queryPermissions(ManagedObjectReference objRef, String[] privileges) {
/*  41 */     Map<String, Boolean> result = new HashMap<>();
/*     */     
/*  43 */     if (privileges == null || privileges.length == 0)
/*  44 */       return result;  byte b;
/*     */     int i;
/*     */     String[] arrayOfString;
/*  47 */     for (i = (arrayOfString = privileges).length, b = 0; b < i; ) { String pv = arrayOfString[b];
/*     */       try {
/*  49 */         result.put(pv, Boolean.valueOf(hasPermissions(objRef, new String[] { pv })));
/*  50 */       } catch (Exception e) {
/*  51 */         this.logger.error("Unable to query priviledge: " + pv, e);
/*     */       } 
/*     */       b++; }
/*     */     
/*  55 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean hasVcPermissions(ManagedObjectReference moref, String[] privileges) throws Exception {
/*  64 */     ManagedObjectReference vcRoot = VmodlHelper.getRootFolder(moref.getServerGuid());
/*  65 */     return hasPermissions(vcRoot, privileges);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean hasPermissions(ManagedObjectReference objRef, String[] privileges) throws Exception {
/*  74 */     QuerySpec query = QueryUtil.buildQuerySpec(objRef, new String[] { "hasPrivileges" });
/*     */     
/*  76 */     return checkPermissions(privileges, query);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public boolean havePermissions(ManagedObjectReference[] objRefs, String[] privileges) throws Exception {
/*  85 */     QuerySpec query = QueryUtil.buildQuerySpec((Object[])objRefs, new String[] { "hasPrivileges" });
/*     */     
/*  87 */     return checkPermissions(privileges, query);
/*     */   }
/*     */   
/*     */   private boolean checkPermissions(String[] privileges, QuerySpec query) throws Exception {
/*  91 */     ParameterSpec param = new ParameterSpec();
/*  92 */     param.propertyName = "hasPrivileges";
/*  93 */     param.parameter = privileges;
/*  94 */     (query.resourceSpec.propertySpecs[0]).parameters = new ParameterSpec[] { param };
/*  95 */     ResultSet resultSet = QueryUtil.getData(query);
/*     */     
/*  97 */     return hasUserPermissions(resultSet);
/*     */   }
/*     */   
/*     */   private boolean hasUserPermissions(ResultSet resultSet) {
/* 101 */     boolean hasPrivilege = true;
/*     */     
/* 103 */     if (resultSet != null && ArrayUtils.isNotEmpty((Object[])resultSet.items)) {
/* 104 */       byte b; int i; ResultItem[] arrayOfResultItem; for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem item = arrayOfResultItem[b]; byte b1; int j; PropertyValue[] arrayOfPropertyValue;
/* 105 */         for (j = (arrayOfPropertyValue = item.properties).length, b1 = 0; b1 < j; ) { PropertyValue pv = arrayOfPropertyValue[b1];
/* 106 */           if ("hasPrivileges".equalsIgnoreCase(pv.propertyName)) {
/* 107 */             hasPrivilege = ((Boolean)pv.value).booleanValue(); break;
/*     */           } 
/*     */           b1++; }
/*     */         
/*     */         b++; }
/*     */     
/*     */     } 
/* 114 */     return hasPrivilege;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/PermissionService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */