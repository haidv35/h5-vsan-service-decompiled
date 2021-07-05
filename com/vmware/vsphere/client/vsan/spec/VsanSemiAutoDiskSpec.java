package com.vmware.vsphere.client.vsan.spec;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vim.host.ScsiDisk;
import com.vmware.vise.core.model.data;
import com.vmware.vsphere.client.vsan.data.ClaimOption;

@data
public class VsanSemiAutoDiskSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ScsiDisk disk;
  
  public ClaimOption claimOption;
  
  public boolean markedAsFlash;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/spec/VsanSemiAutoDiskSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */