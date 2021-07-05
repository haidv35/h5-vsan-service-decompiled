package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityType;
import com.vmware.vise.core.model.data;
import java.util.Map;

@data
public class CapacityHistoryBasicInfo {
  public Map<String, VsanPerfEntityType> entityTypes;
  
  public boolean isPerformanceServiceEnabled;
  
  public boolean hasEditPermission;
  
  public boolean hasReadPoliciesPermission;
  
  public ManagedObjectReference clusterRef;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/CapacityHistoryBasicInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */