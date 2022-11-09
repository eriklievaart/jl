package com.eriklievaart.jl.core.control.param;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.eriklievaart.jl.core.api.MultiPartParameter;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.IdGenerator;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class FileMultipartSupplier implements MultiPartParameter, Closeable {
	private static final IdGenerator ID = new IdGenerator();
	private static final File ROOT = new File("/tmp/upload");
	static {
		ROOT.mkdirs();
	}

	private LogTemplate log = new LogTemplate(getClass());

	private String name;
	private File file;

	public FileMultipartSupplier(String name, byte[] bytes, InputStream is) throws IOException {
		Check.notNull(name, bytes, is);

		this.name = name;
		this.file = createTempFile(bytes, is);
		log.trace("temp file of $ bytes created for upload %", file.length(), name);
	}

	private File createTempFile(byte[] bytes, InputStream is) throws IOException {
		File temp = new File(ROOT, ID.next() + "-" + System.currentTimeMillis());
		log.trace("temp file $", temp.getPath());

		try (FileOutputStream fos = new FileOutputStream(temp)) {
			fos.write(bytes);
			StreamTool.copyStream(is, fos);

		} catch (IOException ioe) {
			close();
			throw ioe;
		}
		return temp;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getSize() {
		return file.length();
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	@Override
	public void close() {
		log.trace("deleting temp file %", file);
		file.delete();
	}
}