package com.vmware.vsphere.client.vsan.health;

import com.vmware.vise.core.model.data;

@data
public class CapacityOverviewData {
  public long provisionedSpace;
  
  public long usedSpace;
  
  public long totalSpace;
  
  public long reservedSpace;
  
  public long physicalUsedSpace;
  
  public long overReservedSpace;
  
  public long freeSpace;
  
  public long vsanOverheadSpace;
  
  public long vsanDpOverheadSpace;
  
  public long vsanDpFragmentationOverheadSpace;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/CapacityOverviewData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */