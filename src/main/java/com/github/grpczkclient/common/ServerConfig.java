
package com.github.grpczkclient.common;

import com.github.grpczkclient.zk.ZkConfig;

/**
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class ServerConfig {

  private ZkConfig zkConfig;
  private int port;
  public ZkConfig getZkConfig() {
    return zkConfig;
  }
  public void setZkConfig(ZkConfig zkConfig) {
    this.zkConfig = zkConfig;
  }
 
  public int getPort() {
    return port;
  }
  public void setPort(int port) {
    this.port = port;
  }
}
