/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vise.usersession.ServerInfo;
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.Authenticator;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp.DpConnection;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp.DpExploratorySettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp.DpTokenAuthenticator;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.SsoAdminFactory;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class DpClient
/*     */ {
/*     */   @Autowired
/*     */   @Qualifier("dpFactory")
/*     */   private ResourceFactory<DpConnection, DpExploratorySettings> producerFactory;
/*     */   @Autowired
/*     */   @Qualifier("vlsiSettingsTemplate")
/*     */   private VlsiSettings vlsiSettingsTemplate;
/*     */   @Autowired
/*     */   private UserSessionService sessionService;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   @Autowired
/*     */   private LookupSvcLocator lsLocator;
/*     */   @Autowired
/*     */   private SsoAdminFactory ssoAdminFactory;
/*     */   @Autowired
/*     */   private LookupSvcClient lsClient;
/*     */   
/*     */   public DpConnection getConnection(ManagedObjectReference clusterRef) {
/*  65 */     if (!this.vmodlHelper.isOfType(clusterRef, ClusterComputeResource.class)) {
/*  66 */       throw new IllegalArgumentException("MoRef must be of cluster, " + clusterRef);
/*     */     }
/*     */     try {
/*  69 */       ServerInfo serverInfo = findServerInfo(clusterRef.getServerGuid());
/*  70 */       URL vcUrl = new URL(serverInfo.serviceUrl);
/*     */       
/*  72 */       URL vsanDpUrl = new URL(
/*  73 */           vcUrl.getProtocol(), 
/*  74 */           vcUrl.getHost(), 
/*  75 */           vcUrl.getPort(), 
/*  76 */           "/vsandp");
/*     */       
/*  78 */       DpTokenAuthenticator tokenAuth = new DpTokenAuthenticator(
/*  79 */           this.sessionService, 
/*  80 */           this.lsLocator, 
/*  81 */           this.ssoAdminFactory, 
/*  82 */           this.lsClient.getProducerFactory(), 
/*  83 */           this.lsClient.getSettings(this.lsLocator.getInfo()));
/*     */       
/*  85 */       VlsiSettings vlsiSettings = this.vlsiSettingsTemplate
/*  86 */         .setServiceInfo(vsanDpUrl.toURI(), void.class)
/*  87 */         .setSslContext(null, (ThumbprintVerifier)new SingleThumbprintVerifier(serverInfo.thumbprint))
/*  88 */         .setAuthenticator((Authenticator)tokenAuth);
/*     */       
/*  90 */       return (DpConnection)this.producerFactory.acquire(new DpExploratorySettings(clusterRef, this.vmodlHelper, vlsiSettings));
/*  91 */     } catch (MalformedURLException|java.net.URISyntaxException e) {
/*  92 */       throw new IllegalStateException("Illegal service address", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ServerInfo findServerInfo(String vcUuid) {
/*  97 */     if (vcUuid == null)
/*  98 */       throw new IllegalArgumentException(
/*  99 */           "vcUuid cannot be null, probably coming from MOR without serverGuid.");  byte b; int i;
/*     */     ServerInfo[] arrayOfServerInfo;
/* 101 */     for (i = (arrayOfServerInfo = (this.sessionService.getUserSession()).serversInfo).length, b = 0; b < i; ) { ServerInfo vcServer = arrayOfServerInfo[b];
/* 102 */       if (vcUuid.equalsIgnoreCase(vcServer.serviceGuid))
/* 103 */         return vcServer; 
/*     */       b++; }
/*     */     
/* 106 */     throw new IllegalStateException("No session cookie for: " + vcUuid);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/DpClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */