package edu.css.thauck.chitchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessages> adapter;
    private static final int RC_SIGN_IN = 123;
    public static final String ChatDataTag = "ChatData";

    public MainActivity() {
    }

    /*
     * Checks if user is signed in. If user is, displays chat room.
     * If user is not, redirects user to sign-in or sign-up screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in or sign up activity
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), RC_SIGN_IN);
        } else {
            // User is already signed in, display a welcome Toast
            Toast.makeText(this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
            // Load chat room contents
            displayChatMessages();
        }

        /*
         * OnClickListener for Floating Action Button to post a new message.
         * Gets a DB reference object.
         * Combination of push and setValue methods automatically generates a new key.
         */
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText message = (EditText)findViewById(R.id.editTextMessage);
                // Read the input field and push a new instance of ChatMessage to the Firebase database
                    // Original Code
                    //FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessages(message.getText().toString(),
                    // FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myDbRef;
                myDbRef = database.getReference(ChatDataTag);
                String messageAuthor = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                String messageText = message.getText().toString();
                ChatMessages msg = new ChatMessages(messageText, messageAuthor);
                String key = myDbRef.child(ChatDataTag).push().getKey();
                myDbRef.child(key).setValue(message);

                // Clear the input
                message.setText("");
            }
        });
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.listViewListMessages);

        adapter = new FirebaseListAdapter<ChatMessages>(this, ChatMessages.class, R.layout.chat_messages, FirebaseDatabase.getInstance().getReference()) {
            // Method to populate the views of each list item.
            // Works similar to the ArrayAdapter class.
            @Override
            protected void populateView(View view, ChatMessages model, int position) {
                // Get references to the views of chat_messages.xml
                TextView messageText = (TextView)view.findViewById(R.id.textViewMessageText);
                TextView messageAuthor = (TextView)view.findViewById(R.id.textViewMessageAuthor);
                TextView messageTime = (TextView)view.findViewById(R.id.textViewMessageTime);

                // Set there text
                messageText.setText(model.getMessageText());
                messageAuthor.setText(model.getMessageAuthor());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("MM-dd-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }

    /*
     * Override to handle result of the intent when a user has signed in.
     * If result code is good, user has signed in successfully.
     * This will display the chat messages.
     * If result code is not good, app will close.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            // Sign in successfully
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Welcome! You are signed in.", Toast.LENGTH_LONG).show();
                displayChatMessages();
            } else {
                // Failed login
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                // Close the app
                finish();
            }
        }
    }

    /* FirebaseUI uses smart lock for passwords.
     * Automatically sign users in to app using saved login info.
     * Method to allow users to sign out when using smart lock.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*
     * Method to handle click events on the menu items.
     * Calls the signOut() method from the AuthUI class.
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSignOut) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                // OnCompleteListener called because sign-out is async
                // Displays Toast message
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "You have been signed out.", Toast.LENGTH_LONG).show();
                    // Close activity
                    finish();
                }
            });
        }
        return true;
    }
}