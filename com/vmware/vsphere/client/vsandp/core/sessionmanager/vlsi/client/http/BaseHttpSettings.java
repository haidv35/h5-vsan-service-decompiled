/*     */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http;
/*     */ 
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.util.ClientCertificate;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.CloseableExecutorService;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.ExecutorSettings;
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
/*     */ public class BaseHttpSettings
/*     */ {
/*     */   protected final ResourceFactory<CloseableExecutorService, ExecutorSettings> executorFactory;
/*     */   protected final ExecutorSettings executorSettings;
/*     */   protected final String proto;
/*     */   protected final String host;
/*     */   protected final int port;
/*     */   protected final String path;
/*     */   protected final int maxConn;
/*     */   protected final int timeout;
/*     */   protected final ClientCertificate trustStore;
/*     */   
/*     */   public BaseHttpSettings(ResourceFactory<CloseableExecutorService, ExecutorSettings> executorFactory, ExecutorSettings executorSettings, String proto, String host, int port, String path, int maxConn, int timeout, ClientCertificate trustStore) {
/*  37 */     this.executorFactory = executorFactory;
/*  38 */     this.executorSettings = executorSettings;
/*  39 */     this.proto = proto;
/*  40 */     this.host = host;
/*  41 */     this.port = port;
/*  42 */     this.path = path;
/*  43 */     this.maxConn = maxConn;
/*  44 */     this.timeout = timeout;
/*  45 */     this.trustStore = trustStore;
/*     */   }
/*     */   
/*     */   public ResourceFactory<CloseableExecutorService, ExecutorSettings> getExecutorFactory() {
/*  49 */     return this.executorFactory;
/*     */   }
/*     */   
/*     */   public ExecutorSettings getExecutorSettings() {
/*  53 */     return this.executorSettings;
/*     */   }
/*     */   
/*     */   public String getProto() {
/*  57 */     return this.proto;
/*     */   }
/*     */   
/*     */   public String getHost() {
/*  61 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  65 */     return this.port;
/*     */   }
/*     */   
/*     */   public String getPath() {
/*  69 */     return this.path;
/*     */   }
/*     */   
/*     */   public int getMaxConn() {
/*  73 */     return this.maxConn;
/*     */   }
/*     */   
/*     */   public int getTimeout() {
/*  77 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public ClientCertificate getTrustStore() {
/*  81 */     return this.trustStore;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  86 */     31;
/*  87 */     int result = 1;
/*  88 */     result = 
/*  89 */       31 * 
/*  90 */       result + (
/*  91 */       (this.executorSettings == null) ? 0 : this.executorSettings
/*  92 */       .hashCode());
/*  93 */     result = 31 * result + ((this.host == null) ? 0 : this.host.hashCode());
/*  94 */     result = 31 * result + this.maxConn;
/*  95 */     result = 31 * result + ((this.path == null) ? 0 : this.path.hashCode());
/*  96 */     result = 31 * result + this.port;
/*  97 */     result = 31 * result + ((this.proto == null) ? 0 : this.proto.hashCode());
/*  98 */     result = 31 * result + this.timeout;
/*  99 */     result = 
/* 100 */       31 * result + ((this.trustStore == null) ? 0 : this.trustStore.hashCode());
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 106 */     if (this == obj)
/* 107 */       return true; 
/* 108 */     if (obj == null)
/* 109 */       return false; 
/* 110 */     if (getClass() != obj.getClass())
/* 111 */       return false; 
/* 112 */     BaseHttpSettings other = (BaseHttpSettings)obj;
/* 113 */     if (this.executorSettings == null) {
/* 114 */       if (other.executorSettings != null)
/* 115 */         return false; 
/* 116 */     } else if (!this.executorSettings.equals(other.executorSettings)) {
/* 117 */       return false;
/* 118 */     }  if (this.host == null) {
/* 119 */       if (other.host != null)
/* 120 */         return false; 
/* 121 */     } else if (!this.host.equals(other.host)) {
/* 122 */       return false;
/* 123 */     }  if (this.maxConn != other.maxConn)
/* 124 */       return false; 
/* 125 */     if (this.path == null) {
/* 126 */       if (other.path != null)
/* 127 */         return false; 
/* 128 */     } else if (!this.path.equals(other.path)) {
/* 129 */       return false;
/* 130 */     }  if (this.port != other.port)
/* 131 */       return false; 
/* 132 */     if (this.proto == null) {
/* 133 */       if (other.proto != null)
/* 134 */         return false; 
/* 135 */     } else if (!this.proto.equals(other.proto)) {
/* 136 */       return false;
/* 137 */     }  if (this.timeout != other.timeout)
/* 138 */       return false; 
/* 139 */     if (this.trustStore == null) {
/* 140 */       if (other.trustStore != null)
/* 141 */         return false; 
/* 142 */     } else if (!this.trustStore.equals(other.trustStore)) {
/* 143 */       return false;
/* 144 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return "BaseHttpSettings [executorFactory=" + this.executorFactory + 
/* 150 */       ", executorSettings=" + this.executorSettings + ", proto=" + this.proto + 
/* 151 */       ", host=" + this.host + ", port=" + this.port + ", path=" + this.path + 
/* 152 */       ", maxConn=" + this.maxConn + ", timeout=" + this.timeout + ", trustStore=" + 
/* 153 */       this.trustStore + "]";
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/http/BaseHttpSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */