package com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.target;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class InitiatorGroupTargetRemoveSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String initiatorGroupName;
  
  public String targetAlias;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/initiatorgroup/target/InitiatorGroupTargetRemoveSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */