/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import com.vmware.vise.usersession.ServerInfo;
/*    */ import com.vmware.vise.usersession.UserSessionService;
/*    */ import com.vmware.vsan.client.util.VmodlHelper;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*    */ import java.net.URI;
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
/*    */ 
/*    */ @Component
/*    */ public class VcClient
/*    */ {
/*    */   @Autowired
/*    */   @Qualifier("vcFactory")
/*    */   private ResourceFactory<VcConnection, VlsiSettings> producerFactory;
/*    */   @Autowired
/*    */   @Qualifier("vlsiSettingsTemplate")
/*    */   private VlsiSettings vlsiSettingsTemplate;
/*    */   @Autowired
/*    */   private UserSessionService sessionService;
/*    */   @Autowired
/*    */   private VmodlHelper _vmodlHelper;
/*    */   
/*    */   public VcConnection getConnection(String uuid) {
/* 49 */     ServerInfo serverInfo = findServerInfo(uuid);
/*    */ 
/*    */     
/* 52 */     Class<?> version = this._vmodlHelper.getVimVmodlVersion(uuid);
/*    */     
/* 54 */     VlsiSettings vlsiSettings = this.vlsiSettingsTemplate
/* 55 */       .setServiceInfo(URI.create(serverInfo.serviceUrl), version)
/* 56 */       .setSslContext(null, (ThumbprintVerifier)new SingleThumbprintVerifier(serverInfo.thumbprint))
/* 57 */       .setSessionCookie(serverInfo.sessionCookie);
/*    */     
/* 59 */     return (VcConnection)this.producerFactory.acquire(vlsiSettings);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VcConnection getVsanVmodlVersionConnection(String uuid) {
/* 66 */     ServerInfo serverInfo = findServerInfo(uuid);
/*    */     
/*    */     try {
/* 69 */       Class<?> version = this._vmodlHelper.getVmodlVersion(uuid, "/vsanServiceVersions.xml");
/* 70 */       VlsiSettings vlsiSettings = this.vlsiSettingsTemplate
/* 71 */         .setServiceInfo(URI.create(serverInfo.serviceUrl), version)
/* 72 */         .setSslContext(null, (ThumbprintVerifier)new SingleThumbprintVerifier(serverInfo.thumbprint))
/* 73 */         .setSessionCookie(serverInfo.sessionCookie);
/*    */       
/* 75 */       return (VcConnection)this.producerFactory.acquire(vlsiSettings);
/* 76 */     } catch (Exception exception) {
/* 77 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private ServerInfo findServerInfo(String vcUuid) {
/* 82 */     if (vcUuid == null)
/* 83 */       throw new IllegalArgumentException(
/* 84 */           "vcUuid cannot be null, probably coming from MOR without serverGuid.");  byte b; int i;
/*    */     ServerInfo[] arrayOfServerInfo;
/* 86 */     for (i = (arrayOfServerInfo = (this.sessionService.getUserSession()).serversInfo).length, b = 0; b < i; ) { ServerInfo vcServer = arrayOfServerInfo[b];
/* 87 */       if (vcUuid.equalsIgnoreCase(vcServer.serviceGuid))
/* 88 */         return vcServer; 
/*    */       b++; }
/*    */     
/* 91 */     throw new IllegalStateException("No session cookie for: " + vcUuid);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/VcClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */