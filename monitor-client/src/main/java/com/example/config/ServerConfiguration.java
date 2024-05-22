package com.example.config;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.ConnectionConfig;
import com.example.utils.MonitorUtils;
import com.example.utils.NetUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
@Configuration
public class ServerConfiguration implements ApplicationRunner {

    @Resource
    NetUtils net;

    @Resource
    MonitorUtils monitor;

    @Bean
    ConnectionConfig connectionConfig() {
        log.info("正在加载服务端连接配置...");
        ConnectionConfig config = this.readConfigurationFromFile();
        if (config == null) {
            config = this.registerToServer();
        }
        System.out.println(monitor.monitorBaseDetail());
        return config;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("正在向服务端更新系统基本信息...");
        net.updateBaseDetails(monitor.monitorBaseDetail());
    }

    private ConnectionConfig registerToServer() {
        Scanner scanner = new Scanner(System.in);
        String token, address;
        do {
            log.info("请输入服务端的网址，地址类似于 'http://192.168.6.111:8080' 这种写法：");
            address = scanner.nextLine();
            log.info("请输入客户端生成的用户注册客户端的Token密钥：");
            token = scanner.nextLine();
        } while (!net.registerToServer(address, token));
        ConnectionConfig config = new ConnectionConfig(address, token);
        this.saveConfigurationToFile(config);
        return config;
    }

    private void saveConfigurationToFile(ConnectionConfig config) {
        File dir = new File("config");
        if (!dir.exists() && dir.mkdir()) {
            log.info("用于保存服务端连接信息的目录以创建完成");
        }
        File file = new File("config/server.json");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(JSONObject.from(config).toJSONString());
        } catch (IOException e) {
            log.error("保存配置文件时出错", e);
        }
        log.info("服务端连接配置已保存到 config/server.json 文件中");
    }

    private ConnectionConfig readConfigurationFromFile() {
        File configurationFile = new File("config/server.json");
        if (configurationFile.exists()) {
            try(FileInputStream stream = new FileInputStream(configurationFile)) {
                String raw = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                return JSONObject.parseObject(raw).to(ConnectionConfig.class);
            } catch (IOException e) {
                log.error("读取配置文件时出错", e);
            }
        }
        return null;
    }
}
