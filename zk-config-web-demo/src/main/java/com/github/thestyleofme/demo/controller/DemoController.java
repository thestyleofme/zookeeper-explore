package com.github.thestyleofme.demo.controller;

import com.github.thestyleofme.demo.utils.DatasourceProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/26 15:34
 * @since 1.0.0
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/db")
    public String getCurrentDatabase() {
        return DatasourceProvider.getHikariDataSource().getSchema();
    }
}
