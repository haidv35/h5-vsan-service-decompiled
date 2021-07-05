/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsan.client.util.VmodlHelper;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ 
/*    */ public class DpExploratorySettings
/*    */ {
/*    */   private final String vcUuid;
/*    */   private final VmodlHelper versionLocator;
/*    */   private final VlsiSettings settingsTemplate;
/*    */   private final ManagedObjectReference acquisitionRef;
/*    */   
/*    */   public DpExploratorySettings(ManagedObjectReference acquisitionRef, VmodlHelper versionLocator, VlsiSettings settingsTemplate) {
/* 16 */     this.versionLocator = versionLocator;
/* 17 */     this.settingsTemplate = settingsTemplate;
/* 18 */     this.vcUuid = acquisitionRef.getServerGuid();
/* 19 */     this.acquisitionRef = acquisitionRef;
/*    */   }
/*    */   
/*    */   public VmodlHelper getVersionLocator() {
/* 23 */     return this.versionLocator;
/*    */   }
/*    */   
/*    */   public VlsiSettings getSettingsTemplate() {
/* 27 */     return this.settingsTemplate;
/*    */   }
/*    */   
/*    */   public String getVcUuid() {
/* 31 */     return this.vcUuid;
/*    */   }
/*    */   
/*    */   public ManagedObjectReference getAcquisitionRef() {
/* 35 */     return this.acquisitionRef;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 40 */     if (this == o) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (!(o instanceof DpExploratorySettings)) {
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     DpExploratorySettings that = (DpExploratorySettings)o;
/*    */     
/* 49 */     return this.vcUuid.equals(that.vcUuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 54 */     return this.vcUuid.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpExploratorySettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */