package com.vmware.vsphere.client.vsan.iscsi.models.config;

import com.vmware.vise.core.model.data;
import com.vmware.vsphere.client.vsan.base.data.StoragePolicyData;

@data
public class VsanIscsiConfigEditSpec {
  public Boolean enableIscsiTargetService;
  
  public String network;
  
  public Integer port;
  
  public VsanIscsiAuthSpec authSpec;
  
  public StoragePolicyData policy;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/iscsi/models/config/VsanIscsiConfigEditSpec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */