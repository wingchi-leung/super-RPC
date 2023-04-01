package demo.rpc.commonapi.service;

import demo.rpc.commonapi.dto.AccountVo;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
     void addBalance(Long accountId, BigDecimal amount);

     void deductBalance(Long accountId, BigDecimal amount);

    List<AccountVo> getAccountList();

    AccountVo getAccount(Long accountId);

    void createAccount(AccountVo accountVo);

    void updateAccount(AccountVo accountVo);

    void deleteAccount(Long accountId);

    void deposit(Long accountId, BigDecimal amount);

    void withdraw(Long accountId, BigDecimal amount);
}
