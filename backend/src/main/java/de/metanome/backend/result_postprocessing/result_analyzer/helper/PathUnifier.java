package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Provides functionality to use system dependent paths and to combine them
 *
 * Created by Alexander Spivak on 17.02.2015.
 */
public class PathUnifier {

  /**
   * Replaces the "/" with the platform dependent separator
   * @param path Path which should become platform dependent
   * @return Returns the platform dependent path
   */
  public static String unifyPath(String path){
    return path.replaceAll("/", Matcher.quoteReplacement(File.separator));
  }

  /**
   * Combines two paths in a platform independent way
   * @param dirPath Path of the directory
   * @param filePath Path of the file inside the directory
   * @return Returns the path of file in the given directory
   */
  public static String combinePaths(String dirPath, String filePath){
    // Unify the directory path
    String correctDirPath = PathUnifier.unifyPath(dirPath);
    // Remove the separator at the path ending
    if(correctDirPath.endsWith(File.separator)){
      correctDirPath = correctDirPath.substring(0, correctDirPath.length()-File.separator.length());
    }

    // Unify the file path
    String correctFilePath = PathUnifier.unifyPath(filePath);
    // Remove separator at the path beginning
    if(correctFilePath.startsWith(File.separator)){
      correctFilePath = correctFilePath.substring(File.separator.length());
    }

    // Return the combination
    return correctDirPath + File.separator + correctFilePath;
  }
}
