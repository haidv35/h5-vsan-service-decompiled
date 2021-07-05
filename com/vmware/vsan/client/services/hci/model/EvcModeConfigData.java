package com.vmware.vsan.client.services.hci.model;

import com.vmware.vise.core.model.data;
import java.util.List;

@data
public class EvcModeConfigData {
  public boolean enabled;
  
  public boolean unsupportedEvcStatus;
  
  public EvcModeData selectedEvcMode;
  
  public List<EvcModeData> supportedIntelEvcMode;
  
  public List<EvcModeData> supportedAmdEvcMode;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/EvcModeConfigData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */