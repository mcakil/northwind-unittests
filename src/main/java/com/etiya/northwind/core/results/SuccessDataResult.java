package com.etiya.northwind.core.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data

public class SuccessDataResult<T> extends DataResult<T> {

    public SuccessDataResult(T data, String message) {
        super(data, true ,message);


    }

    public SuccessDataResult(T data) {
        super(data,true);
    }

    public SuccessDataResult(String message) {
        super(null, true ,message);
    }

    public SuccessDataResult() {
        super(null, true);
    }

 @Override
    public String toString(){
        return  super.toString();
 }
 @Override
    public boolean equals(Object obj){
        return super.equals(obj);
 }

}
