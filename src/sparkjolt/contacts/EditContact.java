package sparkjolt.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditContact extends Activity {
	
	private int id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
		
		String name = getIntent().getStringExtra("name");
		String email = getIntent().getStringExtra("email");
		String phone = getIntent().getStringExtra("phone");
		id = getIntent().getIntExtra("id", 0);
		((EditText) findViewById(R.id.edit_txt_name)).setText(name, TextView.BufferType.EDITABLE);
		((EditText) findViewById(R.id.edit_txt_email)).setText(email, TextView.BufferType.EDITABLE);
		((EditText) findViewById(R.id.edit_txt_phone)).setText(phone, TextView.BufferType.EDITABLE);
	}
	
	public void call(View v) {
    	String number = ((EditText) findViewById(R.id.edit_txt_phone)).getText().toString();
    	startActivity(new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+" + number)));
	}
	
	public void email(View v) {
    	Intent i = new Intent(Intent.ACTION_SEND); 
    	i.setType("text/plain"); 
    	i.putExtra(Intent.EXTRA_EMAIL, new String[]{((EditText) findViewById(R.id.edit_txt_email)).getText().toString()}); 
    	startActivity(i);
	}
	
	public void back(View view) {
		Intent i = new Intent();
		
		i.putExtra("name", ((EditText) findViewById(R.id.edit_txt_name)).getText().toString());
		i.putExtra("email", ((EditText) findViewById(R.id.edit_txt_email)).getText().toString());
		i.putExtra("phone", ((EditText) findViewById(R.id.edit_txt_phone)).getText().toString());
		i.putExtra("id", id);
		i.setData(Uri.parse("Success"));
		setResult(RESULT_OK, i);
		finish();
	}

}
