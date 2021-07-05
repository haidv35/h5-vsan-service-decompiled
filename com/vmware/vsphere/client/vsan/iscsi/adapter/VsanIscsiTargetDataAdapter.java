/*     */ package com.vmware.vsphere.client.vsan.iscsi.adapter;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiInitiatorGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTarget;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetBasicInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
/*     */ import com.vmware.vise.data.Constraint;
/*     */ import com.vmware.vise.data.query.CompositeConstraint;
/*     */ import com.vmware.vise.data.query.DataProviderAdapter;
/*     */ import com.vmware.vise.data.query.PropertyConstraint;
/*     */ import com.vmware.vise.data.query.PropertyValue;
/*     */ import com.vmware.vise.data.query.RequestSpec;
/*     */ import com.vmware.vise.data.query.Response;
/*     */ import com.vmware.vise.data.query.ResultItem;
/*     */ import com.vmware.vise.data.query.ResultSet;
/*     */ import com.vmware.vise.data.query.type;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProfiler;
/*     */ import com.vmware.vsphere.client.vsan.base.util.VsanProviderUtils;
/*     */ import com.vmware.vsphere.client.vsan.iscsi.utils.VsanIscsiTargetUriUtil;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.lang.Validate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @type("VsanIscsiTarget")
/*     */ public class VsanIscsiTargetDataAdapter
/*     */   implements DataProviderAdapter
/*     */ {
/*     */   private static final String VSAN_ISCSI_TARGET_URI_PREFIX = "urn:vsaniscsi:VsanIscsiTarget:VsanIscsiTargetList:NO#";
/*     */   private static final String VSAN_ISCSI_TARGET_CLUSTERREF_PROPERTY = "clusterRef";
/*     */   private static final String VSAN_ISCSI_TARGET_INITIATORGROUPIQN_PROPERTY = "initiatorGroupIqn";
/*     */   private static final String VSAN_ISCSI_TARGET_IQN_FIELD = "iqn";
/*     */   private static final String VSAN_ISCSI_TARGET_ALIAS_FIELD = "alias";
/*     */   private static final String VSAN_ISCSI_TARGET_AUTHTYPE_FIELD = "authType";
/*  59 */   private static final VsanProfiler _profiler = new VsanProfiler(VsanIscsiTargetDataAdapter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Response getData(RequestSpec request) throws Exception {
/*  68 */     ManagedObjectReference clusterRef = null;
/*  69 */     String initiatorGroupIqn = null;
/*     */     
/*  71 */     Constraint constraint = (request.querySpec[0]).resourceSpec.constraint;
/*  72 */     if (constraint instanceof CompositeConstraint) {
/*  73 */       CompositeConstraint compositeConstraint = 
/*  74 */         (CompositeConstraint)constraint;
/*  75 */       Constraint[] childConstraints = compositeConstraint.nestedConstraints; byte b; int i; Constraint[] arrayOfConstraint1;
/*  76 */       for (i = (arrayOfConstraint1 = childConstraints).length, b = 0; b < i; ) { Constraint childConstraint = arrayOfConstraint1[b];
/*  77 */         if (childConstraint instanceof PropertyConstraint) {
/*  78 */           PropertyConstraint propertyConstraint = 
/*  79 */             (PropertyConstraint)childConstraint;
/*  80 */           if (propertyConstraint.propertyName
/*  81 */             .equals("clusterRef")) {
/*  82 */             clusterRef = 
/*  83 */               (ManagedObjectReference)propertyConstraint.comparableValue;
/*  84 */           } else if (propertyConstraint.propertyName
/*  85 */             .equals("initiatorGroupIqn")) {
/*  86 */             initiatorGroupIqn = 
/*  87 */               (String)propertyConstraint.comparableValue;
/*     */           } 
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/*  93 */     Validate.notNull(clusterRef);
/*  94 */     Validate.notEmpty(initiatorGroupIqn);
/*     */     
/*  96 */     Response res = new Response();
/*  97 */     ResultSet rs = new ResultSet();
/*     */     
/*  99 */     ResultItem[] its = 
/* 100 */       createResultItems(getTargetsNotInAccessibleList(clusterRef, 
/* 101 */           initiatorGroupIqn));
/* 102 */     rs.items = its;
/* 103 */     rs.totalMatchedObjectCount = Integer.valueOf(rs.items.length);
/*     */     
/* 105 */     ResultSet[] rss = { rs };
/* 106 */     res.resultSet = rss;
/* 107 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanIscsiTarget[] getTargetsNotInAccessibleList(ManagedObjectReference clusterRef, String initiatorGroupIqn) throws Exception {
/* 113 */     VsanIscsiTargetSystem vsanIscsiSystem = 
/* 114 */       VsanProviderUtils.getVsanIscsiSystem(clusterRef);
/* 115 */     VsanIscsiTarget[] allTargets = null; try {
/* 116 */       Exception exception2, exception1 = null;
/*     */     }
/* 118 */     catch (Exception e) {
/* 119 */       Exception ex = new Exception(e.getLocalizedMessage(), e);
/* 120 */       throw ex;
/*     */     } 
/*     */     
/* 123 */     if (allTargets == null) {
/* 124 */       return null;
/*     */     }
/*     */     
/* 127 */     VsanIscsiInitiatorGroup vsanIscsiInitiatorGroup = 
/* 128 */       vsanIscsiSystem.getIscsiInitiatorGroup(clusterRef, 
/* 129 */         initiatorGroupIqn);
/* 130 */     VsanIscsiTargetBasicInfo[] targetsInAccessibleList = vsanIscsiInitiatorGroup.getTargets();
/*     */     
/* 132 */     ArrayList<VsanIscsiTarget> allNotInAccessibleTargets = 
/* 133 */       new ArrayList<>(Arrays.asList(allTargets));
/* 134 */     if (targetsInAccessibleList != null && targetsInAccessibleList.length > 0) {
/* 135 */       byte b; int i; VsanIscsiTargetBasicInfo[] arrayOfVsanIscsiTargetBasicInfo; for (i = (arrayOfVsanIscsiTargetBasicInfo = targetsInAccessibleList).length, b = 0; b < i; ) { VsanIscsiTargetBasicInfo targetInAccessibleList = arrayOfVsanIscsiTargetBasicInfo[b];
/* 136 */         for (int j = allNotInAccessibleTargets.size() - 1; j >= 0; j--) {
/* 137 */           VsanIscsiTarget unsureTarget = allNotInAccessibleTargets.get(j);
/* 138 */           if (unsureTarget != null && 
/* 139 */             unsureTarget.iqn.equals(targetInAccessibleList.iqn)) {
/* 140 */             allNotInAccessibleTargets.remove(j);
/*     */           }
/*     */         } 
/*     */         b++; }
/*     */     
/*     */     } 
/* 146 */     return allNotInAccessibleTargets.<VsanIscsiTarget>toArray(new VsanIscsiTarget[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private ResultItem[] createResultItems(VsanIscsiTarget[] targets) throws Exception {
/* 151 */     if (targets == null || targets.length == 0) {
/* 152 */       return new ResultItem[0];
/*     */     }
/*     */     
/* 155 */     int targetsCount = targets.length;
/* 156 */     ResultItem[] its = new ResultItem[targetsCount];
/*     */     
/* 158 */     for (int i = 0; i < targetsCount; i++) {
/* 159 */       VsanIscsiTarget target = targets[i];
/* 160 */       ResultItem it = new ResultItem();
/* 161 */       it.resourceObject = 
/* 162 */         new URI("urn:vsaniscsi:VsanIscsiTarget:VsanIscsiTargetList:NO#" + VsanIscsiTargetUriUtil.encode(target.alias));
/* 163 */       it.properties = createPropertyValues(target);
/* 164 */       its[i] = it;
/*     */     } 
/* 166 */     return its;
/*     */   }
/*     */ 
/*     */   
/*     */   private PropertyValue[] createPropertyValues(VsanIscsiTarget target) throws Exception {
/* 171 */     PropertyValue iqn_pv = new PropertyValue();
/* 172 */     iqn_pv.propertyName = "iqn";
/* 173 */     iqn_pv.value = target.iqn;
/*     */     
/* 175 */     PropertyValue alias_pv = new PropertyValue();
/* 176 */     alias_pv.propertyName = "alias";
/* 177 */     alias_pv.value = target.alias;
/*     */     
/* 179 */     PropertyValue authType_pv = new PropertyValue();
/* 180 */     authType_pv.propertyName = "authType";
/* 181 */     authType_pv.value = target.authSpec.authType;
/* 182 */     PropertyValue[] pvs = { iqn_pv, alias_pv, authType_pv };
/* 183 */     return pvs;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/adapter/VsanIscsiTargetDataAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */