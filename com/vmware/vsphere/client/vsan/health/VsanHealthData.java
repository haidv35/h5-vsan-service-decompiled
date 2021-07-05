package com.vmware.vsphere.client.vsan.health;

import com.vmware.vise.core.model.data;
import java.util.Calendar;
import java.util.List;

@data
public class VsanHealthData {
  public VsanHealthStatus status;
  
  public String description;
  
  public String helpId;
  
  public List<VsanTestData> testsData;
  
  public Calendar timeStamp;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/VsanHealthData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */