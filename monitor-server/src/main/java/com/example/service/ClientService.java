package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;

public interface ClientService extends IService<Client> {

    // 注册方法
    String registerToken();
    Client findClientById(int id);
    Client findClientByToken(String token);
    // 验证并注册
    boolean verifyAndRegister(String token);
    void updateClientDetail(ClientDetailVO vo, Client client);
}
