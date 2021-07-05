/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.ext.InvocationContext;
/*    */ import com.vmware.vim.vmomi.client.ext.RequestContextProvider;
/*    */ import com.vmware.vim.vmomi.core.RequestContext;
/*    */ import com.vmware.vim.vmomi.core.impl.RequestContextImpl;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpRequestContextProvider
/*    */   implements RequestContextProvider
/*    */ {
/*    */   private final Map<String, Object> requestProperties;
/*    */   
/*    */   public HttpRequestContextProvider(Map<String, Object> requestProperties) {
/* 19 */     this.requestProperties = requestProperties;
/*    */   }
/*    */ 
/*    */   
/*    */   public RequestContext getRequestContext(InvocationContext invocationContext) {
/* 24 */     RequestContextImpl result = new RequestContextImpl();
/* 25 */     result.putAll(this.requestProperties);
/* 26 */     return (RequestContext)result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/HttpRequestContextProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */