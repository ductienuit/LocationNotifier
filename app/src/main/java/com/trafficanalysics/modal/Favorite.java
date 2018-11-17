package com.trafficanalysics.modal;

public class Favorite
{
    public Favorite(String title, String address) {
        this.title = title;
        this.address = address;
    }

    private String title;

    private String address;

    private String lng;

    private String lat;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String lng)
    {
        this.lng = lng;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", address = "+address+", lng = "+lng+", lat = "+lat+"]";
    }
}
