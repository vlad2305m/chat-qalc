package com.vlad2305m.chatqalc;

import java.io.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import static com.vlad2305m.chatqalc.ChatQalc.LOGGER;

public class MathEngineInstaller {
    public static void install(){
        try {
            File confDir = new File("./config/chatqalc/");
            if (!confDir.exists()) {
                try (InputStream zip = (MathEngine.class.getResourceAsStream("/qalcplatforms/" + PlatformSpecificStuff.zipName()))) {
                    dispatchZip(zip, confDir);}
                PlatformSpecificStuff.linuxPerms();
            }
        } catch (IOException e) {LOGGER.error(e.toString());}
    }


    // From Baeldung
    public static void dispatchZip(InputStream zip, File dest) throws IOException {
        if (zip == null) throw new IOException("Installer not found");
        ZipInputStream zis = new ZipInputStream(zip);
        byte[] buffer = new byte[1024];
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(dest, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
