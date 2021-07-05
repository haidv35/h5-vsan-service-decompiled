package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class VMCreationTestData extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String hostName;
  
  public VsanVMCreationStatus state;
  
  public Exception fault;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VMCreationTestData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */