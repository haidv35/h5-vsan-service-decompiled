/*    */ package com.vmware.vsphere.client.vsan.util;
/*    */ 
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections.map.HashedMap;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataServiceResponse
/*    */ {
/* 19 */   private static final Logger logger = LoggerFactory.getLogger(DataServiceResponse.class);
/*    */   
/*    */   public static final String RESOURCE_OBJECT = "__resourceObject";
/*    */   
/*    */   private final PropertyValue[] propertyValues;
/*    */   private Map<Object, Map<String, Object>> mappedProperties;
/*    */   private final String[] properties;
/*    */   
/*    */   DataServiceResponse(PropertyValue[] propertyValues, String[] properties) {
/* 28 */     this.properties = properties;
/* 29 */     this.propertyValues = propertyValues;
/*    */   }
/*    */   
/*    */   public String[] getRequestedProperties() {
/* 33 */     return this.properties;
/*    */   }
/*    */   
/*    */   public PropertyValue[] getPropertyValues() {
/* 37 */     return this.propertyValues;
/*    */   }
/*    */   
/*    */   public <T> Map<T, Map<String, Object>> getMap() {
/* 41 */     if (this.mappedProperties == null) {
/* 42 */       if (this.propertyValues.length % this.properties.length != 0) {
/* 43 */         logger.warn("The DataService didn't return data for all the requested properties!", (Object[])this.propertyValues);
/*    */       }
/*    */       
/* 46 */       this.mappedProperties = new HashMap<>(); byte b; int i;
/*    */       PropertyValue[] arrayOfPropertyValue;
/* 48 */       for (i = (arrayOfPropertyValue = this.propertyValues).length, b = 0; b < i; ) { HashedMap<String, Object> hashedMap; PropertyValue propertyValue = arrayOfPropertyValue[b];
/* 49 */         if (!(propertyValue.resourceObject instanceof Object)) {
/* 50 */           throw new IllegalStateException("unknown resource object: " + propertyValue.resourceObject);
/*    */         }
/*    */         
/* 53 */         Object resourceObject = propertyValue.resourceObject;
/*    */         
/* 55 */         Map<String, Object> resourceProperties = this.mappedProperties.get(resourceObject);
/* 56 */         if (resourceProperties == null) {
/* 57 */           hashedMap = new HashedMap();
/* 58 */           hashedMap.put("__resourceObject", resourceObject);
/* 59 */           this.mappedProperties.put(resourceObject, hashedMap);
/*    */         } 
/*    */         
/* 62 */         hashedMap.put(propertyValue.propertyName, propertyValue.value); b++; }
/*    */     
/*    */     } 
/* 65 */     return Collections.unmodifiableMap((Map)this.mappedProperties);
/*    */   }
/*    */   
/*    */   public Set<Object> getResourceObjects() {
/* 69 */     return getMap().keySet();
/*    */   }
/*    */   
/*    */   public <P> P getProperty(Object resourceObject, String property) {
/* 73 */     Map<String, Object> objectProperties = (Map<String, Object>)getMap().get(resourceObject);
/* 74 */     if (!objectProperties.containsKey(property)) {
/* 75 */       throw new IllegalStateException("property not found: " + property);
/*    */     }
/* 77 */     return (P)objectProperties.get(property);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/DataServiceResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */