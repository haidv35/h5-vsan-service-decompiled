package com.vmware.vsan.client.services.diskGroups.data;

import com.vmware.vim.binding.vim.host.ScsiDisk;
import com.vmware.vise.core.model.data;

@data
public class RemoveDiskSpec {
  public DecommissionMode decommissionMode;
  
  public ScsiDisk[] disks;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/diskGroups/data/RemoveDiskSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */