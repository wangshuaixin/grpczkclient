package com.github.grpczkclient.zk;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * load default zk config
 * Created by junzhang on 16-8-27.
 */
public class LoadDefaultConfig {

	public static ZkConfig loadConfig() throws IOException {
		InputStream is = LoadDefaultConfig.class.getClassLoader().getResourceAsStream("com/mobvoi/be/common/zkconfig.properties");
		Properties prop = new Properties();
		prop.load(is);
		ZkConfig zkConfig = JSON.parseObject(JSON.toJSONString(prop), ZkConfig.class);
		return zkConfig;
	}
}
