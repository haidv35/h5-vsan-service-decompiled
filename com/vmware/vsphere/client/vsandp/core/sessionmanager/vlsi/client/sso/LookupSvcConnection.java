/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso;
/*     */ 
/*     */ import com.vmware.vim.binding.lookup.LookupService;
/*     */ import com.vmware.vim.binding.lookup.SearchCriteria;
/*     */ import com.vmware.vim.binding.lookup.Service;
/*     */ import com.vmware.vim.binding.lookup.ServiceContent;
/*     */ import com.vmware.vim.binding.lookup.ServiceEndpoint;
/*     */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*     */ import com.vmware.vim.sso.client.util.codec.Base64;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiConnection;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LookupSvcConnection
/*     */   extends VlsiConnection
/*     */ {
/*     */   protected volatile ServiceContent content;
/*     */   
/*     */   public LookupService getLookupService() {
/*  32 */     return (LookupService)createStub(LookupService.class, this.content.getLookupService());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceRegistration getServiceRegistration() {
/*  41 */     return (ServiceRegistration)createStub(ServiceRegistration.class, 
/*  42 */         this.content.getServiceRegistration());
/*     */   }
/*     */   
/*     */   public ServiceContent getContent() {
/*  46 */     return this.content;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final SearchCriteria URN_SSO_STS = mkSearchCriteria(
/*  52 */       "urn:sso:sts", ServiceEndpoint.EndpointProtocol.wsTrust);
/*     */   
/*  54 */   private static final SearchCriteria URN_SSO_ADMIN = mkSearchCriteria(
/*  55 */       "urn:sso:admin", ServiceEndpoint.EndpointProtocol.vmomi);
/*     */   
/*  57 */   protected static CertificateFactory cf = getCertFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   protected static SearchCriteria mkSearchCriteria(String urn, ServiceEndpoint.EndpointProtocol type) {
/*     */     try {
/*  63 */       return new SearchCriteria(new URI(urn), null, (type == null) ? null : 
/*  64 */           type.toString());
/*  65 */     } catch (URISyntaxException e) {
/*  66 */       throw new RuntimeException("Could not parse URN: " + urn, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static CertificateFactory getCertFactory() {
/*     */     try {
/*  72 */       return CertificateFactory.getInstance("X.509");
/*  73 */     } catch (CertificateException e) {
/*  74 */       throw new SsoException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SsoEndpoints getSsoEndpoints() {
/*  80 */     return new SsoEndpoints(getSts(), getAdmin(), null);
/*     */   }
/*     */   
/*     */   public ServiceEndpoint getSts() {
/*  84 */     return retrieveEndpoint(URN_SSO_STS);
/*     */   }
/*     */   
/*     */   public ServiceEndpoint getAdmin() {
/*  88 */     return retrieveEndpoint(URN_SSO_ADMIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceEndpoint retrieveEndpoint(SearchCriteria criteria) {
/*     */     X509Certificate[] certs;
/*  97 */     List<Service> services = findRawServices(criteria);
/*     */     
/*  99 */     if (services.isEmpty()) {
/* 100 */       throw new RuntimeException("Service not found: " + criteria);
/*     */     }
/*     */     
/* 103 */     Service service = services.get(0);
/*     */     
/* 105 */     if (service.endpoints.length < 1) {
/* 106 */       throw new RuntimeException("Endpoint not found: " + criteria);
/*     */     }
/*     */     
/* 109 */     ServiceEndpoint point = service
/* 110 */       .getEndpoints()[0];
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 115 */       certs = (point.getSslTrustAnchor() == null) ? new X509Certificate[0] : 
/* 116 */         getCerts(new String[] { point.getSslTrustAnchor() });
/* 117 */     } catch (CertificateException e) {
/* 118 */       throw new RuntimeException("Could not retrieve endpoint trust anchor", 
/* 119 */           e);
/*     */     } 
/*     */     
/* 122 */     return new ServiceEndpoint(point.getUrl(), certs);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Service> findRawServices(SearchCriteria criteria) {
/*     */     try {
/* 128 */       Service[] services = getLookupService().find(criteria);
/* 129 */       if (services == null) {
/* 130 */         return Collections.emptyList();
/*     */       }
/* 132 */       return Arrays.asList(services);
/* 133 */     } catch (RuntimeException e) {
/* 134 */       throw e;
/* 135 */     } catch (Exception e) {
/* 136 */       throw new RuntimeException("LookupService search failure", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceEndpoint retrieveEndpoint(ServiceRegistration.Filter filter) {
/* 147 */     List<ServiceRegistration.Info> services = findRawServices(filter);
/*     */     
/* 149 */     if (services.isEmpty()) {
/* 150 */       throw new RuntimeException("Service not found: " + filter);
/*     */     }
/*     */     
/* 153 */     ServiceRegistration.Info service = services.get(0);
/*     */     
/* 155 */     return fromInfo(service, (String)null, (String)null);
/*     */   }
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
/*     */   public ServiceEndpoint fromInfo(ServiceRegistration.Info service, String endpointProtocol, String endpointType) {
/*     */     X509Certificate[] certs;
/* 169 */     ServiceRegistration.Endpoint[] endpoints = service.getServiceEndpoints();
/* 170 */     if (endpoints == null || endpoints.length < 1) {
/* 171 */       throw new RuntimeException("Service has zero endpoints: " + service);
/*     */     }
/*     */     
/* 174 */     ServiceRegistration.Endpoint point = null;
/* 175 */     if (endpointProtocol == null && endpointType == null) {
/* 176 */       point = endpoints[0];
/*     */     } else {
/* 178 */       byte b; int i; ServiceRegistration.Endpoint[] arrayOfEndpoint; for (i = (arrayOfEndpoint = endpoints).length, b = 0; b < i; ) { ServiceRegistration.Endpoint endpoint = arrayOfEndpoint[b];
/* 179 */         if ((endpointProtocol == null || 
/* 180 */           endpointProtocol.equals(endpoint.getEndpointType().getProtocol())) && (
/* 181 */           endpointType == null || 
/* 182 */           endpointType.equals(endpoint.getEndpointType().getType()))) {
/* 183 */           point = endpoint;
/*     */           break;
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 189 */     if (point == null) {
/* 190 */       throw new RuntimeException(
/* 191 */           "Cannot find endpoint for protocol " + endpointProtocol + 
/* 192 */           " and/or type " + endpointType + 
/* 193 */           " in service " + service.getServiceId());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 199 */       certs = (point.getSslTrust() == null) ? new X509Certificate[0] : 
/* 200 */         getCerts(point.getSslTrust());
/* 201 */     } catch (CertificateException e) {
/* 202 */       throw new RuntimeException("Could not retrieve endpoint trust anchor", 
/* 203 */           e);
/*     */     } 
/*     */     
/* 206 */     return new ServiceEndpoint(point.getUrl(), certs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ServiceRegistration.Info> findRawServices(ServiceRegistration.Filter filter) {
/*     */     try {
/* 213 */       ServiceRegistration.Info[] services = getServiceRegistration().list(filter);
/* 214 */       if (services == null) {
/* 215 */         return Collections.emptyList();
/*     */       }
/* 217 */       return Arrays.asList(services);
/* 218 */     } catch (RuntimeException e) {
/* 219 */       throw e;
/* 220 */     } catch (Exception e) {
/* 221 */       throw new RuntimeException("ServiceRegistration search failure", e);
/*     */     } 
/*     */   }
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
/*     */   public static X509Certificate fromBase64(String pem) throws CertificateException {
/* 235 */     return (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(
/* 236 */           Base64.decodeBase64(pem.getBytes(Charset.forName("ASCII")))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static X509Certificate[] getCerts(String[] certs) throws CertificateException {
/* 241 */     List<X509Certificate> result = new ArrayList<>(); byte b; int i; String[] arrayOfString;
/* 242 */     for (i = (arrayOfString = certs).length, b = 0; b < i; ) { String pem = arrayOfString[b];
/* 243 */       result.add(fromBase64(pem));
/*     */       b++; }
/*     */     
/* 246 */     return result.<X509Certificate>toArray(new X509Certificate[result.size()]);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/LookupSvcConnection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */