package com.vmware.vsphere.client.vsandp.core.sessionmanager.common;

import java.security.KeyStore;
import java.security.PrivateKey;

public interface LookupSvcLocator {
  public static final String KEYSTORE_JKS = "JKS";
  
  public static final String KEYSTORE_VECS = "VKS";
  
  public static final String H5_ALIAS = "vsphere-webclient";
  
  public static final String TRUSTED_ROOTS_ALIAS = "TRUSTED_ROOTS";
  
  LookupSvcInfo getInfo();
  
  KeyStore getH5Keystore();
  
  PrivateKey getPrivateKey();
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/core/sessionmanager/common/LookupSvcLocator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */