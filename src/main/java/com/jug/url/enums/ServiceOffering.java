package com.jug.url.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ServiceOffering {
    URL_SHORTNER_SERVICE("Url Shortner service",true),
    MONEY_TRANSFER_SERVICE("Money transfer service",true),
    AIRTIME_DATA_SERVICE("Airtime and data service",false);
    @Getter
    private String description;
    @Getter
    private boolean implemented;
    ServiceOffering(String description,boolean implemented){
        this.description = description;
        this.implemented = implemented;
    }

    public static Set<ServiceOffering> listImplementedServices(){
        return Arrays.stream(ServiceOffering.values()).filter(item->item.implemented).collect(Collectors.toSet());
    }
}
