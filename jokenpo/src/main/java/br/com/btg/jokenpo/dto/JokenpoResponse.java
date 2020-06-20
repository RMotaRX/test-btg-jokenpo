package br.com.btg.jokenpo.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JokenpoResponse {

    @NotNull
    @JsonProperty(value = "result")
    private String gameResult;

    @NotNull
    private List<String> history;
}
