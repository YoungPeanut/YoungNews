package info.ipeanut.youngnews.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Chenshao_Young on 15/9/4.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity{

    //give me
    protected abstract int getContentViewLayoutID();
    protected abstract void initViewsAndEvents();



    //enhance
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContentViewLayoutID() != 0){
            setContentView(getContentViewLayoutID());
        } else {
            throw new IllegalArgumentException("you must return a layout id by method getContentViewLayoutID()");
        }



        initViewsAndEvents();

    }


    //function
    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }


}
