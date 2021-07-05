package com.vmware.vsphere.client.vsan.iscsi.models.initiatorgroup.initiator;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class InitiatorGroupInitiatorRemoveSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String initiatorGroupName;
  
  public String initiatorName;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/initiatorgroup/initiator/InitiatorGroupInitiatorRemoveSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */