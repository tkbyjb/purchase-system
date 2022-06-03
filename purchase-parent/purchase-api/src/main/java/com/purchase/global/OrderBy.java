package com.purchase.global;

import lombok.Data;

@Data
public class OrderBy {
    private String field;   //排序字段
    private String way = "desc";  //排序方式asc升序desc降序
}
