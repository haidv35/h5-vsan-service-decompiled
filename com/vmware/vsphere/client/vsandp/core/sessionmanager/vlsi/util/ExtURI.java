/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.util;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtURI
/*    */ {
/*    */   protected URI uri;
/*    */   protected URI proxy;
/*    */   
/*    */   public ExtURI(String uri) throws URISyntaxException {
/* 29 */     int pos = uri.indexOf('|');
/* 30 */     if (pos >= 0) {
/* 31 */       this.uri = new URI(uri.substring(pos + 1));
/*    */       
/* 33 */       String proxyUri = uri.substring(0, pos).trim();
/* 34 */       if (!proxyUri.isEmpty()) {
/* 35 */         this.proxy = new URI(proxyUri);
/*    */       }
/*    */     } else {
/* 38 */       this.uri = new URI(uri);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URI getUri() {
/* 46 */     return this.uri;
/*    */   }
/*    */   
/*    */   public void setUri(URI uri) {
/* 50 */     this.uri = uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URI getProxy() {
/* 60 */     return this.proxy;
/*    */   }
/*    */   
/*    */   public void setProxy(URI proxy) {
/* 64 */     this.proxy = proxy;
/*    */   }
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
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     String proxyUrl = "";
/*    */     
/* 84 */     if (this.proxy != null) {
/* 85 */       proxyUrl = this.proxy.toString();
/* 86 */       if (!proxyUrl.endsWith("/")) {
/* 87 */         proxyUrl = String.valueOf(proxyUrl) + "/";
/*    */       }
/*    */       
/* 90 */       proxyUrl = String.valueOf(proxyUrl) + "|";
/*    */     } 
/*    */     
/* 93 */     return String.valueOf(proxyUrl) + this.uri.toString();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/util/ExtURI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */