package de.metanome.backend.input.file;

import de.metanome.backend.testserver_config.TestServerSetup;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;


public class CsvFileUploadTest extends TestServerSetup {


    @Test
    public void CSVFileUpload() {
        /*Select file to Upload*/
        String csvFilePath =
                Thread.currentThread().getContextClassLoader()
                        .getResource("inputData/inputB.csv").getFile();

        File uploadFile = new File(csvFilePath);

        Assert.assertTrue("File to be uploaded doesnt exist",uploadFile.exists());
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FileDataBodyPart("file",uploadFile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE));
        Response response = target("file-inputs/store").
                request().post(Entity.entity(form,form.getMediaType()));
        System.out.print(response.toString());
        assertEquals("CSV Upload fehlgeschlagen!",204,response.getStatus());

    }
}
