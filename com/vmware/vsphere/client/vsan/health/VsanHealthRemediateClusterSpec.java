package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class VsanHealthRemediateClusterSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference clusterRef;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanHealthRemediateClusterSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */