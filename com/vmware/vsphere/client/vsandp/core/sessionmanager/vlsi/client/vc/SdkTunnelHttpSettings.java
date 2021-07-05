/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc;
/*    */ 
/*    */ import com.vmware.vim.vmomi.client.http.ThumbprintVerifier;
/*    */ import com.vmware.vim.vmomi.core.types.VmodlContext;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.ResourceFactory;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.HttpSettings;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.http.LenientThumbprintVerifier;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.CloseableExecutorService;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.executor.ExecutorSettings;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SdkTunnelHttpSettings
/*    */   extends HttpSettings
/*    */ {
/*    */   public static final int PROXY_PORT = 80;
/*    */   public static final String TUNNEL_HOST = "sdkTunnel";
/*    */   public static final int TUNNEL_PORT = 8089;
/*    */   
/*    */   public SdkTunnelHttpSettings(String vcAddress, ResourceFactory<CloseableExecutorService, ExecutorSettings> executorMgr, ExecutorSettings executorSettings, Class<?> version, VmodlContext vmodlContext) {
/* 45 */     this(vcAddress, 80, executorMgr, executorSettings, version, vmodlContext);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SdkTunnelHttpSettings(String vcAddress, int vcProxyPort, ResourceFactory<CloseableExecutorService, ExecutorSettings> executorMgr, ExecutorSettings executorSettings, Class<?> version, VmodlContext vmodlContext) {
/* 64 */     super("https", "sdkTunnel", 8089, null, "http", vcAddress, vcProxyPort, -1, -1, null, null, (ThumbprintVerifier)new LenientThumbprintVerifier(), executorMgr, executorSettings, version, vmodlContext, null);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SdkTunnelHttpSettings(String vcProxyHost, int vcProxyPort, int maxConn, int timeout, ThumbprintVerifier verifier, ResourceFactory<CloseableExecutorService, ExecutorSettings> executorMgr, ExecutorSettings executorSettings, Class<?> version, VmodlContext vmodlContext) {
/* 85 */     super("https", "sdkTunnel", 8089, null, "http", vcProxyHost, vcProxyPort, maxConn, timeout, null, null, verifier, executorMgr, executorSettings, version, vmodlContext, null);
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/vlsi/client/vc/SdkTunnelHttpSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */