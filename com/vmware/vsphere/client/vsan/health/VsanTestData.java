/*     */ package com.vmware.vsphere.client.vsan.health;
/*     */ 
/*     */ import com.vmware.vim.binding.vmodl.ManagedObjectReference;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthAction;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthGroup;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultBase;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultColumnInfo;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultRow;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultTable;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthResultValues;
/*     */ import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterHealthTest;
/*     */ import com.vmware.vise.core.model.data;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectDataProtectionHealthState;
/*     */ import com.vmware.vsphere.client.vsan.base.data.VsanObjectHealthState;
/*     */ import com.vmware.vsphere.client.vsan.base.util.BaseUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ @data
/*     */ public class VsanTestData
/*     */ {
/*     */   public String testId;
/*     */   public String testName;
/*     */   public String testDescription;
/*     */   public String testShortDescription;
/*     */   public Integer numberOfHealthyEntities;
/*     */   public Integer numberOfAllEntities;
/*     */   public VsanHealthStatus status;
/*     */   public List<VsanTestTable> details;
/*     */   public List<VsanTestData> subtests;
/*     */   public String helpId;
/*     */   public List<VsanClusterHealthAction> actions;
/*     */   
/*     */   public VsanTestData() {}
/*     */   
/*     */   public VsanTestData(VsanClusterHealthGroup healthGroup, Map<ManagedObjectReference, String> moRefToNameMap) {
/*  82 */     this.testName = healthGroup.groupName;
/*     */     
/*  84 */     this.status = VsanHealthStatus.valueOf(healthGroup.groupHealth);
/*  85 */     this.details = getTestDetails(healthGroup.groupDetails, moRefToNameMap);
/*  86 */     this.subtests = new ArrayList<>();
/*  87 */     if (healthGroup.groupTests != null) {
/*  88 */       byte b; int i; VsanClusterHealthTest[] arrayOfVsanClusterHealthTest; for (i = (arrayOfVsanClusterHealthTest = healthGroup.groupTests).length, b = 0; b < i; ) { VsanClusterHealthTest test = arrayOfVsanClusterHealthTest[b];
/*  89 */         this.subtests.add(new VsanTestData(test, moRefToNameMap));
/*     */         b++; }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public VsanTestData(VsanClusterHealthTest test, Map<ManagedObjectReference, String> moRefToNameMap) {
/*  96 */     this.testId = wrapTestId(test.testId);
/*  97 */     this.testName = test.testName;
/*  98 */     this.testDescription = test.testDescription;
/*  99 */     this.testShortDescription = test.testShortDescription;
/* 100 */     this.numberOfHealthyEntities = test.testHealthyEntities;
/* 101 */     this.numberOfAllEntities = test.testAllEntities;
/* 102 */     this.status = VsanHealthStatus.valueOf(test.testHealth);
/* 103 */     this.details = getTestDetails(test.testDetails, moRefToNameMap);
/* 104 */     this.helpId = test.testId;
/*     */     
/* 106 */     if (test.testActions != null) {
/* 107 */       this.actions = new ArrayList<>(test.testActions.length); byte b; int i;
/*     */       VsanClusterHealthAction[] arrayOfVsanClusterHealthAction;
/* 109 */       for (i = (arrayOfVsanClusterHealthAction = test.testActions).length, b = 0; b < i; ) { VsanClusterHealthAction vlha = arrayOfVsanClusterHealthAction[b];
/* 110 */         this.actions.add(vlha);
/*     */         b++; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String wrapTestId(String testId) {
/* 120 */     if (StringUtils.isEmpty(testId)) {
/* 121 */       return "";
/*     */     }
/*     */     
/* 124 */     String[] substrs = StringUtils.split(testId, ".");
/* 125 */     return substrs[substrs.length - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   private List<VsanTestTable> getTestDetails(VsanClusterHealthResultBase[] testDetails, Map<ManagedObjectReference, String> moRefToNameMap) {
/* 130 */     if (testDetails == null) {
/* 131 */       return new ArrayList<>();
/*     */     }
/* 133 */     List<VsanTestTable> testTables = new ArrayList<>(); byte b; int i;
/*     */     VsanClusterHealthResultBase[] arrayOfVsanClusterHealthResultBase;
/* 135 */     for (i = (arrayOfVsanClusterHealthResultBase = testDetails).length, b = 0; b < i; ) { VsanClusterHealthResultBase baseResult = arrayOfVsanClusterHealthResultBase[b];
/* 136 */       VsanTestTable testTable = null;
/* 137 */       if (baseResult instanceof VsanClusterHealthResultTable) {
/* 138 */         testTable = 
/* 139 */           createTestTable((VsanClusterHealthResultTable)baseResult, moRefToNameMap);
/* 140 */       } else if (baseResult instanceof VsanClusterHealthResultValues) {
/* 141 */         testTable = createTestTable((VsanClusterHealthResultValues)baseResult);
/*     */       } 
/* 143 */       testTables.add(testTable);
/*     */       b++; }
/*     */     
/* 146 */     return testTables;
/*     */   }
/*     */   
/*     */   private VsanTestTable createTestTable(VsanClusterHealthResultValues parameters) {
/* 150 */     VsanTestTable testTable = new VsanTestTable();
/* 151 */     testTable.showHeader = false;
/* 152 */     testTable.title = parameters.label;
/*     */     
/* 154 */     VsanTestColumn column = new VsanTestColumn("", ColumnType.string);
/* 155 */     testTable.columns = new VsanTestColumn[] { column };
/*     */     
/* 157 */     List<VsanTestRow> rows = new ArrayList<>();
/* 158 */     if (parameters.values == null)
/* 159 */       return testTable;  byte b; int i;
/*     */     String[] arrayOfString;
/* 161 */     for (i = (arrayOfString = parameters.values).length, b = 0; b < i; ) { String parameter = arrayOfString[b];
/* 162 */       rows.add(createVsanTestRow(parameter)); b++; }
/*     */     
/* 164 */     testTable.rows = rows.<VsanTestRow>toArray(new VsanTestRow[rows.size()]);
/*     */     
/* 166 */     return testTable;
/*     */   }
/*     */ 
/*     */   
/*     */   private VsanTestTable createTestTable(VsanClusterHealthResultTable tableResult, Map<ManagedObjectReference, String> moRefToNameMap) {
/* 171 */     VsanTestTable testTable = new VsanTestTable();
/* 172 */     testTable.showHeader = true;
/* 173 */     testTable.title = tableResult.label;
/* 174 */     testTable.columns = new VsanTestColumn[tableResult.columns.length];
/*     */ 
/*     */     
/* 177 */     for (int i = 0; i < tableResult.columns.length; i++) {
/* 178 */       VsanClusterHealthResultColumnInfo columnInfo = tableResult.columns[i];
/*     */       try {
/* 180 */         ColumnType columnType = ColumnType.valueOf(columnInfo.type);
/* 181 */         testTable.columns[i] = new VsanTestColumn(columnInfo.label, columnType);
/* 182 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 187 */     if (tableResult.rows == null) {
/* 188 */       return testTable;
/*     */     }
/*     */     
/* 191 */     List<VsanTestRow> rows = new ArrayList<>(); byte b; int j; VsanClusterHealthResultRow[] arrayOfVsanClusterHealthResultRow;
/* 192 */     for (j = (arrayOfVsanClusterHealthResultRow = tableResult.rows).length, b = 0; b < j; ) { VsanClusterHealthResultRow resultRow = arrayOfVsanClusterHealthResultRow[b];
/* 193 */       VsanTestRow row = createVsanTestRow(resultRow, testTable.columns, moRefToNameMap);
/* 194 */       rows.add(row);
/*     */       b++; }
/*     */     
/* 197 */     testTable.rows = rows.<VsanTestRow>toArray(new VsanTestRow[rows.size()]);
/*     */     
/* 199 */     return testTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getServerGuid(Map<ManagedObjectReference, String> moRefToNameMap) {
/* 208 */     return (moRefToNameMap != null && !moRefToNameMap.isEmpty()) ? (
/* 209 */       (ManagedObjectReference)moRefToNameMap.keySet().iterator().next()).getServerGuid() : 
/* 210 */       null;
/*     */   }
/*     */ 
/*     */   
/*     */   private ObjectWithName getObjectWithName(String morString, Map<ManagedObjectReference, String> moRefToNameMap, String serverGuid) {
/* 215 */     ManagedObjectReference moRef = BaseUtils.generateMor(morString, serverGuid);
/* 216 */     String name = moRefToNameMap.get(moRef);
/* 217 */     return new ObjectWithName(name, moRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VsanTestRow createVsanTestRow(VsanClusterHealthResultRow resultRow, VsanTestColumn[] columns, Map<ManagedObjectReference, String> moRefToNameMap) {
/* 224 */     String serverGuid = getServerGuid(moRefToNameMap);
/*     */     
/* 226 */     VsanTestCell[] values = new VsanTestCell[resultRow.values.length];
/* 227 */     for (int i = 0; i < resultRow.values.length; i++) {
/* 228 */       String rowValue = resultRow.values[i];
/* 229 */       if (StringUtils.isEmpty(rowValue)) {
/* 230 */         values[i] = new VsanTestCell();
/*     */       } else {
/*     */         VsanObjectHealthState vsanObjectHealthState; VsanObjectDataProtectionHealthState vsanObjectDataProtectionHealthState; String str;
/* 233 */         ColumnType cellType = (columns[i]).columnType;
/* 234 */         if (ColumnType.dynamic.equals(cellType)) {
/*     */           try {
/* 236 */             cellType = ColumnType.valueOf(rowValue.split(":")[0]);
/* 237 */             rowValue = rowValue.substring(cellType.toString().length() + 1);
/* 238 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 243 */         Object<ObjectWithName> cellValue = null;
/* 244 */         switch (cellType) {
/*     */           case mor:
/* 246 */             if (rowValue != null && rowValue.length() > 0 && serverGuid != null) {
/* 247 */               cellValue = (Object<ObjectWithName>)getObjectWithName(rowValue, moRefToNameMap, serverGuid);
/*     */             }
/*     */             break;
/*     */           case listMor:
/* 251 */             if (rowValue != null && rowValue.length() > 0 && serverGuid != null) {
/* 252 */               List<ObjectWithName> listMofs = new ArrayList<>(); byte b; int j; String[] arrayOfString;
/* 253 */               for (j = (arrayOfString = rowValue.split(",")).length, b = 0; b < j; ) { String mofStr = arrayOfString[b];
/* 254 */                 listMofs.add(getObjectWithName(mofStr, moRefToNameMap, serverGuid)); b++; }
/*     */               
/* 256 */               cellValue = (Object<ObjectWithName>)listMofs;
/*     */             } 
/*     */             break;
/*     */           case health:
/* 260 */             cellValue = (Object<ObjectWithName>)VsanHealthStatus.valueOf(rowValue);
/*     */             break;
/*     */           case vsanObjectHealth:
/* 263 */             vsanObjectHealthState = VsanObjectHealthState.fromServerLocalizedString(rowValue);
/*     */             break;
/*     */           case vsanDataProtectionObjectHealth:
/* 266 */             vsanObjectDataProtectionHealthState = VsanObjectDataProtectionHealthState.fromServerLocalizedString(rowValue);
/*     */             break;
/*     */           default:
/* 269 */             str = rowValue;
/*     */             break;
/*     */         } 
/* 272 */         values[i] = new VsanTestCell(cellType, str);
/*     */       } 
/* 274 */     }  VsanTestRow row = new VsanTestRow();
/* 275 */     row.rowValues = values;
/* 276 */     row.nestedRows = getNestedRows(resultRow, columns, moRefToNameMap);
/* 277 */     return row;
/*     */   }
/*     */   
/*     */   private VsanTestRow createVsanTestRow(String value) {
/* 281 */     VsanTestRow row = new VsanTestRow();
/* 282 */     VsanTestCell cell = new VsanTestCell(ColumnType.string, value);
/* 283 */     row.rowValues = new VsanTestCell[] { cell };
/* 284 */     return row;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<VsanTestRow> getNestedRows(VsanClusterHealthResultRow resultRow, VsanTestColumn[] columns, Map<ManagedObjectReference, String> moRefToNameMap) {
/* 290 */     List<VsanTestRow> nestedRows = new ArrayList<>();
/* 291 */     if (resultRow.nestedRows == null)
/* 292 */       return nestedRows;  byte b; int i;
/*     */     VsanClusterHealthResultRow[] arrayOfVsanClusterHealthResultRow;
/* 294 */     for (i = (arrayOfVsanClusterHealthResultRow = resultRow.nestedRows).length, b = 0; b < i; ) { VsanClusterHealthResultRow nestedResultRow = arrayOfVsanClusterHealthResultRow[b];
/* 295 */       VsanTestRow nestedRow = createVsanTestRow(nestedResultRow, columns, moRefToNameMap);
/* 296 */       nestedRows.add(nestedRow); b++; }
/*     */     
/* 298 */     return nestedRows;
/*     */   }
/*     */ }


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanTestData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */