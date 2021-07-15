package cn.gof.oo.pinxue;

import java.math.BigDecimal;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 16:42
 * @description :
 * @question :
 * @usinglink :
 **/
public class VirtualWalletController {

    private VirtualWalletService virtualWalletService;


    public BigDecimal getBalance(Long walletId) {
        return new BigDecimal(BigDecimal.ROUND_CEILING);
    } //查询余额

    public void debit(Long walletId, BigDecimal amount) {
    } //出账

    public void credit(Long walletId, BigDecimal amount) {
    } //入账

    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount) {
    } //转账
}
 
