package com.vmware.vsphere.client.vsan.stretched;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class WitnessHostValidationResult extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference witnessHostRef;
  
  public boolean isHostInTheSameCluster;
  
  public boolean isHostInVsanEnabledCluster;
  
  public boolean hasVsanEnabledNic;
  
  public boolean isHostConnected;
  
  public boolean isPoweredOn;
  
  public boolean isHostInMaintenanceMode;
  
  public boolean isStretchedClusterSupported;
  
  public boolean canClaimHybridGroup;
  
  public boolean hasDiskGroups;
  
  public long claimedCapacity;
  
  public boolean isExternalWitness;
  
  public boolean isEncrypted;
  
  public boolean autoClaimMode;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/WitnessHostValidationResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */