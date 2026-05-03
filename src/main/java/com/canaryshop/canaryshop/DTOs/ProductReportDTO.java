package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record ProductReportDTO(long id,String name, String description, List<String> reports) {

}
