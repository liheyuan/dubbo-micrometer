# Dubbo + SpringCloud + MicroMeter Demo
## 启动方法
1. 启动nacos
```shell
nacos/bin/startup.sh -m standalone
```
2. 启动server中的applicaiton
3. 启动client中的aplication
4. 验证
curl "127.0.0.1:10002/hello"