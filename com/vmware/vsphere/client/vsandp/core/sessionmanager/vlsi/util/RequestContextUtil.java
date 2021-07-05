/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util;
/*    */ 
/*    */ import com.vmware.vim.sso.client.SamlToken;
/*    */ import com.vmware.vim.vmomi.core.RequestContext;
/*    */ import com.vmware.vim.vmomi.core.Stub;
/*    */ import com.vmware.vim.vmomi.core.impl.RequestContextImpl;
/*    */ import com.vmware.vim.vmomi.core.security.SignInfo;
/*    */ import com.vmware.vim.vmomi.core.security.impl.SignInfoImpl;
/*    */ import java.security.PrivateKey;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestContextUtil
/*    */ {
/* 28 */   private static Logger log = LoggerFactory.getLogger(RequestContextUtil.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends com.vmware.vim.binding.vmodl.ManagedObject> T withOperationId(T t) {
/*    */     RequestContextImpl requestContextImpl;
/* 35 */     String opId = MDC.get("operationID");
/* 36 */     if (opId == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 42 */       return t;
/*    */     }
/*    */     
/* 45 */     RequestContext requestContext = ((Stub)t)._getRequestContext();
/* 46 */     if (requestContext == null) {
/* 47 */       requestContextImpl = new RequestContextImpl();
/*    */     }
/*    */     
/* 50 */     requestContextImpl.put("operationID", opId);
/* 51 */     ((Stub)t)._setRequestContext((RequestContext)requestContextImpl);
/*    */     
/* 53 */     return t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends com.vmware.vim.binding.vmodl.ManagedObject> T withSignInfo(T t, PrivateKey privateKey, SamlToken token) {
/*    */     RequestContextImpl requestContextImpl;
/* 62 */     RequestContext requestContext = ((Stub)t)._getRequestContext();
/* 63 */     if (requestContext == null) {
/* 64 */       requestContextImpl = new RequestContextImpl();
/*    */     }
/*    */     
/* 67 */     requestContextImpl
/* 68 */       .setSignInfo((SignInfo)new SignInfoImpl(privateKey, token));
/*    */     
/* 70 */     ((Stub)t)._setRequestContext((RequestContext)requestContextImpl);
/*    */     
/* 72 */     return t;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/util/RequestContextUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */