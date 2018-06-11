package autodex.com.autodex;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import autodex.com.autodex.activitys.UpdateContactActivity;
import autodex.com.autodex.interfacecallback.CameraAndGalleryCallBack;
import autodex.com.autodex.model.webresponse.ProfileDetails;

/**
 * Created by yasar on 7/9/17.
 */

public class Utility {


    private static final String TAG = "Utils";
    private static AppCompatDialog progressDialog;

    public static void showProgress(Context context, String msg) {
//        if (!((Activity) context).isFinishing()) {
        //show dialog

        progressDialog = new AppCompatDialog(context);
//        progressDialog.setTitle("Contact in");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_loading);
//        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
//        }
    }

    public static void showProgressContactInit(Context context, String msg) {
//        if (!((Activity) context).isFinishing()) {
        //show dialog

        progressDialog = new AppCompatDialog(context);
//        progressDialog.setTitle("Contact in");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_loading);
//        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("Contact initializing");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setMessage(msg + " wait");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        }
    }

    public static void hideProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


    public static void startAct(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showDialog(Context context, String title, String msg) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//            builder = new AlertDialog.Builder(context);
//        }
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showDialog(Context context, String title, String msg, final DialogButtonListener dialogButtonListener) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//            builder = new AlertDialog.Builder(context);
//        }
        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogButtonListener.ok();
                        // continue with delete
                    }
                })

                .show();
    }

    public interface DialogButtonListener {
        void ok();
    }

    public static void showMsg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }


    public static boolean emptyValdate(String values) {

        return (values != null && values.length() > 0) ? true : false;
    }

    public static boolean validateNumber(String S) {
        String Regex = "[^\\d]";
        String PhoneDigits = S.replaceAll(Regex, "");
//        return (PhoneDigits.length() != 10);
        return (PhoneDigits.length() != 10);
    }


    public static void callDatePicker(final Context mContext, final EditText your_view) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                String myFormat = "dd-MM-yyyy"; // your format
                String myFormat = "MM-dd-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                your_view.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    public static JSONObject getJSONObjct(String key, Object values) {


        try {
            return new JSONObject().put(key, values);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static void showDialog(final String msg, final Context context,
                                  final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }

//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
    }


    public static void setSpinnerToValue(Spinner spinner, String value) {
        Log.e(TAG, "setSpinnerToValue: " + value);
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().toLowerCase().equals(value.toLowerCase())) {

                Log.e(TAG, "setSpinnerToValue: " + adapter.getItem(i).toString() + "  " + value.toLowerCase());

                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    public static void callCameraAndGallery(Context context, CameraAndGalleryCallBack cameraAndGalleryCallBack) {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = Utility.checkPermission(context);

            if (items[item].equals("Take Photo")) {
                if (result)
                    cameraAndGalleryCallBack.callCamera("Take Photo");

            } else if (items[item].equals("Choose from Library")) {
                if (result)
                    cameraAndGalleryCallBack.callCamera("Choose from Library");

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();


    }
}
