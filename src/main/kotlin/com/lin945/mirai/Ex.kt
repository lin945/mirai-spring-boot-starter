package com.lin945.mirai

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

/**
 * 路由注解加class上
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class MiraiRouter(){

}
/**
 * 群消息注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class onMessage(val message: String,val regex:String="")


/**
 * 群事件消息注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Group(val group:Long=0L)
//存放fun 和实例对象
data class MiraiExClassData(val instance: Any,val kf: KFunction<*>)

/**
 * 读取的配置信息
 */
@ConfigurationProperties(prefix = "mirai")
@ConditionalOnMissingBean
data class MiraiConfig @JvmOverloads
constructor(var qq: Long = 0L, var password: String = "", var deviceInfo: String = "deviceInfo.json")


//spring容器上下文初始化或者刷新发送事件
class MiraiBeanListener : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val ExData = HashMap<Long, MiraiExClassData>()
        val beans = event.applicationContext.getBeansWithAnnotation(MiraiRouter::class.java)
        //遍历beans
        for ((_, owner) in beans) {
            //转换kclass
            val kclass = owner::class
            //获取kfunctions
            val kfuns = kclass.functions
            for (kFunction in kfuns) {
                //找是否有Group注解的
                val annotations = kFunction.annotations
                annotations.find { it is Group }
                    ?.run { this as Group }
                    ?.let {
                        ExData[it.group] = MiraiExClassData(owner, kFunction)
                    }

            }
        }
        MiraiMessageDispatcher.map = ExData
    }
}