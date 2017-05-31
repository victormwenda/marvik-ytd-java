package com.marvik.ytd.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Downloader
 * Provides an API to download files and save the downloaded file to the file system
 */
public final class Downloader {


    private Downloader() {
    }

    /**
     * Returns an instance of this class allowing singleton access
     *
     * @return
     */
    public static Downloader getInstance() {
        return new Downloader();
    }

    /**
     * Download file and save on the file path
     *
     * @param fileUri       the url of the file
     * @param filePath      the storage path of the file
     */
    public void downloadFile(final String fileUri, final String filePath) {

        new Thread(() -> {
            try {
                URL url = new URL(parseUrl(fileUri));
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                String filename = new File(fileUri).getName();

                int count = 0;
                byte[] buffer = new byte[1024];

                File fileDir = new File(filePath).getParentFile();

                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                File downloadFile = new File(fileDir + File.separator + filename);

                //Ensure that we do not always download existing files
                if (downloadFile.exists()) {
                    return;
                }

                FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);

                while ((count = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }

                fileOutputStream.close();


            } catch (MalformedURLException e) {

                e.printStackTrace();


            } catch (IOException e) {

                e.printStackTrace();


            }
        }).start();

    }

    /**
     * Parse fileUri
     * Replaces bad format characters like an empty space from a file uri
     *
     * @param fileUri
     * @return
     */
    private String parseUrl(String fileUri) {

        fileUri = fileUri.replace(" ", "%20");

        if (fileUri.contains(" ")) {
            return parseUrl(fileUri);
        }
        return fileUri;
    }
}