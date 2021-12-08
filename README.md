# 有什么作用?
  可以获取binlog日志
# 如何使用？</h1>
### 第一步: 引入依赖
      
<pre>
   <dependency>
	<groupId>com.housihai</groupId>
	<artifactId>binlog-spring-boot-starter</artifactId>
	<version>0.0.1-RELEASE</version>
   </dependency>
</pre>
### 第二步: 在properties配置文件中配置cannl地址和端口
  
<pre>    
	binlog.serverConfig.host=127.0.0.1
	binlog.serverConfig.port=11111
</pre>

### 第三步: 在启动类加上@EnableBinlog注解开启binlog
     
<pre>
	@EnableBinlog
	@SpringBootApplication
	public class TestApplication {
		public static void main(String[] args) {
			SpringApplication.run(TestApplication.class, args);
		}
	}
</pre>

### 第四步: 实现OperationLogService接口,实现save()方法，并交给spring管理。
        
<pre>
	@Service
	public class OperationLogServiceImpl implements OperationLogService {

		protected static Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

		@Override
		public void save(List<LogDefinition> logDefinitions) throws Exception {
			for (LogDefinition logDefinition : logDefinitions) {
				logger.debug("发布事件：" + logDefinition);
			}
		}
	}
</pre>
