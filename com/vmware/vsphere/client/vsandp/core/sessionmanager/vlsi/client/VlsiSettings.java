/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client;
/*     */ 
/*     */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.ClientCfg;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.HttpSettings;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.ExecutorSettings;
/*     */ import java.net.URI;
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
/*     */ public class VlsiSettings
/*     */ {
/*     */   protected final ResourceFactory<ClientCfg, HttpSettings> httpMgr;
/*     */   protected final HttpSettings httpSettings;
/*     */   protected final Authenticator authenticator;
/*     */   protected final String sessionCookie;
/*     */   
/*     */   public VlsiSettings(ResourceFactory<ClientCfg, HttpSettings> httpFactory, HttpSettings httpSettings, Authenticator authenticator, String sessionCookie) {
/*  33 */     this.httpMgr = httpFactory;
/*  34 */     this.httpSettings = httpSettings;
/*  35 */     this.authenticator = (authenticator != null) ? authenticator : new Authenticator();
/*  36 */     this.sessionCookie = sessionCookie;
/*     */   }
/*     */   
/*     */   public ResourceFactory<ClientCfg, HttpSettings> getHttpFactory() {
/*  40 */     return this.httpMgr;
/*     */   }
/*     */   
/*     */   public VlsiSettings setHttpFactory(ResourceFactory<ClientCfg, HttpSettings> httpMgr) {
/*  44 */     return new VlsiSettings(httpMgr, this.httpSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public HttpSettings getHttpSettings() {
/*  48 */     return this.httpSettings;
/*     */   }
/*     */   
/*     */   public VlsiSettings setHttpSettings(HttpSettings httpSettings) {
/*  52 */     return new VlsiSettings(this.httpMgr, httpSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public Authenticator getAuthenticator() {
/*  56 */     return this.authenticator;
/*     */   }
/*     */   
/*     */   public VlsiSettings setAuthenticator(Authenticator authenticator) {
/*  60 */     return new VlsiSettings(this.httpMgr, this.httpSettings, authenticator, this.sessionCookie);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VlsiSettings setServiceInfo(URI serviceUri, Class<?> version) {
/*  66 */     HttpSettings newSettings = this.httpSettings
/*  67 */       .setServiceUri(serviceUri)
/*  68 */       .setVersion(version);
/*  69 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings setProxyInfo(URI proxyUri) {
/*  73 */     HttpSettings newSettings = this.httpSettings.setProxyUri(proxyUri);
/*  74 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings setSslContext(ClientCertificate trustStore, ThumbprintVerifier thumbprintVerifier) {
/*  78 */     HttpSettings newSettings = this.httpSettings
/*  79 */       .setTrustStore(trustStore)
/*  80 */       .setThumbprintVerifier(thumbprintVerifier);
/*  81 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings setExecutorSettings(ExecutorSettings executorSettings) {
/*  85 */     HttpSettings newSettings = this.httpSettings.setExecutorSettings(executorSettings);
/*  86 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings setConnectionSettings(int maxConn, int timeout) {
/*  90 */     HttpSettings newSettings = this.httpSettings
/*  91 */       .setMaxConn(maxConn)
/*  92 */       .setTimeout(timeout);
/*  93 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings setClientCertificate(ClientCertificate clientCert) {
/*  97 */     HttpSettings newSettings = this.httpSettings.setClientCert(clientCert);
/*  98 */     return new VlsiSettings(this.httpMgr, newSettings, this.authenticator, this.sessionCookie);
/*     */   }
/*     */   
/*     */   public String getSessionCookie() {
/* 102 */     return this.sessionCookie;
/*     */   }
/*     */   
/*     */   public VlsiSettings setSessionCookie(String sessionCookie) {
/* 106 */     return new VlsiSettings(this.httpMgr, this.httpSettings, this.authenticator, sessionCookie);
/*     */   }
/*     */   
/*     */   public VlsiSettings updateFrom(ServiceRegistration.Info info) {
/* 110 */     return updateFrom(info, null, null);
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
/*     */   public VlsiSettings updateFrom(ServiceRegistration.Info info, ServiceRegistration.EndpointType endpointType, Class<?> versionCls) {
/* 133 */     ServiceRegistration.Endpoint point = null;
/* 134 */     if (endpointType == null) {
/*     */ 
/*     */       
/* 137 */       if ((info.getServiceEndpoints()).length != 1) {
/* 138 */         throw new IllegalArgumentException(
/* 139 */             "Exactly one endpoint expected, but found " + (
/* 140 */             info.getServiceEndpoints()).length + 
/* 141 */             " endpoints in service " + info);
/*     */       }
/*     */       
/* 144 */       point = info.getServiceEndpoints()[0];
/*     */     } else {
/*     */       
/* 147 */       ServiceRegistration.Endpoint[] endpoints = info.getServiceEndpoints();
/*     */       
/* 149 */       if (endpoints == null || endpoints.length < 1)
/* 150 */         throw new RuntimeException(
/* 151 */             "No endpoints found for service " + info);  byte b;
/*     */       int i;
/*     */       ServiceRegistration.Endpoint[] arrayOfEndpoint1;
/* 154 */       for (i = (arrayOfEndpoint1 = endpoints).length, b = 0; b < i; ) { ServiceRegistration.Endpoint endpoint = arrayOfEndpoint1[b];
/* 155 */         if ((endpointType.getProtocol() == null || 
/* 156 */           endpointType.getProtocol().equals(endpoint.getEndpointType().getProtocol())) && (
/* 157 */           endpointType.getType() == null || 
/* 158 */           endpointType.getType().equals(endpoint.getEndpointType().getType()))) {
/* 159 */           point = endpoint;
/*     */           break;
/*     */         } 
/*     */         b++; }
/*     */       
/* 164 */       if (point == null) {
/* 165 */         throw new RuntimeException(
/* 166 */             "Cannot find endpoint for protocol " + endpointType.getProtocol() + 
/* 167 */             " and/or type " + endpointType.getType() + 
/* 168 */             " in service " + info);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 173 */     ClientCertificate truststore = new ClientCertificate(
/* 174 */         info.getServiceId(), point.getSslTrust(), 
/* 175 */         "", "", info.getServiceId());
/*     */     
/* 177 */     Class<?> cls = (versionCls != null) ? 
/* 178 */       versionCls : 
/*     */       
/* 180 */       getHttpSettings().getVersion();
/*     */     
/* 182 */     return 
/* 183 */       setSslContext(truststore, null)
/* 184 */       .setProxyInfo(null)
/* 185 */       .setServiceInfo(point.getUrl(), cls);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 190 */     31;
/* 191 */     int result = 1;
/* 192 */     result = 31 * result + ((this.authenticator == null) ? 0 : this.authenticator.hashCode());
/* 193 */     result = 31 * result + ((this.httpSettings == null) ? 0 : this.httpSettings.hashCode());
/* 194 */     result = 31 * result + ((this.sessionCookie == null) ? 0 : this.sessionCookie.hashCode());
/* 195 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 200 */     if (this == obj)
/* 201 */       return true; 
/* 202 */     if (obj == null)
/* 203 */       return false; 
/* 204 */     if (getClass() != obj.getClass())
/* 205 */       return false; 
/* 206 */     VlsiSettings other = (VlsiSettings)obj;
/*     */     
/* 208 */     if (this.authenticator == null) {
/* 209 */       if (other.authenticator != null) {
/* 210 */         return false;
/*     */       }
/* 212 */     } else if (!this.authenticator.equals(other.authenticator)) {
/* 213 */       return false;
/*     */     } 
/*     */     
/* 216 */     if (this.httpSettings == null) {
/* 217 */       if (other.httpSettings != null) {
/* 218 */         return false;
/*     */       }
/* 220 */     } else if (!this.httpSettings.equals(other.httpSettings)) {
/* 221 */       return false;
/*     */     } 
/*     */     
/* 224 */     if (this.sessionCookie == null) {
/* 225 */       if (other.sessionCookie != null) {
/* 226 */         return false;
/*     */       }
/* 228 */     } else if (!this.sessionCookie.equals(other.sessionCookie)) {
/* 229 */       return false;
/*     */     } 
/*     */     
/* 232 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 237 */     return String.format("VlsiSettings [authenticator=%s, httpSettings=%s, sessionCookie=%s]", new Object[] {
/* 238 */           this.authenticator, this.httpSettings, this.sessionCookie
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/VlsiSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */