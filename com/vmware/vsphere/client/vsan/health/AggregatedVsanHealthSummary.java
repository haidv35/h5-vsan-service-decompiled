package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class AggregatedVsanHealthSummary extends DataObjectImpl {
  public HardwareOverallHealth hostSummary;
  
  public HardwareOverallHealth physicalDiskSummary;
  
  public Boolean networkIssueDetected;
  
  public HardwareOverallHealth vmSummary;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/AggregatedVsanHealthSummary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */