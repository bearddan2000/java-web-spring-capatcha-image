package example;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
public class DefaultRestController {

  private void drawCenteredString(String s, int w, int h, Graphics g) {
    FontMetrics fm = g.getFontMetrics();
    int x = (w - fm.stringWidth(s)) / 2;
    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
    g.drawString(s, x, y);
  }
  private BufferedImage generate(String capatcha) throws Exception{

    capatcha = (capatcha == null || capatcha == "") ? "empty" : capatcha;

    BufferedImage bi = new BufferedImage(100, 100,  BufferedImage.TYPE_3BYTE_BGR );
    Graphics2D g=(Graphics2D)bi.getGraphics();

    final Font f = new Font("SansSerif", Font.BOLD, 18);

    g.setColor(Color.white);
    g.fillRect(0, 0, 100, 100);
    g.setColor(Color.black);
    g.setFont(f);
    drawCenteredString(capatcha, 100, 100, g);
    g.drawRect(0, 0, 99, 99);
    g.dispose();

    return bi;
  }
  private String generateStr()
  {

      // chose a Character random from this String
      String AlphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

      // create StringBuffer size of AlphaNumericString
      StringBuilder sb = new StringBuilder(5);

      for (int i = 0; i < 5; i++) {

          // generate a random number between
          // 0 to AlphaNumericString variable length
          int index
              = (int)(AlphaString.length()
                      * Math.random());

          // add Character one by one in end of sb
          sb.append(AlphaString
                        .charAt(index));
      }

      return sb.toString();
  }
  @GetMapping(value = "/image/{capatcha}")
  public ResponseEntity getCapatcha(@PathVariable String capatcha){
    try {

  		BufferedImage img= generate(capatcha);
  		ByteArrayOutputStream baos=new ByteArrayOutputStream();
  		ImageIO.write(img, "jpg", baos );
  		byte[] imageInByte=baos.toByteArray();

  		final HttpHeaders headers = new HttpHeaders();
  		headers.setContentType(new MediaType("image","jpeg"));
  		return new ResponseEntity(imageInByte, headers, HttpStatus.CREATED);
  	} catch (Exception e) {
  		e.printStackTrace();
  	}
  	return null;
  }

  @GetMapping("/capatcha")
  @ResponseBody
  public String getCapatcha() {
      return generateStr();
  }

  @PostMapping("/")
  @ResponseBody
  public String postCapatcha(@RequestParam String guess, @RequestParam String provided) {
      boolean res = guess.equals(provided);
      System.out.println("Guess: " + guess);
      System.out.println("Provided: " + provided);
      return String.format("Match: %s", res ? "true" : "false");
  }
}
