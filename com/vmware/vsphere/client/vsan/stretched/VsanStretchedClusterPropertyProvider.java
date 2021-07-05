/*    */ package com.vmware.vsphere.client.vsan.stretched;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VSANWitnessHostInfo;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanCapabilityUtils;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*    */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VsanStretchedClusterPropertyProvider
/*    */ {
/* 21 */   private static final Log _logger = LogFactory.getLog(VsanStretchedClusterPropertyProvider.class);
/*    */   
/* 23 */   private static final VsanProfiler _profiler = new VsanProfiler(
/* 24 */       VsanStretchedClusterPropertyProvider.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public boolean getIsStretchedClusterSupported(ManagedObjectReference cluster) {
/* 31 */     return VsanCapabilityUtils.isStretchedClusterSupportedOnCluster(cluster);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public List<WitnessHostData> getWitnessHosts(ManagedObjectReference clusterRef) {
/* 39 */     List<WitnessHostData> witnessHosts = new ArrayList<>();
/*    */     
/* 41 */     VsanVcStretchedClusterSystem stretchedClusterSystem = 
/* 42 */       VsanProviderUtils.getVcStretchedClusterSystem(clusterRef);
/*    */     
/* 44 */     if (stretchedClusterSystem != null) {
/* 45 */       VSANWitnessHostInfo[] witnessHostInfos = null; try {
/* 46 */         Exception exception2, exception1 = null;
/*    */       }
/* 48 */       catch (Exception ex) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 56 */         _logger.error("Could not retrieve witness hosts " + ex.getMessage());
/*    */       } 
/*    */       
/* 59 */       if (witnessHostInfos != null) {
/* 60 */         byte b; int i; VSANWitnessHostInfo[] arrayOfVSANWitnessHostInfo; for (i = (arrayOfVSANWitnessHostInfo = witnessHostInfos).length, b = 0; b < i; ) { VSANWitnessHostInfo witnessHost = arrayOfVSANWitnessHostInfo[b];
/* 61 */           if (witnessHost.host != null) {
/*    */ 
/*    */             
/* 64 */             WitnessHostData witness = new WitnessHostData(witnessHost, 
/* 65 */                 clusterRef.getServerGuid());
/* 66 */             witnessHosts.add(witness);
/*    */           }  b++; }
/*    */       
/*    */       } 
/* 70 */     }  return witnessHosts;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/VsanStretchedClusterPropertyProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */