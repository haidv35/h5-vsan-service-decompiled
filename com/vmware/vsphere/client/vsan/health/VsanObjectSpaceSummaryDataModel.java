package com.vmware.vsphere.client.vsan.health;

import com.vmware.vise.core.model.data;

@data
public class VsanObjectSpaceSummaryDataModel {
  public long physicalUsedSpace;
  
  public long reservedSpace;
  
  public String objectType;
  
  public long overheadSpace;
  
  public long tempOverheadSpace;
  
  public long primaryCapacitySpace;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanObjectSpaceSummaryDataModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */