package com.vmware.vsphere.client.vsan.spec;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class VsanSemiAutoDiskMappingsSpec extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference clusterRef;
  
  public ManagedObjectReference hostRef;
  
  public VsanSemiAutoDiskSpec[] disks;
  
  public boolean isAllFlashSupported;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/spec/VsanSemiAutoDiskMappingsSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */