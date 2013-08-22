package connection;

import java.util.LinkedList;

public class Twitter4jPropertiesHandler {

private LinkedList<Twitter4jProperties> properties;

	public Twitter4jPropertiesHandler(){
		properties = new LinkedList<Twitter4jProperties>();
		for(int i=0; i<5; i++)
			properties.add(new Twitter4jProperties(i));
	}

	public Twitter4jProperties getProperties(){
		for (Twitter4jProperties p: this.properties){
			if (!p.isBusy()){
				p.setBusy();
				this.properties.remove(p);
				this.properties.addLast(p);
				return p;
			}
		}
		return null;
			
	}
}
