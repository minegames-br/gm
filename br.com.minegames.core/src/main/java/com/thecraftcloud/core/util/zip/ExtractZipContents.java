package com.thecraftcloud.core.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractZipContents {

	public static void unzip( File file ) {

		try {
			ZipFile zipFile = new ZipFile(file);
			File dir = file.getParentFile();
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				System.out.println(name);
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				//System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", 
						//name, size, compressedSize);

				File _file = new File(dir + "/" + name);
				if (name.endsWith("/")) {
					_file.mkdirs();
					continue;
				}

				File _parent = file.getParentFile();
				if (_parent != null) {
					_parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(_file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
