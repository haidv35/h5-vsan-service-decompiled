/*     */ package com.vmware.vsan.client.services.hci.model;
/*     */ 
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.host.IpConfig;
/*     */ import com.vmware.vim.binding.vim.host.IpRouteConfig;
/*     */ import com.vmware.vim.binding.vim.host.VirtualNic;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vise.core.model.data;
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
/*     */ @data
/*     */ public class NetServiceConfig
/*     */ {
/*     */   public Service service;
/*     */   public boolean useVlan;
/*     */   public int vlan;
/*     */   public String dvpgName;
/*     */   public ManagedObjectReference existingDvpgMor;
/*     */   public Protocol protocol;
/*     */   public IpType ipv4IpType;
/*     */   public HostIpv4Config[] hostIpv4Configs;
/*     */   public IpType ipv6IpType;
/*     */   public HostIpv6Config[] hostIpv6Configs;
/*     */   
/*     */   public ClusterComputeResource.HostVmkNicInfo getHostVmkNicInfo(String hostName) {
/*  35 */     ClusterComputeResource.HostVmkNicInfo result = new ClusterComputeResource.HostVmkNicInfo();
/*  36 */     result.service = this.service.getText();
/*  37 */     result.nicSpec = new VirtualNic.Specification();
/*  38 */     result.nicSpec.ip = getIpConfig(hostName);
/*  39 */     result.nicSpec.ipRouteSpec = getIpRouteSpec(hostName);
/*     */     
/*  41 */     return result;
/*     */   }
/*     */   
/*     */   private IpConfig getIpConfig(String hostName) {
/*  45 */     IpConfig result = new IpConfig();
/*     */     
/*  47 */     if (this.protocol == Protocol.IPV4 || this.protocol == Protocol.MIXED) {
/*  48 */       result.dhcp = (this.ipv4IpType == IpType.DHCP);
/*     */       
/*  50 */       if (!result.dhcp) {
/*  51 */         HostIpv4Config ipv4Config = getHostIpv4Config(hostName);
/*  52 */         result.ipAddress = ipv4Config.ipAddress;
/*  53 */         result.subnetMask = ipv4Config.subnetMask;
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     if (this.protocol == Protocol.IPV6 || this.protocol == Protocol.MIXED) {
/*  58 */       IpConfig.IpV6Address ipv6Address; HostIpv6Config ipv6Config; result.ipV6Config = new IpConfig.IpV6AddressConfiguration();
/*     */       
/*  60 */       switch (this.ipv6IpType) {
/*     */         case null:
/*  62 */           result.ipV6Config.dhcpV6Enabled = Boolean.valueOf(true);
/*     */           break;
/*     */         case ROUTER_ADVERTISEMENT:
/*  65 */           result.ipV6Config.autoConfigurationEnabled = Boolean.valueOf(true);
/*     */           break;
/*     */         
/*     */         case STATIC:
/*  69 */           result.ipV6Config.ipV6Address = new IpConfig.IpV6Address[] { new IpConfig.IpV6Address() };
/*  70 */           result.ipV6Config.autoConfigurationEnabled = Boolean.valueOf(false);
/*  71 */           ipv6Address = result.ipV6Config.ipV6Address[0];
/*     */           
/*  73 */           ipv6Config = getHostIpv6Config(hostName);
/*  74 */           ipv6Address.ipAddress = ipv6Config.ipAddress;
/*  75 */           ipv6Address.prefixLength = ipv6Config.prefixLength;
/*  76 */           ipv6Address.operation = "add";
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   private HostIpv4Config getHostIpv4Config(String hostName) {
/*  85 */     if (this.hostIpv4Configs != null) {
/*  86 */       byte b; int i; HostIpv4Config[] arrayOfHostIpv4Config; for (i = (arrayOfHostIpv4Config = this.hostIpv4Configs).length, b = 0; b < i; ) { HostIpv4Config config = arrayOfHostIpv4Config[b];
/*  87 */         if (config.hostname.equals(hostName)) {
/*  88 */           return config;
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   private HostIpv6Config getHostIpv6Config(String hostName) {
/*  97 */     if (this.hostIpv6Configs != null) {
/*  98 */       byte b; int i; HostIpv6Config[] arrayOfHostIpv6Config; for (i = (arrayOfHostIpv6Config = this.hostIpv6Configs).length, b = 0; b < i; ) { HostIpv6Config config = arrayOfHostIpv6Config[b];
/*  99 */         if (config.hostname.equals(hostName)) {
/* 100 */           return config;
/*     */         }
/*     */         b++; }
/*     */     
/*     */     } 
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   private VirtualNic.IpRouteSpec getIpRouteSpec(String hostName) {
/* 109 */     VirtualNic.IpRouteSpec result = new VirtualNic.IpRouteSpec();
/* 110 */     result.ipRouteConfig = new IpRouteConfig();
/*     */     
/* 112 */     if ((this.protocol == Protocol.IPV4 || this.protocol == Protocol.MIXED) && this.ipv4IpType != IpType.DHCP) {
/* 113 */       result.ipRouteConfig.defaultGateway = (getHostIpv4Config(hostName)).defaultGateway;
/*     */     }
/*     */     
/* 116 */     if ((this.protocol == Protocol.IPV6 || this.protocol == Protocol.MIXED) && this.ipv6IpType == IpType.STATIC) {
/* 117 */       result.ipRouteConfig.ipV6DefaultGateway = (getHostIpv6Config(hostName)).defaultGateway;
/*     */     }
/*     */     
/* 120 */     return result;
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum Protocol {
/* 125 */     IPV4,
/* 126 */     IPV6,
/* 127 */     MIXED;
/*     */   }
/*     */   
/*     */   @data
/*     */   public enum IpType {
/* 132 */     STATIC,
/* 133 */     DHCP,
/* 134 */     ROUTER_ADVERTISEMENT;
/*     */   }
/*     */   
/*     */   @data
/*     */   public static class HostIpv4Config {
/*     */     public String hostname;
/*     */     public String ipAddress;
/*     */     public String subnetMask;
/*     */     public String defaultGateway;
/*     */   }
/*     */   
/*     */   @data
/*     */   public static class HostIpv6Config {
/*     */     public String hostname;
/*     */     public String ipAddress;
/*     */     public int prefixLength;
/*     */     public String defaultGateway;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/NetServiceConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */