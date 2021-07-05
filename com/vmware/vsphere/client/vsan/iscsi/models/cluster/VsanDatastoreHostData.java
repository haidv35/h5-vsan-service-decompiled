package com.vmware.vsphere.client.vsan.iscsi.models.cluster;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class VsanDatastoreHostData extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference vsanDatastoreRef;
  
  public ManagedObjectReference hostRef;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/cluster/VsanDatastoreHostData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */