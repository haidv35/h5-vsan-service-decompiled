package com.vmware.vsphere.client.vsan.upgrade;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class VsanUpgradeSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public boolean performObjectUpgrade;
  
  public boolean downgradeFormat;
  
  public boolean allowReducedRedundancy;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/upgrade/VsanUpgradeSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */