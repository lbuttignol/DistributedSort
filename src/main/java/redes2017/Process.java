package redes2017;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Process {
	private Integer id;
	private InetAddress ip;
	private Integer port;

	public Process(Integer id, String ip, Integer port){
		this.id = id;
		// this.ip = new InetAddress(ip );
		this.port = port;
	}

	public Process(Integer id, Integer port){
		this.id = id;
		this.port = port;
		try{
			this.ip = InetAddress.getLocalHost();
		}catch (UnknownHostException e) {
			System.out.println("Panic !!! On creation of Process");
			System.out.println("ID: " + id);
			System.out.println("Port: " + port);
		}
	}
	public void setId(Integer val){
		this.id = val;
	}
	
	public void setIp(InetAddress val){
		this.ip = val;
	}
	
	public void setPort(Integer val){
		this.port = val;
	}
	
	public InetAddress getIp(){
		return this.ip;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public Integer getPort(){
		return this.port;
	}

	@Override
	public String toString(){
		return "Process " + id + ".";
	}

}