package com.example.pzik;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Parcel;
import android.os.Parcelable;

public class SongData implements Parcelable{
	
	public String duration="-:-", size="unknown", quality="unkwown";
	public String  title, link;

	public SongData(Parcel in){
		readFromParcel(in);
	}
	public SongData(String duration, String size,String quality, String title,
			String link) {
		this.duration = duration;
		this.size = size;
		this.quality=quality;
		this.title = title;
		this.link = link;
	}

	public SongData(String duration, String size,String quality, String title) {
		this.duration = duration;
		this.quality=quality;
		this.size = size;
		this.title = title;
	}
	
	public SongData(String data [], String title) {
		for(int i=0; i<data.length;i++){
			if(data[i].contains(":"))
				this.duration=data[i];
			else if(data[i].toLowerCase().contains("mb") || data[i].toLowerCase().contains("kb"))
				this.size=data[i];
			else if(data[i].toLowerCase().contains("kbps"))
				this.quality=data[i];
		}
		this.title=title;
	}

	public SongData(String[] data, String title, String link) {
		for(int i=0; i<data.length;i++){
			if(data[i].contains(":"))
				this.duration=data[i];
			else if(data[i].toLowerCase().contains("mb") || data[i].toLowerCase().contains("kb"))
				this.size=data[i];
			else if(data[i].toLowerCase().contains("kbps"))
				this.quality=data[i];
		}
		this.title=title;
		this.setLink(link);
	}

	public void setLink(String link){
		this.link=link;
	}
	
	public static ArrayList<SongData> getTrackFromSkullPage(Document doc){
		ArrayList<SongData> result =new ArrayList<SongData>();
		Elements divSongs = doc.select("div#song_html");
		for(Element dSong: divSongs){
			String text [] = dSong.getElementsByClass("left").first().html().split("<br />");
			Element rightSong=dSong.getElementById("right_song");
			String title = rightSong.child(0).text().replace("mp3","");
			String link = rightSong.child(2).select("a[href$=mp3]").first().attr("href");
			SongData sd = new SongData(text, title, link);
			result.add(sd);
		}
		return result;
		
	}

	public static ArrayList <String> getTitle(ArrayList<SongData> lSd){
		ArrayList <String> title= new ArrayList<String>();
		ListIterator <SongData> li = lSd.listIterator();
		while(li.hasNext()){
			title.add(li.next().title);
		}
		return title;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(duration);
		dest.writeString(size);
		dest.writeString(quality);
		dest.writeString(title);
		dest.writeString(link);
	}

	private void readFromParcel(Parcel in){
		this.duration=in.readString();
		this.size=in.readString();
		this.quality=in.readString();
		this.title=in.readString();
		this.link=in.readString();
	}
	  public static final Parcelable.Creator<SongData> CREATOR = new Parcelable.Creator <SongData>() {
	        public SongData createFromParcel(Parcel in ) {
	            return new SongData( in );
	        }

	        public SongData[] newArray(int size) {
	            return new SongData[size];
	        }
	    };
	
}
