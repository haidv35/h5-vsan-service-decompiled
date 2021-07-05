package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.data;
import java.util.List;

@data
public class VsanHealthServicePreCheckResult extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public boolean passed;
  
  public boolean vumRegistered;
  
  public List<VsanTestData> testsData;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanHealthServicePreCheckResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */