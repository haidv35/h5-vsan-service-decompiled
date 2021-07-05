package com.vmware.vsan.client.services.hci.model;

import com.vmware.vise.core.model.data;

@data
public class QuickstartViewData {
  public String header;
  
  public String text;
  
  public boolean showSendFeedbackLink;
  
  public boolean showCloseQuickstartButton;
  
  public ConfigCardData[] configurationCards;
  
  public boolean extendCard;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsan/client/services/hci/model/QuickstartViewData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */