package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import goldfish.bowl.androidphotos52.databinding.OpenAlbumViewBinding;
import goldfish.bowl.androidphotos52.databinding.SearchViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class SearchView extends Fragment {

    private SearchViewBinding binding;

    private DataManager dmInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = SearchViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.userOpenAlbumButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SearchView.this)
//                        .navigate(R.id.action_AlbumsView_to_OpenAlbum);
//            }
//        });


        binding.nextPhotoButton.setOnClickListener((view1 -> handleNextPhotoButtonClick(getContext())));
        binding.prevPhotoButton.setOnClickListener((view1 -> handlePrevPhotoButtonClick(getContext())));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateDisplay(Context context) {
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
        dmInstance.displayThumbnailsOn(binding.photoThumbScrollPane,context);
       // dmInstance.displayAllTagTypesOn(firstTagSpinner);
        //dmInstance.displayAllTagTypesOn(secondTagSpinner);
    }

    public void handleNextPhotoButtonClick(Context context) {
        dmInstance.nextPhoto();
        updateDisplay(context);
    }

    public void handlePrevPhotoButtonClick(Context context) {
        dmInstance.previousPhoto();
        updateDisplay(context);
    }



    public void handleTagSearchButtonClick(Context context) {



        String firstTag = binding.firstTagSpinner.getText().toString();
        String secondTag = binding.secondTagSpinner.getText().toString();

        String firstTagKey = null;
        String firstTagValue = null;
        String secondTagKey = null;
        String secondTagValue = null;

        if (firstTag.isEmpty()) {
            firstTagKey = firstTag.split(": ")[0];
            firstTagValue = firstTag.split(": ")[1];
        }

        if (secondTag.isEmpty()) {
            secondTagKey = secondTag.split(": ")[0];
            secondTagValue = secondTag.split(": ")[1];
        }

        String andOr = binding.andOrSpinner.getSelectedItem().toString();

        if (firstTagKey == null && secondTagKey == null) {
            AndroidUtils.showAlert(context, "Error: No tags selected", "Please select at least one tag to search by.");
            return;
        } else if (firstTagKey == null) {
            dmInstance.filterSearchResultsByOneTag(secondTagKey, secondTagValue);
        } else if (secondTagKey == null) {
            dmInstance.filterSearchResultsByOneTag(firstTagKey, firstTagValue);
        } else if (andOr.equals("")) {
            AndroidUtils.showAlert(context, "Error: No AND/OR selected", "Please select AND or OR to search by.");
            return;
        } else if (andOr.equals("AND")) {
            dmInstance.filterSearchResultsByTwoTagsAnd(firstTagKey, firstTagValue, secondTagKey, secondTagValue);
        } else if (andOr.equals("OR")) {
            dmInstance.filterSearchResultsByTwoTagsOr(firstTagKey, firstTagValue, secondTagKey, secondTagValue);
        } else {
            AndroidUtils.showAlert(context, "Error: Invalid AND/OR selection", "Fix needed :/");
            return;
        }

        updateDisplay(context);

    }
}