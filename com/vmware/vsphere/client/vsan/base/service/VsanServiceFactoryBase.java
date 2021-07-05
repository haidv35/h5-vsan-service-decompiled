/*     */ package com.vmware.vsphere.client.vsan.base.service;
/*     */ 
/*     */ import com.vmware.vim.vmomi.client.Client;
/*     */ import com.vmware.vim.vmomi.client.ClientConfiguration;
/*     */ import com.vmware.vim.vmomi.client.common.Session;
/*     */ import com.vmware.vim.vmomi.client.http.HttpClientConfiguration;
/*     */ import com.vmware.vim.vmomi.client.http.HttpConfiguration;
/*     */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*     */ import com.vmware.vim.vmomi.client.http.impl.HttpConfigurationImpl;
/*     */ import com.vmware.vim.vmomi.core.RequestContext;
/*     */ import com.vmware.vim.vmomi.core.impl.RequestContextImpl;
/*     */ import com.vmware.vise.security.ClientSessionEndListener;
/*     */ import com.vmware.vise.usersession.ServerInfo;
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.SingleThumbprintVerifier;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VsanServiceFactoryBase<T>
/*     */   implements ClientSessionEndListener
/*     */ {
/*  38 */   protected static final Log _logger = LogFactory.getLog(VsanServiceFactoryBase.class);
/*     */   
/*     */   protected static final String VC_SESSION_COOKIE = "Cookie";
/*  41 */   private ConcurrentHashMap<String, T> sessionContext = new ConcurrentHashMap<>();
/*     */   
/*     */   private ExecutorService _threadPoolExecutor;
/*     */   
/*     */   private UserSessionService _userSessionService;
/*     */   private ServiceBundleActivator _bundleActivator;
/*     */   @Autowired
/*     */   private VmodlHelper _vmodlHelper;
/*     */   
/*     */   public void setThreadPoolExecutor(ExecutorService executor) {
/*  51 */     this._threadPoolExecutor = executor;
/*     */   }
/*     */   
/*     */   public void setUserSessionService(UserSessionService userSessionService) {
/*  55 */     this._userSessionService = userSessionService;
/*     */   }
/*     */   
/*     */   public void setBundleActivator(ServiceBundleActivator bundleActivator) {
/*  59 */     this._bundleActivator = bundleActivator;
/*     */   }
/*     */   
/*     */   protected ServiceBundleActivator getBundleActivator() {
/*  63 */     return this._bundleActivator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getService(String vcGuid) {
/*  73 */     String key = getSessionKey(vcGuid);
/*  74 */     T result = this.sessionContext.get(key);
/*  75 */     if (result == null) {
/*  76 */       T newEntity = create(vcGuid);
/*  77 */       result = this.sessionContext.putIfAbsent(key, newEntity);
/*  78 */       if (result == null) {
/*  79 */         result = newEntity;
/*     */       } else {
/*     */         
/*  82 */         destroy(newEntity);
/*     */       } 
/*     */     } 
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionKey(String vcGuid) {
/*  93 */     if (vcGuid == null) {
/*  94 */       return null;
/*     */     }
/*     */     
/*  97 */     return String.valueOf(vcGuid) + ":" + (this._userSessionService.getUserSession()).clientId;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Client createClient(String vcGuid, String serviceDir, Class<?> version) {
/* 102 */     HttpConfigurationImpl httpConfigurationImpl = new HttpConfigurationImpl();
/*     */ 
/*     */     
/* 105 */     SingleThumbprintVerifier singleThumbprintVerifier = new SingleThumbprintVerifier((findInfo(vcGuid)).thumbprint);
/* 106 */     httpConfigurationImpl.setThumbprintVerifier((ThumbprintVerifier)singleThumbprintVerifier);
/*     */     
/* 108 */     HttpClientConfiguration httpClientConfiguration = 
/* 109 */       HttpClientConfiguration.Factory.newInstance();
/* 110 */     httpClientConfiguration.setExecutor(this._threadPoolExecutor);
/* 111 */     httpClientConfiguration.setHttpConfiguration((HttpConfiguration)httpConfigurationImpl);
/*     */ 
/*     */ 
/*     */     
/* 115 */     URI serviceUri = null;
/*     */     try {
/* 117 */       serviceUri = getServiceLocation(vcGuid, serviceDir);
/* 118 */     } catch (Exception ex) {
/* 119 */       _logger.error(ex);
/*     */     } 
/*     */     
/* 122 */     Client client = Client.Factory.createClient(
/* 123 */         serviceUri, 
/* 124 */         version, 
/* 125 */         this._bundleActivator.getVmodlContext(), 
/* 126 */         (ClientConfiguration)httpClientConfiguration);
/*     */     
/* 128 */     return client;
/*     */   }
/*     */   
/*     */   protected Client createClient(String vcGuid, String serviceDir) {
/* 132 */     Class<?> version = this._vmodlHelper.getVsanVmodlVersion(vcGuid);
/*     */     
/* 134 */     _logger.info("Using VMODL version: " + version.getName());
/* 135 */     return createClient(vcGuid, serviceDir, version);
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestContext prepareSessionContext(String vcGuid, Client client) {
/* 140 */     String sessionCookie = (findInfo(vcGuid)).sessionCookie;
/* 141 */     Session session = client.getBinding().createSession(sessionCookie);
/* 142 */     client.getBinding().setSession(session);
/*     */ 
/*     */ 
/*     */     
/* 146 */     RequestContextImpl requestContextImpl = new RequestContextImpl();
/* 147 */     requestContextImpl.put("Cookie", sessionCookie);
/* 148 */     return (RequestContext)requestContextImpl;
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
/*     */   private URI getServiceLocation(String vcGuid, String serviceDir) throws Exception {
/* 160 */     String vcServiceUrl = (findInfo(vcGuid)).serviceUrl;
/*     */     
/* 162 */     if (StringUtils.isEmpty(vcServiceUrl)) {
/* 163 */       _logger.error("getServiceLocation: Failed to retrieve the VC URL.");
/* 164 */       throw new Exception("Failed to retrieve VC service Url");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 170 */       URL vcUrl = new URL(vcServiceUrl);
/* 171 */       URL vsanHealthUrl = new URL(
/* 172 */           vcUrl.getProtocol(), 
/* 173 */           vcUrl.getHost(), 
/* 174 */           vcUrl.getPort(), 
/* 175 */           serviceDir);
/* 176 */       return vsanHealthUrl.toURI();
/* 177 */     } catch (URISyntaxException|java.net.MalformedURLException e) {
/* 178 */       _logger.error(e);
/* 179 */       throw new Exception(e);
/*     */     }  } private ServerInfo findInfo(String vcGuid) {
/*     */     byte b;
/*     */     int i;
/*     */     ServerInfo[] arrayOfServerInfo;
/* 184 */     for (i = (arrayOfServerInfo = (this._userSessionService.getUserSession()).serversInfo).length, b = 0; b < i; ) { ServerInfo info = arrayOfServerInfo[b];
/* 185 */       if (vcGuid.equalsIgnoreCase(info.serviceGuid))
/* 186 */         return info; 
/*     */       b++; }
/*     */     
/* 189 */     throw new IllegalStateException("server info not found: " + vcGuid);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T create(String paramString);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void destroy(T paramT);
/*     */ 
/*     */   
/*     */   public void beanDestroyed() {
/* 202 */     Iterator<Map.Entry<String, T>> iterator = this.sessionContext.entrySet().iterator();
/* 203 */     while (iterator.hasNext()) {
/* 204 */       Map.Entry<String, T> entry = iterator.next();
/* 205 */       iterator.remove();
/* 206 */       destroy(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionEnded(String clientId) {
/* 216 */     Iterator<Map.Entry<String, T>> iterator = this.sessionContext.entrySet().iterator();
/* 217 */     while (iterator.hasNext()) {
/* 218 */       Map.Entry<String, T> entry = iterator.next();
/* 219 */       if (((String)entry.getKey()).endsWith(clientId)) {
/* 220 */         iterator.remove();
/* 221 */         destroy(entry.getValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanServiceFactoryBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */