/*   1 */ package com.vmware.vsphere.client.vsandp.dataproviders.vm;@Component public class VmDataProtectionPropertyProviderAdapter implements PropertyProviderAdapter { @Autowired private DpClient dpClient; @Autowired private VsanDpInventoryHelper inventoryHelper; @Autowired private VcPropertiesFacade vcPropertiesFacade; static { ajc$preClinit(); } private static final String VM_IS_LINKED_CLONE = "isVmLinkedClone"; private static final String VM_DATA_PROTECTION_STATE = "isVmDataProtected"; private static final String VM_RESTORE_ALLOWED = "isVmRestoreAllowed"; private static final JoinPoint.StaticPart ajc$tjp_0; private static void ajc$preClinit() { Factory factory = new Factory("VmDataProtectionPropertyProviderAdapter.java", VmDataProtectionPropertyProviderAdapter.class); ajc$tjp_0 = factory.makeSJP("method-call", (Signature)factory.makeMethodSig("401", "queryCgByObject", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery$Spec", "arg0", "com.vmware.vim.vsandp.binding.vim.vsandp.fault.VsanClusterNotFound", "com.vmware.vim.vsandp.binding.vim.vsandp.cluster.InventoryService$CgMemberQuery"), 116); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Object queryCgByObject_aroundBody1$advice(VmDataProtectionPropertyProviderAdapter ajc$this, InventoryService target, InventoryService.CgMemberQuery.Spec arg0, VsanDpTimingAspect ajc$aspectInstance, AroundClosure ajc$aroundClosure, JoinPoint.StaticPart thisJoinPointStaticPart) {
/*  23 */     long startTimeMs = System.currentTimeMillis();
/*     */     
/*  25 */     AroundClosure aroundClosure = ajc$aroundClosure; Object result = queryCgByObject_aroundBody0(ajc$this, target, arg0);
/*     */     
/*  27 */     long endTimeMs = System.currentTimeMillis();
/*  28 */     long execTimeMs = endTimeMs - startTimeMs;
/*     */     
/*  30 */     if (execTimeMs > 500L) {
/*  31 */       String name = thisJoinPointStaticPart.getSignature().toString();
/*  32 */       String msg = "Executing " + name + 
/*  33 */         " took too long: " + execTimeMs + " ms.";
/*  34 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().warn(msg);
/*     */     } else {
/*  36 */       String name = thisJoinPointStaticPart.getSignature().toString();
/*  37 */       String msg = "Executing " + name + " took : " + execTimeMs + " ms.";
/*  38 */       VsanDpTimingAspect.ajc$inlineAccessFieldGet$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$com_vmware_vsphere_client_vsandp_core_logging_VsanDpTimingAspect$_logger().debug(msg);
/*     */     } 
/*     */     
/*  41 */     return result;
/*     */   }
/*     */   @Autowired
/*     */   public void setDataServiceExtensionRegistry(DataServiceExtensionRegistry registry) {
/*  45 */     TypeInfo ti = new TypeInfo();
/*  46 */     ti.type = VirtualMachine.class.getSimpleName();
/*  47 */     ti.properties = new String[] { "isVmDataProtected", "isVmLinkedClone", "isVmRestoreAllowed" };
/*  48 */     registry.registerDataAdapter(this, new TypeInfo[] { ti });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet getProperties(PropertyRequestSpec propertyRequest) {
/*  56 */     ResultSet result = new ResultSet();
/*  57 */     result.items = new ResultItem[0]; byte b; int i; PropertySpec[] arrayOfPropertySpec;
/*  58 */     for (i = (arrayOfPropertySpec = propertyRequest.properties).length, b = 0; b < i; ) { PropertySpec propertySpec = arrayOfPropertySpec[b]; byte b1; int j; String[] arrayOfString;
/*  59 */       for (j = (arrayOfString = propertySpec.propertyNames).length, b1 = 0; b1 < j; ) { String propertyName = arrayOfString[b1]; try {
/*     */           String str;
/*  61 */           switch ((str = propertyName).hashCode()) { case -869362045: if (str.equals("isVmLinkedClone")) {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*  66 */                 result.items = (ResultItem[])ArrayUtils.addAll((Object[])result.items, (Object[])isVmLinkedClone(propertyRequest.objects)); break;
/*     */               } 
/*     */             case 1607104283:
/*  69 */               if (str.equals("isVmRestoreAllowed")) { result.items = (ResultItem[])ArrayUtils.addAll((Object[])result.items, (Object[])isVmRestoreAllowed(propertyRequest.objects)); break; } 
/*     */             case 1695944483: if (str.equals("isVmDataProtected")) { result.items = (ResultItem[])ArrayUtils.addAll((Object[])result.items, (Object[])isVmDataProtected(propertyRequest.objects)); break; } 
/*     */             default:
/*  72 */               throw new IllegalArgumentException(); }
/*     */         
/*  74 */         } catch (Exception exception) {
/*  75 */           result.error = exception;
/*     */         }  b1++; }
/*     */       
/*     */       b++; }
/*     */     
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   private ResultItem[] isVmDataProtected(Object[] targetObjects) throws Exception {
/*  84 */     ManagedObjectReference[] vmRefs = Arrays.<ManagedObjectReference, Object>copyOf(targetObjects, targetObjects.length, ManagedObjectReference[].class);
/*     */     
/*  86 */     ArrayList<ResultItem> result = new ArrayList();
/*  87 */     PropertyValue[] vmProperties = QueryUtil.getProperties((Object[])vmRefs, 
/*  88 */         new String[] { "config.vmStorageObjectId", "cluster" }).getPropertyValues(); byte b; int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference1;
/*  90 */     for (i = (arrayOfManagedObjectReference1 = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference1[b];
/*  91 */       ManagedObjectReference cluster = getVmProperty(vmRef, vmProperties, "cluster");
/*  92 */       if (cluster != null && 
/*  93 */         VsanCapabilityUtils.isLocalDataProtectionSupported(cluster) && 
/*  94 */         isDataProtectionActive(vmRef, cluster, vmProperties)) {
/*  95 */         result.add(QueryUtil.createResultItem("isVmDataProtected", Boolean.valueOf(true), vmRef));
/*     */       } else {
/*  97 */         result.add(QueryUtil.createResultItem("isVmDataProtected", Boolean.valueOf(false), vmRef));
/*     */       } 
/*     */       b++; }
/*     */     
/* 101 */     return result.<ResultItem>toArray(new ResultItem[vmRefs.length]);
/*     */   }
/*     */   
/*     */   private boolean isDataProtectionActive(ManagedObjectReference vm, ManagedObjectReference cluster, PropertyValue[] vmProperties) {
/* 105 */     String storageObjectId = getVmProperty(vm, vmProperties, "config.vmStorageObjectId");
/*     */ 
/*     */     
/* 108 */     Exception exception1 = null, exception2 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/*     */     } finally {
/* 119 */       exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*     */     
/*     */     } 
/*     */   } private static final InventoryService.CgMemberQuery queryCgByObject_aroundBody0(VmDataProtectionPropertyProviderAdapter paramVmDataProtectionPropertyProviderAdapter, InventoryService paramInventoryService, InventoryService.CgMemberQuery.Spec paramSpec) { return paramInventoryService.queryCgByObject(paramSpec); } private ResultItem[] isVmLinkedClone(Object[] targetObjects) throws Exception {
/* 123 */     ManagedObjectReference[] vmRefs = Arrays.<ManagedObjectReference, Object>copyOf(targetObjects, targetObjects.length, ManagedObjectReference[].class);
/*     */     
/* 125 */     ArrayList<ResultItem> result = new ArrayList();
/* 126 */     PropertyValue[] vmProperties = QueryUtil.getProperties((Object[])vmRefs, 
/* 127 */         new String[] { "config.hardware.device", "cluster" }).getPropertyValues(); byte b; int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 129 */     for (i = (arrayOfManagedObjectReference1 = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference1[b];
/* 130 */       ManagedObjectReference cluster = getVmProperty(vmRef, vmProperties, "cluster");
/* 131 */       if (cluster != null && 
/* 132 */         VsanCapabilityUtils.isLocalDataProtectionSupported(cluster) && 
/* 133 */         isUnmanagedDiskPresent(vmRef, vmProperties)) {
/* 134 */         result.add(QueryUtil.createResultItem("isVmLinkedClone", Boolean.valueOf(true), vmRef));
/*     */       } else {
/* 136 */         result.add(QueryUtil.createResultItem("isVmLinkedClone", Boolean.valueOf(false), vmRef));
/*     */       } 
/*     */       b++; }
/*     */     
/* 140 */     return result.<ResultItem>toArray(new ResultItem[vmRefs.length]);
/*     */   }
/*     */   
/*     */   private boolean isUnmanagedDiskPresent(ManagedObjectReference vm, PropertyValue[] vmProperties) throws Exception {
/* 144 */     VirtualDevice[] devices = getVmProperty(vm, vmProperties, "config.hardware.device"); byte b; int i; VirtualDevice[] arrayOfVirtualDevice1;
/* 145 */     for (i = (arrayOfVirtualDevice1 = devices).length, b = 0; b < i; ) { VirtualDevice device = arrayOfVirtualDevice1[b];
/* 146 */       if (device instanceof VirtualDisk) {
/* 147 */         VirtualDisk disk = (VirtualDisk)device;
/* 148 */         if (this.vcPropertiesFacade.isNativeUnmanagedLinkedClone(disk))
/*     */         {
/* 150 */           return true;
/*     */         }
/*     */       } 
/*     */       b++; }
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResultItem[] isVmRestoreAllowed(Object[] targetObjects) throws Exception
/*     */   {
/* 164 */     ManagedObjectReference[] vmRefs = Arrays.<ManagedObjectReference, Object>copyOf(targetObjects, targetObjects.length, ManagedObjectReference[].class);
/*     */     
/* 166 */     ArrayList<ResultItem> result = new ArrayList();
/* 167 */     PropertyValue[] vmProperties = QueryUtil.getProperties((Object[])vmRefs, new String[] {
/* 168 */           "config.vmStorageObjectId", 
/* 169 */           "cluster", 
/* 170 */           "config.hardware.device" }).getPropertyValues(); byte b; int i;
/*     */     ManagedObjectReference[] arrayOfManagedObjectReference1;
/* 172 */     for (i = (arrayOfManagedObjectReference1 = vmRefs).length, b = 0; b < i; ) { ManagedObjectReference vmRef = arrayOfManagedObjectReference1[b];
/* 173 */       ManagedObjectReference cluster = getVmProperty(vmRef, vmProperties, "cluster");
/* 174 */       boolean hasPermissions = this.inventoryHelper.isVmRestoreAllowed(vmRef);
/*     */       
/* 176 */       if (hasPermissions && 
/* 177 */         cluster != null && 
/* 178 */         VsanCapabilityUtils.isLocalDataProtectionSupported(cluster) && 
/* 179 */         isDataProtectionActive(vmRef, cluster, vmProperties) && 
/* 180 */         !isUnmanagedDiskPresent(vmRef, vmProperties)) {
/* 181 */         result.add(QueryUtil.createResultItem("isVmRestoreAllowed", Boolean.valueOf(true), vmRef));
/*     */       } else {
/* 183 */         result.add(QueryUtil.createResultItem("isVmRestoreAllowed", Boolean.valueOf(false), vmRef));
/*     */       } 
/*     */       b++; }
/*     */     
/* 187 */     return result.<ResultItem>toArray(new ResultItem[vmRefs.length]); } private <T> T getVmProperty(ManagedObjectReference vm, PropertyValue[] vmProperties, String propertyName) {
/*     */     byte b;
/*     */     int i;
/*     */     PropertyValue[] arrayOfPropertyValue;
/* 191 */     for (i = (arrayOfPropertyValue = vmProperties).length, b = 0; b < i; ) { PropertyValue property = arrayOfPropertyValue[b];
/* 192 */       if (property.propertyName.equals(propertyName) && 
/* 193 */         property.resourceObject.equals(vm)) {
/* 194 */         return (T)property.value;
/*     */       }
/*     */       b++; }
/*     */     
/* 198 */     return null;
/*     */   } }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/dataproviders/vm/VmDataProtectionPropertyProviderAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */