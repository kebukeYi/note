package com.java.note.Jdk.lock.transfer;

import com.java.note.Jdk.lock.transfer.entity.UserAccount;
import com.java.note.Jdk.lock.transfer.service.TransferAccountService;
import com.java.note.Jdk.lock.transfer.service.impl.TransferAccountServiceImpl;
import com.java.note.Jdk.lock.transfer.thread.TransferThread;

import java.math.BigDecimal;

/**
 * 描述 : .
 *
 * @author : MoCha
 * @version : v1
 * @date : 2021-08-08 20:44
 */
public class Demo {
    public static void main(String[] args) throws InterruptedException {
        UserAccount zhangsan = new UserAccount("zhangsan", BigDecimal.valueOf(100));
        UserAccount lisi = new UserAccount("lisi", BigDecimal.valueOf(100));
        TransferAccountService transfer = new TransferAccountServiceImpl();
        // 张三转李四
        TransferThread zhangsanToLisi = new TransferThread("zhangsanToLisi", zhangsan, lisi,
                BigDecimal.valueOf(10), transfer);

        // 李四转张三
        TransferThread lisiToZhangsan = new TransferThread("lisiToZhangsan", lisi, zhangsan,
                BigDecimal.valueOf(20), transfer);

        // 线程开始执行
        zhangsanToLisi.start();
        lisiToZhangsan.start();

        //可能 线程A  线程B 还没执行完毕  jvm 就退出了
        zhangsanToLisi.join();
        lisiToZhangsan.join();

        System.out.println(zhangsan);
        System.out.println(lisi);
    }
}
