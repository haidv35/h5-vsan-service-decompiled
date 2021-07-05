package com.vmware.vsphere.client.vsan.perf.model;

import com.vmware.vise.core.model.data;
import java.util.List;

@data
public class PerfGraphMetricsData {
  public List<Double> values;
  
  public PerfGraphThreshold threshold;
  
  public String key;
  
  public String subKey;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/perf/model/PerfGraphMetricsData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */