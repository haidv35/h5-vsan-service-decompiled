package com.vmware.vsphere.client.vsan.base.service;

import com.vmware.vim.binding.vim.VsanUpgradeSystem;
import com.vmware.vim.binding.vim.host.VsanSystem;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vim.vsan.binding.vim.VsanPhoneHomeSystem;
import com.vmware.vim.vsan.binding.vim.VsanUpgradeSystemEx;
import com.vmware.vim.vsan.binding.vim.VsanVcPrecheckerSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanCapabilitySystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanClusterMgmtInternalSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanIscsiTargetSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanObjectSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanPerformanceManager;
import com.vmware.vim.vsan.binding.vim.cluster.VsanSpaceReportSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterConfigSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanVcClusterHealthSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanVcDiskManagementSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanVcStretchedClusterSystem;
import com.vmware.vim.vsan.binding.vim.cluster.VsanVumSystem;
import com.vmware.vim.vsan.binding.vim.host.VsanSystemEx;
import com.vmware.vim.vsan.binding.vim.host.VsanUpdateManager;
import com.vmware.vim.vsan.binding.vim.vsan.VsanVdsSystem;

public interface VsanService {
  String getServiceGuid();
  
  VsanVcStretchedClusterSystem getVsanStretchedClusterSystem();
  
  VsanVcClusterConfigSystem getVsanConfigSystem();
  
  VsanVcDiskManagementSystem getVsanDiskManagementSystem();
  
  VsanPerformanceManager getVsanPerformanceManager();
  
  VsanUpgradeSystemEx getVsanUpgradeSystemEx();
  
  VsanUpgradeSystem getVsanUpgradeSystem();
  
  VsanUpgradeSystem getVsanLegacyUpgradeSystem();
  
  VsanVcClusterHealthSystem getVsanVcClusterHealthSystem();
  
  VsanObjectSystem getVsanObjectSystem();
  
  VsanCapabilitySystem getVsanCapabilitySystem();
  
  VsanSystemEx getVsanSystemEx(ManagedObjectReference paramManagedObjectReference);
  
  VsanSystem getVsanSystem(ManagedObjectReference paramManagedObjectReference);
  
  VsanIscsiTargetSystem getVsanIscsiSystem();
  
  VsanSpaceReportSystem getVsanSpaceReportSystem();
  
  VsanUpdateManager getUpdateManager();
  
  VsanVdsSystem getVdsSystem();
  
  VsanVcPrecheckerSystem getVsanPreCheckerSystem();
  
  <T extends com.vmware.vim.binding.vmodl.ManagedObject> T getManagedObject(ManagedObjectReference paramManagedObjectReference);
  
  void logout();
  
  VsanPhoneHomeSystem getPhoneHomeSystem();
  
  VsanClusterMgmtInternalSystem getVsanClusterMgmtInternalSystem();
  
  VsanVumSystem getVsanVumSystem();
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/base/service/VsanService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */