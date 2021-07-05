/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Arrays;
/*     */ import java.util.Properties;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class FallbackLookupSvcLocator
/*     */   implements LookupSvcLocator
/*     */ {
/*  29 */   private static final String[] WEBCLIENT_PROPS = new String[] {
/*  30 */       "/etc/vmware/vsphere-client/vsphere-client/webclient.properties", 
/*  31 */       "/var/lib/vmware/vsphere-client/vsphere-client/webclient.properties", 
/*  32 */       "C:\\ProgramData\\VMware\\vCenterServer\\cfg\\vsphere-client\\webclient.properties"
/*     */     };
/*     */   
/*     */   private static final String CM_ADDRESS_PROPERTY = "cm.url";
/*     */   
/*     */   private static final String KEYSTORE_PATH = "keystore.jks.path";
/*     */   private static final String KEYSTORE_PASSWORD = "keystore.jks.password";
/*  39 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*     */   private String cmAddress;
/*     */   private String cmThumbprint;
/*     */   
/*     */   private File getWebclientPropsFile() {
/*     */     byte b;
/*     */     int i;
/*     */     String[] arrayOfString;
/*  47 */     for (i = (arrayOfString = WEBCLIENT_PROPS).length, b = 0; b < i; ) { String potentionPath = arrayOfString[b];
/*  48 */       File file = new File(potentionPath);
/*  49 */       if (file.isFile())
/*  50 */         return file; 
/*     */       b++; }
/*     */     
/*  53 */     throw new IllegalStateException("webclient.properties not found: " + Arrays.toString(WEBCLIENT_PROPS));
/*     */   }
/*     */   private KeyStore cmKeystore; private String keystorePass;
/*     */   private void retrieveCmProperties() throws Exception {
/*  57 */     File propertiesFile = getWebclientPropsFile();
/*  58 */     this.logger.debug("Client properties file is '{}'.", propertiesFile.getAbsolutePath());
/*     */     
/*  60 */     Properties properties = new Properties(); try {
/*  61 */       Exception exception2, exception1 = null;
/*     */     
/*     */     }
/*  64 */     catch (IOException e) {
/*  65 */       throw new IllegalStateException("Failed to read properties: " + propertiesFile, e);
/*     */     } 
/*     */     
/*  68 */     String cmAddress = properties.getProperty("cm.url");
/*  69 */     if (cmAddress == null) {
/*  70 */       throw new IllegalStateException("Property 'cm.url' is missing from the local client configuration.");
/*     */     }
/*     */     
/*  73 */     this.logger.debug("Configured CM address is: {}", cmAddress);
/*     */     
/*  75 */     this.cmThumbprint = CertificateUtils.getServerThumbprint(cmAddress);
/*  76 */     this.logger.debug("Configured CM thumbprint is: {}", this.cmThumbprint);
/*     */     
/*  78 */     Object keystorePath = properties.get("keystore.jks.path");
/*  79 */     Object keystorePass = properties.get("keystore.jks.password");
/*     */     
/*  81 */     if (keystorePath != null) {
/*  82 */       this.cmKeystore = (new ClientCertificate(
/*  83 */           keystorePath.toString(), keystorePass.toString(), "", "JKS", "")).getKeystore();
/*     */     }
/*     */     
/*  86 */     this.cmAddress = cmAddress;
/*  87 */     this.keystorePass = keystorePass.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public LookupSvcInfo getInfo() {
/*     */     try {
/*  93 */       if (this.cmAddress == null) {
/*  94 */         retrieveCmProperties();
/*     */       }
/*  96 */       URI cmAddress = new URI(this.cmAddress);
/*  97 */       URI lsAddress = new URI("https", cmAddress.getHost(), 
/*  98 */           "/lookupservice/sdk", null);
/*  99 */       LookupSvcInfo result = (new LookupSvcInfo(lsAddress, this.cmThumbprint))
/* 100 */         .copyWithKeyStore(this.cmKeystore);
/* 101 */       this.logger.trace("Current LS is: {}", result);
/* 102 */       return result;
/* 103 */     } catch (Exception e) {
/* 104 */       throw new IllegalStateException("Failed to retrieve current LookupSvcInfo.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PrivateKey getPrivateKey() {
/*     */     try {
/* 111 */       return (PrivateKey)getInfo().getKeyStore().getKey("vsphere-webclient", this.keystorePass.toCharArray());
/* 112 */     } catch (Exception e) {
/* 113 */       throw new IllegalStateException("Failed to extract private key from JKS.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyStore getH5Keystore() {
/* 119 */     return getInfo().getKeyStore();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/FallbackLookupSvcLocator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */