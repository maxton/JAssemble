/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Max
 */
public class Util {
  static String readFile(String path, Charset encoding) 
    throws IOException 
  {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }
  
  static void saveFile(String path, String text)
          throws IOException {
    byte[] encoded = text.getBytes(StandardCharsets.UTF_8);
    Files.write(Paths.get(path), encoded);
  }
}
