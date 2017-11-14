package redes2017;
public class Process {
	private String ip;
	private Integer port;

	public void setIp(String val){
		this.ip = val;
	}
	
	public void setPort(Integer val){
		this.port = val;
	}
	
	public String getIp(){
		return this.ip;
	}
	
	public Integer getPort(){
		return this.port;
	}


}