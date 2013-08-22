package connection;

public class Twitter4jProperties {

	public String CONSUMER_KEY;
	public String CONSUMER_SECRET;
	public String ACCESS_TOKEN;
	public String ACCESS_TOKEN_SECRET;
	boolean busy;
	
	
	public Twitter4jProperties(int n) {
		
		switch (n) {
			case 0:	this.CONSUMER_KEY = "XXXXXXXXXXXXXXXXXXXXX";
					this.CONSUMER_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					break;
					
			case 1:	this.CONSUMER_KEY = "XXXXXXXXXXXXXXXXXXXXX";
					this.CONSUMER_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					break;
					
			case 2:	this.CONSUMER_KEY = "XXXXXXXXXXXXXXXXXXXXX";
					this.CONSUMER_SECRET = "XXXXXXXXXXXXXXXXXXXXX";			 
					this.ACCESS_TOKEN = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					break;
					
			case 3:	this.CONSUMER_KEY = "XXXXXXXXXXXXXXXXXXXXX";
					this.CONSUMER_SECRET = "XXXXXXXXXXXXXXXXXXXXX";			 
					this.ACCESS_TOKEN = "XXXXXXXXXXXXXXXXXXXXX";
					this.ACCESS_TOKEN_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
					break;
					
			default:	this.CONSUMER_KEY = "XXXXXXXXXXXXXXXXXXXXX";
						this.CONSUMER_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
						this.ACCESS_TOKEN = "XXXXXXXXXXXXXXXXXXXXX";
						this.ACCESS_TOKEN_SECRET = "XXXXXXXXXXXXXXXXXXXXX";
						break;
		}
		
		this.busy = false;
	}
	
	public boolean isBusy() {
		return this.busy;
	}
	
	public void setBusy(){
		this.busy = true;
	}
	
	public void setFree(){
		this.busy = false;
	}

}
