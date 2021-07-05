package com.vmware.vsphere.client.vsan.data;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class VsanHostData extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String name;
  
  public String nodeUuid;
  
  public String faultDomainName;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/data/VsanHostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */