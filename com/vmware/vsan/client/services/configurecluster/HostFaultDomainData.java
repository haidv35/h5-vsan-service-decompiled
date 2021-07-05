/*    */ package com.vmware.vsan.client.services.configurecluster;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsan.client.services.common.data.ConnectionState;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class HostFaultDomainData
/*    */ {
/*    */   public String hostUid;
/*    */   public String name;
/*    */   public String primaryIconId;
/*    */   public String version;
/*    */   public String faultDomainName;
/*    */   public ConnectionState connectionState;
/*    */   public boolean canChangeFaultDomain;
/*    */   public boolean isFaultDomainSupported;
/*    */   public boolean hasEditPrivileges = true;
/*    */   
/*    */   public static HostFaultDomainData createHostFaultDomainData(String hostUid, String name, String primaryIconId, ConnectionState connectionState, String faultDomainName, String version) {
/* 28 */     HostFaultDomainData result = new HostFaultDomainData();
/* 29 */     result.hostUid = hostUid;
/* 30 */     result.name = name;
/* 31 */     result.primaryIconId = primaryIconId;
/* 32 */     result.connectionState = connectionState;
/* 33 */     result.faultDomainName = faultDomainName;
/* 34 */     result.version = version;
/* 35 */     result.canChangeFaultDomain = canChangeFaultDomain(connectionState, result.hasEditPrivileges, version);
/* 36 */     result.isFaultDomainSupported = isFaultDomainSupported(version);
/* 37 */     return result;
/*    */   }
/*    */   
/*    */   private static boolean canChangeFaultDomain(ConnectionState connectionState, boolean hasEditPrivileges, String version) {
/* 41 */     boolean isHostConnected = (connectionState == ConnectionState.connected);
/* 42 */     return (isFaultDomainSupported(version) && isHostConnected && hasEditPrivileges);
/*    */   }
/*    */   
/*    */   private static boolean isFaultDomainSupported(String version) {
/* 46 */     if (StringUtils.isEmpty(version)) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     String[] versionNumbers = version.split("\\.");
/*    */     
/* 52 */     return !(!version.equals("e.x.p") && (
/* 53 */       versionNumbers.length <= 0 || 
/* 54 */       !StringUtils.isNotEmpty(versionNumbers[0]) || 
/* 55 */       Integer.parseInt(versionNumbers[0]) <= 5));
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private String hostUid;
/*    */     private String name;
/*    */     private String primaryIconId;
/*    */     private String version;
/*    */     private String faultDomainName;
/*    */     private ConnectionState connectionState;
/*    */     
/*    */     public Builder hostUid(String hostUid) {
/* 67 */       this.hostUid = hostUid;
/* 68 */       return this;
/*    */     }
/*    */     
/*    */     public Builder name(String name) {
/* 72 */       this.name = name;
/* 73 */       return this;
/*    */     }
/*    */     
/*    */     public Builder primaryIconId(String primaryIconId) {
/* 77 */       this.primaryIconId = primaryIconId;
/* 78 */       return this;
/*    */     }
/*    */     
/*    */     public Builder version(String version) {
/* 82 */       this.version = version;
/* 83 */       return this;
/*    */     }
/*    */     
/*    */     public Builder faultDomainName(String faultDomainName) {
/* 87 */       this.faultDomainName = faultDomainName;
/* 88 */       return this;
/*    */     }
/*    */     
/*    */     public Builder connectionState(ConnectionState connectionState) {
/* 92 */       this.connectionState = connectionState;
/* 93 */       return this;
/*    */     }
/*    */     
/*    */     public HostFaultDomainData createHostFaultDomainData() {
/* 97 */       return HostFaultDomainData.createHostFaultDomainData(this.hostUid, this.name, this.primaryIconId, this.connectionState, this.faultDomainName, this.version);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/configurecluster/HostFaultDomainData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */