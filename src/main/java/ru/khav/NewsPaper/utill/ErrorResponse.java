package ru.khav.NewsPaper.utill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private List<String> messages;
    private long timestamp;

    public ErrorResponse(List<String> lst, long time)
    {
        this.messages=lst;
        this.timestamp=time;
    }
    public ErrorResponse(String lst, long time)
    {
        this.message=lst;
        this.timestamp=time;
    }
}
