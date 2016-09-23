
package com.github.grpczkclient.zk;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class ZkUtils {

  public static Stat checkPath(CuratorFramework curator, String path) throws Exception{
    return curator.checkExists().forPath(path);
  }
  
  public static byte[] getData(CuratorFramework curator, String path) throws Exception{
    return curator.getData().forPath(path);
  }
  
  public static List<String> getChildren(CuratorFramework curator, String path) throws Exception {
    return curator.getChildren().forPath(path);
  }

  public static void createEphemeral(CuratorFramework curator, String path) throws Exception {
    createEphemeral(curator, null, path);
  }

  public static void createEphemeral(CuratorFramework curator, byte[] payload, String path)
      throws Exception {
    curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
        payload);
  }

  public static void createPersistent(CuratorFramework curator, String path) throws Exception {
    createPersistent(curator, null, path);
  }

  public static void createPersistent(CuratorFramework curator, byte[] payload, String path)
      throws Exception {
    curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
        payload);
  }

  public static void delete(CuratorFramework curator, String path) throws Exception {
    curator.delete().deletingChildrenIfNeeded().forPath(path);
  }

  public static String concatPath(String... path) {
    String finalPath = "";
    for (String item : path) {
      if (item.startsWith("/")) {
        finalPath = finalPath.concat(item);
      } else {
        finalPath = finalPath.concat("/").concat(item);
      }
    }
    return finalPath;
  }
}
