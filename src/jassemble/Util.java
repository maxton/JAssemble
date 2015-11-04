/*
 * Copyright (C) 2015 Maxton Connor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jassemble;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Various useful utility functions.
 * @author Max
 */
public class Util {
  /**
   * Load a text file into a String.
   * @param path Path to the file.
   * @param encoding Encoding used by the file.
   * @return The entire content of the text file.
   * @throws IOException 
   */
  static String readFile(String path, Charset encoding) throws IOException  {
    byte[] asBytes = Files.readAllBytes(Paths.get(path));
    return new String(asBytes, encoding);
  }
  
  /**
   * Writes a text file (as UTF-8). 
   * @param path Path to the file.
   * @param text Entire text content of the file
   * @throws IOException 
   */
  static void saveFile(String path, String text) throws IOException {
    byte[] encoded = text.getBytes(StandardCharsets.UTF_8);
    Files.write(Paths.get(path), encoded);
  }
}
