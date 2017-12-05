package com.example.konrad.metroperyskop.model;

import java.util.List;

public class Station
{
    public List<Exit> exits;

    public Exit findPoint(String pointId)
    {
        for(Exit e : exits){
            if(e.pointId == Long.parseLong(pointId))
                return e;
        }
        return null;
    }
}
