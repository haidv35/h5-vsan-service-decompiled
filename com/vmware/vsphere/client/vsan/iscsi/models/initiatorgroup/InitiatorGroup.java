/*    */ package com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup;
/*    */ 
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiInitiatorGroup;
/*    */ import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetBasicInfo;
/*    */ import com.vmware.vise.core.model.data;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.initiator.InitiatorGroupInitiator;
/*    */ import com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.target.InitiatorGroupTarget;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @data
/*    */ public class InitiatorGroup
/*    */ {
/*    */   public String name;
/* 20 */   public List<InitiatorGroupInitiator> initiators = new ArrayList<>();
/* 21 */   public List<InitiatorGroupTarget> targets = new ArrayList<>();
/*    */   
/*    */   public InitiatorGroup(VsanIscsiInitiatorGroup initiatorGroup) {
/* 24 */     String[] groupInitiators = initiatorGroup.getInitiators();
/* 25 */     VsanIscsiTargetBasicInfo[] targetBasicInfos = initiatorGroup.getTargets();
/* 26 */     this.name = initiatorGroup.getName();
/* 27 */     if (ArrayUtils.isNotEmpty((Object[])groupInitiators)) {
/* 28 */       byte b; int i; String[] arrayOfString; for (i = (arrayOfString = groupInitiators).length, b = 0; b < i; ) { String initiatorName = arrayOfString[b];
/* 29 */         InitiatorGroupInitiator initiator = new InitiatorGroupInitiator();
/* 30 */         initiator.name = initiatorName;
/* 31 */         this.initiators.add(initiator); b++; }
/*    */     
/*    */     } 
/* 34 */     if (ArrayUtils.isNotEmpty((Object[])targetBasicInfos)) {
/* 35 */       byte b; int i; VsanIscsiTargetBasicInfo[] arrayOfVsanIscsiTargetBasicInfo; for (i = (arrayOfVsanIscsiTargetBasicInfo = targetBasicInfos).length, b = 0; b < i; ) { VsanIscsiTargetBasicInfo target = arrayOfVsanIscsiTargetBasicInfo[b];
/* 36 */         this.targets.add(new InitiatorGroupTarget(target.getAlias(), target.getIqn()));
/*    */         b++; }
/*    */     
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/initiatorgroup/InitiatorGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */