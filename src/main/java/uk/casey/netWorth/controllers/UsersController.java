package uk.casey.netWorth.controllers;

import java.sql.SQLException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.casey.netWorth.models.LoginRequestModel;
import uk.casey.netWorth.models.RegistrationRequestModel;
import uk.casey.netWorth.services.UsersService;

@RestController
public class UsersController {

  private UsersService usersService;

  @Autowired
  public UsersController(UsersService usersService) {
    this.usersService = usersService;
  }

  @PostMapping(path = "/api/user/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> registerUser(@RequestBody RegistrationRequestModel rq)
      throws SQLException {

    String userId = usersService.registerUser(rq.getUsername(), rq.getPassword(), rq.getEmail());

    return ResponseEntity.ok().body(userId);
  }

  @PostMapping(path = "/api/user/authorise", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> authorise(
      @RequestHeader("userId") UUID userId, @RequestBody LoginRequestModel rq) throws SQLException {

    String token = usersService.authoriseUser(userId, rq.getUsername(), rq.getPassword());
    System.out.println(token);

    return ResponseEntity.ok().body(token);
  }
}
