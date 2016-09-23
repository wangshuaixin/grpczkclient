
package com.github.grpczkclient.common;

import io.grpc.BindableService;

/**
 * @author junzhang
 * @Date Aug 19, 2016
 */
public class GrpcServerData extends GrpcData {
  private BindableService service;

  public GrpcServerData(String servicePackage, String serviceName, BindableService service) {
    super(servicePackage, serviceName);
    this.service = service;
  }

  public BindableService getService() {
    return service;
  }

  public void setService(BindableService service) {
    this.service = service;
  }
}
