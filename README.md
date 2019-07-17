### 集群session



描述 

    https://img-blog.csdnimg.cn/20190525201856587.png
    https://img-blog.csdnimg.cn/20190525201856587.png
    注意：session是有过期时间的，当session过期了，redis中的key也就会被自动删除。
    实际场景中一个服务会至少有两台服务器在提供服务，
    在服务器前面会有一个nginx做负载均衡，用户访问nginx，nginx再决定去访问哪一台服务器。
    当一台服务宕机了之后，另一台服务器也可以继续提供服务，保证服务不中断。
    如果我们将session保存在Web容器(比如tomcat)中，如果一个用户第一次访问被分配到服务器1上面需要登录，
    当某些访问突然被分配到服务器二上，因为服务器二上没有用户在服务器一上登录的会话session信息，
    服务器二还会再次让用户登录，用户已经登录了还让登录就感觉不正常了。
    解决这个问题的思路是用户登录的会话信息不能再保存到Web服务器中，
    而是保存到一个单独的库(redis、mongodb、jdbc等)中，
    所有服务器都访问同一个库，都从同一个库来获取用户的session信息，如用户在服务器一上登录，将会话信息保存到库中，
    用户的下次请求被分配到服务器二，服务器二从库中检查session是否已经存在，
    如果存在就不用再登录了，可以直接访问服务了。

1. 引入spring session依赖


    ```md
        dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
            <version>2.1.5.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
    ``` 
2.properties.application

    ```
        spring:
          session:
            store-type: redis
          redis:
            host: localhost
            port: 6379
        
        server:
          port: 8080
          servlet:
            session:
              timeout: 600
    ```
    
 3. 启动两个应用
 
 
       mvn clean install
       java -jar spring-security-example-0.0.1-SNAPSHOT.jar --server.port=8080
       java -jar spring-security-example-0.0.1-SNAPSHOT.jar --server.port=8081
 
 4. 测试
 
 
     使用其中一个服务去登录 http://localhost:8080/login
     使用另一个服务访问任意接口 http://localhost:8081/user/me 不需要再重新登录就可以直接访问

