package com.java.note.distributed.transaction;

import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author : kebukeyi
 * @date :  2021-06-27 15:36
 * @description :  2PC之XA两阶段提交
 * @question :
 * @usinglink : https://www.yuque.com/docs/share/3ab7cfc4-576d-4a2e-97c5-9259980201e0
 **/
public class XATransaction {

    public static void main(String[] args) {
        try {
            //========================== START ==========================================//

            //是否开启日志
            boolean isLogging = true;

            //获取rm
            Connection conn1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bank1?serverTimezone=UTC", "root", "123456");
            XAResource accountResource = new MysqlXAConnection((com.mysql.cj.jdbc.JdbcConnection) conn1, isLogging).getXAResource();

            Connection conn2 = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bank2?serverTimezone=UTC", "root", "123456");
            XAResource redBagResource = new MysqlXAConnection((com.mysql.cj.jdbc.JdbcConnection) conn2, isLogging).getXAResource();

            //开始XA事物
            //全局事物
            byte[] globalId = UUID.randomUUID().toString().getBytes();
            //标识
            int formatId = 1;

            //分支事物一
            Xid xid1 = new MysqlXid(globalId, UUID.randomUUID().toString().getBytes(), formatId);
            //分支事物二
            Xid xid2 = new MysqlXid(globalId, UUID.randomUUID().toString().getBytes(), formatId);
            try {
                //分支事物1开始，此时状态：ACTIVE
                accountResource.start(xid1, XAResource.TMNOFLAGS);
                //模拟业务
                conn1.prepareStatement("update account_info set account_balance=account_balance-10 where account_no=1").execute();
                accountResource.end(xid1, XAResource.TMSUCCESS);
                //分支事物1 此时状态 IDLE

                //分支事物2开始
                redBagResource.start(xid2, XAResource.TMNOFLAGS);
                //模拟业务
                conn2.prepareStatement("update account_info set account_balance=account_balance+10 where account_no=2").execute();
                //存在异常
                int i = 10 / 0;
                redBagResource.end(xid2, XAResource.TMSUCCESS);
                //分支事物2 此时状态 IDLE

                //第一阶段：准备提交
                int rm1_pre = accountResource.prepare(xid1);
                int rm2_pre = redBagResource.prepare(xid2);

                //XA事物 此时状态 PREPARED
                //第二阶段： TM根据根据第一阶段的情况决定提交还是回滚
                //TM判断有两个事物分支，所以不能优化为一阶段提交
                boolean flag = false;
                if (rm1_pre == XAResource.XA_OK && rm2_pre == XAResource.XA_OK) {
                    accountResource.commit(xid1, flag);
                    redBagResource.commit(xid2, flag);
                } else {
                    accountResource.rollback(xid1);
                    redBagResource.rollback(xid2);
                }
                //================= END =========================//
            } catch (Exception e) {
                //出现异常，也要回滚
                try {
                    accountResource.rollback(xid1);
                    redBagResource.rollback(xid2);
                    e.printStackTrace();
                } catch (XAException xaException) {
                    xaException.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
 
