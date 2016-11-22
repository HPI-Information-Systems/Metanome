package de.metanome.backend.algorithm_loading;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Uploads file to direcotry passed as
 */
public class FileUpload {

    /**
     * Function to upload file to passed directory
     *
     * @param uploadedInputStream InputStream of file send
     * @param fileDetail additional MetaData about uploaded file
     * @param targetDirectory directory were file should be stored
     */
    public void writeFileToDisk(InputStream uploadedInputStream,
                                FormDataContentDisposition fileDetail,
                                String targetDirectory) throws IOException  {

            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = new FileOutputStream(new File(targetDirectory+ fileDetail.getFileName()));
            while ((read = uploadedInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();


    }
}
