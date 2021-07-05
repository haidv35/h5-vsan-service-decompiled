package com.vmware.vsphere.client.vsan.base.service;

public interface VsanServiceFactory {
  VsanService getService(String paramString);
  
  String getSessionKey(String paramString);
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanServiceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */