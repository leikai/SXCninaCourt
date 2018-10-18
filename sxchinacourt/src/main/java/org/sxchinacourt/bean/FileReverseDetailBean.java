package org.sxchinacourt.bean;

import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class FileReverseDetailBean {
    private int TotalCount;

    private int Page;

    private int PageSize;

    private List<FileReverseDetailDataBean> data ;

    public void setTotalCount(int TotalCount){
        this.TotalCount = TotalCount;
    }
    public int getTotalCount(){
        return this.TotalCount;
    }
    public void setPage(int Page){
        this.Page = Page;
    }
    public int getPage(){
        return this.Page;
    }
    public void setPageSize(int PageSize){
        this.PageSize = PageSize;
    }
    public int getPageSize(){
        return this.PageSize;
    }
    public void setData(List<FileReverseDetailDataBean> data){
        this.data = data;
    }
    public List<FileReverseDetailDataBean> getData(){
        return this.data;
    }
}
