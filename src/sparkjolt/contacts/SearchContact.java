package sparkjolt.contacts;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchContact extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_contact);
		
	}
	
	public void back(View view) {
		Intent i = new Intent();
		
		i.putExtra("query", ((EditText) findViewById(R.id.txt_query)).getText().toString());
		i.setData(Uri.parse("Success"));
		setResult(RESULT_OK, i);
		finish();
	}

}
