package redes2017;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Process {
    private Integer id;
    private InetAddress ip;
    private Integer port;

    /**
     *  Contructor of Process with default IP
     *  @param id the number of the Process
     *  @param ip the ip address of the process
     *  @param port the port to make communication 
     */
    public Process(Integer id, String ip, Integer port){
        this.id = id;
        // this.ip = new InetAddress(ip );
        this.port = port;
    }

    /**
     *  Contructor of Process with default IP
     *  @param id the number of the Process
     *  @param port the port to make communication 
     */
    public Process(Integer id, Integer port){
        this.id = id;
        this.port = port;
        try{
            this.ip = InetAddress.getByName("localhost");
        }catch (UnknownHostException e) {
            System.out.println("Panic !!! On creation of Process");
            System.out.println("ID: " + id);
            System.out.println("Port: " + port);
        }
    }

    /**
     *  @param val new id to this process
     */
    public void setId(Integer val){
        this.id = val;
    }
    
    /**
     *  @param val new Ip to this process
     */
    public void setIp(InetAddress val){
        this.ip = val;
    }
    
    /**
     *  @param val new port to this process
     */
    public void setPort(Integer val){
        this.port = val;
    }
    
    /**
     *  @return the IP of this process
     */
    public InetAddress getIp(){
        return this.ip;
    }
    
    /**
     *  @return the id of this process
     */
    public Integer getId(){
        return this.id;
    }
    
    /**
     *  @return the port of this process
     */
    public Integer getPort(){
        return this.port;
    }

    @Override
    public String toString(){
        return "Process " + id + ".";
    }

}