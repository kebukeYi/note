package com.hand.log.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 13:03
 * @description:
 * @question:
 * @link:
 **/
public class RMIServer {

    public static void main(String[] args) {
        try {
            //demo1
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            Reference reference = new Reference("com.hand.log.rmi.Evil", "com.hand.log.rmi.Evil", "com.hand.log.rmi.Evil");
            ReferenceWrapper evilService = new ReferenceWrapper(reference);
            registry.bind("/evil", evilService);

            // demo2
            // 绑定到 JVM 的 RMI Server上
            // EvilServiceImpl evilService = new EvilServiceImpl();
            // Naming.bind("t1",evilService);
            // Naming.rebind("t1",evilService);
            //当RMI注册server是指定了端口时或者不在本机运行时，需要这样写
            //Naming.rebind("//localhost/evil", evilService);

            //demo3
            // LocateRegistry.createRegistry(1099);
            // Naming.rebind("rmi://127.0.0.1:1099/evil", evilService);

            System.out.println("Bind is Finish");
        } catch (RemoteException | NamingException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
 
