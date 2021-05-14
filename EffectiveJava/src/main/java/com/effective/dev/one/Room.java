package com.effective.dev.one;

import sun.misc.Cleaner;

/**
 * @author : kebukeyi
 * @date :  2021-04-14 17:58
 * @description :
 * @usinglink :
 **/
public class Room implements AutoCloseable {

    private static class State implements Runnable {
        int numRooms;

        State(int val) {
            this.numRooms = val;
        }

        @Override
        public void run() {
            System.out.println("Cleaning Room");
            numRooms = 0;
        }
    }

    //private final State state;

    // Our cleanable. Cleans the room when it’s eligible for gc
    // private final Cleaner.Cleanable cleanable;

    // private static final Cleaner cleaner = Cleaner.create();


    @Override
    public void close() throws Exception {
        //  cleanable.clean();
    }
}
 
