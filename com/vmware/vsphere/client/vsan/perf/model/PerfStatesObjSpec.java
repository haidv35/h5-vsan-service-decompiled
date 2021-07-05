package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class PerfStatesObjSpec {
  public ManagedObjectReference clusterRef;
  
  public String profileId;
  
  public boolean isVerboseEnabled;
  
  public boolean isNetworkDiagnosticModeEnabled;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfStatesObjSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */