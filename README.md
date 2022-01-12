# 有什么作用?
  可以非常友好的获取binlog日志
  
# 安装
  下载或编译
  可选 1 - 从这里下载预构建包：https://github.com/sihaihou/binlog/releases
  
# 快速开始
  开始您的第一个项目非常容易。

### 第一步: 引入依赖
      
<pre>
&#60;dependency&#62;
   &#60;groupId&#62;com.housihai&#60;/groupId&#62;
   &#60;artifactId&#62;binlog-spring-boot-starter&#60;/artifactId&#62;
   &#60;version&#62;0.0.1-RELEASE&#60;/version&#62;
&#60;/dependency&#62;
</pre>

### 第二步: 在application.properties配置文件中配置cannl地址和端口
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
# 文档
所有最新和长期的通知也可以从Github 通知问题这里找到。

# 贡献
欢迎贡献者加入 binlog 项目。请联系18307200213@163.com 以了解如何为此项目做出贡献。



