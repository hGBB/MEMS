package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.HamerMailSenderService;
import de.unipa.hams.hamer.domain.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("Duplicates")
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HamerMailSenderServiceServiceImpl implements HamerMailSenderService {

  private final SpringTemplateEngine templateEngine;
  private final JavaMailSender javaMailSender;
  @Value("${mems.contextPath}")
  private String contextPath;
  @Value("${mems.port}")
  private Integer port;

  public void sendPasswordLinkEmail(@NotNull User user, @NotNull String password, @NotNull String uuid) throws MessagingException {
    buildPasswordMailContent(user, uuid, password, "CreateUser");
  }

  private void buildPasswordMailContent(@NotNull User user, @NotNull String uuid, String password, @NotNull String template) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(user.getEmail());
    helper.setSubject("Your new Hams Account");
    helper.setFrom("ep@weilbaum.tk");
    String link = (contextPath + ":" + port + "/resetpwd/new/?token=" + uuid);
    helper.setText(buildPasswordMailContent(user.getName(), user.getLogin(), password, template), true);
    javaMailSender.send(message);
  }

  public void sendResetPasswordLinkEmail(@NotNull User user, @NotNull String uuid) throws MessagingException {
    buildPasswordMailContent(user, uuid, uuid, "ResetPassword");
  }

  @Override
  public void sendNotificationEmail(@NotNull Notification notification) throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(notification.getRecipient().getEmail());
    helper.setFrom("ep@weilbaum.tk");
    helper.setPriority(1);
    if (notification.getTriggeredBy() != null) {
      helper.setSubject("HamerMEMS Alert!");
      helper.setText(buildLightAlertMailContent(notification), true);
    } else {
      helper.setSubject("HamerMEMS Node lost Contact");
      helper.setText(buildLostNodeContactMailContent(notification), true);
    }
    javaMailSender.send(message);
  }

  @Override
  public void sendAlertEmail(@NotNull RuleDesignation designation, @NotNull String value) throws MessagingException {
    AlertRule ar = designation.getRule();
    User u = ar.getConfiguredBy();
    Node n = designation.getNode();
    Kpi k = designation.getKpi();
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(u.getEmail());
    helper.setSubject("Kpi " + k.getName() + " for Node " + n.getName() + " exceeded Threshold");
    helper.setPriority(1);
    helper.setFrom("ep@weilbaum.tk");
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    helper.setText(buildAlertMailContent(ar.getName(), n.getName(), formatter.format(date), ar.getConfiguredBy().getName(), k.getName(), k.getUnit().toString(), String.valueOf(designation.getThreshold()), value));

    Notification notification = new Notification();
    notification.setTriggeredBy(ar);
    notification.setRecipient(u);
    notification.setText("Triggered Kpi: " + k.getName() + "\n" +
      "Current value: " + value + "(Threshold: " + designation.getThreshold() + " in " + k.getUnit() +
      "Affected node: " + n.getName());
  }

  @NotNull
  private String buildPasswordMailContent(@NotNull String name, @NotNull String username, @NotNull String password, @NotNull String template) {
    Context context = new Context();
    context.setVariable("name", name);
    context.setVariable("username", username);
    context.setVariable("password", password);
    return templateEngine.process(template, context);
  }

  @NotNull
  private String buildAlertMailContent(String alertName, String node, String time, String name, String kpiName, String kpiValue, String alertThreshold, String actualValue) {
    Context context = new Context();
    context.setVariable("node", node);
    context.setVariable("time", time);
    context.setVariable("name", name);
    context.setVariable("alertName", alertName);
    context.setVariable("kpiName", kpiName);
    context.setVariable("kpiValue", kpiValue);
    context.setVariable("alertThreshold", alertThreshold);
    context.setVariable("actualValue", actualValue);
    return templateEngine.process("Alert", context);
  }

  @NotNull
  private String buildLightAlertMailContent(@NotNull Notification notification) {
    Context context = new Context();
    context.setVariable("name", notification.getRecipient().getName());
    context.setVariable("message", notification.getText());
    context.setVariable("time", notification.getTimestamp());
    context.setVariable("alertName", notification.getTriggeredBy().getName());
    return templateEngine.process("LightAlert", context);
  }

  @NotNull
  private String buildLostNodeContactMailContent(@NotNull Notification notification) {
    Context context = new Context();
    context.setVariable("name", notification.getRecipient().getName());
    context.setVariable("message", notification.getText());
    context.setVariable("time", notification.getTimestamp());
    return templateEngine.process("LostNodeContact", context);
  }
}
