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
    /**
     *  This method shuld initialize the System with some magical config 
     *  file recived from main 
     */
    private void init(){
        // TO-DO
    }
    
    private static final Integer nodes = 3; 

    private static final Integer arrayLength = 30;

    public static void addSomeValues(DistributedArray a, Middlewar m0){
        m0.barrier();
        if (m0.whoAmI()== 0) {
            a.set(0,5);
            a.set(1,3000);
            a.set(2,60);
            a.set(3,45000);
            a.set(4,6000);
            a.set(5,200);
            a.set(6,14);
            a.set(7,1500);
            a.set(8,1);
            a.set(9,130000);
        }
    }

    public static void swapSomeValues(DistributedArray a,Middlewar m0){
        if (m0.whoAmI()== 0) {

            System.out.println("\nLocal swap!\n");
            System.out.println("index "+ 0 +" = " +a.get(0));
            System.out.println("index "+ 1 +" = " +a.get(1));
            a.swap(0,1);
            System.out.println("index "+ 0 +" = " +a.get(0));
            System.out.println("index "+ 1 +" = " +a.get(1));
            
            System.out.println("\nAll remote swap\n");
            System.out.println("index "+ 8 +" = " +a.get(8));
            System.out.println("index "+ 9 +" = " +a.get(9));
            a.swap(8,9);
            System.out.println("index "+ 8 +" = " +a.get(8));
            System.out.println("index "+ 9 +" = " +a.get(9));

            System.out.println("\nHalf local and half remote swap\n");
            System.out.println("index "+ 2 +" = " + a.get(2));
            System.out.println("index "+ 7 +" = " + a.get(7));
            a.swap(2,7);
            System.out.println("index "+ 2 +" = " + a.get(2));
            System.out.println("index "+ 7 +" = " + a.get(7));

            System.out.println("\nHalf local and half remote null swap\n");
            System.out.println("index "+ 3 +" = " + a.get(3));
            System.out.println("index "+ 10 +" = " + a.get(10));
            a.swap(3,10);
            System.out.println("index "+ 3 +" = " + a.get(3));
            System.out.println("index "+ 10 +" = " + a.get(10));
        }

    }

    public static void toyTest(Integer procId){
        // nodes = 2  and arrayLength = 11 
        DistSystem ds = new DistSystem(2); 
        Middlewar m0 = new Middlewar(procId,ds);  
        DistributedArray a = new DistributedArray(11,m0); 
        
        App.addSomeValues(a,m0);

        if (m0.whoAmI()== 0) {
            // show all values
            System.out.println("unsorted values");
            for (int i=0; i < 11; i++) {
                System.out.println("index "+ i +" = " +a.get(i));
            }
        }

        App.swapSomeValues(a,m0);

        System.out.println("Begin distributed sort");
        a.distributedSort();
        System.out.println("End distributed sort");

        if (m0.whoAmI()== 0) {
         // show all values
            System.out.println("Sorted values ");
            for (int i=0; i<11; i++) {
                 System.out.println("index "+ i +" = " +a.get(i));
            }
        }  
        m0.finish();      
    }


    public static void main(String[] args) {
        // App.toysTest(Integer.parseInt(args[0]));
        App.test2(Integer.parseInt(args[0]));

    }   

    public static void test2(Integer procId){
        DistSystem ds = new DistSystem(nodes); 
        Middlewar m0 = new Middlewar(procId,ds);  
        DistributedArray a = new DistributedArray(arrayLength,m0); 

        m0.barrier();
        System.out.println("pass the barrier");
        App.addRandomValues(a,m0);
        System.out.println("create random values");
        App.showArray(a,m0);
        System.out.println("show values");
        System.out.println("distributed arrat to String");
        a.distributedSort();
        System.out.println("Vaules are Sorted");
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