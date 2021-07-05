package com.vmware.vsphere.client.vsan.iscsi.models.config;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetServiceConfig;
import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectInformation;
import com.vmware.vise.core.model.data;

@data
public class VsanIscsiConfig extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public VsanIscsiTargetServiceConfig vsanIscsiTargetServiceConfig;
  
  public VsanObjectInformation vsanObjectInformation;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/config/VsanIscsiConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */