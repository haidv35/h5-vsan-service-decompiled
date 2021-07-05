/*    */ package com.vmware.vsphere.client.vsan.upgrade;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class VsanVersionInfoPerHost
/*    */ {
/* 17 */   public Map<String, Integer> versions = new HashMap<>(); public VsanVersionInfoPerHost(VsanDiskVersionData[] vsanDiskVersionsData) {
/* 18 */     if (vsanDiskVersionsData == null || vsanDiskVersionsData.length == 0)
/*    */       return;  byte b;
/*    */     int i;
/*    */     VsanDiskVersionData[] arrayOfVsanDiskVersionData;
/* 22 */     for (i = (arrayOfVsanDiskVersionData = vsanDiskVersionsData).length, b = 0; b < i; ) { VsanDiskVersionData versionData = arrayOfVsanDiskVersionData[b];
/* 23 */       String key = String.valueOf(versionData.version);
/* 24 */       if (key != null)
/* 25 */         if (this.versions.containsKey(key)) {
/* 26 */           this.versions.put(key, Integer.valueOf(((Integer)this.versions.get(key)).intValue() + 1));
/*    */         } else {
/* 28 */           this.versions.put(key, Integer.valueOf(1));
/*    */         }  
/*    */       b++; }
/*    */   
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanVersionInfoPerHost.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */