package com.cunyun.smsforeign.dal.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * <p>文件名称：Page.java</p> <p>文件描述：</p> <p>版权所有： 版权所有(C)2016-2099</p> <p>公   司： 优行科技 </p> <p>内容摘要：
 * </p> <p>其他说明： </p> <p>完成日期：2017年01月17日</p>
 *
 * @author liuke1@geely.com
 * @version 1.0
 */
public class Page<T> {

    private List<T> rows;

    private int total;

    @JsonIgnore
    private int limit = 10;

    @JsonIgnore
    private int offset =0;

    public Page(int limit, int offset) {
        if(offset > 1){
            offset = (offset-1)*10;
        }else if(offset==1){
            offset = 0;
        }
        this.limit = limit;
        this.offset = offset;
    }

    public List<T> getRows() {
        return rows;
    }

    public Page setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public Page setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public Page setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public Page setOffset(int offset) {
        this.offset = offset;
        return this;
    }

//    public void pageDeal(Integer offset,Integer limit){
//        if(offset > 1){
//            offset = (offset-1)*10;
//            limit = offset+10;
//        }else if(offset==1){
//            offset = 0;
//        }
//    }
}

