package goldfish.bowl.androidphotos52.utils;

import android.content.Context;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public class AndroidUtils {

        public static void showAlert(Context context,  String title, String message) {
            Builder builder = new Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
}
