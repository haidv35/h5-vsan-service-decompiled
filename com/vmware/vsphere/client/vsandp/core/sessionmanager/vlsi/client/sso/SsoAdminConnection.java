/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*    */ 
/*    */ import com.vmware.vim.binding.sso.admin.ConfigurationManagementService;
/*    */ import com.vmware.vim.binding.sso.admin.ServiceContent;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SsoAdminConnection
/*    */   extends VlsiConnection
/*    */ {
/*    */   protected volatile ServiceContent content;
/*    */   
/*    */   public ServiceContent getContent() {
/* 19 */     return this.content;
/*    */   }
/*    */   
/*    */   public X509Certificate[] getSigningCerts() {
/* 23 */     ConfigurationManagementService cms = (ConfigurationManagementService)createStub(
/* 24 */         ConfigurationManagementService.class, 
/* 25 */         this.content.getConfigurationManagementService());
/*    */     
/*    */     try {
/* 28 */       return CertificateHelper.getCerts(cms.getTrustedCertificates());
/* 29 */     } catch (CertificateException e) {
/* 30 */       throw new SsoException(
/* 31 */           "Unable to retrieve trusted certificates from SSO Admin Service", 
/* 32 */           e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/SsoAdminConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */