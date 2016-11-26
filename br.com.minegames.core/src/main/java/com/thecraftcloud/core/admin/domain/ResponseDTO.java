package com.thecraftcloud.core.admin.domain;

public class ResponseDTO {

	private Boolean result;
	private String message;
	
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static ResponseDTO invalidAction() {
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage("Invalid Action");
		dto.setResult(false);
		return dto;
	}
	public static ResponseDTO actionNotAllowed() {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("Action not allowed.");
		responseDTO.setResult(false);
		return responseDTO;
	}
	
}
