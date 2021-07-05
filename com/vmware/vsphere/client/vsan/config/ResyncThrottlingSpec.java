package com.vmware.vsphere.client.vsan.config;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class ResyncThrottlingSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public int iopsLimit;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/config/ResyncThrottlingSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */