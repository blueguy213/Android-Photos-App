package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;

import goldfish.bowl.androidphotos52.databinding.OpenAlbumViewBinding;
import goldfish.bowl.androidphotos52.databinding.SearchViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.model.ThumbnailAdapter;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class SearchView extends Fragment {

    private SearchViewBinding binding;
    private ThumbnailAdapter searchResultsAdapter;
    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> peopleAdapter;

    private DataManager dmInstance;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dmInstance = DataManager.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SearchViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchResultsAdapter = new ThumbnailAdapter(requireContext(), dmInstance.searchResults);
        locationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dmInstance.getLocationTags());
        peopleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dmInstance.getPeopleTags());
        updateDisplay(getContext());
        binding.nextPhotoButton.setOnClickListener((view1 -> handleNextPhotoButtonClick(getContext())));
        binding.prevPhotoButton.setOnClickListener((view1 -> handlePrevPhotoButtonClick(getContext())));
        binding.searchTagButton.setOnClickListener((view1 -> handleTagSearchButtonClick(getContext())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateDisplay(Context context) {
        searchResultsAdapter = new ThumbnailAdapter(requireContext(), dmInstance.searchResults);
        dmInstance.displaySelectedPhotoOn(binding.photoDisplayImageView);
        binding.thumbnailGridView.setAdapter(searchResultsAdapter);
        if (binding.firstKeySpinner.getSelectedItem().toString().equals("Location")) {
            binding.firstValAutoComp.setAdapter(locationAdapter);
        } else if (binding.firstKeySpinner.getSelectedItem().toString().equals("Person")) {
            binding.firstValAutoComp.setAdapter(peopleAdapter);
        }
        if (binding.secondKeySpinner.getSelectedItem().toString().equals("Location")) {
            binding.secondValAutoComp.setAdapter(locationAdapter);
        } else if (binding.secondKeySpinner.getSelectedItem().toString().equals("Person")) {
            binding.secondValAutoComp.setAdapter(peopleAdapter);
        }
        //photoDisplayImageViewdmInstance.displayThumbnailsOn(binding.thumbnailGridView,context);
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

        //Checks if there is a first and second tag
        Boolean isFirstTag = ((!(binding.firstKeySpinner.getSelectedItem().toString().isEmpty()))&&(!(binding.firstValAutoComp.getText().toString().isEmpty())));
        Boolean isSecondTag = ((!(binding.secondKeySpinner.getSelectedItem().toString().isEmpty()))&&(!(binding.secondValAutoComp.getText().toString().isEmpty())));

        String firstTagKey = null;
        String firstTagValue = null;
        String secondTagKey = null;
        String secondTagValue = null;

        if (isFirstTag) {
            firstTagKey = binding.firstKeySpinner.getSelectedItem().toString();
            firstTagValue = binding.firstValAutoComp.getText().toString();
        }

        if (isSecondTag) {
            secondTagKey =  binding.secondKeySpinner.getSelectedItem().toString();
            secondTagValue = binding.secondValAutoComp.getText().toString();
        }

        String andOr = binding.andOrSpinner.getSelectedItem().toString();

        if (firstTagKey == null && secondTagKey == null) {
            AndroidUtils.showAlert(context, "Error: No tags selected", "Please select at least one  tag to search by.");
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
            AndroidUtils.showAlert(context, "Error: Invalid selection", "Fix needed :/");
            return;
        }

        updateDisplay(context);

    }
}