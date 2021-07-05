/*    */ package com.vmware.vsan.client.services.stretchedcluster;
/*    */ 
/*    */ import com.vmware.vise.core.model.data;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class DomainOrHostData
/*    */ {
/*    */   public String uid;
/*    */   public String label;
/*    */   public boolean isHost;
/*    */   public String iconId;
/*    */   public boolean isPreferred;
/*    */   public boolean inMaintenanceMode;
/*    */   public DomainOrHostData[] children;
/*    */   
/*    */   public static DomainOrHostData createHostData(String uid, String label, String iconId, boolean inMaintenanceMode) {
/* 22 */     DomainOrHostData result = new DomainOrHostData();
/* 23 */     result.uid = uid;
/* 24 */     result.label = label;
/* 25 */     result.isHost = true;
/* 26 */     result.iconId = iconId;
/* 27 */     result.inMaintenanceMode = inMaintenanceMode;
/* 28 */     result.isPreferred = false;
/* 29 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public static DomainOrHostData createDomainData(String uid, String label, boolean isPreferred, List<DomainOrHostData> children) {
/* 34 */     DomainOrHostData result = new DomainOrHostData();
/* 35 */     result.uid = uid;
/* 36 */     result.label = label;
/* 37 */     result.isHost = false;
/* 38 */     result.children = children.<DomainOrHostData>toArray(new DomainOrHostData[children.size()]);
/* 39 */     result.iconId = "vsan-fault-domain";
/* 40 */     result.isPreferred = isPreferred;
/* 41 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 46 */     if (this == o) return true; 
/* 47 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 49 */     DomainOrHostData that = (DomainOrHostData)o;
/*    */     
/* 51 */     return this.uid.equals(that.uid);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 56 */     return this.uid.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/stretchedcluster/DomainOrHostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */