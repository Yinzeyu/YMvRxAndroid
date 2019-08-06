package com.yzy.baselibrary.utils.rom;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/07/03
 *    desc   :
 * </pre>
 */
public class ROMInfo {
  private ROM rom;
  private int baseVersion;
  private String version;

  public ROMInfo(ROM rom) {
    this.rom = rom;
  }

  public ROMInfo(ROM rom, int baseVersion, String version) {
    this.rom = rom;
    this.baseVersion = baseVersion;
    this.version = version;
  }

  public ROM getRom() {

    return rom;
  }

  public void setRom(ROM rom) {
    this.rom = rom;
  }

  public int getBaseVersion() {
    return baseVersion;
  }

  public void setBaseVersion(int baseVersion) {
    this.baseVersion = baseVersion;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}
