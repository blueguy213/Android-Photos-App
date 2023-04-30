package goldfish.bowl.androidphotos52;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import goldfish.bowl.androidphotos52.databinding.OpenAlbumViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;

public class OpenAlbumView extends Fragment {

    private OpenAlbumViewBinding binding;

    private DataManager dmInstance;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dmInstance = DataManager.getInstance(context);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = OpenAlbumViewBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.openAlbumNameText.setText(dmInstance.getOpenedAlbumName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}