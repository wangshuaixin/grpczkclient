// Copyright 2016 Mobvoi Inc. All Rights Reserved.

package com.github.grpczkclient.common;

import com.github.grpczkclient.zk.ZkConfig;

/**
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class ClientConfig {
  private ZkConfig zkConfig;

  public ZkConfig getZkConfig() {
    return zkConfig;
  }

  public void setZkConfig(ZkConfig zkConfig) {
    this.zkConfig = zkConfig;
  }
}
