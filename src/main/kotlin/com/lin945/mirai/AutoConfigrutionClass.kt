package com.lin945.mirai

import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoggerAdapters.asMiraiLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "mirai",name = ["enable"],havingValue = "true" )
@EnableConfigurationProperties(MiraiConfig::class)
open class AutoConfigrutionClass {
    private val log : Logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    @ConditionalOnMissingBean
    open fun bot(config:MiraiConfig,dispatcher: MiraiMessageDispatcher):Bot{
             return BotFactory.newBot(config.qq,config.password){
                fileBasedDeviceInfo(config.deviceInfo)
                protocol= BotConfiguration.MiraiProtocol.ANDROID_PHONE
                botLoggerSupplier={
                    log.asMiraiLogger()
                }
            }.apply { eventChannel.registerListenerHost(dispatcher) }

    }


    @Bean
    @ConditionalOnMissingBean(MiraiMessageDispatcher::class)
    open fun dispatcher(): MiraiMessageDispatcher {
        return MiraiMessageDispatcher
    }
}
@ConfigurationProperties(prefix = "mirai")
@ConditionalOnMissingBean
data class MiraiConfig @JvmOverloads
constructor(var qq:Long=0L,var password:String="",var deviceInfo:String="deviceInfo.json")
