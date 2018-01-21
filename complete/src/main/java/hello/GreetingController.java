package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GreetingController {

	@Autowired
	private SimpMessagingTemplate template;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(String message) throws Exception {
		greetings(message);
		Thread.sleep(1000);
		return new Greeting("Hello2, " + message + "!");
	}

	public void greetings(String message)  {
		this.template.convertAndSend("/topic/greetings", message);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/send")
	public ResponseEntity<?> send() throws Exception {
		greeting("aaa");
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		this.template.convertAndSend("/topic/greetings", "{\"name\":\"aa\"}");
	}

}
