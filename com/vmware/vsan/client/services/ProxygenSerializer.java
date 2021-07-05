/*     */ package com.vmware.vsan.client.services;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.data;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Documented;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxygenSerializer
/*     */ {
/*  37 */   private static Logger logger = LoggerFactory.getLogger(ProxygenSerializer.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Documented
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.PARAMETER, ElementType.FIELD})
/*     */   public static @interface ElementType
/*     */   {
/*     */     Class<?> value();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Class<?> key() default void.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MethodPrefix
/*     */   {
/*     */     static final String GET = "get";
/*     */ 
/*     */ 
/*     */     
/*     */     static final String IS = "is";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserialize(Object data, Class<?> type, ElementType metadata) throws Exception {
/*     */     try {
/*  73 */       if (data == null) {
/*  74 */         if (type.isPrimitive()) {
/*  75 */           return getPrimitiveDefaultValue(type);
/*     */         }
/*  77 */         return null;
/*     */       } 
/*     */       
/*  80 */       if (Map.class.isAssignableFrom(type)) {
/*  81 */         return deserializeAsMap(data, metadata);
/*     */       }
/*     */       
/*  84 */       if (List.class.isAssignableFrom(type)) {
/*  85 */         return deserializeAsList(data, metadata);
/*     */       }
/*     */       
/*  88 */       if (Set.class.isAssignableFrom(type)) {
/*  89 */         return deserializeAsSet(data, metadata);
/*     */       }
/*     */       
/*  92 */       if (Date.class.isAssignableFrom(type)) {
/*  93 */         return new Date(Long.parseLong(data.toString()));
/*     */       }
/*     */       
/*  96 */       if (Calendar.class.isAssignableFrom(type)) {
/*  97 */         Calendar result = Calendar.getInstance();
/*  98 */         result.setTimeInMillis(Long.parseLong(data.toString()));
/*  99 */         return result;
/*     */       } 
/*     */       
/* 102 */       if (type.isArray()) {
/*     */         
/* 104 */         if (type.getComponentType() == byte.class || type.getComponentType() == Byte.class) {
/* 105 */           if (data instanceof String) {
/* 106 */             return ((String)data).getBytes();
/*     */           }
/* 108 */           if (data instanceof List) {
/* 109 */             List<Number> list = (List<Number>)data;
/* 110 */             byte[] result = new byte[list.size()];
/* 111 */             for (int i = 0; i < list.size(); i++) {
/* 112 */               result[i] = ((Number)list.get(i)).byteValue();
/*     */             }
/* 114 */             return result;
/*     */           } 
/*     */         } 
/* 117 */         return deserializeAsArray((List<Object>)data, type);
/*     */       } 
/*     */       
/* 120 */       if (Enum.class.isAssignableFrom(type)) {
/* 121 */         return type.getMethod("valueOf", new Class[] { String.class }).invoke(null, new Object[] { data.toString() });
/*     */       }
/*     */       
/* 124 */       if (data instanceof Map) {
/* 125 */         return deserializeAsModel((Map<String, Object>)data, type);
/*     */       }
/*     */ 
/*     */       
/* 129 */       return deserializeAsPrimitive(data, type);
/* 130 */     } catch (Exception e) {
/*     */       
/* 132 */       throw new IllegalStateException("Cannot deserialize as " + type + ": " + data, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object getPrimitiveDefaultValue(Class<?> type) {
/* 137 */     if (type == boolean.class) {
/* 138 */       return Boolean.valueOf(false);
/*     */     }
/*     */     
/* 141 */     if (type == byte.class) {
/* 142 */       return Byte.valueOf((byte)0);
/*     */     }
/*     */     
/* 145 */     if (type == short.class) {
/* 146 */       return Short.valueOf((short)0);
/*     */     }
/*     */     
/* 149 */     if (type == int.class) {
/* 150 */       return Integer.valueOf(0);
/*     */     }
/*     */     
/* 153 */     if (type == long.class) {
/* 154 */       return Long.valueOf(0L);
/*     */     }
/*     */     
/* 157 */     if (type == float.class) {
/* 158 */       return Float.valueOf(0.0F);
/*     */     }
/*     */     
/* 161 */     if (type == double.class) {
/* 162 */       return Double.valueOf(0.0D);
/*     */     }
/*     */     
/* 165 */     return type.cast(null);
/*     */   }
/*     */   
/*     */   private Object deserializeAsPrimitive(Object data, Class<?> type) {
/*     */     try {
/* 170 */       if (type == boolean.class || type == Boolean.class) {
/* 171 */         return Boolean.valueOf((String)data);
/*     */       }
/*     */       
/* 174 */       if (type == byte.class || type == Byte.class) {
/* 175 */         return Byte.valueOf(((Number)data).byteValue());
/*     */       }
/*     */       
/* 178 */       if (type == short.class || type == Short.class) {
/* 179 */         return Short.valueOf(((Number)data).shortValue());
/*     */       }
/*     */       
/* 182 */       if (type == int.class || type == Integer.class) {
/* 183 */         return Integer.valueOf(((Number)data).intValue());
/*     */       }
/*     */       
/* 186 */       if (type == long.class || type == Long.class) {
/* 187 */         return Long.valueOf(((Number)data).longValue());
/*     */       }
/*     */       
/* 190 */       if (type == float.class || type == Float.class) {
/* 191 */         return Float.valueOf(((Number)data).floatValue());
/*     */       }
/*     */       
/* 194 */       if (type == double.class || type == Double.class) {
/* 195 */         return Double.valueOf(((Number)data).doubleValue());
/*     */       }
/*     */       
/* 198 */       return type.cast(data);
/* 199 */     } catch (Exception e) {
/* 200 */       throw new IllegalArgumentException(
/* 201 */           String.format("Cannot deserialize primitive %s(%s) as %s", new Object[] { data.getClass(), data, type }), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object deserializeAsModel(Map<String, Object> data, Class<?> type) throws Exception {
/* 206 */     Map<String, Object> kvPairs = data;
/* 207 */     Object instance = type.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */ 
/*     */     
/* 210 */     for (String key : kvPairs.keySet()) {
/*     */       byte b; int i; Method[] arrayOfMethod;
/* 212 */       for (i = (arrayOfMethod = type.getMethods()).length, b = 0; b < i; ) { Method setter = arrayOfMethod[b];
/* 213 */         if (setter.getName().equals("set" + Character.toUpperCase(key.charAt(0)) + key.substring(1))) {
/*     */           
/* 215 */           Annotation[][] annotations = setter.getParameterAnnotations();
/* 216 */           if (annotations.length == 1) {
/* 217 */             ElementType innerMeta = null; byte b1; int j; Annotation[] arrayOfAnnotation;
/* 218 */             for (j = (arrayOfAnnotation = annotations[0]).length, b1 = 0; b1 < j; ) { Annotation a = arrayOfAnnotation[b1];
/* 219 */               if (a instanceof ElementType) {
/* 220 */                 innerMeta = (ElementType)a; break;
/*     */               } 
/*     */               b1++; }
/*     */             
/*     */             try {
/* 225 */               setter.invoke(instance, new Object[] { deserialize(kvPairs.get(key), 
/* 226 */                       setter.getParameterTypes()[0], innerMeta) }); break label43;
/* 227 */             } catch (Exception e) {
/* 228 */               throw new IllegalArgumentException(String.format(
/* 229 */                     "Cannot deserialize property '%s' in %s", new Object[] { key, type }), e);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         b++; }
/*     */       
/*     */       Field[] arrayOfField;
/* 236 */       i = (arrayOfField = type.getFields()).length; b = 0; label43: while (true) { if (b >= i)
/*     */         
/*     */         { 
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
/* 249 */           logger.warn("No field/setter found for property '" + key + "' in " + type); break; }  Field field = arrayOfField[b]; if (field.getName().equals(key)) { try { field.set(instance, deserialize(kvPairs.get(key), field.getType(), field.<ElementType>getAnnotation(ElementType.class))); } catch (Exception e) { throw new IllegalArgumentException(String.format("Cannot deserialize property '%s' in %s", new Object[] { key, type }), e); }  break; }  b++; }
/*     */     
/* 251 */     }  return instance;
/*     */   }
/*     */   
/*     */   private Object deserializeAsArray(List<Object> data, Class<?> type) throws Exception {
/* 255 */     List<Object> source = data;
/* 256 */     Object[] result = (Object[])Array.newInstance(type.getComponentType(), source.size());
/* 257 */     for (int i = 0; i < result.length; i++) {
/* 258 */       result[i] = deserialize(source.get(i), type.getComponentType(), null);
/*     */     }
/* 260 */     return result;
/*     */   }
/*     */   
/*     */   private Object deserializeAsSet(Object data, ElementType metadata) throws Exception {
/* 264 */     if (metadata == null) {
/* 265 */       logger.warn("Deserializing set without metadata. This may be due to forgotten annotation on a field or parameter or a return value may consist of nested collections. Returning raw set instance as best effort: " + 
/*     */ 
/*     */           
/* 268 */           data);
/* 269 */       return new HashSet((List)data);
/*     */     } 
/* 271 */     List<Object> source = (List<Object>)data;
/* 272 */     Set<Object> result = new HashSet(source.size());
/* 273 */     for (Object val : source) {
/* 274 */       result.add(deserialize(val, metadata.value(), null));
/*     */     }
/* 276 */     return result;
/*     */   }
/*     */   
/*     */   private Object deserializeAsList(Object data, ElementType metadata) throws Exception {
/* 280 */     if (metadata == null) {
/* 281 */       logger.warn("Deserializing list without metadata. This may be due to forgotten annotation on a field or parameter or a return value may consist of nested collections. Returning raw list instance as best effort: " + 
/*     */ 
/*     */           
/* 284 */           data);
/* 285 */       return data;
/*     */     } 
/* 287 */     List<Object> source = (List<Object>)data;
/* 288 */     List<Object> result = new ArrayList(source.size());
/* 289 */     for (Object val : source) {
/* 290 */       result.add(deserialize(val, metadata.value(), null));
/*     */     }
/* 292 */     return result;
/*     */   }
/*     */   
/*     */   private Object deserializeAsMap(Object data, ElementType metadata) throws Exception {
/* 296 */     if (metadata == null) {
/* 297 */       logger.warn("Deserializing map without metadata. This may be due to forgotten annotation on a field or parameter or a return value may consist of nested collections. Returning raw map instance as best effort: " + 
/*     */ 
/*     */           
/* 300 */           data);
/* 301 */       return data;
/*     */     } 
/* 303 */     boolean deserializeKey = (metadata.key() != void.class);
/* 304 */     Map<Object, Object> source = (Map<Object, Object>)data;
/* 305 */     Map<Object, Object> result = new HashMap<>();
/* 306 */     for (Object key : source.keySet()) {
/* 307 */       if (deserializeKey) {
/* 308 */         result.put(deserialize(key, metadata.key(), null), 
/* 309 */             deserialize(source.get(key), metadata.value(), null)); continue;
/*     */       } 
/* 311 */       result.put(key, deserialize(source.get(key), metadata.value(), null));
/*     */     } 
/*     */     
/* 314 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] deserializeMethodInput(List<Object> data, Method method) throws Exception {
/* 324 */     Class[] types = method.getParameterTypes();
/* 325 */     ElementType[] metadata = getElementMetadata(method);
/* 326 */     if (data.size() != types.length) {
/* 327 */       throw new IllegalStateException("Service method parameters count (" + 
/* 328 */           types.length + ") do not match provided input length (" + 
/* 329 */           data.size() + ")");
/*     */     }
/*     */     
/* 332 */     Object[] result = new Object[data.size()];
/* 333 */     for (int i = 0; i < data.size(); i++) {
/* 334 */       result[i] = deserialize(data.get(i), types[i], metadata[i]);
/*     */     }
/* 336 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object serialize(Object data) throws Exception {
/* 343 */     if (data == null) {
/* 344 */       return data;
/*     */     }
/*     */     
/* 347 */     if (data instanceof ManagedObjectReference) {
/* 348 */       ManagedObjectReference mor = (ManagedObjectReference)data;
/*     */ 
/*     */       
/* 351 */       Map<String, String> map = new HashMap<>();
/* 352 */       map.put("type", mor.getType());
/* 353 */       map.put("serverGuid", mor.getServerGuid());
/* 354 */       map.put("value", mor.getValue());
/*     */       
/* 356 */       return Collections.unmodifiableMap(map);
/*     */     } 
/*     */     
/* 359 */     if (data instanceof Enum) {
/* 360 */       return ((Enum)data).name();
/*     */     }
/*     */     
/* 363 */     if (data instanceof Date) {
/* 364 */       return Long.valueOf(((Date)data).getTime());
/*     */     }
/*     */     
/* 367 */     if (data instanceof Calendar) {
/* 368 */       return Long.valueOf(((Calendar)data).getTimeInMillis());
/*     */     }
/*     */     
/* 371 */     Object[] sourceArray = null;
/* 372 */     if (data.getClass().isArray()) {
/* 373 */       Class<?> componentType = data.getClass().getComponentType();
/* 374 */       if (componentType.isPrimitive()) {
/* 375 */         List<Object> dataList = null;
/* 376 */         if (boolean.class.isAssignableFrom(componentType)) {
/* 377 */           boolean[] dataArr = (boolean[])data;
/* 378 */           dataList = new ArrayList(dataArr.length); byte b; int i; boolean[] arrayOfBoolean1;
/* 379 */           for (i = (arrayOfBoolean1 = dataArr).length, b = 0; b < i; ) { boolean el = arrayOfBoolean1[b];
/* 380 */             dataList.add(Boolean.valueOf(el)); b++; }
/*     */         
/* 382 */         } else if (byte.class.isAssignableFrom(componentType)) {
/* 383 */           byte[] dataArr = (byte[])data;
/* 384 */           dataList = new ArrayList(dataArr.length); byte b; int i; byte[] arrayOfByte1;
/* 385 */           for (i = (arrayOfByte1 = dataArr).length, b = 0; b < i; ) { byte el = arrayOfByte1[b];
/* 386 */             dataList.add(Byte.valueOf(el)); b++; }
/*     */         
/* 388 */         } else if (char.class.isAssignableFrom(componentType)) {
/* 389 */           char[] dataArr = (char[])data;
/* 390 */           dataList = new ArrayList(dataArr.length); byte b; int i; char[] arrayOfChar1;
/* 391 */           for (i = (arrayOfChar1 = dataArr).length, b = 0; b < i; ) { char el = arrayOfChar1[b];
/* 392 */             dataList.add(Character.valueOf(el)); b++; }
/*     */         
/* 394 */         } else if (double.class.isAssignableFrom(componentType)) {
/* 395 */           double[] dataArr = (double[])data;
/* 396 */           dataList = new ArrayList(dataArr.length); byte b; int i; double[] arrayOfDouble1;
/* 397 */           for (i = (arrayOfDouble1 = dataArr).length, b = 0; b < i; ) { double el = arrayOfDouble1[b];
/* 398 */             dataList.add(Double.valueOf(el)); b++; }
/*     */         
/* 400 */         } else if (float.class.isAssignableFrom(componentType)) {
/* 401 */           float[] dataArr = (float[])data;
/* 402 */           dataList = new ArrayList(dataArr.length); byte b; int i; float[] arrayOfFloat1;
/* 403 */           for (i = (arrayOfFloat1 = dataArr).length, b = 0; b < i; ) { float el = arrayOfFloat1[b];
/* 404 */             dataList.add(Float.valueOf(el)); b++; }
/*     */         
/* 406 */         } else if (int.class.isAssignableFrom(componentType)) {
/* 407 */           int[] dataArr = (int[])data;
/* 408 */           dataList = new ArrayList(dataArr.length); byte b; int i, arrayOfInt1[];
/* 409 */           for (i = (arrayOfInt1 = dataArr).length, b = 0; b < i; ) { int el = arrayOfInt1[b];
/* 410 */             dataList.add(Integer.valueOf(el)); b++; }
/*     */         
/* 412 */         } else if (long.class.isAssignableFrom(componentType)) {
/* 413 */           long[] dataArr = (long[])data;
/* 414 */           dataList = new ArrayList(dataArr.length); byte b; int i; long[] arrayOfLong1;
/* 415 */           for (i = (arrayOfLong1 = dataArr).length, b = 0; b < i; ) { long el = arrayOfLong1[b];
/* 416 */             dataList.add(Long.valueOf(el)); b++; }
/*     */         
/* 418 */         } else if (short.class.isAssignableFrom(componentType)) {
/* 419 */           short[] dataArr = (short[])data;
/* 420 */           dataList = new ArrayList(dataArr.length); byte b; int i; short[] arrayOfShort1;
/* 421 */           for (i = (arrayOfShort1 = dataArr).length, b = 0; b < i; ) { short el = arrayOfShort1[b];
/* 422 */             dataList.add(Short.valueOf(el));
/*     */             b++; }
/*     */         
/*     */         } else {
/* 426 */           throw new IllegalArgumentException("Unknown primitive type?!?!?");
/*     */         } 
/*     */         
/* 429 */         sourceArray = dataList.toArray();
/*     */       } else {
/* 431 */         sourceArray = (Object[])data;
/*     */       } 
/* 433 */     } else if (data instanceof Collection) {
/* 434 */       sourceArray = ((Collection)data).toArray();
/*     */     } 
/*     */     
/* 437 */     if (sourceArray != null) {
/* 438 */       List<Object> list = new ArrayList(); byte b; int i; Object[] arrayOfObject;
/* 439 */       for (i = (arrayOfObject = sourceArray).length, b = 0; b < i; ) { Object o = arrayOfObject[b];
/* 440 */         list.add(serialize(o)); b++; }
/*     */       
/* 442 */       return list;
/*     */     } 
/*     */     
/* 445 */     if (data instanceof Map) {
/* 446 */       Map<Object, Object> sourceMap = (Map<Object, Object>)data;
/* 447 */       Map<Object, Object> resultMap = new HashMap<>();
/* 448 */       for (Object key : sourceMap.keySet()) {
/* 449 */         resultMap.put(serialize(key), serialize(sourceMap.get(key)));
/*     */       }
/* 451 */       return resultMap;
/*     */     } 
/*     */     
/* 454 */     if (data.getClass().getAnnotation(data.class) != null || 
/* 455 */       data.getClass().getAnnotation(data.class) != null) {
/* 456 */       Map<String, Object> serialized = new HashMap<>(); byte b; int i; Method[] arrayOfMethod;
/* 457 */       for (i = (arrayOfMethod = data.getClass().getMethods()).length, b = 0; b < i; ) { Method method = arrayOfMethod[b];
/* 458 */         String propertyName = getPropertyName(method);
/* 459 */         if (propertyName != null)
/* 460 */           serialized.put(propertyName, serialize(method.invoke(data, new Object[0])));  b++; }
/*     */       
/*     */       Field[] arrayOfField;
/* 463 */       for (i = (arrayOfField = data.getClass().getFields()).length, b = 0; b < i; ) { Field field = arrayOfField[b];
/* 464 */         String propertyName = getPropertyName(field);
/* 465 */         if (propertyName != null)
/* 466 */           serialized.put(propertyName, serialize(field.get(data))); 
/*     */         b++; }
/*     */       
/* 469 */       return serialized;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 474 */     return data;
/*     */   }
/*     */   
/*     */   private static String getPropertyName(Method method) throws NoSuchMethodException {
/* 478 */     String getterPrefix = null;
/* 479 */     if (method.getName().startsWith("get") && 
/* 480 */       method.getName().length() > "get".length()) {
/* 481 */       getterPrefix = "get";
/* 482 */     } else if (Boolean.class.isAssignableFrom(method.getReturnType()) && 
/* 483 */       method.getName().startsWith("is") && 
/* 484 */       method.getName().length() > "is".length()) {
/* 485 */       getterPrefix = "is";
/*     */     } 
/*     */     
/* 488 */     if (getterPrefix == null)
/*     */     {
/* 490 */       return null;
/*     */     }
/*     */     
/* 493 */     if ((method.getParameterTypes()).length != 0) {
/* 494 */       return null;
/*     */     }
/*     */     
/* 497 */     if ((method.getModifiers() & 0x8) != 0) {
/* 498 */       return null;
/*     */     }
/*     */     
/* 501 */     return String.valueOf(Character.toLowerCase(method.getName().charAt(getterPrefix.length()))) + 
/* 502 */       method.getName().substring(getterPrefix.length() + 1);
/*     */   }
/*     */   
/*     */   private static String getPropertyName(Field field) {
/* 506 */     if ((field.getModifiers() & 0x8) != 0) {
/* 507 */       return null;
/*     */     }
/* 509 */     return field.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ElementType[] getElementMetadata(Method method) {
/* 518 */     Annotation[][] parameterAnnotations = method.getParameterAnnotations();
/* 519 */     ElementType[] typedAnnotations = 
/* 520 */       new ElementType[parameterAnnotations.length];
/*     */ 
/*     */     
/* 523 */     for (int i = 0; i < typedAnnotations.length; i++) {
/*     */       byte b; int j;
/*     */       Annotation[] arrayOfAnnotation;
/* 526 */       for (j = (arrayOfAnnotation = parameterAnnotations[i]).length, b = 0; b < j; ) { Annotation a = arrayOfAnnotation[b];
/* 527 */         if (a instanceof ElementType) {
/* 528 */           typedAnnotations[i] = (ElementType)a; break;
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 533 */     return typedAnnotations;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/ProxygenSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */