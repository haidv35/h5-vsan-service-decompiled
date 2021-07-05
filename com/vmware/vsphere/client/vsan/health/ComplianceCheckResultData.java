package com.vmware.vsphere.client.vsan.health;

import com.vmware.vim.binding.impl.vmodl.DataObjectImpl;
import com.vmware.vise.core.model.data;

@data
public class ComplianceCheckResultData extends DataObjectImpl {
  private static final long serialVersionUID = 1L;
  
  public ComplianceCheckSummary summary;
  
  public ComplianceCheckResultObj[] details;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsan/health/ComplianceCheckResultData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */