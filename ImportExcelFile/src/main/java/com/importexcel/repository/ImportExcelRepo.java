package com.importexcel.repository;

import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Repository
public class ImportExcelRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;

    public ImportExcelRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getEmployeeData() {
        String sql = """
            SELECT e.emp_id AS employee_id, e.emp_first_name AS employee_name, e.username,
                   etd.managerId, etd.managerName AS manager_name, mep.project_id, p.project_name,
                   mde.designation_id, d.designation_name, mtp.type_id, t.type_name,
                   GROUP_CONCAT(c.client_brand_name) AS client_nameList
            FROM employee e
            LEFT JOIN employee_transfer_details etd ON e.emp_id = etd.employeeId
            LEFT JOIN mapping_employee_project mep ON e.emp_id = mep.emp_id
            LEFT JOIN project p ON mep.project_id = p.project_id
            LEFT JOIN mapping_designation_employee mde ON e.emp_id = mde.employee_id
            LEFT JOIN designation d ON mde.designation_id = d.designation_id
            LEFT JOIN mapping_type_project mtp ON p.project_id = mtp.project_id
            LEFT JOIN type t ON mtp.type_id = t.type_id
            LEFT JOIN mapping_client_project mcp ON p.project_id = mcp.project_id
            LEFT JOIN client c ON mcp.client_id = c.client_id
            GROUP BY e.emp_id, e.emp_first_name, e.username, etd.managerId, etd.managerName,
                     mep.project_id, p.project_name, mde.designation_id, d.designation_name,
                     mtp.type_id, t.type_name
        """;

        return jdbcTemplate.queryForList(sql, Map.of());  // No parameters needed
    }

    public List<Map<String, Object>> getClientData() {
        String sql = "SELECT client_id, client_brand_name FROM client";
        return jdbcTemplate.queryForList(sql, Map.of());  // No parameters needed
    }
}
