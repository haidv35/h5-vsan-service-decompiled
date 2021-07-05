package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class GetClosestSyncPointsSpec {
  public Long targetTime;
  
  public ManagedObjectReference[] vmRefs;
  
  public boolean restoreOnlyFromLocal;
  
  public boolean restoreOnlyFromQuiesced;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/model/GetClosestSyncPointsSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */