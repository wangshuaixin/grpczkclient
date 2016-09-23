
package com.github.grpczkclient.common;

/**
 * @author junzhang
 * @Date Aug 17, 2016
 */
public class GrpcData {

  private String servicePackage;
  private String serviceName;

  public GrpcData() {
  }

  public GrpcData(String servicePackage, String serviceName) {
    this.servicePackage = servicePackage;
    this.serviceName = serviceName;
  }

  public String getServicePackage() {
    return servicePackage;
  }

  public void setServicePackage(String servicePackage) {
    this.servicePackage = servicePackage;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

}
