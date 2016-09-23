
package com.github.grpczkclient.zk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * Created by junzhang on 16-9-22.
 */
public class LocalStorage implements Runnable {
  private static final Logger logger = Logger.getLogger(ZkClient.class);
  private Map<String, List<String>> serviceMap = new ConcurrentHashMap<String, List<String>>();
  private static String TEMPFILE_PREFFIX = "grpc";
  private static String TEMPFILE_SUFFIX = "tmp";
  private static String TEMPFILE_PATH;

  static {
    try {
      File file = File.createTempFile(TEMPFILE_PREFFIX, TEMPFILE_SUFFIX);
      TEMPFILE_PATH = file.getAbsolutePath();
    } catch (IOException e) {
      logger.error("init temp file exception: ", e);
    }
  }

  /**
   * constructor.
   * 
   * @param serviceMap:
   *          save in file
   */
  public LocalStorage(Map<String, List<String>> serviceMap) {
    this.serviceMap = serviceMap;
  }

  @Override
  public void run() {
    try (FileWriter fw = new FileWriter(TEMPFILE_PATH);
        BufferedWriter bw = new BufferedWriter(fw)) {
      bw.write(JSON.toJSONString(serviceMap));
      bw.flush();
    } catch (IOException e) {
      logger.error("save local storage exception: ", e);
    }
  }

  /**
   * get service map from local storage.
   * 
   * @return service map
   */
  public static Map<String, List<String>> getLocalStorage() {
    Map<String, List<String>> map = new HashMap<>();
    try (FileReader fr = new FileReader(TEMPFILE_PATH);
        BufferedReader br = new BufferedReader(fr)) {
      String content = br.readLine();
      map = JSON.parseObject(content, new TypeReference<Map<String, List<String>>>() {
      });
    } catch (IOException e) {
      logger.error("load local storage exception: ", e);
    }
    return map;
  }
  
  public static void deleteTmpfile(){
    File file = new File(TEMPFILE_PATH);
    if(file.exists()){
      file.exists();
    }
  }
}
