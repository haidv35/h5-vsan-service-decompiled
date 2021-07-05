package com.vmware.vsphere.client.vsan.base.service;

import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;

public interface VsphereHealthService {
  VsanVcClusterHealthSystem getVsphereHealthSystem();
  
  void logout();
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsphereHealthService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */