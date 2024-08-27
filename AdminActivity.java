package com.example.qradmin;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class AdminActivity extends AppCompatActivity {

    private EditText nameEditText, bioEditText, websiteEditText;
    private ImageView imageView;
    private Button saveButton, uploadImageButton;
    private Uri imageUri;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nameEditText = findViewById(R.id.et_name);
        bioEditText = findViewById(R.id.et_bio);
        websiteEditText = findViewById(R.id.et_website);
        imageView = findViewById(R.id.iv_image);
        saveButton = findViewById(R.id.btn_save);
        uploadImageButton = findViewById(R.id.btn_upload_image);

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users"); // Reference to the "Users" node
        storageReference = FirebaseStorage.getInstance().getReference("user_images");

        uploadImageButton.setOnClickListener(v -> openImagePicker());
        saveButton.setOnClickListener(v -> uploadImageAndSaveUser());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadImageAndSaveUser() {
        if (imageUri != null) {
            String imageID = UUID.randomUUID().toString();
            storageReference.child(imageID).putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    storageReference.child(imageID).getDownloadUrl().addOnCompleteListener(uriTask -> {
                        if (uriTask.isSuccessful()) {
                            String imageUrl = uriTask.getResult().toString();
                            saveUserToFirebase(imageUrl);
                        }
                    });
                }
            });
        } else {
            saveUserToFirebase(null);
        }
    }

    private void saveUserToFirebase(String imageUrl) {
        String name = nameEditText.getText().toString();
        String bio = bioEditText.getText().toString();
        String website = websiteEditText.getText().toString();

        if (name.isEmpty() || bio.isEmpty() || website.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, bio, website, imageUrl);
        String userId = UUID.randomUUID().toString(); // Generate unique user ID

        // Store data in Firebase Realtime Database under the generated user ID
        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminActivity.this, "User added successfully to Realtime Database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminActivity.this, "Failed to add user to Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
