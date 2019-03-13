package com.example.vaibh.mosaic;

import java.io.Serializable;

public class BussinessOpportunity implements Serializable {

    private int businessOpportunityId;
    private int restaurantId;
    private String businessRequirement;
    private String description;

    public int getBusinessOpportunityId() {
        return businessOpportunityId;
    }

    public void setBusinessOpportunityId(int businessOpportunityId) {
        this.businessOpportunityId = businessOpportunityId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getBusinessRequirement() {
        return businessRequirement;
    }

    public void setBusinessRequirement(String businessRequirement) {
        this.businessRequirement = businessRequirement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
