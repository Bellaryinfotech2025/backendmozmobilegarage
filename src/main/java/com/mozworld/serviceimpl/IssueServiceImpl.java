package com.mozworld.serviceimpl;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mozworld.entity.IssueEntity;
import com.mozworld.repo.IssueRepository;
import com.mozworld.service.IssueService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappToken;

    @Value("${whatsapp.from.phone}")
    private String fromPhone;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public IssueEntity createIssue(IssueEntity issue) {
        // Generate tracking ID
        String trackingId = generateTrackingId();
        issue.setTrackingId(trackingId);
        issue.setCreatedDate(LocalDateTime.now());

        // Save to DB
        IssueEntity savedIssue = issueRepository.save(issue);

        // Send Email
        sendEmail(savedIssue);

        // Send WhatsApp
        sendWhatsApp(savedIssue);

        return savedIssue;
    }

    private String generateTrackingId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder("M-V-G-");
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            id.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return id.toString();
    }

    private void sendEmail(IssueEntity issue) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(issue.getEmail());
            helper.setSubject("✅ Repair Request Confirmed – Tracking ID: " + issue.getTrackingId());

            String customIssue = (issue.getCustomIssue() != null && !issue.getCustomIssue().trim().isEmpty())
                    ? issue.getCustomIssue().trim()
                    : "Not specified";

            String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Repair Confirmation</title>
                </head>
                <body style="margin:0; padding:0; background:#f5f7fa; font-family:'Segoe UI', Arial, sans-serif;">
                    <table align="center" width="100%" cellpadding="0" cellspacing="0" style="max-width:640px; margin:20px auto; background:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.08);">
                        <tr>
                            <td align="center" style="background:linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%); padding:40px 20px;">
                                <h1 style="color:#ffffff; font-size:32px; margin:0; font-weight:600; letter-spacing:1px;">
                                    Moz Mobile Garage
                                </h1>
                                <p style="color:#e0e7ff; font-size:17px; margin:12px 0 0; font-weight:300;">
                                    Quality Service • Expert Technicians
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:40px 30px 20px; text-align:center;">
                                <div style="display:inline-block; width:90px; height:90px; background:#10b981; border-radius:50%; line-height:90px; box-shadow:0 8px 20px rgba(16,185,129,0.3);">
                                    <span style="font-size:48px; color:#ffffff;">✓</span>
                                </div>
                                <h2 style="font-size:28px; color:#111827; margin:24px 0 12px; font-weight:600;">
                                    Your Repair Request is Confirmed!
                                </h2>
                                <p style="font-size:17px; color:#4b5563; margin:0; line-height:1.6;">
                                    Dear <strong>NAME_PLACEHOLDER</strong>, thank you for trusting us with your device.
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:0 30px 40px;">
                                <div style="background:#f8fafc; border-radius:12px; padding:30px; border:1px solid #e2e8f0;">
                                    <p style="font-size:18px; color:#1f2937; margin:0 0 24px; font-weight:500;">
                                        Service Details
                                    </p>
                                    <table width="100%" cellpadding="14" cellspacing="0" style="font-size:16px;">
                                        <tr>
                                            <td style="color:#374151; font-weight:600; width:40%;">Customer Name</td>
                                            <td style="color:#111827;">NAME_PLACEHOLDER</td>
                                        </tr>
                                        <tr style="background:#f1f5f9;">
                                            <td style="color:#374151; font-weight:600;">Email</td>
                                            <td style="color:#111827;">EMAIL_PLACEHOLDER</td>
                                        </tr>
                                        <tr>
                                            <td style="color:#374151; font-weight:600;">Phone Number</td>
                                            <td style="color:#111827;">PHONE_PLACEHOLDER</td>
                                        </tr>
                                        <tr style="background:#f1f5f9;">
                                            <td style="color:#374151; font-weight:600;">Pickup Address</td>
                                            <td style="color:#111827;">ADDRESS_PLACEHOLDER</td>
                                        </tr>
                                        <tr>
                                            <td style="color:#374151; font-weight:600;">Device Brand</td>
                                            <td style="color:#111827;">BRAND_PLACEHOLDER</td>
                                        </tr>
                                        <tr style="background:#f1f5f9;">
                                            <td style="color:#374151; font-weight:600;">Device Model</td>
                                            <td style="color:#111827;">MODEL_PLACEHOLDER</td>
                                        </tr>
                                        <tr>
                                            <td style="color:#374151; font-weight:600;">Issue Reported</td>
                                            <td style="color:#111827;">ISSUE_PLACEHOLDER</td>
                                        </tr>
                                        <tr style="background:#f1f5f9;">
                                            <td style="color:#374151; font-weight:600;">Additional Notes</td>
                                            <td style="color:#111827;">CUSTOM_PLACEHOLDER</td>
                                        </tr>
                                        <tr style="background:#dcfce7;">
                                            <td style="color:#166534; font-weight:700; font-size:18px;">Tracking ID</td>
                                            <td style="color:#166534; font-weight:700; font-size:18px;">TRACKING_PLACEHOLDER</td>
                                        </tr>
                                        <tr>
                                            <td style="color:#374151; font-weight:600;">Submitted On</td>
                                            <td style="color:#111827;">DATE_PLACEHOLDER</td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="padding:0 30px 40px; text-align:center;">
                                <p style="font-size:16px; color:#4b5563; line-height:1.7; margin:0 0 20px;">
                                    Our expert technician will contact you within the next <strong>2 hours</strong> to confirm pickup time and provide an estimated quote.
                                </p>
                                <p style="font-size:15px; color:#6b7280; margin:0;">
                                    Need immediate assistance? Reply to this email or call us at <strong>+91-XXXXX-XXXXX</strong>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" style="background:#1e293b; padding:30px; color:#94a3b8; font-size:14px;">
                                <p style="margin:0 0 10px;">
                                    <strong style="color:#ffffff; font-size:16px;">Mobile Repair Pro</strong>
                                </p>
                                <p style="margin:0; line-height:1.6;">
                                    Premium Mobile Repair Service • Bellary, Karnataka<br>
                                    © 2026 All Rights Reserved • Trusted by Thousands
                                </p>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """;

            // Safely replace placeholders
            String htmlContent = htmlTemplate
                    .replace("NAME_PLACEHOLDER", escapeHtml(issue.getName()))
                    .replace("EMAIL_PLACEHOLDER", escapeHtml(issue.getEmail()))
                    .replace("PHONE_PLACEHOLDER", escapeHtml(issue.getPhone()))
                    .replace("ADDRESS_PLACEHOLDER", escapeHtml(issue.getAddress()))
                    .replace("BRAND_PLACEHOLDER", escapeHtml(issue.getMobileBrand()))
                    .replace("MODEL_PLACEHOLDER", escapeHtml(issue.getMobileModel()))
                    .replace("ISSUE_PLACEHOLDER", escapeHtml(issue.getIssueType()))
                    .replace("CUSTOM_PLACEHOLDER", escapeHtml(customIssue))
                    .replace("TRACKING_PLACEHOLDER", escapeHtml(issue.getTrackingId()))
                    .replace("DATE_PLACEHOLDER", issue.getCreatedDate().toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Helper method to prevent XSS or broken HTML if user input has < > & etc.
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    private void sendWhatsApp(IssueEntity issue) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + whatsappToken);

        String messageText = """
            Issue Registered Successfully!
            Name: %s
            Email: %s
            Phone: %s
            Address: %s
            Mobile Brand: %s
            Mobile Model: %s
            Issue Type: %s
            Custom Issue: %s
            Tracking ID: %s
            Created Date: %s
            Our team will contact you soon.
            """.formatted(
            issue.getName(), issue.getEmail(), issue.getPhone(), issue.getAddress(),
            issue.getMobileBrand(), issue.getMobileModel(), issue.getIssueType(),
            issue.getCustomIssue() != null ? issue.getCustomIssue() : "N/A",
            issue.getTrackingId(), issue.getCreatedDate()
        );

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("messaging_product", "whatsapp");
        body.add("to", issue.getPhone());  // Assume international format, e.g., 91xxxxxxxxxx for India
        body.add("type", "text");
        body.add("text", messageText);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(whatsappApiUrl, request, String.class);
        } catch (Exception e) {
            // Handle exception (log or throw)
        }
    }
    
    
    
    @Override
    public IssueEntity getIssueByTrackingId(String trackingId) {
        return issueRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));   
    }
}