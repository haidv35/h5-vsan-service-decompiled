/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.sso.explorer;
/*    */ 
/*    */ import com.vmware.vim.binding.lookup.ServiceRegistration;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractLsExplorer<R>
/*    */ {
/*    */   private final ServiceRegistration lookupService;
/*    */   
/*    */   public AbstractLsExplorer(ServiceRegistration lookupService) {
/* 28 */     this.lookupService = lookupService;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public R get(UUID uuid) {
/* 38 */     R result = map().get(uuid);
/* 39 */     if (result != null) {
/* 40 */       return result;
/*    */     }
/* 42 */     throw new IllegalStateException("Service registration not found: " + uuid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<R> list() {
/* 50 */     return new HashSet<>(map().values());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<UUID, R> map() {
/* 59 */     ServiceRegistration.Info[] list = this.lookupService.list(getFilter());
/* 60 */     if (list == null || list.length == 0) {
/* 61 */       return Collections.emptyMap();
/*    */     }
/* 63 */     Map<UUID, R> result = new HashMap<>(); byte b; int i; ServiceRegistration.Info[] arrayOfInfo1;
/* 64 */     for (i = (arrayOfInfo1 = list).length, b = 0; b < i; ) { ServiceRegistration.Info registration = arrayOfInfo1[b];
/* 65 */       mapRegistration(createRegistration(registration), result); b++; }
/*    */     
/* 67 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract R createRegistration(ServiceRegistration.Info paramInfo);
/*    */   
/*    */   protected abstract void mapRegistration(R paramR, Map<UUID, R> paramMap);
/*    */   
/*    */   protected abstract ServiceRegistration.Filter getFilter();
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/sso/explorer/AbstractLsExplorer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */