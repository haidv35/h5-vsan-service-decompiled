package com.vmware.vsphere.client.vsan.spec;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vim.host.ScsiDisk;
import com.vmware.vise.core.model.data;

@data
public class VsanQueryDataEvacuationInfoSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ScsiDisk[] disks;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/spec/VsanQueryDataEvacuationInfoSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */