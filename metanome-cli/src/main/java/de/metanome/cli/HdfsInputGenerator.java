package de.metanome.cli;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.input.file.FileIterator;
import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This {@link RelationalInputGenerator} allows to read CSV files from HDFS in Metanome algorithms.
 */
public class HdfsInputGenerator implements RelationalInputGenerator {

    final ConfigurationSettingFileInput settings;

    public HdfsInputGenerator(ConfigurationSettingFileInput settings) {
        this.settings = settings;
    }

    @Override
    public RelationalInput generateNewCopy() throws InputGenerationException, AlgorithmConfigurationException {
        // Validate the input.
        String hfdsUrl = this.settings.getFileName();
        Validate.notNull(hfdsUrl, "No HDFS file given.");
        Validate.isTrue(hfdsUrl.startsWith("hdfs:"), "Not an HDFS file: %s", hfdsUrl);

        // Get a name for the relation.
        String relationName = getFileName(hfdsUrl);

        try {
            // Create a HDFS reader.
            Configuration conf = new Configuration();
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            FileSystem fs = FileSystem.get(new URI(hfdsUrl), conf);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fs.open(new Path(hfdsUrl))));

            // Create the input.
            return new FileIterator(relationName, reader, this.settings);
        } catch (IOException | URISyntaxException | InputIterationException e) {
            throw new InputGenerationException("Could not access HDFS.", e);
        }
    }

    public String getUrl() {
        return this.settings.getFileName();
    }

    public ConfigurationSettingFileInput getSettings() {
        return this.settings;
    }

    /**
     * Retrieve the file name of a URL (without the path).
     *
     * @param url a URL
     * @return the file name
     */
    public static String getFileName(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        return url.substring(lastSlashIndex + 1);
    }

    @Override
    public void close() throws Exception {
        // Pass.
    }
}
