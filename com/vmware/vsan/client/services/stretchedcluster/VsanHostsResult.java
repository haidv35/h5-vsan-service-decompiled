/*    */ package com.vmware.vsan.client.services.stretchedcluster;
/*    */ 
/*    */ import com.vmware.vim.binding.vim.HostSystem;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VSANWitnessHostInfo;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vise.data.query.PropertyValue;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanHostsResult
/*    */ {
/*    */   private final PropertyValue[] hostData;
/*    */   private final VSANWitnessHostInfo[] witnessHostInfos;
/*    */   public final Set<ManagedObjectReference> members;
/*    */   public final Set<ManagedObjectReference> connectedMembers;
/*    */   public final Set<ManagedObjectReference> witnesses;
/*    */   
/*    */   public VsanHostsResult() {
/* 25 */     this(new PropertyValue[0], new VSANWitnessHostInfo[0]);
/*    */   }
/*    */   
/*    */   public VsanHostsResult(PropertyValue[] hostData, VSANWitnessHostInfo[] witnessHostInfos) {
/* 29 */     this.hostData = hostData;
/* 30 */     this.witnessHostInfos = witnessHostInfos;
/*    */     
/* 32 */     Set<ManagedObjectReference> members = new HashSet<>();
/* 33 */     Set<ManagedObjectReference> connectedMembers = new HashSet<>();
/* 34 */     Set<ManagedObjectReference> witnesses = new HashSet<>(); byte b; int i;
/*    */     PropertyValue[] arrayOfPropertyValue;
/* 36 */     for (i = (arrayOfPropertyValue = hostData).length, b = 0; b < i; ) { PropertyValue val = arrayOfPropertyValue[b];
/* 37 */       if (val.propertyName.equals("runtime.connectionState")) {
/* 38 */         ManagedObjectReference hostRef = (ManagedObjectReference)val.resourceObject;
/* 39 */         members.add(hostRef);
/* 40 */         if (HostSystem.ConnectionState.connected.equals(val.value)) {
/* 41 */           connectedMembers.add(hostRef);
/*    */         }
/*    */       } 
/*    */       b++; }
/*    */     
/* 46 */     if (witnessHostInfos != null) {
/* 47 */       for (int j = 0; j < witnessHostInfos.length; j++) {
/* 48 */         witnesses.add((witnessHostInfos[j]).host);
/*    */       }
/*    */     }
/*    */     
/* 52 */     this.members = members;
/* 53 */     this.connectedMembers = connectedMembers;
/* 54 */     this.witnesses = witnesses;
/*    */   }
/*    */   
/*    */   public VSANWitnessHostInfo[] getWitnessInfos() {
/* 58 */     return this.witnessHostInfos;
/*    */   }
/*    */   
/*    */   public Set<ManagedObjectReference> getAll() {
/* 62 */     Set<ManagedObjectReference> result = new HashSet<>();
/* 63 */     result.addAll(this.members);
/* 64 */     result.addAll(this.witnesses);
/* 65 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/stretchedcluster/VsanHostsResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */