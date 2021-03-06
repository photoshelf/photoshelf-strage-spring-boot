package org.photoshelf.storage;

import org.photoshelf.storage.internal.InvalidMimeTypeException;
import org.photoshelf.storage.exception.InvalidImageException;
import org.photoshelf.storage.internal.MimeType;

import java.io.*;
import java.util.Optional;

public class Photo {

	private Identifier id;
	private MimeType mimeType;
	private byte[] image;

	public Photo(InputStream image) throws InvalidImageException {
		initialize(image);
	}

	static Photo of(Identifier id, InputStream image) throws InvalidImageException {
		Photo instance = new Photo(image);
		instance.id = id;
		return instance;
	}

	Optional<Identifier> identifier() {
		return Optional.ofNullable(this.id);
	}

	public byte[] getImage() {
		return image;
	}

	public MimeType mimeType() {
		return mimeType;
	}

	public boolean isNew() {
		return this.id == null;
	}

	private void initialize(InputStream inputStream) throws InvalidImageException {
		try {
			this.image = inputStream.readAllBytes();
		} catch (IOException e) {
			throw new InvalidImageException("cannot read data", e);
		}

		try {
			MimeType mimeType = MimeType.guessFromStream(new ByteArrayInputStream(this.image));
			if (!mimeType.isImage()) {
				throw new InvalidImageException("is not a image");
			}
			this.mimeType = mimeType;
		} catch (IOException | InvalidMimeTypeException e) {
			throw new InvalidImageException("cannot get mimetype", e);
		}
	}
}
