/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.vmware.vim.binding.pbm.version.version11;
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import com.vmware.vise.usersession.ServerInfo;
/*    */ import com.vmware.vise.usersession.UserSessionService;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.HttpSettings;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.pbm.PbmConnection;
/*    */ import java.net.URI;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Qualifier;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component
/*    */ public class PbmClient
/*    */ {
/*    */   private static final String PBM_ENDPOINT_PATH = "/pbm/sdk";
/*    */   @Autowired
/*    */   @Qualifier("pbmFactory")
/*    */   private ResourceFactory<PbmConnection, VlsiSettings> producerFactory;
/*    */   @Autowired
/*    */   @Qualifier("vlsiSettingsTemplate")
/*    */   private VlsiSettings vlsiSettingsTemplate;
/*    */   @Autowired
/*    */   private UserSessionService sessionService;
/*    */   
/*    */   public PbmConnection getConnection(String uuid) {
/*    */     try {
/* 51 */       ServerInfo serverInfo = findServerInfo(uuid);
/*    */       
/* 53 */       URI vcEndpoint = URI.create(serverInfo.serviceUrl);
/* 54 */       URI pbmEndpoint = new URI(
/* 55 */           vcEndpoint.getScheme(), null, vcEndpoint.getHost(), vcEndpoint.getPort(), 
/* 56 */           "/pbm/sdk", null, null);
/*    */       
/* 58 */       HttpSettings httpSettings = this.vlsiSettingsTemplate.getHttpSettings().setRequestProperties(
/* 59 */           (Map)ImmutableMap.of("vcSessionCookie", serverInfo.sessionCookie));
/*    */       
/* 61 */       VlsiSettings vlsiSettings = this.vlsiSettingsTemplate
/* 62 */         .setHttpSettings(httpSettings)
/* 63 */         .setServiceInfo(pbmEndpoint, version11.class)
/* 64 */         .setSslContext(null, (ThumbprintVerifier)new SingleThumbprintVerifier(serverInfo.thumbprint));
/*    */       
/* 66 */       return (PbmConnection)this.producerFactory.acquire(vlsiSettings);
/* 67 */     } catch (Exception e) {
/* 68 */       throw new NotAccessibleException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private ServerInfo findServerInfo(String vcUuid) {
/* 73 */     if (vcUuid == null)
/* 74 */       throw new IllegalArgumentException(
/* 75 */           "vcUuid cannot be null, probably coming from MOR without serverGuid.");  byte b; int i;
/*    */     ServerInfo[] arrayOfServerInfo;
/* 77 */     for (i = (arrayOfServerInfo = (this.sessionService.getUserSession()).serversInfo).length, b = 0; b < i; ) { ServerInfo vcServer = arrayOfServerInfo[b];
/* 78 */       if (vcUuid.equalsIgnoreCase(vcServer.serviceGuid))
/* 79 */         return vcServer; 
/*    */       b++; }
/*    */     
/* 82 */     throw new IllegalStateException("No session cookie for: " + vcUuid);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/PbmClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */