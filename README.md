<h1>那有什么作用？</h1>
<p>获取binlog日志</p>
<h1>如何使用？</h1>
<p>第一步:引入依赖</p>
<code>
   <dependency>
	<groupId>com.housihai</groupId>
	<artifactId>binlog-spring-boot-starter</artifactId>
	<version>0.0.1-RELEASE</version>
   </dependency>
</code>
<p>第二步:配置cannl地址</p>
<code>    
   binlog.serverConfig.host=47.114.74.174
   binlog.serverConfig.port=11111
</code>

<p>第三步:开启binlog</p>
<code>
   在启动类加上@EnableBinlog注解开启binlog
</code>

<p>第四步:监听binlog日志</p>
<code>
   实现OperationLogService接口,实现save()方法，并交给spring管理。
</code>
