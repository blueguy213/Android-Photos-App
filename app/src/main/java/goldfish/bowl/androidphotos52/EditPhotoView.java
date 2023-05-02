package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import goldfish.bowl.androidphotos52.databinding.EditPhotoViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;

public class EditPhotoView extends Fragment {

    private DataManager dmInstance;
    private EditPhotoViewBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dmInstance = DataManager.getInstance(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = EditPhotoViewBinding.inflate(inflater, container, false);
        binding.addLocationButton.setOnClickListener(view1 -> handleAddLocationButton(getContext()));
        binding.addPersonButton.setOnClickListener((view1 -> handleAddPersonButton(getContext())));
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void handleAddLocationButton(Context context) {
        //String value = binding.addLocationAutocomplete.get;

    }

    public void handleAddPersonButton(Context context) {
        //String value = binding.addLocationAutocomplete.get;

    }
//    public void handleAddTagButtonClick(Context context) {
//        // Get the key and value from the text fields.
//        String key = binding.addTagKeyBox.getSelectedItem().toString();
//        String value = binding.addLocationAutocomplete.getSelectedItem().toString();
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
//
//
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
//


}
