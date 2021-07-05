package com.vmware.vsan.client.services.diskGroups.data;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class DiskMappingSpec {
  public ManagedObjectReference clusterRef;
  
  public VsanDiskMapping[] mappings;
  
  public boolean isAllFlashSupported;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskGroups/data/DiskMappingSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */