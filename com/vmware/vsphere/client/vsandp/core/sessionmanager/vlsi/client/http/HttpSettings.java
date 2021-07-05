/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*     */ 
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlContext;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.CloseableExecutorService;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.ExecutorSettings;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Map;
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
/*     */ public class HttpSettings
/*     */   extends BaseHttpSettings
/*     */ {
/*     */   protected final Class<?> version;
/*     */   protected final String proxyProto;
/*     */   protected final String proxyHost;
/*     */   protected final int proxyPort;
/*     */   protected final ClientCertificate clientCert;
/*     */   protected final ThumbprintVerifier thumbprintVerifier;
/*     */   protected final VmodlContext vmodlContext;
/*     */   protected final Map<String, Object> requestProperties;
/*     */   
/*     */   public static HttpSettings createTemplate(ResourceFactory<CloseableExecutorService, ExecutorSettings> executorFactory, ExecutorSettings executorSettings, VmodlContext vmodlContext, int maxConnections, int timeoutInMillis) {
/*  44 */     return new HttpSettings("https", null, -1, null, null, null, -1, maxConnections, timeoutInMillis, null, null, 
/*  45 */         (ThumbprintVerifier)new LenientThumbprintVerifier(), executorFactory, executorSettings, 
/*  46 */         Void.class, vmodlContext, null);
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
/*     */   public HttpSettings(String proto, String host, int port, String path, String proxyProto, String proxyHost, int proxyPort, int maxConn, int timeout, ClientCertificate clientCert, ClientCertificate trustStore, ThumbprintVerifier thumbprintVerifier, ResourceFactory<CloseableExecutorService, ExecutorSettings> executorMgr, ExecutorSettings executorSettings, Class<?> version, VmodlContext vmodlContext, Map<String, Object> requestProperties) {
/*  83 */     super(executorMgr, executorSettings, proto, host, port, path, maxConn, timeout, trustStore);
/*  84 */     this.proxyProto = proxyProto;
/*  85 */     this.proxyHost = proxyHost;
/*  86 */     this.proxyPort = proxyPort;
/*  87 */     this.clientCert = clientCert;
/*  88 */     this.thumbprintVerifier = thumbprintVerifier;
/*  89 */     this.version = version;
/*  90 */     this.vmodlContext = vmodlContext;
/*  91 */     this.requestProperties = requestProperties;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpSettings setExecutorFactory(ResourceFactory<CloseableExecutorService, ExecutorSettings> executorMgr) {
/*  96 */     return new HttpSettings(
/*  97 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/*  98 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, executorMgr, 
/*  99 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setExecutorSettings(ExecutorSettings executorSettings) {
/* 103 */     return new HttpSettings(
/* 104 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 105 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 106 */         executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setServiceUri(URI serviceUri) {
/* 110 */     return new HttpSettings(
/* 111 */         serviceUri.getScheme(), serviceUri.getHost(), serviceUri.getPort(), 
/* 112 */         serviceUri.getPath(), this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 113 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 114 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public Class<?> getVersion() {
/* 118 */     return this.version;
/*     */   }
/*     */   
/*     */   public HttpSettings setVersion(Class<?> version) {
/* 122 */     return new HttpSettings(
/* 123 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 124 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 125 */         this.executorSettings, version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setProto(String proto) {
/* 129 */     return new HttpSettings(
/* 130 */         proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 131 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 132 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setHost(String host) {
/* 136 */     return new HttpSettings(
/* 137 */         this.proto, host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 138 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 139 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setPort(int port) {
/* 143 */     return new HttpSettings(
/* 144 */         this.proto, this.host, port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 145 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 146 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setPath(String path) {
/* 150 */     return new HttpSettings(
/* 151 */         this.proto, this.host, this.port, path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 152 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 153 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setProxyUri(URI proxyUri) {
/* 157 */     return new HttpSettings(
/* 158 */         this.proto, this.host, this.port, this.path, 
/* 159 */         (proxyUri == null) ? null : proxyUri.getScheme(), 
/* 160 */         (proxyUri == null) ? null : proxyUri.getHost(), 
/* 161 */         (proxyUri == null) ? -1 : proxyUri.getPort(), 
/* 162 */         this.maxConn, this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, 
/* 163 */         this.executorFactory, this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public String getProxyProto() {
/* 167 */     return this.proxyProto;
/*     */   }
/*     */   
/*     */   public HttpSettings setProxyProto(String proxyProto) {
/* 171 */     return new HttpSettings(
/* 172 */         this.proto, this.host, this.port, this.path, proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 173 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 174 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public String getProxyHost() {
/* 178 */     return this.proxyHost;
/*     */   }
/*     */   
/*     */   public HttpSettings setProxyHost(String proxyHost) {
/* 182 */     return new HttpSettings(
/* 183 */         this.proto, this.host, this.port, this.path, this.proxyProto, proxyHost, this.proxyPort, this.maxConn, 
/* 184 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 185 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public int getProxyPort() {
/* 189 */     return this.proxyPort;
/*     */   }
/*     */   
/*     */   public HttpSettings setProxyPort(int proxyPort) {
/* 193 */     return new HttpSettings(
/* 194 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, proxyPort, this.maxConn, 
/* 195 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 196 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setMaxConn(int maxConn) {
/* 200 */     return new HttpSettings(
/* 201 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, maxConn, 
/* 202 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 203 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setTimeout(int timeout) {
/* 207 */     return new HttpSettings(
/* 208 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 209 */         timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 210 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public ClientCertificate getClientCert() {
/* 214 */     return this.clientCert;
/*     */   }
/*     */   
/*     */   public HttpSettings setClientCert(ClientCertificate clientCert) {
/* 218 */     return new HttpSettings(
/* 219 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 220 */         this.timeout, clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 221 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public HttpSettings setTrustStore(ClientCertificate trustStore) {
/* 225 */     return new HttpSettings(
/* 226 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 227 */         this.timeout, this.clientCert, trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 228 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public ThumbprintVerifier getThumbprintVerifier() {
/* 232 */     return this.thumbprintVerifier;
/*     */   }
/*     */   
/*     */   public HttpSettings setThumbprintVerifier(ThumbprintVerifier thumbprintVerifier) {
/* 236 */     return new HttpSettings(
/* 237 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 238 */         this.timeout, this.clientCert, this.trustStore, thumbprintVerifier, this.executorFactory, 
/* 239 */         this.executorSettings, this.version, this.vmodlContext, this.requestProperties);
/*     */   }
/*     */   
/*     */   public Map<String, Object> getRequestProperties() {
/* 243 */     return this.requestProperties;
/*     */   }
/*     */   
/*     */   public HttpSettings setRequestProperties(Map<String, Object> requestProperties) {
/* 247 */     return new HttpSettings(
/* 248 */         this.proto, this.host, this.port, this.path, this.proxyProto, this.proxyHost, this.proxyPort, this.maxConn, 
/* 249 */         this.timeout, this.clientCert, this.trustStore, this.thumbprintVerifier, this.executorFactory, 
/* 250 */         this.executorSettings, this.version, this.vmodlContext, requestProperties);
/*     */   }
/*     */   
/*     */   public VmodlContext getVmodlContext() {
/* 254 */     return this.vmodlContext;
/*     */   }
/*     */   
/*     */   public boolean isViaProxy() {
/* 258 */     return (this.proxyHost != null);
/*     */   }
/*     */   
/*     */   public URI makeUri() {
/*     */     try {
/* 263 */       return new URI(getProto(), null, getHost(), getPort(), getPath(), 
/* 264 */           null, null);
/* 265 */     } catch (URISyntaxException e) {
/* 266 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 272 */     31;
/* 273 */     int result = super.hashCode();
/* 274 */     result = 
/* 275 */       31 * result + ((this.clientCert == null) ? 0 : this.clientCert.hashCode());
/* 276 */     result = 
/* 277 */       31 * result + ((this.proxyHost == null) ? 0 : this.proxyHost.hashCode());
/* 278 */     result = 31 * result + this.proxyPort;
/* 279 */     result = 
/* 280 */       31 * result + ((this.proxyProto == null) ? 0 : this.proxyProto.hashCode());
/* 281 */     result = 
/* 282 */       31 * 
/* 283 */       result + (
/* 284 */       (this.thumbprintVerifier == null) ? 0 : this.thumbprintVerifier
/* 285 */       .hashCode());
/* 286 */     result = 31 * result + ((this.version == null) ? 0 : this.version.hashCode());
/* 287 */     result = 
/* 288 */       31 * result + (
/* 289 */       (this.vmodlContext == null) ? 0 : this.vmodlContext.hashCode());
/* 290 */     result = 31 * result + ((this.requestProperties == null) ? 0 : this.requestProperties.hashCode());
/* 291 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 296 */     if (this == obj)
/* 297 */       return true; 
/* 298 */     if (!super.equals(obj))
/* 299 */       return false; 
/* 300 */     if (getClass() != obj.getClass())
/* 301 */       return false; 
/* 302 */     HttpSettings other = (HttpSettings)obj;
/* 303 */     if (this.clientCert == null) {
/* 304 */       if (other.clientCert != null)
/* 305 */         return false; 
/* 306 */     } else if (!this.clientCert.equals(other.clientCert)) {
/* 307 */       return false;
/* 308 */     }  if (this.proxyHost == null) {
/* 309 */       if (other.proxyHost != null)
/* 310 */         return false; 
/* 311 */     } else if (!this.proxyHost.equals(other.proxyHost)) {
/* 312 */       return false;
/* 313 */     }  if (this.proxyPort != other.proxyPort)
/* 314 */       return false; 
/* 315 */     if (this.proxyProto == null) {
/* 316 */       if (other.proxyProto != null)
/* 317 */         return false; 
/* 318 */     } else if (!this.proxyProto.equals(other.proxyProto)) {
/* 319 */       return false;
/* 320 */     }  if (this.thumbprintVerifier == null) {
/* 321 */       if (other.thumbprintVerifier != null)
/* 322 */         return false; 
/* 323 */     } else if (!this.thumbprintVerifier.equals(other.thumbprintVerifier)) {
/* 324 */       return false;
/* 325 */     }  if (this.version == null) {
/* 326 */       if (other.version != null)
/* 327 */         return false; 
/* 328 */     } else if (!this.version.equals(other.version)) {
/* 329 */       return false;
/* 330 */     }  if (this.vmodlContext == null) {
/* 331 */       if (other.vmodlContext != null)
/* 332 */         return false; 
/* 333 */     } else if (!this.vmodlContext.equals(other.vmodlContext)) {
/* 334 */       return false;
/* 335 */     }  if (this.requestProperties == null) {
/* 336 */       if (other.requestProperties != null)
/* 337 */         return false; 
/* 338 */     } else if (!this.requestProperties.equals(other.requestProperties)) {
/* 339 */       return false;
/* 340 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 345 */     return "HttpSettings [version=" + this.version + ", proxyProto=" + this.proxyProto + 
/* 346 */       ", proxyHost=" + this.proxyHost + ", proxyPort=" + this.proxyPort + 
/* 347 */       ", clientCert=" + this.clientCert + ", thumbprintVerifier=" + 
/* 348 */       this.thumbprintVerifier + ", vmodlContext=" + this.vmodlContext + 
/* 349 */       ", executorFactory=" + this.executorFactory + ", executorSettings=" + 
/* 350 */       this.executorSettings + ", proto=" + this.proto + ", host=" + this.host + 
/* 351 */       ", port=" + this.port + ", path=" + this.path + ", maxConn=" + this.maxConn + 
/* 352 */       ", timeout=" + this.timeout + ", trustStore=" + this.trustStore + 
/* 353 */       ", requestProperties=" + this.requestProperties + "]";
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/HttpSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */