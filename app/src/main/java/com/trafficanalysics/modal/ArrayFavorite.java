package com.trafficanalysics.modal;

import java.util.ArrayList;

public class ArrayFavorite {

    private ArrayList<Favorite> arrFavorites = new ArrayList<>();

    public ArrayList<Favorite> getArrFavorites ()
    {
        return arrFavorites;
    }

    public void setTheloai (ArrayList<Favorite> arrFavorites)
    {
        this.arrFavorites = arrFavorites;
    }
}
