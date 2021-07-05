/*     */ package com.vmware.vsphere.client.vsan.util;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.PropertySpec;
/*     */ import com.vmware.vise.data.query.CompositeConstraint;
/*     */ import com.vmware.vise.data.query.ObjectIdentityConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.QuerySpec;
/*     */ import com.vmware.vise.data.query.Response;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ @data
/*     */ public class QueryUtil {
/*     */   private static ObjectReferenceService _objectReferenceService;
/*     */   private static DataService _dataService;
/*     */   public static final String SERVER_GUID_PROPERTY = "serverGuid";
/*     */   public static final String NAME_PROPERTY = "name";
/*     */   public static final String PRIMARY_ICON_ID_PROPERTY = "primaryIconId";
/*     */   public static final String CLUSTER_PROPERTY = "cluster";
/*     */   public static final String CLUSTER_HOST_PROPERTY = "host";
/*     */   public static final String DATASTORE_PROPERTY = "datastore";
/*     */   
/*     */   public static void setObjectReferenceService(ObjectReferenceService objectReferenceService) {
/*  27 */     _objectReferenceService = objectReferenceService;
/*     */   }
/*     */   public static final String CLUSTER_HOST_COUNT_PROPERTY = "host._length"; public static final String HOST_VSAN_NODE_UUID_PROPERTY = "config.vsanHostConfig.clusterInfo.nodeUuid"; public static final String HOST_CONNECTION_STATE_PROPERTY = "runtime.connectionState"; public static final String HOST_MAINTENANCE_MODE_PROPERTY = "runtime.inMaintenanceMode"; public static final String HOST_QUARANTINE_MODE_PROPERTY = "runtime.inQuarantineMode"; public static final String WITNESS_HOST_RELATION = "witnessHost"; public static final String ALL_VSAN_HOSTS_RELATION = "allVsanHosts"; public static final String VSAN_DISK_GROUP_PROPERTY_NAME = "vsanDisksAndGroupsData"; public static final String VSAN_DISK_MAP_DATA = "vsanDiskMapData"; public static final String VSAN_PHYSICAL_DISK_VIRTUAL_MAPPING = "vsanPhysicalDiskVirtualMapping"; public static final String VSAN_HOST_STORAGE_ADAPTER_DEVICES = "vsanStorageAdapterDevices"; public static final String VM_DEVICES_PROPERTY = "config.hardware.device"; public static final String VM_NAMESPACE_CAPABILITY_METADATA = "namespaceCapabilityMetadata"; public static final String VM_PATH_NAME = "summary.config.vmPathName"; public static final String VSAN_ENABLED_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.enabled"; public static final String HOST_VERSION_PROPERTY = "config.product.version"; public static final String HOST_VSAN_CONFIG_PROPERTY = "config.vsanHostConfig"; public static final String HOST_VSAN_ENABLED_PROPERTY = "config.vsanHostConfig.enabled"; public static final String ISCSI_TARGETS_PROPERTY = "iscsiTargets"; public static final String DISK_CLAIM_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.autoClaimStorage"; public static final String CLUSTER_VSAN_CONFIG_UUID_PROPERTY = "configurationEx[@type='ClusterConfigInfoEx'].vsanConfigInfo.defaultConfig.uuid"; public static final String CLUSTER_DRS_ENABLED = "configuration.drsConfig"; public static final String VM_STORAGE_OBJECT_ID_PROPERTY = "config.vmStorageObjectId";
/*     */   public static final String HOST_FAULT_DOMAIN = "config.vsanHostConfig.faultDomainInfo.name";
/*     */   
/*     */   public static void setDataService(DataService dataService) {
/*  33 */     _dataService = dataService;
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
/*     */   public static <T> T getProperty(ManagedObjectReference target, String propertyName, Object parameter) throws Exception {
/*  85 */     ResourceSpec rs = new ResourceSpec();
/*     */     
/*  87 */     ObjectIdentityConstraint oic = new ObjectIdentityConstraint();
/*  88 */     oic.target = target;
/*  89 */     oic.targetType = target.getType();
/*  90 */     rs.constraint = (Constraint)oic;
/*     */     
/*  92 */     PropertySpec ps = new PropertySpec();
/*  93 */     ps.propertyNames = new String[] { propertyName };
/*     */     
/*  95 */     if (parameter != null) {
/*  96 */       ParameterSpec paramSpec = new ParameterSpec();
/*  97 */       paramSpec.parameter = parameter;
/*  98 */       paramSpec.propertyName = propertyName;
/*  99 */       ps.parameters = new ParameterSpec[] { paramSpec };
/*     */     } else {
/* 101 */       ps.parameters = new ParameterSpec[0];
/*     */     } 
/* 103 */     rs.propertySpecs = new PropertySpec[] { ps };
/*     */     
/* 105 */     QuerySpec query = new QuerySpec();
/* 106 */     query.resourceSpec = rs;
/*     */     
/* 108 */     RequestSpec request = new RequestSpec();
/* 109 */     request.querySpec = new QuerySpec[] { query };
/* 110 */     Response response = _dataService.getData(request);
/* 111 */     if (response.resultSet.length != 1 || (response.resultSet[0]).items.length != 1) {
/* 112 */       throw new IllegalStateException("illegal resource, 1 item expected");
/*     */     }
/* 114 */     if ((response.resultSet[0]).error != null)
/* 115 */       throw (response.resultSet[0]).error;  byte b;
/*     */     int i;
/*     */     PropertyValue[] arrayOfPropertyValue;
/* 118 */     for (i = (arrayOfPropertyValue = ((response.resultSet[0]).items[0]).properties).length, b = 0; b < i; ) { PropertyValue pv = arrayOfPropertyValue[b];
/* 119 */       if (pv.propertyName.equals(propertyName)) {
/* 120 */         return (T)pv.value;
/*     */       }
/*     */       b++; }
/*     */     
/* 124 */     throw new IllegalStateException("Property value not found: " + propertyName);
/*     */   }
/*     */   
/*     */   public static <T> T getProperty(ManagedObjectReference target, String propertyName) throws Exception {
/* 128 */     return getProperty(target, propertyName, null);
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
/*     */   public static DataServiceResponse getProperties(Object obj, String[] properties) throws Exception {
/* 144 */     return getProperties(new Object[] { obj }, properties);
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
/*     */   public static DataServiceResponse getProperties(Object[] objs, String[] properties) throws Exception {
/* 167 */     if (objs == null || objs.length == 0 || 
/* 168 */       properties == null || properties.length == 0) {
/* 169 */       throw new Exception("Invalid parameters for getProperties");
/*     */     }
/*     */     
/* 172 */     Object obj = objs[0];
/*     */     
/* 174 */     QuerySpec query = buildQuerySpec(objs, properties);
/*     */     
/* 176 */     query.name = String.valueOf(_objectReferenceService.getUid(obj)) + ".properties";
/*     */     
/* 178 */     ResultSet resultSet = getData(query);
/* 179 */     return getDataServiceResponse(resultSet, properties);
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
/*     */   public static DataServiceResponse getPropertyForRelatedObjects(Object object, String relationship, String targetType, String property) throws Exception {
/* 195 */     return getPropertiesForRelatedObjects(object, relationship, targetType, new String[] { property });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataServiceResponse getPropertiesForRelatedObjects(Object obj, String relationship, String targetType, String[] properties) throws Exception {
/* 233 */     if (obj == null || properties == null || properties.length == 0) {
/* 234 */       throw new Exception("invalid parameters in getPropertiesForRelatedObjects");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 239 */     if (relationship == null || relationship.length() == 0) {
/* 240 */       return getProperties(obj, properties);
/*     */     }
/*     */ 
/*     */     
/* 244 */     ObjectIdentityConstraint objectConstraint = 
/* 245 */       createObjectIdentityConstraint(obj);
/*     */ 
/*     */     
/* 248 */     RelationalConstraint relationalConstraint = 
/* 249 */       createRelationalConstraint(relationship, 
/* 250 */         (Constraint)objectConstraint, 
/* 251 */         Boolean.valueOf(true), 
/* 252 */         targetType);
/*     */     
/* 254 */     QuerySpec query = buildQuerySpec((Constraint)relationalConstraint, properties);
/* 255 */     query.name = String.valueOf(_objectReferenceService.getUid(obj)) + "." + relationship + ".properties";
/*     */     
/* 257 */     ResultSet resultSet = getData(query);
/* 258 */     return getDataServiceResponse(resultSet, properties);
/*     */   }
/*     */   
/*     */   public static DataServiceResponse getDataServiceResponse(ResultSet resultSet, String[] properties) throws Exception {
/* 262 */     if (resultSet.totalMatchedObjectCount.intValue() == 0 && resultSet.error != null) {
/* 263 */       throw resultSet.error;
/*     */     }
/*     */ 
/*     */     
/* 267 */     List<PropertyValue> result = new ArrayList<>();
/* 268 */     if (resultSet != null && resultSet.items != null) {
/* 269 */       byte b; int i; ResultItem[] arrayOfResultItem; for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem item = arrayOfResultItem[b];
/* 270 */         Map<String, PropertyValue> resultValues = new HashMap<>();
/* 271 */         if (item != null && item.properties != null) {
/* 272 */           byte b1; int j; PropertyValue[] arrayOfPropertyValue; for (j = (arrayOfPropertyValue = item.properties).length, b1 = 0; b1 < j; ) { PropertyValue propValue = arrayOfPropertyValue[b1];
/* 273 */             if (propValue != null) {
/*     */               
/* 275 */               if (propValue.resourceObject == null && item.resourceObject != null) {
/* 276 */                 propValue.resourceObject = item.resourceObject;
/*     */               }
/* 278 */               resultValues.put(propValue.propertyName, propValue);
/* 279 */               result.add(propValue);
/*     */             } 
/*     */             b1++; }
/*     */           
/*     */           String[] arrayOfString;
/* 284 */           for (j = (arrayOfString = properties).length, b1 = 0; b1 < j; ) { String property = arrayOfString[b1];
/* 285 */             if (!resultValues.containsKey(property)) {
/* 286 */               PropertyValue pv = new PropertyValue();
/* 287 */               pv.propertyName = property;
/* 288 */               pv.resourceObject = item.resourceObject;
/* 289 */               pv.value = null;
/* 290 */               result.add(pv);
/*     */             }  b1++; }
/*     */         
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 297 */     return new DataServiceResponse(result.<PropertyValue>toArray(new PropertyValue[0]), properties);
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
/*     */   public static ResultSet getData(QuerySpec query) throws Exception {
/* 311 */     return getDataMultiSpec(new QuerySpec[] { query })[0];
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
/*     */   public static ResultSet[] getDataMultiSpec(QuerySpec[] queries) throws Exception {
/* 325 */     RequestSpec requestSpec = new RequestSpec();
/* 326 */     requestSpec.querySpec = queries;
/*     */     
/* 328 */     Response response = _dataService.getData(requestSpec);
/*     */     
/* 330 */     ResultSet[] result = response.resultSet;
/* 331 */     if (result == null || result.length == 0 || result[0] == null) {
/* 332 */       throw new Exception("Empty result");
/*     */     }
/*     */     
/* 335 */     if ((response.resultSet[0]).error != null) {
/* 336 */       throw (response.resultSet[0]).error;
/*     */     }
/* 338 */     return result;
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
/*     */   public static QuerySpec buildQuerySpec(Object entity, String[] properties) {
/* 350 */     ObjectIdentityConstraint oc = new ObjectIdentityConstraint();
/* 351 */     oc.target = entity;
/*     */     
/* 353 */     String targetType = _objectReferenceService.getResourceObjectType(entity);
/* 354 */     Set<String> targetTypes = new HashSet<>();
/* 355 */     targetTypes.add(targetType);
/*     */     
/* 357 */     QuerySpec query = buildQuerySpec((Constraint)oc, properties, targetTypes);
/* 358 */     return query;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static QuerySpec buildQuerySpec(Object[] entities, String[] properties) {
/* 364 */     if (entities.length == 1) {
/* 365 */       return buildQuerySpec(entities[0], properties);
/*     */     }
/* 367 */     CompositeConstraint cc = new CompositeConstraint();
/* 368 */     cc.conjoiner = Conjoiner.OR;
/* 369 */     Constraint[] nestedConstraints = new Constraint[entities.length];
/* 370 */     Set<String> targetTypes = new HashSet<>();
/* 371 */     String targetType = null;
/*     */     
/* 373 */     for (int index = 0; index < entities.length; index++) {
/* 374 */       ObjectIdentityConstraint oc = new ObjectIdentityConstraint();
/* 375 */       oc.target = entities[index];
/* 376 */       nestedConstraints[index] = (Constraint)oc;
/*     */       
/* 378 */       targetType = _objectReferenceService.getResourceObjectType(oc.target);
/* 379 */       targetTypes.add(targetType);
/*     */     } 
/* 381 */     cc.nestedConstraints = nestedConstraints;
/*     */     
/* 383 */     QuerySpec query = buildQuerySpec((Constraint)cc, properties, targetTypes);
/* 384 */     return query;
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
/*     */   public static QuerySpec buildQuerySpec(Constraint constraint, String[] properties) {
/* 398 */     QuerySpec query = buildQuerySpec(constraint, properties, null);
/* 399 */     return query;
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
/*     */   public static QuerySpec buildQuerySpec(Constraint constraint, String[] properties, Set<String> targetTypes) {
/* 419 */     QuerySpec query = new QuerySpec();
/* 420 */     ResourceSpec resourceSpec = new ResourceSpec();
/* 421 */     resourceSpec.constraint = constraint;
/*     */     
/* 423 */     List<PropertySpec> pSpecs = new ArrayList<>();
/* 424 */     if (targetTypes != null) {
/* 425 */       for (String targetType : targetTypes) {
/* 426 */         PropertySpec propSpec = createPropertySpec(properties, targetType);
/* 427 */         pSpecs.add(propSpec);
/*     */       } 
/*     */     } else {
/* 430 */       PropertySpec propSpec = createPropertySpec(properties, null);
/* 431 */       pSpecs.add(propSpec);
/*     */     } 
/*     */     
/* 434 */     resourceSpec.propertySpecs = pSpecs.<PropertySpec>toArray(new PropertySpec[0]);
/* 435 */     query.resourceSpec = resourceSpec;
/*     */     
/* 437 */     return query;
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
/*     */   public static RelationalConstraint createRelationalConstraint(String relationship, Constraint constraintOnRelatedObject, Boolean hasInverseRelation, String targetType) {
/* 457 */     RelationalConstraint rc = new RelationalConstraint();
/* 458 */     rc.relation = relationship;
/* 459 */     rc.hasInverseRelation = hasInverseRelation.booleanValue();
/* 460 */     rc.constraintOnRelatedObject = constraintOnRelatedObject;
/* 461 */     rc.targetType = targetType;
/* 462 */     return rc;
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
/*     */   public static ObjectIdentityConstraint createObjectIdentityConstraint(Object entity) {
/* 474 */     ObjectIdentityConstraint oc = new ObjectIdentityConstraint();
/* 475 */     oc.target = entity;
/* 476 */     oc.targetType = _objectReferenceService.getResourceObjectType(entity);
/* 477 */     return oc;
/*     */   }
/*     */ 
/*     */   
/*     */   public static PropertyConstraint createPropertyConstraint(String targetType, String propertyName, Comparator comparator, Object value) {
/* 482 */     PropertyConstraint propConstraint = new PropertyConstraint();
/* 483 */     propConstraint.targetType = targetType;
/* 484 */     propConstraint.propertyName = propertyName;
/* 485 */     propConstraint.comparableValue = value;
/* 486 */     propConstraint.comparator = comparator;
/* 487 */     return propConstraint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompositeConstraint createCompositeConstraint(Conjoiner conjoiner, String targetType, Constraint... nestedConstraints) {
/* 497 */     CompositeConstraint constraint = new CompositeConstraint();
/* 498 */     constraint.targetType = targetType;
/* 499 */     constraint.conjoiner = conjoiner;
/* 500 */     constraint.nestedConstraints = nestedConstraints;
/* 501 */     return constraint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static PropertySpec createPropertySpec(String[] properties, String targetType) {
/* 509 */     PropertySpec propSpec = new PropertySpec();
/* 510 */     propSpec.type = targetType;
/* 511 */     propSpec.propertyNames = properties;
/* 512 */     return propSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Response newResponse(ResultSet... resultSet) {
/* 522 */     Response result = new Response();
/* 523 */     result.resultSet = resultSet;
/*     */     
/* 525 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResultSet newResultSet(ResultItem... items) {
/* 535 */     ResultSet result = new ResultSet();
/* 536 */     result.items = items;
/* 537 */     result.totalMatchedObjectCount = (items != null) ? Integer.valueOf(items.length) : null;
/*     */     
/* 539 */     return result;
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
/*     */   public static ResultItem newResultItem(Object object, PropertyValue... props) {
/* 551 */     ResultItem result = new ResultItem();
/* 552 */     result.resourceObject = object;
/* 553 */     result.properties = props;
/*     */     
/* 555 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyValue newProperty(String name, Object value) {
/* 566 */     PropertyValue result = new PropertyValue();
/* 567 */     result.propertyName = name;
/* 568 */     result.value = value;
/*     */     
/* 570 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAnyPropertyRequested(PropertySpec[] propertySpecs, String... properties) {
/* 581 */     if (ArrayUtils.isEmpty((Object[])propertySpecs) || ArrayUtils.isEmpty((Object[])properties)) {
/* 582 */       return false;
/*     */     }
/* 584 */     Set<String> propertiesSet = new HashSet<>(Arrays.asList(properties));
/* 585 */     boolean result = false; byte b; int i; PropertySpec[] arrayOfPropertySpec;
/* 586 */     for (i = (arrayOfPropertySpec = propertySpecs).length, b = 0; b < i; ) { PropertySpec pSpec = arrayOfPropertySpec[b]; byte b1; int j; String[] arrayOfString;
/* 587 */       for (j = (arrayOfString = pSpec.propertyNames).length, b1 = 0; b1 < j; ) { String p = arrayOfString[b1];
/* 588 */         if (propertiesSet.contains(p)) {
/* 589 */           result = true; break;
/*     */         }  b1++; }
/*     */       
/*     */       b++; }
/*     */     
/* 594 */     return result;
/*     */   }
/*     */   
/*     */   public static String[] getPropertyNames(PropertySpec[] props) {
/* 598 */     Set<String> allProperties = new HashSet<>(); byte b; int i; PropertySpec[] arrayOfPropertySpec;
/* 599 */     for (i = (arrayOfPropertySpec = props).length, b = 0; b < i; ) { PropertySpec propSpec = arrayOfPropertySpec[b];
/* 600 */       if (!ArrayUtils.isEmpty((Object[])propSpec.propertyNames)) {
/*     */         byte b1; int j;
/*     */         String[] arrayOfString;
/* 603 */         for (j = (arrayOfString = propSpec.propertyNames).length, b1 = 0; b1 < j; ) { String propertyName = arrayOfString[b1];
/* 604 */           allProperties.add(propertyName); b1++; }
/*     */       
/*     */       }  b++; }
/* 607 */      return allProperties.<String>toArray(new String[allProperties.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<ManagedObjectReference, List<PropertyValue>> groupPropertiesByObject(PropertyValue[] properties) {
/* 617 */     Map<ManagedObjectReference, List<PropertyValue>> result = new HashMap<>(); byte b; int i;
/*     */     PropertyValue[] arrayOfPropertyValue;
/* 619 */     for (i = (arrayOfPropertyValue = properties).length, b = 0; b < i; ) { PropertyValue property = arrayOfPropertyValue[b];
/* 620 */       ManagedObjectReference objectMor = (ManagedObjectReference)property.resourceObject;
/* 621 */       if (!result.containsKey(objectMor)) {
/* 622 */         result.put(objectMor, new ArrayList<>());
/*     */       }
/*     */       
/* 625 */       ((List<PropertyValue>)result.get(objectMor)).add(property);
/*     */       b++; }
/*     */     
/* 628 */     return result;
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
/*     */   public static Constraint combineIntoSingleConstraint(Constraint[] constraints, Conjoiner conjoiner) {
/* 643 */     if (constraints == null || constraints.length == 0) {
/* 644 */       return null;
/*     */     }
/*     */     
/* 647 */     if (constraints.length == 1) {
/* 648 */       return constraints[0];
/*     */     }
/*     */     
/* 651 */     return (Constraint)createCompositeConstraint(constraints, conjoiner);
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
/*     */   public static CompositeConstraint createCompositeConstraint(Constraint[] nestedConstraints, Conjoiner conjoiner) {
/* 668 */     CompositeConstraint compositeConstraint = new CompositeConstraint();
/* 669 */     compositeConstraint.nestedConstraints = nestedConstraints;
/* 670 */     compositeConstraint.conjoiner = conjoiner;
/*     */     
/* 672 */     return compositeConstraint;
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
/*     */   public static Constraint createConstraintForRelationship(Object object, String relationship, String targetType) {
/* 696 */     ObjectIdentityConstraint objectConstraint = 
/* 697 */       createObjectIdentityConstraint(object);
/*     */ 
/*     */     
/* 700 */     RelationalConstraint relationalConstraint = 
/* 701 */       createRelationalConstraint(relationship, 
/* 702 */         (Constraint)objectConstraint, 
/* 703 */         Boolean.valueOf(true), 
/* 704 */         targetType);
/*     */     
/* 706 */     return (Constraint)relationalConstraint;
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
/*     */   public static void throwIfObjectNotFound(Object[] targetEntities, ResultSet resultSet) throws ManagedObjectNotFound {
/* 722 */     Object[] deletedObjects = detectDeletedObjects(targetEntities, resultSet);
/*     */     
/* 724 */     if (deletedObjects.length == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 729 */     Object firstObject = deletedObjects[0];
/* 730 */     if (firstObject instanceof ManagedObjectReference) {
/* 731 */       throw new ManagedObjectNotFound((ManagedObjectReference)firstObject);
/*     */     }
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
/*     */   public static Object[] detectDeletedObjects(Object[] targetEntities, ResultSet resultSet) {
/* 750 */     if (targetEntities == null || targetEntities.length == 0)
/*     */     {
/* 752 */       return targetEntities;
/*     */     }
/*     */     
/* 755 */     if (resultSet == null || resultSet.error != null)
/*     */     {
/*     */       
/* 758 */       return (Object[])Array.newInstance(targetEntities[0].getClass(), 0);
/*     */     }
/*     */     
/* 761 */     HashMap<String, Object> deletedObjects = new HashMap<>(); byte b; int i;
/*     */     Object[] arrayOfObject;
/* 763 */     for (i = (arrayOfObject = targetEntities).length, b = 0; b < i; ) { Object entity = arrayOfObject[b];
/* 764 */       deletedObjects.put(_objectReferenceService.getUid(entity), entity);
/*     */       b++; }
/*     */     
/* 767 */     if (resultSet.items != null) {
/*     */       ResultItem[] arrayOfResultItem;
/*     */       
/* 770 */       for (i = (arrayOfResultItem = resultSet.items).length, b = 0; b < i; ) { ResultItem resultItem = arrayOfResultItem[b];
/* 771 */         Object object = resultItem.resourceObject;
/* 772 */         if (object != null) {
/* 773 */           deletedObjects.remove(_objectReferenceService.getUid(object));
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/* 778 */     Collection<Object> result = deletedObjects.values();
/* 779 */     return result.toArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidRequest(PropertyRequestSpec propertyRequest) {
/* 786 */     if (propertyRequest == null) {
/* 787 */       return false;
/*     */     }
/* 789 */     if (ArrayUtils.isEmpty(propertyRequest.objects) || 
/* 790 */       ArrayUtils.isEmpty((Object[])propertyRequest.properties)) {
/* 791 */       return false;
/*     */     }
/* 793 */     return true;
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
/*     */   public static PropertyValue[] createPropValue(String name, Object value, Object provider) {
/* 805 */     PropertyValue propValue = new PropertyValue();
/* 806 */     propValue.propertyName = name;
/* 807 */     propValue.value = value;
/* 808 */     propValue.resourceObject = provider;
/* 809 */     return new PropertyValue[] { propValue };
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
/*     */   public static ResultItem createResultItem(String property, Object value, Object provider) {
/* 821 */     ResultItem resultItem = new ResultItem();
/* 822 */     resultItem.resourceObject = provider;
/* 823 */     resultItem.properties = createPropValue(property, value, provider);
/* 824 */     return resultItem;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/QueryUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */