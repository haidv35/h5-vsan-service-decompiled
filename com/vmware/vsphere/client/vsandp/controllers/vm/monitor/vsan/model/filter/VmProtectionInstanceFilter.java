package com.vmware.vsphere.client.vsandp.controllers.vm.monitor.vsan.model.filter;

import com.vmware.vise.core.model.data;

@data
public class VmProtectionInstanceFilter {
  public double newerThanThreeDays;
  
  public double betweenThreeAndSevenDays;
  
  public double betweenOneAndTwoWeeks;
  
  public double betweenTwoAndFourWeeks;
  
  public double olderThanFourWeeks;
  
  public int newerThanThreeDaysCount;
  
  public int betweenThreeAndSevenDaysCount;
  
  public int betweenOneAndTwoWeeksCount;
  
  public int betweenTwoAndFourWeeksCount;
  
  public int olderThanFourWeeksCount;
}


/* Location:              /Users/haidv/Downloads/h5-vsan-service.jar!/com/vmware/vsphere/client/vsandp/controllers/vm/monitor/vsan/model/filter/VmProtectionInstanceFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */