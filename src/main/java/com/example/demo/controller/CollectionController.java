package com.example.demo.controller;

import com.example.demo.domain.NormalCollection;
import com.example.demo.domain.TableCollection;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.util.GetUser.getPrincipal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

@RestController
@PreAuthorize("hasRole('USER')")
public class CollectionController {

    @Resource
    private UserService userService;


}
