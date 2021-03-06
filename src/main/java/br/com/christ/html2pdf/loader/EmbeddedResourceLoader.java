package br.com.christ.html2pdf.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EmbeddedResourceLoader implements ResourceLoader {
	public String getStringFromReference(String reference) throws IOException {
		return new String(getBytesFromReference(reference));
	}

	public byte[] getBytesFromReference(String reference) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(reference);
		copyStream(inputStream, outputStream);
		return outputStream.toByteArray();
	}

	public byte[] getBytesFromResource(String reference) throws IOException {
		return getBytesFromReference(reference);
	}

	private void copyStream(InputStream input, OutputStream output) throws IOException {
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = input.read(data, 0, data.length)) != -1) {
			output.write(data, 0, nRead);
		}
		output.flush();
	}
}
