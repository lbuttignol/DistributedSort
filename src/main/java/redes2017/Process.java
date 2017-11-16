package redes2017;
public class Process {
	private Integer id;
	private String ip;
	private Integer port;

	public void setId(Integer val){
		this.id = val;
	}
	
	public void setIp(String val){
		this.ip = val;
	}
	
	public void setPort(Integer val){
		this.port = val;
	}
	
	public String getIp(){
		return this.ip;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public Integer getPort(){
		return this.port;
	}

}