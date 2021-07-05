/*    */ package com.vmware.vsphere.client.vsan.stretched;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VSANWitnessHostInfo;
/*    */ import com.vmware.vise.core.model.data;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class WitnessHostData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public ManagedObjectReference witnessHost;
/*    */   public String preferredFaultDomainName;
/*    */   public String unicastAgentAddress;
/*    */   
/*    */   public WitnessHostData() {}
/*    */   
/*    */   public WitnessHostData(VSANWitnessHostInfo witnessInfo, String serverGuid) {
/* 37 */     this.witnessHost = witnessInfo.host;
/* 38 */     this.witnessHost.setServerGuid(serverGuid);
/* 39 */     this.preferredFaultDomainName = witnessInfo.preferredFdName;
/* 40 */     this.unicastAgentAddress = witnessInfo.unicastAgentAddr;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/WitnessHostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */