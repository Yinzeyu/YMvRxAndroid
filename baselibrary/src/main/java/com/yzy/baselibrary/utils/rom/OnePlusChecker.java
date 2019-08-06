package com.yzy.baselibrary.utils.rom;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/07/17
 *    desc   :
 * </pre>
 */
public class OnePlusChecker extends Checker {
  @Override
  protected String getManufacturer() {
    return ManufacturerList.OnePlus;
  }

  @Override
  protected String[] getAppList() {
    return AppList.ONEPLUS_APPS;
  }

  @Override
  public ROM getRom() {
    return ROM.OnePlus;
  }

  @Override
  public ROMInfo checkBuildProp(RomProperties properties) throws Exception {
    return null;
  }
}
