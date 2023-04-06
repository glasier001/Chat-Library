package com.commonlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by root on 4/7/16.
 */
public final class DeleteUtils {

    public DeleteUtils(File fileOrDirectory) {
        DeleteRecursive(fileOrDirectory);

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

    public static boolean DeleteRecursive(File fileOrDirectory, Context applicationContext) {
        try {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles()) {
                    DeleteRecursive(child, applicationContext);
                    deleteFileFromMediaStore(applicationContext.getContentResolver(), child);
                }
            return fileOrDirectory.delete();
        } catch (Exception e) {

        }

        return false;
    }


    public static void deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();

            final Uri uri = MediaStore.Files.getContentUri("external");
            final int result = contentResolver.delete(uri,
                    MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
            if (result == 0) {
                final String absolutePath = file.getAbsolutePath();
                if (!absolutePath.equals(canonicalPath)) {
                    contentResolver.delete(uri,
                            MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                }
            }
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
    }
}
