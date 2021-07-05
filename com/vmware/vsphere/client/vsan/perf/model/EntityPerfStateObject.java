package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vim.vsan.binding.vim.cluster.VsanPerfEntityMetricCSV;
import com.vmware.vise.core.model.data;

@data
public class EntityPerfStateObject {
  public String errorMessage;
  
  public VsanPerfEntityMetricCSV[] metrics;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/EntityPerfStateObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */