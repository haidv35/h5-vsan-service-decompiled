/*    */ package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.util.Date;
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
/*    */ public class CacheEntry<R extends Closeable>
/*    */ {
/*    */   protected final R resource;
/*    */   protected final Runnable parentCloseHandler;
/* 24 */   protected int refCount = 1;
/*    */ 
/*    */   
/* 27 */   protected long lastReleaseTime = -1L;
/*    */   
/*    */   public R getResource() {
/* 30 */     return this.resource;
/*    */   }
/*    */   
/*    */   public CacheEntry(R resource, Runnable parentCloseHandler) {
/* 34 */     this.resource = resource;
/* 35 */     this.parentCloseHandler = parentCloseHandler;
/*    */   }
/*    */   
/*    */   public int getRefCount() {
/* 39 */     return this.refCount;
/*    */   }
/*    */   
/*    */   public void incRefCount() {
/* 43 */     this.refCount++;
/*    */   }
/*    */   
/*    */   public void decRefCount() {
/* 47 */     if (this.refCount <= 0) {
/* 48 */       throw new IllegalStateException(
/* 49 */           "Releasing an entry with zero refCount");
/*    */     }
/*    */     
/* 52 */     this.refCount--;
/* 53 */     this.lastReleaseTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public Runnable getParentCloseHandler() {
/* 57 */     return this.parentCloseHandler;
/*    */   }
/*    */   
/*    */   public long getLastReleaseTime() {
/* 61 */     return this.lastReleaseTime;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return String.format("CacheEntry [resource=%s, refCount=%s, lastReleaseTime=%s]", new Object[] {
/* 67 */           this.resource, Integer.valueOf(this.refCount), new Date(this.lastReleaseTime)
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/CacheEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */