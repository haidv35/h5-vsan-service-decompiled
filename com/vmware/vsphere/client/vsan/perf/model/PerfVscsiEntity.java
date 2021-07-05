package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vise.core.model.data;

@data
public class PerfVscsiEntity {
  public Integer busId;
  
  public Integer position;
  
  public String vmdkName;
  
  public String deviceName;
  
  public int controllerKey;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfVscsiEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */