package com.adminnick.SpringBootProject.Util.Email;

import lombok.Data;

@Data
public class emailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
}
