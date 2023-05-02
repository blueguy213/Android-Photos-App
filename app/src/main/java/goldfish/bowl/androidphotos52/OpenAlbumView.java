package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import goldfish.bowl.androidphotos52.databinding.OpenAlbumViewBinding;
import goldfish.bowl.androidphotos52.model.Album;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.model.ThumbnailAdapter;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class OpenAlbumView extends Fragment {

    private OpenAlbumViewBinding binding;

    private DataManager dmInstance;

    private ThumbnailAdapter thumbnailAdapter;

    private final ActivityResultLauncher<String> photoPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), (ActivityResultCallback<Uri>) uri -> onPhotoPicked(getContext(), uri));

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dmInstance = DataManager.getInstance(context);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = OpenAlbumViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void updateDisplay() {
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
        binding.photoThumbnailGridView.setAdapter(thumbnailAdapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thumbnailAdapter = new ThumbnailAdapter(requireContext(), dmInstance.getAlbums().get(dmInstance.getSelectedAlbumIndex()).getPhotos());
        updateDisplay();
        binding.openAlbumNameText.setText(dmInstance.getOpenedAlbumName());
        binding.addPhotoButton.setOnClickListener((view1 -> handleAddPhotoButtonClick()));
        binding.removePhotoButton.setOnClickListener(view1 -> handleRemovePhotoButton(getContext()));
        binding.nextPhotoButton.setOnClickListener((view1 -> handleNextPhotoButtonClick(getContext())));
        binding.prevPhotoButton.setOnClickListener((view1 -> handlePrevPhotoButtonClick(getContext())));
        binding.movePhotoButton.setOnClickListener((view1 -> handleMovePhotoButtonClick(getContext())));
        binding.editPhotoButton.setOnClickListener((view1 -> AndroidUtils.switchFragment(getContext(), R.id.main_fragment_container, new EditPhotoView())));
        List<Album> unopenedAlbums = new ArrayList<Album>(dmInstance.getAlbums());
        unopenedAlbums.remove(dmInstance.getSelectedAlbumIndex());
        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(getContext(), android.R.layout.simple_spinner_item,unopenedAlbums);
        binding.destinationAlbumSpinner.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void onPhotoPicked(Context context, Uri uri) {
        if (uri != null) {
            dmInstance.addPhotoToOpenedAlbum(context, uri);
        } else {
            AndroidUtils.showAlert(getContext(),"Error: Photo not selected!", "You did not select a photo.");
        }
        updateDisplay();
    }

    public void handleAddPhotoButtonClick(){
        photoPickerLauncher.launch("image/*");
    }


    public void handleRemovePhotoButton(Context context){ //not tested
        dmInstance.removeSelectedPhoto(context);
        updateDisplay();
    }

    public void handleNextPhotoButtonClick(Context context) {
        dmInstance.nextPhoto();
        updateDisplay();
    }

    public void handlePrevPhotoButtonClick(Context context) {
        dmInstance.previousPhoto();
        updateDisplay();
    }

    public void handleMovePhotoButtonClick(Context context) {
        Album destinationAlbum = (Album) binding.destinationAlbumSpinner.getSelectedItem();
        if (destinationAlbum == null) {
            AndroidUtils.showAlert(context, "Error: No Destination Album Selected", "You must select a destination album to move photo.");
            return;
        }
        if (dmInstance.copySelectedPhotoToAlbum(context, destinationAlbum.toString()) > -1) {
            dmInstance.removeSelectedPhoto(context);
        }
        updateDisplay();
    }
}