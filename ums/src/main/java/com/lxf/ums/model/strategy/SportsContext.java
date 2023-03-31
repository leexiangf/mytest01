package com.lxf.ums.model.strategy;

/**
 * @Classname SportsContext
 * @Description
 * @Date 2022/8/10 16:04
 * @Author lxf
 */
public class SportsContext {

    public static Sports sports;
    private Sports sport;

    public SportsContext(Sports sports){
        SportsContext.sports = sports;
        this.sport=sports;
    }
    public SportsContext(){
    }

    public static void setSports(Sports sports){
        SportsContext.sports = sports;
    }

    public void setSport(Sports sport) {
        this.sport = sport;
    }

    public void sportTime(){
        this.sport.sportTime();
    }
}
