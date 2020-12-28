package com.example.clemsx.todolist;

import android.test.AndroidTestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Clemsx on 30/01/2018.
 */
public class MainActivityTest extends AndroidTestCase {

    public void checkDate() throws Exception {
        MainActivity classtest = new MainActivity();

        boolean res;
        String date = "dflsdfsf";
        String date1 = "10/10/1000";
        String date2 = "10/sdfdfdfdf";
        String date3 = "qsfsdm333";

        res = classtest.checkDate(date);
        assertEquals(false, res);
        res = classtest.checkDate(date1);
        assertEquals(true, res);
        res = classtest.checkDate(date2);
        assertEquals(false, res);
        res = classtest.checkDate(date3);
        assertEquals(false, res);
    }

}