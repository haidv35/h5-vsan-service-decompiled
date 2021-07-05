/*     */ package com.vmware.vsan.client.services;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.vmware.vim.binding.vmodl.LocalizableMessage;
/*     */ import com.vmware.vim.binding.vmodl.MethodFault;
/*     */ import com.vmware.vim.binding.vmodl.RuntimeFault;
/*     */ import com.vmware.vise.data.query.ObjectReferenceService;
/*     */ import com.vmware.vsphere.client.vsan.util.MessageBundle;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Controller;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Controller
/*     */ @RequestMapping({"/proxy"})
/*     */ public class ProxygenController
/*     */   extends RestControllerBase
/*     */ {
/*     */   private static class MutationOperation
/*     */   {
/*     */     static final String VALIDATE = "validate";
/*     */     static final String APPLY = "apply";
/*     */   }
/*  43 */   private static final Logger logger = LoggerFactory.getLogger(ProxygenController.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ObjectReferenceService objRefService;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private MessageBundle messages;
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/service/{beanIdOrClassName}/{methodName}"}, method = {RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public Object invokeService(@PathVariable("beanIdOrClassName") String beanIdOrClassName, @PathVariable("methodName") String methodName, @RequestBody Map<String, Object> body) throws Exception {
/*     */     try {
/*  65 */       Object bean = null;
/*     */       try {
/*  67 */         bean = this.beanFactory.getBean(beanIdOrClassName);
/*  68 */       } catch (BeansException beansException) {
/*  69 */         bean = this.beanFactory.getBean(Class.forName(beanIdOrClassName));
/*     */       }  byte b; int i;
/*     */       Method[] arrayOfMethod;
/*  72 */       for (i = (arrayOfMethod = bean.getClass().getMethods()).length, b = 0; b < i; ) { Method method = arrayOfMethod[b];
/*  73 */         if (!method.getName().equals(methodName)) {
/*     */           b++;
/*     */           continue;
/*     */         } 
/*  77 */         ProxygenSerializer serializer = new ProxygenSerializer();
/*     */ 
/*     */         
/*  80 */         List<Object> rawData = (List<Object>)body.get("methodInput");
/*  81 */         Object[] methodInput = serializer.deserializeMethodInput(rawData, method);
/*     */ 
/*     */         
/*  84 */         Object result = method.invoke(bean, methodInput);
/*     */ 
/*     */         
/*  87 */         Map<String, Object> map = new HashMap<>();
/*  88 */         map.put("result", serializer.serialize(result));
/*  89 */         return map; }
/*     */ 
/*     */     
/*  92 */     } catch (Exception e) {
/*  93 */       logger.error("service method failed to invoke", e);
/*  94 */       return handleException(e);
/*     */     } 
/*     */     
/*  97 */     logger.error("service method not found: " + methodName + " @ " + beanIdOrClassName);
/*  98 */     return handleException(null);
/*     */   }
/*     */   
/*     */   private Object handleException(Throwable t) {
/* 102 */     if (t instanceof InvocationTargetException) {
/* 103 */       return handleException(((InvocationTargetException)t).getTargetException());
/*     */     }
/*     */     
/* 106 */     if (t instanceof java.util.concurrent.ExecutionException && t.getCause() != t) {
/* 107 */       return handleException(t.getCause());
/*     */     }
/*     */     
/* 110 */     if (t instanceof com.vmware.vise.data.query.DataException && t.getCause() != t) {
/* 111 */       return handleException(t.getCause());
/*     */     }
/*     */     
/* 114 */     if (t instanceof com.vmware.vim.vmomi.client.common.UnexpectedStatusCodeException) {
/* 115 */       return ImmutableMap.of("error", this.messages.string("util.dataservice.notRespondingFault"));
/*     */     }
/*     */     
/* 118 */     LocalizableMessage[] faultMessage = null;
/* 119 */     if (t instanceof MethodFault) {
/* 120 */       faultMessage = ((MethodFault)t).getFaultMessage();
/* 121 */     } else if (t instanceof RuntimeFault) {
/* 122 */       faultMessage = ((RuntimeFault)t).getFaultMessage();
/*     */     } 
/*     */     
/* 125 */     if (faultMessage != null) {
/* 126 */       byte b; int i; LocalizableMessage[] arrayOfLocalizableMessage; for (i = (arrayOfLocalizableMessage = faultMessage).length, b = 0; b < i; ) { LocalizableMessage localizable = arrayOfLocalizableMessage[b];
/* 127 */         if (localizable.getMessage() != null && !localizable.getMessage().isEmpty()) {
/* 128 */           return ImmutableMap.of("error", localizeFault(localizable.getMessage()));
/*     */         }
/* 130 */         if (localizable.getKey() != null && !localizable.getKey().isEmpty()) {
/* 131 */           return ImmutableMap.of("error", localizeFault(localizable.getKey()));
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/* 136 */     if (t != null && t.getMessage() != null && !t.getMessage().isEmpty()) {
/* 137 */       return ImmutableMap.of("error", t.getMessage());
/*     */     }
/*     */     
/* 140 */     return ImmutableMap.of("error", this.messages.string("util.dataservice.dataExtractionFault"));
/*     */   }
/*     */ 
/*     */   
/*     */   private String localizeFault(String key) {
/* 145 */     return key;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/ProxygenController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */