package io.github.lory1990.datocms.dto;

public class GetDataResponseDTO<A> {

    private A data;

    public GetDataResponseDTO(A data) {
        this.data = data;
    }

    public GetDataResponseDTO(){}

    public A getData() {
        return data;
    }

    public void setData(A data) {
        this.data = data;
    }
}
