package com.vmware.vsan.client.services.inventory;

import com.vmware.vim.binding.vmodl.ManagedObjectReference;
import com.vmware.vise.core.model.data;

@data
public class InventoryEntryData {
  public ManagedObjectReference nodeRef;
  
  public String name;
  
  public boolean isLeafNode;
  
  public String iconShape;
  
  public boolean connected;
  
  public boolean isDrsEnabled;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/inventory/InventoryEntryData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */