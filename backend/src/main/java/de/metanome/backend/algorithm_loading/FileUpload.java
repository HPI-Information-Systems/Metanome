package de.metanome.backend.algorithm_loading;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Uploads file to directory passed as argument
 */
public class FileUpload {

    /**
     * Function to upload file to directory passed as argument
     *
     * @param uploadedInputStream InputStream of file send
     * @param fileDetail additional MetaData about uploaded file
     * @param targetDirectory directory were file should be stored
     * @return if file already existed
     **/
    public boolean writeFileToDisk(InputStream uploadedInputStream,
                                FormDataContentDisposition fileDetail,
                                String targetDirectory) throws IOException  {

            int read = 0;
            byte[] bytes = new byte[1024];
            String filepath = targetDirectory + fileDetail.getFileName();


            /*If file already exists delete for cases like algorithm gets
            reuploaded because of changes to the algorithm or inputfile
             */

            boolean file_exist = Files.deleteIfExists(Paths.get(filepath));
            File f = new File(filepath);

            OutputStream out = new FileOutputStream(new File(filepath));


            while ((read = uploadedInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            /* Returns information if file already existed to prevent adding algorithm / inputfile again              to databse which is not possible due to key constraints */
            return file_exist;


    }
}
