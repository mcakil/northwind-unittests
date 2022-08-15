package com.etiya.northwind.core.helpers;

import com.etiya.northwind.business.requests.PageSortRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableSorter {
    public static Pageable getSortedPageable(PageSortRequest sortRequest) {
        Pageable pageable;
        if (sortRequest.isAscending()) {
            pageable = PageRequest.of(sortRequest.getNumber() - 1, sortRequest.getItemAmount(), Sort.by(sortRequest.getFieldName()).ascending());
        } else {
            pageable = PageRequest.of(sortRequest.getNumber() - 1, sortRequest.getItemAmount(), Sort.by(sortRequest.getFieldName()).descending());
        }
        return pageable;
    }
}
