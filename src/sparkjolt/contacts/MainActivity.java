package sparkjolt.contacts;

import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.*;

public class MainActivity extends ListActivity implements Constants {
	
	// Database adapter for the contacts
	private Adapter db = new Adapter(this);
	private int currentItem;
	private ArrayAdapter<String> adapter;
	private ArrayList<Integer> contacts_id = new ArrayList<Integer>();
	private ArrayList<String> contacts_name = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		// Open the database
		db.open();
		listContacts();
		
		// Make the home application button clickable
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Add the contextual action bar
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        switch (item.getItemId()) {
		            case R.id.button_delete:
		            	// Delete the contacts from the database
		            	boolean b = db.deleteContact(contacts_id.get(currentItem));
		                mode.finish();
		                listContacts();
		                return true;
		            case R.id.button_phone:
		            	String number = db.getContact(contacts_id.get(currentItem)).getString(3);
		            	startActivity(new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:+" + number)));
		            	mode.finish();
		            	return true;
		            case R.id.button_email:
		            	Intent i = new Intent(Intent.ACTION_SEND); 
		            	i.setType("text/plain"); 
		            	i.putExtra(Intent.EXTRA_EMAIL, new String[]{db.getContact(contacts_id.get(currentItem)).getString(2)}); 
		            	startActivity(i);
		            	return true;
		            default:
		                return false;
		        }
		    }

		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.contact_actions, menu);
		        return true;
		    }

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				currentItem = position;
			}
			
		});
	}

	private void searchContacts(String query) {
		// Display all the contacts matching a query
		contacts_name.clear();
		contacts_id.clear();
		Cursor c = db.searchContacts(query);
		if (c.moveToFirst()) {
			do {
				contacts_name.add(c.getString(1));
				contacts_id.add(c.getInt(0));
			} while (c.moveToNext());
			adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.label, contacts_name);
			setListAdapter(adapter);
		}
	}

	// Display all the contacts by creating buttons
	private void listContacts() {
		contacts_name.clear();
		contacts_id.clear();
		Cursor c = db.getAllContacts();
		if (c.moveToFirst()) {
			do {
				contacts_name.add(c.getString(1));
				contacts_id.add(c.getInt(0));
			} while (c.moveToNext());
		}
		adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.label, contacts_name);
		setListAdapter(adapter);
	}
	
	public void addContact() {
		startActivityForResult(new Intent("sparkjolt.contacts.AddContact"), ADD_REQUEST_CODE);
	}
	
	
	public void email(View v) {
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{((EditText) findViewById(R.id.edit_txt_email)).getText().toString()}); 
		startActivity(i);
	}
	
	public void searchContact() {
		startActivityForResult(new Intent("sparkjolt.contacts.SearchContact"), SEARCH_REQUEST_CODE);
	}
	
	public void editContact(int id) {
		Intent i = new Intent("sparkjolt.contacts.EditContact");
		i.putExtra("name", db.getContact(id).getString(1));
		i.putExtra("email", db.getContact(id).getString(2));
		i.putExtra("phone", db.getContact(id).getString(3));
		i.putExtra("id", id);
		startActivityForResult(i, EDIT_REQUEST_CODE);
	}
	
	@Override
	public void onActivityResult(int request, int result, Intent data) {
		if (request == ADD_REQUEST_CODE && result == RESULT_OK) {
			long id = db.insertContact(data.getStringExtra("name"), data.getStringExtra("email"), data.getStringExtra("phone"));
			listContacts();
		} else if (request == SEARCH_REQUEST_CODE && result == RESULT_OK) {
			searchContacts(data.getStringExtra("query"));
		} else if (request == EDIT_REQUEST_CODE && result == RESULT_OK) {
			boolean b = db.deleteContact(data.getIntExtra("id", 0));
			long id = db.insertContact(data.getStringExtra("name"), data.getStringExtra("email"), data.getStringExtra("phone"));
			listContacts();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		createMenu(menu);
		return true;
	}
	
	private void createMenu(Menu menu) {
		MenuItem all = menu.add(0, 0, 0, "View All");
		all.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
    	MenuItem search = menu.add(0, 1, 0, "Search");
    	search.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	
		MenuItem add = menu.add(0, 2, 0, "Add");
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return menuChoice(item);
	}
	
	private boolean menuChoice(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent i = new Intent(this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				return true;
			case 0:
				listContacts();
				return true;
			case 1:
				searchContact();
				return true;
			case 2:
				addContact();
				return true;
		}
		return false;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		editContact(contacts_id.get(position));
	}
}