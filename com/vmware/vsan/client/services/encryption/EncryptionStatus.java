package com.vmware.vsan.client.services.encryption;

import com.vmware.vise.core.model.data;
import com.vmware.vsphere.client.vsan.data.EncryptionState;

@data
public class EncryptionStatus {
  public EncryptionState state;
  
  public String kmipClusterId;
  
  public Boolean eraseDisksBeforeUse;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/encryption/EncryptionStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */