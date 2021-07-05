/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;
/*    */ 
/*    */ import java.security.KeyStore;
/*    */ import java.security.PrivateKey;
/*    */ import java.util.Arrays;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class DelegatingLookupSvcLocator
/*    */   implements LookupSvcLocator
/*    */ {
/* 23 */   private static final Log logger = LogFactory.getLog(DelegatingLookupSvcLocator.class);
/*    */   
/*    */   private final LookupSvcLocator[] availableLocators;
/*    */   
/*    */   public DelegatingLookupSvcLocator(LookupSvcLocator[] availableLocators) {
/* 28 */     this.availableLocators = availableLocators;
/*    */   }
/*    */ 
/*    */   
/*    */   public LookupSvcInfo getInfo() {
/* 33 */     LookupSvcInfo result = null; byte b; int i;
/*    */     LookupSvcLocator[] arrayOfLookupSvcLocator;
/* 35 */     for (i = (arrayOfLookupSvcLocator = this.availableLocators).length, b = 0; b < i; ) { LookupSvcLocator locator = arrayOfLookupSvcLocator[b];
/* 36 */       logger.trace("Trying to obtain LookupSvcInfo from: " + locator);
/*    */       try {
/* 38 */         result = locator.getInfo();
/* 39 */         logger.trace("Successfully obtained LookupSvcInfo from " + locator + " => " + result);
/*    */         break;
/* 41 */       } catch (Throwable t) {
/* 42 */         logger.trace("Failed to obtain LookupSvcInfo from " + locator, t);
/*    */       } 
/*    */       b++; }
/*    */     
/* 46 */     if (result != null) {
/* 47 */       return result;
/*    */     }
/* 49 */     logger.error("Could not obtain LookupSvcInfo, all locators failed: " + Arrays.toString((Object[])this.availableLocators));
/* 50 */     throw new IllegalStateException("Unable to obtain LookupSvcInfo from any locator.");
/*    */   }
/*    */   public KeyStore getH5Keystore() {
/*    */     byte b;
/*    */     int i;
/*    */     LookupSvcLocator[] arrayOfLookupSvcLocator;
/* 56 */     for (i = (arrayOfLookupSvcLocator = this.availableLocators).length, b = 0; b < i; ) { LookupSvcLocator locator = arrayOfLookupSvcLocator[b];
/*    */       try {
/* 58 */         return locator.getH5Keystore();
/* 59 */       } catch (Throwable t) {
/* 60 */         logger.trace("Failed to obtain H5 keystore from " + locator, t);
/*    */       }  b++; }
/*    */     
/* 63 */     throw new IllegalStateException("Unable to obtain H5 keystore from any locator.");
/*    */   } public PrivateKey getPrivateKey() {
/*    */     byte b;
/*    */     int i;
/*    */     LookupSvcLocator[] arrayOfLookupSvcLocator;
/* 68 */     for (i = (arrayOfLookupSvcLocator = this.availableLocators).length, b = 0; b < i; ) { LookupSvcLocator locator = arrayOfLookupSvcLocator[b];
/*    */       try {
/* 70 */         PrivateKey key = locator.getPrivateKey();
/* 71 */         if (key != null) {
/* 72 */           return key;
/*    */         }
/* 74 */       } catch (Throwable t) {
/* 75 */         logger.trace("Failed to obtain private key from " + locator, t);
/*    */       }  b++; }
/*    */     
/* 78 */     throw new IllegalStateException("Unable to obtain PrivateKey from any locator.");
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/DelegatingLookupSvcLocator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */