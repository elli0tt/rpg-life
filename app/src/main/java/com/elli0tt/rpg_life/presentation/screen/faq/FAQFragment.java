package com.elli0tt.rpg_life.presentation.screen.faq;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.elli0tt.rpg_life.R;

import static android.app.Activity.RESULT_OK;

public class FAQFragment extends Fragment {

    private Button pictureButton;
    private AppCompatImageView imageView;

    private static final int DOWNLOAD_IMAGE_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pictureButton = view.findViewById(R.id.picture_button);
        imageView = view.findViewById(R.id.image_view);

        pictureButton.setOnClickListener(v -> {
            Intent downloadImageIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            downloadImageIntent
                    .putExtra("crop", true)
                    .putExtra("aspectX", 1)
                    .putExtra("aspectY", 1)
                    .putExtra("outputX", 200)
                    .putExtra("outputY", 200)
                    .putExtra("return-data", true);
            startActivityForResult(downloadImageIntent, DOWNLOAD_IMAGE_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DOWNLOAD_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageView.setImageBitmap(data.getExtras().getParcelable("data"));
        }

    }
}
