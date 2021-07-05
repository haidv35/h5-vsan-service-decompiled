package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;
import java.util.Date;

@data
public class PerfTimeRangeData extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public String name;
  
  public Date from;
  
  public Date to;
  
  public ManagedObjectReference clusterRef;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfTimeRangeData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */