package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource;

public interface ResourceFactory<R extends Resource, S> {
  R acquire(S paramS);
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/ResourceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */