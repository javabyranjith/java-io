package jbr.javaio;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * A simple example to read the bz2 file.
 */
public class ReadBz2File {

  public String getBz2FileContent(String bz2File) {
    StringBuffer result = new StringBuffer();
    CompressorInputStream compressorInputStream = null;

    try {
      BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(bz2File));
      compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(bufferedInputStream);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compressorInputStream));

      String line;
      while ((line = bufferedReader.readLine()) != null) {
        result.append(line + "\n");
      }
    } catch (CompressorException | IOException e) {
      e.printStackTrace();
    }

    return result.toString();
  }

}
