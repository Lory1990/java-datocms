package io.github.lory1990.datocms.datocms.dto;

import java.util.Map;

public class GetDataRequestDTO {

    private String operationName;
    private String query;
    private Map<String, String> variables;

    public GetDataRequestDTO(String operationName, String query, Map<String, String> variables) {
        this.operationName = operationName;
        this.query = query;
        this.variables = variables;
    }

    public GetDataRequestDTO(){}

    public String getOperationName() {
        return operationName;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

}
