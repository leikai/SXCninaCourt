package org.sxchinacourt.bean;

import java.util.List;

/**
 *
 * @author 殇冰无恨
 * @date 2017/10/11
 */

public class DepositRootBean {
    private int TotalCount;

    private int Page;

    private int PageSize;

    private List<DepositDataBean> data ;

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
    public void setData(List<DepositDataBean> data){
        this.data = data;
    }
    public List<DepositDataBean> getData(){
        return this.data;
    }
}
