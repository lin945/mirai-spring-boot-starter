package com.lin945.mirai

import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.SimpleListenerHost

object MiraiMessageDispatcher: SimpleListenerHost() {

    @EventHandler
    fun AbstractEvent.onEvent(){

    }
}