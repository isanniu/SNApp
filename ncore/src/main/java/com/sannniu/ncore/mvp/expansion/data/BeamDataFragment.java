package com.sannniu.ncore.mvp.expansion.data;


import com.sannniu.ncore.mvp.bijection.BeamFragment;
import com.sannniu.ncore.mvp.bijection.RequiresPresenter;

/**
 * Created by Mr.Jude on 2015/8/20.
 */
@RequiresPresenter(BeamDataActivityPresenter.class)
public class BeamDataFragment<T extends BeamDataFragmentPresenter,M> extends BeamFragment<T> {

    public void setData(M data){}
    public void setError(Throwable e){}

}
