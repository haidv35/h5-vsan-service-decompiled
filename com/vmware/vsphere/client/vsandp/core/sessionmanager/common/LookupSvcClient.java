/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.vmware.vim.binding.lookup.version.internal.version2;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.LookupSvcConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class LookupSvcClient
/*     */ {
/*  27 */   private static final Logger logger = LoggerFactory.getLogger(LookupSvcClient.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LS_URI_DEFAULT_PATH = "/lookupservice/sdk";
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   @Qualifier("vlsiSettingsTemplate")
/*     */   private VlsiSettings vlsiSettingsTemplate;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   @Qualifier("lsFactory")
/*     */   private ResourceFactory<LookupSvcConnection, VlsiSettings> producerFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private LookupSvcLocator lsLocator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupSvcConnection getConnection() {
/*  54 */     return getConnection(this.lsLocator.getInfo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupSvcConnection getConnection(LookupSvcInfo lsInfo) {
/*  61 */     long ts = System.currentTimeMillis();
/*     */     try {
/*  63 */       LookupSvcConnection connection = (LookupSvcConnection)this.producerFactory.acquire(getSettings(lsInfo));
/*  64 */       logger.debug("Connection acquired: {} ({} ms)", connection, Long.valueOf(System.currentTimeMillis() - ts));
/*  65 */       return connection;
/*  66 */     } catch (Exception e) {
/*  67 */       throw new NotAccessibleException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupSvcInfo getLocalLsInfo() {
/*  77 */     return this.lsLocator.getInfo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VlsiSettings getSettings(LookupSvcInfo lsInfo) {
/*  85 */     if (lsInfo == null) {
/*  86 */       lsInfo = getLocalLsInfo();
/*     */     }
/*  88 */     ThumbprintVerifier thumbprintVerifier = lsInfo.getThumbprintVerifier();
/*  89 */     ClientCertificate trustStore = null;
/*  90 */     if (lsInfo.getKeyStore() != null) {
/*  91 */       trustStore = new ClientCertificate(
/*  92 */           lsInfo.getAddress().getHost(), lsInfo.getKeyStore(), "", "", lsInfo.getAddress().getHost());
/*     */     }
/*  94 */     VlsiSettings settings = this.vlsiSettingsTemplate
/*  95 */       .setServiceInfo(lsInfo.getAddress(), version2.class)
/*  96 */       .setSslContext(trustStore, thumbprintVerifier);
/*  97 */     return settings;
/*     */   }
/*     */   
/*     */   public ResourceFactory<LookupSvcConnection, VlsiSettings> getProducerFactory() {
/* 101 */     return this.producerFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI createServiceUri(String address) throws URISyntaxException {
/*     */     URI icAddress;
/* 112 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(address), "Address cannot be null.");
/*     */     
/*     */     try {
/* 115 */       icAddress = new URI("https", address, "/lookupservice/sdk", null);
/* 116 */     } catch (URISyntaxException uRISyntaxException) {
/* 117 */       icAddress = new URI(address);
/*     */     } 
/* 119 */     return icAddress;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/LookupSvcClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */