package com.yzy.pj.im

/**
 *description: IM的事件Action.
 *@date 2019/3/11 14:53.
 *@author: yzy.
 */
interface IMConstant {

    /**
     * 登录相关的Action
     */
    interface LoginAction {
        companion object {
            /**
             * 登录Action：登录成功
             */
            const val ACTION_IM_LOGIN_SUCCESS = "action.im.login.success"

            /**
             * 登录Action：登录异常
             */
            const val ACTION_IM_LOGIN_ERROR = "action.im.login.error"
            /**
             * 登录Action：融云token无效（过期等情况）
             */
            const val ACTION_IM_LOGIN_TOKENINCORRECT = "action.im.login.tokenIncorrect"
        }
    }

    interface ConnectionAction {
        companion object {
            /**
             * Connection action：Network is unavailable
             */
            const val ACTION_IM_CONNECTION_NETWORK_UNAVAILABLE =
                    "action.im.connection.network_unavailable"
            /**
             * Connection action：Connect Success.
             */
            const val ACTION_IM_CONNECTION_CONNECTED = "action.im.connection.connected"
            /**
             * Connection action：Connecting
             */
            const val ACTION_IM_CONNECTION_CONNECTING = "action.im.connection.connecting"
            /**
             * Connection action：Disconnected
             */
            const val ACTION_IM_CONNECTION_DISCONNECTED = "action.im.connection.disconnected"
            /**
             * Connection action：Login on the other device, and be kicked offline.
             */
            const val ACTION_IM_CONNECTION_KICKED_OFFLINE_BY_OTHER_CLIENT =
                    "action.im.connection.kicked_offline_by_other_client"
            /**
             * Connection action：Token incorrect.
             */
            const val ACTION_IM_CONNECTION_TOKEN_INCORRECT = "action.im.connection.token_incorrect"
            /**
             * Connection action：Server invalid.
             */
            const val ACTION_IM_CONNECTION_SERVER_INVALID = "action.im.connection.server_invalid"
            /**
             * Connection action：User blocked by admin
             */
            const val ACTION_IM_CONNECTION_CONN_USER_BLOCKED = "action.im.connection.conn_user_blocked"
        }
    }
}