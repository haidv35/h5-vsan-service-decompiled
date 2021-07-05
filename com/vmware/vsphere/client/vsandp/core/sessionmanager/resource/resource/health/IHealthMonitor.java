package com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.health;

public interface IHealthMonitor<R extends com.vmware.vsphere.client.vsandp.core.sessionmanager.resource.resource.Resource, S> {
  void onCreated(R paramR, S paramS);
  
  void onDisposed(R paramR, S paramS);
  
  void check(R paramR, S paramS) throws Exception;
  
  void onError(R paramR, S paramS, Throwable paramThrowable);
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/resource/resource/health/IHealthMonitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */