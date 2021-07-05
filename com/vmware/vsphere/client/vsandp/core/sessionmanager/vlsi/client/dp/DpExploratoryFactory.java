/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.dp;
/*    */ 
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.VlsiSettings;
/*    */ 
/*    */ public class DpExploratoryFactory
/*    */   implements ResourceFactory<DpConnection, DpExploratorySettings> {
/*    */   private final ResourceFactory<DpConnection, VlsiSettings> producerFactory;
/*    */   
/*    */   public DpExploratoryFactory(ResourceFactory<DpConnection, VlsiSettings> producerFactory) {
/* 12 */     this.producerFactory = producerFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   public DpConnection acquire(DpExploratorySettings settings) {
/* 17 */     Class<?> versionClass = settings.getVersionLocator().getVsanDpVmodlVersion(settings.getVcUuid());
/* 18 */     VlsiSettings builder = settings.getSettingsTemplate();
/* 19 */     VlsiSettings dpSettings = builder.setHttpSettings(builder.getHttpSettings().setVersion(versionClass));
/* 20 */     return (DpConnection)this.producerFactory.acquire(dpSettings);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/dp/DpExploratoryFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */