/*     */ package com.vmware.vsan.client.services;
/*     */ 
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ @RequestMapping(value = {"/data"}, method = {RequestMethod.GET})
/*     */ public class DataAccessController
/*     */   extends RestControllerBase
/*     */ {
/*     */   private static final String OBJECT_ID = "id";
/*     */   private final ObjectReferenceService _objectReferenceService;
/*     */   
/*     */   @Autowired
/*     */   public DataAccessController(@Qualifier("objectReferenceService") ObjectReferenceService objectReferenceService) {
/*  34 */     this._objectReferenceService = objectReferenceService;
/*  35 */     QueryUtil.setObjectReferenceService(objectReferenceService);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataAccessController() {
/*  41 */     this._objectReferenceService = null;
/*     */   }
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
/*     */   @RequestMapping({"/properties/{objectId}"})
/*     */   @ResponseBody
/*     */   public Map<String, Object> getProperties(@PathVariable("objectId") String encodedObjectId, @RequestParam("properties") String properties) throws Exception {
/*  66 */     Object ref = getDecodedReference(encodedObjectId);
/*  67 */     String objectId = this._objectReferenceService.getUid(ref);
/*     */     
/*  69 */     String[] props = properties.split(",");
/*  70 */     PropertyValue[] pvs = QueryUtil.getProperties(ref, props).getPropertyValues();
/*  71 */     Map<String, Object> propsMap = new HashMap<>();
/*  72 */     propsMap.put("id", objectId); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/*  73 */     for (i = (arrayOfPropertyValue1 = pvs).length, b = 0; b < i; ) { PropertyValue pv = arrayOfPropertyValue1[b];
/*  74 */       propsMap.put(pv.propertyName, pv.value); b++; }
/*     */     
/*  76 */     return propsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/multiObjectProperties/{objectIds}"})
/*     */   @ResponseBody
/*     */   public Object getMultiObjectProperties(@PathVariable("objectIds") String[] objectIds, @RequestParam("properties") String props) throws Exception {
/*  84 */     List<Object> objects = new ArrayList(); byte b; int i; String[] arrayOfString1;
/*  85 */     for (i = (arrayOfString1 = objectIds).length, b = 0; b < i; ) { String objectId = arrayOfString1[b];
/*  86 */       objects.add(getDecodedReference(objectId));
/*     */       b++; }
/*     */     
/*  89 */     String[] properties = props.split(",");
/*  90 */     PropertyValue[] pvs = QueryUtil.getProperties(objects.toArray(), properties).getPropertyValues();
/*  91 */     return pvs;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/propertiesByRelation/{objectId}"})
/*     */   @ResponseBody
/*     */   public PropertyValue[] getPropertiesForRelatedObject(@PathVariable("objectId") String encodedObjectId, @RequestParam(value = "relation", required = true) String relation, @RequestParam(value = "targetType", required = true) String targetType, @RequestParam(value = "properties", required = true) String properties) throws Exception {
/* 123 */     Object ref = getDecodedReference(encodedObjectId);
/* 124 */     String[] props = properties.split(",");
/* 125 */     PropertyValue[] result = 
/* 126 */       QueryUtil.getPropertiesForRelatedObjects(ref, relation, targetType, props).getPropertyValues();
/* 127 */     return result;
/*     */   }
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
/*     */   private Object getDecodedReference(String encodedObjectId) throws Exception {
/* 141 */     Object ref = this._objectReferenceService.getReference(encodedObjectId, true);
/* 142 */     if (ref == null) {
/* 143 */       throw new Exception("Object not found with id: " + encodedObjectId);
/*     */     }
/* 145 */     return ref;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/DataAccessController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */