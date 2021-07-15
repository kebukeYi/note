package cn.gof.oo.pinxue;

import java.math.BigDecimal;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 16:45
 * @description :
 * @question :
 * @usinglink :
 **/
public class VirtualWalletRepository {
    public VirtualWalletEntity getWalletEntity(Long walletId) {
        return new VirtualWalletEntity();
    }

    public BigDecimal getBalance(Long walletId) {
        return new BigDecimal(BigDecimal.ROUND_DOWN);
    }

    public void updateBalance(Long walletId, BigDecimal subtract) {
    }
}
 
