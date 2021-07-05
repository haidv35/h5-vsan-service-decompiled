package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class VsanTestTable extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String title;
  
  public VsanTestColumn[] columns;
  
  public VsanTestRow[] rows;
  
  public boolean showHeader;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanTestTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */