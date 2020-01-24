package aws.s3;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Arquivo {
	
	@NotNull
	private Long id;
	
	@NotNull
	private byte[] file;
	
	@NotBlank
	private String contentType;
	
	public Arquivo() {
		// Construtor vazio para serializacao
	}
	
	public Arquivo(String id, String contentType, byte[] file) {
		this.id = Long.valueOf(id);
		this.contentType = contentType;
		this.file = file;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
}
