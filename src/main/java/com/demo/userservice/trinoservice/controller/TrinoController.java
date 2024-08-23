package com.demo.userservice.trinoservice.controller;

import com.demo.userservice.trinoservice.service.TrinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trino/api/")
public class TrinoController {

    @Autowired
    private TrinoService trinoService;

    @GetMapping("/tables")
    public List<String> getTablesInSchema(@RequestParam String schemaName) {
        return trinoService.getTablesInSchema(schemaName);
    }

    @GetMapping("/table-data")
    public List<Map<String, Object>> getTableData(@RequestParam String schemaName, @RequestParam String tableName) {
        return trinoService.getTableData(schemaName, tableName);
    }

}
