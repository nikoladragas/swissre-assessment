package org.swissre.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {
    private static final String DELIMITER = ",";
    private final BufferedReader bufferedReader;

    /**
     * Creates new CsvReader for specified file in resources directory
     * @param fileName - name of the file to read
     * @throws NullPointerException - if name is null
     * @throws FileNotFoundException – if the file does not exist, is a directory rather than a regular file,
     * or for some other reason cannot be opened for reading
     */
    public CsvReader(String fileName) throws FileNotFoundException, NullPointerException {
        this.bufferedReader = new BufferedReader(new FileReader(getClass().getClassLoader().getResource(fileName).getFile()));
    }

    public String[] readLine() throws IOException {
        String line = bufferedReader.readLine();
        return line != null ? line.split(DELIMITER) : null;
    }
}
