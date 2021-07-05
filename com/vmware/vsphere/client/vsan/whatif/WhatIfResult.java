package com.vmware.vsphere.client.vsan.whatif;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class WhatIfResult extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public WhatIfData noDataMigration;
  
  public WhatIfData ensureAccessibility;
  
  public WhatIfData fullDataMigration;
  
  public Boolean isWhatIfSupported;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/whatif/WhatIfResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */