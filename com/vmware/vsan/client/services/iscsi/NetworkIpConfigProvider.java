/*    */ package com.vmware.vsan.client.services.iscsi;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.host.IpConfig;
/*    */ import com.vmware.vim.binding.vim.host.NetworkInfo;
/*    */ import com.vmware.vim.binding.vim.host.VirtualNic;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.target.NetworkIpSetting;
/*    */ import com.vmware.vsphere.client.vsan.util.DataServiceResponse;
/*    */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component
/*    */ public class NetworkIpConfigProvider
/*    */ {
/*    */   private static final String NETWORK_CONFIG_PROPERTY = "config.network";
/*    */   private static final String DHCP_ORIGIN = "dhcp";
/*    */   private static final String SLAAC_ORIGIN = "linklayer";
/*    */   
/*    */   @TsService
/*    */   public List<NetworkIpSetting> getIpSetting(ManagedObjectReference[] hostRefs, String[] vnicNames) throws Exception {
/* 38 */     List<NetworkIpSetting> ipSettingList = new ArrayList<>();
/* 39 */     DataServiceResponse response = QueryUtil.getProperties((Object[])hostRefs, new String[] { "config.network" });
/* 40 */     if (response == null) {
/* 41 */       return ipSettingList;
/*    */     }
/*    */     
/* 44 */     for (int i = 0; i < hostRefs.length; i++) {
/* 45 */       NetworkIpSetting setting = new NetworkIpSetting();
/* 46 */       NetworkInfo networkConfig = (NetworkInfo)((Map)response.getMap().get(hostRefs[i])).get("config.network");
/* 47 */       String vnicName = vnicNames[i]; byte b; int j;
/*    */       VirtualNic[] arrayOfVirtualNic;
/* 49 */       for (j = (arrayOfVirtualNic = networkConfig.vnic).length, b = 0; b < j; ) { VirtualNic vnic = arrayOfVirtualNic[b];
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 54 */         if (!vnicName.equals(vnic.device)) {
/*    */           b++; continue;
/*    */         } 
/* 57 */         if (!StringUtils.isBlank(vnic.spec.ip.ipAddress)) {
/* 58 */           setting.ipV4Address = vnic.spec.ip.ipAddress;
/*    */         }
/* 60 */         if (networkConfig.ipV6Enabled.booleanValue()) {
/* 61 */           setting.ipV6Address = getFormattedIpV6Address(vnic.spec.ip.ipV6Config.ipV6Address);
/*    */         }
/*    */         break; }
/*    */       
/* 65 */       ipSettingList.add(setting);
/*    */     } 
/* 67 */     return ipSettingList;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String getFormattedIpV6Address(IpConfig.IpV6Address[] ipV6Addresses) {
/* 77 */     if (ArrayUtils.isEmpty((Object[])ipV6Addresses))
/* 78 */       return null;  byte b; int i;
/*    */     IpConfig.IpV6Address[] arrayOfIpV6Address;
/* 80 */     for (i = (arrayOfIpV6Address = ipV6Addresses).length, b = 0; b < i; ) { IpConfig.IpV6Address addr = arrayOfIpV6Address[b];
/* 81 */       if ("dhcp".equalsIgnoreCase(addr.origin))
/* 82 */         return String.valueOf(addr.ipAddress) + "/" + addr.prefixLength; 
/* 83 */       if ("linklayer".equalsIgnoreCase(addr.origin))
/* 84 */         return String.valueOf(addr.ipAddress) + "/" + addr.prefixLength; 
/*    */       b++; }
/*    */     
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/iscsi/NetworkIpConfigProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */