/*    */ package com.vmware.vsan.client.services.dataprotection.model;
/*    */ 
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vise.core.model.data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class DatastoreData
/*    */ {
/*    */   public ManagedObjectReference mor;
/*    */   public String name;
/*    */   public String primaryIconId;
/*    */   public Boolean isNfs;
/*    */   public Type type;
/*    */   public Long capacity;
/*    */   public Long freeSpace;
/*    */   public String url;
/*    */   
/*    */   @data
/*    */   public enum Type
/*    */   {
/* 26 */     NFS_3("NFS"),
/* 27 */     NFS_41("NFS41"),
/* 28 */     VMFS("VMFS"),
/* 29 */     VVOL("VVOL"),
/* 30 */     VSAN("vsan");
/*    */     
/*    */     private String name;
/*    */     
/*    */     Type(String name) {
/* 35 */       this.name = name;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 42 */       return this.name;
/*    */     }
/*    */     
/*    */     public static Type fromString(String type) {
/*    */       byte b;
/*    */       int i;
/*    */       Type[] arrayOfType;
/* 49 */       for (i = (arrayOfType = values()).length, b = 0; b < i; ) { Type typeEnum = arrayOfType[b];
/* 50 */         if (typeEnum.name.equals(type)) {
/* 51 */           return typeEnum;
/*    */         }
/*    */         b++; }
/*    */       
/* 55 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/dataprotection/model/DatastoreData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */