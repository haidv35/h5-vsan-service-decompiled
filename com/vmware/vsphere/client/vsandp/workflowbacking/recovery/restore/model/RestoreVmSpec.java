package com.vmware.vsphere.client.vsandp.workflowbacking.recovery.restore.model;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;
import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;

@data
public class RestoreVmSpec {
  public VmProtectionInstance[] selectedSyncPoints;
  
  public String storagePolicyId;
  
  public ManagedObjectReference selectedVmFolder;
  
  public ManagedObjectReference selectedNetwork;
  
  public ManagedObjectReference selectedResourcePool;
  
  public String vmName;
  
  public boolean powerOn;
  
  public boolean createIndependentVm;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/workflowbacking/recovery/restore/model/RestoreVmSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */