/*     */ package com.vmware.vsan.client.services.update;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.health.HclUpdateOfflineSpec;
/*     */ import com.vmware.vsphere.client.vsan.impl.VsanPropertyProvider;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.Inflater;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ import sun.misc.BASE64Encoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class UpdateDbService
/*     */ {
/*  34 */   private static final VsanProfiler profiler = new VsanProfiler(UpdateDbService.class);
/*  35 */   private static final Log _logger = LogFactory.getLog(UpdateDbService.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private VsanPropertyProvider vsanPropertyProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public Date getHclLastUpdatedDate(ManagedObjectReference vcRef) throws Exception {
/*  49 */     ManagedObjectReference clusterRef = null;
/*  50 */     if (!VsanCapabilityUtils.isGetHclLastUpdateOnVcSupported(vcRef)) {
/*  51 */       clusterRef = this.vsanPropertyProvider.getAnyVsanCluster(vcRef);
/*  52 */       if (clusterRef == null) {
/*  53 */         _logger.warn("Cannot find a cluster on the VC: " + vcRef);
/*  54 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     VsanVcClusterHealthSystem healthSystem = VsanProviderUtils.getVsanVcClusterHealthSystem(vcRef);
/*  59 */     if (healthSystem == null) {
/*  60 */       return null;
/*     */     }
/*     */     
/*  63 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/*  69 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } @TsService
/*     */   public Date getReleaseCatalogLastUpdatedDate(ManagedObjectReference moRef) throws Exception {
/*  74 */     Exception exception1 = null, exception2 = null;
/*     */     try {
/*     */     
/*     */     } finally {
/*  78 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void updateHclDbFromWeb(ManagedObjectReference entity) throws Exception {
/*  88 */     Exception exception1 = null, exception2 = null;
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
/*     */   @TsService
/*     */   public boolean uploadHclDb(ManagedObjectReference entity, HclUpdateOfflineSpec spec) throws Exception {
/* 102 */     Exception exception1 = null, exception2 = null;
/*     */     try {
/*     */     
/*     */     } finally {
/* 106 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public void uploadReleaseDb(ManagedObjectReference entity, byte[] data) throws Exception {
/* 117 */     if (!VsanCapabilityUtils.isUpdateVumReleaseCatalogOfflineSupported(entity)) {
/* 118 */       String message = Utils.getLocalizedString("vsan.common.error.notSupported");
/* 119 */       throw new Exception(message);
/*     */     } 
/*     */     
/* 122 */     Exception exception1 = null, exception2 = null;
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
/*     */   private Date convertUtcDate(Calendar date) {
/* 136 */     if (date != null) {
/* 137 */       date.add(14, date.getTimeZone().getRawOffset());
/*     */     }
/* 139 */     return (date == null) ? null : date.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateGzipBase64EncodedString(byte[] fileByteArray) throws Exception {
/* 150 */     Validate.notNull(fileByteArray);
/*     */ 
/*     */     
/* 153 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 154 */     GZIPOutputStream gzipos = new GZIPOutputStream(baos);
/*     */     try {
/* 156 */       gzipos.write(fileByteArray);
/* 157 */     } catch (Exception e) {
/* 158 */       throw e;
/*     */     } finally {
/* 160 */       gzipos.close();
/* 161 */       baos.close();
/*     */     } 
/*     */     
/* 164 */     BASE64Encoder encoder = new BASE64Encoder();
/* 165 */     return encoder.encode(baos.toByteArray());
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
/*     */   private byte[] decompressZlibByteArrayString(byte[] zlibByteArray) throws DataFormatException, IOException {
/* 178 */     ByteArrayOutputStream baos = 
/* 179 */       new ByteArrayOutputStream(zlibByteArray.length);
/* 180 */     Inflater decompressor = new Inflater();
/*     */     
/*     */     try {
/* 183 */       decompressor.setInput(zlibByteArray);
/*     */       
/* 185 */       byte[] buf = new byte[1024];
/* 186 */       while (!decompressor.finished()) {
/* 187 */         int count = decompressor.inflate(buf);
/* 188 */         if (count == 0 && decompressor.needsInput()) {
/* 189 */           throw new DataFormatException(Utils.getLocalizedString("vsan.update.catalog.content.error"));
/*     */         }
/* 191 */         baos.write(buf, 0, count);
/*     */       } 
/*     */     } finally {
/* 194 */       decompressor.end();
/* 195 */       baos.close();
/*     */     } 
/* 197 */     return baos.toByteArray();
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/update/UpdateDbService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */