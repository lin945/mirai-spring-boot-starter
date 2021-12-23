package com.lin945.mirai

import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupEvent
import kotlin.reflect.full.callSuspend

object MiraiMessageDispatcher: SimpleListenerHost() {

    lateinit var map:Map<Long,MiraiExClassData>
    @EventHandler
    fun AbstractEvent.onEvent(){

    }

    @EventHandler
    suspend fun GroupEvent.onEvent() {
        map[group.id]?.also {
                miraiClassData->
            //kotlin 反射获取的参数列表
            miraiClassData.kf.parameters.apply {
                println(this)
                val parameterArrays = arrayOfNulls<Any>(size-1)
                forEachIndexed { index, kParameter ->
                    if (kParameter.type.classifier == this@onEvent::class) {
                        parameterArrays[index-1]=this@onEvent
                    }
                }
                miraiClassData.kf.apply {
                    if (this.isSuspend){
                        callSuspend(miraiClassData.instance,*parameterArrays)
//                        call(, Continuation(EmptyCoroutineContext){
//                            it:Result<Any>->
//                        })
                    }else{
                        call(miraiClassData.instance,*parameterArrays)
                    }
                }
            }
        }
    }
}