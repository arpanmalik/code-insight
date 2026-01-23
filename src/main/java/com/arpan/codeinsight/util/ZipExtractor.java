package com.arpan.codeinsight.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ZipExtractor {

    public void extract(Path zipPath, Path extractDir) throws IOException {

        Files.createDirectories(extractDir);

        try (ZipInputStream zis =
                     new ZipInputStream(Files.newInputStream(zipPath))) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                Path newPath = extractDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
