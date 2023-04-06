package com.commonlib.utils;

import android.app.Activity;
import android.net.Uri;

import com.commonlib.utils.getfile.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by mauliksantoki on 12/7/17.
 */

public final class PrepareMultipartBody {

    private PrepareMultipartBody(){}

    public static RequestBody requestFile;
    public static MultipartBody.Part body;
    public static File file;

    /*post_data={"sender_user_id":"112","receiver_user_id":"89","login_token":"121212","file_type":"1","file_count":"2","next_user_consultation_chat_log_id":"55"}&file_path1=FILE1&file_path2=FILE2*/

    public static MultipartBody.Part prepareFilePart(Activity activity, int i, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        file = FileUtils.getFile(activity, fileUri);

//        // create RequestBody instance from file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse(getContentResolver().getType(fileUri)),
//                        file
//                );
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

        String type = activity.getContentResolver().getType(fileUri);
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        body = MultipartBody.Part.createFormData("upload_file" + (i + 1), file.getName(), requestFile);
        // adds another part within the multipart request

        return body;
    }

    public static MultipartBody.Part prepareFilePart(Activity activity, String name, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        file = FileUtils.getFile(activity, fileUri);

//        // create RequestBody instance from file
//        RequestBody requestFile =
//                RequestBody.create(
//                        MediaType.parse(getContentResolver().getType(fileUri)),
//                        file
//                );
//
//        // MultipartBody.Part is used to send also the actual file name
//        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

        String type = activity.getContentResolver().getType(fileUri);
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part is used to send also the actual filename
        body = MultipartBody.Part.createFormData(name, file.getName(), requestFile);
        // adds another part within the multipart request

        return body;
    }
}
