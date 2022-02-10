package com.example.iSee.Database;

import android.provider.BaseColumns;

public final class UserContrat {
    private UserContrat() {
    }

    ;

    public static class UserTable implements BaseColumns {

        public static final String Table_Name = "User";
        public static final String column_name_fullname = "fullname";
        public static final String column_name_email = "email";
        public static final String column_name_password = "password";
        public static final String column_name_langage = "langage";
        public static final String column_name_vision = "vision";




    }
}