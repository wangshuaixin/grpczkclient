
package com.github.grpczkclient.zk;

import com.github.grpczkclient.common.ClientConfig;
import com.github.grpczkclient.common.GrpcData;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * @author junzhang
 * @Date Aug 5, 2016
 */
public final class ZkClient {
  private static final Logger logger = Logger.getLogger(ZkClient.class);

  private ClientConfig config;
  private static CuratorFramework curator;
  private static Map<String, List<String>> serviceMap = new ConcurrentHashMap<String, List<String>>();
  private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

  static {
    executorService.scheduleAtFixedRate(new LocalStorage(serviceMap), 30, 30, TimeUnit.MINUTES);
  }

  public ZkClient(ClientConfig config) throws Exception {
    this.config = config;
    startCurator();
  }

  private void startCurator() {
    ZkConfig zkConfig = config.getZkConfig();
    if (zkConfig.getConnectTimeout() > 0 && zkConfig.getSessiontTimeout() > 0) {
      curator = CuratorFrameworkFactory.newClient(zkConfig.getConnectString(),
          zkConfig.getSessiontTimeout(), zkConfig.getConnectTimeout(),
          new ExponentialBackoffRetry(zkConfig.getSleepTime(), zkConfig.getTryTimes()));
    } else {
      curator = CuratorFrameworkFactory.newClient(zkConfig.getConnectString(),
          new ExponentialBackoffRetry(zkConfig.getSleepTime(), zkConfig.getTryTimes()));
    }
    curator.start();
  }

  public void close() {
    if (curator != null) {
      curator.close();
    }
    LocalStorage.deleteTmpfile();
  }

  public String getProvider(GrpcData service) throws Exception {
    String key = service.getServicePackage() + service.getServiceName();
    if (!serviceMap.containsKey(key)) {
      List<String> providers = ZkUtils.getChildren(curator, ZkUtils.concatPath(ZkPath.ROOT,
          service.getServicePackage(), service.getServiceName(), ZkPath.PROVIDER));
      serviceMap.put(key, providers);
      curator.getChildren().usingWatcher(new CustomerWatcher()).forPath(ZkUtils.concatPath(
          ZkPath.ROOT, service.getServicePackage(), service.getServiceName(), ZkPath.PROVIDER));
    }
    if (!serviceMap.get(key).isEmpty()) {
      return serviceMap.get(key).get(0);
    } else {
      Map<String, List<String>> localMap = LocalStorage.getLocalStorage();
      return localMap.get(key).isEmpty() ? null : localMap.get(key).get(0);
    }
  }

  private class CustomerWatcher implements Watcher {

    public void process(WatchedEvent event) {
      if (event.getType().equals(EventType.NodeChildrenChanged)) {
        try {
          String service = event.getPath().split("/")[2];
          List<String> providers = ZkUtils.getChildren(curator, event.getPath());
          serviceMap.replace(service, providers);
          curator.getChildren().usingWatcher(new CustomerWatcher())
              .forPath(ZkUtils.concatPath(ZkPath.ROOT, service, ZkPath.PROVIDER));
        } catch (Exception e) {
          logger.error("get service provider exception", e);
        }
      }
    }

  }

}
