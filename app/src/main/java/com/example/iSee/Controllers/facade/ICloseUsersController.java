package com.example.iSee.Controllers.facade;

import java.util.List;

public interface ICloseUsersController {
    void onGetCloseUsers(double latitude,double longitude);
    void onGetCloseUsersByLang(List<String> languages);
}
