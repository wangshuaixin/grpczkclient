
package com.github.grpczkclient.zk;

import java.net.SocketException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author junzhang
 * @Date Aug 4, 2016
 */
public final class ZkServer {
  private ZkConfig config;
  private static CuratorFramework curator;

  public ZkServer(ZkConfig config) throws SocketException, Exception {
    this.config = config;
    startCurator();
  }

  private void startCurator() {
    if (config.getConnectTimeout() > 0 && config.getSessiontTimeout() > 0) {
      curator = CuratorFrameworkFactory.newClient(config.getConnectString(),
          config.getSessiontTimeout(), config.getConnectTimeout(),
          new ExponentialBackoffRetry(config.getSleepTime(), config.getTryTimes()));
    } else {
      curator = CuratorFrameworkFactory.newClient(config.getConnectString(),
          new ExponentialBackoffRetry(config.getSleepTime(), config.getTryTimes()));
    }
    curator.start();
  }

  public void registerService(ZkData zkData) throws SocketException, Exception {
    ZkUtils.createEphemeral(curator, ZkUtils.concatPath(ZkPath.ROOT, zkData.getServicePackage(),
        zkData.getServiceName(), ZkPath.PROVIDER, zkData.getIp() + ":" + zkData.getPort()));
  }

  public void close() {
    if (curator != null) {
      curator.close();
    }
  }
}
