# mirai-spring-boot-stater
一个简单的stater for mirai 机器人


# Usage
## 1.添加依赖
```xml
        <dependency>
            <groupId>com.lin945</groupId>
            <artifactId>mirai-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
## 2.填写配置
```properties
mirai.enable=true
mirai.qq=100000
mirai.password=password
```

## 3.注入你的bot
```java
    @Autowired
    Bot bot;
```
kt
```kotlin
    @Autowired
    lateinit var bot: Bot
```

## 4.登录

```java
 bot.login();
```

## 5.添加注解使用

```kotlin
@MiraiRouter
class aa {
    @Autowired
    lateinit var bot: Bot

   //群号
    @Group(100000L)
    fun handler(groupMessageEvent: GroupMessageEvent) {
        println("message"+groupMessageEvent.message)
    }
}
```