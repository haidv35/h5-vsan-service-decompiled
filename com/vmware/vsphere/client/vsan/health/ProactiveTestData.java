/*    */ package com.vmware.vsphere.client.vsan.health;
/*    */ 
/*    */ import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
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
/*    */ @data
/*    */ public class ProactiveTestData
/*    */   extends DataObjectImpl
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public VsanTestData generalData;
/*    */   public Long timestamp;
/*    */   public PerfTestType perfTestType;
/*    */   public String helpId;
/*    */   public ManagedObjectReference taskMoRef;
/*    */   
/*    */   public ProactiveTestData() {
/* 27 */     this.taskMoRef = null;
/*    */   }
/*    */   
/*    */   @data
/*    */   public enum PerfTestType {
/*    */     vmCreation, multicast, unicast;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/ProactiveTestData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */