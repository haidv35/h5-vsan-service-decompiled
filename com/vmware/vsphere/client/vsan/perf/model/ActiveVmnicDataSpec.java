/*    */ package com.vmware.vsphere.client.vsan.perf.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActiveVmnicDataSpec
/*    */ {
/*    */   public List<ManagedObjectReference> switches;
/*    */   public Map<String, ManagedObjectReference> uuidSwitchMap;
/*    */   public Map<ManagedObjectReference, ManagedObjectReference> switchNetworkMap;
/*    */   public Map<ManagedObjectReference, String[]> networkUplinksMap;
/*    */   
/*    */   public Set<String> getUplinksBySwitchUuid(String uuid) {
/* 23 */     Set<String> uplinks = new HashSet<>();
/*    */     
/* 25 */     ManagedObjectReference switchRef = this.uuidSwitchMap.get(uuid);
/* 26 */     if (switchRef != null) {
/* 27 */       ManagedObjectReference networkRef = this.switchNetworkMap.get(switchRef);
/* 28 */       String[] activeUplink = this.networkUplinksMap.get(networkRef);
/* 29 */       if (!ArrayUtils.isEmpty((Object[])activeUplink)) {
/* 30 */         uplinks.addAll(Arrays.asList(activeUplink));
/*    */       }
/*    */     } 
/*    */     
/* 34 */     return uplinks;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/ActiveVmnicDataSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */