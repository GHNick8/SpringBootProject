package com.adminnick.SpringBootProject.Util;

import java.io.File;
import java.nio.file.Paths;

public class AppUtil {
    public static String get_upload_path(String fileName) {
        return Paths.get(new File("uploads").getAbsolutePath(), fileName).toString();
    }
}
