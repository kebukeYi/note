package cn.gof.oo.pinxue;

import java.math.BigDecimal;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 16:47
 * @description :
 * @question :
 * @usinglink :
 **/
public class VirtualWalletTransactionEntity {

    BigDecimal amount;
    Long createTime;
    TransactionType type;
    Long fromWalletId;
    Long toWalletId;

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public TransactionType getType() {
        return type;
    }

    public Long getFromWalletId() {
        return fromWalletId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setFromWalletId(Long fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public Long getToWalletId() {
        return toWalletId;
    }

    public void setToWalletId(Long toWalletId) {
        this.toWalletId = toWalletId;
    }
}
 
