package autodex.com.autodex.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 27/9/17.
 */

public class SectionDataModel implements Serializable {

    private String headerTitle;
    private List<GetContactListResponse> allItemsInSection;
    private List<GetContactListResponse> temp;


    public SectionDataModel() {

    }

    public SectionDataModel(String headerTitle, List<GetContactListResponse> allItemsInSection, List<GetContactListResponse> temp) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
        this.temp = temp;
    }

    public List<GetContactListResponse> getTemp() {
        return temp;
    }

    public void setTemp(List<GetContactListResponse> temp) {
        this.temp = temp;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<GetContactListResponse> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(List<GetContactListResponse> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}