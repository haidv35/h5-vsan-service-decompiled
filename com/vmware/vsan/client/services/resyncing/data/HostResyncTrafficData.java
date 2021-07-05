package com.vmware.vsan.client.services.resyncing.data;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class HostResyncTrafficData {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference hostRef;
  
  public String primaryIconId;
  
  public String name;
  
  public int resyncTraffic;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/resyncing/data/HostResyncTrafficData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */