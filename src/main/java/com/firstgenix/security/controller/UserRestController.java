package com.firstgenix.security.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.firstgenix.security.JwtTokenUtil;
import com.firstgenix.security.JwtUser;
import com.firstgenix.security.json.ExternalUser;
import com.firstgenix.security.model.User;
import com.firstgenix.security.service.JwtUserDetailsService;

@RestController
@CrossOrigin(origins = { "http://localhost:4200"}, maxAge = 8000)
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request){
      JwtUser jwtuser=(JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String token = request.getHeader(tokenHeader).substring(7);
      String username = jwtTokenUtil.getUsernameFromToken(token);
      JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
      
      return user;
    }
 
   @RequestMapping(value = "${jwt.route.registration.path}", method = RequestMethod.POST)
   public ResponseEntity<User> registration(@RequestBody ExternalUser externalUser){
	   System.out.println(externalUser.getUsername());
   User userInstance=((JwtUserDetailsService)userDetailsService).saveUser(externalUser);
   return new ResponseEntity<User>(userInstance,HttpStatus.OK);
    }
   
   @RequestMapping(value = "${jwt.route.userupdate.path}", method = RequestMethod.PUT)
   public ResponseEntity<JwtUser> EditUser(@PathVariable("id")String id, @RequestBody User user)  {
   JwtUser user1 = (JwtUser)((JwtUserDetailsService) userDetailsService).editUser(user,id);
   return new ResponseEntity<JwtUser>(user1,HttpStatus.OK);
}

   @RequestMapping(value = "${jwt.route.userdelete.path}", method = RequestMethod.DELETE)
   public ResponseEntity<?> DeleteUser(@PathVariable("id")String id)  {
   ((JwtUserDetailsService)userDetailsService).deleteUser(id);
   return new ResponseEntity<>(HttpStatus.OK);
}
   
   @RequestMapping(value = "${jwt.route.getuser.path}", method = RequestMethod.GET)
   public ResponseEntity<JwtUser> getUser(@PathVariable("id")String id) {
   JwtUser user=(JwtUser)((JwtUserDetailsService)userDetailsService).getUser(id);
   return new ResponseEntity<JwtUser>(user,HttpStatus.OK);
}
   
   
   @RequestMapping(value = "${jwt.route.getorganisation.path}", method = RequestMethod.GET)
   public ResponseEntity<JwtUser> getOrganisation(@PathVariable("id")String id) {
   JwtUser user=(JwtUser)((JwtUserDetailsService)userDetailsService).getOrganisation(id);
   return new ResponseEntity<JwtUser>(user,HttpStatus.OK);
}
   

   @RequestMapping(value = "${jwt.route.checkAuthority.path}",method = RequestMethod.GET)
   public boolean checkAuthority(HttpServletRequest request,@PathVariable("aid")String id){
   String token = request.getHeader(tokenHeader).substring(7);
   String username = jwtTokenUtil.getUsernameFromToken(token);
   JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
   String authorityname=((JwtUserDetailsService)userDetailsService).findAuthorityName(id);
   if(((JwtUserDetailsService) userDetailsService).userHasAuthority(authorityname,user)){
         return true;
	 }else{
		 return false; 
	 }
}
   
   @RequestMapping(value = "${jwt.route.setenabled.path}", method = RequestMethod.PUT)
   public ResponseEntity<JwtUser> setEnabled(@PathVariable("id")String id, @RequestBody User user)  {
   JwtUser user1 = (JwtUser)((JwtUserDetailsService) userDetailsService).setEnabled(user, id);
   return new ResponseEntity<JwtUser>(user1,HttpStatus.OK);
}

}
