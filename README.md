<h1>那有什么作用？</h1>
<p>获取binlog日志</p>
<h1>如何使用？</h1>
<p>第一步:引入依赖</p>
<pre>
   <dependency>
	<groupId>com.housihai</groupId>
	<artifactId>binlog-spring-boot-starter</artifactId>
	<version>0.0.1-RELEASE</version>
   </dependency>
</pre>
<p>第二步:在配置文件中配置cannl地址和端口</p>
<pre>    
   binlog.serverConfig.host=127.0.0.1
   binlog.serverConfig.port=11111
</pre>

<p>第三步:开启binlog</p>
<pre>
   在启动类加上@EnableBinlog注解开启binlog
   	
	@EnableBinlog
	@SpringBootApplication
	public class TestMybatisApplication {

		public static void main(String[] args) {
			SpringApplication.run(TestMybatisApplication.class, args);
		}

	}
</pre>

<p>第四步:监听binlog日志</p>
<pre>
   实现OperationLogService接口,实现save()方法，并交给spring管理。
   
   	@Service
	public class OperationLogServiceImpl implements OperationLogService{

		protected static Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

		@Override
		public void save(List<LogDefinition> logDefinitions) throws Exception {
			for (LogDefinition logDefinition : logDefinitions) {
				logger.debug("发布事件："+logDefinition);
			}
		}
	}

</pre>
