package sparkjolt.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddContact extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);
	}
	
	public void back(View view) {
		Intent i = new Intent();
		
		i.putExtra("name", ((EditText) findViewById(R.id.txt_name)).getText().toString());
		i.putExtra("email", ((EditText) findViewById(R.id.txt_email)).getText().toString());
		i.putExtra("phone", ((EditText) findViewById(R.id.txt_phone)).getText().toString());
		i.setData(Uri.parse("Success"));
		setResult(RESULT_OK, i);
		finish();
	}

}
