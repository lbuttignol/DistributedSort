package redes2017;

import java.net.InetAddress;

public class Process {
	private Integer id;
	private InetAddress ip;
	private Integer port;

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

}