/*     */ package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan;
/*     */ 
/*     */ import com.vmware.proxygen.ts.TsService;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstanceProviderModel;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.filter.VmProtectionInstanceFilter;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.filter.VmProtectionInstanceFilterEnum;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.filter.VmProtectionInstanceFilterSpec;
/*     */ import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits.PitProvider;
/*     */ import com.vmware.vsphere.client.vsandp.data.ProtectionType;
/*     */ import com.vmware.vsphere.client.vsandp.helper.VsanDpInventoryHelper;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class VmDataProtectionSyncPointsController
/*     */ {
/*  27 */   private static final Logger logger = LoggerFactory.getLogger(VmDataProtectionSyncPointsController.class);
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private PitProvider pitProvider;
/*     */   
/*     */   @Autowired
/*     */   private VsanDpInventoryHelper inventoryHelper;
/*     */ 
/*     */   
/*     */   @TsService
/*     */   public VmProtectionInstanceProviderModel getGlobalCatalogModel(ManagedObjectReference vmRef, VmProtectionInstanceFilterSpec filter) throws Exception {
/*  39 */     logger.debug("Getting list of sync points for vm {}", vmRef);
/*     */ 
/*     */     
/*  42 */     TreeSet<VmProtectionInstance> localPits = this.pitProvider.getLocalPits(vmRef);
/*  43 */     logger.debug("Successfully retrieved local pits for vm {}. Total count of pits found: {}", 
/*  44 */         vmRef, Integer.valueOf(localPits.size()));
/*     */ 
/*     */ 
/*     */     
/*  48 */     TreeSet<VmProtectionInstance> archivePits = this.pitProvider.getArchivePits(vmRef);
/*  49 */     if (archivePits == null) {
/*  50 */       logger.debug("Vm {} does not have active archive series.", vmRef);
/*     */     } else {
/*  52 */       logger.debug("Successfully retrieved archive pits for vm {}. Total count of pits found: {}", 
/*  53 */           vmRef, Integer.valueOf(archivePits.size()));
/*     */     } 
/*     */ 
/*     */     
/*  57 */     VmProtectionInstanceProviderModel dpModel = prepareProtectionsDataProviderModel(localPits, archivePits, filter);
/*  58 */     dpModel.hasRestorePermission = this.inventoryHelper.isVmRestoreAllowed(vmRef);
/*  59 */     return dpModel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VmProtectionInstanceProviderModel prepareProtectionsDataProviderModel(TreeSet<VmProtectionInstance> localPits, TreeSet<VmProtectionInstance> archivePits, VmProtectionInstanceFilterSpec filter) {
/*  65 */     VmProtectionInstanceProviderModel result = new VmProtectionInstanceProviderModel();
/*     */     
/*  67 */     TreeSet<VmProtectionInstance> allInstances = new TreeSet<>();
/*     */ 
/*     */ 
/*     */     
/*  71 */     TreeSet<VmProtectionInstance> filteredInstances = new TreeSet<>(new VmProtectionInstanceComparator());
/*     */     
/*  73 */     allInstances.addAll(localPits);
/*     */     
/*  75 */     localPits = filterPits(localPits, filter);
/*  76 */     result.instances.put(ProtectionType.LOCAL, localPits);
/*  77 */     filteredInstances.addAll(localPits);
/*     */     
/*  79 */     result.protectionTypes.add(ProtectionType.LOCAL);
/*     */     
/*  81 */     if (archivePits != null) {
/*  82 */       allInstances.addAll(archivePits);
/*     */       
/*  84 */       archivePits = filterPits(archivePits, filter);
/*  85 */       result.instances.put(ProtectionType.ARCHIVE, archivePits);
/*  86 */       filteredInstances.addAll(archivePits);
/*     */       
/*  88 */       result.protectionTypes.add(ProtectionType.ARCHIVE);
/*     */     } 
/*     */     
/*  91 */     result.instancesCount = allInstances.size();
/*     */ 
/*     */     
/*  94 */     result.filterModel = getFilterModel(allInstances, filter.timezone);
/*     */     
/*  96 */     for (VmProtectionInstance instance : filteredInstances) {
/*  97 */       result.headerInstances.add(instance.syncPoint);
/*     */     }
/*     */     
/* 100 */     if (!filteredInstances.isEmpty()) {
/*     */       
/* 102 */       result.lowestDate = ((VmProtectionInstance)filteredInstances.last()).syncPoint;
/* 103 */       result.highestDate = ((VmProtectionInstance)filteredInstances.first()).syncPoint;
/*     */     } 
/*     */     
/* 106 */     for (Date date : result.headerInstances) {
/*     */       
/* 108 */       for (ProtectionType protectionType : result.protectionTypes) {
/* 109 */         TreeSet<VmProtectionInstance> instances = (TreeSet<VmProtectionInstance>)result.instances.get(protectionType);
/*     */         
/* 111 */         VmProtectionInstance emptyInstance = VmProtectionInstance.getEmptyInstance(date);
/* 112 */         if (!instances.contains(emptyInstance)) {
/* 113 */           instances.add(emptyInstance);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeSet<VmProtectionInstance> filterPits(TreeSet<VmProtectionInstance> instances, VmProtectionInstanceFilterSpec filter) {
/* 127 */     TreeSet<VmProtectionInstance> result = new TreeSet<>(new VmProtectionInstanceComparator());
/*     */     
/* 129 */     Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(filter.timezone));
/* 130 */     calendar.set(11, 23);
/* 131 */     calendar.set(12, 59);
/* 132 */     calendar.set(13, 59);
/* 133 */     calendar.set(14, 999);
/*     */     
/* 135 */     Calendar calendarFrom = resetDate(Calendar.getInstance(TimeZone.getTimeZone(filter.timezone)));
/*     */     
/* 137 */     switch (filter.type) {
/*     */       case null:
/* 139 */         calendarFrom.add(5, -2);
/* 140 */         for (VmProtectionInstance instance : instances) {
/* 141 */           if (instance.syncPoint.after(calendarFrom.getTime()) || instance.syncPoint.equals(
/* 142 */               calendarFrom.getTime()))
/*     */           {
/* 144 */             result.add(instance);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case THREE_SEVEN_DAYS:
/* 149 */         calendar.add(5, -3);
/* 150 */         calendarFrom.add(5, -6);
/* 151 */         for (VmProtectionInstance instance : instances) {
/* 152 */           if ((instance.syncPoint.after(calendarFrom.getTime()) || instance.syncPoint.equals(
/* 153 */               calendarFrom.getTime())) && (
/* 154 */             instance.syncPoint.before(calendar.getTime()) || instance.syncPoint.equals(calendar.getTime()))) {
/* 155 */             result.add(instance);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case ONE_TWO_WEEKS:
/* 160 */         calendar.add(5, -7);
/* 161 */         calendarFrom.add(5, -13);
/* 162 */         for (VmProtectionInstance instance : instances) {
/* 163 */           if ((instance.syncPoint.after(calendarFrom.getTime()) || instance.syncPoint.equals(
/* 164 */               calendarFrom.getTime())) && (
/* 165 */             instance.syncPoint.before(calendar.getTime()) || instance.syncPoint.equals(calendar.getTime()))) {
/* 166 */             result.add(instance);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case TWO_FOUR_WEEKS:
/* 171 */         calendar.add(5, -14);
/* 172 */         calendarFrom.add(5, -27);
/* 173 */         for (VmProtectionInstance instance : instances) {
/* 174 */           if ((instance.syncPoint.after(calendarFrom.getTime()) || instance.syncPoint.equals(
/* 175 */               calendarFrom.getTime())) && (
/* 176 */             instance.syncPoint.before(calendar.getTime()) || instance.syncPoint.equals(calendar.getTime()))) {
/* 177 */             result.add(instance);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case OLDER_THAN_FOUR_WEEKS:
/* 182 */         calendar.add(5, -28);
/* 183 */         for (VmProtectionInstance instance : instances) {
/* 184 */           if (instance.syncPoint.before(calendar.getTime())) {
/* 185 */             result.add(instance);
/*     */           }
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 191 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VmProtectionInstanceFilter getFilterModel(Set<VmProtectionInstance> allInstances, String timezone) {
/* 198 */     VmProtectionInstanceFilter result = new VmProtectionInstanceFilter();
/*     */     
/* 200 */     Calendar threeDaysCalendar = resetDate(Calendar.getInstance(TimeZone.getTimeZone(timezone)));
/* 201 */     threeDaysCalendar.add(5, -2);
/*     */     
/* 203 */     Calendar sevenDaysCalendar = resetDate(Calendar.getInstance(TimeZone.getTimeZone(timezone)));
/* 204 */     sevenDaysCalendar.add(5, -6);
/*     */     
/* 206 */     Calendar twoWeeksCalendar = resetDate(Calendar.getInstance(TimeZone.getTimeZone(timezone)));
/* 207 */     twoWeeksCalendar.add(5, -13);
/*     */     
/* 209 */     Calendar fourWeeksCalendar = resetDate(Calendar.getInstance(TimeZone.getTimeZone(timezone)));
/* 210 */     fourWeeksCalendar.add(5, -27);
/*     */     
/* 212 */     int newerThanThreeDays = 0;
/* 213 */     int betweenThreeAndSevenDays = 0;
/* 214 */     int betweenOneAndTwoWeeks = 0;
/* 215 */     int betweenTwoAndFourWeeks = 0;
/* 216 */     int olderThanFourWeeks = 0;
/*     */     
/* 218 */     for (VmProtectionInstance instance : allInstances) {
/* 219 */       if (threeDaysCalendar.getTime().before(instance.syncPoint) || threeDaysCalendar.getTime().equals(
/* 220 */           instance.syncPoint)) {
/* 221 */         newerThanThreeDays++; continue;
/* 222 */       }  if (sevenDaysCalendar.getTime().before(instance.syncPoint) || sevenDaysCalendar.getTime().equals(
/* 223 */           instance.syncPoint)) {
/* 224 */         betweenThreeAndSevenDays++; continue;
/* 225 */       }  if (twoWeeksCalendar.getTime().before(instance.syncPoint) || twoWeeksCalendar.getTime().equals(
/* 226 */           instance.syncPoint)) {
/* 227 */         betweenOneAndTwoWeeks++; continue;
/* 228 */       }  if (fourWeeksCalendar.getTime().before(instance.syncPoint) || fourWeeksCalendar.getTime().equals(
/* 229 */           instance.syncPoint)) {
/* 230 */         betweenTwoAndFourWeeks++; continue;
/*     */       } 
/* 232 */       olderThanFourWeeks++;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     result.newerThanThreeDays = newerThanThreeDays / allInstances.size() * 100.0D;
/* 237 */     result.betweenThreeAndSevenDays = betweenThreeAndSevenDays / allInstances.size() * 100.0D;
/* 238 */     result.betweenOneAndTwoWeeks = betweenOneAndTwoWeeks / allInstances.size() * 100.0D;
/* 239 */     result.betweenTwoAndFourWeeks = betweenTwoAndFourWeeks / allInstances.size() * 100.0D;
/* 240 */     result.olderThanFourWeeks = olderThanFourWeeks / allInstances.size() * 100.0D;
/*     */     
/* 242 */     result.newerThanThreeDaysCount = newerThanThreeDays;
/* 243 */     result.betweenThreeAndSevenDaysCount = betweenThreeAndSevenDays;
/* 244 */     result.betweenOneAndTwoWeeksCount = betweenOneAndTwoWeeks;
/* 245 */     result.betweenTwoAndFourWeeksCount = betweenTwoAndFourWeeks;
/* 246 */     result.olderThanFourWeeksCount = olderThanFourWeeks;
/*     */     
/* 248 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Calendar resetDate(Calendar calendar) {
/* 255 */     calendar.set(11, 0);
/* 256 */     calendar.set(12, 0);
/* 257 */     calendar.set(13, 0);
/* 258 */     calendar.set(14, 0);
/*     */     
/* 260 */     return calendar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VmProtectionInstanceComparator
/*     */     implements Comparator<VmProtectionInstance>
/*     */   {
/*     */     public int compare(VmProtectionInstance o1, VmProtectionInstance o2) {
/* 270 */       return o2.syncPoint.compareTo(o1.syncPoint);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/VmDataProtectionSyncPointsController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */