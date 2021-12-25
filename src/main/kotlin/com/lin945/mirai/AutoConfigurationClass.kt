package com.lin945.mirai

import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import kotlin.reflect.full.functions

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "mirai", name = ["enable"], havingValue = "true")
@EnableConfigurationProperties(MiraiConfig::class)
open class AutoConfigurationClass {


    /**
     * bot对象
     */
    @Bean
    @ConditionalOnMissingBean
    open fun bot(config: MiraiConfig, dispatcher: MiraiMessageDispatcher): Bot {
        return BotFactory.newBot(config.qq, config.password) {
            fileBasedDeviceInfo(config.deviceInfo)
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PHONE
//                botLoggerSupplier={
//                    log.asMiraiLogger()
//                }
        }.apply { eventChannel.registerListenerHost(dispatcher) }

    }

    /**
     *
     */
    @Bean
    @ConditionalOnMissingBean(MiraiMessageDispatcher::class)
    open fun dispatcher(): MiraiMessageDispatcher {
        return MiraiMessageDispatcher
    }

    /**
     * 在spring容器初始化完成后扫描bean
     */
    @Bean
    @ConditionalOnMissingBean
    open fun miraiBeanScanner(): MiraiBeanListener {
        return MiraiBeanListener()
    }


}


