package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vise.core.model.data;
import java.util.List;

@data
public class HostVnicsData {
  public String hostName;
  
  public List<PerfVnicEntity> vnics;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/HostVnicsData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */