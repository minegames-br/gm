package com.thecraftcloud.core.admin.domain;

public class ResponseDTO {
	
	public static final String ADM_SUCCESS = "ADM-000-001";
	public static final String ADM_FAILURE = "ADM-999-001";
	public static final String ADM_UNABLE_TO_COMPLETE_ACTION = "ADM-999-002";

	private Boolean result;
	private String code;
	private String message;
	private ResponseType type;
	private String json;
	
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
		dto.setType(ResponseType.TEXT);
		dto.setCode(ADM_SUCCESS);
		dto.setResult(false);
		return dto;
	}
	public static ResponseDTO actionNotAllowed() {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setMessage("Action not allowed.");
		responseDTO.setCode(ADM_SUCCESS);
		responseDTO.setType(ResponseType.TEXT);
		responseDTO.setResult(false);
		return responseDTO;
	}
	public ResponseType getType() {
		return type;
	}
	public void setType(ResponseType type) {
		this.type = type;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static ResponseDTO incompleteRequest(String message ) {
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage( message );
		dto.setType(ResponseType.TEXT);
		dto.setCode(ADM_SUCCESS);
		dto.setResult(false);
		return dto;
	}
	public static ResponseDTO unableToCompleteAction(String message) {
		ResponseDTO dto = new ResponseDTO();
		dto.setMessage( message );
		dto.setType(ResponseType.TEXT);
		dto.setCode(ADM_UNABLE_TO_COMPLETE_ACTION);
		dto.setResult(false);
		return dto;
	}
	
}
