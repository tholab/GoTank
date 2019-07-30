package com.example.black.go_tankuser.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.example.black.go_tankuser.network.RetrofitClient;
import com.example.black.go_tankuser.service.CompanyService;
import com.example.black.go_tankuser.service.HistoryService;
import com.example.black.go_tankuser.service.ReservasiService;
import com.example.black.go_tankuser.service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApiUtils {
    private ApiUtils(){

    }


//    //pake ini untuk koneksi heroku
//    private static final String API_URL = "https://gotank.herokuapp.com/api/";
//    public static final String ENDPOINT = "https://gotank.herokuapp.com/";


    //Pake dibawah ini untuk koneksi local
    public static final String IMAGE_DIRECTORY_NAME = "introslider";

    public static final String ENDPOINT = "http://192.168.43.108:81/";
    private static final String API_URL = "http://192.168.43.108:81/api/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    public static CompanyService getCompanyService(){
        return RetrofitClient.getClient(API_URL).create(CompanyService.class);
    }
    public static ReservasiService getReservasiService(){
        return RetrofitClient.getClient(API_URL).create(ReservasiService.class);
    }
    public static HistoryService getHistoryService(){
        return RetrofitClient.getClient(API_URL).create(HistoryService.class);
    }

    public static String getAddressSimple(Double late, Double longe, Context context) {
        String addressStr = "";
        Geocoder geocoder;
        List<Address> yourAddresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            yourAddresses = geocoder.getFromLocation(late,longe, 1);
            if (yourAddresses.size() > 0) {
                addressStr += yourAddresses.get(0).getAddressLine(0);
                addressStr += yourAddresses.get(0).getAddressLine(1);
                addressStr += yourAddresses.get(0).getAddressLine(2);
            } else {
                addressStr = "Nama jalan tidak diketahui...";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressStr;
    }

}
