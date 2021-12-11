package com.hand.log.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 13:22
 * @description:
 * @question:
 * @link:
 **/
public class EvilServiceImpl extends UnicastRemoteObject implements EvilService {

    protected EvilServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Evil> getList() throws RemoteException {
        System.out.println("Get Person Start!");
        final ArrayList<Evil> evils = new ArrayList<>();
        evils.add(new Evil(1, "波吉", 14));
        evils.add(new Evil(2, "戴达", 13));
        evils.add(new Evil(3, "伯斯", 43));
        return evils;
    }
}
 
