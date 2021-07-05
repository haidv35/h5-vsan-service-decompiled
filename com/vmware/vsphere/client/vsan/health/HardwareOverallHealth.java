package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class HardwareOverallHealth extends DataObjectImpl {
  public Integer total;
  
  public Integer issueCount;
  
  public String overallStatus;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/HardwareOverallHealth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */