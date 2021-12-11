package com.hand.log.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 13:21
 * @description:
 * @question:
 * @link:
 **/
public interface EvilService extends Remote {
    List<Evil> getList() throws RemoteException;
}
 
