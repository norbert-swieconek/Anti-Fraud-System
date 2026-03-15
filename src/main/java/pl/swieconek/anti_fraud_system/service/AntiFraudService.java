package pl.swieconek.anti_fraud_system.service;

import pl.swieconek.anti_fraud_system.dto.*;

import java.util.List;

public interface AntiFraudService {
    IpResponse addIpBlackList(IpRequest ipRequest);
    CardNumberResponse addCardNumberBlackList(CardNumberRequest cardNumberRequest);
    DeleteStatusResponse removeIpBlackList(String ip);
    DeleteStatusResponse removeCardNumberBlackList(String cardNumber);
    List<IpResponse> getBlackListIps();
    List<CardNumberResponse> getBlackListCardNumbers();
}
