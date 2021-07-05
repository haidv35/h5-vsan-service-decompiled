package com.vmware.vsan.client.services.hci.model;

import com.vmware.vise.core.model.data;

@data
public class ConfigureWizardData {
  public boolean openYesNoDialog;
  
  public boolean openWarningDialog;
  
  public String dialogText;
  
  public String[] warningDialogContent;
  
  public boolean isExtend;
  
  public boolean isStandalone;
  
  public boolean optOutOfNetworking;
  
  public boolean optOutOfNetworkingDisabled;
  
  public boolean enableFaultDomainForSingleSiteCluster;
  
  public boolean largeScaleClusterSupport;
  
  public boolean showDvsPage;
  
  public boolean showVmotionTrafficPage;
  
  public boolean showVsanTrafficPage;
  
  public boolean showAdvancedOptionsPage;
  
  public boolean showClaimDisksPage;
  
  public boolean showFaultDomainsPageComponent;
  
  public boolean showSingleSiteFaultDomainsPage;
  
  public boolean showWitnessHostPageComponent;
  
  public boolean showClaimDisksWitnessHostPage;
  
  public VsanClusterType selectedVsanClusterType;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/ConfigureWizardData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */