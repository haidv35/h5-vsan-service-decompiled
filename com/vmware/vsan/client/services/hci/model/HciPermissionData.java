package com.vmware.vsan.client.services.hci.model;

import com.vmware.vise.core.model.data;

@data
public class HciPermissionData {
  public boolean hasEditClusterBasics;
  
  public boolean hasAddHosts;
  
  public boolean hasConfigureCluster;
  
  public boolean hasEditCluster;
  
  public boolean hasRenameCluster;
  
  public boolean hasAddStandaloneHost;
  
  public boolean hasMoveHost;
  
  public boolean hasDvsCreate;
  
  public boolean hasDvsModify;
  
  public boolean hasCreatePortgroup;
  
  public boolean hasHostNetwrokConfig;
  
  public boolean hasNetworkAssign;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/HciPermissionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */