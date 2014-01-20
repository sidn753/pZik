package com.example.pzik;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity{
	public final static String EXTRA_MESSAGE = "songInfos";
	private ListView listView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<SongData> songs;
	private ArrayList<String> songName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Find listView
		this.listView = (ListView) findViewById(R.id.listView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** Called when user clicks on search button **/

	public void searchSong(View view) {
		
		// String url="http://pleer.com/search?q="+songToSearch.replace(' ','+')+"&target=tracks&page=1";

		EditText editText = (EditText) findViewById(R.id.edit_message);
		String songToSearch = editText.getText().toString().replace(' ', '+');

		// create url
		String urlskull = "http://mp3skull.com/mp3/" + songToSearch + ".html";

		// Retrieve song list
		new RetrieveDocTask().execute(urlskull);
		
		this.listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this, PlaySongActivity.class);
				intent.putExtra(EXTRA_MESSAGE, songs.get(pos));
		        startActivity(intent);

			}
			 
		});
		// intent.putExtra(EXTRA_MESSAGE, songToSearch);
		
	}

	 
	class RetrieveDocTask extends AsyncTask<String, Void, ArrayList<SongData>> {

		private ProgressDialog dialog;
		protected void onPreExecute() {
			dialog=ProgressDialog.show(MainActivity.this, "Searching", "Please wait while searching for music...");
		}

		@Override
		protected ArrayList<SongData> doInBackground(String... urls) {
			// TODO Auto-generated method stub
			Document doc = null;
			for (String url : urls) {

				try {
					doc = Jsoup.connect(url).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			songs= SongData.getTrackFromSkullPage(doc);
			songName = SongData.getTitle(songs);
			listAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simple_row, songName);
			return songs;
		}

		@Override
		protected void onPostExecute( ArrayList<SongData> song) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			listView.setAdapter(listAdapter);

		}

	}

}
