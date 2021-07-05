package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vise.core.model.data;
import java.util.List;

@data
public class PerfVirtualMachineDiskData {
  public List<PerfVirtualDiskEntity> virtualDisks;
  
  public List<PerfVscsiEntity> vscsiEntities;
  
  public String vmUuid;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfVirtualMachineDiskData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */