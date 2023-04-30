package goldfish.bowl.androidphotos52.utils;

import android.content.Context;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AndroidUtils {

        public static void showAlert(Context context,  String title, String message) {
            Builder builder = new Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        public static void switchFragment(Context context, int containerId, Fragment fragment) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(containerId, fragment);
            fragmentTransaction.commit();
        }
}
