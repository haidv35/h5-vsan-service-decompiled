package com.vmware.vsphere.client.vsan.health;

import com.vmware.vise.core.model.data;
import java.util.List;

@data
public class VsanSpaceUsageDataModel {
  public long totalCapacityB;
  
  public long totalUsedB;
  
  public CapacityOverviewData overview;
  
  public List<VsanObjectSpaceSummaryDataModel> spaceDetail;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanSpaceUsageDataModel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */