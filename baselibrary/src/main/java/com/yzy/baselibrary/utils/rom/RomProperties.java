package com.yzy.baselibrary.utils.rom;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/07/10
 *    desc   :
 * </pre>
 */
class RomProperties {

  Properties properties;

  public RomProperties() {
  }

  private static String getSystemProperty(String key) throws Exception {
    Class<?> clz = Class.forName("android.os.SystemProperties");
    Method get = clz.getMethod("get", String.class, String.class);
    return (String) get.invoke(clz, key, null);
  }

  public void setBuildProp(Properties properties) {
    this.properties = properties;
  }

  public String getProperty(String key) throws Exception {
    if (properties != null) {
      return properties.getProperty(key);
    } else {
      return getSystemProperty(key);
    }
  }
}
