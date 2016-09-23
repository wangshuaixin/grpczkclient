
package com.github.grpczkclient.zk;

/**
 * @author junzhang
 * @Date Aug 4, 2016
 */
public class ZkConfig {
  private String connectString;
  private int connectTimeout;
  private int sessiontTimeout;
  private int tryTimes;
  private int sleepTime;
  public String getConnectString() {
    return connectString;
  }
  public void setConnectString(String connectString) {
    this.connectString = connectString;
  }
  public int getConnectTimeout() {
    return connectTimeout;
  }
  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }
  public int getSessiontTimeout() {
    return sessiontTimeout;
  }
  public void setSessiontTimeout(int sessiontTimeout) {
    this.sessiontTimeout = sessiontTimeout;
  }
  public int getTryTimes() {
    return tryTimes;
  }
  public void setTryTimes(int tryTimes) {
    this.tryTimes = tryTimes;
  }
  public int getSleepTime() {
    return sleepTime;
  }
  public void setSleepTime(int sleepTime) {
    this.sleepTime = sleepTime;
  }
}
