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

import goldfish.bowl.androidphotos52.databinding.OpenAlbumViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.model.ThumbnailAdapter;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class OpenAlbumView extends Fragment {

    private OpenAlbumViewBinding binding;

    private DataManager dmInstance;

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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ThumbnailAdapter thumbnailAdapter = new ThumbnailAdapter(requireContext(), dmInstance.getAlbums().get(dmInstance.getSelectedAlbumIndex()).getPhotos());
        binding.photoThumbnailGridView.setAdapter(thumbnailAdapter);

        binding.openAlbumNameText.setText(dmInstance.getOpenedAlbumName());
        binding.addPhotoButton.setOnClickListener((view1 -> handleAddPhotoButtonClick()));
        binding.removePhotoButton.setOnClickListener(view1 -> handleRemovePhotoButton(getContext()));
        binding.nextPhotoButton.setOnClickListener((view1 -> handleNextPhotoButtonClick(getContext())));
        binding.prevPhotoButton.setOnClickListener((view1 -> handlePrevPhotoButtonClick(getContext())));
        binding.movePhotoButton.setOnClickListener((view1 -> handleMovePhotoButtonClick(getContext())));
//        binding.addTagButton.setOnClickListener((view1 -> handleAddTagButtonClick(getContext())));
//        binding.deleteTagButton.setOnClickListener((view1 -> handleDeleteTagButtonClick(getContext())));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


//    /**
//     * Updates display on Album view
//     */
//    private void updateDisplay() {
//        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
//        //Why are these methods not available in data manager?
//
//        //dmInstance.displayUnopenedAlbumsOn(destinationAlbumSpinner);
//        //dmInstance.displayDeletableTagsOn(selectTagToDeleteSpinner);
//       // dmInstance.displayCreatableTagsOn(addTagKeyBox, addTagValueBox);
//    }


    private void onPhotoPicked(Context context, Uri uri) {
        if (uri != null) {
            dmInstance.addPhotoToOpenedAlbum(context, uri);
            dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
        } else {
            AndroidUtils.showAlert(getContext(),"Error: Photo not selected!", "You did not select a photo.");
        }
    }

    public void handleAddPhotoButtonClick(){
        photoPickerLauncher.launch("image/*");
    }
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Choose a Photo");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("All Images", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"),
//                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
//                new FileChooser.ExtensionFilter("GIF", "*.gif"),
//                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
//                new FileChooser.ExtensionFilter("PNG", "*.png")
//        );

//        Stage stage = new Stage();
//        File selectedFile = fileChooser.showOpenDialog(stage);

//        if (selectedFile != null) {
//            try {
//                String selectedFileURI = selectedFile.toURI().toString();
//
//                if (dmInstance.hasPhoto(selectedFileURI)) {
//                    AndroidUtils.showAlert(context,"Error: Photo already exists!", "The photo you selected already exists in the album.");
//                    return;
//                }
//
//                dmInstance.addPhotoToOpenedAlbum(context, selectedFileURI);
//                updateDisplay();
//            } catch (MalformedURLException e) {
//                AndroidUtils.showAlert(context, "Error: Invalid File", e.getMessage());
//            }
//        } else {
//            AndroidUtils.showAlert(context, "Error: Enter a file!", "You did not select a file that exists.");
//        }
//        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);


    public void handleRemovePhotoButton(Context context){ //not tested
        dmInstance.removeSelectedPhoto(context);
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
    }

    public void handleNextPhotoButtonClick(Context context) {
        dmInstance.nextPhoto();
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
    }

    public void handlePrevPhotoButtonClick(Context context) {
        dmInstance.previousPhoto();
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
    }

    public void handleMovePhotoButtonClick(Context context) {
        String destinationAlbumName = binding.destinationAlbumSpinner.getSelectedItem().toString();
        if (dmInstance.copySelectedPhotoToAlbum(context, destinationAlbumName) > -1) {
            dmInstance.removeSelectedPhoto(context);
        }
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
    }
//
//    public void handleCopyPhotoButtonClick(Context context) {
//        String destinationAlbumName = binding.destinationAlbumSpinner.getSelectedItem().toString();
//        dmInstance.copySelectedPhotoToAlbum(context, destinationAlbumName);
//    }


//    public void handleAddTagButtonClick(Context context) {
//        // Get the key and value from the text fields.
//        String key = binding.addTagKeyBox.getSelectedItem().toString();
//        String value = binding.addTagValueBox.getSelectedItem().toString();
//
//        // Check if the key and value are valid.
//        if (key.isEmpty() || value.isEmpty()) {
//            AndroidUtils.showAlert(context, "Error: Invalid Tag", "You must enter a key and value for the tag.");
//            return;
//        }
//
////        // Check if the tag should be unique
////        if (isTagUnique.isSelected() && dmInstance.hasTagKey(key)) {
////            JavaFXUtils.showAlert(context, "Error : Invalid Tag", "That tag is unique and already exists.");
////            return;
////        }
//
//        // Add the tag to the photo.
//        dmInstance.addTagToSelectedPhoto(context, key, value);
//        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
//    }


//    public void handleDeleteTagButtonClick(Context context) {
//        // Get the tag from the choice box.
//        String tag = binding.selectTagToDeleteSpinner.getSelectedItem().toString();
//        //System.out.println("Tag: " + tag);
//
//        // Check if the key and value are valid.
//        if (tag.isEmpty()) {
//            AndroidUtils.showAlert(context, "Error: No Tag Selected", "You must select a tag to delete.");
//            return;
//        }
//        // Delete the tag from the photo.
//        dmInstance.deleteTagFromSelectedPhoto(context,tag);
//        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
//    }


}