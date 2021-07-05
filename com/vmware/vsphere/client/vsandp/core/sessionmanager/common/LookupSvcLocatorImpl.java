/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import com.vmware.af.VmAfClient;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.net.URI;
/*    */ import java.security.Key;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.cert.CertificateException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LookupSvcLocatorImpl
/*    */   implements LookupSvcLocator
/*    */ {
/* 22 */   private static final Log logger = LogFactory.getLog(LookupSvcLocatorImpl.class);
/*    */   
/*    */   private VmAfClient afClient;
/*    */   
/*    */   private KeyStore vecsKeystore;
/*    */   
/*    */   public LookupSvcInfo getInfo() {
/*    */     try {
/* 30 */       KeyStore keyStore = getKeyStore();
/* 31 */       URI address = URI.create(getAfClient().getLSLocation());
/* 32 */       return new LookupSvcInfo(address, keyStore);
/* 33 */     } catch (Exception e) {
/* 34 */       throw new IllegalStateException("Could not find LS info from AFD/VECS", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public PrivateKey getPrivateKey() {
/*    */     try {
/* 41 */       KeyStore keyStore = KeyStore.getInstance("VKS");
/* 42 */       keyStore.load(getLoader("vsphere-webclient"));
/* 43 */       Key key = keyStore.getKey("vsphere-webclient", null);
/* 44 */       if (key instanceof PrivateKey) {
/* 45 */         return (PrivateKey)key;
/*    */       }
/* 47 */     } catch (Exception e) {
/* 48 */       throw new IllegalStateException("Failed to acquire private key.", e);
/*    */     } 
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   private VmAfClient getAfClient() {
/* 54 */     if (this.afClient == null) {
/* 55 */       this.afClient = new VmAfClient("localhost");
/*    */     }
/* 57 */     return this.afClient;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private KeyStore getKeyStore() throws KeyStoreException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CertificateException, NoSuchAlgorithmException, IOException {
/* 63 */     if (this.vecsKeystore == null) {
/* 64 */       this.vecsKeystore = KeyStore.getInstance("VKS");
/* 65 */       this.vecsKeystore.load(getLoader("TRUSTED_ROOTS"));
/* 66 */       logger.info("VECS keystore loaded: " + this.vecsKeystore);
/*    */     } 
/* 68 */     return this.vecsKeystore;
/*    */   }
/*    */ 
/*    */   
/*    */   private KeyStore.LoadStoreParameter getLoader(String alias) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 73 */     Class<? extends KeyStore.LoadStoreParameter> loaderClass = 
/* 74 */       (Class)Class.forName("com.vmware.provider.VecsLoadStoreParameter");
/* 75 */     KeyStore.LoadStoreParameter loader = loaderClass.getConstructor(new Class[] { String.class
/* 76 */         }).newInstance(new Object[] { alias });
/* 77 */     return loader;
/*    */   }
/*    */ 
/*    */   
/*    */   public KeyStore getH5Keystore() {
/*    */     try {
/* 83 */       KeyStore keyStore = KeyStore.getInstance("VKS");
/* 84 */       keyStore.load(getLoader("vsphere-webclient"));
/* 85 */       return keyStore;
/* 86 */     } catch (Exception e) {
/* 87 */       throw new IllegalStateException("Failed to acquire private key.", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/LookupSvcLocatorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */