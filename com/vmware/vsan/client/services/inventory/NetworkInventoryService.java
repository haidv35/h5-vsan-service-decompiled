/*     */ package com.vmware.vsan.client.services.inventory;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vim.Network;
/*     */ import com.vmware.vim.binding.vim.Tag;
/*     */ import com.vmware.vim.binding.vim.dvs.DistributedVirtualPortgroup;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ @Component
/*     */ public class NetworkInventoryService
/*     */   extends InventoryBrowserService
/*     */ {
/*     */   private static final String UPLINK_KEY = "SYSTEM/DVS.UPLINKPG";
/*     */   @Autowired
/*     */   private VcClient vcClient;
/*     */   @Autowired
/*     */   private VmodlHelper vmodlHelper;
/*     */   
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeInfo(ManagedObjectReference[] nodeRefs) throws Exception {
/*  41 */     return super.getNodeInfo(nodeRefs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public InventoryEntryData[] getNodeChildren(ManagedObjectReference parentNode) throws Exception {
/*  48 */     return super.getNodeChildren(parentNode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ManagedObjectReference> listChildrenRefs(ManagedObjectReference parent) {
/*  53 */     Exception exception1 = null, exception2 = null;
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
/*     */   protected boolean isLeafNode(ManagedObjectReference item) {
/*  70 */     return this.vmodlHelper.isOfType(item, Network.class);
/*     */   }
/*     */   
/*     */   private List<ManagedObjectReference> filterChildren(ManagedObjectReference[] allChildren) {
/*  74 */     List<ManagedObjectReference> result = new ArrayList<>(); byte b; int i; ManagedObjectReference[] arrayOfManagedObjectReference;
/*  75 */     for (i = (arrayOfManagedObjectReference = allChildren).length, b = 0; b < i; ) { ManagedObjectReference childRef = arrayOfManagedObjectReference[b];
/*  76 */       if (DistributedVirtualPortgroup.class.isAssignableFrom(this.vmodlHelper.getTypeClass(childRef))) {
/*  77 */         Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  83 */       else if (this.vmodlHelper.isNetworkFolder(childRef) || 
/*  84 */         Network.class.isAssignableFrom(this.vmodlHelper.getTypeClass(childRef))) {
/*  85 */         result.add(childRef);
/*     */       }  b++; }
/*     */     
/*  88 */     return result;
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
/*     */   private static boolean searchTagsForKey(Tag[] tags, String key) {
/* 100 */     if (ArrayUtils.isEmpty((Object[])tags) || StringUtils.isEmpty(key))
/* 101 */       return false;  byte b;
/*     */     int i;
/*     */     Tag[] arrayOfTag;
/* 104 */     for (i = (arrayOfTag = tags).length, b = 0; b < i; ) { Tag tag = arrayOfTag[b];
/* 105 */       if (key.equals(tag.key)) {
/* 106 */         return true;
/*     */       }
/*     */       b++; }
/*     */     
/* 110 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/NetworkInventoryService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */