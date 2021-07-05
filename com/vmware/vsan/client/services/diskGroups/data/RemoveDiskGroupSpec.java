package com.vmware.vsan.client.services.diskGroups.data;

import com.vmware.vise.core.model.data;

@data
public class RemoveDiskGroupSpec {
  public DecommissionMode decommissionMode;
  
  public VsanDiskMapping[] mappings;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskGroups/data/RemoveDiskGroupSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */