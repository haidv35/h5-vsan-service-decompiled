/*     */ package com.vmware.vsphere.client.vsan.util;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.vsan.host.DiskResult;
/*     */ import com.vmware.vim.binding.vmodl.MethodFault;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.ObjectMapper;
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
/*     */ public class Utils
/*     */ {
/*     */   private static MessageBundle MESSAGE_BUNDLE;
/*     */   
/*     */   public static void setMessageBundle(MessageBundle messageBundle) {
/*  26 */     MESSAGE_BUNDLE = messageBundle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDiskEligible(DiskResult result) {
/*  33 */     DiskResult.State diskState = Enum.<DiskResult.State>valueOf(DiskResult.State.class, result.state);
/*     */     
/*  35 */     if (diskState == DiskResult.State.eligible) {
/*  36 */       return true;
/*     */     }
/*     */     
/*  39 */     return false;
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
/*     */   public static String getLocalizedString(String key) {
/*  52 */     return MESSAGE_BUNDLE.string(key);
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
/*     */   public static String getLocalizedString(String key, String... params) {
/*  67 */     return MESSAGE_BUNDLE.string(key, params);
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
/*     */   public static MethodFault getMethodFault(Throwable e) {
/*  87 */     if (e == null) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     if (e instanceof MethodFault) {
/*  92 */       return (MethodFault)e;
/*     */     }
/*     */     
/*  95 */     MethodFault methodFault = new MethodFault();
/*  96 */     methodFault.setMessage(e.getMessage());
/*  97 */     methodFault.initCause(e);
/*     */ 
/*     */     
/* 100 */     if (e instanceof com.vmware.vim.binding.vmodl.RuntimeFault)
/*     */     {
/*     */ 
/*     */       
/* 104 */       methodFault.setFaultCause((Exception)e);
/*     */     }
/*     */     
/* 107 */     return methodFault;
/*     */   }
/*     */   
/*     */   public static <T> List<T> arrayToList(Object... array) {
/* 111 */     if (array != null) {
/* 112 */       return Arrays.asList((T[])array);
/*     */     }
/* 114 */     return Collections.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonNode getJsonRootNode(String jsonStr) {
/* 119 */     if (StringUtils.isEmpty(jsonStr)) {
/* 120 */       return null;
/*     */     }
/* 122 */     ObjectMapper mapper = new ObjectMapper();
/*     */     
/* 124 */     JsonNode rootNode = null;
/*     */     try {
/* 126 */       rootNode = mapper.readTree(jsonStr);
/* 127 */     } catch (Exception exception) {}
/* 128 */     return rootNode;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */