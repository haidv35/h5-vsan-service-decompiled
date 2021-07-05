/*     */ package com.vmware.vsan.client.util;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.vmware.vim.binding.vim.Folder;
/*     */ import com.vmware.vim.binding.vim.version.stable;
/*     */ import com.vmware.vim.binding.vim.version.unstable;
/*     */ import com.vmware.vim.binding.vim.version.version10;
/*     */ import com.vmware.vim.binding.vim.version.version11;
/*     */ import com.vmware.vim.binding.vim.version.version13;
/*     */ import com.vmware.vim.binding.vim.version.versions;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.service;
/*     */ import com.vmware.vim.binding.vmodl.versionId;
/*     */ import com.vmware.vim.binding.vmodl.wsdlName;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlContext;
/*     */ import com.vmware.vim.vmomi.core.types.VmodlType;
/*     */ import com.vmware.vim.vsan.binding.vsan.version.version9;
/*     */ import com.vmware.vim.vsan.binding.vsan.version.versions;
/*     */ import com.vmware.vise.usersession.ServerInfo;
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import com.vmware.vsphere.client.vsan.base.util.NetUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ @Component
/*     */ public class VmodlHelper {
/*  47 */   private static final Log _logger = LogFactory.getLog(VmodlHelper.class);
/*     */   
/*     */   public static final String HOST_FOLDER_PREFIX = "group-h";
/*     */   
/*     */   public static final String DATACENTER_FOLDER_PREFIX = "group-d";
/*     */   
/*     */   public static final String NETWORK_FOLDER_PREFIX = "group-n";
/*     */   public static final String STORAGE_FOLDER_PREFIX = "group-s";
/*     */   public static final String VM_FOLDER_PREFIX = "group-v";
/*     */   public static final String VC_ROOT_FOLDER = "group-d1";
/*     */   public static final String VSAN_VERSIONING_FILE = "/vsanServiceVersions.xml";
/*     */   public static final String VIM_VERSIONING_FILE = "/vimServiceVersions.xml";
/*     */   private static final String PROP_NAMESPACES = "namespaces";
/*     */   private static final String PROP_NAMESPACE = "namespace";
/*     */   private static final String PROP_NAME = "name";
/*     */   private static final String PROP_VERSION = "version";
/*     */   private static final String PROP_PRIOR_VERSIONS = "priorVersions";
/*     */   private static final String VERSION_PATTERN = "urn:%s:%s";
/*     */   private static final String UNSTABLE_VERSION_REGEX = "urn:\\w+:u\\w+";
/*     */   private static final String STABLE_VERSION_REGEX = "urn:\\w+:s\\w+";
/*     */   private static final String RELEASE_VERSION_REGEX = "urn:\\w+:r\\w+";
/*     */   private static final String UNSTABLE_VERSION_PREFIX = "u";
/*     */   private static final String STABLE_VERSION_PREFIX = "s";
/*     */   private static final String RELEASE_VERSION_PREFIX = "r";
/*  71 */   private static final ImmutableList<Class<? extends Annotation>> SUPPORTED_VERSIONS = ImmutableList.of(
/*  72 */       version9.class, 
/*  73 */       version8.class, 
/*  74 */       version7.class, 
/*  75 */       version6.class, 
/*  76 */       version5.class, 
/*  77 */       version4.class, 
/*  78 */       version3.class, 
/*  79 */       unstable.class, 
/*  80 */       stable.class, 
/*  81 */       version13.class, 
/*  82 */       version11.class, 
/*  83 */       version10.class, (Object[])new Class[0]);
/*     */ 
/*     */   
/*     */   public static final String MOREF_UID_PREFIX = "urn:vmomi";
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VmodlContext vmodlContext;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UserSessionService userSessionService;
/*     */   
/*  96 */   private Map<String, Class<?>> cachedVimVersions = new ConcurrentHashMap<>();
/*  97 */   private Map<String, Class<?>> cachedVsanVersions = new ConcurrentHashMap<>();
/*  98 */   private Map<String, Class<?>> cachedVsanDpVersions = new ConcurrentHashMap<>();
/*     */   
/*     */   public Class<?> getTypeClass(ManagedObjectReference ref) {
/* 101 */     VmodlType vmodlType = this.vmodlContext.getVmodlTypeMap().getVmodlType(ref.getType());
/* 102 */     return vmodlType.getTypeClass();
/*     */   }
/*     */   
/*     */   public boolean isOfType(ManagedObjectReference ref, Class<?> typeClass) {
/* 106 */     return typeClass.isAssignableFrom(getTypeClass(ref));
/*     */   }
/*     */   
/*     */   public boolean isHostFolder(ManagedObjectReference entity) {
/* 110 */     return (isOfType(entity, Folder.class) && entity.getValue().contains("group-h"));
/*     */   }
/*     */   
/*     */   public boolean isDatacenterFolder(ManagedObjectReference entity) {
/* 114 */     return (isOfType(entity, Folder.class) && entity.getValue().contains("group-d"));
/*     */   }
/*     */   
/*     */   public boolean isNetworkFolder(ManagedObjectReference entity) {
/* 118 */     return (isOfType(entity, Folder.class) && entity.getValue().contains("group-n"));
/*     */   }
/*     */   
/*     */   public boolean isVmFolder(ManagedObjectReference entity) {
/* 122 */     return (isOfType(entity, Folder.class) && entity.getValue().contains("group-v"));
/*     */   }
/*     */   
/*     */   public boolean isStorageFolder(ManagedObjectReference entity) {
/* 126 */     return (isOfType(entity, Folder.class) && entity.getValue().contains("group-s"));
/*     */   }
/*     */   
/*     */   public boolean isVcRootFolder(ManagedObjectReference entity) {
/* 130 */     return (isOfType(entity, Folder.class) && entity.getValue().equalsIgnoreCase("group-d1"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ManagedObjectReference getRootFolder(String serverGuid) {
/* 140 */     ManagedObjectReference root = new ManagedObjectReference(((wsdlName)Folder.class
/* 141 */         .<wsdlName>getAnnotation(wsdlName.class)).value(), "group-d1", serverGuid);
/*     */     
/* 143 */     return root;
/*     */   }
/*     */   
/*     */   public static ManagedObjectReference getStorageSystem(ManagedObjectReference hostRef) {
/* 147 */     ManagedObjectReference storageSystem = new ManagedObjectReference(
/* 148 */         "HostStorageSystem", hostRef.getValue().replace("host", "storageSystem"), hostRef.getServerGuid());
/* 149 */     return storageSystem;
/*     */   }
/*     */   
/*     */   public ManagedObjectReference getVsanSystem(ManagedObjectReference hostRef) {
/* 153 */     ManagedObjectReference vsanSystem = new ManagedObjectReference(
/* 154 */         "HostVsanSystem", hostRef.getValue().replace("host", "vsanSystem"), hostRef.getServerGuid());
/* 155 */     return vsanSystem;
/*     */   }
/*     */   
/*     */   public ManagedObjectReference getVsanInternalSystem(ManagedObjectReference hostRef) {
/* 159 */     ManagedObjectReference vsanInternalSystem = new ManagedObjectReference(
/* 160 */         "HostVsanInternalSystem", hostRef.getValue().replace("host", "ha-vsan-internal-system"), hostRef.getServerGuid());
/* 161 */     return vsanInternalSystem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String morefToString(ManagedObjectReference moRef) {
/* 169 */     if (moRef == null) {
/* 170 */       return "";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 175 */     return "urn:vmomi:" + moRef.getType() + ':' + moRef.getValue() + ":" + moRef.getServerGuid();
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
/*     */   public static ManagedObjectReference assignServerGuid(ManagedObjectReference ref, String serverGuid) {
/* 187 */     if (ref.getServerGuid() == null) {
/* 188 */       ref.setServerGuid(serverGuid);
/*     */     }
/* 190 */     return ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ManagedObjectReference[] assignServerGuid(ManagedObjectReference[] refs, String serverGuid) {
/*     */     byte b;
/*     */     int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference;
/* 201 */     for (i = (arrayOfManagedObjectReference = refs).length, b = 0; b < i; ) { ManagedObjectReference ref = arrayOfManagedObjectReference[b];
/* 202 */       assignServerGuid(ref, serverGuid); b++; }
/*     */     
/* 204 */     return refs;
/*     */   }
/*     */   
/*     */   public Class<?> getVimVmodlVersion(String vcGuid) {
/* 208 */     Class<?> result = this.cachedVimVersions.get(vcGuid);
/* 209 */     if (result == null) {
/*     */       try {
/* 211 */         result = getVmodlVersion(vcGuid, "/vimServiceVersions.xml", null);
/* 212 */       } catch (Exception exception) {
/* 213 */         result = versions.VIM_VERSION_STABLE;
/*     */       } 
/* 215 */       this.cachedVimVersions.put(vcGuid, result);
/*     */     } 
/* 217 */     return result;
/*     */   }
/*     */   
/*     */   public Class<?> getVsanVmodlVersion(String vcGuid) {
/* 221 */     Class<?> result = this.cachedVsanVersions.get(vcGuid);
/* 222 */     if (result == null) {
/*     */       try {
/* 224 */         result = getVmodlVersion(vcGuid, "/vsanServiceVersions.xml", "/vimServiceVersions.xml");
/* 225 */       } catch (Exception exception) {
/* 226 */         result = versions.VSAN_VERSION_STABLE;
/*     */       } 
/* 228 */       this.cachedVsanVersions.put(vcGuid, result);
/*     */     } 
/* 230 */     return result;
/*     */   }
/*     */   
/*     */   public Class<?> getVsanDpVmodlVersion(String vcGuid) {
/* 234 */     Class<?> result = this.cachedVsanDpVersions.get(vcGuid);
/* 235 */     if (result == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 241 */       result = version11.class;
/* 242 */       this.cachedVsanDpVersions.put(vcGuid, result);
/*     */     } 
/* 244 */     return result;
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
/*     */   public Class<?> getVmodlVersion(String vcGuid, String primaryVersionFile, String secondaryVersionFile) throws Exception {
/* 258 */     String vcEndpoint = (findInfo(vcGuid)).serviceUrl;
/* 259 */     Set<String> versionKeys = null;
/*     */     try {
/* 261 */       versionKeys = readVmodlVersionKeys(vcEndpoint, primaryVersionFile, secondaryVersionFile);
/* 262 */     } catch (Exception exception) {
/* 263 */       throw new IllegalStateException("Failed to read VMODL version: " + vcEndpoint);
/*     */     } 
/*     */     
/* 266 */     String unstableVersion = getMatchingVersion(versionKeys, "urn:\\w+:u\\w+");
/* 267 */     String stableVersion = getMatchingVersion(versionKeys, "urn:\\w+:s\\w+");
/* 268 */     String releaseVersion = getMatchingVersion(versionKeys, "urn:\\w+:r\\w+");
/*     */     
/* 270 */     for (Class<? extends Annotation> version : SUPPORTED_VERSIONS) {
/* 271 */       String namespace = ((service)version.<service>getAnnotation(service.class)).namespace();
/* 272 */       String versionId = ((versionId)version.<versionId>getAnnotation(versionId.class)).value();
/* 273 */       String versionString = String.format("urn:%s:%s", new Object[] { namespace, versionId });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 280 */       if ((versionId.startsWith("u") && unstableVersion != null) || (
/* 281 */         versionId.startsWith("s") && stableVersion != null) || (
/* 282 */         versionId.startsWith("r") && releaseVersion != null)) {
/* 283 */         return version;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 288 */       if (versionKeys.contains(versionString)) {
/* 289 */         return version;
/*     */       }
/* 291 */       _logger.warn("Version '" + versionString + "' not supported by the server.");
/*     */     } 
/*     */ 
/*     */     
/* 295 */     throw new Exception("No matching VMODL version found");
/*     */   }
/*     */   
/*     */   private String getMatchingVersion(Set<String> versionKeys, String regex) {
/* 299 */     for (String versionKey : versionKeys) {
/* 300 */       if (versionKey.matches(regex)) {
/* 301 */         return versionKey;
/*     */       }
/*     */     } 
/*     */     
/* 305 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getVmodlVersion(String vcGuid, String primaryVersionFile) throws Exception {
/* 316 */     return getVmodlVersion(vcGuid, primaryVersionFile, null); } private ServerInfo findInfo(String vcGuid) {
/*     */     byte b;
/*     */     int i;
/*     */     ServerInfo[] arrayOfServerInfo;
/* 320 */     for (i = (arrayOfServerInfo = (this.userSessionService.getUserSession()).serversInfo).length, b = 0; b < i; ) { ServerInfo info = arrayOfServerInfo[b];
/* 321 */       if (vcGuid.equalsIgnoreCase(info.serviceGuid))
/* 322 */         return info; 
/*     */       b++; }
/*     */     
/* 325 */     throw new IllegalStateException("server info not found: " + vcGuid);
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
/*     */   private Set<String> readVmodlVersionKeys(String vcEndpoint, String primaryVersionFile, String secondaryVersionFile) throws ParserConfigurationException, IOException, SAXException, KeyManagementException, NoSuchAlgorithmException {
/* 339 */     Set<String> versions = new HashSet<>();
/*     */     
/* 341 */     String versionXml = readVmodVersionXml(vcEndpoint, primaryVersionFile, secondaryVersionFile);
/*     */     
/* 343 */     DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 344 */     Document xml = xmlBuilder.parse(new ByteArrayInputStream(versionXml.getBytes()));
/*     */ 
/*     */     
/* 347 */     NodeList namespaces = xml.getElementsByTagName("namespaces").item(0).getChildNodes();
/*     */ 
/*     */     
/* 350 */     if (namespaces.getLength() == 0) {
/* 351 */       throw new IllegalStateException("No namespaces found!");
/*     */     }
/* 353 */     NodeList namespace = namespaces.item(0).getChildNodes();
/*     */     
/* 355 */     String name = null;
/* 356 */     String version = null;
/* 357 */     NodeList priorVersions = null; int i;
/* 358 */     for (i = 0; i < namespace.getLength(); i++) {
/* 359 */       Node property = namespace.item(i);
/* 360 */       String propName = property.getNodeName();
/* 361 */       if (StringUtils.isEmpty(propName)) {
/*     */         
/* 363 */         _logger.warn("Empty property met... strange but let's continue with the next element");
/*     */       } else {
/*     */         String str;
/*     */         
/* 367 */         switch ((str = propName).hashCode()) { case 3373707: if (str.equals("name")) {
/*     */               
/* 369 */               name = property.getTextContent(); break;
/*     */             } 
/*     */           case 351608024:
/* 372 */             if (str.equals("version")) { version = property.getTextContent(); break; }
/*     */           
/*     */           case 1477847941:
/* 375 */             if (str.equals("priorVersions")) { priorVersions = property.getChildNodes(); break; }
/*     */           
/*     */           default:
/* 378 */             _logger.warn("Unknown property met: " + propName); break; }
/*     */       
/*     */       } 
/*     */     } 
/* 382 */     if (name == null || version == null) {
/* 383 */       throw new IllegalStateException("Could not find primary name and version in namespace.");
/*     */     }
/*     */     
/* 386 */     versions.add(createVersion(name, version));
/*     */ 
/*     */     
/* 389 */     if (priorVersions != null && priorVersions.getLength() != 0) {
/* 390 */       for (i = 0; i < priorVersions.getLength(); i++) {
/* 391 */         Node property = priorVersions.item(i);
/* 392 */         String propName = property.getNodeName();
/*     */         
/* 394 */         if (StringUtils.isEmpty(propName)) {
/*     */           
/* 396 */           _logger.warn("Empty property met... strange but let's continue with the next element");
/*     */         }
/*     */         else {
/*     */           
/* 400 */           String v = null; String str1;
/* 401 */           switch ((str1 = propName).hashCode()) { case 351608024: if (str1.equals("version")) {
/*     */                 
/* 403 */                 v = property.getTextContent(); break;
/*     */               } 
/*     */             default:
/* 406 */               _logger.warn("Unknonw property met: " + propName);
/*     */               break; }
/*     */           
/* 409 */           if (StringUtils.isNotEmpty(v)) {
/* 410 */             versions.add(createVersion(name, v));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 415 */     return versions;
/*     */   }
/*     */   
/*     */   private static String createVersion(String name, String versionNumber) {
/* 419 */     return String.valueOf(name) + ":" + versionNumber;
/*     */   }
/*     */ 
/*     */   
/*     */   private String readVmodVersionXml(String vcEndpoint, String primaryVersionFile, String secondaryVersionFile) throws NoSuchAlgorithmException, KeyManagementException, IOException {
/* 424 */     String versionXml = readVersions(String.valueOf(vcEndpoint) + primaryVersionFile);
/* 425 */     if (versionXml == null && secondaryVersionFile != null)
/*     */     {
/*     */       
/* 428 */       versionXml = readVersions(String.valueOf(vcEndpoint) + secondaryVersionFile);
/*     */     }
/* 430 */     if (versionXml == null) {
/* 431 */       throw new IllegalStateException("Failed to read VMODL version: " + vcEndpoint);
/*     */     }
/* 433 */     return versionXml;
/*     */   }
/*     */ 
/*     */   
/*     */   private String readVersions(String serviceUrl) throws NoSuchAlgorithmException, KeyManagementException, IOException {
/* 438 */     HttpsURLConnection conn = null;
/*     */     
/* 440 */     try { conn = NetUtils.createUntrustedConnection(serviceUrl);
/* 441 */       conn.setRequestMethod("GET");
/* 442 */       conn.setUseCaches(false);
/*     */       
/* 444 */       int responseCode = conn.getResponseCode();
/* 445 */       if (NetUtils.isSuccess(responseCode)) {
/* 446 */         String str; StringBuilder result = new StringBuilder();
/* 447 */         Exception exception1 = null, exception2 = null; try { BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
/*     */           try { String line;
/* 449 */             while ((line = in.readLine()) != null)
/* 450 */               result.append(line);  }
/*     */           finally
/* 452 */           { if (in != null) in.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */            }
/*     */       
/*     */       }  }
/* 456 */     finally { conn.disconnect(); }
/*     */     
/* 458 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/util/VmodlHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */