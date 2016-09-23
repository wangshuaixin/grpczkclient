
package com.github.grpczkclient;

import com.github.grpczkclient.common.GrpcServerData;
import com.github.grpczkclient.common.ServerConfig;
import com.github.grpczkclient.zk.LoadDefaultConfig;
import com.github.grpczkclient.zk.ZkConfig;
import com.github.grpczkclient.zk.ZkData;
import com.github.grpczkclient.zk.ZkServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * grpc server.
 * 
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class GrpcServer {
  private ZkServer zkServer;
  private Server server;
  private ServerConfig config;
  private List<GrpcServerData> serverDatas;

  /**
   * constructor.
   * 
   * @param config
   *          serverconfig
   * @throws SocketException
   *           : zk connetc exception
   * @throws Exception
   *           : register to zk exception
   */
  public GrpcServer(ServerConfig config, List<GrpcServerData> serverDatas)
      throws SocketException, Exception {
    this.config = config;
    this.serverDatas = serverDatas;

    zkServer = new ZkServer(config.getZkConfig());
    registerService();
  }

  public GrpcServer(int port, List<GrpcServerData> serverDatas) throws Exception {
    ZkConfig zkConfig = LoadDefaultConfig.loadConfig();
    ServerConfig config = new ServerConfig();
    config.setZkConfig(zkConfig);
    config.setPort(port);
    this.config = config;
    this.serverDatas = serverDatas;

    zkServer = new ZkServer(config.getZkConfig());
    registerService();
  }

  private void registerService() throws SocketException, Exception {
    for (GrpcServerData temp : serverDatas) {
      ZkData service = new ZkData(temp.getServicePackage(), temp.getServiceName(), getRealIp(),
          config.getPort());
      zkServer.registerService(service);
    }
  }

  /**
   * start grpc server.
   * 
   * @throws IOException
   *           : grpc server port exception
   */
  public void start() throws IOException {

    ServerBuilder<?> builder = ServerBuilder.forPort(config.getPort());
    for (GrpcServerData service : serverDatas) {
      builder.addService(service.getService().bindService());
    }
    server = builder.build().start();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        GrpcServer.this.stop();
      }
    });
  }

  /**
   * stop the grpc server and close zk connection.
   */
  public void stop() {
    if (server != null) {
      server.shutdown();
    }
    if (zkServer != null) {
      zkServer.close();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * get server ip.
   * 
   * @return server ip
   * @throws SocketException
   *           exception
   * @throws UnknownHostException
   *           exception
   */
  public static String getRealIp() throws SocketException, UnknownHostException {
    String localip = null;
    String netip = null;

    Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
    InetAddress ip = null;
    boolean finded = false;
    while (netInterfaces.hasMoreElements() && !finded) {
      NetworkInterface ni = netInterfaces.nextElement();
      Enumeration<InetAddress> address = ni.getInetAddresses();
      while (address.hasMoreElements()) {
        ip = address.nextElement();
        if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
            && ip.getHostAddress().indexOf(":") == -1) {
          netip = ip.getHostAddress();
          finded = true;
          break;
        } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
            && ip.getHostAddress().indexOf(":") == -1) {
          localip = ip.getHostAddress();
        }
      }
    }

    if (netip != null && !"".equals(netip)) {
      return netip;
    } else {
      return localip;
    }
  }
}
