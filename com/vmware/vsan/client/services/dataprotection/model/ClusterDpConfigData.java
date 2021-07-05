package com.vmware.vsan.client.services.dataprotection.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class ClusterDpConfigData {
  public String archivalDpDatastoreName;
  
  public String archivalDpDatastoreUrl;
  
  public ManagedObjectReference archivalDpDatastoreRef;
  
  public Integer usageThreshold;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/dataprotection/model/ClusterDpConfigData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */