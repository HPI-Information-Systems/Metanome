package de.metanome.cli;

import de.metanome.algorithm_integration.algorithm_execution.FileCreationException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TempFileGenerator implements FileGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(TempFileGenerator.class);

  private final String prefix;
  private final String suffix;
  private final File directory;
  private final List<File> created;
  private final boolean clearTempFiles;
  private final boolean clearTempFilesByPrefix;

  TempFileGenerator(final String prefix, final String directory, final boolean clearTempFiles,
      final boolean clearTempFilesByPrefix) {

    this.prefix = prefix;
    this.suffix = "tmp";
    this.created = new ArrayList<>();
    this.directory = new File(directory == null ? System.getProperty("java.io.tmpdir") : directory);
    this.directory.mkdirs();
    this.clearTempFiles = clearTempFiles;
    this.clearTempFilesByPrefix = clearTempFilesByPrefix;
  }

  @Override
  public File getTemporaryFile() throws FileCreationException {
    try {
      final File file = File.createTempFile(prefix, suffix, directory);
      created.add(file);
      return file;
    } catch (final IOException e) {
      throw new FileCreationException("error", e);
    }
  }

  @Override
  public void close() {
    // nop
  }

  void cleanUp() {
    if (clearTempFiles || clearTempFilesByPrefix) {
      try {
        removeFiles();
      } catch (final IOException e) {
        LOG.error("cannot remove temp files", e);
      }
    }
  }

  private void removeFiles() throws IOException {
    Files.walkFileTree(directory.toPath(), Collections.emptySet(), 1,
        new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {

            if (clearTempFiles && created.contains(file.toFile())) {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
            }

            if (clearTempFilesByPrefix && file.toFile().getName().startsWith(prefix)) {
              Files.delete(file);
            }

            return FileVisitResult.CONTINUE;
          }

        });

    created.clear();
  }
}
