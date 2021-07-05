/*     */ package com.vmware.vsphere.client.vsan.base.util;
/*     */ 
/*     */ import com.vmware.vim.binding.pbm.capability.CapabilityInstance;
/*     */ import com.vmware.vim.binding.pbm.capability.CapabilityMetadata;
/*     */ import com.vmware.vim.binding.pbm.capability.ConstraintInstance;
/*     */ import com.vmware.vim.binding.pbm.capability.Operator;
/*     */ import com.vmware.vim.binding.pbm.capability.PropertyInstance;
/*     */ import com.vmware.vim.binding.pbm.compliance.ComplianceResult;
/*     */ import com.vmware.vim.binding.pbm.compliance.PolicyStatus;
/*     */ import com.vmware.vim.binding.pbm.profile.Profile;
/*     */ import com.vmware.vim.binding.pbm.profile.ProfileId;
/*     */ import com.vmware.vim.binding.vim.ClusterComputeResource;
/*     */ import com.vmware.vim.binding.vim.Datastore;
/*     */ import com.vmware.vim.binding.vim.host.DiskDimensions;
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.binding.vmodl.MethodFault;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StorageComplianceResult;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StorageOperationalStatus;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.StoragePolicyStatus;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vsan.client.util.VmodlHelper;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanComplianceStatus;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanOperationalStatus;
/*     */ import com.vmware.vsphere.client.vsan.util.QueryUtil;
/*     */ import com.vmware.vsphere.client.vsan.util.Utils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.lang.Validate;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class BaseUtils
/*     */ {
/*     */   public static final String VSAN_SERVICE_STRINGS = "vsanservice";
/*     */   private static final int BLOCK_SIZE_DEFAULT = 512;
/*     */   public static final String VMWARE_VSAN_NAMESPACE = "VSAN";
/*     */   private static final String DATASTORE_HOST_PROPERTY = "host";
/*     */   private static final String HOST_PARENT_PROPERTY = "parent";
/*  54 */   private static final Log _logger = LogFactory.getLog(BaseUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUTCTimeZone(Calendar calendar) {
/*  60 */     if (calendar != null) {
/*  61 */       calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> getProperties(ManagedObjectReference moRef, String[] properties) throws Exception {
/*  72 */     HashMap<String, Object> result = new HashMap<>();
/*  73 */     PropertyValue[] propValues = QueryUtil.getProperties(moRef, properties).getPropertyValues();
/*  74 */     if (propValues != null) {
/*  75 */       byte b; int i; PropertyValue[] arrayOfPropertyValue; for (i = (arrayOfPropertyValue = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue[b];
/*  76 */         result.put(propValue.propertyName, propValue.value);
/*     */         b++; }
/*     */     
/*     */     } 
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long lbaToBytes(DiskDimensions.Lba lba) {
/*  92 */     int blockSize = lba.blockSize;
/*  93 */     if (blockSize == 0) {
/*  94 */       blockSize = 512;
/*     */     }
/*  96 */     return lba.block * blockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static VsanComplianceStatus getComplianceStatus(ComplianceResult complianceResult) {
/* 103 */     if (complianceResult == null) {
/* 104 */       return VsanComplianceStatus.UNKNOWN;
/*     */     }
/*     */     
/* 107 */     if (complianceResult.mismatch) {
/* 108 */       return VsanComplianceStatus.OUT_OF_DATE;
/*     */     }
/*     */     
/* 111 */     if (complianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.compliant.name()))
/* 112 */       return VsanComplianceStatus.COMPLIANT; 
/* 113 */     if (complianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.nonCompliant.name()))
/* 114 */       return VsanComplianceStatus.NOT_COMPLIANT; 
/* 115 */     if (complianceResult.complianceStatus.equals(ComplianceResult.ComplianceStatus.notApplicable.name())) {
/* 116 */       return VsanComplianceStatus.NOT_APPLICABLE;
/*     */     }
/*     */     
/* 119 */     return VsanComplianceStatus.UNKNOWN;
/*     */   }
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
/*     */   public static VsanOperationalStatus getOperationalState(StorageOperationalStatus operationalStatus) {
/* 133 */     if (operationalStatus == null) {
/* 134 */       return null;
/*     */     }
/*     */     
/* 137 */     if (operationalStatus.healthy != null && operationalStatus.healthy.booleanValue()) {
/* 138 */       if (operationalStatus.transitional != null && operationalStatus.transitional.booleanValue()) {
/* 139 */         return VsanOperationalStatus.HEALTHY_TRANSITIONAL;
/*     */       }
/* 141 */       return VsanOperationalStatus.HEALTHY;
/*     */     } 
/*     */     
/* 144 */     if (operationalStatus.transitional != null && operationalStatus.transitional.booleanValue()) {
/* 145 */       return VsanOperationalStatus.UNHEALTHY_TRANSITIONAL;
/*     */     }
/* 147 */     return VsanOperationalStatus.UNHEALTHY_DISK_UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Profile getProfileByUuid(List<Profile> profiles, String profileUuid) {
/* 157 */     if (profiles == null || profiles.size() == 0) {
/* 158 */       return null;
/*     */     }
/*     */     
/* 161 */     for (Profile profile : profiles) {
/* 162 */       if (profile.profileId.uniqueId.equals(profileUuid)) {
/* 163 */         return profile;
/*     */       }
/*     */     } 
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getProfileNameByUuid(List<Profile> profiles, String profileUuid, boolean returnUuidIfNoNameFound) {
/* 173 */     String result = "";
/* 174 */     Profile profile = getProfileByUuid(profiles, profileUuid);
/* 175 */     if (profile != null) {
/* 176 */       result = profile.name;
/* 177 */     } else if (returnUuidIfNoNameFound) {
/* 178 */       result = profileUuid;
/*     */     } 
/* 180 */     return result;
/*     */   }
/*     */   
/*     */   public static ComplianceResult toComplianceResult(StorageComplianceResult storageComplianceResult) {
/* 184 */     if (storageComplianceResult == null) {
/* 185 */       return null;
/*     */     }
/*     */     
/* 188 */     ComplianceResult result = new ComplianceResult();
/* 189 */     result.checkTime = storageComplianceResult.checkTime;
/* 190 */     result.mismatch = storageComplianceResult.mismatch;
/* 191 */     List<PolicyStatus> violatedPolicies = new ArrayList<>();
/* 192 */     if (storageComplianceResult.violatedPolicies != null) {
/* 193 */       byte b; int i; StoragePolicyStatus[] arrayOfStoragePolicyStatus; for (i = (arrayOfStoragePolicyStatus = storageComplianceResult.violatedPolicies).length, b = 0; b < i; ) { StoragePolicyStatus status = arrayOfStoragePolicyStatus[b];
/* 194 */         String id = (status.id == null) ? "" : status.id;
/* 195 */         CapabilityInstance expInstance = new CapabilityInstance();
/* 196 */         expInstance.id = new CapabilityMetadata.UniqueId("VSAN", id);
/* 197 */         expInstance.constraint = new ConstraintInstance[] {
/* 198 */             new ConstraintInstance(
/* 199 */               new PropertyInstance[] {
/* 200 */                 new PropertyInstance(status.id, Operator.NOT.toString(), status.expectedValue)
/*     */               })
/*     */           };
/*     */         
/* 204 */         CapabilityInstance currInstance = new CapabilityInstance();
/* 205 */         currInstance.id = new CapabilityMetadata.UniqueId("VSAN", id);
/* 206 */         currInstance.constraint = new ConstraintInstance[] {
/* 207 */             new ConstraintInstance(
/* 208 */               new PropertyInstance[] {
/* 209 */                 new PropertyInstance(status.id, Operator.NOT.toString(), status.currentValue)
/*     */               })
/*     */           };
/*     */         
/* 213 */         PolicyStatus newStatus = new PolicyStatus(expInstance, currInstance);
/* 214 */         violatedPolicies.add(newStatus);
/*     */         b++; }
/*     */     
/*     */     } 
/* 218 */     result.violatedPolicies = violatedPolicies.<PolicyStatus>toArray(new PolicyStatus[violatedPolicies.size()]);
/* 219 */     result.complianceStatus = storageComplianceResult.complianceStatus;
/* 220 */     result.profile = new ProfileId(storageComplianceResult.profile);
/* 221 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Profile> getStorageProfiles(ManagedObjectReference moref) throws MethodFault {
/* 232 */     ManagedObjectReference vcRootRef = VmodlHelper.getRootFolder(moref.getServerGuid());
/* 233 */     List<Profile> profiles = new ArrayList<>();
/*     */     try {
/* 235 */       PropertyValue[] resultset = 
/* 236 */         QueryUtil.getPropertiesForRelatedObjects(
/* 237 */           vcRootRef, 
/* 238 */           "pbmProfiles", 
/* 239 */           "PbmRequirementStorageProfile", 
/* 240 */           new String[] { "profileContent"
/* 241 */           }).getPropertyValues(); byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 242 */       for (i = (arrayOfPropertyValue1 = resultset).length, b = 0; b < i; ) { PropertyValue propVal = arrayOfPropertyValue1[b];
/* 243 */         Profile profile = (Profile)propVal.value;
/* 244 */         profiles.add(profile); b++; }
/*     */     
/* 246 */     } catch (Exception ex) {
/* 247 */       throw Utils.getMethodFault(ex);
/*     */     } 
/* 249 */     return profiles;
/*     */   }
/*     */   
/*     */   public static ManagedObjectReference getCluster(ManagedObjectReference moRef) {
/* 253 */     Validate.notNull(moRef);
/* 254 */     String moRefType = moRef.getType();
/* 255 */     if (ClusterComputeResource.class.getSimpleName().equalsIgnoreCase(moRefType))
/* 256 */       return moRef; 
/* 257 */     if (Datastore.class.getSimpleName().equalsIgnoreCase(moRefType)) {
/* 258 */       return getClusterFromDatastore(moRef);
/*     */     }
/* 260 */     throw new IllegalArgumentException("Not supported MoRef type.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static ManagedObjectReference getClusterFromDatastore(ManagedObjectReference dsRef) {
/* 265 */     String[] dsProperties = { "summary.type", "host" };
/*     */     
/*     */     try {
/* 268 */       PropertyValue[] propValues = QueryUtil.getProperties(dsRef, dsProperties).getPropertyValues();
/* 269 */       String type = null;
/* 270 */       Datastore.HostMount[] hosts = null; byte b; int i; PropertyValue[] arrayOfPropertyValue1;
/* 271 */       for (i = (arrayOfPropertyValue1 = propValues).length, b = 0; b < i; ) { PropertyValue propValue = arrayOfPropertyValue1[b];
/* 272 */         if (propValue.propertyName.equals("summary.type")) {
/* 273 */           type = (String)propValue.value;
/* 274 */         } else if (propValue.propertyName.equals("host")) {
/* 275 */           hosts = (Datastore.HostMount[])propValue.value;
/*     */         } 
/*     */         b++; }
/*     */       
/* 279 */       if ("vsan".equalsIgnoreCase(type)) {
/* 280 */         return getParentCluster(hosts);
/*     */       }
/* 282 */     } catch (Exception ex) {
/* 283 */       _logger.error("Could not retrieve cluster from datastore", ex);
/*     */     } 
/* 285 */     return null;
/*     */   }
/*     */   
/*     */   private static ManagedObjectReference getParentCluster(Datastore.HostMount[] hostMounts) throws Exception {
/* 289 */     List<ManagedObjectReference> hosts = new ArrayList<>(); byte b; int i; Datastore.HostMount[] arrayOfHostMount;
/* 290 */     for (i = (arrayOfHostMount = hostMounts).length, b = 0; b < i; ) { Datastore.HostMount h = arrayOfHostMount[b];
/* 291 */       if (h.key != null)
/* 292 */         hosts.add(h.key); 
/*     */       b++; }
/*     */     
/* 295 */     PropertyValue[] propValues = 
/* 296 */       QueryUtil.getProperties(hosts.toArray(), new String[] { "parent" }).getPropertyValues(); PropertyValue[] arrayOfPropertyValue1;
/* 297 */     for (int j = (arrayOfPropertyValue1 = propValues).length; i < j; ) { PropertyValue propValue = arrayOfPropertyValue1[i];
/* 298 */       if (propValue.value != null && propValue.value instanceof ManagedObjectReference)
/* 299 */         return (ManagedObjectReference)propValue.value; 
/*     */       i++; }
/*     */     
/* 302 */     return null;
/*     */   }
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
/*     */   public static ManagedObjectReference generateMor(String rowValue, String serverGuid) {
/* 317 */     String[] params = rowValue.split(":");
/*     */ 
/*     */ 
/*     */     
/* 321 */     if (params == null || params.length < 3) {
/* 322 */       return null;
/*     */     }
/* 324 */     return new ManagedObjectReference(params[params.length - 2], params[params.length - 1], serverGuid);
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/util/BaseUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */