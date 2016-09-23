
package com.github.grpczkclient;

import com.github.grpczkclient.common.ClientConfig;
import com.github.grpczkclient.common.GrpcData;
import com.github.grpczkclient.zk.LoadDefaultConfig;
import com.github.grpczkclient.zk.ZkClient;
import com.github.grpczkclient.zk.ZkConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Grpc client.
 *
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class GrpcClient {
	private ZkClient zkClient;
	// <server, channel> map, eg <127.0.0.1:10040, channel>
	private static Map<String, ManagedChannel> channels =
					new ConcurrentHashMap<String, ManagedChannel>();
	// <service, server> map, eg <greeterservice, 127.0.0.1:10040>
	private static Map<String, String> servers = new ConcurrentHashMap<String, String>();

	public GrpcClient() throws Exception {
		ZkConfig zkConfig = LoadDefaultConfig.loadConfig();
		ClientConfig config = new ClientConfig();
		config.setZkConfig(zkConfig);
		zkClient = new ZkClient(config);
	}

	public GrpcClient(ClientConfig config) throws Exception {
		zkClient = new ZkClient(config);
	}

	/**
	 * get service ManagedChannel.
	 *
	 * @param service : grpc data
	 * @return managedchannel of the service
	 * @throws Exception :channel exception
	 */
	public ManagedChannel getManagedChannel(GrpcData service) throws Exception {
		String key = service.getServicePackage() + service.getServiceName();
		if (servers.containsKey(key)) {
			return channels.get(servers.get(key));
		} else {
			String provider = zkClient.getProvider(service);
			if (!channels.containsKey(provider)) {
				ManagedChannel channel = ManagedChannelBuilder.forTarget(provider).usePlaintext(true)
								.build();
				channels.put(provider, channel);
			}

			return channels.get(provider);
		}
	}

	/**
	 * shutdown the client.
	 *
	 * @throws InterruptedException : channel exception
	 */
	public void shutdown() throws InterruptedException {
		for (ManagedChannel channel : channels.values()) {
			channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		}
	}
}
