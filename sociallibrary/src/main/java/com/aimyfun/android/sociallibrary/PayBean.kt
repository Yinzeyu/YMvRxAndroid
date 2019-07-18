package com.aimyfun.android.sociallibrary

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:00
 */
class PayBean {
  var params: String? = null

  var out_trade_no: String? = null

  var appid: String? = null
  var noncestr: String? = null
  var packageX: String? = null
  var partnerid: String? = null
  var prepayid: String? = null
  var timestamp: String? = null
  var orderNumber: String? = null
  var sign: String? = null
  /**
   * 提交订单type
   */
  var submitPayType: Int = 0
}
