package com.lin945.mirai

import org.springframework.stereotype.Component
import kotlin.reflect.KFunction

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
annotation class MiraiRouter(){

}
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Group(val group:Long=0L)

data class MiraiExClassData(val instance: Any,val kf: KFunction<*>)