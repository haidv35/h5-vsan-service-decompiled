package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.provider.pits;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vim.vsandp.binding.vim.vsandp.GroupInstanceData;
import com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.VmProtectionInstance;
import com.vmware.vsphere.client.vsandp.data.ProtectionType;
import java.util.TreeSet;

public interface PitProvider {
  TreeSet<VmProtectionInstance> getLocalPits(ManagedObjectReference paramManagedObjectReference) throws Exception;
  
  TreeSet<VmProtectionInstance> getArchivePits(ManagedObjectReference paramManagedObjectReference) throws Exception;
  
  VmProtectionInstance createProtectionInstance(String paramString, ProtectionType paramProtectionType, GroupInstanceData paramGroupInstanceData);
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/provider/pits/PitProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */