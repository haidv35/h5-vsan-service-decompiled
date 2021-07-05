/*     */ package com.vmware.vsphere.client.vsandp.helper;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.Datacenter;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.services.common.PermissionService;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ @Component
/*     */ public class VsanDpInventoryHelper
/*     */ {
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   @Autowired
/*     */   PermissionService permissionService;
/*     */   
/*     */   public ManagedObjectReference getVmFolderOfDataCenter(ManagedObjectReference target) {
/*  41 */     if (this.vmodlHelper.isOfType(target, Datacenter.class)) {
/*  42 */       Exception exception1 = null, exception2 = null; try {  }
/*     */       finally
/*  44 */       { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */          }
/*     */     
/*  47 */     }  return target;
/*     */   }
/*     */   
/*     */   public ManagedObjectReference getVmFolder(ManagedObjectReference vmRef) {
/*  51 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/*  66 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } public ManagedObjectReference getVmResourcePool(ManagedObjectReference vmRef) {
/*  70 */     Exception exception1 = null, exception2 = null;
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
/*     */     try {
/*     */     
/*     */     } finally {
/*  84 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } private boolean isDrsEnabledOnSourceCluster(ManagedObjectReference vmRef) {
/*  88 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 101 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public ManagedObjectReference getVmCluster(ManagedObjectReference vmRef) {
/*     */     try {
/* 110 */       return (ManagedObjectReference)QueryUtil.getProperty(vmRef, "cluster");
/* 111 */     } catch (Exception exception) {
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ManagedObjectReference getVmDc(ManagedObjectReference vmRef) {
/* 117 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 126 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } public ManagedObjectReference getVmDatastore(ManagedObjectReference vmRef) throws Exception {
/* 130 */     PropertyValue[] vmDatastores = QueryUtil.getPropertiesForRelatedObjects(
/* 131 */         vmRef, 
/* 132 */         "datastore", 
/* 133 */         "datastore", 
/* 134 */         new String[] { "summary.type"
/* 135 */         }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 136 */     for (i = (arrayOfPropertyValue1 = vmDatastores).length, b = 0; b < i; ) { PropertyValue datastore = arrayOfPropertyValue1[b];
/* 137 */       if (datastore.propertyName.equals("summary.type") && datastore.value.equals("vsan"))
/* 138 */         return (ManagedObjectReference)datastore.resourceObject; 
/*     */       b++; }
/*     */     
/* 141 */     throw new IllegalArgumentException("vSAN datastore not found for the given VM.");
/*     */   }
/*     */   
/*     */   public ManagedObjectReference getVmNetwork(ManagedObjectReference vmRef) {
/* 145 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 154 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   }
/*     */   public boolean isVmRestoreAllowed(ManagedObjectReference vmRef) throws Exception {
/* 159 */     ManagedObjectReference clusterRef = getVmCluster(vmRef);
/* 160 */     boolean hasVcPermissions = this.permissionService.hasVcPermissions(
/* 161 */         clusterRef, new String[] { "StorageProfile.View", "System.Read" });
/* 162 */     ManagedObjectReference vmDatastore = getVmDatastore(vmRef);
/* 163 */     boolean hasDatastorePermissions = this.permissionService.hasPermissions(vmDatastore, 
/* 164 */         new String[] { "Vsan.DataProtection.Management" });
/* 165 */     return (hasVcPermissions && hasDatastorePermissions);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/helper/VsanDpInventoryHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */