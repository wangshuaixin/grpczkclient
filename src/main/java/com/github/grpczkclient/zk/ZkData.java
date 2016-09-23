
package com.github.grpczkclient.zk;

import com.github.grpczkclient.common.GrpcData;

/**
 * @author junzhang
 * @Date Aug 19, 2016
 */
public class ZkData extends GrpcData {
  private String ip;
  private int port;

  public ZkData(String servicePackage, String serviceName, String ip, int port) {
    super(servicePackage, serviceName);
    this.ip = ip;
    this.port = port;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
