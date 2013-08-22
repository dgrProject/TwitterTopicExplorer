package core;


import java.util.LinkedList;
import java.util.TreeMap;

import connection.Twitter4jProperties;
import domain.Tweet;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public class StreamCatcher {
	
    private TwitterStream twitterStream;
    private boolean finished;
    private LinkedList<Tweet> tweets;
    private String[] query;
    private HashtagMapper mapper;
    private Twitter4jProperties properties;
	
	public StreamCatcher(String[] query, Twitter4jProperties properties) {
		this.finished = false;
		this.tweets = new LinkedList<Tweet>();
		this.query = query;
		this.mapper = new HashtagMapper();
		this.properties = properties;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey(this.properties.CONSUMER_KEY)
          .setOAuthConsumerSecret(this.properties.CONSUMER_SECRET)
          .setOAuthAccessToken(this.properties.ACCESS_TOKEN)
          .setOAuthAccessTokenSecret(this.properties.ACCESS_TOKEN_SECRET);

        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	}
	
	public boolean getFinished() {
		return this.finished;
	}
	
	public void setFinished(boolean value) {
		this.finished = value;
	}
	
	public void getTweetsStream() throws InterruptedException {
		
			StatusListener listener = new StatusListener(){
			
			@Override
            public void onStatus(Status status) {
				if (true) { //possible lang filters
					Tweet t = new Tweet(status);
					tweets.add(t);					
				}	
            }

			// Called upon deletionNotice notices
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            // This notice will be sent each time a limited stream becomes unlimited.
            // If this number is high and or rapidly increasing, it is an indication that your
            // predicate is too broad, and you should consider a predicate with higher selectivity
            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            	System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            // Called upon location deletion messages
            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            // Called when receiving stall warnings
            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        
        
        this.twitterStream.addListener(listener);
        
        FilterQuery filterQuery = new FilterQuery().track(query);
        twitterStream.filter(filterQuery);
       

	}
	
	public int getTotalTweets(){
		return this.tweets.size();
	}
	
	public void updateMap(){
		this.mapper.updateMap(this.tweets);
	}

	public TreeMap<String, Integer> getMap() {
		return this.mapper.getMap();
	}

	public LinkedList<Tweet> getTweets() {		
		LinkedList<Tweet> list = new LinkedList<Tweet>();
		list.addAll(this.tweets);
		return list;
	}
	
	public void closeStream(){
		this.twitterStream.cleanUp();
		this.twitterStream.shutdown();
		this.properties.setFree();
		this.setFinished(true);
	}

	public void clear() {
		this.tweets.clear();
		
	} 
	
}
