package com.vmware.vsan.client.services.hci.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class DvpgData {
  public String name;
  
  public ManagedObjectReference existingDvpg;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/DvpgData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */