package ca.sheridancollege.hoodsi.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckUser {
    private String email;
    private String password;
    private int roleId;
}
