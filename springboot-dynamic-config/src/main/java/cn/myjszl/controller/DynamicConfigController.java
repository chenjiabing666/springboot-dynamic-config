package cn.myjszl.controller;

import com.sun.scenario.effect.impl.ImagePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RefreshScope
public class DynamicConfigController {

    @Autowired
    private ContextRefresher contextRefresher;

    @Autowired
    private ConfigurableEnvironment environment;

    @Value("${config.version}")
    private String version;

    @Value("${config.app.name}")
    private String appName;

    @Value("${config.platform}")
    private String platform;


    @GetMapping("/show/version")
    public String test(){
        return "version="+version+"-appName="+appName+"-platform="+platform;
    }

    @GetMapping("/show/refresh")
    public String refresh(){
        //修改配置文件中属性
        HashMap<String, Object> map = new HashMap<>();
        map.put("config.version",99);
        map.put("config.app.name","appName");
        map.put("config.platform","ORACLE");
        MapPropertySource propertySource=new MapPropertySource("dynamic",map);
        //将修改后的配置设置到environment中
        environment.getPropertySources().addFirst(propertySource);
        //异步调用refresh方法，避免阻塞一直等待无响应
        new Thread(() -> contextRefresher.refresh()).start();
        return "success";
    }



}
