package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vise.core.model.data;

@data
public class PerfVirtualDiskEntity {
  public String vmdkPath;
  
  public String datastorePath;
  
  public String datastoreName;
  
  public String diskName;
  
  public int controllerKey;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfVirtualDiskEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */