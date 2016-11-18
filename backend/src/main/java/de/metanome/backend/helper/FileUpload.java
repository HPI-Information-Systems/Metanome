package de.metanome.backend.helper;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Vincent on 18.11.16.
 */
public class FileUpload {
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
