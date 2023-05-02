package goldfish.bowl.androidphotos52;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import goldfish.bowl.androidphotos52.databinding.AlbumsViewBinding;
import goldfish.bowl.androidphotos52.model.Album;
import goldfish.bowl.androidphotos52.model.DataManager;
import goldfish.bowl.androidphotos52.utils.AndroidUtils;

public class AlbumsView extends Fragment {

    private AlbumsViewBinding binding;
    private DataManager dmInstance;
    private ListAdapter albumListAdapter;

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
        albumListAdapter = new ArrayAdapter<Album>(requireContext(), android.R.layout.simple_list_item_1, dmInstance.getAlbums());
        binding.albumsListView.setAdapter((ListAdapter) albumListAdapter);
        binding.albumsListView.setOnItemClickListener((parent, view1, position, id) -> onAlbumSelected(position));;
        binding.userCreateAlbumButton.setOnClickListener(view1 -> createAlbumButtonCallback());
        binding.userDeleteAlbumButton.setOnClickListener(view1 -> deleteAlbumButtonCallback());
        binding.userOpenAlbumButton.setOnClickListener(view1 -> openAlbumButtonCallback(getContext(), R.id.main_fragment_container));
        binding.userRenameAlbumButton.setOnClickListener(view1 -> renameAlbumButtonCallback());
    }

    private void onAlbumSelected(int index) {
        dmInstance.setSelectedAlbumIndex(index);
        // Set the background color of the selected item to blue and the rest to white
        for (int i = 0; i < binding.albumsListView.getChildCount(); i++) {
            TextView textView = (TextView) binding.albumsListView.getChildAt(i);
            if (i == index) {
                textView.setBackgroundColor(getResources().getColor(R.color.pink, null));
            } else {
                textView.setBackgroundColor(getResources().getColor(R.color.white, null));
            }
        }
    }

    private void createAlbumButtonCallback() {
        dmInstance.addAlbum(getContext(), Objects.requireNonNull(binding.userCreateAlbumField.getText()).toString());
        binding.albumsListView.setAdapter((ListAdapter) albumListAdapter);
    }

    private void deleteAlbumButtonCallback() {
        int selectedAlbumIndex = dmInstance.getSelectedAlbumIndex();
        if (selectedAlbumIndex != -1) {
            // Get the album name using the selected album index
            Album album = (Album) albumListAdapter.getItem(selectedAlbumIndex);
            dmInstance.removeAlbum(getContext(), album.getName());
            binding.albumsListView.setAdapter((ListAdapter) albumListAdapter);
        } else {
            AndroidUtils.showAlert(getContext(), "No album selected", "Please select an album to delete");
        }
    }

    private void openAlbumButtonCallback(Context context, int containerId) {
        int selectedAlbumIndex = dmInstance.getSelectedAlbumIndex();
        if (selectedAlbumIndex != -1) {
            Album album = (Album) albumListAdapter.getItem(selectedAlbumIndex);
            dmInstance.openAlbum(album.getName());
            AndroidUtils.switchFragment(context, containerId, new OpenAlbumView());
        } else {
            AndroidUtils.showAlert(getContext(), "No album selected", "Please select an album to open");
        }

    }

    private void renameAlbumButtonCallback() {
        int selectedAlbumIndex = dmInstance.getSelectedAlbumIndex();
        if (selectedAlbumIndex != -1) {
            Album album = (Album) albumListAdapter.getItem(selectedAlbumIndex);
            dmInstance.renameAlbum(getContext(), album.getName(), Objects.requireNonNull(binding.userRenameAlbumField.getText()).toString());
            binding.albumsListView.setAdapter((ListAdapter) albumListAdapter);
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