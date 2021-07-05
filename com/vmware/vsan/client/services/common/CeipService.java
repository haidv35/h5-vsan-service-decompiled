/*    */ package com.vmware.vsan.client.services.common;
/*    */ 
/*    */ import com.vmware.proxygen.ts.TsService;
/*    */ import com.vmware.vim.binding.vim.option.OptionManager;
/*    */ import com.vmware.vim.binding.vim.option.OptionValue;
/*    */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.common.VcClient;
/*    */ import com.vmware.vsphere.client.vsandp.core.sessionmanager.vlsi.client.vc.VcConnection;
/*    */ import java.util.Iterator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.ObjectMapper;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class CeipService
/*    */ {
/* 26 */   private Logger logger = LoggerFactory.getLogger(CeipService.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private VcClient vcClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @TsService
/*    */   public boolean getCeipServiceEnabled(ManagedObjectReference clusterRef) throws Exception {
/*    */     OptionValue optionValues[], optionValue;
/* 42 */     Exception exception1 = null, exception2 = null; try { VcConnection vcConnection = this.vcClient.getConnection(clusterRef.getServerGuid()); 
/* 43 */       try { OptionManager optionManager = (OptionManager)vcConnection.createStub(OptionManager.class, (vcConnection.getContent()).setting);
/* 44 */         optionValues = optionManager.queryView("VirtualCenter.DataCollector.ConsentData"); }
/* 45 */       finally { if (vcConnection != null) vcConnection.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */        }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 52 */     ObjectMapper mapper = new ObjectMapper();
/*    */     
/*    */     try {
/* 55 */       JsonNode rootNode = mapper.readTree((String)optionValue.value);
/* 56 */       JsonNode consentConfigNodes = rootNode.get("consentConfigurations");
/* 57 */       if (consentConfigNodes == null) {
/* 58 */         return false;
/*    */       }
/* 60 */       Iterator<JsonNode> consentNodesIterator = consentConfigNodes.getElements();
/* 61 */       while (consentNodesIterator.hasNext()) {
/* 62 */         JsonNode consentNode = consentNodesIterator.next();
/* 63 */         JsonNode consentIdNode = consentNode.get("consentId");
/* 64 */         if (consentIdNode != null && consentIdNode.getIntValue() == 2) {
/* 65 */           JsonNode consentEnabledNode = consentNode.get("consentAccepted");
/* 66 */           if (consentEnabledNode != null) {
/* 67 */             return consentEnabledNode.getBooleanValue();
/*    */           }
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 73 */     } catch (Exception ex) {
/* 74 */       this.logger.error("Error parsing the information for CEIP service enabled", ex);
/*    */ 
/*    */ 
/*    */       
/* 78 */       return true;
/*    */     } 
/*    */     
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/common/CeipService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */