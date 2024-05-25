package com.example.utils;

import com.example.entity.dto.RuntimeData;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InfluxDbUtils {

    @Value("${spring.influx.url}")
    String url;

    private final String BUCKET = "monitor";
    private final String ORG = "default";
    private final String token = "cYhDpdwAb4V_gXK9PhQXxYsczDJl6OFVapek35jwCD6lrTX7zdO6rQO5bfk3K0oWh48ySi42Ut7Oxg6BH9BESg==";

    private InfluxDBClient client;

    @PostConstruct
    public void init() {
        client = InfluxDBClientFactory.create(url, token.toCharArray());
    }

    public void writeRuntimeData(int clientId, RuntimeDetailVO vo) {
        RuntimeData data = new RuntimeData();
        BeanUtils.copyProperties(vo, data);
        data.setTimestamp(new Date(vo.getTimestamp()).toInstant());
        data.setClientId(clientId);
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeMeasurement(BUCKET, ORG, WritePrecision.NS, data);

    }
}
