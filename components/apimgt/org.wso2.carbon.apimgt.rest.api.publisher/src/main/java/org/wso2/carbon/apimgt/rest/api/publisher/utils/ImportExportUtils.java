/***********************************************************************************************************************
 *
 *  *
 *  *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  *   WSO2 Inc. licenses this file to you under the Apache License,
 *  *   Version 2.0 (the "License"); you may not use this file except
 *  *   in compliance with the License.
 *  *   You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing,
 *  *  software distributed under the License is distributed on an
 *  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  *  KIND, either express or implied.  See the License for the
 *  *  specific language governing permissions and limitations
 *  *  under the License.
 *  *
 *
 */

package org.wso2.carbon.apimgt.rest.api.publisher.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.rest.api.common.util.RestApiUtil;

import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ImportExportUtils {

    private static final Logger log = LoggerFactory.getLogger(ImportExportUtils.class);

    static void deleteDirectory(String path) throws APIManagementException{
        if (new File(path).isDirectory()) {
            try {
                FileUtils.deleteDirectory(new File(path));
            } catch (IOException e) {
                throw new APIManagementException("Error while deleting directory at" + path, e);
            }
        }
    }

    static void createDirectory(String path) throws APIManagementException {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new APIManagementException("Error in creating directory at: " + path, e);
        }
    }

    static void createArchiveFromUploadedData (InputStream inputStream, String archivePath)
            throws APIManagementException {

        FileOutputStream outFileStream = null;
        try {
            outFileStream = new FileOutputStream(new File(archivePath));
            IOUtils.copy(inputStream, outFileStream);
        } catch (IOException e) {
            throw new APIManagementException("Error in Creating archive from uploaded data", e);
        } finally {
            IOUtils.closeQuietly(outFileStream);
        }
    }

    /**
     * This method decompresses API the archive
     *
     * @param archiveFilePath  Path to archive containing the API
     * @param destination location of the archive to be extracted
     * @return Name of the extracted directory
     * @throws APIManagementException If the decompressing fails
     */
    static String extractArchive(String archiveFilePath, String destination) throws
            APIManagementException {

        BufferedInputStream inputStream = null;
        InputStream zipInputStream = null;
        FileOutputStream outputStream = null;
        ZipFile zip = null;
        String archiveName = null;

        try {
            zip = new ZipFile(new File(archiveFilePath));
            Enumeration zipFileEntries = zip.entries();
            int index = 0;

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {

                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();

                //This index variable is used to get the extracted folder name; that is root directory
                if (index == 0) {
                    archiveName = currentEntry.substring(0, currentEntry.indexOf('/'));
                    --index;
                }

                File destinationFile = new File(destination, currentEntry);
                File destinationParent = destinationFile.getParentFile();

                // create the parent directory structure
                if (destinationParent.mkdirs()) {
                    log.info("Creation of folder is successful. Directory Name : " + destinationParent.getName());
                }

                if (!entry.isDirectory()) {
                    zipInputStream = zip.getInputStream(entry);
                    inputStream = new BufferedInputStream(zipInputStream);

                    // write the current file to the destination
                    outputStream = new FileOutputStream(destinationFile);
                    IOUtils.copy(inputStream, outputStream);
                }
            }
            return archiveName;
        } catch (IOException e) {
            throw new APIManagementException("Failed to extract archive file", e);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(zip);
        }
    }

    static void createFile (String location) throws APIManagementException {
        try {
            Files.createFile(Paths.get(location));
        } catch (IOException e) {
            throw new APIManagementException("Error in creating file at: " + location, e);
        }
    }

    static void writeToFile(String path, String content) throws APIManagementException {
        OutputStreamWriter writer = null;
        FileOutputStream fileOutStream = null;
        StringReader stringReader = null;

        try {
            fileOutStream = new FileOutputStream(path);
            stringReader = new StringReader(content);
            writer = new OutputStreamWriter(fileOutStream, Charset.forName( "UTF-8" ));
            IOUtils.copy(stringReader, writer);
        } catch (IOException e) {
            throw new APIManagementException("I/O error while writing to file at: " + path, e);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(fileOutStream);
            IOUtils.closeQuietly(stringReader);
        }

    }

    static String readFileContentAsText(String path) throws APIManagementException {

        try {
            return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        } catch (IOException e) {
            throw new APIManagementException("Error while reading file " + path, e);
        }
    }

    static InputStream readFileContentAsStream(String path) throws
            APIManagementException {

        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            throw new APIManagementException("Error while reading file " + path, e);
        }
    }

    static void writeStreamToFile(String path, InputStream inputStream) throws
            APIManagementException {

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            IOUtils.copy(inputStream, outputStream);

        } catch (FileNotFoundException e) {
            throw new APIManagementException(e);

        } catch (IOException e) {
            throw new APIManagementException("Unable to write to file at path: " + path, e);

        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * Archive a provided source directory to a zipped file
     *
     * @param sourceDirectory Source directory
     */
    static void archiveDirectory(String sourceDirectory, String archiveLocation, String archiveName)
            throws APIManagementException {

        File directoryToZip = new File(sourceDirectory);

        List<File> fileList = new ArrayList<>();
        getAllFiles(directoryToZip, fileList);
        try {
            writeArchiveFile(directoryToZip, fileList, archiveLocation, archiveName);
        } catch (IOException e) {
            throw new APIManagementException("Unable to create the archive", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Archived API generated successfully");
        }

    }

    /**
     * Retrieve all the files included in the source directory to be archived
     *
     * @param sourceDirectory Source directory
     * @param fileList        List of files
     */
    private static void getAllFiles(File sourceDirectory, List<File> fileList) {
        File[] files = sourceDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory()) {
                    getAllFiles(file, fileList);
                }
            }
        }
    }

    /**
     * Generate archive file
     *
     * @param directoryToZip Location of the archive
     * @param fileList       List of files to be included in the archive
     */
    private static void writeArchiveFile(File directoryToZip, List<File> fileList, String
            archiveLocation, String archiveName)
            throws IOException {

        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(archiveLocation + File.separator + archiveName + ".zip");
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            for (File file : fileList) {
                if (!file.isDirectory()) {
                    addToArchive(directoryToZip, file, zipOutputStream);
                }
            }

        } finally {
            IOUtils.closeQuietly(zipOutputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    /**
     * Add files of the directory to the archive
     *
     * @param directoryToZip  Location of the archive
     * @param file            File to be included in the archive
     * @param zipOutputStream Output stream
     */
    private static void addToArchive(File directoryToZip, File file, ZipOutputStream zipOutputStream) throws IOException {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);

            // Get relative path from archive directory to the specific file
            String zipFilePath = file.getCanonicalPath()
                    .substring(directoryToZip.getCanonicalPath().length() + 1, file.getCanonicalPath().length());
            if (File.separatorChar != '/')
                zipFilePath = zipFilePath.replace(File.separatorChar, '/');
            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zipOutputStream.putNextEntry(zipEntry);

            IOUtils.copy(fileInputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public static Response handleError (Logger logger, String errorMessage, APIManagementException exception) {
        logger.error(errorMessage, exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(RestApiUtil.buildInternalServerErrorException
                (errorMessage)).build();
    }

    public static Response handleError (Logger logger, String errorMessage) {
        logger.error(errorMessage);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(RestApiUtil.buildInternalServerErrorException
                (errorMessage)).build();
    }
}
