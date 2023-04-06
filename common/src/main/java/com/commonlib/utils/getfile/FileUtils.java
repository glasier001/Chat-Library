
package com.commonlib.utils.getfile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.commonlib.BuildConfig;
import com.commonlib.constants.AppConstants;
import com.commonlib.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.content.FileProvider;

/**
 * @author Peli
 * @author paulburke (ipaulpro)
 * @version 2013-12-11
 */
public final class FileUtils {
    public static final String MIME_TYPE_AUDIO = "audio/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "application/*";
    public static final String HIDDEN_PREFIX = ".";
    public static final boolean DEBUG = false; // Set to true to enable logging


    /**
     * TAG for log messages.
     */
    static final String TAG = "FileUtils";
    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    public static Comparator<File> sComparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase().compareTo(
                    f2.getName().toLowerCase());
        }
    };


    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories) and skip hidden files
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };
    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sDirFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return directories only and skip hidden directories
            return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    public static String imagePath;


    private FileUtils() {
    } //public constructor to enforce Singleton pattern

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }


    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath(final Context context, final Uri uri) {

        if (DEBUG)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isLocalStorageDocument(uri)) {
                    // The path is the id
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        return DocumentsContract.getDocumentId(uri);
                    }
                }
                // ExternalStorageProvider
                else if (isExternalStorageDocument(uri)) {
                    String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    String id = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        id = DocumentsContract.getDocumentId(uri);
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                } else {
                    try {
                        InputStream io = context.getContentResolver().openInputStream(uri);
                        Log.e("drive_io-------", String.valueOf(io));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }

                if (isGoogleDocUri(uri)) {
                    return uri.getLastPathSegment();
                }

                String test = uri.getLastPathSegment();
                Log.e("drive_uri---------", test);

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is {@link LocalStorageProvider}.
     * @author paulburke
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return LocalStorageProvider.AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /*-------------------------------*/

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Docs.
     */
    public static boolean isGoogleDocUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, File file) {
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail");

        if (!isMediaUri(uri)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bm = null;
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (DEBUG)
                        Log.d(TAG, "Got thumb ID: " + id);

                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Video.Thumbnails.MINI_KIND,
                                null);
                    } else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                                resolver,
                                id,
                                MediaStore.Images.Thumbnails.MINI_KIND,
                                null);
                    }
                }
            } catch (Exception e) {
                if (DEBUG)
                    Log.e(TAG, "getThumbnail", e);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return bm;
    }

    /**
     * Convert File into Uri. If the return uri does not contain content schema, it can crash app especially in cursor operation.
     *
     * @param file
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    public static Uri getUriFromStringPath(String path) {
        return Uri.parse(path);
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (type == null) {
            type = url.substring(url.lastIndexOf(".") + 1);
        }
        return type;
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    public static Intent createGetContentIntent() {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    public static Bitmap getBitmapFromUri(Uri conentUri) {
        Bitmap bitmap = null;
        File file = getFileFromUri(conentUri);
        try {
            InputStream ims = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File getFileFromUri(Uri contentUri) {
        File file = null;
        if (contentUri != null) {
            file = new File(contentUri.getPath());
        }
        return file;
    }

    public static void getPersistablePermission(Activity activity, Intent data, Uri uri) {

        final int takeFlags = data.getFlags()
                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//             Check for the freshest data.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getContentResolver().takePersistableUriPermission(uri, takeFlags);
        }

        ////revoke permisions
        activity.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, Calendar.getInstance().getTimeInMillis() + "", null);
        return Uri.parse(path);
    }

    public static String getStringPathFromURI(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String getImagePathFromUri(Uri contentUri) {
        return getFileFromUri(contentUri).getAbsolutePath();
    }

    public static File saveImage(Context mContext, Bitmap bitmap) {
        String imagePath = null;
        File imageFile = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);

        File myFolder = getAppImageDir();

        try {
            imageFile = new File(myFolder, Calendar.getInstance().getTimeInMillis() + ".PNG");
            imageFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());

            MediaScannerConnection.scanFile(mContext, new String[]{imageFile.getPath()},
                    new String[]{"image/*"}, null);

            fileOutputStream.close();
            imagePath = imageFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public static File getAppImageDir() {
        File myFolder = new File(Environment.getExternalStorageDirectory() + AppConstants.APPLICATION_DIR_NAME
                + AppConstants.IMAGES_DIR);
        if (!myFolder.exists()) {
            myFolder.mkdirs();
        }
        return myFolder;
    }

    public static File getAppVideoDir() {
        File myFolder = new File(Environment.getExternalStorageDirectory() + AppConstants.APPLICATION_DIR_NAME
                + AppConstants.VIDEOS_DIR);
        if (!myFolder.exists()) {
            myFolder.mkdirs();
        }
        return myFolder;
    }

    public static Bitmap getBitmapByContentResolver(Context context, Uri contentUri) {
        Bitmap bitmap = null;
        if (contentUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(), contentUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static List<String> getFileInformation(Activity activity, Context context, String mediaPath) {

        List<String> fileInfo = new ArrayList<>();
        String mimeType = null;
        String videoDuration = "";

        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};


        Cursor imagecursor = null;
        Cursor imagecursory = null;

        // FIXME: 16/3/18  sagar: Try to find length if possible
        File file = new File(mediaPath);
        Uri uri = getUri(context, file);
        mimeType = activity.getContentResolver().getType(uri);

        if (mimeType == null) {
            mimeType = getMimeTypeByStringPath(mediaPath);
        }

        if (mimeType == null) {
            mimeType = getMimeType(file);
        }

        if (mimeType != null) {
            if (mimeType.contains("video")) {
                videoDuration = getDurationMark(mediaPath);
            }
        }

        try {
            imagecursor = activity.managedQuery(uri, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            // FIXME: 15/3/18  sagar: In case of any conflict, try above method
//            imagecursory = activity.getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

        } catch (Exception e) {
            e.printStackTrace();
        }


        int imageNameColumnIndex = imagecursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int imageSizeColumnIndex = imagecursor.getColumnIndex(OpenableColumns.SIZE);


        //We will not get it in cursor but We already have it!
//                String imagePath = imagecursor.getString(imagePathColumnIndex);
//                imagecursor.moveToPosition(i);

        imagecursor.moveToFirst();

        String mediaName = imagecursor.getString(imageNameColumnIndex);
        String mediaSize = videoDuration + " " + Utils.convertBytesToSuitableUnit(Long.parseLong(imagecursor.getString(imageSizeColumnIndex)));

        fileInfo.add(mediaPath);
        fileInfo.add(mediaSize);
        fileInfo.add(mediaName);

        return fileInfo;
    }

    public static Uri getUri(Context context, File file) {
        return FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider",
                file);
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeTypeByStringPath(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getDurationMark(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
        } catch (Exception e) {
//            Log.e("getDurationMark", e.toString());
            return "?:??";
        }
        String time = null;

        //fix for the gallery picker crash
        // if it couldn't detect the media file
        try {
//            Log.e("file", filePath);
            time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            Log.e("getDurationMark", ex.toString());
        }

        //fix for the gallery picker crash
        // if it couldn't extractMetadata() of a media file
        //time was null
        time = time == null ? "0" : time.isEmpty() ? "0" : time;
        //bam crash - no more :)
        int timeInMillis = Integer.parseInt(time);
        int duration = timeInMillis / 1000;
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append(":");
        }
        if (minutes < 10) {
            sb.append("0").append(minutes);
        } else {
            sb.append(minutes);
        }
        sb.append(":");
        if (seconds < 10) {
            sb.append("0").append(seconds);
        } else {
            sb.append(seconds);
        }
        return sb.toString();
    }

    public static File saveAndGetFile(Context context, Uri uri, String fileDir, boolean deleteOldContents) {
        // FIXME: 22/12/17  sagar: Sometimes, uri is null
        return saveFile(context, uri, fileDir, deleteOldContents);
    }

    public static File saveFile(Context context, Uri uri, String fileDir, boolean deleteOldContents) {
        return FileUtils.saveFile(context,
                new File(
                        Environment.getExternalStorageDirectory()
                                + File.separator
                                + AppConstants.APPLICATION_DIR_NAME
                                + fileDir),
                uri, FileUtils.getFileName(context, uri), deleteOldContents);
    }

    /**
     * This method saves/writes/makes new destination file from the source file to the given directory.
     *
     * @param context             Present Context
     * @param directory           A file having full string path where we want to save/write new file from the source
     * @param source              A Uri source from where we will read
     * @param isDeleteOldContents A boolean true if we want to delete all available contents inside of our file directory.
     */
    public static File saveFile(Context context, File directory, Uri source, String destination, boolean isDeleteOldContents) {

        //TODO 26/10/17 => sagar =>Note :-If true, Deletes old document to reduce app data?
        if (isDeleteOldContents) {
            DeleteContents(directory);
        }

        InputStream io = null;
        File file = null;

        try {
            io = context.getContentResolver().openInputStream(source);

            //Saves selected file with extension?!
            file = new File(directory, destination);

            OutputStream output = new FileOutputStream(file);

            //Writes contents from the origin to new born file
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = io != null ? io.read(buffer) : 0) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (io != null) {
                    io.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * This method will give file name for content scheme only
     *
     * @param context Present Context
     * @param uri     Can be achieved from intent.getData
     * @return Name of the file (String format)
     */
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();

        }
        return getFileNameFromString(result);
    }

    public static void DeleteContents(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                child.delete();
            }
        } else {
            fileOrDirectory.delete();
        }
        createApplicationFolder();
    }

    public static String getFileNameFromString(String result) {
        if (result != null) {
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    public static File createApplicationFolder() {

        File f = new File(Environment.getExternalStorageDirectory(), File.separator + AppConstants.APPLICATION_DIR_NAME);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + AppConstants.APPLICATION_DIR_NAME + AppConstants.IMAGES_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + AppConstants.APPLICATION_DIR_NAME + AppConstants.VIDEOS_DIR);
        f.mkdirs();
       /* f = new File(Environment.getExternalStorageDirectory(), File.separator + AppUrls.APPLICATION_DIR_NAME + AppUrls.TEXT_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + AppUrls.APPLICATION_DIR_NAME + AppUrls.ZIP_DIR);
        f.mkdirs();*/
        return f;
    }

    public static String getMimeTypeByContentResolver(Context context, Uri uri) {
        /*
         * Get the file's content URI from the incoming Intent, then
         * get the file's MIME type
         */
        return context.getContentResolver().getType(uri);
    }

    public static String getMimeTypeByCursor(Context context, Uri uri) {
        /*
         * Get the file's content URI from the incoming Intent,
         * then query the server app to get the file's display name
         * and size.
         */
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        String size = Long.toString(returnCursor.getLong(sizeIndex));
        String type = null;
        return type;
    }

    public static long getFileSize(Context context, Uri uri) {
        /*
         * Get the file's content URI from the incoming Intent,
         * then query the server app to get the file's display name
         * and size.
         */
        // FIXME: 21/12/17  sagar: Can throw protocol excepion for some explorer due to file:// instead of content://
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        long size = returnCursor.getLong(sizeIndex);
        return size;
    }

    public static boolean DeleteRecursive(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    DeleteRecursive(child);

            return fileOrDirectory.delete();
        } catch (Exception e) {

        }
        return false;
    }

    public static File getSpecificDirectory(String parentFolder, String endFolder) {
        File applicationFolder = createApplicationFolder();

        try {
            applicationFolder.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!applicationFolder.exists()) {
            applicationFolder.mkdirs();
        }

        File myDir = new File(Environment.getExternalStorageDirectory()
                + File.separator
                + AppConstants.APPLICATION_DIR_NAME
                + parentFolder
                + endFolder);

        try {
            myDir.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        return myDir;
    }

    // FIXME: 15/3/18  sagar: Replace literals
    public boolean isValidImageFileSize(String fileType, File file) {
        // FIXME: 22/12/17  sagar: This can be several "OR" conditions by image types
        if (fileType.contains("image")) {
            if (getFileSize(file) > 2) {
                return false;
            }
        }
        return getFileSize(file) <= 20;
    }

    public long getFileSize(File file) {
        long fileSizeInBytes = file.length();
        long fileSizeInKb = fileSizeInBytes / 1024;
        long fileSizeInMb = fileSizeInKb / 1024;
        return fileSizeInMb;
    }

    public static String readStringFromFile(Context c, int fileRawResId) {
        try {
            InputStream is = c.getResources().openRawResource(fileRawResId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Will be used by camera after taking the picture to store the image
     * with Unique Image name in pre-defined Image file (folder) here
     * so that it can be used later easily by file provider.
     */
    public static File getImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";

        // Not available for other apps
        //  File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Publicly available for all apps
        File storageDir = FileUtils.getAppImageDir();

        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".png",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null && !image.exists()) {
            image.mkdirs();
        }
        // Save a file: path for use with ACTION_VIEW intents
        /*imagePath = "file:" + image.getAbsolutePath();*/

        if (image != null) {
            imagePath = image.getAbsolutePath();
        }

        return image;
    }

    /**
     * Will be used by camera after taking the picture to store the image
     * with Unique Image name in pre-defined Image file (folder) here
     * so that it can be used later easily by file provider.
     */
    public static File getVideoFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String videoFileName = "PashuPal_" + timeStamp + "_";

        // Not available for other apps
        //  File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //Publicly available for all apps
        File storageDir = FileUtils.getAppVideoDir();

        File video = null;
        try {
            video = File.createTempFile(
                    videoFileName,  /* prefix */
                    ".mp4",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (video != null && !video.exists()) {
            video.mkdirs();
        }
        // Save a file: path for use with ACTION_VIEW intents
        /*videoPath = "file:" + video.getAbsolutePath();*/
        return video;
    }

}
