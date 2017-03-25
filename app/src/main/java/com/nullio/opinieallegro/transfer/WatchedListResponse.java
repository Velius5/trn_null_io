package com.nullio.opinieallegro.transfer;

import java.util.List;

public class WatchedListResponse {

    private int count;

    private List<OfferResponse> offers;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<OfferResponse> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferResponse> offers) {
        this.offers = offers;
    }
}
