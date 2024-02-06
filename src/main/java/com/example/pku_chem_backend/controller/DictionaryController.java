package com.example.pku_chem_backend.controller;

import com.example.pku_chem_backend.mapper.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin(origins = "*")
public class DictionaryController {
    @Autowired
    private DictionaryMapper dictionaryMapper;


}
