package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import goldfish.bowl.androidphotos52.databinding.EditPhotoViewBinding;
import goldfish.bowl.androidphotos52.model.Album;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.model.Tags;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

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

//        List<String> peopleTags = dmInstance.getPeopleTags();

        binding.tagsDisplayText.setText(dmInstance.getPeopleTags().toString());
        dmInstance.displaySelectedPhotoOn(binding.imageView);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        List<String> tagValues = dmInstance.getPeopleTags();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,tagValues);
//        binding.deletePersonSpinner.setAdapter(adapter);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.countries_list);
//        textView.setAdapter(adapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public void handleAddLocationButton(Context context) {
        String value = Objects.requireNonNull(binding.addLocationTextField.getText()).toString();
        // Check if the key and value are valid.
        if (value.isEmpty()) {
            AndroidUtils.showAlert(context, "Error: Invalid Tag", "You must enter value for the Location tag.");
            return;
        }

        // Add the tag to the photo.
        dmInstance.addTagToSelectedPhoto(context, "Location", value);
       // dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);

    }

    public void handleAddPersonButton(Context context) {
        String value = Objects.requireNonNull(binding.addPersonTextField.getText()).toString();
        // Check if the key and value are valid.
        if (value.isEmpty()) {
            AndroidUtils.showAlert(context, "Error: Invalid Tag", "You must enter a value for the People tag.");
            return;
        }
        dmInstance.addTagToSelectedPhoto(context, "People", value);
    }
//
//        // Add the tag to the photo.
//        dmInstance.addTagToSelectedPhoto(context, "People", value);
//        // dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
//
//
//    }
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

    public void deleteLocationButtonClick(Context context) {

        dmInstance.deleteTagFromSelectedPhoto(context,"location: x");
    }

    public void deletePersonButtonClick(Context context) {
        // Get the tag from the choice box.
        String tag = binding.deletePersonSpinner.getSelectedItem().toString();
        //System.out.println("Tag: " + tag);

        // Check if the key and value are valid.
        if (tag.isEmpty()) {
            AndroidUtils.showAlert(context, "Error: No Tag Selected", "You must select a tag to delete.");
            return;
        }
        // Delete the tag from the photo.
        dmInstance.deleteTagFromSelectedPhoto(context,tag);
        dmInstance.displaySelectedPhotoOn(binding.imageView);
    }

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

