/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.CachedResourceFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachedHttpFactory
/*    */   extends CachedResourceFactory<ClientCfg, HttpSettings>
/*    */ {
/*    */   public CachedHttpFactory() {
/* 14 */     super(new HttpFactory());
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/CachedHttpFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */