package domain;

import java.util.LinkedList;

import miscellaneous.Utility;


import twitter4j.HashtagEntity;
import twitter4j.Status;

public class Tweet {

	//private String user;
	private String text;
	private LinkedList<String> hashtags;
	boolean isRetweet;
	private long id;
	
	public Tweet(Status status){
		//this.user = status.getUser().getScreenName();
		this.text = status.getText();
		hashtags = getHashtags(status);
		isRetweet = status.isRetweet();
		this.id = status.getId();
	}

	private LinkedList<String> getHashtags(Status status) {
		LinkedList<String> hashtags = new LinkedList<String>();
		HashtagEntity[] hTagsArray = status.getHashtagEntities();
		for (int i=0; i< hTagsArray.length; i++){
			hashtags.add(hTagsArray[i].getText().toLowerCase());
		}
		return hashtags;
	}
	
	public boolean hasHashtag(String hashtag){
		return this.hashtags.contains(hashtag);
	}

	public LinkedList<String> getHashtags() {
		return this.hashtags;
	}

	public boolean hasNewHashtags(String[] query) {
		for (String hashtag: hashtags){
			if(!Utility.exists(hashtag, query))
				return true;
		}
		return false;
	}

	public long getId() {
		return this.id;
	}

	public String getText() {
		return this.text;
	}

}
