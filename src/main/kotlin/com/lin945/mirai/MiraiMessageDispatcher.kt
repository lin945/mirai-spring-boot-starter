package com.lin945.mirai

import kotlinx.coroutines.SupervisorJob
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.GroupEvent
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.isAccessible

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
            val kf = miraiClassData.kf
            val owner=miraiClassData.instance
            //设置可访问
            kotlin.runCatching {
                kf.isAccessible = true
            }
            kf.parameters.apply {
                println(this)
                val parameterArrays = arrayOfNulls<Any>(size-1)
                forEachIndexed { index, kParameter ->
                    if (kParameter.type.classifier == this@onEvent::class) {
                        parameterArrays[index-1]=this@onEvent
                    }
                }
                kf.CallEvent(owner,parameterArrays)
            }
        }
    }

    suspend inline fun KFunction<*>.CallEvent(owner:Any, vararg arg: Any) {
         if (this.isSuspend) {
             callSuspend(owner,arg)
         }else{
             call(owner,arg)
         }
    }


}