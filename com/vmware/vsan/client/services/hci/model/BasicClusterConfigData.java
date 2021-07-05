package com.vmware.vsan.client.services.hci.model;

import com.vmware.vise.core.model.data;
import java.util.Map;

@data
public class BasicClusterConfigData {
  public int hosts;
  
  public int notConfiguredHosts;
  
  public HciWorkflowState hciWorkflowState;
  
  public Map<Service, DvsData> dvsDataByService;
  
  public boolean haEnabled;
  
  public boolean drsEnabled;
  
  public boolean vsanEnabled;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/BasicClusterConfigData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */