/*    */ package com.vmware.vsan.client.services.resyncing;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.base.data.IscsiLun;
/*    */ import com.vmware.vsphere.client.vsan.base.data.IscsiTarget;
/*    */ import com.vmware.vsphere.client.vsan.base.data.VsanObject;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.VsanIscsiTargetProviderParameter;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.providers.VsanIscsiTargetPropertyProvider;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class VsanResyncingIscsiTargetComponentsProvider
/*    */ {
/*    */   private static final String ISCSI_ROOT_VALUE = "iSCSI Objects";
/* 26 */   private static final Log _logger = LogFactory.getLog(VsanResyncingIscsiTargetComponentsProvider.class);
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private VsanIscsiTargetPropertyProvider iscsiTargetPropertyProvider;
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, VsanObject> getIscsiResyncObjects(ManagedObjectReference clusterRef, Set<String> resyncObjectsUuids) {
/* 36 */     VsanIscsiTargetProviderParameter iscsiTargetsParam = 
/* 37 */       new VsanIscsiTargetProviderParameter(false, false);
/*    */     try {
/* 39 */       IscsiTarget[] iscsiTargets = this.iscsiTargetPropertyProvider.getIscsiTargets(clusterRef, iscsiTargetsParam);
/*    */ 
/*    */       
/* 42 */       return getResyncingIscsiTargets(iscsiTargets, resyncObjectsUuids);
/* 43 */     } catch (Exception e) {
/*    */       
/* 45 */       _logger.error("Unable to fetch the iscsi targets from cluster " + clusterRef, e);
/* 46 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Map<String, VsanObject> getResyncingIscsiTargets(IscsiTarget[] iscsiTargets, Set<String> resyncObjectsUuids) throws Exception {
/* 59 */     Map<String, VsanObject> result = new HashMap<>();
/*    */     byte b;
/*    */     int i;
/*    */     IscsiTarget[] arrayOfIscsiTarget;
/* 63 */     for (i = (arrayOfIscsiTarget = iscsiTargets).length, b = 0; b < i; ) { IscsiTarget iscsiTarget = arrayOfIscsiTarget[b];
/* 64 */       if (resyncObjectsUuids.contains(iscsiTarget.vsanObjectUuid)) {
/* 65 */         result.put(iscsiTarget.vsanObjectUuid, iscsiTarget);
/*    */       }
/*    */       
/* 68 */       if (iscsiTarget.lunCount.intValue() != 0)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 74 */         for (int j = iscsiTarget.luns.size(); j > 0; j--) {
/* 75 */           IscsiLun lun = iscsiTarget.luns.get(j - 1);
/* 76 */           if (resyncObjectsUuids.contains(lun.vsanObjectUuid)) {
/*    */             
/* 78 */             result.put(iscsiTarget.vsanObjectUuid, iscsiTarget);
/*    */             
/* 80 */             result.put(lun.vsanObjectUuid, lun);
/*    */           } else {
/*    */             
/* 83 */             iscsiTarget.luns.remove(j - 1);
/*    */           } 
/*    */         }  }  b++; }
/*    */     
/* 87 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/VsanResyncingIscsiTargetComponentsProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */