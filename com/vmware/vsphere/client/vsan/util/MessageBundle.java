/*     */ package com.vmware.vsphere.client.vsan.util;
/*     */ 
/*     */ import com.vmware.vise.usersession.UserSessionService;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public final class MessageBundle
/*     */ {
/*     */   private static class UTF8Control
/*     */     extends ResourceBundle.Control
/*     */   {
/*     */     private UTF8Control() {}
/*     */     
/*     */     public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
/* 182 */       String bundleName = toBundleName(baseName, locale);
/* 183 */       ResourceBundle bundle = null;
/* 184 */       if (format.equals("java.class")) {
/*     */         
/*     */         try {
/* 187 */           Class<? extends ResourceBundle> bundleClass = 
/* 188 */             (Class)loader.loadClass(bundleName);
/*     */ 
/*     */ 
/*     */           
/* 192 */           if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
/* 193 */             bundle = bundleClass.newInstance();
/*     */           } else {
/* 195 */             throw new ClassCastException(String.valueOf(bundleClass.getName()) + 
/* 196 */                 " cannot be cast to ResourceBundle");
/*     */           } 
/* 198 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */       }
/* 200 */       else if (format.equals("java.properties")) {
/* 201 */         final String resourceName = toResourceName(bundleName, "properties");
/* 202 */         final ClassLoader classLoader = loader;
/* 203 */         final boolean reloadFlag = reload;
/* 204 */         InputStream stream = null;
/*     */         try {
/* 206 */           stream = AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction<InputStream>() {
/*     */                 public InputStream run() throws IOException {
/* 208 */                   InputStream is = null;
/* 209 */                   if (reloadFlag) {
/* 210 */                     URL url = classLoader.getResource(resourceName);
/* 211 */                     if (url != null) {
/* 212 */                       URLConnection connection = url.openConnection();
/* 213 */                       if (connection != null) {
/*     */ 
/*     */                         
/* 216 */                         connection.setUseCaches(false);
/* 217 */                         is = connection.getInputStream();
/*     */                       } 
/*     */                     } 
/*     */                   } else {
/* 221 */                     is = classLoader.getResourceAsStream(resourceName);
/*     */                   } 
/* 223 */                   return is;
/*     */                 }
/*     */               });
/* 226 */         } catch (PrivilegedActionException e) {
/* 227 */           throw (IOException)e.getException();
/*     */         } 
/* 229 */         if (stream != null) {
/*     */           try {
/* 231 */             bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
/*     */           } finally {
/* 233 */             stream.close();
/*     */           } 
/*     */         }
/*     */       } else {
/* 237 */         throw new IllegalArgumentException("unknown format: " + format);
/*     */       } 
/* 239 */       return bundle;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Locale getFallbackLocale(String baseName, Locale locale) {
/* 250 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final Logger logger = LoggerFactory.getLogger(MessageBundle.class);
/*     */   @Autowired
/*     */   private UserSessionService sessionService;
/*     */   private String bundlePath;
/*     */   
/*     */   public MessageBundle() {
/*     */     this("vsanservice");
/*     */   }
/*     */   
/*     */   public MessageBundle(String bundlePath) {
/*     */     this.bundlePath = bundlePath;
/*     */   }
/*     */   
/*     */   public String string(String key) {
/*     */     return string(key, (Object[])null);
/*     */   }
/*     */   
/*     */   public String string(String key, Object... parameters) {
/*     */     String formatString = loadResourceBundle().getString(key);
/*     */     if (parameters == null || parameters.length == 0)
/*     */       return formatString; 
/*     */     formatString = formatString.replaceAll("'", "''");
/*     */     return MessageFormat.format(formatString, parameters);
/*     */   }
/*     */   
/*     */   public String string(String key, String... parameters) {
/*     */     return string(key, (Object[])parameters);
/*     */   }
/*     */   
/*     */   private ResourceBundle loadResourceBundle() {
/*     */     try {
/*     */       return ResourceBundle.getBundle(this.bundlePath, getCurrentLocale(), getClass().getClassLoader(), new UTF8Control(null));
/*     */     } catch (MissingResourceException e) {
/*     */       throw new IllegalStateException("Cannot load module: " + this.bundlePath, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Locale getCurrentLocale() {
/*     */     Locale locale = Locale.US;
/*     */     try {
/*     */       String languageTag = (this.sessionService.getUserSession()).locale.replaceAll("_", "-");
/*     */       locale = Locale.forLanguageTag(languageTag);
/*     */     } catch (Throwable t) {
/*     */       logger.error("Cannot determine current locale, fallback to default: {}", locale, t);
/*     */     } 
/*     */     return locale;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/util/MessageBundle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */