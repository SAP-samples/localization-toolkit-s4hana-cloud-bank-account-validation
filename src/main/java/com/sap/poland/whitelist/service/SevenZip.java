package com.sap.poland.whitelist.service;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

public class SevenZip {
    static byte[] extract(byte[] zippedData, String entryName) throws Exception {
        SeekableInMemoryByteChannel inMemoryByteChannel = new SeekableInMemoryByteChannel(zippedData);
        SevenZFile sevenZFile = new SevenZFile(inMemoryByteChannel);
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();
        while (entry != null) {
            if (entry.getName().equalsIgnoreCase(entryName)) {
            byte[] content = new byte[(int) entry.getSize()];
                sevenZFile.read(content, 0, content.length);
            return content;
            }
            entry = sevenZFile.getNextEntry();
        }
        return null;
    }
}
