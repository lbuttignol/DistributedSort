package redes2017;
import java.util.LinkedList;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.lang.InterruptedException;
import java.lang.SecurityException;
import java.util.Random;

/**
 *  Main class on this application this run the distributed bubblesort
 */ 
class App {
    
    // private static final Integer nodes = 5; 
    private static  Integer nodes; 

    // private static final Integer arrayLength = 3000;
    private static Integer arrayLength;

    public static void main(String[] args) {
        System.out.println(Integer.parseInt(args[0]));
        System.out.println(args[1]);
        System.out.println(args[2]);
        nodes = Integer.parseInt(args[0]);
        arrayLength = Integer.parseInt(args[1]);

        App.test(Integer.parseInt(args[2]));

    }   

    public static void test(Integer procId){
        DistSystem ds = new DistSystem(nodes); 
        Middlewar m0 = new Middlewar(procId,ds);  
        DistributedArray a = new DistributedArray(arrayLength,m0); 

        m0.barrier();

        App.addRandomValues(a,m0);

        App.showArray(a,m0);

        a.distributedSort();

        App.showArray(a,m0);

        m0.finish();
    }

    public static void addRandomValues(DistributedArray a, Middlewar m0){
        m0.barrier();
        if (m0.whoAmI() == 0) {
            Random rand = new Random();
            for (int i = 0; i < arrayLength ; i++) {
                a.set(i,rand.nextInt());
            }
        }
        m0.barrier();
    }
    
    public static void showArray(DistributedArray a,Middlewar m0){
        m0.barrier();
        if (m0.whoAmI() == 0) {
            for (int i = 0; i < arrayLength; i++) {
                System.out.println("index "+ i + " = " +a.get(i));
            }
        }
        m0.barrier();
    }


}