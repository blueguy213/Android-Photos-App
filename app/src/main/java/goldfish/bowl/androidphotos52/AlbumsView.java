package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import goldfish.bowl.androidphotos52.databinding.AlbumsViewBinding;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class AlbumsView extends Fragment {

    private AlbumsViewBinding binding;
    private DataManager dmInstance;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dmInstance = DataManager.getInstance(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AlbumsViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dmInstance.displayAlbumsOn(getContext(), binding.albumsListView);

        binding.userCreateAlbumButton.setOnClickListener(this::createAlbumButtonCallback);
        binding.userDeleteAlbumButton.setOnClickListener(view1 -> deleteAlbumButtonCallback());
        binding.userOpenAlbumButton.setOnClickListener(view1 -> openAlbumButtonCallback(getContext(), R.id.main_fragment_container));
        binding.userRenameAlbumButton.setOnClickListener(view1 -> renameAlbumButtonCallback());
    }

    private void createAlbumButtonCallback(View view) {
        dmInstance.addAlbum(getContext(), Objects.requireNonNull(binding.userCreateAlbumField.getText()).toString());
        dmInstance.displayAlbumsOn(getContext(), binding.albumsListView);
    }

    private void deleteAlbumButtonCallback() {
        View selectedAlbumName = binding.albumsListView.getSelectedView();
        if (selectedAlbumName != null) {
            dmInstance.removeAlbum(getContext(), ((TextView) selectedAlbumName).getText().toString());
            dmInstance.displayAlbumsOn(getContext(), binding.albumsListView);
        } else {
            AndroidUtils.showAlert(getContext(), "No album selected", "Please select an album to delete");
        }
    }

    private void openAlbumButtonCallback(Context context, int containerId) {
        TextView selectedAlbumView = ((TextView) binding.albumsListView.getSelectedView());
        if (selectedAlbumView != null) {
            String selectedAlbumName = selectedAlbumView.getText().toString();
            dmInstance.openAlbum(selectedAlbumName);
            AndroidUtils.switchFragment(context, containerId, new OpenAlbumView());
        } else {
            AndroidUtils.showAlert(getContext(), "No album selected", "Please select an album to open");
        }

    }

    private void renameAlbumButtonCallback() {
        View selectedAlbumName = binding.albumsListView.getSelectedView();
        if (selectedAlbumName != null) {
            dmInstance.renameAlbum(getContext(), ((TextView) selectedAlbumName).getText().toString(), Objects.requireNonNull(binding.userRenameAlbumField.getText()).toString());
            dmInstance.displayAlbumsOn(getContext(), binding.albumsListView);
        } else {
            AndroidUtils.showAlert(getContext(), "No album selected", "Please select an album to rename");
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}