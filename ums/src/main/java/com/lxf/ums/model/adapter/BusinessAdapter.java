package com.lxf.ums.model.adapter;

/**
 * @Classname BusinessAdapter
 * @Description
 * @Date 2022/8/10 17:23
 * @Author lxf
 */
public class BusinessAdapter extends OldBusiness implements NewBusiness{
    @Override
    public void newSend() {
        super.sendMsg();
        System.out.println("新的业务02");
    }
}
