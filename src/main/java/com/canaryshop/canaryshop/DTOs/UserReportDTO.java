package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record UserReportDTO(long id, String username, String email, List <String> reports) {

}
