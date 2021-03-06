package org.ow2.sirocco.cimi.tools;

import java.util.Arrays;
import java.util.List;

import org.ow2.sirocco.cimi.sdk.QueryParams;

import com.beust.jcommander.Parameter;

public class ResourceSelectParam {
    @Parameter(names = "-select", description = "comma-separated attributes to return")
    protected String select;

    private List<String> selectedAttributes;

    private boolean selectAll;

    public ResourceSelectParam(final String... defaultSelectValue) {
        if (defaultSelectValue.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (String attr : defaultSelectValue) {
                sb.append(attr + ",");
            }
            sb.setLength(sb.length() - 1);
            this.select = sb.toString();
        }
    }

    public String getSelect() {
        return this.select;
    }

    private List<String> getSelectEntries() {
        if (this.selectedAttributes == null && this.select != null) {
            String[] attributes = this.select.split(",");
            this.selectedAttributes = Arrays.asList(attributes);
            this.selectAll = this.selectedAttributes.contains("*");
        }
        return this.selectedAttributes;
    }

    public boolean isSelected(final String attribute) {
        List<String> selectedAttributes = this.getSelectEntries();
        if (selectedAttributes == null || this.selectAll) {
            return true;
        }
        return selectedAttributes.contains(attribute);
    }

    public int isSelected(final String... attributes) {
        int matches = 0;
        for (String attribute : attributes) {
            if (this.isSelected(attribute)) {
                matches++;
            }
        }
        return matches;
    }

    public QueryParams.Builder buildQueryParams() {
        return QueryParams.builder().select(this.select);
    }

    public QueryParams getQueryParams() {
        return this.buildQueryParams().build();
    }

}
