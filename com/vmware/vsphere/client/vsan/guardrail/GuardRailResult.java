package com.vmware.vsphere.client.vsan.guardrail;

import com.vmware.vise.core.model.data;
import com.vmware.vsan.client.services.resyncing.data.RepairTimerData;

@data
public class GuardRailResult {
  public String[] hostsInMaintenanceMode;
  
  public boolean resyncCollected;
  
  public boolean isClusterInResync;
  
  public Long objectsToSyncCount;
  
  public Long recoveryETA;
  
  public RepairTimerData repairTimerData;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/guardrail/GuardRailResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */