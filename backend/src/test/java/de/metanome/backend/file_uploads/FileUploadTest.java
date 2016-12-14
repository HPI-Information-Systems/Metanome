/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.metanome.backend.file_uploads;


import de.metanome.backend.testserver_config.TestServerSetup;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Assert;
import org.junit.Ignore;

import java.io.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class FileUploadTest extends TestServerSetup {


    //Test commented out due to strange errors when mvn is used to run the test
    @Ignore
    public void JARFileUpload() {
        /*Select file to Upload*/
        String jarFilePath =
                Thread.currentThread().getContextClassLoader()
                        .getResource("algorithms/example_basic_stat_algorithm.jar").getFile();

        File uploadFile = new File(jarFilePath);

        Assert.assertTrue("File to be uploaded doesnt exist",uploadFile.exists());
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FileDataBodyPart("file",uploadFile,
                                MediaType.APPLICATION_OCTET_STREAM_TYPE));
        MediaType form_Mediatype = form.getMediaType();
        Response response = target("algorithms/store").
                    request().post(Entity.entity(form,form.getMediaType()));
        System.out.print(response.toString());
        assertEquals("Algorithmen Upload fehlgeschlagen!",204,response.getStatus());

    }

    //Test commented out due to strange errors when mvn is used to run the test
    @Ignore
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
