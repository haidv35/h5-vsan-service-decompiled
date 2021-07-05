package com.vmware.vsphere.client.vsan.stretched;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vim.binding.vim.vsan.host.DiskMapping;
import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;
import com.vmware.vsan.client.services.ProxygenSerializer.ElementType;
import java.util.List;

@data
public class VsanStretchedClusterConfig extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ManagedObjectReference witnessHost;
  
  public DiskMapping witnessHostDiskMapping;
  
  public String preferredSiteName;
  
  @ElementType(ManagedObjectReference.class)
  public List<ManagedObjectReference> preferredSiteHosts;
  
  public String secondarySiteName;
  
  @ElementType(ManagedObjectReference.class)
  public List<ManagedObjectReference> secondarySiteHosts;
  
  public boolean isFaultDomainConfigurationChanged;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/stretched/VsanStretchedClusterConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */